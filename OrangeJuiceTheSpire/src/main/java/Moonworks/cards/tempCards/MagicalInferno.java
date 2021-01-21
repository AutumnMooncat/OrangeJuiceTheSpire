package Moonworks.cards.tempCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.cards.abstractCards.AbstractMagicalCard;
import Moonworks.cards.abstractCards.AbstractTempCard;
import Moonworks.characters.TheStarBreaker;
import basemod.BaseMod;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import java.util.ArrayList;
import java.util.List;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class MagicalInferno extends AbstractMagicalCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(MagicalInferno.class.getSimpleName());
    public static final String IMG = makeCardPath("MagicalInferno.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 0;
    private static final int DAMAGE = 8;
    private static final int UPGRADE_PLUS_DMG = 2;

    private static final int VIGOR = 6;
    private static final int UPGRADE_PLUS_VIGOR = 2;

    private static final Integer[] NORMA_LEVELS = {-1};

    // /STAT DECLARATION/


    public MagicalInferno() {
        super(ID, IMG, COST, TYPE, COLOR, TARGET);
        baseDamage = damage = DAMAGE;
        baseMagicNumber = magicNumber = VIGOR;
        this.isMultiDamage = true;
        //this.setDisplayRarity(CardRarity.RARE);
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.DAMAGEMOD, -1, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.MAGICMOD, -1, NORMA_LEVELS[0], null));

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        /*int hits = 0;
        //if(true){
            for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters){
                if (!aM.isDeadOrEscaped()){
                    hits++;
                }
            }
        //} else { //Super buggy, bypassing code with if (true)
        //    hits = AbstractDungeon.getMonsters().monsters.size();
        //}

        for (int i = 0 ; i < hits ; i++){
            this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
        }*/
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            if (!aM.isDeadOrEscaped()) {
                int index = AbstractDungeon.getMonsters().monsters.indexOf(aM);
                this.addToBot(new DamageAction(aM, new DamageInfo(p, multiDamage[index], damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE, true));
                for (AbstractMonster aM2: AbstractDungeon.getMonsters().monsters) {
                    if (aM2 != aM && !aM2.isDeadOrEscaped()) {
                        this.addToBot(new DamageAction(aM2, new DamageInfo(p, multiDamage[index]/2, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE, true));
                    }
                }
            }
        }

        this.addToBot(new ApplyPowerAction(p, p, new VigorPower(p, magicNumber)));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //rawDescription = UPGRADE_DESCRIPTION; //Upgrade use buggy, removing updated description
            upgradeDamage(UPGRADE_PLUS_DMG); //Uncommented
            upgradeMagicNumber(UPGRADE_PLUS_VIGOR);
            initializeDescription();
        }
    }
}