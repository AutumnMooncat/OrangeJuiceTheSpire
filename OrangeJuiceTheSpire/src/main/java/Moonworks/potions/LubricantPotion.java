package Moonworks.potions;

import Moonworks.OrangeJuiceMod;
import basemod.BaseMod;
import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class LubricantPotion extends CustomPotion {


    public static final String POTION_ID = OrangeJuiceMod.makeID("LubricantPotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public LubricantPotion() {
        // The bottle shape and inside is determined by potion size and color. The actual colors are the main DefaultMod.java
        super(NAME, POTION_ID, PotionRarity.COMMON, PotionSize.SPHERE, PotionColor.FIRE);

        // Do you throw this potion at an enemy or do you just consume it.
        isThrown = true;
        targetRequired = true;
    }

    @Override
    public void use(AbstractCreature target) {
        this.getPotency(AbstractDungeon.ascensionLevel);
        AbstractPlayer p = AbstractDungeon.player;
        this.addToBot(new ApplyPowerAction(target, p, new VulnerablePower(target, potency, false)));
        for (AbstractMonster aM: AbstractDungeon.getMonsters().monsters) {
            if (aM != target) {
                this.addToBot(new ApplyPowerAction(aM, p, new VulnerablePower(aM, potency/2, false)));
            }
        }
    }

    // This is your potency.
    @Override
    public int getPotency(final int ascensionLevel) {
        return 2;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        description = potionStrings.DESCRIPTIONS[0] + potency + potionStrings.DESCRIPTIONS[1];
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(
                BaseMod.getKeywordTitle("vulnerable"),
                BaseMod.getKeywordDescription("vulnerable")
        ));
        tips.add(new PowerTip(
                BaseMod.getKeywordTitle("moonworks:splash"),
                BaseMod.getKeywordDescription("moonworks:splash")
        ));
    }

    @Override
    public AbstractPotion makeCopy() {
        return new LubricantPotion();
    }
}