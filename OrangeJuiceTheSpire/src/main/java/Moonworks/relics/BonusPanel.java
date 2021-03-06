package Moonworks.relics;

import Moonworks.OrangeJuiceMod;
import Moonworks.util.TextureLoader;
import Moonworks.util.interfaces.NormaAttentiveObject;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;

import static Moonworks.OrangeJuiceMod.makeRelicOutlinePath;
import static Moonworks.OrangeJuiceMod.makeRelicPath;

public class BonusPanel extends CustomRelic implements NormaAttentiveObject {

    private static final int ENERGY = 1;

    // ID, images, text.
    public static final String ID = OrangeJuiceMod.makeID("BonusPanel");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("BonusPanel.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("BonusPanel.png"));

    public BonusPanel() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.HEAVY);
    }

    // All functionality is handled in LeapThroughSpaceMarking

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onGainNorma(int normaLevel, int increasedBy) {
        this.addToBot(new GainEnergyAction(ENERGY));
        flash();
    }

    @Override
    public void onGainNormaCharge(int numerator, int increasedBy) {}
}