package Moonworks.cards.tempCards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractTempCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.BlastingLightPower;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class StarBlastingLight extends AbstractTempCard {

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

    private static final Integer[] NORMA_LEVELS = {-1};

    // /STAT DECLARATION/


    public StarBlastingLight() {
        super(ID, IMG, COST, TYPE, COLOR, TARGET, NORMA_LEVELS);
        //damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = MIN_HITS;
        secondMagicNumber = baseSecondMagicNumber = MAX_HITS;
        //this.setDisplayRarity(CardRarity.RARE);
        this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        //this.bannerColor = BANNER_COLOR_RARE.cpy();
        //this.imgFrameColor = IMG_FRAME_COLOR_RARE.cpy();
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.MAGICMOD, 1, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.SECONDMAGICMOD, 1, NORMA_LEVELS[0], null));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        TALK_TEXT = cardStrings.EXTENDED_DESCRIPTION[AbstractDungeon.cardRandomRng.random(1, 3)];
        this.addToBot(new VFXAction(p, new ScreenOnFireEffect(), 0.0F));
        this.addToBot(new TalkAction(true, TALK_TEXT, 0.4f, 2.0f));
        logger.info("Magic: "+magicNumber+". Base Magic: "+baseMagicNumber+".");
        logger.info("Second: "+ secondMagicNumber +". Base Second: "+ baseSecondMagicNumber +".");
        int hits = AbstractDungeon.cardRandomRng.random(magicNumber, secondMagicNumber);
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

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //rawDescription = UPGRADE_DESCRIPTION;
            upgradeMagicNumber(UPGRADE_PLUS_MIN_HITS);
            upgradeSecondMagicNumber(UPGRADE_PLUS_MAX_HITS);
            initializeDescription();
        }
    }
}