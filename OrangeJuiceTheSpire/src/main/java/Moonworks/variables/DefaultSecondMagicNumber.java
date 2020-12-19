package Moonworks.variables;

import basemod.abstracts.DynamicVariable;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import Moonworks.cards.abstractCards.AbstractDefaultCard;
import com.megacrit.cardcrawl.core.Settings;

import static Moonworks.OrangeJuiceMod.makeID;

public class DefaultSecondMagicNumber extends DynamicVariable {

    //For in-depth comments, check the other variable(DefaultCustomVariable). It's nearly identical.
    public static boolean invertColor = false;

    @Override
    public String key() {
        return makeID("SecondMagic");
        // This is what you put between "!!" in your card strings to actually display the number.
        // You can name this anything (no spaces), but please pre-phase it with your mod name as otherwise mod conflicts can occur.
        // Remember, we're using makeID so it automatically puts "theDefault:" (or, your id) before the name.
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((AbstractDefaultCard) card).isDefaultSecondMagicNumberModified;

    }

    @Override
    public int value(AbstractCard card) {
        return ((AbstractDefaultCard) card).defaultSecondMagicNumber;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ((AbstractDefaultCard) card).defaultBaseSecondMagicNumber;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((AbstractDefaultCard) card).upgradedDefaultSecondMagicNumber;
    }

    public static void invertColorDirection() {
        invertColor = true;
    }

    public static void revertColorDirection() {
        invertColor = false;
    }

    @Override
    public Color getIncreasedValueColor() {
        return invertColor ? Settings.RED_TEXT_COLOR : Settings.GREEN_TEXT_COLOR;
    }

    @Override
    public Color getDecreasedValueColor() {
        return invertColor ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR;
    }
}