package Moonworks.cards.interfaces;

import basemod.BaseMod;
import basemod.helpers.TooltipInfo;

public interface RangedAttack {

    //If our ranged card is only ranged with some conditional, we set this to true
    boolean conditional = false;

    //If the above is true, this lets us know or condition was actually met
    boolean conditionMet = false;

    //Used to add the tag to the card
    String rangedTag = BaseMod.getKeywordTitle("moonworks:ranged");

    //Used to add the tooltip to the card
    TooltipInfo rangedTooltipInfo = new TooltipInfo(BaseMod.getKeywordTitle("moonworks:ranged"), BaseMod.getKeywordDescription("moonworks:ranged"));
}
