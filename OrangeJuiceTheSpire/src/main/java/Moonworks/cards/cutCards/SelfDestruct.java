package Moonworks.cards.cutCards;

import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.cards.interfaces.RangedAttack;
import Moonworks.patches.PowerBypassHelper;
import Moonworks.patches.RangedPatches;
import basemod.AutoAdd;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EntanglePower;

import java.util.ArrayList;
import java.util.List;

import static Moonworks.OrangeJuiceMod.makeCardPath;
@AutoAdd.Ignore
public class SelfDestruct extends AbstractNormaAttentiveCard implements RangedAttack {

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(SelfDestruct.class.getSimpleName());
    public static final String IMG = makeCardPath("SelfDestruct.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;

    private static final int BLOCK = 10;
    private static final int UPGRADE_PLUS_BLOCK = 5;

    private static final int DAMAGE = 30;
    private static final int UPGRADE_PLUS_DAMAGE = 10;
    private static final int SELF_DAMAGE = 30;

    private static final int BLOCK_REDUCTION = 2;
    private static final int UPGRADE_PLUS_BLOCK_REDUCTION = 1;

    private static final Integer[] NORMA_LEVELS = {-1};
    protected final ArrayList<String> bypassList = new ArrayList<>();

    // /STAT DECLARATION/

    //TODO new effect? Pull entirely?
    public SelfDestruct() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, NORMA_LEVELS);
        //block = baseBlock = BLOCK;
        damage = baseDamage = DAMAGE;
        invertedNumber = baseInvertedNumber = SELF_DAMAGE;
        secondMagicNumber = baseSecondMagicNumber = BLOCK_REDUCTION;
        isMultiDamage = true;

        bypassList.add(EntanglePower.POWER_ID);

        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.INVERTEDMOD, -1, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        /*this.addToBot(new GainBlockAction(p, block));
        this.addToBot(new DamageAction(p, new DamageInfo(p, AbstractDungeon.player.currentHealth/2, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.FIRE));
        this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
        if (getNormaLevel() >= 3) {
            this.addToBot(new AddTemporaryHPAction(p, p, 10));
        }*/

        //Bolster current Block before the self damage hits
        this.addToBot(new AbstractGameAction() {
            public void update() {
                p.currentBlock *= secondMagicNumber;
                this.isDone = true;
            }});

        //Hit oneself
        this.addToBot(new DamageAction(p, new DamageInfo(p, invertedNumber, DamageInfo.DamageType.THORNS), true));

        //Reset Block back to normal
        this.addToBot(new AbstractGameAction() {
            public void update() {
                if (p.currentBlock > 0 && secondMagicNumber > 0) {
                    //Let it be 1 at minimum so we don't muck around with a 0 block amount that is still visible.
                    p.currentBlock = Math.max(1, MathUtils.floor((float)p.currentBlock / secondMagicNumber));
                }
                this.isDone = true;
            }});

        //Deal damage to everyone
        //this.addToBot(new DamageAllEnemiesAction(p, multiDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.FIRE));

        //A for loop rather than for-each would be more reasonable. Oh well.
        int i = 0;
        //Loop through every monster and hit them with the appropriate damage value
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped()) {
                DamageInfo rangedDamage = new DamageInfo(p, multiDamage[i], damageTypeForTurn);
                RangedPatches.RangedField.ranged.set(rangedDamage, true);
                this.addToBot(new DamageAction(aM, rangedDamage, AbstractGameAction.AttackEffect.FIRE, true));
            }
            i++;
        }

        //Ensure powers that care about hitting all targets function properly
        for (AbstractPower pow : p.powers) {
            pow.onDamageAllEnemies(this.multiDamage);
        }
    }

    @Override //Allow this card to be played when Entangled
    public boolean hasEnoughEnergy() {
        return (boolean) PowerBypassHelper.performBypass(super::hasEnoughEnergy, bypassList, AbstractDungeon.player);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            //this.upgradeBlock(UPGRADE_PLUS_BLOCK);
            //this.upgradeDamage(UPGRADE_PLUS_DAMAGE);
            this.upgradeSecondMagicNumber(UPGRADE_PLUS_BLOCK_REDUCTION);
            this.initializeDescription();
        }
    }

    @Override
    public List<String> getCardDescriptors() {
        List<String> tags = new ArrayList<>();
        tags.add(rangedTag);
        tags.addAll(super.getCardDescriptors());
        return tags;
    }

    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        List<TooltipInfo> compoundList = new ArrayList<>();
        compoundList.add(rangedTooltipInfo);
        if (super.getCustomTooltipsTop() != null) compoundList.addAll(super.getCustomTooltipsTop());
        return compoundList;
    }
}