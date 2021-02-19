package Moonworks.relics;

import Moonworks.cards.tempCards.StarBlastingLight;
import Moonworks.characters.TheStarBreaker;
import Moonworks.util.interfaces.NormaAttentiveObject;
import Moonworks.util.NormaHelper;
import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import Moonworks.OrangeJuiceMod;
import Moonworks.util.TextureLoader;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import static Moonworks.OrangeJuiceMod.makeRelicOutlinePath;
import static Moonworks.OrangeJuiceMod.makeRelicPath;

public class SBBomb extends CustomRelic implements NormaAttentiveObject {

    // ID, images, text.
    public static final String ID = OrangeJuiceMod.makeID("SBBomb");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("SBBomb.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("SBBomb.png"));

    public SBBomb() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.HEAVY);
        this.counter = 0;
        if (AbstractDungeon.player != null) {
            counter = NormaHelper.getNumerator(AbstractDungeon.player);
        }
    }

    //See if we need to pulse
    public void atBattleStart() {
        checkOneChargeAway();
    }

    @Override //Should replace default relic. Thanks kiooeht#3584 10/25/2020, #modding-technical
    public void obtain() {
        //Grab the player
        AbstractPlayer p = AbstractDungeon.player;
        //Upgrade norma conditions
        NormaHelper.upgradeNormaConditions(p);
        //Set our counter
        this.counter = NormaHelper.getNumerator(p);
        //If we have the starter relic...
        if (p.hasRelic(BrokenBomb.ID)) {
            //Find it...
            for (int i = 0; i < p.relics.size(); ++i) {
                if (p.relics.get(i).relicId.equals(BrokenBomb.ID)) {
                    //Replace it
                    instantObtain(p, i, true);
                    break;
                }
            }
        } else {
            super.obtain();
        }
    }

    //Only spawn if we have BrokenBomb
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(BrokenBomb.ID);
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onGainNorma(int current, int increasedBy) {
        //Create a card to add
        AbstractCard starBlastingLight = new StarBlastingLight();
        //Flash this relic
        this.flash();
        //Stop pulsing
        this.pulse = false;
        //Play the happy animation
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (AbstractDungeon.player instanceof TheStarBreaker) {
                    ((TheStarBreaker) AbstractDungeon.player).playAnimation("happy");
                }
                this.isDone = true;
            }
        });
        //Say a voice line
        this.addToBot(new TalkAction(true, DESCRIPTIONS[1], 0.4f, 2.0f));
        //Refresh the hand before adding the card
        AbstractDungeon.player.hand.refreshHandLayout();
        //Check if we need up upgrade the card
        if (AbstractDungeon.player.hasPower("MasterRealityPower")) {
            starBlastingLight.upgrade();
        }
        //If we have room in our hand...
        if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
            //Put it there
            AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(starBlastingLight.makeStatEquivalentCopy(), (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
        } else {
            //Put it on the top on the draw pile otherwise
            AbstractDungeon.effectList.add(new ShowCardAndAddToDrawPileEffect(starBlastingLight.makeStatEquivalentCopy(), (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F, false, false));
        }
        //Reset our counter
        this.counter = NormaHelper.getNumerator(AbstractDungeon.player);
    }

    @Override
    public void onGainNormaCharge(int current, int increasedBy) {
        this.counter = current;
        checkOneChargeAway();
    }

    public void checkOneChargeAway() {
        if (NormaHelper.getDenominator(AbstractDungeon.player) - NormaHelper.getNumerator(AbstractDungeon.player) == 1) {
            this.beginPulse();
            this.pulse = true;
        }
    }
}
