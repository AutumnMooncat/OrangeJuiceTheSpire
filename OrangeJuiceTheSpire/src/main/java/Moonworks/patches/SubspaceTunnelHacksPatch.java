package Moonworks.patches;
/*
import Moonworks.cards.SubspaceTunnel;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;

import java.util.Iterator;

@SpirePatch(
        clz = UseCardAction.class, //This is the class you're patching.
        method = "update" //This is the name of the method of that class that you're patching.
)
public class SubspaceTunnelHacksPatch
{
    @SpirePrefixPatch //This means this is going to be added at the "start" of the thing you're patching.
    public static SpireReturn<?> UseCardActionPatch(UseCardAction __instance) //Patches receive both the instance (when the method is called, the AbstractCreature it's being called on) and any parameters of the method being patched (in this case, the block amount.)
    {
        if ((float)ReflectionHacks.getPrivateInherited(__instance, __instance.getClass(), "duration") == 0.15F) {
            if (ReflectionHacks.getPrivate(__instance, __instance.getClass(), "targetCard") instanceof SubspaceTunnel) {
                SubspaceTunnel card = ReflectionHacks.getPrivate(__instance, __instance.getClass(), "targetCard");

                //Preamble stuff, copied directly from decompiled code
                Iterator var1 = AbstractDungeon.player.powers.iterator();
                while(var1.hasNext()) {
                    AbstractPower p = (AbstractPower)var1.next();
                    if (!card.dontTriggerOnUseCard) {
                        p.onAfterUseCard(card, __instance);
                    }
                }

                var1 = AbstractDungeon.getMonsters().monsters.iterator();

                while(var1.hasNext()) {
                    AbstractMonster m = (AbstractMonster)var1.next();
                    Iterator var3 = m.powers.iterator();
                    while(var3.hasNext()) {
                        AbstractPower p = (AbstractPower)var3.next();
                        if (!card.dontTriggerOnUseCard) {
                            p.onAfterUseCard(card, __instance);
                        }
                    }
                }
                //Done preamble, actually do stuff now

                //Reset all the stuff
                if (AbstractDungeon.player.hoveredCard == card) {
                    AbstractDungeon.player.releaseCard();
                }
                card.unhover();
                card.untip();
                card.stopGlowing();

                //Actually move it now
                if (card.targetGroup == AbstractDungeon.player.drawPile) {
                    card.shrink();
                    card.darken(false);
                    AbstractDungeon.getCurrRoom().souls.onToDeck(card, true, true);
                } else if (card.targetGroup == AbstractDungeon.player.discardPile) {
                    card.shrink();
                    card.darken(false);
                    AbstractDungeon.getCurrRoom().souls.discard(card, true);
                    //AbstractDungeon.player.onCardDrawOrDiscard();
                } else if (card.targetGroup == AbstractDungeon.player.exhaustPile) {
                    AbstractDungeon.effectList.add(new ExhaustCardEffect(card));
                    //AbstractDungeon.player.onCardDrawOrDiscard();
                } else {
                    //Something wrong happened, just continue on as usual
                    return SpireReturn.Continue();
                }
                AbstractDungeon.player.onCardDrawOrDiscard();
                __instance.isDone = true;
                return SpireReturn.Return(null);
            }
        }
        return SpireReturn.Continue();
    }
}*/

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