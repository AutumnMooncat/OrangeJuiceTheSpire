package Moonworks.cards;

import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.patches.FixedPatches;
import Moonworks.relics.LittleGull;
import Moonworks.vfx.VFXContainer;
import basemod.helpers.CardModifierManager;
import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;
import java.util.function.BiFunction;

import static Moonworks.OrangeJuiceMod.*;

public class CloudOfSeagulls extends AbstractNormaAttentiveCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(CloudOfSeagulls.class.getSimpleName());
    public static final String IMG = makeCardPath("CloudOfSeagulls.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    //public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String SELF_DAMAGE_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION[1];

    //public static final gullImage =
    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;
    private static final int DAMAGE = 2;
    private static final int UPGRADE_PLUS_DAMAGE = 1;
    private static final int GULLS_ADDED = 1;
    private static final int HITS = 2;
    private static final int UPGRADE_PLUS_HITS = 1;
    private static final int CLOUDS_PER_THROW = 3;

    //private static final int UPGRADE_PLUS_DAMAGE = 1;

    //private static final AbstractCard card = new JonathanRush();

    private static final Integer[] NORMA_LEVELS = {2};

    // /STAT DECLARATION/


    public CloudOfSeagulls() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, NORMA_LEVELS);
        this.cardsToPreview = new JonathanRush();
        thirdMagicNumber = baseThirdMagicNumber = DAMAGE;
        magicNumber = baseMagicNumber = GULLS_ADDED;
        secondMagicNumber = baseSecondMagicNumber = HITS;
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.INFOMOD, 1, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int k = 0 ; k < secondMagicNumber ; k++) {
            ArrayList<AbstractCreature> validTargets = new ArrayList<>();
            if (enableSelfDamage || !upgraded) {
                validTargets.add(p);
            }
            for (AbstractMonster abstractMonster : AbstractDungeon.getMonsters().monsters) {
                if (!abstractMonster.isDeadOrEscaped() && abstractMonster.currentHealth > 0) {
                    validTargets.add(abstractMonster);
                }
            }
            if (validTargets.size() > 0) {
                AbstractCreature t = validTargets.get(AbstractDungeon.cardRandomRng.random(0, validTargets.size()-1));
                final boolean hasThorns = t.hasPower(ThornsPower.POWER_ID); //We use a different animation for thorns, once I figure out how

                if (!disableGullVfx) {
                    //Gull Cloud
                    for (int j = 0 ; j < CLOUDS_PER_THROW ; j++) {
                        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                            public void update() {
                                for (int i = 0 ; i < AbstractDungeon.cardRandomRng.random(15, 20) ; i++) {
                                    float startX = MathUtils.random((float) Settings.WIDTH * 0.1F, (float)Settings.WIDTH * 0.9F);
                                    int angleCorrect = startX > Settings.WIDTH * 0.5F ? 0 : 90;
                                    boolean flip = startX < Settings.WIDTH * 0.5F;
                                    AbstractGameEffect cloudGull = new VfxBuilder(flip ? VFXContainer.GULL_ATTACK_TEXTURE : VFXContainer.GULL_ATTACK_TEXTURE_FLIP, startX, Settings.HEIGHT, 1.8f)
                                            .setScale(MathUtils.random(0.17f,0.23f))
                                            .gravity(-50f)
                                            .velocity(MathUtils.random(180f, 270f)+angleCorrect, MathUtils.random(800f, 1200f))
                                            .rotate(MathUtils.random(10f, 40f) * (flip ? -1 : 1))
                                            .build();
                                    AbstractDungeon.effectList.add(cloudGull);
                                }
                                this.isDone = true;
                            }
                        });
                    }
                    //Gull Attack
                    AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                        public void update() {
                            float startX = MathUtils.random((float) Settings.WIDTH * 0.25F, (float)Settings.WIDTH * 0.75F);
                            boolean flip = startX > t.drawX;
                            //Normal attack
                            AbstractGameEffect hurtGull = new VfxBuilder(flip ? VFXContainer.GULL_HURT_TEXTURE : VFXContainer.GULL_HURT_TEXTURE_FLIP, t.drawX, t.drawY, 1.5f)
                                    .setScale(0.2f)
                                    //.moveX(t.drawX, t.drawX + (flip ? Settings.WIDTH * 0.05F : -Settings.WIDTH * 0.05F), VfxBuilder.Interpolations.LINEAR)
                                    //.moveY(t.drawY, Settings.HEIGHT/3f, VfxBuilder.Interpolations.CIRCLE)
                                    .gravity(50f)
                                    .velocity(MathUtils.random(45f, 135f), MathUtils.random(600f, 800f))
                                    .rotate(MathUtils.random(50f, 200f) * (MathUtils.randomBoolean() ? -1 : 1))
                                    .build();
                            AbstractGameEffect shootGull = new VfxBuilder(flip ? VFXContainer.GULL_ATTACK_TEXTURE : VFXContainer.GULL_ATTACK_TEXTURE_FLIP, startX, Settings.HEIGHT, 0.5f)
                                    .scale(0.2f, 0.25f, VfxBuilder.Interpolations.SWINGIN)
                                    .moveX(startX, t.drawX)
                                    .moveY(Settings.HEIGHT, t.drawY)
                                    .rotate(MathUtils.random(50f, 200f) * (flip ? -1 : 1))
                                    .build();
                            AbstractGameEffect shootGullAtThorns = new VfxBuilder(flip ? VFXContainer.GULL_ATTACK_TEXTURE : VFXContainer.GULL_ATTACK_TEXTURE_FLIP, startX, Settings.HEIGHT, 0.5f)
                                    .scale(0.2f, 0.25f, VfxBuilder.Interpolations.SWINGIN)
                                    .moveX(startX, t.drawX)
                                    .moveY(Settings.HEIGHT, t.drawY)
                                    .rotate(MathUtils.random(50f, 200f) * (flip ? -1 : 1))
                                    .triggerVfxAt(0.5F, 1, (aFloat, aFloat2) -> hurtGull)
                                    .build();
                            AbstractDungeon.effectList.add(hasThorns ? shootGullAtThorns : shootGull);
                            //AbstractDungeon.effectList.add(hurtGull);
                            this.isDone = true;
                        }
                    });
                }

                //Do the damage
                DamageInfo fixedDamage = new DamageInfo(p, thirdMagicNumber, damageTypeForTurn);
                FixedPatches.FixedField.fixed.set(fixedDamage, true);
                this.addToBot(new DamageAction(t, fixedDamage, hasThorns ? AbstractGameAction.AttackEffect.SLASH_HORIZONTAL : AbstractGameAction.AttackEffect.BLUNT_LIGHT, true));

            }

        }

        //Add the Jonathan Rush
        this.addToBot(new MakeTempCardInDrawPileAction(cardsToPreview.makeStatEquivalentCopy(), magicNumber, true, true));

        //Draw a card if we pass the Norma check
        if(getNormaLevel() >= 2) {
            this.addToBot(new DrawCardAction(1));
        }

        //If you have Little Gull, trigger it
        if (AbstractDungeon.player.hasRelic(LittleGull.ID)) {
            LittleGull lg = (LittleGull) AbstractDungeon.player.getRelic(LittleGull.ID);
            lg.doGullDamage();
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            cardsToPreview.upgrade();
            //upgradeThirdMagicNumber(UPGRADE_PLUS_DAMAGE);
            upgradeSecondMagicNumber(UPGRADE_PLUS_HITS);
            if (!enableSelfDamage) {
                target = CardTarget.ALL_ENEMY;
                rawDescription = UPGRADE_DESCRIPTION;
            } else {
                rawDescription = SELF_DAMAGE_DESCRIPTION;
            }

            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return super.makeCopy();
    }
}