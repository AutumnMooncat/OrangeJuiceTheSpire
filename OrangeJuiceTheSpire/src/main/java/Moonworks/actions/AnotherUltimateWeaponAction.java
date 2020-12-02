package Moonworks.actions;

import Moonworks.powers.FreeCardPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class AnotherUltimateWeaponAction extends AbstractGameAction {
    private final boolean freeToPlayOnce;
    private final AbstractPlayer p;
    private final int energyOnUse;
    private final boolean upgraded;

    public AnotherUltimateWeaponAction(AbstractPlayer p, boolean upgraded, boolean freeToPlayOnce, int energyOnUse) {
        this.p = p;
        this.freeToPlayOnce = freeToPlayOnce;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
        this.upgraded = upgraded;
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
        if (upgraded) {
            effect += 1;
        }
        if (effect > 0) {

            this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, effect)));
            this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, effect)));

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
        }

        this.isDone = true;
    }
}
