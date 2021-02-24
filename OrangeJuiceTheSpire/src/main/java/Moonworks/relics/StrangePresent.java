package Moonworks.relics;

import Moonworks.OrangeJuiceMod;
import Moonworks.characters.TheStarBreaker;
import Moonworks.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static Moonworks.OrangeJuiceMod.makeRelicOutlinePath;
import static Moonworks.OrangeJuiceMod.makeRelicPath;

public class StrangePresent extends CustomRelic {

    // ID, images, text.
    public static final String ID = OrangeJuiceMod.makeID("StrangePresent");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("Present.png"));
    private static final Texture IMG2 = TextureLoader.getTexture(makeRelicPath("Present2.png"));
    private static final Texture IMG3 = TextureLoader.getTexture(makeRelicPath("Present3.png"));
    private static final Texture IMG4 = TextureLoader.getTexture(makeRelicPath("Present4.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("Present.png"));

    private static final int ACTIVATION_TURN = 5;
    private static final int DEBUFF = 4;

    private boolean activated;

    public StrangePresent() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.FLAT);
    }

    public void atBattleStart() {
        this.counter = 0;
    }

    public void atTurnStart() {
        if (!activated) {
            ++this.counter;
            if (this.counter == ACTIVATION_TURN) {
                this.setTexture(IMG2);
                this.beginLongPulse();
            }
        } else {
            this.setTexture(IMG4);
        }
    }

    public void onPlayerEndTurn() {
        if (this.counter == ACTIVATION_TURN) {
            //Set new image
            this.setTexture(IMG3);
            //Flash
            this.flash();
            //Play Happy (if SB)
            this.addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    if (AbstractDungeon.player instanceof TheStarBreaker) {
                        ((TheStarBreaker) AbstractDungeon.player).playAnimation("happy");
                    }
                    this.isDone = true;
                }
            });
            //Show relic above us
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            //Apply debuffs to all enemies
            for (AbstractMonster m :AbstractDungeon.getMonsters().monsters) {
                if (!m.isDeadOrEscaped()) {
                    this.addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new VulnerablePower(m, DEBUFF, false), DEBUFF, true));
                    this.addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new WeakPower(m, DEBUFF, false), DEBUFF, true));
                    //this.addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new FrailPower(m, DEBUFF, false), DEBUFF, true));
                }
            }
            this.stopPulse();
            this.counter = -1;
            this.activated = true;
        }
    }

    public void justEnteredRoom(AbstractRoom room) {
        this.setTexture(IMG);
        this.activated = false;
    }

    public void onVictory() {
        //Rset counter
        this.counter = -1;
        this.activated = false;
        this.stopPulse();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
