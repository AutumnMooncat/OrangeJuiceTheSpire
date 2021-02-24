package Moonworks.potions;

import Moonworks.OrangeJuiceMod;
import Moonworks.powers.BookOfMemoriesPower;
import Moonworks.powers.NormaPower;
import Moonworks.util.NormaHelper;
import basemod.abstracts.CustomPotion;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
//TODO this is likely to break so many modded things. Perhaps an entirely new effect is in order
public class OverloadPotion extends CustomPotion {


    public static final String POTION_ID = OrangeJuiceMod.makeID("OverloadPotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public OverloadPotion() {
        // The bottle shape and inside is determined by potion size and color. The actual colors are the main DefaultMod.java
        super(NAME, POTION_ID, PotionRarity.RARE, PotionSize.BOTTLE, PotionColor.FIRE);

        // Do you throw this potion at an enemy or do you just consume it.
        isThrown = false;
    }

    @Override
    public void use(AbstractCreature target) {
        this.getPotency(AbstractDungeon.ascensionLevel);
        AbstractPlayer p = AbstractDungeon.player;
        for (AbstractPower pow : p.powers) {
            //this.addToBot(new ApplyPowerAction(p, p, pow, pow.amount*potency));
            this.addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    if (pow instanceof BookOfMemoriesPower) {
                        ((BookOfMemoriesPower) pow).duplicateMemories(1+potency);
                        pow.updateDescription();
                    } else if (pow instanceof NormaPower) {
                        NormaHelper.applyNormaPowerNoTriggers(AbstractDungeon.player, pow.amount*potency);
                    } else {
                        pow.flash();
                        pow.amount *= 1+potency;
                        if (pow instanceof TwoAmountPower) {
                            ((TwoAmountPower)pow).amount2 *= 1+potency;
                        }
                        pow.updateDescription();
                    }
                    this.isDone = true;
                }
            });
        }
    }

    // This is your potency.
    @Override
    public int getPotency(final int ascensionLevel) {
        return 1;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        description = potionStrings.DESCRIPTIONS[0] + (potency+1) + potionStrings.DESCRIPTIONS[1];
        tips.clear();
        tips.add(new PowerTip(name, description));
    }

    @Override
    public AbstractPotion makeCopy() {
        return new OverloadPotion();
    }
}