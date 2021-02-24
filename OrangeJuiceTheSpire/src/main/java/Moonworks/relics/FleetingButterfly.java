package Moonworks.relics;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.ApplyAndUpdateMemoriesAction;
import Moonworks.util.TextureLoader;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CtBehavior;

import static Moonworks.OrangeJuiceMod.makeRelicOutlinePath;
import static Moonworks.OrangeJuiceMod.makeRelicPath;
import static Moonworks.powers.BookOfMemoriesPower.getViability;

public class FleetingButterfly extends CustomRelic {

    // ID, images, text.
    public static final String ID = OrangeJuiceMod.makeID("FleetingButterfly");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("FleetingButterfly.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("FleetingButterfly.png"));

    public boolean triggeredThisCombat = false;

    public FleetingButterfly() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    public void atBattleStart() {
        this.triggeredThisCombat = false;
        this.beginLongPulse();
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (!triggeredThisCombat) {
            //Grab player for reference
            AbstractPlayer p = AbstractDungeon.player;
            //Flash Relic
            flash();
            //Stop Pulsing
            stopPulse();
            //Show Relic above player
            this.addToBot(new RelicAboveCreatureAction(p, this));
            //Remainder is handled by the patch
        }
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @SpirePatch(
            clz = UseCardAction.class,
            method = "update"
    )
    public static class UseCardActionHack
    {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static SpireReturn<?> UseCardActionPatch(UseCardAction __instance, AbstractCard ___targetCard, float ___duration)
        {
            AbstractRelic rel = AbstractDungeon.player.getRelic(FleetingButterfly.ID);
            if (rel instanceof FleetingButterfly && !((FleetingButterfly) rel).triggeredThisCombat) {
                ((FleetingButterfly) rel).triggeredThisCombat = true;
                if (!getViability(___targetCard))
                {
                    return SpireReturn.Continue();
                }

                ReflectionHacks.setPrivate(__instance, AbstractGameAction.class, "duration", ___duration - Gdx.graphics.getDeltaTime());
                AbstractDungeon.player.cardInUse = null;
                ___targetCard.exhaustOnUseOnce = false;
                ___targetCard.dontTriggerOnUseCard = false;
                AbstractDungeon.actionManager.addToBottom(new HandCheckAction());
                AbstractDungeon.actionManager.addToBottom(new ApplyAndUpdateMemoriesAction(___targetCard));
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

}
