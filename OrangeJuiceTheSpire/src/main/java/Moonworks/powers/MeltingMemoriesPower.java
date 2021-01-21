package Moonworks.powers;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.MeltingMemoriesAction;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MeltingMemoriesPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = OrangeJuiceMod.makeID("MeltingMemoriesPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    private boolean upgrade;


    public MeltingMemoriesPower(final AbstractCreature owner, final int amount, final boolean upgrade) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.upgrade = upgrade;

        type = PowerType.BUFF;
        isTurnBased = true;

        // We load those txtures here.
        this.loadRegion("establishment");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }


    @Override
    public void atStartOfTurnPostDraw() {
        super.atStartOfTurnPostDraw();
        flash();
        this.addToBot(new MeltingMemoriesAction(upgrade));
        this.addToBot(new ReducePowerAction(owner, owner, this, 1));
        //updateDescription();
    }

    //If we reapply a newer power that is upgraded, we turn all our old stacks into upgraded ones
    public void setUpgrade(boolean upgrade) {
        this.upgrade = upgrade;
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new MeltingMemoriesPower(owner, amount, upgrade);
    }
}