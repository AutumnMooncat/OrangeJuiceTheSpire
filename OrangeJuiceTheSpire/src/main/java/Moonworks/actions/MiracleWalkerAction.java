package Moonworks.actions;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import Moonworks.OrangeJuiceMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MiracleWalkerAction extends AbstractGameAction {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());
    private final boolean upgrade;
    private final AbstractCard card;
    private final int index;

    public MiracleWalkerAction(AbstractCard card) {
        this(card,false);
    }

    public MiracleWalkerAction(AbstractCard card, boolean upgrade) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.amount = 1;
        this.upgrade = upgrade;
        this.card = card;
        this.index = AbstractDungeon.player.hand.group.indexOf(card);
    }

    public void update() {
        AbstractCard tmp = AbstractDungeon.returnTrulyRandomCard().makeCopy();
        if((card.upgraded || upgrade) && tmp.canUpgrade()) {
            tmp.upgrade();
        }
        tmp.current_x = card.current_x;
        tmp.current_y = card.current_y;
        tmp.target_x = card.target_x;
        tmp.target_y = card.target_y;
        tmp.drawScale = 1.0F;
        tmp.targetDrawScale = card.targetDrawScale;
        tmp.angle = card.angle;
        tmp.targetAngle = card.targetAngle;
        tmp.superFlash(Color.WHITE.cpy());
        tmp.costForTurn = 0;
        tmp.isCostModifiedForTurn = true;
        //tmp.name += "?";
        //tmp.initializeTitle();
        AbstractDungeon.player.hand.group.set(index, tmp);
        AbstractDungeon.player.hand.glowCheck();
        this.tickDuration();
    }
}
