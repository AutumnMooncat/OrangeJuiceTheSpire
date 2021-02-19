package Moonworks.cardModifiers;

import Moonworks.OrangeJuiceMod;
import Moonworks.actions.FragileExhaustOnDrawAction;
import Moonworks.actions.WitherExhaustImmediatelyAction;
import Moonworks.cards.abstractCards.AbstractNormaAttentiveCard;
import basemod.BaseMod;
import basemod.abstracts.AbstractCardModifier;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.SneckoField;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Random;
@Deprecated
public class CorruptedModifier extends AbstractCardModifier {
    public static final Logger logger = LogManager.getLogger(OrangeJuiceMod.class.getName());
    private static final int MAX = CorruptionEffects.values().length;
    private final CorruptionEffects effect;
    private boolean appliedReduce;

    public enum CorruptionEffects {
        BLANK {
            @Override
            public String toString() {
                return OrangeJuiceMod.getModID().toLowerCase()+":"+BaseMod.getKeywordTitle("Blank");
            }
        }, //does nothing
        VOIDING {
            @Override
            public String toString() {
                return OrangeJuiceMod.getModID().toLowerCase()+":"+BaseMod.getKeywordTitle("moonworks:voiding");
            }
        }, //Lower energy when drawn
        REDUCE {
            @Override
            public String toString() {
                return OrangeJuiceMod.getModID().toLowerCase()+":"+BaseMod.getKeywordTitle("moonworks:reduced");
            }
        }, //Lower base values by 1
        SNECKO {
            @Override
            public String toString() {
                return "Snecko";
            }
        }, //Randomize cost on draw, Done and Working
        HIDEINFO {
            @Override
            public String toString() {
                return "Redacted";
            }
        }, //Turn name and description into ???
        DEGRADE {
            @Override
            public String toString() {
                return OrangeJuiceMod.getModID().toLowerCase()+":"+BaseMod.getKeywordTitle("moonworks:degraded");
            }
        }, //Damage Block and Magic number all go down, Done
        EXHAUST {
            @Override
            public String toString() {
                return "Exhaust";
            }
        }, //Card now exhausts
        ETHEREAL {
            @Override
            public String toString() {
                return "Ethereal";
            }
        }, //Card is now ethereal
        FRAGILE {
            @Override
            public String toString() {
                return OrangeJuiceMod.getModID().toLowerCase()+":"+BaseMod.getKeywordTitle("moonworks:fragile");
            }
        }, //Card is now exhaust and ethereal
        CORRUPTDESC {
            @Override
            public String toString() {
                return "";
            }
        }, //Description becomes corrupted text, effects masterdeck
        CORRUPTFULL {
            @Override
            public String toString() {
                return "";
            }
        }, //Name and Description become corrupted text, effects masterdeck
        PURGING {
            @Override
            public String toString() {
                return "Purge";
            }
        },
        WITHER {
            @Override
            public String toString() {
                return OrangeJuiceMod.getModID().toLowerCase()+":"+BaseMod.getKeywordTitle("moonworks:withering");
            }
        }
    }

    /*
    There are other effects that are not card mods, but can still happen. There will be handled in the main reloading code
    Draining - Lose energy
    Heavily Draining - Lose max energy for this combat
    Injured - Add N Injury to your draw pile
    Damage - Lose HP
    Wither - Lose Max HP
    Weak - Apply Weak
    Vulnerable - Apply Vulnerable
    Removed Card - Card is not recovered for this combat
     */

    public CorruptedModifier(CorruptionEffects effect) {
        this(effect, false);
    }

    public CorruptedModifier(CorruptionEffects effect, boolean appliedReduce) {
        this.effect = effect;
        this.appliedReduce = appliedReduce;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        super.onInitialApplication(card);
        card.name+="?";
        switch (effect) {
            case SNECKO:
                SneckoField.snecko.set(card, true);
                break;
            case HIDEINFO:
            //case CORRUPTFULL:
                card.name = "???";
                //card.initializeTitle();
                //ReflectionHacks.privateMethod(card.getClass(),"initializeTitle").invoke(card);
                break;
            case EXHAUST:
                card.exhaust = true;
                break;
            case ETHEREAL:
                card.isEthereal = true;
                break;
            case PURGING:
                card.purgeOnUse = true;
                break;
            case WITHER:
                AbstractDungeon.actionManager.addToTop(new WitherExhaustImmediatelyAction(card));
                break;
            case REDUCE:
                if (!appliedReduce) {
                    if (card.baseDamage > 0) {
                        card.baseDamage -= 1;
                        card.damage = card.baseDamage;
                        card.isDamageModified = true;
                    }
                    if (card.baseBlock > 0) {
                        card.baseBlock -= 1;
                        card.block = card.baseBlock;
                        card.isBlockModified = true;
                    }
                    if (card.baseMagicNumber > 0) {
                        card.baseMagicNumber -= 1;
                        card.magicNumber = card.baseMagicNumber;
                        card.isMagicNumberModified = true;
                    }
                    appliedReduce = true;
                    card.initializeDescription();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDrawn(AbstractCard card) {
        super.onDrawn(card);
        switch (effect){
            case VOIDING:
                AbstractDungeon.actionManager.addToTop(new LoseEnergyAction(1));
                break;
            case DEGRADE:
                if (card.baseDamage > 0) {
                    int mod = AbstractDungeon.cardRandomRng.random(0, 3);
                    card.baseDamage = Math.max(card.baseDamage - mod, 1);
                    card.isDamageModified = true;
                }
                if (card.baseBlock > 0) {
                    int mod = AbstractDungeon.cardRandomRng.random(0, 3);
                    card.baseDamage = Math.max(card.baseDamage - mod, 1);
                    card.isBlockModified = true;
                }
                if (card.baseMagicNumber > 0) {
                    int mod = AbstractDungeon.cardRandomRng.random(0, 3);
                    card.baseMagicNumber = Math.max(card.baseMagicNumber - mod, 1);
                    card.isMagicNumberModified = true;
                }
                card.initializeDescription();
                break;
            case FRAGILE:
                if (AbstractDungeon.cardRandomRng.random(0, 2) == 2) {
                    AbstractDungeon.actionManager.addToTop(new FragileExhaustOnDrawAction(card));
                }
                break;
            case WITHER:
                AbstractDungeon.actionManager.addToTop(new FragileExhaustOnDrawAction(card));
                break;
            default:
                break;
        }
    }

    @Override
    public void onRetained(AbstractCard card) {
        super.onRetained(card);
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (effect == CorruptionEffects.BLANK) {
            //This one does nothing but add a ? to the end of the name
            return rawDescription;
        }
        if (effect == CorruptionEffects.HIDEINFO){
            return "???";
        }
        if (effect == CorruptionEffects.CORRUPTFULL){
            if (AbstractDungeon.cardRandomRng != null) {
                card.name = getRandomString(AbstractDungeon.cardRandomRng.random(5, 10));
            }
        }
        if (effect == CorruptionEffects.CORRUPTDESC || effect == CorruptionEffects.CORRUPTFULL){
            if (AbstractDungeon.cardRandomRng != null) {
                //First grab all new lines and format them as a @ to unpack,
                //Then grab all energy icons and turn them into a single character
                //Then grab all dynvars and replace them with a single character
                //Finally grab all keyword headers and remove them
                //If a mistake happens and we end up with more or less text than we normally would, its fine since we cant read it anyway, lol!
                String s = rawDescription.
                        replace("NL", "@").
                        replace("[E]", "A").
                        replaceAll("[!].+?[!]", "A").
                        replaceAll("\\S+?[:]\\S", "A");
                char[] chars = s.toCharArray();
                for (int i = 0 ; i < chars.length ; i++) {
                    if (chars[i] != ' ' && chars[i] != '@' && chars[i] != ':' && chars[i] != '+' && chars[i] != '.') {
                        chars[i] = getRandomString(AbstractDungeon.cardRandomRng.random(1, 1)).charAt(0);
                    }
                }
                return new String(chars).replace("@", "NL");

            } else {
                //Dont do anything if we dont have the ability to randomize it yet
                return rawDescription;
            }
        }
        //Big meme hack, might fix the issue, might not
        //if (card instanceof AbstractNormaAttentiveCard) ((AbstractNormaAttentiveCard) card).DESCRIPTION = "Corrupted: "+effect.toString()+". NL " + rawDescription;
        return "Corrupted: "+effect.toString()+". NL " + rawDescription;
    }

    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        return false;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new CorruptedModifier(effect, appliedReduce);
    }

    private String getRandomString(int length) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}