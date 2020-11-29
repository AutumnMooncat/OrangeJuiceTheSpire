package Moonworks.actions;

import Moonworks.powers.InvisibleBombPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.TheBombPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import java.util.ArrayList;

public class WeAreWarudaAction extends AbstractGameAction {

    private DamageInfo info;
    private ArrayList<AbstractPower> debuffs = new ArrayList<>(); //This could be done with a Hashmap instead of 2 ArrayLists
    private ArrayList<Integer> stacks = new ArrayList<>();

    public WeAreWarudaAction(AbstractCreature target, AbstractCreature source) {
        this.actionType = ActionType.DAMAGE;
        this.target = target;
        this.source = source;
    }

    public void update() {
        debuffs.clear();
        stacks.clear();

        for (AbstractPower aP : AbstractDungeon.player.powers) {
            if (aP.type == AbstractPower.PowerType.DEBUFF || aP instanceof InvisibleBombPower || aP instanceof TheBombPower) {
                aP.owner = target;
                debuffs.add(aP);
                stacks.add(aP.amount);
                this.addToTop(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, aP.ID));
            }
        }

        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped()) {
                for (AbstractPower aP : aM.powers) {
                    if (aP.type == AbstractPower.PowerType.DEBUFF || aP instanceof InvisibleBombPower || aP instanceof TheBombPower) {
                        aP.owner = target;
                        debuffs.add(aP);
                        stacks.add(aP.amount);
                        this.addToTop(new RemoveSpecificPowerAction(aM, aM, aP.ID));
                    }
                }
            }
        }

        int i = 0;
        for (AbstractPower aP : debuffs) {
            this.addToBot(new ApplyPowerAction(target, source, aP, stacks.get(i)));
            AbstractDungeon.onModifyPower();
            i++;
        }

        this.isDone = true;
    }
}