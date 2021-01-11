package Moonworks.cards;

import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.defect.SunderAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class AwakeningOfTalent extends AbstractNormaAttentiveCard {

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(AwakeningOfTalent.class.getSimpleName());
    public static final String IMG = makeCardPath("AwakeningOfTalent.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 2;

    private static final int DAMAGE = 7;
    private static final int UPGRADE_PLUS_DMG = 3;

    private static final int BLOCK = 7;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    private static final int CARDS = 1;
    private static final int UPGRADE_PLUS_CARDS = 1;

    private static final int ENERGY = 1;

    private static final Integer[] NORMA_LEVELS = {4};

    // /STAT DECLARATION/

    public AwakeningOfTalent() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, NORMA_LEVELS);
        damage = baseDamage = DAMAGE;
        block = baseBlock = BLOCK;
        magicNumber = baseMagicNumber = CARDS;
        secondMagicNumber = baseSecondMagicNumber = ENERGY;
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.INFOMOD, 1, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
    }
    @Override
    public float getTitleFontSize() {
        return 21F;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new SunderAction(m, new DamageInfo(p, damage, damageTypeForTurn), secondMagicNumber));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(magicNumber));
        if (getNormaLevel() >= NORMA_LEVELS[0]) {
           this.addToBot(new GainEnergyAction(secondMagicNumber));
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DMG);
            this.upgradeBlock(UPGRADE_PLUS_BLOCK);
            //this.upgradeMagicNumber(UPGRADE_PLUS_CARDS);
            this.initializeDescription();
        }
    }
}