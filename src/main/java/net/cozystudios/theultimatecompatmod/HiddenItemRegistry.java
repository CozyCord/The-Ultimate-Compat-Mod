package net.cozystudios.theultimatecompatmod;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class HiddenItemRegistry {

    public enum Category { WOOD, FARMING }

    private static final Map<Identifier, Identifier> LOSER_TO_WINNER = new LinkedHashMap<>();
    private static final Set<Identifier> WOOD_LOSERS = new LinkedHashSet<>();
    private static final Set<Identifier> FARMING_LOSERS = new LinkedHashSet<>();
    private static final Identifier AIR = new Identifier("minecraft:air");

    private HiddenItemRegistry() {}

    public static void put(String loser, String winner, Category category) {
        Identifier loserId = new Identifier(loser);
        LOSER_TO_WINNER.put(loserId, new Identifier(winner));
        switch (category) {
            case WOOD -> WOOD_LOSERS.add(loserId);
            case FARMING -> FARMING_LOSERS.add(loserId);
        }
    }

    public static Map<Identifier, Identifier> map() {
        return LOSER_TO_WINNER;
    }

    public static List<Item> loadedLoserItems() {
        return loadedItemsIn(LOSER_TO_WINNER.keySet());
    }

    public static List<Item> loadedFarmingLoserItems() {
        return loadedItemsIn(FARMING_LOSERS);
    }

    public static List<Item> loadedWoodLoserItems() {
        return loadedItemsIn(WOOD_LOSERS);
    }

    private static List<Item> loadedItemsIn(Iterable<Identifier> ids) {
        List<Item> out = new ArrayList<>();
        Item air = Registries.ITEM.get(AIR);
        for (Identifier id : ids) {
            if (Registries.ITEM.containsId(id)) {
                Item item = Registries.ITEM.get(id);
                if (item != null && item != air) {
                    out.add(item);
                }
            }
        }
        return out;
    }
}
