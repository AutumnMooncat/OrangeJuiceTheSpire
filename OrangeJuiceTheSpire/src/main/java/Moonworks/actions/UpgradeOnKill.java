package Moonworks.actions;

import Moonworks.OrangeJuiceMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.UUID;

public class UpgradeOnKill extends AbstractGameAction {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());
    private final DamageInfo info;
    private final UUID uuid;

    public UpgradeOnKill(final AbstractMonster m, final DamageInfo info, UUID targetUUID) {
        this.uuid = targetUUID;
        this.target = m;
        this.info = info;
        actionType = ActionType.SPECIAL;
        duration = 0.05F;
    }

    @Override
    public void update() {
        logger.info("Begin UOK");
        if (this.target != null) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.FIRE));
            this.target.damage(this.info);
            //this.addToBot(new DamageAction(this.target, this.info, AttackEffect.FIRE));
            logger.info("Target hit!");
            if ((((AbstractMonster)this.target).isDying || this.target.currentHealth <= 0) && !this.target.halfDead && !this.target.hasPower("Minion")) {
                logger.info("Fatal!");
                Iterator<AbstractCard> var1 = AbstractDungeon.player.masterDeck.group.iterator();
                AbstractCard c;
                while(var1.hasNext()) {
                    c = var1.next();
                    if (c.uuid.equals(this.uuid)) {
                        if(c.canUpgrade()) {
                            //logger.info("Upgrading...");
                            c.upgrade();
                        }
                    }
                }

                for(var1 = GetAllInBattleInstances.get(this.uuid).iterator(); var1.hasNext(); c.baseDamage = c.misc) {
                    c = var1.next();
                    if(c.canUpgrade()) {
                        logger.info("Upgrading...");
                        c.upgrade();
                    }
                }

            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        this.tickDuration();
    }
}
