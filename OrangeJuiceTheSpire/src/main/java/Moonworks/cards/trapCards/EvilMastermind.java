package Moonworks.cards.trapCards;

import Moonworks.cardModifiers.EvilMastermindHelperModifier;
import Moonworks.cards.abstractCards.AbstractTrapCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.BigBangBellPower;
import basemod.BaseMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import jdk.nashorn.internal.ir.annotations.Ignore;

import java.util.ArrayList;
import java.util.HashMap;

import static Moonworks.OrangeJuiceMod.makeCardPath;
import static Moonworks.OrangeJuiceMod.makeID;

public class EvilMastermind extends AbstractTrapCard {

    // TEXT DECLARATION

    public static final String ID = makeID(EvilMastermind.class.getSimpleName());
    public static final String IMG = makeCardPath("EvilMastermind.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NORMAL_DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTRA_DESCRIPTIONS = cardStrings.EXTENDED_DESCRIPTION;

    // /TEXT DECLARATION/

    // STORED INFORMATION

    private ArrayList<AbstractTrapCard> trapCardArrayList = new ArrayList<>();
    private HashMap<String, Integer> trapAmounts = new HashMap<>();

    public ArrayList<AbstractTrapCard> getTrapCardArrayList() {
        return trapCardArrayList;
    }

    public HashMap<String, Integer> getTrapAmounts() {
        return trapAmounts;
    }

    // /STORED INFORMATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 2;

    // /STAT DECLARATION/


    public EvilMastermind() { //Oh look, yet another card that I should really really be using actions for, lol.

        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
        if (AbstractDungeon.player != null) {
            findTrapCards();
        }
        //upgrade(); // for testing
        CardModifierManager.addModifier(this, new EvilMastermindHelperModifier(EXTENDED_DESCRIPTION));
        initializeDescription();
    }

    private void findTrapCards() {
        //Empty the current lists first
        trapAmounts.clear();
        trapCardArrayList.clear();

        //Loop through hand, draw pile, etc.
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            //If we find a Trap card that IS NOT an EvilMastermind (Because oh god the recursion)
            if (c instanceof AbstractTrapCard && !(c instanceof EvilMastermind)) {
                //Add it to our Array of found Traps
                trapCardArrayList.add((AbstractTrapCard) c.makeStatEquivalentCopy());
            }
        }
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c instanceof AbstractTrapCard && !(c instanceof EvilMastermind)) {
                trapCardArrayList.add((AbstractTrapCard) c.makeStatEquivalentCopy());
            }
        }
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c instanceof AbstractTrapCard && !(c instanceof EvilMastermind)) {
                trapCardArrayList.add((AbstractTrapCard) c.makeStatEquivalentCopy());
            }
        }
        for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
            if (c instanceof AbstractTrapCard && !(c instanceof EvilMastermind)) {
                trapCardArrayList.add((AbstractTrapCard) c.makeStatEquivalentCopy());
            }
        }
        //Grab the trap cards by name, and dump them into a map. Because maps. Mainly because we need to know the # of unique traps later
        for(AbstractTrapCard t : this.trapCardArrayList) {
            //Queue infomercial: THERES GOT TO BE A BETTER WAY! I do like maps though
            //Take the + and ? off the end of the name so upgraded and non-upgraded count as the same thing
            trapAmounts.put(t.name.replace("+","").replace("?",""), trapAmounts.getOrDefault(t.name.replace("+","").replace("?",""), 0)+1);
        }
    }

    @Override
    public void applyPowers() {
        findTrapCards();
        initializeDescription();
        super.applyPowers();
    }

    // Actions the card should do. This is going to get hecking messy
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //----------This needs to be put in an action for goodness' sake---------//
        AbstractMonster aM = AbstractDungeon.getRandomMonster();
        for(AbstractTrapCard t : this.trapCardArrayList){
            t.calculateCardDamage(aM);
            t.use(p, aM);
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.isInnate = true;
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}