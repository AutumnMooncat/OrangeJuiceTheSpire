package Moonworks.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import Moonworks.OrangeJuiceMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BigBangBellPower extends AbstractTrapPower implements CloneablePowerInterface {

    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

    public static final String POWER_ID = OrangeJuiceMod.makeID("BigBangBellPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final AbstractMonster target;
    public AbstractCreature source;
    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    private static final float MULTI = 1.5f;

    public BigBangBellPower(final AbstractMonster owner, final AbstractCreature source, final int amount) {
        //logger.info("Poppo initializing on " + owner.toString());
        name = NAME;
        ID = POWER_ID;

        this.target = owner;
        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.loadRegion("cExplosion");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
        //logger.info("Poppo initialized for " + this.target.toString());
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        super.onAttack(info, damageAmount, target);
        logger.info("On Attack. damageAmount: "+damageAmount+". info.output: "+info.output+". target: "+target);
        //If they deal unblocked attack damage to ANY creature
        if (damageAmount > 0) {
            flash();
            this.addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    CardCrawlGame.sound.play("ATTACK_FLAME_BARRIER", 0.05F);
                    this.isDone = true;
                }
            });
            this.addToBot(new DamageAction(owner, new DamageInfo(AbstractDungeon.player, amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
            this.addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void atEndOfRound() {
        super.atEndOfRound();
        flash();
        this.amount *= MULTI;
        updateDescription();
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new BigBangBellPower(target, source, amount);
    }
}
