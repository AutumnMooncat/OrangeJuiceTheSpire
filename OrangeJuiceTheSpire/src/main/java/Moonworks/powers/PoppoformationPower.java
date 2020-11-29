package Moonworks.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import Moonworks.OrangeJuiceMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PoppoformationPower extends AbstractPower implements CloneablePowerInterface {

    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

    public static final String POWER_ID = OrangeJuiceMod.makeID("PoppoformationPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final AbstractMonster target;
    public AbstractCreature source;
    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public PoppoformationPower(final AbstractMonster owner, final AbstractCreature source, final int amount) {
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
        this.loadRegion("hex");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
        //logger.info("Poppo initialized for " + this.target.toString());
    }

    /* //Covered in the PoppoformationPatch
    @Override
    public void atEndOfTurn(boolean isPlayer) { //Almost works, uses ended turns intents however, so not the right functionality.
        super.atEndOfTurn(isPlayer);
        if(target.intent == null) {
            logger.info("Intent null!");
        }
        logger.info("Poppo start of turn for" + target.toString());
        logger.info("Poppo intent " + target.intent.toString());
        this.flash();
        if (target.intent.name().contains("ATTACK")) {
        //if (target.intent == AbstractMonster.Intent.ATTACK || target.intent == AbstractMonster.Intent.ATTACK_BUFF || target.intent == AbstractMonster.Intent.ATTACK_DEBUFF ) {
            this.addToBot(new ApplyPowerAction(target, source, new WeakPower(target, 1, false)));
        }
        else if (target.intent.name().contains("DEFEND")) {
        //else if (target.intent == AbstractMonster.Intent.DEFEND || target.intent == AbstractMonster.Intent.DEFEND_BUFF || target.intent == AbstractMonster.Intent.DEFEND_DEBUFF) {
            this.addToBot(new ApplyPowerAction(target, source, new FrailPower(target, 1, false)));
        } else if (target.intent == AbstractMonster.Intent.ATTACK_DEFEND) {
            this.addToBot(new ApplyPowerAction(target, source, new WeakPower(target, 1, false)));
            this.addToBot(new ApplyPowerAction(target, source, new FrailPower(target, 1, false)));
        } else {
            this.addToBot(new ApplyPowerAction(target, source, new VulnerablePower(target, 1, false)));
        }
        this.amount--;
        if (this.amount == 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
        updateDescription();
    }//*/

    /*
    @Override // None of these work
    public void duringTurn()
    public void atEndOfRound()
    public void atStartOfTurn()
    public void atStartOfTurnPostDraw()
    public void atEndOfTurn(boolean isPlayer)
    public void atStartOfTurn()
    */

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else if (amount > 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new PoppoformationPower(target, source, amount);
    }
}
