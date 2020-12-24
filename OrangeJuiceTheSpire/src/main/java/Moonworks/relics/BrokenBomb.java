package Moonworks.relics;

import Moonworks.cards.tempCards.StarBlastingLight;
import Moonworks.powers.NormaPower;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import Moonworks.OrangeJuiceMod;
import Moonworks.util.TextureLoader;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import static Moonworks.OrangeJuiceMod.makeRelicOutlinePath;
import static Moonworks.OrangeJuiceMod.makeRelicPath;

public class BrokenBomb extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = OrangeJuiceMod.makeID("BrokenBomb");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("BrokenBomb.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("BrokenBomb.png"));

    public BrokenBomb() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.HEAVY);
        this.counter = 0;
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.SKILL) {
            ++this.counter;
            if (this.counter == 10) {
                AbstractCard starBlastingLight = new StarBlastingLight();
                this.counter = 0;
                this.flash();
                this.pulse = false;
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new NormaPower(AbstractDungeon.player, 1)));
                AbstractDungeon.actionManager.addToBottom(new TalkAction(true, DESCRIPTIONS[1], 4.0f, 2.0f));
                AbstractDungeon.player.hand.refreshHandLayout();
                if (AbstractDungeon.player.hasPower("MasterRealityPower")) {
                    starBlastingLight.upgrade();
                }
                if (AbstractDungeon.player.hand.size() < 10) {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(starBlastingLight.makeStatEquivalentCopy(), (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                } else {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDrawPileEffect(starBlastingLight.makeStatEquivalentCopy(), (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F, false, false));
                }
            } else if (this.counter == 9) {
                this.beginPulse();
                this.pulse = true;
            }
        }

    }

    public void atBattleStart() {
        if (this.counter == 9) {
            this.beginPulse();
            this.pulse = true;
        }

    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
