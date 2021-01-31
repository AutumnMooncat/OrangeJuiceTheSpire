package Moonworks.powers;

import Moonworks.OrangeJuiceMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;

public class WhimsicalWindmillPower extends AbstractPower implements CloneablePowerInterface {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

    public static final String POWER_ID = OrangeJuiceMod.makeID("WhimsicalWindmillPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public final HashSet<AbstractCreature> attackedSet = new HashSet<>();

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public WhimsicalWindmillPower(final AbstractCreature owner, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.loadRegion("controlled_change"); //controlled_change
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    //Regain energy equal to the number of creates in our set then clear it.
    @Override
    public void onEnergyRecharge() {
        AbstractDungeon.player.gainEnergy(attackedSet.size());
        attackedSet.clear();
        super.onEnergyRecharge();
        updateDescription();
    }

    //If we are attacked, add the creature to the set. The set ensures to duplicates can not exist, so this covers multi-hits.
    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner != owner) {
            attackedSet.add(info.owner);
        }
        updateDescription();
        return super.onAttacked(info, damageAmount);
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(DESCRIPTIONS[0]).append(amount).append(DESCRIPTIONS[1]);
        if (attackedSet.size() > 0) {
            sb.append(DESCRIPTIONS[2]).append(attackedSet.size()).append(DESCRIPTIONS[3]);
        }
        description = sb.toString();
    }

    @Override
    public AbstractPower makeCopy() {
        return new WhimsicalWindmillPower(owner, amount);
    }
}