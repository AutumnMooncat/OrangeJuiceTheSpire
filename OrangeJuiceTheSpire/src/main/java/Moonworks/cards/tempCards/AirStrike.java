package Moonworks.cards.tempCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.cards.abstractCards.AbstractTempCard;
import Moonworks.cards.abstractCards.AbstractTrapCard;
import Moonworks.characters.TheStarBreaker;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.defect.DamageAllButOneEnemyAction;
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

public class AirStrike extends AbstractTempCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(AirStrike.class.getSimpleName());
    public static final String IMG = makeCardPath("AirStrike.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 2;

    private static final int DAMAGE = 20;
    private static final int UPGRADE_PLUS_DMG = 10;

    // /STAT DECLARATION/


    public AirStrike() {
        super(ID, IMG, COST, TYPE, COLOR, TARGET);
        baseDamage = DAMAGE;
        //this.setDisplayRarity(CardRarity.RARE);
        this.isMultiDamage = true;
        //this.isInAutoplay = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped()) {
                int index = AbstractDungeon.getMonsters().monsters.indexOf(aM);
                this.addToBot(new DamageAction(aM, new DamageInfo(p, multiDamage[index], damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE, true));
                for (AbstractMonster aM2: AbstractDungeon.getMonsters().monsters) {
                    if (aM2 != aM && !aM2.isDeadOrEscaped()) {
                        this.addToBot(new DamageAction(aM2, new DamageInfo(p, multiDamage[index]/2, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE, true));
                    }
                }
            }
        }

        for (AbstractPower pow : p.powers) {
            pow.onDamageAllEnemies(multiDamage);
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //rawDescription = UPGRADE_DESCRIPTION;
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}