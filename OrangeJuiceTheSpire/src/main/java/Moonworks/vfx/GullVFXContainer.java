package Moonworks.vfx;

import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class GullVFXContainer {
    //Gull Cloud
    public static void cloudVFX(int clouds) {
        for (int j = 0 ; j < clouds ; j++) {
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                public void update() {
                    for (int i = 0 ; i < AbstractDungeon.cardRandomRng.random(15, 20) ; i++) {
                        float startX = MathUtils.random((float) Settings.WIDTH * 0.1F, (float)Settings.WIDTH * 0.9F);
                        int angleCorrect = startX > Settings.WIDTH * 0.5F ? 0 : 90;
                        boolean flip = startX < Settings.WIDTH * 0.5F;
                        AbstractGameEffect cloudGull = new VfxBuilder(flip ? VFXContainer.GULL_ATTACK_TEXTURE : VFXContainer.GULL_ATTACK_TEXTURE_FLIP, startX, Settings.HEIGHT, 1.8f)
                                .setScale(MathUtils.random(0.17f,0.23f))
                                .gravity(-50f)
                                .velocity(MathUtils.random(180f, 270f)+angleCorrect, MathUtils.random(800f, 1200f))
                                .rotate(MathUtils.random(10f, 40f) * (flip ? -1 : 1))
                                .build();
                        AbstractDungeon.effectList.add(cloudGull);
                    }
                    this.isDone = true;
                }
            });
        }
    }

    //Gull Cloud Attack
    public static void diveAttackVFX(AbstractCreature t, boolean hasThorns) {
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            public void update() {
                float startX = MathUtils.random((float) Settings.WIDTH * 0.25F, (float)Settings.WIDTH * 0.75F);
                boolean flip = startX > t.drawX;
                //Normal attack
                AbstractGameEffect hurtGull = new VfxBuilder(flip ? VFXContainer.GULL_HURT_TEXTURE : VFXContainer.GULL_HURT_TEXTURE_FLIP, t.drawX, t.drawY, 1.5f)
                        .setScale(0.2f)
                        //.moveX(t.drawX, t.drawX + (flip ? Settings.WIDTH * 0.05F : -Settings.WIDTH * 0.05F), VfxBuilder.Interpolations.LINEAR)
                        //.moveY(t.drawY, Settings.HEIGHT/3f, VfxBuilder.Interpolations.CIRCLE)
                        .gravity(50f)
                        .velocity(MathUtils.random(45f, 135f), MathUtils.random(600f, 800f))
                        .rotate(MathUtils.random(50f, 200f) * (MathUtils.randomBoolean() ? -1 : 1))
                        .build();
                AbstractGameEffect shootGull = new VfxBuilder(flip ? VFXContainer.GULL_ATTACK_TEXTURE : VFXContainer.GULL_ATTACK_TEXTURE_FLIP, startX, Settings.HEIGHT, 0.4f)
                        .scale(0.2f, 0.25f, VfxBuilder.Interpolations.SWINGIN)
                        .moveX(startX, t.drawX)
                        .moveY(Settings.HEIGHT, t.drawY)
                        .rotate(MathUtils.random(50f, 200f) * (flip ? -1 : 1))
                        .build();
                AbstractGameEffect shootGullAtThorns = new VfxBuilder(flip ? VFXContainer.GULL_ATTACK_TEXTURE : VFXContainer.GULL_ATTACK_TEXTURE_FLIP, startX, Settings.HEIGHT, 0.4f)
                        .scale(0.2f, 0.25f, VfxBuilder.Interpolations.SWINGIN)
                        .moveX(startX, t.drawX)
                        .moveY(Settings.HEIGHT, t.drawY)
                        .rotate(MathUtils.random(50f, 200f) * (flip ? -1 : 1))
                        .triggerVfxAt(0.4F, 1, (aFloat, aFloat2) -> hurtGull)
                        .build();
                AbstractDungeon.effectList.add(hasThorns ? shootGullAtThorns : shootGull);
                //AbstractDungeon.effectList.add(hurtGull);
                this.isDone = true;
            }
        });
    }

    //Gull Rush Attack
    public static void rushAttackVFX (AbstractCreature t, boolean hasThorns, boolean heavySFX, int gullThrows) {
        for (int gulls = 0 ; gulls < gullThrows ; gulls++) {
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                public void update() {
                    //Gull Attack
                    float startX, startY = Settings.HEIGHT;
                    startX = MathUtils.random((float) Settings.WIDTH * 0.0F, (float)Settings.WIDTH * 1.0F);
                    /*if (AbstractDungeon.cardRandomRng.random(0, 1) == 0) {
                        startX = MathUtils.random((float) Settings.WIDTH * 0.0F, (float)Settings.WIDTH * 1.0F);
                    } else {
                        startY = MathUtils.random((float) Settings.HEIGHT * 0.8F, (float)Settings.HEIGHT * 1.0F);
                    }*/
                    float m = (t.drawY - startY) / (t.drawX - startX);
                    float b = startY - (m * startX);
                    float yIntercept = m * Settings.WIDTH + b;
                    float xIntercept = -b / m;
                    float targetX, targetY;
                    if (xIntercept > Settings.WIDTH) {
                        // Y intercept happens first
                        targetX = Settings.WIDTH;
                        targetY = yIntercept;
                    } else {
                        targetX = xIntercept;
                        targetY = 0;
                    }
                    boolean flip = startX > t.drawX;

                    //Normal attack
                    AbstractGameEffect hurtGull = new VfxBuilder(VFXContainer.GULL_HURT_TEXTURE, t.drawX, t.drawY, 1.5f)
                            .setScale(0.22f)
                            //.moveX(t.drawX, t.drawX + (flip ? Settings.WIDTH * 0.05F : -Settings.WIDTH * 0.05F), VfxBuilder.Interpolations.LINEAR)
                            //.moveY(t.drawY, Settings.HEIGHT/3f, VfxBuilder.Interpolations.CIRCLE)
                            .gravity(50f)
                            .velocity(MathUtils.random(45f, 135f), MathUtils.random(600f, 800f))
                            .rotate(MathUtils.random(50f, 200f) * (MathUtils.randomBoolean() ? -1 : 1))
                            .build();
                    AbstractGameEffect shootGull = new VfxBuilder(flip ? VFXContainer.GULL_ATTACK_TEXTURE_FLIP : VFXContainer.GULL_ATTACK_TEXTURE, startX, startY, 0.5f)
                            .setScale(0.22f)
                            .moveX(startX, targetX, VfxBuilder.Interpolations.EXP5IN)
                            .moveY(startY, targetY, VfxBuilder.Interpolations.EXP5IN)
                            .rotate(MathUtils.random(50f, 200f) * (flip ? -1 : 1))
                            .playSoundAt(0.48f, heavySFX ? "BLUNT_HEAVY" : "BLUNT_FAST")
                            .build();
                    AbstractGameEffect shootGullAtThorns = new VfxBuilder(flip ? VFXContainer.GULL_ATTACK_TEXTURE_FLIP : VFXContainer.GULL_ATTACK_TEXTURE, startX, startY, 0.5f)
                            .setScale(0.22f)
                            .moveX(startX, t.drawX, VfxBuilder.Interpolations.EXP5IN)
                            .moveY(startY, t.drawY, VfxBuilder.Interpolations.EXP5IN)
                            .rotate(MathUtils.random(50f, 200f) * (flip ? -1 : 1))
                            .playSoundAt(0.48f, "ATTACK_FAST")
                            .triggerVfxAt(0.5F, 1, (aFloat, aFloat2) -> hurtGull)
                            .build();
                    AbstractDungeon.effectList.add(hasThorns ? shootGullAtThorns : shootGull);
                    this.addToTop(new WaitAction(0.015F));
                    //AbstractDungeon.effectList.add(hurtGull);
                    this.isDone = true;
                }
            });
        }
    }
}
