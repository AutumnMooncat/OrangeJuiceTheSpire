package Moonworks.cards.trapCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.cards.abstractCards.AbstractTrapCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.PlushieMasterPower;
import basemod.BaseMod;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class PlushieMaster extends AbstractTrapCard {

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

    private static final int COST = 1;

    private static final int HITS = 4;
    private static final int UPGRADE_PLUS_HITS = 2;

    private static final int TEMP_HP = 3;
    private static final int DAMAGE = 7;
    private static final int STACK_MULTIPLE = 1;

    private static final Integer[] NORMA_LEVELS = {2};

    // /STAT DECLARATION/


    public PlushieMaster() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, NORMA_LEVELS);
        magicNumber = baseMagicNumber = HITS;
        damage = baseDamage = DAMAGE;
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.MAGICMOD, 2, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Map<AbstractMonster, Integer> map = new HashMap<>();
        for (int i = 0 ; i < magicNumber ; i++) {
            AbstractMonster aM = AbstractDungeon.getRandomMonster();
            map.put(aM, map.getOrDefault(aM, 0)+1);
        }
        for (AbstractMonster aM : map.keySet()) {
            this.addToBot(new ApplyPowerAction(aM, p, new PlushieMasterPower(aM, p, map.get(aM), TEMP_HP, DAMAGE)));
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //rawDescription = UPGRADE_DESCRIPTION;
            upgradeMagicNumber(UPGRADE_PLUS_HITS);
            initializeDescription();
        }
    }
}