package net.cozystudios.theultimatecompatmod.farming;

import net.cozystudios.theultimatecompatmod.HiddenItemRegistry;

public final class FarmingDuplicates {

    private FarmingDuplicates() {}

    public static void register() {
        HiddenItemRegistry.put("farm_and_charm:tomato",          "farmersdelight:tomato");
        HiddenItemRegistry.put("farm_and_charm:tomato_seeds",    "farmersdelight:tomato_seeds");
        HiddenItemRegistry.put("farm_and_charm:lettuce",         "farmersdelight:cabbage");
        HiddenItemRegistry.put("farm_and_charm:lettuce_seeds",   "farmersdelight:cabbage_seeds");
        HiddenItemRegistry.put("farm_and_charm:onion",           "farmersdelight:onion");
        HiddenItemRegistry.put("farm_and_charm:minced_beef",     "farmersdelight:minced_beef");
        HiddenItemRegistry.put("farm_and_charm:bacon",           "farmersdelight:bacon");
        HiddenItemRegistry.put("farm_and_charm:chicken_parts",   "farmersdelight:chicken_cuts");
        HiddenItemRegistry.put("farm_and_charm:lamb_ham",        "farmersdelight:mutton_chops");
        HiddenItemRegistry.put("farm_and_charm:dough",           "farmersdelight:wheat_dough");
        HiddenItemRegistry.put("farm_and_charm:raw_pasta",       "farmersdelight:raw_pasta");
        HiddenItemRegistry.put("farm_and_charm:rotten_tomato",   "farmersdelight:rotten_tomato");
        HiddenItemRegistry.put("farm_and_charm:dog_food",        "farmersdelight:dog_food");
        HiddenItemRegistry.put("farm_and_charm:bacon_with_eggs", "farmersdelight:bacon_and_eggs");

        HiddenItemRegistry.put("candlelight:dough",              "farmersdelight:wheat_dough");
    }
}
