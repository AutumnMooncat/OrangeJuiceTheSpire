package Moonworks.powers;

import Moonworks.OrangeJuiceMod;
import basemod.ClickableUIElement;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EnergizedBluePower;
import com.megacrit.cardcrawl.powers.EnergizedPower;

public class LeapThroughTimePower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = OrangeJuiceMod.makeID("LeapThroughTimePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public PowerUIElement powerElement;

    private int energyBorrowed;


    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public LeapThroughTimePower(final AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;

        type = PowerType.BUFF;
        isTurnBased = false;
        powerElement = new PowerUIElement();

        // We load those txtures here.
        this.loadRegion("time");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if (energyBorrowed > 0) {
            description = DESCRIPTIONS[0] + DESCRIPTIONS[1] + energyBorrowed + DESCRIPTIONS[2];
        } else {
            description = DESCRIPTIONS[0];
        }
    }

    @Override
    public void atEndOfRound() {
        super.atEndOfRound();
        AbstractDungeon.player.energy.energy -= energyBorrowed;
    }

    @Override
    public void onEnergyRecharge() {
        super.onEnergyRecharge();
        restoreMaxEnergy();
    }

    public void restoreMaxEnergy() {
        if (energyBorrowed > 0) {
            flash();
            AbstractDungeon.player.energy.energy += energyBorrowed;
            energyBorrowed = 0;
        }
        updateDescription();
    }

    @Override
    public AbstractPower makeCopy() {
        return new LeapThroughTimePower(owner);
    }

    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        c = new Color(0.0F, 1.0F, 0.0F, 1.0F);
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.energyBorrowed), x, y, this.fontScale, c);
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
        super.renderIcons(sb, x, y, c);
        //clickableHitbox.move(x, y);
        powerElement.move(x, y);
        powerElement.render(sb);
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        powerElement.update();
    }

    public int determineNextTurnEnergy() {
        //Relics modify this var, so this should cover most, if not all, relics
        int energy = AbstractDungeon.player.energy.energyMaster;
        //Expressly ignore Ice Cream, as we only care about how much our energy would go up by, not the final energy

        //Check if we have the Energized Powers
        AbstractPower pow = AbstractDungeon.player.getPower(EnergizedPower.POWER_ID);
        if (pow instanceof EnergizedPower) {
            energy += pow.amount;
        }
        pow = AbstractDungeon.player.getPower(EnergizedBluePower.POWER_ID);
        if (pow instanceof EnergizedBluePower) {
            energy += pow.amount;
        }

        //Reduce by any energy we have already borrowed
        energy -= energyBorrowed;

        //There are probably more checks I haven't thought of, but this will do for now
        return energy;
    }

    public class PowerUIElement extends ClickableUIElement {
        private static final float hitboxSize = 40f;
        private static final float moveDelta = hitboxSize/2;

        public PowerUIElement() {
            super(new TextureAtlas(Gdx.files.internal("powers/powers.atlas")).findRegion("48/" + ""),//carddraw
                    0, 0,
                    hitboxSize, hitboxSize);
        }

        public void move(float x, float y) {
            move(x, y, moveDelta);
        }

        public void move(float x, float y, float d) {
            this.setX(x-(d * Settings.scale));
            this.setY(y-(d * Settings.scale));
        }


        @Override
        protected void onHover() {}

        @Override
        protected void onUnhover() {}

        @Override
        protected void onClick() {
            if (!AbstractDungeon.actionManager.turnHasEnded) {
                //Determine if we have E to take
                boolean hasEToTake = determineNextTurnEnergy() > 0;
                //If we do...
                if (hasEToTake) {
                    //Flash
                    flash();
                    //Gain E
                    addToTop(new GainEnergyAction(1));
                    //Set less E for next turn
                    energyBorrowed++;
                    //Update description
                    updateDescription();
                }
            }
        }
    }
}
