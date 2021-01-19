package Moonworks.cards;

import Moonworks.actions.AmbushAction;
import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.patches.PiercingPatches;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class Ambush extends AbstractNormaAttentiveCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(Ambush.class.getSimpleName());
    public static final String IMG = makeCardPath("Ambush.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int VULNERABLE = 1;
    private static final int UPGRADE_PLUS_VULNERABLE = 1;

    private static final Integer[] NORMA_LEVELS = {3};

    // /STAT DECLARATION/


    public Ambush() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, NORMA_LEVELS);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = VULNERABLE;
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.INFOMOD, 1, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //Check if we pass our Norma requirement
        boolean normaCheck = getNormaLevel() >= NORMA_LEVELS[0];

        //Our Pierce will hit for as much block as the main attack will actually remove
        int blockDelta = Math.min(m.currentBlock, damage);

        //Apply Vulnerable. I should probably use an action for recalculating damage on the vulnerable target...
        //BUT this works well enough for now as it shows on card hover
        this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false)));

        //Deal the damage, it was already boosted for Vulnerable if it will be applied
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT,true));

        //Only do pierce if we pass our norma and actually have any to do
        if (normaCheck && blockDelta > 0) {
            DamageInfo pierceDamage = new DamageInfo(p, blockDelta, DamageInfo.DamageType.HP_LOSS);
            PiercingPatches.PiercingField.piercing.set(pierceDamage, true);
            this.addToBot(new DamageAction(m, pierceDamage, AbstractGameAction.AttackEffect.NONE, true));
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        super.calculateCardDamage(m);
        if(m != null && !m.hasPower("Vulnerable") && !m.hasPower("Artifact")){
            this.damage *= AbstractDungeon.player.hasRelic("Paper Frog") ? 1.75F : 1.5F;
        }
        this.isDamageModified = this.damage != this.baseDamage;
        initializeDescription();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_VULNERABLE);
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}