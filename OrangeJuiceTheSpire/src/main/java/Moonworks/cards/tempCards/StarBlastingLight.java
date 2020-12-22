package Moonworks.cards.tempCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.BlastingLightPower;
import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;

import java.util.ArrayList;
import java.util.List;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class StarBlastingLight extends AbstractNormaAttentiveCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(StarBlastingLight.class.getSimpleName());
    public static final String IMG = makeCardPath("StarBlastingLight.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static String TALK_TEXT;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 0;

    private static final int MIN_HITS = 3;
    private static final int UPGRADE_PLUS_MIN_HITS = 1;
    private static final int MAX_HITS = 5;
    private static final int UPGRADE_PLUS_MAX_HITS = 2;

    // /STAT DECLARATION/


    public StarBlastingLight() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        //damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = MIN_HITS;
        defaultSecondMagicNumber = defaultBaseSecondMagicNumber = MAX_HITS;
        this.purgeOnUse = true;
        //this.setDisplayRarity(CardRarity.RARE);
        this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        setBackgroundTexture(OrangeJuiceMod.TEMP_SKILL_WHITE_ICE, OrangeJuiceMod.TEMP_SKILL_WHITE_ICE_PORTRAIT);
        //this.bannerColor = BANNER_COLOR_RARE.cpy();
        //this.imgFrameColor = IMG_FRAME_COLOR_RARE.cpy();
    }
    public List<String> getCardDescriptors() {
        List<String> tags = new ArrayList<>();
        tags.add("Special");
        return tags;
    }

    private static ArrayList<TooltipInfo> specialTooltip;
    @Override
    public List<TooltipInfo> getCustomTooltipsTop() {
        if (specialTooltip == null)
        {
            specialTooltip = new ArrayList<>();
            specialTooltip.add(new TooltipInfo(BaseMod.getKeywordTitle("moonworks:Special"), BaseMod.getKeywordDescription("moonworks:Special")));
        }
        return specialTooltip;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        TALK_TEXT = cardStrings.EXTENDED_DESCRIPTION[AbstractDungeon.cardRandomRng.random(0, 2)];
        this.addToBot(new TalkAction(true, TALK_TEXT, 4.0f, 2.0f));
        this.addToBot(new VFXAction(p, new ScreenOnFireEffect(), 1.0F));
        int hits = AbstractDungeon.cardRandomRng.random(magicNumber, defaultSecondMagicNumber);
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            AbstractPower pow = aM.getPower(ArtifactPower.POWER_ID);
            int artifactAmount = 0;
            if (pow != null) {
                artifactAmount = pow.amount;
                this.addToTop(new ReducePowerAction(aM, p, pow, hits));
            }
            if (hits > artifactAmount) {
                this.addToBot(new ApplyPowerAction(aM, p, new BlastingLightPower(aM, hits-artifactAmount), hits-artifactAmount, true));
            }
        }
    }

    @Override
    public void applyPowers() {
        this.magicNumber = this.baseMagicNumber;
        this.isMagicNumberModified = false;
        this.defaultSecondMagicNumber = this.defaultBaseSecondMagicNumber;
        this.isDefaultSecondMagicNumberModified = false;
        super.applyPowers();
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        int bonus = getNormaLevel();
        super.calculateCardDamage(m);
        if (bonus > 0) {
            this.magicNumber += bonus;
            this.defaultSecondMagicNumber += bonus;
            this.isMagicNumberModified = true;
            this.isDefaultSecondMagicNumberModified = true;
        }
        initializeDescription();
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