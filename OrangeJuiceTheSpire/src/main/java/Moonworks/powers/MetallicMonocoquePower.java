package Moonworks.powers;

import Moonworks.OrangeJuiceMod;
import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MetallicMonocoquePower extends TwoAmountPower implements CloneablePowerInterface {
    public AbstractCreature source;
    public static final String POWER_ID = OrangeJuiceMod.makeID("MetallicMonocoquePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public MetallicMonocoquePower(final AbstractCreature owner, final int thornAmount, final int amount) {
        name = NAME;
        ID = POWER_ID;
        this.amount2 = amount;
        this.amount = thornAmount;

        this.owner = owner;
        //this.amount = amount;
        //this.thornAmount = thornAmount;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.loadRegion("armor");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if(damageAmount > 0) {
            this.flash();
            if (info.type == DamageInfo.DamageType.NORMAL) {
                damageAmount -= amount2;
            } else {
                damageAmount -= amount;
            }
        }
        return super.onAttackedToChangeDamage(info, damageAmount);
    }

    public void stackPower(int stackAmount) {
        //this.reduceThornAmount += 2*stackAmount; //but its either 2 OR 3 based on if upgraded... Bypassed issue by making thorns not upgrade...
        super.stackPower(stackAmount);
        updateDescription();
    }
    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount2 + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new MetallicMonocoquePower(owner, amount, amount2);
    }
}
