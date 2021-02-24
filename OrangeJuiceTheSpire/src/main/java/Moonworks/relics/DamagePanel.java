package Moonworks.relics;

import Moonworks.OrangeJuiceMod;
import Moonworks.util.TextureLoader;
import Moonworks.util.interfaces.NormaAttentiveObject;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static Moonworks.OrangeJuiceMod.makeRelicOutlinePath;
import static Moonworks.OrangeJuiceMod.makeRelicPath;

public class DamagePanel extends CustomRelic implements NormaAttentiveObject {

    private static final int DAMAGE = 7;

    // ID, images, text.
    public static final String ID = OrangeJuiceMod.makeID("DamagePanel");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("DamagePanel.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("DamagePanel.png"));

    public DamagePanel() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    // All functionality is handled in LeapThroughSpaceMarking

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onGainNorma(int normaLevel, int increasedBy) {
        this.addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, DAMAGE, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE));
        flash();
    }

    @Override
    public void onGainNormaCharge(int numerator, int increasedBy) {}
}