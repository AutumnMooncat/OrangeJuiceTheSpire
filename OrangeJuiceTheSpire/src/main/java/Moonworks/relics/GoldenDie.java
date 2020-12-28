package Moonworks.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import Moonworks.OrangeJuiceMod;
import Moonworks.util.TextureLoader;

import static Moonworks.OrangeJuiceMod.makeRelicOutlinePath;
import static Moonworks.OrangeJuiceMod.makeRelicPath;

public class GoldenDie extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = OrangeJuiceMod.makeID("GoldenDie");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("GoldenDie.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("GoldenDie.png"));

    public GoldenDie() {
        super(ID, IMG, OUTLINE, RelicTier.SHOP, LandingSound.FLAT);
    }

    // All functionality for this is handled in AbstractGiftCard

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
