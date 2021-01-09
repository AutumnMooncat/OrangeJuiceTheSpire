package Moonworks.cardModifiers;

import Moonworks.OrangeJuiceMod;
import Moonworks.cards.trapCards.EvilMastermind;
import basemod.BaseMod;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EvilMastermindHelperModifier extends AbstractCardModifier {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());
    private final String[] descriptionParts;


    public EvilMastermindHelperModifier(String[] descriptionParts) {
        this.descriptionParts = descriptionParts;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        //Initialize a new string builder
        StringBuilder sb = new StringBuilder();
        EvilMastermind c;
        if (card instanceof EvilMastermind) {
            c = (EvilMastermind) card;
            //If we can actually get these lists, dynamically build the card description
            if (c.getTrapCardArrayList() != null && c.getTrapAmounts() != null && c.getTrapAmounts().size() > 0) {
                if (c.upgraded) {
                    sb.append(descriptionParts[0]);
                }
                int lines = 0;
                int traps = 0;
                sb.append(descriptionParts[2]);
                for (String s : c.getTrapAmounts().keySet()) {
                    sb.append(" NL ").append(s).append(": ").append(c.getTrapAmounts().get(s)); //Add each name and how many there are
                    if (c.getTrapAmounts().get(s) > 1) {
                        sb.append(descriptionParts[6]);
                    } else {
                        sb.append(descriptionParts[5]);
                    }
                    lines++;
                    traps += c.getTrapAmounts().get(s);
                    if (lines >= (c.upgraded ? 3 : 4) && (c.getTrapAmounts().size() - lines > 1)) { //If we still have more than 1 line, but we dont have room for them
                        sb.append(descriptionParts[3]).append(c.getTrapCardArrayList().size()-traps).append(descriptionParts[4]);
                        break; //We have too many lines, dump the rest of the info
                    }
                }
            } else { //Grab the base text
                sb.append(rawDescription);
            }
            sb.append(descriptionParts[1]); //Add the exhaust part
            return sb.toString();
        }
        return rawDescription;
    }

    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        return false;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new EvilMastermindHelperModifier(descriptionParts);
    }

    @Override
    public boolean isInherent(AbstractCard card) {
        return true;
    }
}