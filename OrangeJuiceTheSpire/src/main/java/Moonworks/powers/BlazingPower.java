package Moonworks.powers;

import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlazingPower extends AbstractPower implements CloneablePowerInterface {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

    public AbstractCreature source;

    public static final String POWER_ID = OrangeJuiceMod.makeID("BlazingPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int secondAmount;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public BlazingPower(final AbstractCreature owner, final int blast, final int blastScale) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = blast;
        this.secondAmount = blastScale;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.loadRegion("firebreathing");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public void atStartOfTurnPostDraw() {
        this.flash();
        /*this.addToBot(new VFXAction(owner, new ScreenOnFireEffect(), 0.0F));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.XLONG, false);
                this.isDone = true;
            }
        });*/
        //Play the happy animation
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (owner instanceof TheStarBreaker) {
                    ((TheStarBreaker) owner).playAnimation("happy");
                }
                this.isDone = true;
            }
        });
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            AbstractPower pow = aM.getPower(ArtifactPower.POWER_ID);
            int artifactAmount = 0;
            if (pow != null) {
                artifactAmount = pow.amount;
                this.addToTop(new ReducePowerAction(aM, owner, pow, amount));
            }
            if (amount > artifactAmount) {
                this.addToBot(new ApplyPowerAction(aM, owner, new BlastingLightPower(aM, amount - artifactAmount), amount - artifactAmount, true));
            }
        }
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                scale();
                this.isDone = true;
            }
        });
    }

    public void updateScaling (int secondAmount) {
        this.secondAmount += secondAmount;
        updateDescription();
    }

    private void scale() {
        this.amount += this.secondAmount;
        updateDescription();
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + secondAmount + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new BlazingPower(owner, amount, secondAmount);
    }
}
