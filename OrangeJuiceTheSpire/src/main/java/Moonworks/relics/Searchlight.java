package Moonworks.relics;

import Moonworks.OrangeJuiceMod;
import Moonworks.powers.Heat300PercentPower;
import Moonworks.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Moonworks.OrangeJuiceMod.makeRelicOutlinePath;
import static Moonworks.OrangeJuiceMod.makeRelicPath;

public class Searchlight extends CustomRelic {

    // ID, images, text.
    public static final String ID = OrangeJuiceMod.makeID("Searchlight");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Searchlight.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Searchlight.png"));

    //TODO center image and maybe make it larger
    public Searchlight() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.SOLID);
    }

    @Override
    public void atBattleStart() {
        AbstractMonster target = AbstractDungeon.getRandomMonster();
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m.currentHealth > target.currentHealth) {
                target = m;
            }
        }
        this.addToBot(new RelicAboveCreatureAction(target, this));
        this.addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new Heat300PercentPower(target, AbstractDungeon.player,2)));
        super.atBattleStart();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
