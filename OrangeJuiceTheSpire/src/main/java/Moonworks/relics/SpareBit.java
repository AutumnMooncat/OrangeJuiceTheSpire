package Moonworks.relics;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.DeployBits;
import Moonworks.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static Moonworks.OrangeJuiceMod.makeRelicOutlinePath;
import static Moonworks.OrangeJuiceMod.makeRelicPath;
//TODO bigger image
public class SpareBit extends CustomRelic {

    // ID, images, text.
    public static final String ID = OrangeJuiceMod.makeID("SpareBit");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("SpareBit.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("SpareBit.png"));

    public static final int BONUS = 2;

    public SpareBit() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.SOLID);
    }

    @Override
    public void onEquip() {
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new DeployBits(), (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new DeployBits(), (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));

    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
