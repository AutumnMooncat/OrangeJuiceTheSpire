package Moonworks.relics;

import Moonworks.cards.CloudOfSeagulls;
import Moonworks.cards.JonathanRush;
import Moonworks.vfx.VFXContainer;
import basemod.abstracts.CustomRelic;
import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import Moonworks.OrangeJuiceMod;
import Moonworks.util.TextureLoader;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import static Moonworks.OrangeJuiceMod.*;

public class LittleGull extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = OrangeJuiceMod.makeID("LittleGull");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("LittleGull.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("LittleGull.png"));

    public LittleGull() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    public void atTurnStart() {
        doGullDamage();
    }

    public void doGullDamage() {
        int DMG = AbstractDungeon.cardRandomRng.random(2, 7);
        AbstractCreature t = AbstractDungeon.getRandomMonster();
        this.flash();
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));

        if (!disableGullVfx) {
            AbstractGameEffect shootGull = new VfxBuilder(VFXContainer.GULL_ATTACK_TEXTURE, 0f, Settings.HEIGHT, 0.5f)
                    .setScale(0.22f)
                    .moveX(0f, t.drawX, VfxBuilder.Interpolations.EXP5IN)
                    .moveY(Settings.HEIGHT, t.drawY, VfxBuilder.Interpolations.EXP5IN)
                    .rotate(MathUtils.random(50f, 200f))
                    //.playSoundAt(0.48f, DMG == 7 ? "BLUNT_HEAVY" : "BLUNT_FAST")
                    .build();

            AbstractDungeon.effectList.add(shootGull);
            //this.addToBot(new WaitAction(0.2F));
        }

        this.addToTop(new DamageAction(t, new DamageInfo(AbstractDungeon.player, DMG, DamageInfo.DamageType.THORNS),
                DMG == 7 ? AbstractGameAction.AttackEffect.BLUNT_HEAVY : AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        this.addToTop(new WaitAction(0.2F));
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
