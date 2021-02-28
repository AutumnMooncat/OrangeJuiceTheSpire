package Moonworks.cards.interfaces;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public interface OnDealDamageCard {
    int onDealDamage(AbstractMonster m, DamageInfo info, int damageAmount);
}
