package Moonworks.relics;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.uniqueCards.LulusLuckiestEgg;
import Moonworks.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static Moonworks.OrangeJuiceMod.makeRelicOutlinePath;
import static Moonworks.OrangeJuiceMod.makeRelicPath;

//TODO center image, perhaps larger as well
public class TrulyGoldenEgg extends CustomRelic {

    // ID, images, text.
    public static final String ID = OrangeJuiceMod.makeID("TrulyGoldenEgg");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("TrulyGoldenEgg.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("TrulyGoldenEgg.png"));

    public TrulyGoldenEgg() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.SOLID);
    }

    @Override
    public void onEquip() {
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new LulusLuckiestEgg(), (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Moonworks.cards.uniqueCards.TrulyGoldenEgg(), (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
