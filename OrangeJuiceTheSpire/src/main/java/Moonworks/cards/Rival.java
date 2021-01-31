package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.RivalPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class Rival extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In-Progress Form At the start of your turn, play a TOUCH.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(Rival.class.getSimpleName());
    public static final String IMG = makeCardPath("Rival.png");
    public static final String IMG2 = makeCardPath("RivalEdit.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;

    // /STAT DECLARATION/


    public Rival() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //Start at the first monster
        AbstractCreature target = AbstractDungeon.getMonsters().monsters.get(0);

        //If the first one doesnt have Rival, find the healthiest one, or whoever current has Rival
        if (!target.hasPower(RivalPower.POWER_ID)) {
            for (AbstractCreature newTarget : AbstractDungeon.getMonsters().monsters) {
                if (!newTarget.isDeadOrEscaped()) {
                    //If they have Rival, stop looking
                    if (newTarget.hasPower(RivalPower.POWER_ID)) {
                        target = newTarget;
                        break;
                    }
                    //Else get whichever one has the most HP
                    if (newTarget.currentHealth > target.currentHealth) {
                        target = newTarget;
                    }
                }
            }
        }


        //Make it your Rival
        this.addToBot(new ApplyPowerAction(target, p, new RivalPower(target, 1)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            initializeDescription();
        }
    }
}
