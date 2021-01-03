package Moonworks.cards;

import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
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

import static Moonworks.OrangeJuiceMod.*;

public class CloudOfSeagulls extends AbstractNormaAttentiveCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(CloudOfSeagulls.class.getSimpleName());
    public static final String IMG = makeCardPath("CloudOfSeagulls.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String SELF_DAMAGE_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION[1];
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DAMAGE = 2;
    private static final int GULLS = 1;

    //private static final int UPGRADE_PLUS_DAMAGE = 1;

    //private static final AbstractCard card = new JonathanRush();

    private static final Integer[] NORMA_LEVELS = {2};

    // /STAT DECLARATION/


    public CloudOfSeagulls() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, NORMA_LEVELS);
        this.cardsToPreview = new JonathanRush();
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = GULLS;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (enableSelfDamage || !upgraded) {
            int numTargets = 1;
            for (AbstractMonster abstractMonster : AbstractDungeon.getMonsters().monsters) {
                if (!abstractMonster.isDeadOrEscaped()) {
                    numTargets++;
                }
            }
            if (AbstractDungeon.cardRandomRng.random(1, numTargets) == 1) {
                this.addToBot(new DamageAction(p, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            } else {
                this.addToBot(new AttackDamageRandomEnemyAction(this, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            }
        } else {
            this.addToBot(new AttackDamageRandomEnemyAction(this, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }

        /*for(int i = 0; i < this.magicNumber; ++i) {
            this.addToBot(new MakeTempCardInHandAction(cardsToPreview.makeStatEquivalentCopy()));
        }*/
        this.addToBot(new MakeTempCardInDrawPileAction(cardsToPreview.makeStatEquivalentCopy(), magicNumber, true, true));
        if(getNormaLevel() >= 2) {
            this.addToBot(new DrawCardAction(1));
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            cardsToPreview.upgrade();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            if (!enableSelfDamage) {
                target = CardTarget.ALL_ENEMY;
            } else {
                UPGRADE_DESCRIPTION = SELF_DAMAGE_DESCRIPTION;
            }
            //upgradeMagicNumber(UPGRADE_PLUS_HITS);
            initializeDescription();
        }
    }
}