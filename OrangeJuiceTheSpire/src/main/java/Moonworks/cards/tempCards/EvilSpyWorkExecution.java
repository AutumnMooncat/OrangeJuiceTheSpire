package Moonworks.cards.tempCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.cards.abstractCards.AbstractTempCard;
import Moonworks.cards.interfaces.RangedAttack;
import Moonworks.characters.TheStarBreaker;
import Moonworks.patches.RangedPatches;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.List;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class EvilSpyWorkExecution extends AbstractTempCard implements RangedAttack {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(EvilSpyWorkExecution.class.getSimpleName());
    public static final String IMG = makeCardPath("EvilSpyWorkExecution.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 0;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int DRAW = 1;
    private static final int UPGRADE_PLUS_DRAW = 1;

    // /STAT DECLARATION/


    public EvilSpyWorkExecution() {
        super(ID, IMG, COST, TYPE, COLOR, TARGET);
        AutoplayField.autoplay.set(this, true);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = DRAW;
        this.isMultiDamage = true;
        //this.setDisplayRarity(CardRarity.UNCOMMON);
    }

    @Override
    public float getTitleFontSize() {
        return 17F;
    }
    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //Thanks to it being ranged damage, I'd need to rewrite the entire DamageAll action, or break it down into single hits for each enemy
        //this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE, true));

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

        //Draw a new card
        this.addToBot(new DrawCardAction(magicNumber));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        //We shouldnt ever get the upgraded version from ESWP, since it expressly upgrades count, not adding upgraded versions of this card.
        //Nevertheless if the player manually upgrades these cards somehow, they will be in for a treat, as they are much more powerful.
        if (!upgraded) {
            upgradeName();
            //rawDescription = UPGRADE_DESCRIPTION;
            //upgradeMagicNumber(UPGRADE_PLUS_DRAW);
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
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