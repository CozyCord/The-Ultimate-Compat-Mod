package net.cozystudios.theultimatecompatmod.unlocks;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class Families {

    private Families() {}

    private static final Map<Identifier, Set<Identifier>> ITEM_TO_FAMILY = new HashMap<>();

    private record WoodPair(String mod, String wood) {}

    private static final WoodPair[][] WOOD_GROUPS = {
        { new WoodPair("biomeswevegone","redwood"), new WoodPair("clutter","redwood"), new WoodPair("natures_spirit","redwood"), new WoodPair("regions_unexplored","redwood") },
        { new WoodPair("biomeswevegone","maple"), new WoodPair("natures_spirit","maple"), new WoodPair("regions_unexplored","maple") },
        { new WoodPair("biomeswevegone","willow"), new WoodPair("natures_spirit","willow"), new WoodPair("regions_unexplored","willow") },
        { new WoodPair("biomeswevegone","cypress"), new WoodPair("natures_spirit","cypress"), new WoodPair("regions_unexplored","cypress") },
        { new WoodPair("biomeswevegone","baobab"), new WoodPair("regions_unexplored","baobab") },
        { new WoodPair("biomeswevegone","fir"), new WoodPair("natures_spirit","fir") },
        { new WoodPair("biomeswevegone","mahogany"), new WoodPair("natures_spirit","mahogany") },
        { new WoodPair("biomeswevegone","aspen"), new WoodPair("natures_spirit","aspen") },
        { new WoodPair("biomeswevegone","palm"), new WoodPair("regions_unexplored","palm"), new WoodPair("beachparty","palm") },
        { new WoodPair("meadow","pine"), new WoodPair("biomeswevegone","pine"), new WoodPair("regions_unexplored","pine") },
        { new WoodPair("natures_spirit","palo_verde"), new WoodPair("biomeswevegone","palo_verde") },
        { new WoodPair("natures_spirit","joshua"), new WoodPair("regions_unexplored","joshua") },
        { new WoodPair("natures_spirit","larch"), new WoodPair("regions_unexplored","larch") }
    };

    private static final String[] SUFFIXES = {
        "_log", "_wood", "_planks", "_leaves", "_sapling",
        "_slab", "_stairs", "_fence", "_fence_gate",
        "_door", "_trapdoor", "_pressure_plate", "_button",
        "_sign", "_hanging_sign", "_boat", "_chest_boat"
    };
    private static final String[] STRIPPED_SUFFIXES = { "_log", "_wood" };
    private static final String[][] COMPAT_DYNAMIC = {
        { "supplementaries", "{source_mod}/sign_post_{wood}" },
        { "suppsquared",    "{source_mod}/item_shelf_{wood}" }
    };

    private static final String[][] FARMING_HOP_SINGLETONS = {
        { "farm_and_charm:tomato" },
        { "farm_and_charm:tomato_seeds" },
        { "farm_and_charm:lettuce" },
        { "farm_and_charm:lettuce_seeds" },
        { "farm_and_charm:onion" },
        { "farm_and_charm:minced_beef" },
        { "farm_and_charm:bacon" },
        { "farm_and_charm:chicken_parts" },
        { "farm_and_charm:lamb_ham" },
        { "farm_and_charm:dough" },
        { "farm_and_charm:raw_pasta" },
        { "farm_and_charm:rotten_tomato" },
        { "farm_and_charm:dog_food" },
        { "farm_and_charm:bacon_with_eggs" },
        { "candlelight:dough" },
        { "alcocraftplus:hop" },
        { "alcocraftplus:hop_seeds" },
        { "clutter:hops" },
        { "clutter:hops_seeds" }
    };

    public static void register() {
        if (!ITEM_TO_FAMILY.isEmpty()) return;

        for (WoodPair[] group : WOOD_GROUPS) {
            for (WoodPair pair : group) {
                Set<Identifier> family = new LinkedHashSet<>();
                for (String suf : SUFFIXES) {
                    family.add(new Identifier(pair.mod(), pair.wood() + suf));
                }
                for (String suf : STRIPPED_SUFFIXES) {
                    family.add(new Identifier(pair.mod(), "stripped_" + pair.wood() + suf));
                }
                for (String[] tpl : COMPAT_DYNAMIC) {
                    String compatMod = tpl[0];
                    String path = tpl[1]
                        .replace("{source_mod}", pair.mod())
                        .replace("{wood}", pair.wood());
                    family.add(new Identifier(compatMod, path));
                }
                for (Identifier id : family) {
                    ITEM_TO_FAMILY.put(id, family);
                }
            }
        }

        for (String[] singleton : FARMING_HOP_SINGLETONS) {
            Set<Identifier> family = new LinkedHashSet<>();
            for (String s : singleton) family.add(new Identifier(s));
            for (Identifier id : family) {
                ITEM_TO_FAMILY.put(id, family);
            }
        }
    }

    public static Set<Identifier> getFamily(Identifier item) {
        Set<Identifier> f = ITEM_TO_FAMILY.get(item);
        return f == null ? Set.of() : f;
    }

    public static Set<Identifier> allTrackedItems() {
        return new HashSet<>(ITEM_TO_FAMILY.keySet());
    }
}
