package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractModdedCard;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.AcceleratorPower;
import Moonworks.powers.FreeCardPower;
import Moonworks.powers.LeapThroughTimePower;
import basemod.AutoAdd;
import basemod.helpers.CardModifierManager;
import basemod.helpers.ModalChoice;
import basemod.helpers.ModalChoiceBuilder;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.MasterRealityPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class Accelerator extends AbstractNormaAttentiveCard implements ModalChoice.Callback{

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In-Progress Form At the start of your turn, play a TOUCH.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(Accelerator.class.getSimpleName());
    public static final String IMG = makeCardPath("Accelerator.png");
    public static final String IMG2 = makeCardPath("AcceleratorMix.png");

    private static ArrayList<TooltipInfo> ExceptionsTooltip;
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 2;
    private static final int UPGRADE_REDUCED_COST = 0;

    private static final int STACKS = 1;
    private static final int UPGRADE_PLUS_STACKS = 1;

    private ModalChoice modal;

    private static final Integer[] NORMA_LEVELS = {5};

    // /STAT DECLARATION/


    public Accelerator() {

        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = STACKS;
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.INFOMOD, 1, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[2]));
        //this.tags.add(BaseModCardTags.FORM); //Tag your strike, defend and form cards so that they work correctly.

    }

    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        if (ExceptionsTooltip == null)
        {
            ExceptionsTooltip = new ArrayList<>();
            ExceptionsTooltip.add(new TooltipInfo(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[1]));
        }
        List<TooltipInfo> compoundList = new ArrayList<>(ExceptionsTooltip);
        if (super.getCustomTooltipsTop() != null) compoundList.addAll(super.getCustomTooltipsTop());
        return compoundList;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //Check if there is reason to open the modal option
        boolean freePower = p.hasPower(FreeCardPower.POWER_ID);
        if (getNormaLevel() >= NORMA_LEVELS[0] && (this.freeToPlayOnce || EnergyPanel.totalCount >= this.costForTurn + 1)) {
            AbstractCard option1 = new AcceleratorOption1(name, magicNumber);
            AbstractCard option2 = new AcceleratorOption2(name, magicNumber, freeToPlayOnce || freePower);
            modal = new ModalChoiceBuilder()
                    .setCallback(this)
                    .addOption(option1)
                    .addOption(option2)
                    .create();

            modal.open();
        } else {
            this.addToBot(new ApplyPowerAction(p, p, new AcceleratorPower(p, magicNumber)));
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            //this.isInnate = true;
            //rawDescription = UPGRADE_DESCRIPTION;
            upgradeName();
            //upgradeBaseCost(UPGRADE_REDUCED_COST);
            upgradeMagicNumber(UPGRADE_PLUS_STACKS);
            initializeDescription();
        }
    }

    @Override
    public void optionSelected(AbstractPlayer p, AbstractMonster m, int i) {

    }

    @AutoAdd.Ignore
    private class AcceleratorOption1 extends AbstractModdedCard {
        public AcceleratorOption1(String name, int amount) {
            super(AcceleratorOption1.class.getSimpleName(), name, IMG, -2, EXTENDED_DESCRIPTION[3], CardType.POWER, COLOR, CardRarity.SPECIAL, CardTarget.NONE);
            this.magicNumber = this.baseMagicNumber = amount;
        }

        @Override
        public void upgrade() {}

        @Override
        public void use(AbstractPlayer p, AbstractMonster m) {
            this.addToBot(new ApplyPowerAction(p, p, new AcceleratorPower(p, magicNumber)));
        }

        @Override
        public AbstractCard makeCopy() {
            return new AcceleratorOption1(name, magicNumber);
        }
    }

    @AutoAdd.Ignore
    private class AcceleratorOption2 extends AbstractModdedCard {

        private final boolean free;
        public AcceleratorOption2(String name, int amount, boolean free) {
            super(AcceleratorOption2.class.getSimpleName(), name, IMG2, -2, EXTENDED_DESCRIPTION[4], CardType.POWER, COLOR, CardRarity.SPECIAL, CardTarget.NONE);
            this.magicNumber = this.baseMagicNumber = amount;
            this.free = free;
        }

        @Override
        public void upgrade() {}

        @Override
        public void use(AbstractPlayer p, AbstractMonster m) {
            this.addToBot(new ApplyPowerAction(p, p, new AcceleratorPower(p, magicNumber)));
            if (!free) {
                this.addToBot(new LoseEnergyAction(1));
            }
            this.addToBot(new ApplyPowerAction(p, p, new MasterRealityPower(p)));
        }

        @Override
        public AbstractCard makeCopy() {
            return new AcceleratorOption2(name, magicNumber, freeToPlayOnce);
        }
    }
}
