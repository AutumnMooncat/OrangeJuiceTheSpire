package Moonworks.powers;

import Moonworks.OrangeJuiceMod;
import basemod.BaseMod;
import basemod.ClickableUIElement;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class HolyNightPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = OrangeJuiceMod.makeID("HolyNightPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public PowerUIElement powerElement;

    private int clicks;
    private boolean infinite;
    //private final String infSymbol = new String(Character.toString('\u221E').getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);


    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public HolyNightPower(final AbstractCreature owner, int amount, boolean infinite) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.infinite = infinite;

        type = PowerType.BUFF;
        isTurnBased = false;
        powerElement = new PowerUIElement();

        // We load those txtures here.
        this.loadRegion("draw");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if (infinite) {
            description = DESCRIPTIONS[0] + DESCRIPTIONS[2];
        } else {
            description = DESCRIPTIONS[0] + (amount-clicks) + DESCRIPTIONS[1];
        }
    }

    @Override
    public void onEnergyRecharge() {
        super.onEnergyRecharge();
        restoreUses();
    }

    public void restoreUses() {
        flashWithoutSound();
        clicks = 0;
        updateDescription();
    }

    @Override
    public AbstractPower makeCopy() {
        return new HolyNightPower(owner, amount, infinite);
    }

    public void setInfinite(boolean infinite) {
        this.infinite = infinite;
    }

    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        c = new Color(0.0F, 1.0F, 0.0F, 1.0F);
        if (infinite) {
            //The infinity symbol doesn't load :(
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, "", x, y, this.fontScale, c);
        } else {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount - this.clicks), x, y, this.fontScale, c);
        }
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
            //If it's our turn
            if (!AbstractDungeon.actionManager.turnHasEnded) {
                //If we have uses...
                if (clicks < amount || infinite) {
                    //If we have room in our hand...
                    if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                        //If we have E to take
                        if (EnergyPanel.totalCount > 0) {
                            //Flash
                            flash();
                            //Lose E
                            addToTop(new LoseEnergyAction(1));
                            //increment clicks
                            clicks++;
                            //Draw cards
                            addToBot(new DrawCardAction(2));
                            //Update description
                            updateDescription();
                        }
                        //Out of energy message?
                    } else {
                        //Hand full message?
                        AbstractDungeon.player.createHandIsFullDialog();
                    }
                }
                //No uses message?
            }
            //Not our turn message?
        }
    }
}
