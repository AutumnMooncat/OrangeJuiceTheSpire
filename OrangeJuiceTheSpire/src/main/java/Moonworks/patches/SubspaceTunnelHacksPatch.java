package Moonworks.patches;

import Moonworks.cards.SubspaceTunnel;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

// Thank you Alchyr#3696
@SpirePatch(
        clz = UseCardAction.class, //This is the class you're patching.
        method = "update" //This is the name of the method of that class that you're patching.
)
public class SubspaceTunnelHacksPatch
{
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static SpireReturn<?> UseCardActionPatch(UseCardAction __instance, AbstractCard ___targetCard, float ___duration)
    {
        if (___targetCard instanceof SubspaceTunnel)
        {
            SubspaceTunnel card = (SubspaceTunnel) ___targetCard;

            if (!card.success)
            {
                return SpireReturn.Continue();
            }

            ReflectionHacks.setPrivate(__instance, AbstractGameAction.class, "duration", ___duration - Gdx.graphics.getDeltaTime());

            AbstractDungeon.player.cardInUse = null;
            ___targetCard.exhaustOnUseOnce = false;
            ___targetCard.dontTriggerOnUseCard = false;
            AbstractDungeon.actionManager.addToBottom(new HandCheckAction());

            return SpireReturn.Return(null);
        }

        return SpireReturn.Continue();
    }


    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "purgeOnUse");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}