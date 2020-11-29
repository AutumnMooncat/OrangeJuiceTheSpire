package Moonworks.powers;

import Moonworks.OrangeJuiceMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TreasureThiefPower extends AbstractPower implements CloneablePowerInterface {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

    public AbstractCreature source;

    public static final String POWER_ID = OrangeJuiceMod.makeID("TreasureThiefPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public int count;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public TreasureThiefPower(final AbstractCreature owner, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        count = 0;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        //this.loadRegion("retain");
        this.loadRegion("rebound");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    /*
    @Override //Works for intent block, but this is covered by the patch!
    public void atEndOfRound() {
        if(!this.owner.isDying && !this.owner.isDead && this.owner.currentBlock > 0) {
            this.flash();
            this.owner.loseBlock();
            this.amount--;
            if (this.amount == 0) {
                this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
            }
        }
        super.atEndOfRound();
    }*/

    @Override
    public void atStartOfTurn() {
        this.count = 0;
        updateDescription();
        super.atStartOfTurn();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if(!this.owner.isDead) {
            if(count < amount){
                this.flash();
                this.count++;
                this.addToBot(new DrawCardAction(1));
            }
            updateDescription();
        }
        return super.onAttacked(info, damageAmount);
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if (count+1 == amount) {
            description = DESCRIPTIONS[0] + (amount-count) + DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[0] + (amount-count) + DESCRIPTIONS[2];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new TreasureThiefPower(owner, amount);
    }
}
