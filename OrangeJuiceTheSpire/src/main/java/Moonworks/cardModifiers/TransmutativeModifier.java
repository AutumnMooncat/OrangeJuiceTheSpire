package Moonworks.cardModifiers;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.TransmutativeAction;
import Moonworks.powers.NormaPower;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.BindException;

public class TransmutativeModifier extends AbstractCardModifier {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());
    public boolean infinite, free;
    public int uses;
    public boolean normaEffect = false;

    public TransmutativeModifier(int uses, boolean free) {
        this.uses = uses;
        this.free = free;
        this.infinite = uses == -1;
    }

    @Override
    public void onDrawn(AbstractCard card) {
        if (AbstractDungeon.player.hasPower(NormaPower.POWER_ID)) {
            normaEffect = AbstractDungeon.player.getPower(NormaPower.POWER_ID).amount >= 5;
        }
        AbstractDungeon.actionManager.addToTop(new TransmutativeAction(this, card, normaEffect));
        super.onDrawn(card);
    }

    @Override
    public void onRetained(AbstractCard card) {
        if (AbstractDungeon.player.hasPower(NormaPower.POWER_ID)) {
            normaEffect = AbstractDungeon.player.getPower(NormaPower.POWER_ID).amount >= 5;
        }
        AbstractDungeon.actionManager.addToTop(new TransmutativeAction(this, card, normaEffect));
        super.onRetained(card);
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return "moonworks:Transformative. NL " + rawDescription;
    }

    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        if (infinite) {
            return false;
        } else {
            return uses == 0;
        }
    }

    @Override
    public boolean isInherent(AbstractCard card) {
        return infinite;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new TransmutativeModifier(uses, free);
    }
}
