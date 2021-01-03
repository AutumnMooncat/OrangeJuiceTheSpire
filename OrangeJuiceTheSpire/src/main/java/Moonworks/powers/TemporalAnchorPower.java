package Moonworks.powers;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.LeapThroughSpace;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class TemporalAnchorPower extends TwoAmountPower implements CloneablePowerInterface {
    public AbstractCreature source;
    public static final String POWER_ID = OrangeJuiceMod.makeID("TemporalAnchorPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final int baseNumerator;


    private final Color greenColor = Color.GREEN.cpy();
    private final Color yellowColor = Color.YELLOW.cpy();
    private final Color redColor = Color.RED.cpy();
    private final Color darkRedColor = Color.MAROON.cpy();


    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public TemporalAnchorPower(final AbstractCreature owner, final int denominator, final int numerator) {
        name = NAME;
        ID = POWER_ID;
        this.amount = Math.min(denominator, 100);
        this.amount2 = Math.min(numerator, denominator);
        this.baseNumerator = Math.min(numerator, denominator);

        this.owner = owner;

        type = AbstractPower.PowerType.BUFF;
        this.isTurnBased = false; // Maybe?
        this.canGoNegative = false;

        // We load those txtures here.
        this.loadRegion("time");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public void stackPower(int stackAmount) {
        //Do nothing
        //updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        super.atStartOfTurnPostDraw();
        //Recall this function to auto stack itsself
        this.flash();
        CardCrawlGame.sound.play("HEART_BEAT", 0.05F);
        if (this.amount2 < this.amount) {
            this.amount2 = Math.min(this.amount, this.amount2 + this.baseNumerator);
        }
        updateDescription();
    }

    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        c = this.amount2 <= LeapThroughSpace.DIVERGENCET0LIMIT ? this.greenColor :
                this.amount2 <= LeapThroughSpace.DIVERGENCET1LIMIT ? this.yellowColor :
                this.amount2 <= LeapThroughSpace.DIVERGENCET2LIMIT ? this.redColor : this.darkRedColor;
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x, y, this.fontScale, c);
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount2), x, y + 15.0F * Settings.scale, this.fontScale, c);
    }


    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if (amount2 < 100) {
            String divergenceColor = amount2 <= LeapThroughSpace.DIVERGENCET0LIMIT ? "#g" : amount2 <= LeapThroughSpace.DIVERGENCET1LIMIT ? "#y" : "#r";
            divergenceColor += amount2+"/"+amount;
            description = DESCRIPTIONS[0] + divergenceColor + DESCRIPTIONS[1] + baseNumerator + DESCRIPTIONS[2];
        } else {
            description = DESCRIPTIONS[3];
        }

    }

    @Override
    public AbstractPower makeCopy() {
        return new TemporalAnchorPower(owner, amount, amount2);
    }
}