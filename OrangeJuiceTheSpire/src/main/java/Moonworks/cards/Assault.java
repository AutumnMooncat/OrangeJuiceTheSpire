package Moonworks.cards;

import Moonworks.cardModifiers.NormaDynvarModifier;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import Moonworks.patches.FixedPatches;
import Moonworks.patches.PiercingPatches;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.blue.Melter;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;

import static Moonworks.OrangeJuiceMod.makeCardPath;

public class Assault extends AbstractNormaAttentiveCard {

    // TEXT DECLARATION

    public static final String ID = OrangeJuiceMod.makeID(Assault.class.getSimpleName());
    public static final String IMG = makeCardPath("Assault.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheStarBreaker.Enums.COLOR_WHITE_ICE;

    private static final int COST = 1;

    private static final int DAMAGE = 7;
    private static final int UPGRADE_PLUS_DAMAGE = 2;

    private static final Integer[] NORMA_LEVELS = {2};

    // /STAT DECLARATION/

    public Assault() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, NORMA_LEVELS);
        damage = baseDamage = DAMAGE;
        //damageType = damageTypeForTurn = DamageInfo.DamageType.HP_LOSS;
        CardModifierManager.addModifier(this, new NormaDynvarModifier(NormaDynvarModifier.DYNVARMODS.DAMAGEMOD, 2, NORMA_LEVELS[0], EXTENDED_DESCRIPTION[0]));
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //Our Pierce will hit for as much block as the main attack will actually remove
        int blockDelta = Math.min(m.currentBlock, damage);

        //Main attack
        DamageInfo fixedDamage = new DamageInfo(p, damage, damageTypeForTurn);
        FixedPatches.FixedField.fixed.set(fixedDamage, true);
        this.addToBot(new DamageAction(m, fixedDamage, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));

        //If we have any piercing to do, do it
        if (blockDelta > 0) {
            DamageInfo pierceDamage = new DamageInfo(p, blockDelta, DamageInfo.DamageType.HP_LOSS);
            PiercingPatches.PiercingField.piercing.set(pierceDamage, true);
            this.addToBot(new DamageAction(m, pierceDamage, AbstractGameAction.AttackEffect.NONE, true));
        }
    }

    //Stops powers from effecting the card
    @Override
    public void applyPowers() {}

    //Don't let damage be modified
    @Override
    public void calculateCardDamage(AbstractMonster mo) {}

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DAMAGE);
            this.initializeDescription();
        }
    }
}