package Moonworks.powers;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.ConsumePurgeImmediatelyAction;
import Moonworks.cardModifiers.MemoryModifier;
import Moonworks.cards.abstractCards.AbstractGiftCard;
import Moonworks.cards.abstractCards.AbstractTempCard;
import Moonworks.cards.tempCards.SeriousBattle;
import Moonworks.util.MemoryHelper;
import basemod.ClickableUIElement;
import basemod.helpers.CardModifierManager;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.ExhaustiveField;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.HashSet;
import java.util.stream.Collectors;

public class BookOfMemoriesPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = OrangeJuiceMod.makeID("BookOfMemoriesPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Color ATTACK_COLOR = OrangeJuiceMod.ATTACK_ORANGE.cpy();
    private static final Color SKILL_COLOR = OrangeJuiceMod.SKILL_GREEN.cpy();

    //private Hitbox clickableHitbox;
    public BookUIElement bookElement;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    //private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    //private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    private final CardGroup memories = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

    //private final HashSet<AbstractCard> cards = new HashSet<>();

    public BookOfMemoriesPower(final AbstractPlayer owner, HashSet<AbstractCard> cards) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        addCardsToSet(cards);
        this.amount = this.memories.size();

        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        // We load those txtures here.
        this.loadRegion("book"); //establishment
        //clickableHitbox = new Hitbox(84, 84);
        bookElement = new BookUIElement();
        //this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        //this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();

    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        this.amount = memories.size();
        updateDescription();
    }

    public void addCardsToSet(HashSet<AbstractCard> cards) {
        for (AbstractCard card : cards) {
            CardModifierManager.addModifier(card, new MemoryModifier(false));
            MemoryHelper.MemoryAssociation.associations.get(card).add(this);
            memories.addToTop(card);
            AbstractDungeon.actionManager.removeFromQueue(card);
            card.unhover();
            card.untip();
            card.stopGlowing();
            this.addToTop(new ConsumePurgeImmediatelyAction(card));
        }
        //this.cards.addAll(cards);
        updateDescription();
        //flash();
    }

    public void removeCardsFromSet(HashSet<AbstractCard> cards) {
        for (AbstractCard card : cards) {
            MemoryHelper.MemoryAssociation.associations.get(card).remove(this);
            memories.removeCard(card);
        }
        //this.cards.removeAll(cards);
        updateDescription();
        //flash();
    }

    @Override
    public void atEndOfRound() {
        //For each card...
        for (AbstractCard c : memories.group) {
            //Decrement all cooldown stacks
            MemoryHelper.decrementCooldown(c);

            //Reset active flags
            if (MemoryHelper.cooldownReady(c)) {
                MemoryHelper.setActive(c, true);
            }
        }
        updateDescription();
        super.atEndOfRound();
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card instanceof SeriousBattle) {
            for (int i = 0 ; i < card.magicNumber ; i++) {
                AbstractCard c = MemoryHelper.getRandomReady(memories);
                if (c != null) {
                    MemoryHelper.useMemory(c, m);
                    flash();
                    updateDescription();
                }
            }
        } else {
            AbstractCard c = MemoryHelper.getRandomReady(memories, card.type);
            if (c != null) {
                MemoryHelper.useMemory(c);
                flash();
                updateDescription();
            }
        }

        super.onPlayCard(card, m);
    }

    @Override
    public void atStartOfTurn() {
        if (needNewCard()) {
            this.addToTop(new MakeTempCardInHandAction(new SeriousBattle()));
        }
        super.atStartOfTurn();
    }

    @Override
    public void onInitialApplication() {
        this.addToTop(new MakeTempCardInHandAction(new SeriousBattle()));
        super.onInitialApplication();
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        this.amount = memories.size();
        StringBuilder sb = new StringBuilder();
        sb.append(DESCRIPTIONS[0]);
        if (this.amount == 0) {
            sb.append(DESCRIPTIONS[1]);
        } else {
            //Figure out how many cards there are, used to ensure we don't add an extra new line at the end
            //int i = cards.size();
            sb.append(DESCRIPTIONS[2]);
            /*for (AbstractCard c : cards) {
                sb.append(c.name);
                if (MemoryHelper.isExhausted(c)) {
                    sb.append(DESCRIPTIONS[4]);
                } else if (MemoryHelper.isReadyToUse(c)) {
                    sb.append(DESCRIPTIONS[3]);
                } else {
                    sb.append(DESCRIPTIONS[5]).append(MemoryHelper.getCooldown(c));
                }
                if (c.exhaust || c.exhaustOnUseOnce || c.purgeOnUse) {
                    sb.append(DESCRIPTIONS[6]).append("1");
                } else if (ExhaustiveField.ExhaustiveFields.baseExhaustive.get(c) != -1) {
                    sb.append(DESCRIPTIONS[6]).append(ExhaustiveField.ExhaustiveFields.exhaustive);
                }
                if (--i > 0) {
                    sb.append(" NL ");
                }

            }*/
        }
        description = sb.toString();
    }

    @Override
    public AbstractPower makeCopy() {
        return new BookOfMemoriesPower((AbstractPlayer) owner, new HashSet<>(memories.group));
    }

    public static boolean getViability(AbstractCard c) {
        return (c.type != AbstractCard.CardType.POWER && c.type != AbstractCard.CardType.STATUS && c.type != AbstractCard.CardType.CURSE && !(c instanceof AbstractGiftCard) && !(c instanceof AbstractTempCard) && c.cost >= 0);
    }

    public static boolean needNewCard() {
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof SeriousBattle) {
                return false;
            }
        }
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c instanceof SeriousBattle) {
                return false;
            }
        }
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c instanceof SeriousBattle) {
                return false;
            }
        }
        return true;
    }

    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        super.renderAmount(sb, x, y, c);
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(MemoryHelper.getAllReady(memories, AbstractCard.CardType.ATTACK).size()), x, y + 15.0F, this.fontScale, ATTACK_COLOR);
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(MemoryHelper.getAllReady(memories, AbstractCard.CardType.SKILL).size()), x - 20.0F, y + 15.0F * Settings.scale, this.fontScale, SKILL_COLOR);
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
        super.renderIcons(sb, x, y, c);
        //clickableHitbox.move(x, y);
        bookElement.move(x, y);
        bookElement.render(sb);
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        bookElement.update();
    }

    public class BookUIElement extends ClickableUIElement {
        private static final float hitboxSize = 40f;
        private static final float moveDelta = hitboxSize/2;

        public BookUIElement() {
            super(new TextureAtlas(Gdx.files.internal("powers/powers.atlas")).findRegion("48/" + ""),//carddraw
                    0, 0,
                    hitboxSize, hitboxSize);
        }

        public void move(float x, float y) {
            move(x, y, moveDelta);
        }

        public void move(float x, float y, float d) {
            this.setX(x-(d * Settings.scale));
            this.setY(y-(d * Settings.scale));
        }


        @Override
        protected void onHover() {
            //TipHelper.renderGenericTip(this.x, this.y - 15f * Settings.scale, NAME, DESCRIPTIONS[0]);
            //OrangeJuiceMod.logger.info("Hovered Book");
        }

        @Override
        protected void onUnhover() {
            //OrangeJuiceMod.logger.info("Unhovered Book");
        }

        @Override
        protected void onClick() {
            if (!AbstractDungeon.actionManager.turnHasEnded) {
                //OrangeJuiceMod.logger.info("Clicked Book");
                for (AbstractCard c : memories.group) {
                    c.applyPowers();
                    c.initializeDescription();
                }
                AbstractDungeon.gridSelectScreen.open(memories, 0, true, DESCRIPTIONS[12]);
                //AbstractDungeon.gridSelectScreen.openConfirmationGrid(memories, DESCRIPTIONS[12]);
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
            }

        }
    }
}