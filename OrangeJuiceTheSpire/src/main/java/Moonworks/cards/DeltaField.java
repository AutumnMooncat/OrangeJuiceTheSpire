package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.powers.TemporaryStrengthPower;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class DeltaField extends AbstractNormaAttentiveCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(DeltaField.class.getSimpleName());
    public static final String IMG = makeCardPath("DeltaField.png");

    //private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //public static final String SPENT_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;

    private static final int VULNERABLE = 1;
    private static final int UPGRADE_PLUS_VULNERABLE = 1;

    private static final int TEMP_STR_LOSS = 1;
    private static final int UPGRADE_PLUS_TEMP_STR_LOSS = 1;

    private static final Integer[] NORMA_LEVELS = {2};


    // /STAT DECLARATION/


    public DeltaField() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = VULNERABLE;
        this.secondMagicNumber = this.baseSecondMagicNumber = TEMP_STR_LOSS;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //Play a delayed Attack animation, it will finish after the forward lightning propagation
        this.addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                if (p instanceof TheStarBreaker) {
                    ((TheStarBreaker) p).playAnimation("attack");
                }
                this.isDone = true;
            }
        });
        //For all monsters...
        for (AbstractMonster aM : AbstractDungeon.getMonsters().monsters) {
            //If they are alive...
            if (!aM.isDeadOrEscaped()) {
                //Play VFX
                this.addToTop(new VFXAction(new LightningEffect(aM.drawX, aM.drawY)));

                //Make sounds
                this.addToTop(new SFXAction("ORB_LIGHTNING_CHANNEL"));
                this.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));
                this.addToTop(new SFXAction("ORB_LIGHTNING_PASSIVE"));
                this.addToTop(new SFXAction("POWER_SHACKLE"));

                //Shake
                CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.SHORT, false);

                //Apply Vulnerable
                this.addToBot(new ApplyPowerAction(aM, p, new VulnerablePower(aM, magicNumber, false), magicNumber));

                //Apply negative Temp Str
                this.addToBot(new ApplyPowerAction(aM, p, new TemporaryStrengthPower(aM, -secondMagicNumber), -secondMagicNumber));
            }

        }

    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //upgradeMagicNumber(UPGRADE_PLUS_VULNERABLE);
            upgradeSecondMagicNumber(UPGRADE_PLUS_TEMP_STR_LOSS);
            initializeDescription();
        }
    }
}
