package Moonworks.relics;

import Moonworks.RandomChatterHelper;
import Moonworks.util.interfaces.NormaAttentiveObject;
import Moonworks.util.NormaHelper;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import Moonworks.OrangeJuiceMod;
import Moonworks.util.TextureLoader;

import static Moonworks.OrangeJuiceMod.*;

public class Homemark extends CustomRelic implements ClickableRelic, NormaAttentiveObject { // You must implement things you want to use from StSlib
    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     * StSLib for Clickable Relics
     *
     * Usable once per turn. Right click: Evoke your rightmost orb.
     */

    // ID, images, text.
    public static final String ID = OrangeJuiceMod.makeID("Homemark");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Homemark.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Homemark.png"));

    private static final int COST = 120;

    public Homemark() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.CLINK);
        if (AbstractDungeon.player != null) {
            this.counter = NormaHelper.getBaseNorma(AbstractDungeon.player);
        }
    }


    @Override
    public void onRightClick() {// On right click
        //If we are not in combat...
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            AbstractPlayer p = AbstractDungeon.player;
            //If we havent hit the max and we have enough money
            if (NormaHelper.canUpgradeBase(p) && canAfford()) {
                //Increment base norma
                NormaHelper.upgradeBase(p);
                //Spend gold
                p.loseGold(COST);
                //Say stuff
                RandomChatterHelper.showChatter(RandomChatterHelper.getHomemarkText(), preTalkProbability, enablePreBattleTalkEffect);
                //Grab updated description
                this.description = this.getUpdatedDescription();
                //Clear tips and reapply them so it uses our updated number
                this.tips.clear();
                this.tips.add(new PowerTip(this.name, this.description));
                this.initializeTips();
            }
        }
    }

    private boolean canAfford() {
        return AbstractDungeon.player.gold >= COST;
    }

    private boolean canUpgrade() {
        return NormaHelper.canUpgradeBase(AbstractDungeon.player);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        //Pulse if norma can be upgraded and we can afford it
        if (!pulse && canAfford() && canUpgrade()) {
            beginLongPulse();
        }
    }

    @Override
    public void onGainGold() {
        super.onGainGold();
        //Pulse if norma can be upgraded and we can afford it
        if (!pulse && canAfford() && canUpgrade()) {
            beginLongPulse();
        }
    }

    @Override
    public void onLoseGold() {
        super.onLoseGold();
        //Stop pulsing if we cant afford it
        if(!canAfford()) {
            stopPulse();
        }
    }

    @Override
    public void onSpendGold() {
        super.onSpendGold();
        //Stop pulsing if we cant afford it
        if(!canAfford()) {
            stopPulse();
        }
    }

    @Override
    public void atBattleStartPreDraw() {
        //Stop flashing when combat starts
        stopPulse();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        int i = 0;
        if (AbstractDungeon.player != null) {
            i = NormaHelper.getBaseNorma(AbstractDungeon.player);
        }
        this.counter = i;
        return DESCRIPTIONS[0] + i + DESCRIPTIONS[1] + COST + DESCRIPTIONS[2];
    }

    @Override
    public void onGainNorma(int normaLevel, int increasedBy) {
        //Grab new counter value
        this.counter = NormaHelper.getBaseNorma(AbstractDungeon.player);
        //Don't pulse if it cant go any higher
        if(!NormaHelper.canUpgradeBase(AbstractDungeon.player)){
            stopPulse();
        }
    }

    @Override
    public void onGainNormaCharge(int numerator, int increasedBy) {}
}
