package Moonworks.cards.interfaces;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.magicalCards.MagicalMassacre;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.FireBurstParticleEffect;

public interface RevengeAttack {

    void applyRevengePower();

    class RevengeVFXContainer {
        public static void flashVFX(AbstractCard card) {
            AbstractGameEffect effect = new RevengeVFXEffect(card.current_x, card.current_y);
            AbstractDungeon.effectsQueue.add(effect);
        }
    }

    class RevengePower extends AbstractPower implements CloneablePowerInterface, InvisiblePower {

        public static AbstractCreature lastAttacker;
        public static final String NAME = RevengePower.class.getName();
        public static final String POWER_ID = OrangeJuiceMod.makeID(RevengePower.class.getSimpleName());

        public RevengePower(AbstractCreature owner) {
            this.ID = POWER_ID;
            this.name = NAME;
            this.owner = owner;
        }

        @Override
        public int onAttacked(DamageInfo info, int damageAmount) {
            if (info.owner != owner) {
                lastAttacker = info.owner;
            }
            return super.onAttacked(info, damageAmount);
        }

        @Override
        public AbstractPower makeCopy() {
            return new RevengePower(owner);
        }
    }

    class RevengeVFXEffect extends AbstractGameEffect {
        private TextureAtlas.AtlasRegion img;
        private static final float DUR = 1.0F;
        private float x;
        private float y;
        private float vX;
        private float vY;
        private float floor;
        private static final float GRAVITY;

        public RevengeVFXEffect(float x, float y) {
            int roll = MathUtils.random(0, 2);
            if (roll == 0) {
                this.img = ImageMaster.TORCH_FIRE_1;
            } else if (roll == 1) {
                this.img = ImageMaster.TORCH_FIRE_2;
            } else {
                this.img = ImageMaster.TORCH_FIRE_3;
            }

            float scaleFactor = MathUtils.random(3.0F, 4.0F);

            this.duration = MathUtils.random(0.5F, 1.0F);
            this.x = x - (float)(this.img.packedWidth / 2);
            this.y = y - (float)(this.img.packedHeight / 2);
            //this.color = new Color(MathUtils.random(0.1F, 0.3F), MathUtils.random(0.8F, 1.0F), MathUtils.random(0.1F, 0.3F), 0.0F);
            this.color = Color.RED.cpy();
            this.color.a = 0.0F;
            this.rotation = MathUtils.random(-10.0F, 10.0F);
            this.scale = MathUtils.random(2.0F, 4.0F) * Settings.scale * scaleFactor;
            this.vX = MathUtils.random(-900.0F, 900.0F) * Settings.scale;
            this.vY = MathUtils.random(0.0F, 500.0F) * Settings.scale;
            this.floor = MathUtils.random(100.0F, 250.0F) * Settings.scale;
        }

        public void update() {
            this.vY += GRAVITY / this.scale * Gdx.graphics.getDeltaTime();
            this.x += this.vX * Gdx.graphics.getDeltaTime() * MathUtils.sinDeg(Gdx.graphics.getDeltaTime());
            this.y += this.vY * Gdx.graphics.getDeltaTime();
            if (this.scale > 0.3F * Settings.scale) {
                this.scale -= Gdx.graphics.getDeltaTime() * 2.0F;
            }

            if (this.y < this.floor) {
                this.vY = -this.vY * 0.75F;
                this.y = this.floor + 0.1F;
                this.vX *= 1.1F;
            }

            if (1.0F - this.duration < 0.1F) {
                this.color.a = Interpolation.fade.apply(0.0F, 1.0F, (1.0F - this.duration) * 10.0F);
            } else {
                this.color.a = Interpolation.pow2Out.apply(0.0F, 1.0F, this.duration);
            }

            this.duration -= Gdx.graphics.getDeltaTime();
            if (this.duration < 0.0F) {
                this.isDone = true;
            }

        }

        public void render(SpriteBatch sb) {
            sb.setBlendFunction(770, 1);
            sb.setColor(this.color);
            sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale, this.scale, this.rotation);
            sb.setBlendFunction(770, 771);
        }

        public void dispose() {
        }

        static {
            GRAVITY = 180.0F * Settings.scale;
        }
    }
}
