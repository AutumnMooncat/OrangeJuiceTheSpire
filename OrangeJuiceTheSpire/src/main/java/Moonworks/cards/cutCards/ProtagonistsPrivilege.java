package Moonworks.cards.cutCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.ProtagonistsPrivilegeAction;
import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.ProtagonistsPrivilegePower;
import basemod.AutoAdd;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Moonworks.OrangeJuiceMod.makeCardPath;
@AutoAdd.Ignore
public class ProtagonistsPrivilege extends AbstractNormaAttentiveCard {

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(ProtagonistsPrivilege.class.getSimpleName());
    public static final String IMG = makeCardPath("ProtagonistsPrivilege.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 2;

    private static final int DAMAGE = 16;
    private static final int UPGRADE_PLUS_DMG = 4;

    private static final int CARDS = 2;
    private static final int UPGRADE_PLUS_CARDS = 1;

    private static final int HEAL = 3;

    private static final Integer[] NORMA_LEVELS = {2};

    // /STAT DECLARATION/

    public ProtagonistsPrivilege() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, NORMA_LEVELS);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = CARDS;
        secondMagicNumber = baseSecondMagicNumber = HEAL;
        this.tags.add(CardTags.HEALING);
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.INFOMOD, 1, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
    }
    @Override
    public float getTitleFontSize() {
        return 18F;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        //Grab our power if it exists
        ProtagonistsPrivilegePower pow = (ProtagonistsPrivilegePower) p.getPower(ProtagonistsPrivilegePower.POWER_ID);
        OrangeJuiceMod.logger.info("Playing Protog. Do we have the power: "+ (pow!=null));

        //Define the damage action, we will add a copy of it if we pass the draw check
        DamageAction dA = new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY);
        this.addToBot(dA);

        //Heal if we pass the check
        if (getNormaLevel() >= NORMA_LEVELS[0]) {
            this.addToBot(new HealAction(p, p, this.magicNumber));
        }

        //Define the update action for if we already have the power
        AbstractGameAction addNewInfo = new AbstractGameAction() {
            public void update() {
                if (pow != null) {
                    pow.addInfoPair(dA, magicNumber);
                }
                this.isDone = true;
            }};

        //Update the power if we succeed and have it. Apply the power if we succeed but don't
        if (pow != null) {
            this.addToBot(new ProtagonistsPrivilegeAction(magicNumber, addNewInfo));
        } else {
            this.addToBot(new ProtagonistsPrivilegeAction(magicNumber, new ApplyPowerAction(p, p, new ProtagonistsPrivilegePower(p, dA, magicNumber))));
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DMG);
            //this.upgradeMagicNumber(UPGRADE_PLUS_CARDS);
            this.initializeDescription();
        }
    }
}
