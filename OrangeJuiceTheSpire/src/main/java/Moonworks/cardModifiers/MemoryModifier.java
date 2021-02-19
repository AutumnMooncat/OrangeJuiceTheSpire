package Moonworks.cardModifiers;

import Moonworks.OrangeJuiceMod;
import Moonworks.patches.TypeOverridePatch;
import Moonworks.powers.BookOfMemoriesPower;
import Moonworks.util.MemoryHelper;
import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.abstracts.AbstractCardModifier;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MemoryModifier extends AbstractCardModifier {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());
    public static final String ID = OrangeJuiceMod.makeID("MemoryModifier");
    public static final String MEMORY_OF = BookOfMemoriesPower.DESCRIPTIONS[7];
    public static final String ACTIVE_DESC = BookOfMemoriesPower.DESCRIPTIONS[8];
    public static final String EXHAUST_DESC = BookOfMemoriesPower.DESCRIPTIONS[9];
    public static final String COOLDOWN_DESC1 = BookOfMemoriesPower.DESCRIPTIONS[10];
    public static final String COOLDOWN_DESC2 = BookOfMemoriesPower.DESCRIPTIONS[11];
    private String oldName;
    private int oldCost;
    private static final int UNPLAYABLE_COST = -2;
    public boolean exhausted;

    public MemoryModifier(boolean exhausted) {
        this.exhausted = exhausted;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        //logger.info("Appled Memory Mod to "+card);
        oldName = card.name;
        oldCost = card.cost;
        modifyCost(card);
        modifyName(card);
        modifyBackground(card);
        modifyType(card);
        super.onInitialApplication(card);

        /*if (card instanceof AngelHand || card instanceof DevilHand) {
            if (card.cardsToPreview != null) {
                CardModifierManager.addModifier(card.cardsToPreview, new MemoryModifier(associatedPower));
            }
        }*/
    }

    public void modifyCost(AbstractCard card) {
        card.cost = UNPLAYABLE_COST;
        card.costForTurn = UNPLAYABLE_COST;
    }

    public void modifyName(AbstractCard card) {
        String[] splitName = card.name.trim().split("\\s+");
        card.name = MEMORY_OF + splitName[splitName.length-1];
        ReflectionHacks.RMethod method = ReflectionHacks.privateMethod(AbstractCard.class, "initializeTitle");
        method.invoke(card);
        //card.initializeTitle();
    }

    public void modifyBackground(AbstractCard card) {
        if (card instanceof CustomCard) {
            if (card.type == AbstractCard.CardType.ATTACK) {
                ((CustomCard) card).setBackgroundTexture(OrangeJuiceMod.MAGIC_ATTACK_WHITE_ICE, OrangeJuiceMod.MAGIC_ATTACK_WHITE_ICE_PORTRAIT);
            } else if (card.type != AbstractCard.CardType.POWER) { //Power is already blue, we just expressly want to do nothing
                ((CustomCard) card).setBackgroundTexture(OrangeJuiceMod.MAGIC_SKILL_WHITE_ICE, OrangeJuiceMod.MAGIC_SKILL_WHITE_ICE_PORTRAIT);
            }
        }
    }

    public void modifyType(AbstractCard card) {
        TypeOverridePatch.TypeOverrideField.typeOverride.set(card, BaseMod.getKeywordTitle("moonworks:memory"));
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        StringBuilder sb = new StringBuilder();
        if (MemoryHelper.isExhausted(card)) {
            sb.append(EXHAUST_DESC);
        } else if (MemoryHelper.isReadyToUse(card)) {
            sb.append(ACTIVE_DESC);
        } else {
            sb.append(COOLDOWN_DESC1).append(MemoryHelper.getCooldown(card)).append(COOLDOWN_DESC2);
        }
        return sb.toString() + rawDescription;
    }

    @Override
    public void onExhausted(AbstractCard card) {
        this.exhausted = true;
        super.onExhausted(card);
    }

    //Can not play a Memory card
    @Override
    public boolean canPlayCard(AbstractCard card) {
        return false;
    }

    //Does not remove when played
    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        return false;
    }

    //Can not be removed once added
    @Override
    public boolean isInherent(AbstractCard card) {
        return true;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new MemoryModifier(exhausted);
    }
}