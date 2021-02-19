package Moonworks.relics;

import Moonworks.OrangeJuiceMod;
import Moonworks.util.TextureLoader;
import Moonworks.util.interfaces.NormaAttentiveObject;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;

import static Moonworks.OrangeJuiceMod.makeRelicOutlinePath;
import static Moonworks.OrangeJuiceMod.makeRelicPath;

public class DrawPanel extends CustomRelic implements NormaAttentiveObject {

    // ID, images, text.
    public static final String ID = OrangeJuiceMod.makeID("DrawPanel");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("DrawPanel.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("DrawPanel.png"));

    public DrawPanel() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    // All functionality is handled in LeapThroughSpaceMarking

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onGainNorma(int current, int increasedBy) {
        this.addToBot(new DrawCardAction(1));
        flash();
    }

    @Override
    public void onGainNormaCharge(int current, int increasedBy) {}
}