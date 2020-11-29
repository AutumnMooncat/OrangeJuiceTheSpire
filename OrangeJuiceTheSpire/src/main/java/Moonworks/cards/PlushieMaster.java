package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.PlushieMasterPower;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class PlushieMaster extends AbstractNormaAttentiveCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(PlushieMaster.class.getSimpleName());
    public static final String IMG = makeCardPath("PlushieMaster.png");

    //private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 2;

    private static final int MIN_HITS = 3;
    private static final int UPGRADE_PLUS_MIN_HITS = 1;

    private static final int MAX_HITS = 5;
    private static final int UPGRADE_PLUS_MAX_HITS = 1;

    private static final int TEMP_HP = 2;
    private static final int DAMAGE = 2;
    private static final int STACK_MULTIPLE = 1;

    // /STAT DECLARATION/


    public PlushieMaster() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MIN_HITS;
        damage = baseDamage = DAMAGE;
        defaultSecondMagicNumber = defaultBaseSecondMagicNumber = MAX_HITS;
        setBackgroundTexture(OrangeJuiceMod.TRAP_WHITE_ICE, OrangeJuiceMod.TRAP_WHITE_ICE_PORTRAIT);
    }
    public List<String> getCardDescriptors() {
        List<String> tags = new ArrayList<>();
        tags.add("Trap");
        return tags;
    }
    private static ArrayList<TooltipInfo> TrapTooltip;
    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        if (TrapTooltip == null)
        {
            TrapTooltip = new ArrayList<>();
            TrapTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("moonworks:Trap"), BaseMod.getKeywordDescription("moonworks:Trap")));
        }
        return TrapTooltip;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractMonster aM;
        int hits = AbstractDungeon.cardRandomRng.random(magicNumber, defaultSecondMagicNumber);
        int bonus1 = 0, bonus2 = 0;
        switch (getNormaLevel()) {
            case 5:
            case 4:
            case 3:
            case 2: bonus2 = 1;
            case 1: bonus1 = 1;
            default:
        }
        for (int i = 0 ; i < hits ; i++) {
            aM = AbstractDungeon.getRandomMonster();
            this.addToBot(new ApplyPowerAction(aM, p, new PlushieMasterPower(aM, p, STACK_MULTIPLE, TEMP_HP+bonus2, DAMAGE+bonus1)));
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //rawDescription = UPGRADE_DESCRIPTION;
            upgradeMagicNumber(UPGRADE_PLUS_MIN_HITS);
            upgradeDefaultSecondMagicNumber(UPGRADE_PLUS_MAX_HITS);
            initializeDescription();
        }
    }
}