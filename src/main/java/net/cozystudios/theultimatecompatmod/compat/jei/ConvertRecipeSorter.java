package net.cozystudios.theultimatecompatmod.compat.jei;

import net.cozystudios.theultimatecompatmod.workbench.CompatConvertRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ConvertRecipeSorter {

    private ConvertRecipeSorter() {}

    private static final int CAT_WOOD = 0;
    private static final int CAT_FARMING = 1;
    private static final int CAT_HOPS = 2;
    private static final int CAT_OTHER = 99;

    private static final String[] SUFFIX_ORDER = {
        "_log", "_wood", "stripped_log", "stripped_wood",
        "_planks", "_leaves", "_sapling",
        "_slab", "_stairs", "_fence", "_fence_gate",
        "_door", "_trapdoor", "_pressure_plate", "_button",
        "_sign", "_hanging_sign", "_boat", "_chest_boat"
    };
    private static final Map<String, Integer> SUFFIX_PRIORITY = new LinkedHashMap<>();
    static {
        for (int i = 0; i < SUFFIX_ORDER.length; i++) SUFFIX_PRIORITY.put(SUFFIX_ORDER[i], i);
        SUFFIX_PRIORITY.put("sign_post", SUFFIX_ORDER.length);
        SUFFIX_PRIORITY.put("item_shelf", SUFFIX_ORDER.length + 1);
    }

    private static final String[] WOOD_NAMES = {
        "aspen", "baobab", "cypress", "fir", "joshua", "larch",
        "mahogany", "maple", "palm", "palo_verde", "pine", "redwood", "willow"
    };
    private static final Map<String, Integer> WOOD_PRIORITY = new LinkedHashMap<>();
    static {
        for (int i = 0; i < WOOD_NAMES.length; i++) WOOD_PRIORITY.put(WOOD_NAMES[i], i);
    }

    private static final String[] FARMING_HOP_ORDER = {
        "tomato", "tomato_seeds", "lettuce", "lettuce_seeds",
        "onion", "cabbage", "cabbage_seeds",
        "minced_beef", "bacon", "chicken_parts", "chicken_cuts",
        "lamb_ham", "mutton_chops",
        "dough", "wheat_dough", "raw_pasta", "rotten_tomato",
        "dog_food", "bacon_with_eggs", "bacon_and_eggs",
        "hops", "hop", "hops_seeds", "hop_seeds"
    };
    private static final Map<String, Integer> FARMING_HOP_PRIORITY = new LinkedHashMap<>();
    static {
        for (int i = 0; i < FARMING_HOP_ORDER.length; i++) FARMING_HOP_PRIORITY.put(FARMING_HOP_ORDER[i], i);
    }

    private static final java.util.Set<String> WOOD_MODS = java.util.Set.of(
        "biomeswevegone", "clutter", "natures_spirit", "regions_unexplored",
        "beachparty", "meadow", "supplementaries", "suppsquared"
    );
    private static final java.util.Set<String> HOP_MODS = java.util.Set.of(
        "brewery", "alcocraftplus"
    );

    public static Comparator<CompatConvertRecipe> comparator() {
        return Comparator
            .comparingInt((CompatConvertRecipe r) -> category(inputId(r)))
            .thenComparingInt(r -> suffixPriority(inputId(r)))
            .thenComparing(r -> baseName(inputId(r)))
            .thenComparing(r -> inputId(r).getNamespace())
            .thenComparing(r -> outputId(r).toString());
    }

    private static Identifier inputId(CompatConvertRecipe r) {
        ItemStack[] stacks = r.getIngredient().getMatchingStacks();
        if (stacks.length == 0) return new Identifier("minecraft", "air");
        return Registries.ITEM.getId(stacks[0].getItem());
    }

    private static Identifier outputId(CompatConvertRecipe r) {
        return Registries.ITEM.getId(r.getResult().getItem());
    }

    private static int category(Identifier id) {
        String ns = id.getNamespace();
        if (WOOD_MODS.contains(ns)) return CAT_WOOD;
        if (HOP_MODS.contains(ns)) return CAT_HOPS;
        if (ns.equals("farm_and_charm") || ns.equals("farmersdelight") || ns.equals("candlelight")) {
            return CAT_FARMING;
        }
        if (ns.equals("clutter")) {
            String path = id.getPath();
            if (path.startsWith("hops")) return CAT_HOPS;
            return CAT_WOOD;
        }
        return CAT_OTHER;
    }

    private static int suffixPriority(Identifier id) {
        String path = id.getPath();

        if (path.contains("/sign_post_")) return SUFFIX_PRIORITY.get("sign_post");
        if (path.contains("/item_shelf_")) return SUFFIX_PRIORITY.get("item_shelf");

        if (path.startsWith("stripped_")) {
            if (path.endsWith("_log")) return SUFFIX_PRIORITY.get("stripped_log");
            if (path.endsWith("_wood")) return SUFFIX_PRIORITY.get("stripped_wood");
        }

        for (int i = SUFFIX_ORDER.length - 1; i >= 0; i--) {
            String suf = SUFFIX_ORDER[i];
            if (suf.startsWith("_") && path.endsWith(suf)) {
                return SUFFIX_PRIORITY.get(suf);
            }
        }

        Integer p = FARMING_HOP_PRIORITY.get(path);
        return p != null ? p : Integer.MAX_VALUE;
    }

    private static String baseName(Identifier id) {
        String path = id.getPath();

        int idx = path.indexOf("/sign_post_");
        if (idx >= 0) return path.substring(idx + "/sign_post_".length());
        idx = path.indexOf("/item_shelf_");
        if (idx >= 0) return path.substring(idx + "/item_shelf_".length());

        String p = path;
        if (p.startsWith("stripped_")) p = p.substring("stripped_".length());

        for (String suf : SUFFIX_ORDER) {
            if (suf.startsWith("_") && p.endsWith(suf)) {
                p = p.substring(0, p.length() - suf.length());
                break;
            }
        }

        Integer priority = WOOD_PRIORITY.get(p);
        if (priority != null) {
            return String.format("%03d_%s", priority, p);
        }
        return p;
    }
}
