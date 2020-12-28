package Moonworks.cards.giftCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractGiftCard;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class Bloodlust extends AbstractGiftCard {

    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(Bloodlust.class.getSimpleName());
    public static final String IMG = makeCardPath("Bloodlust.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = -2;
    private static final int DAMAGE = 4;
    //private static final int UPGRADE_PLUS_DAMAGE = -1; //Maybe do this instead of healing more?
    private static final int HEAL = 2;
    //private static final int UPGRADE_PLUS_HEAL = 1;
    private static final int USES = 7;
    private static final int UPGRADE_PLUS_USES = 3; // Maybe this?

    //private static final int UPGRADE_PLUS_RETAINS = 1;

    // /STAT DECLARATION/


    public Bloodlust() {

        this(USES, false);

    }

    public Bloodlust(int currentUses, boolean checkedGolden) {

        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, USES, currentUses, checkedGolden);
        this.magicNumber = this.baseMagicNumber = HEAL;
        this.tags.add(CardTags.HEALING);

    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if(active) {
            AbstractPlayer p = AbstractDungeon.player;
            if(c.type == CardType.ATTACK) {
                this.addToBot(new HealAction(p, p, magicNumber));
                this.applyEffect();
            }
        }
        super.onPlayCard(c, m);
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        if(active) {
            this.applyDamageEffect();
        }
    }

    @Override
    public void atTurnStartPreDraw() {
        super.atTurnStartPreDraw();
        if(active) {
            this.applyDamageEffect();
        }
    }

    public void applyDamageEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        int lessDamage = getNormaLevel() >= 2 ? 1 : 0;
        this.addToBot(new DamageAction(p, new DamageInfo(p, DAMAGE-lessDamage, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        //Dont use a use here. This will result in us being able to hold this Gift as long as we want, but get a limited number of heals
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //upgradeMagicNumber(UPGRADE_PLUS_HEAL);
            upgradeDefaultSecondMagicNumber(UPGRADE_PLUS_USES);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Bloodlust(defaultSecondMagicNumber, checkedGolden);
    }
}
