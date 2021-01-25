package Moonworks.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import Moonworks.cards.abstractCards.AbstractModdedCard;

import static Moonworks.OrangeJuiceMod.makeID;

public class SecondMagicNumber extends DynamicVariable {

    //For in-depth comments, check the other variable(DefaultCustomVariable). It's nearly identical.
    //public static boolean invertColor = false;

    @Override
    public String key() {
        return makeID("SecondMagic");
        // This is what you put between "!!" in your card strings to actually display the number.
        // You can name this anything (no spaces), but please pre-phase it with your mod name as otherwise mod conflicts can occur.
        // Remember, we're using makeID so it automatically puts "theDefault:" (or, your id) before the name.
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((AbstractModdedCard) card).isSecondMagicNumberModified;

    }

    @Override
    public int value(AbstractCard card) {
        return ((AbstractModdedCard) card).secondMagicNumber;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ((AbstractModdedCard) card).baseSecondMagicNumber;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((AbstractModdedCard) card).upgradedSecondMagicNumber;
    }

    //Moved to a new variable
    /*public static void invertColorDirection() {
        invertColor = true;
    }*/

    /*public static void revertColorDirection() {
        invertColor = false;
    }*/

}