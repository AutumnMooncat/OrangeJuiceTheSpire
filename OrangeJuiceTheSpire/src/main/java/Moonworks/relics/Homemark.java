package Moonworks.relics;

import Moonworks.actions.NormaBreakAction;
import Moonworks.powers.NormaPower;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import Moonworks.OrangeJuiceMod;
import Moonworks.util.TextureLoader;

import static Moonworks.OrangeJuiceMod.makeRelicOutlinePath;
import static Moonworks.OrangeJuiceMod.makeRelicPath;

public class Homemark extends CustomRelic implements ClickableRelic { // You must implement things you want to use from StSlib
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
    public static boolean broken = false;

    public Homemark() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.CLINK);
    }


    @Override
    public void onRightClick() {// On right click
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            if (!broken && counter < 5 && AbstractDungeon.player.gold >= COST) {
                if(this.counter == -1){
                    this.counter = 0;
                }
                this.counter++;
                AbstractDungeon.player.loseGold(COST);
                this.description = this.getUpdatedDescription();
                this.tips.clear();
                this.tips.add(new PowerTip(this.name, this.description));
                this.initializeTips();
                if(this.counter == 5){
                    stopPulse();
                }
            }
        }
    }

    public void breakHomemark() {
        broken = true;
        this.counter = 6;
        this.description = this.getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
        beginPulse();
    }

    @Override
    public void onEquip() {
        super.onEquip();
        if (!pulse && AbstractDungeon.player.gold >= COST && counter < 5) {
            beginLongPulse();
        }
    }

    @Override
    public void onGainGold() {
        super.onGainGold();
        if (!pulse && AbstractDungeon.player.gold >= COST && counter < 5) {
            beginLongPulse();
        }
    }

    @Override
    public void onLoseGold() {
        super.onLoseGold();
        if(AbstractDungeon.player.gold < COST) {
            stopPulse();
        }
    }

    @Override
    public void onSpendGold() {
        super.onSpendGold();
        if(AbstractDungeon.player.gold < COST) {
            stopPulse();
        }
    }

    // Flash at the start of Battle.
    @Override
    public void atBattleStartPreDraw() {
        stopPulse();
        if(!broken) {
            if (counter > 0) {
                flash();
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new NormaPower(AbstractDungeon.player, counter)));
            }
        } else {
            this.addToBot(new NormaBreakAction(AbstractDungeon.player, false));
        }

    }

    // Description
    @Override
    public String getUpdatedDescription() {
        if (broken) {
            return "What have you done?";
        }
        return DESCRIPTIONS[0] + Math.max(0, counter) + DESCRIPTIONS[1] + COST + DESCRIPTIONS[2];
    }

}
