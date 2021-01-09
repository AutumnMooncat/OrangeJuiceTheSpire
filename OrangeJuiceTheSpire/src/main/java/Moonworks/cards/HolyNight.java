package Moonworks.cards;

import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.powers.DrawPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class HolyNight extends AbstractNormaAttentiveCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In-Progress Form At the start of your turn, play a TOUCH.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(HolyNight.class.getSimpleName());
    public static final String IMG = makeCardPath("HolyNight.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;

    private static final int DRAW = 1;

    private static final Integer[] NORMA_LEVELS = {3};

    // /STAT DECLARATION/


    public HolyNight() {

        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, NORMA_LEVELS);
        this.magicNumber = this.baseMagicNumber = DRAW;
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.INFOMOD, 1, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new DrawPower(p, magicNumber)));
        if (getNormaLevel() >= NORMA_LEVELS[0]) {
            if (this.freeToPlayOnce || EnergyPanel.totalCount >= 2) {
                if (!this.freeToPlayOnce) {
                    p.energy.use(1);
                }
                this.addToBot(new ApplyPowerAction(p, p, new DrawPower(p, magicNumber)));
            }
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.isInnate = true;
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
