package Moonworks.relics;

import Moonworks.OrangeJuiceMod;
import Moonworks.powers.BlastingLightPower;
import Moonworks.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static Moonworks.OrangeJuiceMod.makeRelicOutlinePath;
import static Moonworks.OrangeJuiceMod.makeRelicPath;

public class ScorchingSun extends CustomRelic {

    // ID, images, text.
    public static final String ID = OrangeJuiceMod.makeID("ScorchingSun");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("ScorchingSun.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("ScorchingSun.png"));

    public ScorchingSun() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.SOLID);
    }


    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount > 0 && target != AbstractDungeon.player && info.type == DamageInfo.DamageType.NORMAL) {
            this.flash();
            this.addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new BlastingLightPower(target, 1)));
        }
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
