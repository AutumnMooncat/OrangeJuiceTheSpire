package Moonworks.cardModifiers;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractDefaultCard;
import Moonworks.powers.NormaPower;
import basemod.BaseMod;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NormaDynvarModifier extends AbstractCardModifier {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());
    public static final String upgradeGreen = "[#7fff00]"; //For text purposes, the upgrade green is [#7fff00] and downgrade red is [#efc851]
    public final String normaDescription;
    private static final int MAX = DYNVARMODS.values().length;
    private final DYNVARMODS effect;
    public final int amount;
    public final boolean allowNegatives;
    public int normaCheck;
    private final boolean normaX;
    private boolean effectApplied; //We use this as a flag to not apply effects to the magic numbers more than once
    private int lastAmountApplied; //We probably dont need this one
    private int cumulativeAmountApplied;
    private int lastNormaChecked;

    public enum DYNVARMODS {
        /**
         * Used to dynamically modify the damage of any card
         */
        DAMAGEMOD,
        /**
         * Used to dynamically modify the Block of any card
         */
        BLOCKMOD,
        /**
         * Used to modify the magic number of any card, much less elegant
         */
        MAGICMOD,
        /**
         * Used to modify the second magic number of a Moonworks:AbstractDefaultCard, even less elegant
         */
        SECONDMAGICMOD,
        /**
         * Used to modify the third magic number of a Moonworks:AbstractDefaultCard, just as bad as before
         */
        THIRDMAGICMOD,
        /**
         * Used to modify the inverted magic number of a Moonworks:AbstractDefaultCard, just as bad as before
         */
        INVERTEDMOD,
        /**
         * Used to add descriptions to a card when you dont want to change any dynvars. Will dynamically color if the norma effect is active
         */
        INFOMOD,
        /**
         * Used when you need to modify a dynamic variable defined by a different mod
         * Can be used with locator and pre/postfix patches by using customHookCall() and customHookRemovedCall
         */
        CUSTOMHOOKMOD
    }

    /***
     * The one the want to use almost always.
     * @param effect - Holds information on which dynamic variable to modify. If you have a custom dynvar, use CUSTOMHOOKMOD and customHookCall() to patch it
     * @param nonZeroModAmount - Can be a positive or negative modification. Will do nothing if the value is 0
     * @param normaCheck - Use -1 for norma X, generally speaking, dont use a number over 5... lol
     * @param normaDescription - What will be displayed under he card. Dynamically highlights if you pass the normaCheck. Will not display anything if you pass null
     */
    public NormaDynvarModifier(DYNVARMODS effect, int nonZeroModAmount, int normaCheck, String normaDescription) {
        this(effect, nonZeroModAmount, normaCheck, normaDescription, false);
    }
    /***
     * Included in case someone needs to allow for negative valued variables, for whatever reason
     * @param effect - Holds information on which dynamic variable to modify. If you have a custom dynvar, use CUSTOMHOOKMOD and customHookCall() to patch it
     * @param nonZeroModAmount - Can be a positive or negative modification. Will do nothing if the value is 0
     * @param normaCheck - Use -1 for norma X, generally speaking, dont use a number over 5... lol
     * @param normaDescription - What will be displayed under he card. Dynamically highlights if you pass the normaCheck. Will not display anything if you pass null
     * @param allowNegativeBaseValues - Do we allow the dynamic variables to go below 0? Usually we really don't want to mess with this, use at your own risk.
     */
    public NormaDynvarModifier(DYNVARMODS effect, int nonZeroModAmount, int normaCheck, String normaDescription, boolean allowNegativeBaseValues) {
        this.effect = effect;
        this.amount = nonZeroModAmount;
        this.normaX = normaCheck == -1;
        this.normaCheck = this.normaX ? 1: normaCheck; //Could use absolute value, but thats not intuitive to read
        this.allowNegatives = allowNegativeBaseValues;
        this.normaDescription = normaDescription;
    }

    public void customHookCall(AbstractCard card) {}

    public void customHookRemovedCall(AbstractCard card) {}

    //Deal with Damage here, obviously
    @Override
    public float modifyDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        int bonus = 0;
        if (effect == DYNVARMODS.DAMAGEMOD) {
            if (normaX) {
                bonus = getNormaLevel() * amount;
            } else {
                bonus = getNormaLevel() >= normaCheck ? amount : 0;
            }
        }
        return super.modifyDamage(damage, type, card, target) + bonus; //This should handle negative values just fine, though we will see, lol
    }

    //Deal with Block here
    @Override
    public float modifyBlock(float block, AbstractCard card) {
        int bonus = 0;
        if (effect == DYNVARMODS.BLOCKMOD) {
            if (normaX) {
                bonus = getNormaLevel() * amount;
            } else {
                bonus = getNormaLevel() >= normaCheck ? amount : 0;
            }
        }
        return super.modifyBlock(block, card) + bonus; //This should handle negative values just fine, though we will see, lol
    }

    //Deal with magic numbers and other dynvars here, but make sure to use the boolean check
    @Override
    public void onApplyPowers(AbstractCard card) {
        boolean normaLowered = getNormaLevel() < lastNormaChecked;
        lastNormaChecked = getNormaLevel();
        if (normaLowered) {
            removeEffects(card); //Release old buffs, they will be recalculated
        }
        if (normaX || (!effectApplied && getNormaLevel() >= normaCheck)) {
            int amountToApply = calculateNormaEffects(card);
            if (amountToApply != 0) {
                applyEffects(card, amountToApply);
            }
        }
        super.onApplyPowers(card);
    }

    private int calculateNormaEffects(AbstractCard card) {
        int bonus = 0;
        int temp;
        if (normaX) {
            temp = amount * getNormaLevel() - cumulativeAmountApplied;
        } else {
            temp = amount;
        }
        if (temp != 0) {
            switch (effect) {
                case MAGICMOD:
                    bonus = allowNegatives || temp >= 0 ? temp : -Math.min(-temp, card.magicNumber);
                    break;
                case SECONDMAGICMOD:
                    if (card instanceof AbstractDefaultCard) {
                        AbstractDefaultCard c = (AbstractDefaultCard) card;
                        bonus = allowNegatives || temp >= 0 ? temp : -Math.min(-temp, c.secondMagicNumber);
                    }
                    break;
                case INVERTEDMOD:
                    if (card instanceof AbstractDefaultCard) {
                        AbstractDefaultCard c = (AbstractDefaultCard) card;
                        bonus = allowNegatives || temp >= 0 ? temp : -Math.min(-temp, c.invertedNumber);
                    }
                    break;
                case CUSTOMHOOKMOD:
                    customHookCall(card);
                    break;
                default:
                    break;
            }
        }
        return bonus; //Will default to 0 if temp was 0
    }

    private void applyEffects(AbstractCard card, int amountToApply) {
        switch (effect) {
            case MAGICMOD:
                amountToApply = allowNegatives || amount >= 0 ? amount : -Math.min(-amount, card.magicNumber);
                card.magicNumber += amountToApply;
                card.isMagicNumberModified = card.baseMagicNumber != card.magicNumber;
                break;
            case SECONDMAGICMOD:
                if (card instanceof AbstractDefaultCard) {
                    AbstractDefaultCard c = (AbstractDefaultCard) card;
                    amountToApply = allowNegatives || amount >= 0 ? amount : -Math.min(-amount, c.secondMagicNumber);
                    c.secondMagicNumber += amountToApply;
                    c.isSecondMagicNumberModified = c.baseSecondMagicNumber != c.secondMagicNumber;
                }
                break;
            case THIRDMAGICMOD:
                if (card instanceof AbstractDefaultCard) {
                    AbstractDefaultCard c = (AbstractDefaultCard) card;
                    amountToApply = allowNegatives || amount >= 0 ? amount : -Math.min(-amount, c.thirdMagicNumber);
                    c.thirdMagicNumber += amountToApply;
                    c.isThirdMagicNumberModified = c.baseThirdMagicNumber != c.thirdMagicNumber;
                }
                break;
            case INVERTEDMOD:
                if (card instanceof AbstractDefaultCard) {
                    AbstractDefaultCard c = (AbstractDefaultCard) card;
                    amountToApply = allowNegatives || amount >= 0 ? amount : -Math.min(-amount, c.invertedNumber);
                    c.invertedNumber += amountToApply;
                    c.isInvertedNumberModified = c.baseInvertedNumber != c.invertedNumber;
                }
                break;
            case CUSTOMHOOKMOD:
                customHookCall(card);
                break;
            default:
                break;
        }
        effectApplied = true;
        lastAmountApplied = amountToApply;
        cumulativeAmountApplied += amountToApply;
    }

    private void removeEffects(AbstractCard card) {
        int amountToRemove = normaX ? cumulativeAmountApplied : lastAmountApplied;
        switch (effect) {
            case MAGICMOD:
                card.magicNumber -= amountToRemove;
                card.isMagicNumberModified = card.baseMagicNumber != card.magicNumber;
                break;
            case SECONDMAGICMOD:
                if (card instanceof AbstractDefaultCard) {
                    AbstractDefaultCard c = (AbstractDefaultCard) card;
                    c.secondMagicNumber -= amountToRemove;
                    c.isSecondMagicNumberModified = c.baseSecondMagicNumber != c.secondMagicNumber;
                }
                break;
            case THIRDMAGICMOD:
                if (card instanceof AbstractDefaultCard) {
                    AbstractDefaultCard c = (AbstractDefaultCard) card;
                    c.thirdMagicNumber -= amountToRemove;
                    c.isThirdMagicNumberModified = c.baseThirdMagicNumber != c.thirdMagicNumber;
                }
                break;
            case INVERTEDMOD:
                if (card instanceof AbstractDefaultCard) {
                    AbstractDefaultCard c = (AbstractDefaultCard) card;
                    c.invertedNumber -= amountToRemove;
                    c.isInvertedNumberModified = c.baseInvertedNumber != c.invertedNumber;
                }
                break;
            case CUSTOMHOOKMOD:
                customHookRemovedCall(card);
                break;
            default:
                break;
        }
        effectApplied = false; //Free the flag so we can reapply when our Norma is at the right level again
        lastAmountApplied = 0;
        cumulativeAmountApplied = 0;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (normaDescription != null) {
            StringBuilder sb = new StringBuilder();
            boolean passedCheck = getNormaLevel() >= (normaX ? 1 : normaCheck);
            sb.append(" NL ");
            sb.append(passedCheck ? upgradeGreen : "*");
            sb.append(BaseMod.getKeywordTitle("moonworks:norma")).append(" ");
            sb.append(passedCheck ? upgradeGreen : "*");
            sb.append(normaX ? "X" : normaCheck);
            sb.append(": ");
            sb.append(normaDescription);
            rawDescription += sb.toString();
        }
        return rawDescription;
    }

    @Override
    public void atEndOfTurn(AbstractCard card, CardGroup group) {
        if(group != AbstractDungeon.player.hand) {
            removeEffects(card);
        }
        super.atEndOfTurn(card, group);
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        //removeEffects(card); //Maybe
        super.onInitialApplication(card);
    }

    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        return false;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new NormaDynvarModifier(effect, amount, normaCheck, normaDescription, allowNegatives);
    }

    @Override
    public boolean isInherent(AbstractCard card) {
        return true;
    }

    public static int getNormaLevel() {
        if(AbstractDungeon.player != null && AbstractDungeon.player.hasPower(NormaPower.POWER_ID)) {
            return AbstractDungeon.player.getPower(NormaPower.POWER_ID).amount;
        }
        return 0;
    }
}