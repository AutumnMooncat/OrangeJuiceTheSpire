package Moonworks.cards;

import Moonworks.cards.abstractCards.AbstractDynamicCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class Gamble extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(Gamble.class.getSimpleName());
    public static final String IMG = makeCardPath("Gamble.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;
    private static final int DAMAGE = 999;
    private static final int UPGRADE_REDUCED_COST = 0;

    // /STAT DECLARATION/


    public Gamble() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        damageTypeForTurn = DamageInfo.DamageType.HP_LOSS;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        int numTargets = 1;
        for (AbstractMonster abstractMonster : AbstractDungeon.getMonsters().monsters) {
            if (!abstractMonster.isDeadOrEscaped()) {
                numTargets++;
            }
        }

        if (AbstractDungeon.cardRandomRng.random(1, numTargets) == 1) {
            p.currentHealth = 1;
            p.healthBarUpdatedEvent();
            //AbstractDungeon.actionManager.addToBottom(new InstantKillAction(p)); // Sad day...
            AbstractDungeon.actionManager.addToBottom(
                    new DamageAction(p, new DamageInfo(p, damage, damageTypeForTurn),
                            AbstractGameAction.AttackEffect.FIRE));
        } else {
            AbstractMonster m2 = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
            m2.currentHealth = 1;
            m2.healthBarUpdatedEvent();
            AbstractDungeon.actionManager.addToBottom(
                    new DamageAction(m2, new DamageInfo(p, damage, damageTypeForTurn),
                            AbstractGameAction.AttackEffect.FIRE));
            //AbstractDungeon.actionManager.addToBottom(new SuicideAction(m2));

        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.selfRetain = true;
            rawDescription = UPGRADE_DESCRIPTION;
            upgradeBaseCost(UPGRADE_REDUCED_COST);
            initializeDescription();
        }
    }
}