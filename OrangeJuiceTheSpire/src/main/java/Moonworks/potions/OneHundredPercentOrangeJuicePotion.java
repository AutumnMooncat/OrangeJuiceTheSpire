package Moonworks.potions;

import Moonworks.OrangeJuiceMod;
import Moonworks.powers.NormaPower;
import Moonworks.util.NormaHelper;
import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public class OneHundredPercentOrangeJuicePotion extends CustomPotion {


    public static final String POTION_ID = OrangeJuiceMod.makeID("OneHundredPercentOrangeJuicePotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public OneHundredPercentOrangeJuicePotion() {
        // The bottle shape and inside is determined by potion size and color. The actual colors are the main DefaultMod.java
        super(NAME, POTION_ID, PotionRarity.COMMON, PotionSize.BOTTLE, PotionColor.FIRE);

        // Do you throw this potion at an enemy or do you just consume it.
        isThrown = false;
    }

    @Override
    public void use(AbstractCreature target) {
        this.getPotency(AbstractDungeon.ascensionLevel);
        AbstractPlayer p = AbstractDungeon.player;
        NormaHelper.applyNormaPowerNoTriggers(p, 5);
        this.addToBot(new HealAction(p, p, potency));
    }

    // This is your potency.
    @Override
    public int getPotency(final int ascensionLevel) {
        return 5;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        description = potionStrings.DESCRIPTIONS[0] + potency + potionStrings.DESCRIPTIONS[1];
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(NormaPower.NAME,NormaPower.DESCRIPTIONS[0]));
    }

    @Override
    public AbstractPotion makeCopy() {
        return new OneHundredPercentOrangeJuicePotion();
    }
}