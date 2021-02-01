package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.SteadyPower;
import basemod.ReflectionHacks;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class RainbowColoredCircle extends AbstractNormaAttentiveCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(RainbowColoredCircle.class.getSimpleName());
    public static final String IMG = makeCardPath("RainbowColoredCircle.png");

    //private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //public static final String SPENT_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    private static final int BLOCK_REMOVAL = 6;

    private static final int ENSURED_CONVERSION = 5;

    private static final Integer[] NORMA_LEVELS = {2};
    private ArrayList<TooltipInfo> OptimizationTooltip;

    // /STAT DECLARATION/


    public RainbowColoredCircle() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, NORMA_LEVELS);
        //this.block = this.baseBlock = BLOCK;
        this.magicNumber = this.baseMagicNumber = BLOCK_REMOVAL;
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.MAGICMOD, 4, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //Lose the appropriate amount of Block
        this.addToBot(new LoseBlockAction(p, p, magicNumber));

        //Gain that much Steady
        this.addToBot(new ApplyPowerAction(p, p, new SteadyPower(p, magicNumber)));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        //If we pass the Norma Check...
        if (getNormaLevel() >= NORMA_LEVELS[0]) {
            int totalDmg = 0, dmg;
            //Determine how much damage we should be taking
            for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
                if (!aM.isDeadOrEscaped() && aM.getIntentBaseDmg() >= 0) {
                    dmg = (int) ReflectionHacks.getPrivate(aM, AbstractMonster.class, "intentDmg");
                    if ((boolean)ReflectionHacks.getPrivate(aM, AbstractMonster.class, "isMultiDmg"))
                    {
                        dmg *= (int)ReflectionHacks.getPrivate(aM, AbstractMonster.class, "intentMultiAmt");
                    }
                    totalDmg += Math.max(0, dmg);
                }
            }
            //Figure out how much block we have left over after taking 5 away, compare it to the damage we expect to take
            int deltaBlock = Math.max(0, AbstractDungeon.player.currentBlock - totalDmg - ENSURED_CONVERSION);
            //Determine our amount we will actually remove. Will always take at least the ensured amount, assuming we actually HAVE block that is
            magicNumber = Math.min(Math.min(baseMagicNumber, ENSURED_CONVERSION + deltaBlock), AbstractDungeon.player.currentBlock);
        } else {
            //Our amount to convert is either the full amount or whatever we actually have to convert, much easier to compute
            magicNumber = Math.min(baseMagicNumber, AbstractDungeon.player.currentBlock);
        }
    }

    @Override
    public float getTitleFontSize() {
        return 18F;
    }

    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        if (OptimizationTooltip == null)
        {
            OptimizationTooltip = new ArrayList<>();
            OptimizationTooltip.add(new TooltipInfo(EXTENDED_DESCRIPTION[1], EXTENDED_DESCRIPTION[2]));
        }
        List<TooltipInfo> compoundList = new ArrayList<>(OptimizationTooltip);
        if (super.getCustomTooltipsTop() != null) compoundList.addAll(super.getCustomTooltipsTop());
        return compoundList;
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeBaseCost(UPGRADE_COST);
            initializeDescription();
        }
    }
}
