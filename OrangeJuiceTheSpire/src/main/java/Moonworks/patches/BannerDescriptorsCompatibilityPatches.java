package Moonworks.patches;

import Moonworks.cards.abstractCards.AbstractMagicalCard;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/*
public class BannerDescriptorsCompatibilityPatches {

    @SpirePatch(
            clz = AbstractCard.class,
            method = "renderDynamicFrame"
    )
    public static class FixDynamicFrame {
        private static final Vector2 tmp = new Vector2(0.0F, 0.0F);

        public FixDynamicFrame() {
        }

        public static SpireReturn<Void> Prefix(AbstractCard __instance, SpriteBatch sb, float x, float y, float typeOffset, float typeWidth) {
            if (__instance instanceof AbstractMagicalCard) {
                try {
                    Method renderHelperMethod = AbstractCard.class.getDeclaredMethod("dynamicFrameRenderHelper", SpriteBatch.class, TextureAtlas.AtlasRegion.class, float.class, float.class, float.class, float.class);
                    renderHelperMethod.setAccessible(true);
                    switch(((AbstractMagicalCard) __instance).displayRarity) {
                        case BASIC:
                        case CURSE:
                        case SPECIAL:
                        case COMMON:
                            renderHelperMethod.invoke(__instance, sb, ImageMaster.CARD_COMMON_FRAME_MID, x, y, 0.0F, typeWidth);
                            renderHelperMethod.invoke(__instance, sb, ImageMaster.CARD_COMMON_FRAME_LEFT, x, y, -typeOffset, 1.0F);
                            renderHelperMethod.invoke(__instance, sb, ImageMaster.CARD_COMMON_FRAME_RIGHT, x, y, typeOffset, 1.0F);
                            break;
                        case UNCOMMON:
                            renderHelperMethod.invoke(__instance, sb, ImageMaster.CARD_UNCOMMON_FRAME_MID, x, y, 0.0F, typeWidth);
                            renderHelperMethod.invoke(__instance, sb, ImageMaster.CARD_UNCOMMON_FRAME_LEFT, x, y, -typeOffset, 1.0F);
                            renderHelperMethod.invoke(__instance, sb, ImageMaster.CARD_UNCOMMON_FRAME_RIGHT, x, y, typeOffset, 1.0F);
                            break;
                        case RARE:
                            renderHelperMethod.invoke(__instance, sb, ImageMaster.CARD_RARE_FRAME_MID, x, y, 0.0F, typeWidth);
                            renderHelperMethod.invoke(__instance, sb, ImageMaster.CARD_RARE_FRAME_LEFT, x, y, -typeOffset, 1.0F);
                            renderHelperMethod.invoke(__instance, sb, ImageMaster.CARD_RARE_FRAME_RIGHT, x, y, typeOffset, 1.0F);
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return SpireReturn.Return(null);
            } else {
                return SpireReturn.Continue();
            }
        }
    }
}*/
