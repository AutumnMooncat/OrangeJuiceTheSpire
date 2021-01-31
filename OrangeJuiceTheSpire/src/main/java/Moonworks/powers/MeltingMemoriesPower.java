package Moonworks.powers;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.MeltingMemoriesConvertMemoryAction;
import Moonworks.actions.WitherExhaustImmediatelyAction;
import Moonworks.powers.interfaces.AssociateableInterface;
import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.ExhaustiveField;
import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.getRandomMonster;

public class MeltingMemoriesPower extends AbstractPower implements CloneablePowerInterface, AssociateableInterface {
    public AbstractCreature source;

    public static final String POWER_ID = OrangeJuiceMod.makeID("MeltingMemoriesPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public boolean upgrade;


    public MeltingMemoriesPower(final AbstractCreature owner, final int amount, final boolean upgrade) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.upgrade = upgrade;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.loadRegion("establishment");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public MeltingMemoriesPower(final AbstractCreature owner, final int amount, final boolean upgrade, final ArrayList<AbstractCard> cards) {
        this(owner, amount, upgrade);
        this.cards.addAll(cards);
        updateDescription();
    }


    @Override
    public void atStartOfTurnPostDraw() {
        super.atStartOfTurnPostDraw();

        //Flash the power
        flash();

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

                            //Calculate the stuff
                            card.applyPowers();
                            card.calculateCardDamage(t);

                            OrangeJuiceMod.logger.info("Using Memory: "+card);

                            //Use the card on the target
                            card.use(AbstractDungeon.player, t);

                            //Flash the card the color of a Power
                            card.flash(OrangeJuiceMod.POWER_BLUE.cpy());
                            //card.flash(Color.PURPLE.cpy()); //Purple is nice too

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

    @Override
    public void onInitialApplication() {
        //this.addToBot(new MeltingMemoriesSpawnMagicCardAction(this));
        this.addToBot(new MeltingMemoriesConvertMemoryAction(this, amount));
        super.onInitialApplication();
    }

    @Override
    public void stackPower(int stackAmount) {
        //this.addToBot(new MeltingMemoriesSpawnMagicCardAction(this));
        this.addToBot(new MeltingMemoriesConvertMemoryAction(this, stackAmount));
        super.stackPower(stackAmount);
    }

    //If we reapply a newer power that is upgraded, we turn all our old stacks into upgraded ones
    public void setUpgrade(boolean upgrade) {
        this.upgrade = upgrade;
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        StringBuilder sb = new StringBuilder();
        if (cards.size() == 0) { //Empty
            sb.append(DESCRIPTIONS[2]);
        } else { //Show the list of cards
            sb.append(DESCRIPTIONS[3]);

            /*//Append all the card names
            for (AbstractCard card : cards) {
                sb.append(" NL ").append(card.name);
            }//*/
        }
        description = sb.toString();
    }

    @Override
    public AbstractPower makeCopy() {
        return new MeltingMemoriesPower(owner, amount, upgrade, cards);
    }
}