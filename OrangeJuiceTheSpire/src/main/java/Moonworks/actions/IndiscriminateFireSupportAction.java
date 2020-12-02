package Moonworks.actions;

import Moonworks.powers.FreeCardPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class IndiscriminateFireSupportAction extends AbstractGameAction {
    public int[] multiDamage;
    private final boolean freeToPlayOnce;
    private final DamageInfo.DamageType damageType;
    private final AbstractPlayer p;
    private final int energyOnUse;
    private final int dazed;
    private final AttackEffect attackEffect;

    public IndiscriminateFireSupportAction(AbstractPlayer p, int[] multiDamage, DamageInfo.DamageType damageType, int dazed, AttackEffect attackEffect, boolean freeToPlayOnce, int energyOnUse) {
        this.multiDamage = multiDamage;
        this.damageType = damageType;
        this.p = p;
        this.freeToPlayOnce = freeToPlayOnce;
        this.dazed = dazed;
        this.attackEffect = attackEffect;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
    }

    public void update() {
        int currentE = EnergyPanel.totalCount;
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        if (this.p.hasRelic("Chemical X")) {
            effect += 2;
            this.p.getRelic("Chemical X").flash();
        }

        if (effect > 0) {
            for(int i = 0; i < effect; ++i) {
                this.addToBot(new DamageAllEnemiesAction(this.p, this.multiDamage, this.damageType, attackEffect, true));
            }

            if (!this.freeToPlayOnce) {
                this.p.energy.use(EnergyPanel.totalCount);
            }
            if(this.p.hasPower(FreeCardPower.POWER_ID)) {
                AbstractPower pow = this.p.getPower(FreeCardPower.POWER_ID);
                this.addToTop(new GainEnergyAction(currentE));
                pow.flash();
                pow.amount--;
                if (pow.amount == 0) {
                    this.addToTop(new RemoveSpecificPowerAction(pow.owner, pow.owner, pow.ID));
                }
            }

            this.addToBot(new MakeTempCardInDrawPileAction(new Dazed(), dazed, true, true));
        }

        this.isDone = true;
    }
}