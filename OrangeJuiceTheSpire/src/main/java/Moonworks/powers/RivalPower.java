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

public class RivalPower extends AbstractPower implements CloneablePowerInterface {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

    public static final String POWER_ID = OrangeJuiceMod.makeID("RivalPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public int damageBoostPercentile;
    public boolean attackedPlayer;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public RivalPower(final AbstractCreature owner, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        damageBoostPercentile = amount*10;
        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.loadRegion("master_smite"); //controlled_change
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        updateDescription();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (target == AbstractDungeon.player) {
            attackedPlayer = true;
        }
        super.onAttack(info, damageAmount, target);
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        damage *= (1.0f + damageBoostPercentile/100f);
        return super.atDamageReceive(damage, damageType);
    }

    @Override
    public void atEndOfRound() {
        if (attackedPlayer) {
            flash();
            attackedPlayer = false;
            damageBoostPercentile += amount*10;
            updateDescription();
        }
        super.atEndOfRound();
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        description = owner.name + DESCRIPTIONS[0] + damageBoostPercentile + DESCRIPTIONS[1] + amount*10 + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new RivalPower(owner, amount);
    }
}