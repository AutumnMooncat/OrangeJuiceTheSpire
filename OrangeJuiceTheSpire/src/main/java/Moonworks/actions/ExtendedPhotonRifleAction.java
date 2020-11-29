package Moonworks.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class ExtendedPhotonRifleAction extends AbstractGameAction {
    private final boolean freeToPlayOnce;
    private final AbstractPlayer p;
    private final int energyOnUse;
    private final int hits;
    private final DamageInfo info;

    public ExtendedPhotonRifleAction(AbstractPlayer p, AbstractMonster m, final DamageInfo info, int hits, boolean freeToPlayOnce, int energyOnUse) {
        this.p = p;
        this.target = m;
        this.hits = hits;
        this.info = info;
        this.freeToPlayOnce = freeToPlayOnce;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
    }

    public void update() {
        int effect = hits * EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = hits * this.energyOnUse;
        }
        if (this.p.hasRelic("Chemical X")) {
            effect += 2 * hits;
            this.p.getRelic("Chemical X").flash();
        }
        if (effect > 0) {

            for (int i = 0; i < effect; i++) {
                if (this.target != null) {
                    //Add a delay, for the love of god
                    //AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.FIRE));
                    //this.target.damage(this.info);
                    this.addToBot(new DamageAction(this.target, this.info, AttackEffect.BLUNT_LIGHT));
                }
            }

            if (!this.freeToPlayOnce) {
                this.p.energy.use(EnergyPanel.totalCount);
            }
        }

        this.isDone = true;
    }
}
