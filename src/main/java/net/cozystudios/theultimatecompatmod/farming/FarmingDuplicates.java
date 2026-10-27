package net.cozystudios.theultimatecompatmod.farming;

import net.cozystudios.theultimatecompatmod.HiddenItemRegistry;

public final class FarmingDuplicates {

    private FarmingDuplicates() {}

    public static void register() {
        put("farm_and_charm:tomato",          "farmersdelight:tomato");
        put("farm_and_charm:tomato_seeds",    "farmersdelight:tomato_seeds");
        put("farm_and_charm:lettuce",         "farmersdelight:cabbage");
        put("farm_and_charm:lettuce_seeds",   "farmersdelight:cabbage_seeds");
        put("farm_and_charm:onion",           "farmersdelight:onion");
        put("farm_and_charm:minced_beef",     "farmersdelight:minced_beef");
        put("farm_and_charm:bacon",           "farmersdelight:bacon");
        put("farm_and_charm:chicken_parts",   "farmersdelight:chicken_cuts");
        put("farm_and_charm:lamb_ham",        "farmersdelight:mutton_chops");
        put("farm_and_charm:dough",           "farmersdelight:wheat_dough");
        put("farm_and_charm:raw_pasta",       "farmersdelight:raw_pasta");
        put("farm_and_charm:rotten_tomato",   "farmersdelight:rotten_tomato");
        put("farm_and_charm:dog_food",        "farmersdelight:dog_food");
        put("farm_and_charm:bacon_with_eggs", "farmersdelight:bacon_and_eggs");

        put("candlelight:dough",              "farmersdelight:wheat_dough");

        put("alcocraftplus:hop",              "brewery:hops");
        put("alcocraftplus:hop_seeds",        "brewery:hops_seeds");
        put("clutter:hops",                   "brewery:hops");
        put("clutter:hops_seeds",             "brewery:hops_seeds");
    }

    private static void put(String loser, String winner) {
        HiddenItemRegistry.put(loser, winner, HiddenItemRegistry.Category.FARMING);
    }
}
