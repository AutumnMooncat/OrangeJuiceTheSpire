package Moonworks.cards.interfaces;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public interface OnEnemyBuffCard {
    void powerApplied(ApplyPowerAction action, AbstractCreature target, AbstractCreature source, AbstractPower pow, int amount, AbstractGameAction.AttackEffect effect);
}
