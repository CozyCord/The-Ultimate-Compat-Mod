package net.cozystudios.theultimatecompatmod.wood;

import net.cozystudios.theultimatecompatmod.HiddenItemRegistry;

public final class WoodDuplicates {

    private WoodDuplicates() {}

    public static void register() {
        addStandardSet("clutter",            "redwood",    "biomeswevegone", "redwood");
        addStandardSet("natures_spirit",     "redwood",    "biomeswevegone", "redwood");
        addStandardSet("regions_unexplored", "redwood",    "biomeswevegone", "redwood");

        addStandardSet("natures_spirit",     "maple",      "biomeswevegone", "maple");
        addStandardSet("regions_unexplored", "maple",      "biomeswevegone", "maple");

        addStandardSet("natures_spirit",     "willow",     "biomeswevegone", "willow");
        addStandardSet("regions_unexplored", "willow",     "biomeswevegone", "willow");

        addStandardSet("natures_spirit",     "cypress",    "biomeswevegone", "cypress");
        addStandardSet("regions_unexplored", "cypress",    "biomeswevegone", "cypress");

        addStandardSet("regions_unexplored", "baobab",     "biomeswevegone", "baobab");

        addStandardSet("natures_spirit",     "fir",        "biomeswevegone", "fir");

        addStandardSet("natures_spirit",     "mahogany",   "biomeswevegone", "mahogany");

        addStandardSet("natures_spirit",     "aspen",      "biomeswevegone", "aspen");

        addStandardSet("regions_unexplored", "palm",       "biomeswevegone", "palm");
        addStandardSet("beachparty",         "palm",       "biomeswevegone", "palm");

        addStandardSet("biomeswevegone",     "pine",       "meadow",         "pine");
        addStandardSet("regions_unexplored", "pine",       "meadow",         "pine");

        addStandardSet("biomeswevegone",     "palo_verde", "natures_spirit", "palo_verde");

        addStandardSet("regions_unexplored", "joshua",     "natures_spirit", "joshua");

        addStandardSet("regions_unexplored", "larch",      "natures_spirit", "larch");
    }

    private static void addStandardSet(String loserMod, String loserWood,
                                        String winnerMod, String winnerWood) {
        for (String suffix : SUFFIXES) {
            HiddenItemRegistry.put(
                loserMod  + ":" + loserWood  + suffix,
                winnerMod + ":" + winnerWood + suffix);
        }
        for (String suffix : STRIPPED_SUFFIXES) {
            HiddenItemRegistry.put(
                loserMod  + ":stripped_" + loserWood  + suffix,
                winnerMod + ":stripped_" + winnerWood + suffix);
        }
        for (String[] tpl : COMPAT_DYNAMIC) {
            String compatMod = tpl[0];
            String template  = tpl[1];
            String loserPath  = template.replace("{source_mod}", loserMod).replace("{wood}", loserWood);
            String winnerPath = template.replace("{source_mod}", winnerMod).replace("{wood}", winnerWood);
            HiddenItemRegistry.put(compatMod + ":" + loserPath, compatMod + ":" + winnerPath);
        }
    }

    private static final String[] SUFFIXES = {
        "_log",
        "_wood",
        "_planks",
        "_leaves",
        "_sapling",
        "_slab",
        "_stairs",
        "_fence",
        "_fence_gate",
        "_door",
        "_trapdoor",
        "_pressure_plate",
        "_button",
        "_sign",
        "_hanging_sign",
        "_boat",
        "_chest_boat"
    };

    private static final String[] STRIPPED_SUFFIXES = {
        "_log",
        "_wood"
    };

    private static final String[][] COMPAT_DYNAMIC = {
        {"supplementaries", "{source_mod}/sign_post_{wood}"},
        {"suppsquared",    "{source_mod}/item_shelf_{wood}"}
    };
}
