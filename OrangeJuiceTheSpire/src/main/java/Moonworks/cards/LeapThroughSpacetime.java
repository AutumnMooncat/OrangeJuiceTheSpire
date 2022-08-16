package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.LeapThroughSpacePower;
import Moonworks.powers.LeapThroughTimePower;
import basemod.AutoAdd;
import basemod.helpers.ModalChoice;
import basemod.helpers.ModalChoiceBuilder;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class LeapThroughSpacetime extends AbstractNormaAttentiveCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In-Progress Form At the start of your turn, play a TOUCH.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(LeapThroughSpacetime.class.getSimpleName());
    public static final String LTS_ID = OrangeJuiceMod.makeID(LeapThroughSpace.class.getSimpleName());
    public static final String LTT_ID = OrangeJuiceMod.makeID(LeapThroughTime.class.getSimpleName());
    public static final String IMG = makeCardPath("LeapThroughSpacetime.png");
    public static final String LTS_IMG = makeCardPath("LeapThroughSpacePower.png");
    public static final String LTT_IMG = makeCardPath("LeapThroughSpaceMarkingPower.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;

    private static final int TEMP_COST = -2;

    // /STAT DECLARATION/

    public LeapThroughSpacetime() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new TalkAction(true, EXTENDED_DESCRIPTION[0], 0.2f, 2.0f));
        ArrayList<AbstractCard> cards = new ArrayList<>();
        cards.add(new LeapThroughSpace(EXTENDED_DESCRIPTION[1]));
        cards.add(new LeapThroughTime(EXTENDED_DESCRIPTION[2]));
        this.addToBot(new ChooseOneAction(cards));// 52
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            //rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public float getTitleFontSize() {
        return 20F;
    }

    @AutoAdd.Ignore
    public static class LeapThroughSpace extends AbstractNormaAttentiveCard {

        private final String useText;

        public LeapThroughSpace(String useText) {
            super(LTS_ID, LTS_IMG, TEMP_COST, CardType.POWER, COLOR, CardRarity.SPECIAL, CardTarget.SELF);
            this.useText = useText;
        }

        @Override
        public void upgrade() {}

        @Override
        public void use(AbstractPlayer p, AbstractMonster m) {
            this.addToBot(new TalkAction(true, useText, 0.4f, 2.0f));
            this.addToBot(new ApplyPowerAction(p, p, new LeapThroughSpacePower(p)));
        }

        @Override
        public void onChoseThisOption() {
            use(AbstractDungeon.player, null);
        }

        @Override
        public AbstractCard makeCopy() {
            return new LeapThroughSpace(useText);
        }
    }
    @AutoAdd.Ignore
    public static class LeapThroughTime extends AbstractNormaAttentiveCard {

        private final String useText;

        public LeapThroughTime(String useText) {
            super(LTT_ID, LTT_IMG, TEMP_COST, CardType.POWER, COLOR, CardRarity.SPECIAL, CardTarget.SELF);
            this.useText = useText;
        }

        @Override
        public void upgrade() {}

        @Override
        public void use(AbstractPlayer p, AbstractMonster m) {
            this.addToBot(new TalkAction(true, useText, 0.4f, 2.0f));
            this.addToBot(new ApplyPowerAction(p, p, new LeapThroughTimePower(p)));
        }

        @Override
        public void onChoseThisOption() {
            use(AbstractDungeon.player, null);
        }

        @Override
        public AbstractCard makeCopy() {
            return new LeapThroughTime(useText);
        }
    }
}
