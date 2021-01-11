package Moonworks.cards.trapCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.cards.abstractCards.AbstractTrapCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.InvisibleBombPower;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class StarBlastingFuse extends AbstractTrapCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(StarBlastingFuse.class.getSimpleName());
    public static final String IMG = makeCardPath("StarBlastingFuse.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static String TALK_TEXT;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 2;
    private static final int HP_LOSS = 30;
    private static final int UPGRADE_PLUS_HP_LOSS = 10;

    // /STAT DECLARATION/


    public StarBlastingFuse() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = HP_LOSS;
        shuffleBackIntoDrawPile = true;
        //this.isMultiDamage = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        TALK_TEXT = cardStrings.EXTENDED_DESCRIPTION[AbstractDungeon.cardRandomRng.random(0, 2)];
        this.addToBot(new TalkAction(true, TALK_TEXT, 4.0f, 2.0f));

        this.addToBot(new ApplyPowerAction(m, p, new InvisibleBombPower(m, p)));
        shuffleBackIntoDrawPile = true;
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        //Blow up bombs here
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m.getPower(InvisibleBombPower.POWER_ID) != null) {
                ((InvisibleBombPower)m.getPower(InvisibleBombPower.POWER_ID)).blast(magicNumber);
            }
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_HP_LOSS);
            initializeDescription();
        }
    }
}