package Moonworks.powers;

import Moonworks.OrangeJuiceMod;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InvisibleBombPower extends AbstractPower implements CloneablePowerInterface {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());

    public AbstractCreature source;

    public static final String POWER_ID = OrangeJuiceMod.makeID("InvisibleBombPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private final int damage;
    private int count;
    private boolean detonate;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public InvisibleBombPower(final AbstractCreature owner, final AbstractCreature source, final int damage, final int stacks) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.damage = damage;
        this.amount = stacks;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        this.count = 0;
        this.detonate = false;
        this.source = source;
        //this.multiDamage = damage;

        // We load those txtures here.
        this.loadRegion("the_bomb");
        //logger.info("Blasting Fuse?");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public void atStartOfTurn() {
        int dets = 0;
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            count++;
            if (count == 3) {
                detonate = true;
            }
            for (int i = 0 ; i < amount ; i++){
                if (AbstractDungeon.cardRandomRng.random(1, 3) == 1 || detonate) {
                    this.flashWithoutSound();
                    this.addToBot(new DamageAction(owner, new DamageInfo(source, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.FIRE));
                    dets++;
                }
            }
            this.amount -= dets;
            updateDescription();
            if (this.amount <= 0) {
                this.addToBot(new RemoveSpecificPowerAction(owner, owner, this.ID));
            }
        }
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if (count >= 2) {
            description = DESCRIPTIONS[0] + damage + DESCRIPTIONS[3];
        } else {
            description = DESCRIPTIONS[0] + damage + DESCRIPTIONS[1] + (3-count) + DESCRIPTIONS[2];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new InvisibleBombPower(owner, source, damage, amount);
    }
}
