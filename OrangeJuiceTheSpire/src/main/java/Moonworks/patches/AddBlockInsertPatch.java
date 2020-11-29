package Moonworks.patches;

/*
@SpirePatch(clz = AbstractCreature.class, method = "addBlock")
public class AddBlockInsertPatch {
    public static final Logger logger = LogManager.getLogger(DefaultMod.class.getName());
    @SpireInsertPatch(locator = Locator.class)
    public static void patch(AbstractCreature __instance, @ByRef int[] blockAmount) {
        //To make sure it doesn't interfere with other mods
        if(AbstractDungeon.player.chosenClass == TheDefault.Enums.THE_DEFAULT) {
            if (__instance instanceof AbstractMonster) {
                float tmp = blockAmount[0];
                logger.info("Intro block:" + tmp);
                for (AbstractPower p : __instance.powers) {
                    logger.info("Checking power:" + p);
                    tmp = p.modifyBlock(tmp);
                    logger.info("Current block:" + tmp);
                }
                logger.info("Final block:" + tmp);
                blockAmount[0] = MathUtils.floor(tmp);
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCreature.class, "currentBlock");
            return LineFinder.findInOrder(ctBehavior, finalMatcher);
        }
    }
}*/