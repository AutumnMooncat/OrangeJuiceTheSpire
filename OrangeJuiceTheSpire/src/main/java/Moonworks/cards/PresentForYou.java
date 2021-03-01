package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.cards.abstractCards.AbstractGiftCard;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.cards.abstractCards.AbstractTrapCard;
import Moonworks.characters.TheStarBreaker;
import basemod.AutoAdd;
import basemod.helpers.ModalChoice;
import basemod.helpers.ModalChoiceBuilder;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class PresentForYou extends AbstractDynamicCard implements ModalChoice.Callback {

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(PresentForYou.class.getSimpleName());
    public static final String NICE_ID = OrangeJuiceMod.makeID(NiceList.class.getSimpleName());
    public static final String NAUGHTY_ID = OrangeJuiceMod.makeID(NaughtyList.class.getSimpleName());

    public static final String IMG = makeCardPath("PresentForYou.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final CardStrings cardStringsNice = CardCrawlGame.languagePack.getCardStrings(NICE_ID);
    private static final CardStrings cardStringsNaughty = CardCrawlGame.languagePack.getCardStrings(NAUGHTY_ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;
    private static final int UPGRADE_COST = 1;
    private static final int TEMP_COST = -2;

    private static final int GIFTS = 1;
    private static final int UPGRADE_PLUS_GIFTS = 1;

    private ModalChoice modal;
    private final AbstractCard nice = new NiceList();
    private final AbstractCard naughty = new NaughtyList();

    // /STAT DECLARATION/

    public PresentForYou() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = GIFTS;
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ((NiceList)nice).setVal(magicNumber);
        ((NaughtyList)naughty).setVal(magicNumber);
        modal = new ModalChoiceBuilder()
                .setCallback(this)
                .addOption(nice)
                .addOption(naughty)
                .create();
        modal.open();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            //this.upgradeBaseCost(UPGRADE_COST);
            this.upgradeMagicNumber(UPGRADE_PLUS_GIFTS);
            this.initializeDescription();
        }
    }

    @Override
    public void optionSelected(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster, int i) {}

    @AutoAdd.Ignore
    public class NiceList extends AbstractNormaAttentiveCard {

        public NiceList() {
            super(NICE_ID, IMG, TEMP_COST, CardType.SKILL, COLOR, CardRarity.SPECIAL, CardTarget.SELF);
        }
        public NiceList(int val) {
            super(NICE_ID, IMG, TEMP_COST, CardType.SKILL, COLOR, CardRarity.SPECIAL, CardTarget.SELF);
            setVal(val);
        }

        @Override
        public void upgrade() {}

        @Override
        public void use(AbstractPlayer p, AbstractMonster m) {
            ArrayList<AbstractCard> cards = new ArrayList<>();
            cards.addAll(AbstractDungeon.srcCommonCardPool.group.stream().filter(c -> c instanceof AbstractGiftCard).collect(Collectors.toCollection(ArrayList::new)));
            cards.addAll(AbstractDungeon.srcUncommonCardPool.group.stream().filter(c -> c instanceof AbstractGiftCard).collect(Collectors.toCollection(ArrayList::new)));
            cards.addAll(AbstractDungeon.srcRareCardPool.group.stream().filter(c -> c instanceof AbstractGiftCard).collect(Collectors.toCollection(ArrayList::new)));
            for (int i = 0 ; i < magicNumber ; i++) {
                AbstractCard card = cards.get(AbstractDungeon.cardRandomRng.random(cards.size() - 1));
                this.addToBot(new MakeTempCardInDrawPileAction(card, 1, true, true));
            }
        }

        @Override
        public AbstractCard makeCopy() {
            return new NiceList(magicNumber);
        }

        private void setVal(int amount) {
            this.magicNumber = this.baseMagicNumber = amount;
            if (this.magicNumber > 1) {
                this.rawDescription = cardStringsNice.UPGRADE_DESCRIPTION;
            }
        }
    }

    @AutoAdd.Ignore
    public class NaughtyList extends AbstractNormaAttentiveCard {

        public NaughtyList() {
            super(NAUGHTY_ID, IMG, TEMP_COST, CardType.SKILL, COLOR, CardRarity.SPECIAL, CardTarget.SELF);
        }
        public NaughtyList(int val) {
            super(NAUGHTY_ID, IMG, TEMP_COST, CardType.SKILL, COLOR, CardRarity.SPECIAL, CardTarget.SELF);
            setVal(val);
        }

        @Override
        public void upgrade() {}

        @Override
        public void use(AbstractPlayer p, AbstractMonster m) {
            ArrayList<AbstractCard> cards = new ArrayList<>();
            cards.addAll(AbstractDungeon.srcCommonCardPool.group.stream().filter(c -> c instanceof AbstractTrapCard).collect(Collectors.toCollection(ArrayList::new)));
            cards.addAll(AbstractDungeon.srcUncommonCardPool.group.stream().filter(c -> c instanceof AbstractTrapCard).collect(Collectors.toCollection(ArrayList::new)));
            cards.addAll(AbstractDungeon.srcRareCardPool.group.stream().filter(c -> c instanceof AbstractTrapCard).collect(Collectors.toCollection(ArrayList::new)));
            for (int i = 0 ; i < magicNumber ; i++) {
                AbstractCard card = cards.get(AbstractDungeon.cardRandomRng.random(cards.size() - 1));
                this.addToBot(new MakeTempCardInDrawPileAction(card, 1, true, true));
            }
        }

        @Override
        public AbstractCard makeCopy() {
            return new NaughtyList(magicNumber);
        }

        private void setVal(int amount) {
            this.magicNumber = this.baseMagicNumber = amount;
            if (this.magicNumber > 1) {
                this.rawDescription = cardStringsNaughty.UPGRADE_DESCRIPTION;
            }
        }
    }
}