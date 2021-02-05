package Moonworks.powers;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.WitherExhaustImmediatelyAction;
import Moonworks.cardModifiers.MemoryModifier;
import Moonworks.cards.abstractCards.AbstractGiftCard;
import Moonworks.cards.abstractCards.AbstractTempCard;
import Moonworks.patches.MemoryAssociationPatch;
import basemod.helpers.CardModifierManager;
import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.ExhaustiveField;
import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.HashSet;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.getRandomMonster;

public class BookOfMemoriesPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = OrangeJuiceMod.makeID("BookOfMemoriesPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public boolean upgrade;
    private final HashSet<AbstractCard> cards = new HashSet<>();

    public BookOfMemoriesPower(final AbstractPlayer owner, HashSet<AbstractCard> cards) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        addCardsToSet(cards);
        this.amount = this.cards.size();

        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        // We load those txtures here.
        this.loadRegion("book"); //establishment
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();

    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        this.amount = cards.size();
        updateDescription();
    }

    public void addCardsToSet(HashSet<AbstractCard> cards) {
        for (AbstractCard card : cards) {
            CardModifierManager.addModifier(card, new MemoryModifier(false));
            MemoryAssociationPatch.MemoryAssociation.associations.get(card).add(this);
        }
        this.cards.addAll(cards);
    }

    public void removeCardsFromSet(HashSet<AbstractCard> cards) {
        for (AbstractCard card : cards) {
            MemoryAssociationPatch.MemoryAssociation.associations.get(card).remove(this);
        }
        this.cards.removeAll(cards);
    }

    @Override
    public void atStartOfTurnPostDraw() {
        OrangeJuiceMod.logger.info("Num cards: "+cards.size());
        OrangeJuiceMod.logger.info("Cards: "+cards.toString());
        super.atStartOfTurnPostDraw();

        for (AbstractCard card : cards) {

            //Use an action so things don't get messed up
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                public void update() {

                    //Play it if it is in your hand
                    if(AbstractDungeon.player.hand.contains(card)) {
                        //Get a random monster
                        AbstractMonster t = getRandomMonster();

                        //If it isn't null, which will hopefully be the case unless something messed with getRandomMonster
                        if (t != null) {

                            //Flash the power
                            flash();

                            //Calculate the stuff
                            card.applyPowers();
                            card.calculateCardDamage(t);

                            OrangeJuiceMod.logger.info("Using Memory: "+card);

                            //Flash the card the color of a Power
                            card.flash(OrangeJuiceMod.POWER_BLUE.cpy());
                            //card.flash(Color.PURPLE.cpy()); //Purple is nice too

                            //Use the card on the target
                            card.use(AbstractDungeon.player, t);

                            //Sure hope this works for Exhaustive cards, lol
                            if(ExhaustiveField.ExhaustiveFields.baseExhaustive.get(card) != -1) {
                                ExhaustiveVariable.increment(card);
                            }

                            //Exhaust it if it would have exhausted when played
                            if (card.exhaust || card.exhaustOnUseOnce || card.purgeOnUse) {
                                this.addToBot(new WitherExhaustImmediatelyAction(card));
                            }
                        }
                    }

                    //End Action
                    this.isDone = true;
                }
            });
        }
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        this.amount = cards.size();
        if (this.amount == 0) {
            description = DESCRIPTIONS[2];
        } else {
            description = DESCRIPTIONS[3];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new BookOfMemoriesPower((AbstractPlayer) owner, cards);
    }

    public static boolean getViability(AbstractCard c) {
        return (c.type != AbstractCard.CardType.POWER && c.type != AbstractCard.CardType.STATUS && c.type != AbstractCard.CardType.CURSE && !(c instanceof AbstractGiftCard) && !(c instanceof AbstractTempCard) && c.cost >= 0);
    }
}