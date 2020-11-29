package Moonworks.cards;

import Moonworks.cards.defaultcards.AbstractNormaAttentiveCard;
import Moonworks.powers.Heat300PercentPower;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class ShieldCounter extends AbstractNormaAttentiveCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(ShieldCounter.class.getSimpleName());
    public static final String IMG = makeCardPath("ShieldCounter.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 2;
    private static final int DAMAGE = 0;

    // /STAT DECLARATION/


    public ShieldCounter() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(upgraded){
            AbstractDungeon.actionManager.addToBottom(new RemoveAllBlockAction(m, p));
        }
        // Thanks @Alchyr#3696, #modding-technical
        int dmg = (int)ReflectionHacks.getPrivate(m, AbstractMonster.class, "intentDmg");
        if ((boolean)ReflectionHacks.getPrivate(m, AbstractMonster.class, "isMultiDmg"))
        {
            dmg *= (int)ReflectionHacks.getPrivate(m, AbstractMonster.class, "intentMultiAmt");
        }
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, dmg, damageTypeForTurn),
                        AbstractGameAction.AttackEffect.SLASH_HEAVY));
        if (getNormaLevel() >= 4) {
            this.addToBot(new ApplyPowerAction(m, p, new Heat300PercentPower(m, 1)));
        }

    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}