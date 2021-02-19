package Moonworks.relics;

import Moonworks.OrangeJuiceMod;
import Moonworks.util.TextureLoader;
import Moonworks.util.interfaces.NormaAttentiveObject;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static Moonworks.OrangeJuiceMod.makeRelicOutlinePath;
import static Moonworks.OrangeJuiceMod.makeRelicPath;

public class HealPanel extends CustomRelic implements NormaAttentiveObject {

    private static final int DRAW = 1;

    // ID, images, text.
    public static final String ID = OrangeJuiceMod.makeID("HealPanel");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("HealPanel.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("HealPanel.png"));

    public HealPanel() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.HEAVY);
    }

    // All functionality is handled in LeapThroughSpaceMarking

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onGainNorma(int current, int increasedBy) {
        this.addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, DRAW));
        flash();
    }

    @Override
    public void onGainNormaCharge(int current, int increasedBy) {}
}