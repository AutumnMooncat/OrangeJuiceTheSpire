package Moonworks.powers;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.LongDistanceShot;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NormaPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = OrangeJuiceMod.makeID("NormaPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public boolean broken;
    public boolean safeBreak;

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

    public void breakNorma(boolean upgraded) {
        safeBreak = upgraded;
        broken = true;
        amount = 6;
        name = NAME + " #r" + amount;
        fontScale = 0.0F;
        this.addToBot(new ApplyPowerAction(owner, owner, new VulnerablePower(owner, safeBreak ? 2 : 10, false)));
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
        if (broken) {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x, y, this.fontScale, new Color(1.0F, 0.0F, 0.0F, 1.0F));
        } else {
            super.renderAmount(sb, x, y , c);
            if (this.amount == 0) {
                FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x, y, this.fontScale, new Color(0.0F, 1.0F, 0.0F, 1.0F));
            }
        }
    }
    private void applyBrokenEffects() {
        int hpLoss = AbstractDungeon.cardRandomRng.random(10, 15);
        this.addToBot(new VFXAction(owner, new ScreenOnFireEffect(), 1.0F));
        this.addToBot(new LoseHPAction(owner, owner, hpLoss - (safeBreak ? 9 : 0)));
        this.addToBot(new GainEnergyAction(AbstractDungeon.cardRandomRng.random(1, 3)));
        if (AbstractDungeon.cardRandomRng.random(1, 2) == 1){
            this.addToBot(new ApplyPowerAction(owner, owner, new DoubleDamagePower(owner, 1, false)));
        } else {
            this.addToBot(new ApplyPowerAction(owner, owner, new IntangiblePlayerPower(owner, 1)));
        }
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            AbstractPower pow = aM.getPower(ArtifactPower.POWER_ID);
            int artifactAmount = 0;
            if (pow != null) {
                artifactAmount = pow.amount;
                this.addToTop(new ReducePowerAction(aM, owner, pow, hpLoss));
            }
            if (hpLoss > artifactAmount) {
                this.addToBot(new ApplyPowerAction(aM, owner, new BlastingLightPower(aM, hpLoss-artifactAmount), hpLoss-artifactAmount, true));
            }
        }
    }

    /*
    //Play LDS cards, also do this in stack
    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        if (amount > 0) {
            autoPlayLDS();
        }
    }*/

    private void autoPlayLDS() {
        //Make a map of the cards and where they came from
        Map<LongDistanceShot, CardGroup> LDSMap = new HashMap<>();

        //Find the ones that are more or less active
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c instanceof LongDistanceShot) {
                LDSMap.put((LongDistanceShot) c, AbstractDungeon.player.drawPile);
            }
        }
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c instanceof LongDistanceShot) {
                LDSMap.put((LongDistanceShot) c, AbstractDungeon.player.discardPile);
            }
        }
        for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
            if (c instanceof LongDistanceShot) {
                LDSMap.put((LongDistanceShot) c, AbstractDungeon.player.exhaustPile);
            }
        }

        //Set them all to AutoPlay and move them to the hand
        for (LongDistanceShot l : LDSMap.keySet()) {
            //l.oldAutoPlayState = AutoplayField.autoplay.get(l);
            //l.normaAutoPlay = true;
            //AutoplayField.autoplay.set(l, true);
            AbstractDungeon.player.hand.addToTop(l);
            LDSMap.get(l).removeCard(l);
        }

        //Clear the map
        LDSMap.clear();
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
        if (stackAmount == 0) {
            //We have no reason to do anything if we are stacking by 0 >:L
            return;
        }
        if (stackAmount > 1) {
            //If we get a big amount of Norma all at the same time, break it down into individual increases of 1 so our cards can flash properly
            for (int i = 0 ; i < stackAmount ; i++){
                this.stackPower(1);
            }
            return;
        }
        if (stackAmount < -1) {
            //If we get a big amount of negative Norma all at the same time, break it down into individual decreases of 1 so our cards can flash properly
            for (int i = 0 ; i < -stackAmount ; i++){
                this.stackPower(-1);
            }
            return;
        }
        if (stackAmount > 0 && amount < 5) {
            autoPlayLDS();
        }
        if(!broken) {
            boolean flashCardsGreen = stackAmount > 0 && amount < 5; //If we are already at Norma 5, we don't want to flash the cards when we increase
            boolean flashCardsRed = stackAmount < 0 && amount > 0; //If we are already at Norma 0, we don't want to flash the cards when we decrease
            super.stackPower(stackAmount);
            if (amount > 5) {
                amount = 5;
            }
            if (amount < 0) {
                amount = 0;
            }
            name = NAME + " " + amount;
            updateDescription();

            if(flashCardsGreen){
                for (AbstractCard c : AbstractDungeon.player.hand.group) {
                    if (c instanceof AbstractNormaAttentiveCard) {
                        if (((AbstractNormaAttentiveCard) c).normaLevels.contains(amount) || ((AbstractNormaAttentiveCard) c).normaLevels.contains(-1)){
                            c.flash(Color.GREEN);
                        }
                    }
                }
            }
            if(flashCardsRed){
                for (AbstractCard c : AbstractDungeon.player.hand.group) {
                    if (c instanceof AbstractNormaAttentiveCard) {
                        if (((AbstractNormaAttentiveCard) c).normaLevels.contains(amount+1) || ((AbstractNormaAttentiveCard) c).normaLevels.contains(-1)){
                            c.flash(Color.RED);
                        }
                    }
                }
            }
        }
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if (!broken) {
            description = DESCRIPTIONS[0];
        } else {
            //description = DESCRIPTIONS[1];
            description = "#r"+getRandomString(AbstractDungeon.cardRandomRng.random(18, 18));
        }

    }

    @Override
    public AbstractPower makeCopy() {
        return new NormaPower(owner, amount, broken);
    }
}
