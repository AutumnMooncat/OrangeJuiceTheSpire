package Moonworks.cards;

import Moonworks.actions.AnotherUltimateWeaponAction;
import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.powers.FreeCardPower;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class AnotherUltimateWeapon extends AbstractNormaAttentiveCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In-Progress Form At the start of your turn, play a TOUCH.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(AnotherUltimateWeapon.class.getSimpleName());
    public static final String IMG = makeCardPath("AnotherUltimateWeapon.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = -1;

    private static final int STR = 0;
    private static final int BLOCK = 0;

    private static final Integer[] NORMA_LEVELS = {-1};

    // /STAT DECLARATION/


    public AnotherUltimateWeapon() {

        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, NORMA_LEVELS);
        //this.tags.add(BaseModCardTags.FORM); //Tag your strike, defend and form cards so that they work correctly.
        this.baseMagicNumber = this.magicNumber = STR;
        this.baseBlock = this.block = BLOCK;
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.MAGICMOD, 1, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.BLOCKMOD, 5, NORMA_LEVELS[0], null));


    }
    @Override
    public float getTitleFontSize() {
        return 17F;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber)));
        //this.addToBot(new GainBlockAction(p, block));
        this.addToBot(new AnotherUltimateWeaponAction(p, this.magicNumber, this.block, this.upgraded, this.freeToPlayOnce, this.energyOnUse));
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
