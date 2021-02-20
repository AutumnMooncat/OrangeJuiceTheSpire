package Moonworks.potions;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.NormaBreakAction;
import Moonworks.powers.NormaPower;
import basemod.BaseMod;
import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;

import java.util.ArrayList;

public class OneHundredPercentOrangeJuicePotion extends CustomPotion {


    public static final String POTION_ID = OrangeJuiceMod.makeID("OneHundredPercentOrangeJuicePotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    private boolean upgraded = false;

    public OneHundredPercentOrangeJuicePotion() {
        // The bottle shape and inside is determined by potion size and color. The actual colors are the main DefaultMod.java
        super(NAME, POTION_ID, PotionRarity.RARE, PotionSize.BOTTLE, PotionColor.ENERGY);

        // Do you throw this potion at an enemy or do you just consume it.
        isThrown = false;
    }

    @Override
    public void use(AbstractCreature target) {
        this.getPotency(AbstractDungeon.ascensionLevel);
        AbstractPlayer p = AbstractDungeon.player;
        this.addToBot(new NormaBreakAction(p, upgraded));
    }

    // This is your potency.
    @Override
    public int getPotency(final int ascensionLevel) {
    AbstractPlayer p = AbstractDungeon.player;
        if (p != null && p.hasRelic("SacredBark")) {
            upgraded = true;
        }
        return 6;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        description = potionStrings.DESCRIPTIONS[0] + potency + (upgraded ? potionStrings.DESCRIPTIONS[2] : potionStrings.DESCRIPTIONS[1]);
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(NormaPower.NAME,NormaPower.DESCRIPTIONS[0]));
    }

    @Override
    public AbstractPotion makeCopy() {
        return new OneHundredPercentOrangeJuicePotion();
    }
}
