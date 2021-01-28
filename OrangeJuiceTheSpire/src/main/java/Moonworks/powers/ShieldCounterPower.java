package Moonworks.powers;

import Moonworks.OrangeJuiceMod;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShieldCounterPower extends AbstractTrapPower implements CloneablePowerInterface {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

    public AbstractCreature source;

    public static final String POWER_ID = OrangeJuiceMod.makeID("ShieldCounterPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private boolean retain;
    private boolean blockedAttack;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public ShieldCounterPower(final AbstractCreature owner, final int turns, final boolean retain) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = turns;
        this.retain = retain;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.loadRegion("noPain");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        blockedAttack = this.owner.currentBlock >= MathUtils.floor(damage);
        return super.atDamageFinalReceive(damage, type);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (blockedAttack && info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS
                && info.owner != null && info.owner != this.owner) {
            this.flash();
            this.addToTop(new DamageAction(info.owner, new DamageInfo(this.owner, info.output, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE, true));
        }
        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void atEndOfRound() {
        if (!retain || this.owner.currentBlock <= 0) {
            if (this.amount <= 1) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
            } else {
                AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
            }
        }
    }

    public void setRetain(boolean retain) {
        this.retain = retain;
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        StringBuilder sb = new StringBuilder();
        if (amount == 1) {
            sb.append(DESCRIPTIONS[0]);
        } else if (amount > 1) {
            sb.append(DESCRIPTIONS[1]).append(amount).append(DESCRIPTIONS[2]);
        }
        sb.append(DESCRIPTIONS[3]);
        if (retain) {
            sb.append(DESCRIPTIONS[4]);
        }
        description = sb.toString();
    }

    @Override
    public AbstractPower makeCopy() {
        return new ShieldCounterPower(owner, amount, retain);
    }
}
