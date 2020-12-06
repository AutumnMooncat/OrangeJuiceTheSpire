package Moonworks.powers;

import Moonworks.OrangeJuiceMod;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;

import java.util.Random;

public class NormaPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = OrangeJuiceMod.makeID("NormaPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public boolean broken;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public NormaPower(final AbstractCreature owner, final int amount) {
        this(owner, amount, false);
    }

    public NormaPower(final AbstractCreature owner, final int amount, boolean broken) {
        name = NAME;// + " " + amount;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.broken = broken;

        type = PowerType.BUFF;
        isTurnBased = false;
        this.priority = 0;

        // We load those txtures here.
        this.loadRegion("focus");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public void breakNorma() {
        broken = true;
        amount = 6;
        name = NAME + " #r" + amount;
        fontScale = 0.0F;
        this.addToBot(new ApplyPowerAction(owner, owner, new VulnerablePower(owner, 10, false)));
        updateDescription();
        applyBrokenEffects();
    }
    public void onEnergyRecharge() {
        super.onEnergyRecharge();
        if (broken) {
            applyBrokenEffects();
        }
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        super.renderAmount(sb, x, y , c);
        if (broken) {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x, y, this.fontScale, new Color(1.0F, 0.0F, 0.0F, 1.0F));
        }

    }
    private void applyBrokenEffects() {
        this.addToBot(new VFXAction(owner, new ScreenOnFireEffect(), 1.0F));
        this.addToBot(new LoseHPAction(owner, owner, AbstractDungeon.cardRandomRng.random(10, 15)));
        this.addToBot(new GainEnergyAction(AbstractDungeon.cardRandomRng.random(1, 3)));
        if (AbstractDungeon.cardRandomRng.random(1, 2) == 1){
            this.addToBot(new ApplyPowerAction(owner, owner, new DoubleDamagePower(owner, 1, false)));
        } else {
            this.addToBot(new ApplyPowerAction(owner, owner, new IntangiblePlayerPower(owner, 1)));
        }
        for (int i = 0 ; i < AbstractDungeon.cardRandomRng.random(5, 10) ; i++) {
            for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters){
                if (!aM.isDeadOrEscaped()) {
                    this.addToBot(new ApplyPowerAction(aM, owner, new BlastingLightPower(aM, 1)));
                }
            }
        }
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        if (broken) {
            //name = NAME + " #r" + getRandomString(AbstractDungeon.cardRandomRng.random(5, 5));
            updateDescription();
            //amount = AbstractDungeon.cardRandomRng.random(100, 999);
        }
    }

    private String getRandomString(int length) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    @Override
    public void stackPower(int stackAmount) {
        if(!broken) {
            super.stackPower(stackAmount);
            if (amount > 5) {
                amount = 5;
            }
            name = NAME + " " + amount;
            updateDescription();
        }
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if (!broken) {
            description = DESCRIPTIONS[0];
        } else {
            //description = DESCRIPTIONS[1];
            description = "#r"+getRandomString(AbstractDungeon.cardRandomRng.random(20, 20));
        }

    }

    @Override
    public AbstractPower makeCopy() {
        return new NormaPower(owner, amount);
    }
}
