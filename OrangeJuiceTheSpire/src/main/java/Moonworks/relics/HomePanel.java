package Moonworks.relics;

import Moonworks.OrangeJuiceMod;
import Moonworks.util.NormaHelper;
import Moonworks.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static Moonworks.OrangeJuiceMod.makeRelicOutlinePath;
import static Moonworks.OrangeJuiceMod.makeRelicPath;

public class HomePanel extends CustomRelic {

    private static final int REDUCTION = -2;

    // ID, images, text.
    public static final String ID = OrangeJuiceMod.makeID("HomePanel");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("HomePanel.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("HomePanel.png"));

    public HomePanel() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.HEAVY);
    }

    // All functionality is handled in LeapThroughSpaceMarking

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void obtain() {
        super.obtain();
        NormaHelper.modifyNormaDenominator(AbstractDungeon.player, REDUCTION);
    }

    /*@Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(HealPanel.ID)
                && AbstractDungeon.player.hasRelic(BonusPanel.ID)
                && AbstractDungeon.player.hasRelic(DrawPanel.ID)
                && AbstractDungeon.player.hasRelic(DamagePanel.ID);
    }*/
}