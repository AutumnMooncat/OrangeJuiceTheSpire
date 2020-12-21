package Moonworks.cards.trapCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
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

public class StarBlastingFuse extends AbstractNormaAttentiveCard {

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
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 2;
    private static final int DAMAGE = 10;
    private static final int MIN_HITS = 3;
    private static final int UPGRADE_PLUS_MIN_HITS = 1;
    private static final int MAX_HITS = 5;
    private static final int UPGRADE_PLUS_MAX_HITS = 1;

    // /STAT DECLARATION/


    public StarBlastingFuse() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = MIN_HITS;
        defaultSecondMagicNumber = defaultBaseSecondMagicNumber = MAX_HITS;
        //this.isMultiDamage = true;
        //this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        //this.bannerColor = BANNER_COLOR_RARE.cpy();
        //this.imgFrameColor = IMG_FRAME_COLOR_RARE.cpy();
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
        TALK_TEXT = cardStrings.EXTENDED_DESCRIPTION[AbstractDungeon.cardRandomRng.random(0, 2)];
        this.addToBot(new TalkAction(true, TALK_TEXT, 4.0f, 2.0f));
        //this.addToBot(new VFXAction(p, new ScreenOnFireEffect(), 1.0F));
        int hits = AbstractDungeon.cardRandomRng.random(magicNumber, defaultSecondMagicNumber);
        for (int i = 0 ; i < hits ; i++){
            aM = AbstractDungeon.getRandomMonster();
            this.addToBot(new ApplyPowerAction(aM, p, new InvisibleBombPower(aM, p, damage, 1)));
            //this.addToBot(new DamageAllEnemiesAction(p, damage, damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
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