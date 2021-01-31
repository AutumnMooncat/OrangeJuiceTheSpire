package Moonworks.actions;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import Moonworks.OrangeJuiceMod;
import Moonworks.cards.magicalCards.MagicalInferno;
import Moonworks.cards.magicalCards.MagicalMassacre;
import Moonworks.cards.magicalCards.MagicalRevenge;
import Moonworks.powers.MeltingMemoriesPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
@Deprecated
public class MeltingMemoriesSpawnMagicCardAction extends AbstractGameAction {
    private boolean appliedCards = false;
    public final AbstractPlayer p;
    public final MeltingMemoriesPower power;
    private static final UIStrings uiStrings;
    public static final String[] TEXT;

    public MeltingMemoriesSpawnMagicCardAction(MeltingMemoriesPower power) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.amount = 1;
        this.power = power;
        p = AbstractDungeon.player;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            //Create a group of cards
            CardGroup generatedCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            //Fill it with the options
            generateMagicCardChoices(generatedCards);

            //Upgrade if our power is upgraded, OR if we have Master Reality
            if (power.upgrade || p.hasPower("MasterRealityPower")) {
                for (AbstractCard card : generatedCards.group) {
                    card.upgrade();
                }
            }

            //Show the screen and pick the cards
            AbstractDungeon.gridSelectScreen.open(generatedCards, amount, TEXT[0], false);
        } else {
            if(!appliedCards) {
                for (AbstractCard cardToAdd : AbstractDungeon.gridSelectScreen.selectedCards) {
                    power.cards.add(cardToAdd);
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(cardToAdd, (float)Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                appliedCards = true;
                power.updateDescription();
            }
        }
        this.tickDuration();
    }

    private void generateMagicCardChoices(CardGroup group) {
        group.addToBottom(new MagicalInferno());
        group.addToBottom(new MagicalMassacre());
        group.addToBottom(new MagicalRevenge());
    }

    /**
     * Empty hook for if a different card is added to this via a patch or something
     * @param card - The card that was selected
     */
    public void otherCardHook(AbstractCard card) {}

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(OrangeJuiceMod.makeID("ChooseMagic"));
        TEXT = uiStrings.TEXT;
    }
}
