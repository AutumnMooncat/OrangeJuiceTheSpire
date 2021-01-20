package Moonworks.cards;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.UpgradeRushAction;
import Moonworks.cards.abstractCards.AbstractDynamicCard;
import Moonworks.characters.TheStarBreaker;
import Moonworks.relics.LittleGull;
import Moonworks.vfx.VFXContainer;
import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;
import java.util.function.BiFunction;

import static Moonworks.OrangeJuiceMod.*;

public class JonathanRush extends AbstractDynamicCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * Big Slap Deal 10(15)) damage.
     */

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(JonathanRush.class.getSimpleName());
    public static final String IMG = makeCardPath("JonathanRush.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 0;
    private static final int DAMAGE = 3;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int BONUS_DAMAGE = 2;
    private static final int UPGRADE_PLUS_BONUS_DAMAGE = 1;

    // /STAT DECLARATION/


    public JonathanRush() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        AutoplayField.autoplay.set(this, true);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = BONUS_DAMAGE;
        //this.returnToHand = true;
        //this.isInAutoplay = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCreature> validTargets = new ArrayList<>();
        for (AbstractMonster abstractMonster : AbstractDungeon.getMonsters().monsters) {
            if (!abstractMonster.isDeadOrEscaped()) {
                validTargets.add(abstractMonster);
            }
        }
        if (validTargets.size() > 0) {
            AbstractCreature t = validTargets.get(AbstractDungeon.cardRandomRng.random(0, validTargets.size()-1));
            calculateCardDamage((AbstractMonster) t);
            boolean hasThorns = t.hasPower(ThornsPower.POWER_ID); //We use a different animation for thorns, once I figure out how

            //If we have not disabled gull vfx...
            if (!disableGullVfx) {
                //Throws multiple gulls at high attack damage
                int gullThrows = Math.min(20, Math.max(1, MathUtils.floor(damage / 3f)));
                for (int gulls = 0 ; gulls < gullThrows ; gulls++) {
                    AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                        public void update() {
                            //Gull Attack
                            float startX, startY = Settings.HEIGHT;
                            startX = MathUtils.random((float) Settings.WIDTH * 0.0F, (float)Settings.WIDTH * 1.0F);
                    /*if (AbstractDungeon.cardRandomRng.random(0, 1) == 0) {
                        startX = MathUtils.random((float) Settings.WIDTH * 0.0F, (float)Settings.WIDTH * 1.0F);
                    } else {
                        startY = MathUtils.random((float) Settings.HEIGHT * 0.8F, (float)Settings.HEIGHT * 1.0F);
                    }*/
                            float m = (t.drawY - startY) / (t.drawX - startX);
                            float b = startY - (m * startX);
                            float yIntercept = m * Settings.WIDTH + b;
                            float xIntercept = -b / m;
                            float targetX, targetY;
                            if (xIntercept > Settings.WIDTH) {
                                // Y intercept happens first
                                targetX = Settings.WIDTH;
                                targetY = yIntercept;
                            } else {
                                targetX = xIntercept;
                                targetY = 0;
                            }
                            boolean flip = startX > t.drawX;

                            //Normal attack
                            AbstractGameEffect hurtGull = new VfxBuilder(VFXContainer.GULL_HURT_TEXTURE, t.drawX, t.drawY, 1.5f)
                                    .setScale(0.22f)
                                    //.moveX(t.drawX, t.drawX + (flip ? Settings.WIDTH * 0.05F : -Settings.WIDTH * 0.05F), VfxBuilder.Interpolations.LINEAR)
                                    //.moveY(t.drawY, Settings.HEIGHT/3f, VfxBuilder.Interpolations.CIRCLE)
                                    .gravity(50f)
                                    .velocity(MathUtils.random(45f, 135f), MathUtils.random(600f, 800f))
                                    .rotate(MathUtils.random(50f, 200f) * (MathUtils.randomBoolean() ? -1 : 1))
                                    .build();
                            AbstractGameEffect shootGull = new VfxBuilder(flip ? VFXContainer.GULL_ATTACK_TEXTURE_FLIP : VFXContainer.GULL_ATTACK_TEXTURE, startX, startY, 0.5f)
                                    .setScale(0.22f)
                                    .moveX(startX, targetX, VfxBuilder.Interpolations.EXP5IN)
                                    .moveY(startY, targetY, VfxBuilder.Interpolations.EXP5IN)
                                    .rotate(MathUtils.random(50f, 200f) * (flip ? -1 : 1))
                                    .playSoundAt(0.48f, damage > 15 ? "BLUNT_HEAVY" : "BLUNT_FAST")
                                    .build();
                            AbstractGameEffect shootGullAtThorns = new VfxBuilder(flip ? VFXContainer.GULL_ATTACK_TEXTURE_FLIP : VFXContainer.GULL_ATTACK_TEXTURE, startX, startY, 0.5f)
                                    .setScale(0.22f)
                                    .moveX(startX, t.drawX, VfxBuilder.Interpolations.EXP5IN)
                                    .moveY(startY, t.drawY, VfxBuilder.Interpolations.EXP5IN)
                                    .rotate(MathUtils.random(50f, 200f) * (flip ? -1 : 1))
                                    .playSoundAt(0.48f, "ATTACK_FAST")
                                    .triggerVfxAt(0.5F, 1, (aFloat, aFloat2) -> hurtGull)
                                    .build();
                            AbstractDungeon.effectList.add(hasThorns ? shootGullAtThorns : shootGull);
                            this.addToTop(new WaitAction(0.015F));
                            //AbstractDungeon.effectList.add(hurtGull);
                            this.isDone = true;
                        }
                    });
                }

                //Do the damage action
                if (!disableGullVfx) {
                    this.addToBot(new DamageAction(t, new DamageInfo(p, damage, damageTypeForTurn)/*,
                hasThorns ? AbstractGameAction.AttackEffect.SLASH_HORIZONTAL :
                        damage > 15 ? AbstractGameAction.AttackEffect.BLUNT_HEAVY : AbstractGameAction.AttackEffect.BLUNT_LIGHT*/));
                } else {
                    this.addToBot(new DamageAction(t, new DamageInfo(p, damage, damageTypeForTurn),
                            hasThorns ? AbstractGameAction.AttackEffect.SLASH_HORIZONTAL :
                                    damage > 15 ? AbstractGameAction.AttackEffect.BLUNT_HEAVY : AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }

                //Upgrade the rushes you have
                this.addToBot(new UpgradeRushAction(this, magicNumber));

                //If you have Little Gull, trigger it
                if (AbstractDungeon.player.hasRelic(LittleGull.ID)) {
                    LittleGull lg = (LittleGull) AbstractDungeon.player.getRelic(LittleGull.ID);
                    lg.doGullDamage();
                }
            }
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            //rawDescription = UPGRADE_DESCRIPTION;
            upgradeMagicNumber(UPGRADE_PLUS_BONUS_DAMAGE);
            //upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}