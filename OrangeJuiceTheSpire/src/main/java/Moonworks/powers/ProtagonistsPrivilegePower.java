package Moonworks.powers;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.ProtagonistsPrivilegeAction;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

public class ProtagonistsPrivilegePower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = OrangeJuiceMod.makeID("ProtagonistsPrivilegePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    //Two arrayLists, because iterating backwards over a Map is awful
    private final ArrayList<DamageAction> damageArray = new ArrayList<>();
    private final ArrayList<Integer> drawArray = new ArrayList<>();



    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    /**
     *
     * @param owner - Who this power is applied to. Should be a player to avoid shenanigans
     * @param damage - The damage action our card just did. We don't recalculate by choice
     * @param draw - The amount of cards to draw. We keep this in case something changes the draw power of the card
     */
    public ProtagonistsPrivilegePower(final AbstractCreature owner, final DamageAction damage, final Integer draw) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = 1;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.loadRegion("rushdown");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
        damageArray.add(damage);
        drawArray.add(draw);

        updateDescription();
    }

    /**
     * Used for copying
     * @param owner - Who this power is applied to. Should be a player to avoid shenanigans
     * @param damages - The array of damage actions to copy
     * @param draws -  The array of cards to copy
     */
    private ProtagonistsPrivilegePower(final AbstractCreature owner, ArrayList<DamageAction> damages, final ArrayList<Integer> draws) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = damageArray.size();

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.loadRegion("rushdown");
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        damageArray.addAll(damages);
        drawArray.addAll(draws);

        updateDescription();
    }

    public void onEnergyRecharge() {
        super.onEnergyRecharge();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        super.atStartOfTurnPostDraw();
        //OrangeJuiceMod.logger.info("Protag Power starting up. # stacks: " + damageArray.size());
        int oldStacks = damageArray.size();
        for (int i = 0 ; i < oldStacks ; i++) {
            //OrangeJuiceMod.logger.info("Protag Power entering loop. Index: " + i +". Current size: "+ damageArray.size());
            int finalI = i;

            //Flash
            this.addToBot(new AbstractGameAction() {
                public void update() {
                    flash();
                    this.isDone = true;
                }});

            //Deal the same damage as last time
            this.addToBot(new AbstractGameAction() {
                public void update() {
                    //Get the original monster
                    AbstractCreature t = damageArray.get(finalI).target;

                    //If its null, dead, or otherwise gone, grab a new target.
                    //If the card has a null target to begin with, that it ought not interact with what we pass into the use function
                    //Still grab a non-null target anyway for safety
                    if (t == null || t.isDeadOrEscaped() || t.currentHealth <= 0 || !AbstractDungeon.getMonsters().monsters.contains(t)) {
                        t = AbstractDungeon.getRandomMonster();
                        //OrangeJuiceMod.logger.info("Protag Power grabbed new target. Monster: " + t);
                    }

                    damageArray.get(finalI).isDone = false;
                    this.addToBot(damageArray.get(finalI));
                    this.isDone = true;
                }});

            //Define our recovery action
            ProtagonistsPrivilegePower reference = this;
            AbstractGameAction reapplyPower = new AbstractGameAction() {
                public void update() {
                    //Reapply by doing nothing?
                    //reference.addInfoPair(damageArray.get(finalI), drawArray.get(finalI));
                    this.isDone = true;
                }};
            AbstractGameAction decrementPower = new AbstractGameAction() {
                public void update() {
                    reference.removeInfoPair(finalI, finalI);
                    if (reference.amount <= 0) {
                        this.addToTop(new RemoveSpecificPowerAction(owner, owner, reference));
                    }
                    this.isDone = true;
                }};

            //Do the draw action and recover
            this.addToBot(new ProtagonistsPrivilegeAction(drawArray.get(finalI), reapplyPower, decrementPower));
            //OrangeJuiceMod.logger.info("Protag Power new size: "+amount);
            updateDescription();
        }
        /*
        AbstractPower ref = this;
        this.addToBot(new AbstractGameAction() {
            public void update() {
                OrangeJuiceMod.logger.info("Protag Power clean up. Previous size: " + amount);
                amount = damageArray.size();
                OrangeJuiceMod.logger.info("Protag Power clean up. New size: "+amount);
                if (amount <= 0) {
                    this.addToBot(new RemoveSpecificPowerAction(owner, owner, ref));
                    OrangeJuiceMod.logger.info("Protag Power didn't add any new activations. Removing power");
                }
                this.isDone = true;
            }});*/

        updateDescription();
    }

    //No stacking like this, expressly stack via addDamageDrawTargetInfo
    public void stackPower(int stackAmount) {}

    public void addInfoPair(DamageAction damage, Integer draw) {
        damageArray.add(damage);
        drawArray.add(draw);
        amount = damageArray.size();
        updateDescription();
        flash();
    }

    public void removeInfoPair(int damageIndex, int drawIndex) {
        damageArray.remove(damageIndex);
        drawArray.remove(drawIndex);
        amount = damageArray.size();
        updateDescription();
        flash();
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if (damageArray.size() == 0) {
            description = DESCRIPTIONS[0];
        } else {
            description = DESCRIPTIONS[0] + damageArray.get(0).amount + DESCRIPTIONS[1] + drawArray.get(0) + DESCRIPTIONS[2];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new ProtagonistsPrivilegePower(owner, damageArray, drawArray);
    }
}