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
