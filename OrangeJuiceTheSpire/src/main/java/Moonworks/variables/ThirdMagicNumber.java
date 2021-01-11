package Moonworks.variables;

import Moonworks.cards.abstractCards.AbstractDefaultCard;
import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;

import static Moonworks.OrangeJuiceMod.makeID;

public class ThirdMagicNumber extends DynamicVariable {

    //For in-depth comments, check the other variable(DefaultCustomVariable). It's nearly identical.
    //public static boolean invertColor = false;

    @Override
    public String key() {
        return makeID("ThirdMagic");
        // This is what you put between "!!" in your card strings to actually display the number.
        // You can name this anything (no spaces), but please pre-phase it with your mod name as otherwise mod conflicts can occur.
        // Remember, we're using makeID so it automatically puts "theDefault:" (or, your id) before the name.
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((AbstractDefaultCard) card).isThirdMagicNumberModified;

    }

    @Override
    public int value(AbstractCard card) {
        return ((AbstractDefaultCard) card).thirdMagicNumber;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ((AbstractDefaultCard) card).baseThirdMagicNumber;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((AbstractDefaultCard) card).upgradedThirdMagicNumber;
    }

}