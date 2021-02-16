package Moonworks.cards.cutCards;
//
//import Moonworks.OrangeJuiceMod;
//import Moonworks.cards.abstractCards.AbstractDynamicCard;
//import Moonworks.characters.TheStarBreaker;
//import Moonworks.powers.TemporalAnchorPower;
//import Moonworks.relics.WarpPanel;
//import basemod.AutoAdd;
//import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
//import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.core.CardCrawlGame;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//
//import static Moonworks.OrangeJuiceMod.makeCardPath;
//@Deprecated
//@AutoAdd.Ignore
//public class LeapThroughSpace extends AbstractDynamicCard {
//
//    // TEXT DECLARATION
//
//    public static final String ID = OrangeJuiceMod.makeID(LeapThroughSpace.class.getSimpleName());
//    public static final String IMG = makeCardPath("LeapThroughSpacePower.png");
//
//    // /TEXT DECLARATION/
//
//
//    // STAT DECLARATION
//
//    private static final CardRarity RARITY = CardRarity.RARE;
//    private static final CardTarget TARGET = CardTarget.SELF;
//    private static final CardType TYPE = CardType.POWER;
//    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;
//
//    private static final int COST = 1;
//    private static final int UPGRADE_COST = 0;
//
//    public static final int DIVERGENCE = 15;
//    public static final int UPGRADE_PLUS_DIVERGENCE = -5;
//    public static final int DIVERGENCET0LIMIT = 25;
//    public static final int DIVERGENCET1LIMIT = 50;
//    public static final int DIVERGENCET2LIMIT = 90;
//
//    public static final int DIVERGENCEMAX = 100;
//    public static final int DIVERGENCELIMIT = 100;
//    public static final int DIVERGENCERELICLIMIT = 80;
//
//
//
//    // /STAT DECLARATION/
//
//    public LeapThroughSpace() {
//        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
//        //This one is expressly just a preview with no functionality
//        this.cardsToPreview = new LeapThroughTime(this, true);
//        this.magicNumber = this.baseMagicNumber = DIVERGENCE;
//    }
//
//    // Actions the card should do.
//    @Override
//    public void use(AbstractPlayer p, AbstractMonster m) {
//        //This one is created and actually stores our data.
//        CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05F);
//        CardCrawlGame.sound.play("HEART_BEAT", 0.05F);
//        LeapThroughTime c = new LeapThroughTime(this, false);
//        boolean hasRelic = AbstractDungeon.player.hasRelic(WarpPanel.ID);
//        this.addToBot(new MakeTempCardInDrawPileAction(c, 1, false, true, true));
//        if (hasRelic) {
//            AbstractDungeon.player.getRelic(WarpPanel.ID).flash();
//        }
//        this.addToBot(new ApplyPowerAction(p, p, new TemporalAnchorPower(p, hasRelic ? DIVERGENCERELICLIMIT : DIVERGENCELIMIT, magicNumber)));
//        //AbstractDungeon.effectList.add(new ShowCardAndAddToDrawPileEffect(c, this.x, this.y, this.randomSpot, this.autoPosition, this.toBottom));
//    }
//
//    // Upgraded stats.
//    @Override
//    public void upgrade() {
//        if (!this.upgraded) {
//            this.upgradeName();
//            this.cardsToPreview.upgrade();
//            this.upgradeBaseCost(UPGRADE_COST);
//            this.upgradeMagicNumber(UPGRADE_PLUS_DIVERGENCE);
//            this.initializeDescription();
//        }
//    }
//}