package Moonworks.relics;

import Moonworks.vfx.GullVFXContainer;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import Moonworks.OrangeJuiceMod;
import Moonworks.util.TextureLoader;

import static Moonworks.OrangeJuiceMod.*;

public class LittleGull extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = OrangeJuiceMod.makeID("LittleGull");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("LittleGull.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("LittleGull.png"));

    public LittleGull() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    public void atTurnStart() {
        doGullDamage();
    }

    public void doGullDamage() {
        int DMG = AbstractDungeon.cardRandomRng.random(2, 7);
        AbstractCreature t = AbstractDungeon.getRandomMonster();
        if (t != null) {
            this.flash();
            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));

            if (!disableGullVfx) {
                //Ignore thorns in this case, since it wont matter
                GullVFXContainer.diveAttackVFX(t, false);
            }

            this.addToTop(new DamageAction(t, new DamageInfo(AbstractDungeon.player, DMG, DamageInfo.DamageType.THORNS),
                    DMG == 7 ? AbstractGameAction.AttackEffect.BLUNT_HEAVY : AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            this.addToTop(new WaitAction(0.2F));
        }
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
