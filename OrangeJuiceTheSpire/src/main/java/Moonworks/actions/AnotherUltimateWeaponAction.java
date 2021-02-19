package Moonworks.actions;

import Moonworks.powers.FreeCardPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
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
    private final int block, strength;
    private final boolean upgraded;

    public AnotherUltimateWeaponAction(AbstractPlayer p, int strength, int block, boolean upgraded, boolean freeToPlayOnce, int energyOnUse) {
        this.p = p;
        this.freeToPlayOnce = freeToPlayOnce;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
        this.strength = strength;
        this.block = block;
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
        if (effect+strength > 0) {
            this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, effect+strength)));
        }
        if (effect+block > 0) {
            this.addToBot(new GainBlockAction(p, 5*effect+block));
        }
        if (effect > 0 || strength > 0 || block > 0) {

            if (!this.freeToPlayOnce) {
                this.p.energy.use(EnergyPanel.totalCount);
            }
        }

        this.isDone = true;
    }
}
