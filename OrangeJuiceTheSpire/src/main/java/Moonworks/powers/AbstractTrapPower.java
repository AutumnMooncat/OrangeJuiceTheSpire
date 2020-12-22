package Moonworks.powers;

import com.megacrit.cardcrawl.powers.AbstractPower;

//This is mainly an empty container for use of instanceof in patching and othe logic checks
public abstract class AbstractTrapPower extends AbstractPower {

    public AbstractTrapPower() {
        //Traps may be negative effects, but all of them need to be of type BUFF tp go through Artifact
        this.type = PowerType.BUFF;
    }
}
