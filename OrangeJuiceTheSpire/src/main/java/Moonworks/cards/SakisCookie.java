package Moonworks.cards;

import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.cards.giftCards.SweetBattle;
import Moonworks.powers.SteadyPower;
import Moonworks.powers.TemporaryDexterityPower;
import Moonworks.powers.TemporaryStrengthPower;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import java.util.ArrayList;
import java.util.List;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class SakisCookie extends AbstractNormaAttentiveCard {

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(SakisCookie.class.getSimpleName());
    public static final String IMG = makeCardPath("SakisCookie.png");

    private static ArrayList<TooltipInfo> OptimizationTooltip;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 0;

    private static final int HEAL = 2;
    private static final int UPGRADE_PLUS_HEAL = 1;

    private static final int STRDEX = 1;
    private static final int UPGRADE_PLUS_STRDEX = 1;

    private static final int VIGORSTEADY = 3;
    private static final int UPGRADE_PLUS_VIGORSTEADY = 2;

    private static final Integer[] NORMA_LEVELS = {2};

    // /STAT DECLARATION/

    public SakisCookie() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, NORMA_LEVELS);
        magicNumber = baseMagicNumber = HEAL;
        thirdMagicNumber = baseThirdMagicNumber = VIGORSTEADY;
        exhaust = true;
        this.tags.add(CardTags.HEALING);
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.INFOMOD, 0, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
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

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = effectLogic();
        boolean sweetBattle = hasSweetBattle();
        int boostEffect = sweetBattle ? 2 : 1;
        switch (effect) { //Switches allow us to run code based on the value in the switch.
            case 1: //Big Block. We multiply by 2 or 3 here
                AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, magicNumber*boostEffect));
                break; //Break after each case since we dont want it to then look at the other cases
            case 2: //Draw 2 or 3 cards, as this is stored in magicNumber
                this.addToBot(new ApplyPowerAction(p, p, new TemporaryDexterityPower(p, secondMagicNumber*boostEffect)));
                this.addToBot(new ApplyPowerAction(p, p, new SteadyPower(p, thirdMagicNumber*boostEffect)));
                break;
            case 3: //Buff 2 or 3 stacks of temp dex
                this.addToBot(new ApplyPowerAction(p, p, new TemporaryStrengthPower(p, secondMagicNumber*boostEffect)));
                this.addToBot(new ApplyPowerAction(p, p, new VigorPower(p, thirdMagicNumber*boostEffect)));
                break;
        }
        if (sweetBattle) {
            this.purgeOnUse = true;
        }
    }
    private boolean hasSweetBattle() {
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof SweetBattle) {
                return true;
            }
        }
        return false;
    }
    private int effectLogic() {
        if (!upgraded) {
            return AbstractDungeon.cardRandomRng.random(1, 3); //Choose a random number for the effect
        } else {
            if (AbstractDungeon.player.currentHealth < AbstractDungeon.player.maxHealth) {
                return 1;
            } else if (enemyAttacking() && hasBlockingCard()){
                return 2;
            } else {
                return 3;
            }
        }
    }
    private boolean enemyAttacking() {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m.getIntentBaseDmg() >= 0) {
                return true;
            }
        }
        return false;
    }
    private boolean hasBlockingCard() {
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.baseBlock > 0) {
                return true;
            }
        }
        return false;
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_HEAL);
            this.upgradeThirdMagicNumber(UPGRADE_PLUS_VIGORSTEADY);
            //this.upgradeMagicNumber(UPGRADE_PLUS_HEAL);
            this.initializeDescription();
        }
    }
}