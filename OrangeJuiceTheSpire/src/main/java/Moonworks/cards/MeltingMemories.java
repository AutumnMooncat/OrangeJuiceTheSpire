package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.MeltingMemoriesAction;
import Moonworks.cards.defaultcards.AbstractDynamicCard;
import Moonworks.cards.tempCards.MagicalInferno;
import Moonworks.cards.tempCards.MagicalMassacre;
import Moonworks.cards.tempCards.MagicalRevenge;
import Moonworks.characters.TheStarBreaker;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class MeltingMemories extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In-Progress Form At the start of your turn, play a TOUCH.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(MeltingMemories.class.getSimpleName());
    public static final String IMG = makeCardPath("MeltingMemories.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 3;

    private static float duration = 0;
    private static int state = 0;

    private final ArrayList<AbstractCard> previews = new ArrayList<>();
    // /STAT DECLARATION/


    public MeltingMemories() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        previews.add(new MagicalInferno());
        previews.add(new MagicalMassacre());
        previews.add(new MagicalRevenge());
        this.cardsToPreview = previews.get(state);

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new MeltingMemoriesAction(upgraded));
    }

    @Override
    public void update() {
        super.update();
        duration += Gdx.graphics.getDeltaTime();
        if (duration > 2.0f) {
            state = (state + 1) % previews.size();
            cardsToPreview = previews.get(state);
            duration = 0.0f;
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            for (AbstractCard card : previews) {
                card.upgrade();
            }
            initializeDescription();
        }
    }
}
