package Moonworks.potions;

import Moonworks.OrangeJuiceMod;
import Moonworks.powers.TemporaryStrengthPower;
import basemod.BaseMod;
import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public class LiquidChainsPotion extends CustomPotion {


    public static final String POTION_ID = OrangeJuiceMod.makeID("LiquidChainsPotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public LiquidChainsPotion() {
        // The bottle shape and inside is determined by potion size and color. The actual colors are the main DefaultMod.java
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.SPIKY, PotionColor.FIRE);

        // Do you throw this potion at an enemy or do you just consume it.
        isThrown = true;
        targetRequired = true;
    }

    @Override
    public void use(AbstractCreature target) {
        this.getPotency(AbstractDungeon.ascensionLevel);
        AbstractPlayer p = AbstractDungeon.player;
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                CardCrawlGame.sound.play("POWER_SHACKLE", 0.05F);
                this.isDone = true;
            }
        });
        this.addToBot(new ApplyPowerAction(target, p, new TemporaryStrengthPower(target, -potency)));
    }

    @Override
    public int getPotency(final int ascensionLevel) {
        return 6;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        description = potionStrings.DESCRIPTIONS[0] + potency + potionStrings.DESCRIPTIONS[1];
        tips.clear();
        tips.add(new PowerTip(name, description));
        /*tips.add(new PowerTip(
                BaseMod.getKeywordTitle("strength"),
                BaseMod.getKeywordDescription("strength")
        ));*/
        tips.add(new PowerTip(
                TipHelper.capitalize(GameDictionary.STRENGTH.NAMES[0]),
                GameDictionary.keywords.get(GameDictionary.STRENGTH.NAMES[0])));
    }

    @Override
    public AbstractPotion makeCopy() {
        return new LiquidChainsPotion();
    }
}