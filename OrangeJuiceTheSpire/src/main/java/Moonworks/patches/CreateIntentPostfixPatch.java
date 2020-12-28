package Moonworks.patches;

import Moonworks.powers.BigBangBellPower;
import Moonworks.powers.Heat300PercentPower;
import Moonworks.powers.PoppoformationPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;


@SpirePatch(
        clz = AbstractMonster.class, //This is the class you're patching.
        method = "createIntent" //This is the name of the method of that class that you're patching.
)
public class CreateIntentPostfixPatch
{
    @SpirePostfixPatch
    //@SpirePrefixPatch //This means this is going to be added at the "start" of the thing you're patching.
    public static SpireReturn<?> IntentReader(AbstractMonster __instance) //Patches receive both the instance (when the method is called, the AbstractCreature it's being called on) and any parameters of the method being patched (in this case, the block amount.)
    {
        if (__instance.hasPower(PoppoformationPower.POWER_ID))
        {
            AbstractPower p = __instance.getPower(PoppoformationPower.POWER_ID);
            AbstractMonster.Intent currentIntent = __instance.intent;
            p.flash();
            p.amount--;
            p.updateDescription();
            if (p.amount == 0) {
                AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(p.owner, p.owner, PoppoformationPower.POWER_ID));
            }
            if(!p.owner.isDeadOrEscaped() && (p.owner instanceof AbstractMonster)) {
                //This should work for custom intents
                boolean isAttack = (((AbstractMonster)p.owner).getIntentBaseDmg() >= 0 && currentIntent != AbstractMonster.Intent.STUN);
                //I dont know if there is a more intellegent way to do this. This will not work for custom intents
                boolean isBlock = (
                        currentIntent == AbstractMonster.Intent.DEFEND ||
                        currentIntent == AbstractMonster.Intent.DEFEND_BUFF ||
                        currentIntent == AbstractMonster.Intent.DEFEND_DEBUFF ||
                        currentIntent == AbstractMonster.Intent.ATTACK_DEFEND);
                if (isAttack) {
                    __instance.addToBot(new ApplyPowerAction(p.owner, AbstractDungeon.player, new WeakPower(p.owner, 1, false)));
                }
                if (isBlock) {
                    __instance.addToBot(new ApplyPowerAction(p.owner, AbstractDungeon.player, new FrailPower(p.owner, 1, false)));
                }
                if (!isAttack && !isBlock) {
                    __instance.addToBot(new ApplyPowerAction(p.owner, AbstractDungeon.player, new VulnerablePower(p.owner, 1, false)));
                }
            }
            /*
            switch (currentIntent) {
                case ATTACK:
                case ATTACK_BUFF:
                case ATTACK_DEBUFF:
                    __instance.addToBot(new ApplyPowerAction(p.owner, AbstractDungeon.player, new WeakPower(p.owner, 1, false)));
                    break;
                case ATTACK_DEFEND:
                    __instance.addToBot(new ApplyPowerAction(p.owner, AbstractDungeon.player, new WeakPower(p.owner, 1, false)));
                    __instance.addToBot(new ApplyPowerAction(p.owner, AbstractDungeon.player, new FrailPower(p.owner, 1, false)));
                    break;
                case DEFEND:
                case DEFEND_DEBUFF:
                case DEFEND_BUFF:
                    __instance.addToBot(new ApplyPowerAction(p.owner, AbstractDungeon.player, new FrailPower(p.owner, 1, false)));
                    break;
                default:
                    __instance.addToBot(new ApplyPowerAction(p.owner, AbstractDungeon.player, new VulnerablePower(p.owner, 1, false)));
                    break;
            }*/

        }

        if (__instance.hasPower(BigBangBellPower.POWER_ID))
        {
            AbstractPower p = __instance.getPower(BigBangBellPower.POWER_ID);
            AbstractMonster.Intent currentIntent = __instance.intent;
            p.flash();
            //If they arent dead, and it is a monster, and the monster is attacking
            if(!p.owner.isDeadOrEscaped() && (p.owner instanceof AbstractMonster)) {
                //Detonate if we stun them
                if (((AbstractMonster)p.owner).getIntentBaseDmg() >= 0 && currentIntent != AbstractMonster.Intent.STUN) {
                    p.amount *= 1.5F;
                    p.updateDescription();
                } else {
                    __instance.addToBot(new DamageAction(__instance, new DamageInfo(AbstractDungeon.player, p.amount, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.FIRE));
                    AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(p.owner, p.owner, BigBangBellPower.POWER_ID));
                }
            }
            /*
            switch (currentIntent) {
                case ATTACK:
                case ATTACK_BUFF:
                case ATTACK_DEBUFF:
                case ATTACK_DEFEND:
                    p.amount *= 1.5F;
                    p.updateDescription();
                    break;
                default:
                    __instance.addToBot(new DamageAction(__instance, new DamageInfo(AbstractDungeon.player, p.amount, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.FIRE));
                    AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(p.owner, p.owner, BigBangBellPower.POWER_ID));
                    break;
            }*/
        }
        return SpireReturn.Continue(); //This allows you to either return early, change the value returned, or just continue like this.
    }
}