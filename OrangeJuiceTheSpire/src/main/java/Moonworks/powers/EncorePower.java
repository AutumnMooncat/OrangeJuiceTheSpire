package Moonworks.powers;

import Moonworks.OrangeJuiceMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class EncorePower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = OrangeJuiceMod.makeID("EncorePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    //Two arrayLists, because iterating backwards over a Map is awful
    private final ArrayList<AbstractCard> cardArray = new ArrayList<>();
    private final ArrayList<AbstractMonster> targetArray = new ArrayList<>();
    //private boolean multiCardSupport = false;

    //private AbstractCard lastCard;
    //private AbstractMonster lastMonster;
    private String lastName = "?";



    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public EncorePower(final AbstractCreature owner, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.loadRegion("hymn");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public void onEnergyRecharge() {
        super.onEnergyRecharge();

        for (int i = 0 ; i < Math.min(amount, cardArray.size()) ; i++) {

            //Use an action so things don't get messed up
            int finalI = i;
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                public void update() {
                    //Flash this power
                    flash();

                    //Get the original monster
                    AbstractMonster t = targetArray.get(finalI);
                    /*OrangeJuiceMod.logger.info("Target: "+t);
                    if (t != null) {
                        OrangeJuiceMod.logger.info("Alive?: "+t.isDeadOrEscaped());
                        OrangeJuiceMod.logger.info("HP: "+t.currentHealth);
                    }*/

                    //If its null, dead, or otherwise gone, grab a new target.
                    //If the card has a null target to begin with, that it ought not interact with what we pass into the use function
                    //Still grab a non-null target anyway for safety
                    if (t == null || t.isDeadOrEscaped() || t.currentHealth <= 0 || !AbstractDungeon.getMonsters().monsters.contains(t)) {
                        t = AbstractDungeon.getRandomMonster(t);
                        //OrangeJuiceMod.logger.info("Selected new target : "+t);
                    }
                    if (t != null) {
                        //Calculate the stuff
                        cardArray.get(finalI).applyPowers();
                        cardArray.get(finalI).calculateCardDamage(t);

                        //Use the card on the (new) target
                        cardArray.get(finalI).use(AbstractDungeon.player, t);
                    }

                    //End Action
                    this.isDone = true;
                }
            });
        }

        //Use an action so things don't get messed up
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            public void update() {
                //Clear the arrays and wait for next time
                cardArray.clear();
                targetArray.clear();

                //Clear the card text for next time
                lastName = "?";

                //Update descriptions
                updateDescription();

                //End Action
                this.isDone = true;
            }
        });
    }

    //Fix scaling here?
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        updateDescription();
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        super.onPlayCard(card, m);

        //Put a copy of every card played into the arrays
        if(!card.purgeOnUse) {
            cardArray.add(0, card);
            targetArray.add(0, m);
            lastName = getNameStrings();
        }
        //Update
        updateDescription();
    }

    private String getNameStrings() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0 ;  i < amount ; i++) {
            if (i < cardArray.size()) {
                sb.append(cardArray.get(i).name).append(i < (amount - 1) ? ", " : ""); //Add the name, and add space if there are still more names to add
            } else {
                sb.append("?").append(i < (amount - 1) ? ", " : ""); //Add a ? if we don't have a card yet, and add space if there are still more names to add

            }
        }

        return sb.toString();
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if (cardArray.size() == 0) {
            description = DESCRIPTIONS[0];
        } else {
            description = DESCRIPTIONS[0] + DESCRIPTIONS[1] + lastName + DESCRIPTIONS[2];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new EncorePower(owner, amount);
    }
}