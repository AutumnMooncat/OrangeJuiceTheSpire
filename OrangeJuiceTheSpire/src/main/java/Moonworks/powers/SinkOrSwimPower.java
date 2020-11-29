package Moonworks.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.*;
import Moonworks.OrangeJuiceMod;

public class SinkOrSwimPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = OrangeJuiceMod.makeID("SinkOrSwimPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private int debuffStacks;
    private final int baseDebuffStacks;
    private int count = 0;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public SinkOrSwimPower(final AbstractCreature owner, final int amount, final int debuffStacks) {
        name = NAME;
        ID = POWER_ID;
        this.debuffStacks = this.baseDebuffStacks = debuffStacks;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.loadRegion("energized_blue");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public void onEnergyRecharge() {
        this.flash();
        AbstractDungeon.player.gainEnergy(this.amount);
        if (count < debuffStacks) {
            count++;
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new VulnerablePower(this.owner, 1, false)));
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new WeakPower(this.owner, 1, false)));
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new FrailPower(this.owner, 1, false)));
            updateDescription();
        }

    }
    //Fix scaling here?
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        this.debuffStacks += baseDebuffStacks*stackAmount;
        updateDescription();

    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if (count == debuffStacks) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else if (count+1 == debuffStacks) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2] + (debuffStacks-count) + DESCRIPTIONS[3];
        } else {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2] + (debuffStacks-count) + DESCRIPTIONS[4];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new SinkOrSwimPower(owner, amount, debuffStacks);
    }
}
