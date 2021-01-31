package Moonworks.patches;

import Moonworks.cards.abstractCards.AbstractMagicalCard;
import Moonworks.cards.abstractCards.AbstractModdedCard;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.RenderCardDescriptors;
import basemod.patches.com.megacrit.cardcrawl.screens.SingleCardViewPopup.RenderCardDescriptorsSCV;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CtBehavior;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BannerDescriptorsCompatibilityPatches {

    /*@SpirePatch(
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
    }*/

    /*
    @SpirePatch(
            clz = AbstractCard.class,
            method = "renderType"
    )
    public static class OverrideTypeRender {

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"text"}
        )
        public static void Insert(AbstractCard __instance, SpriteBatch sb, @ByRef String[] text) {
            if (__instance instanceof AbstractModdedCard && ((AbstractModdedCard) __instance).getBaseDescriptor() != null) {
                text[0] = ((AbstractModdedCard) __instance).getBaseDescriptor();
            }
        }

        private static class Locator extends SpireInsertLocator {
            private Locator() {
            }

            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(FontHelper.class, "renderRotatedText");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }

    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "renderCardTypeText"
    )
    public static class OverrideTypeRenderSCV {

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"label"}
        )
        public static void Insert(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card, @ByRef String[] text) {
            if (___card instanceof AbstractModdedCard && ((AbstractModdedCard) ___card).getBaseDescriptor() != null) {
                text[0] = ((AbstractModdedCard) ___card).getBaseDescriptor();
            }
        }

        private static class Locator extends SpireInsertLocator {
            private Locator() {
            }

            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(FontHelper.class, "renderFontCentered");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }*/

    @SpirePatch(
            clz = RenderCardDescriptors.Text.class,
            method = "Insert"
    )
    public static class OverrideTypeRenderPatch {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void OverrideType(AbstractCard ___card, SpriteBatch sb, String[] text) {
            if (TypeOverridePatch.TypeOverrideField.typeOverride.get(___card) != null) {
                text[0] = TypeOverridePatch.TypeOverrideField.typeOverride.get(___card);
            }
            /*if (___card instanceof AbstractModdedCard && ((AbstractModdedCard) ___card).getBaseDescriptor() != null) {
                text[0] = ((AbstractModdedCard) ___card).getBaseDescriptor();
            }*/
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(List.class, "add");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }

    @SpirePatch(
            clz = RenderCardDescriptors.Frame.class,
            method = "Insert"
    )
    public static class OverrideTypeSizePatch {

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"typeText", "descriptors"}
        )
        public static void OverrideType(AbstractCard ___card, SpriteBatch sb, float x, float y, float[] tOffset, float[] tWidth, @ByRef String[] typeText, @ByRef List<String>[] descriptors) {
            if (TypeOverridePatch.TypeOverrideField.typeOverride.get(___card) != null) {
                typeText[0] = TypeOverridePatch.TypeOverrideField.typeOverride.get(___card);
                GlyphLayout gl = new GlyphLayout();
                FontHelper.cardTypeFont.getData().setScale(1.0F);
                gl.setText(FontHelper.cardTypeFont, typeText[0]);
                tOffset[0] = (gl.width - 38.0F * Settings.scale) / 2.0F;
                tWidth[0] = (gl.width - 0.0F) / (32.0F * Settings.scale);
            }

            /*if (___card instanceof AbstractModdedCard && ((AbstractModdedCard) ___card).getBaseDescriptor() != null) {
                typeText[0] = ((AbstractModdedCard) ___card).getBaseDescriptor();
                GlyphLayout gl = new GlyphLayout();
                FontHelper.cardTypeFont.getData().setScale(1.0F);
                gl.setText(FontHelper.cardTypeFont, typeText[0]);
                tOffset[0] = (gl.width - 38.0F * Settings.scale) / 2.0F;
                tWidth[0] = (gl.width - 0.0F) / (32.0F * Settings.scale);
            }*/
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(List.class, "add");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }

    @SpirePatch(
            clz = RenderCardDescriptorsSCV.Frame.class,
            method = "Insert"
    )
    public static class OverrideTypeSizePatchSCV {

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"typeText", "descriptors"}
        )
        public static void OverrideTypeSCV(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card, float[] tOffset, float[] tWidth, @ByRef String[] typeText, @ByRef List<String>[] descriptors) {
            if (TypeOverridePatch.TypeOverrideField.typeOverride.get(___card) != null) {
                typeText[0] = TypeOverridePatch.TypeOverrideField.typeOverride.get(___card);
                GlyphLayout gl = new GlyphLayout();
                FontHelper.panelNameFont.getData().setScale(1.0F);
                gl.setText(FontHelper.panelNameFont, typeText[0]);
                tOffset[0] = (gl.width - 70.0F * Settings.scale) / 2.0F;
                tWidth[0] = (gl.width - 0.0F) / (62.0F * Settings.scale);
            }

            /*if (___card instanceof AbstractModdedCard && ((AbstractModdedCard) ___card).getBaseDescriptor() != null) {
                typeText[0] = ((AbstractModdedCard) ___card).getBaseDescriptor();
                GlyphLayout gl = new GlyphLayout();
                FontHelper.panelNameFont.getData().setScale(1.0F);
                gl.setText(FontHelper.panelNameFont, typeText[0]);
                tOffset[0] = (gl.width - 70.0F * Settings.scale) / 2.0F;
                tWidth[0] = (gl.width - 0.0F) / (62.0F * Settings.scale);
            }*/
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(List.class, "add");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }
}
