package net.cozystudios.theultimatecompatmod;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class HiddenItemRegistry {

    private static final Map<Identifier, Identifier> LOSER_TO_WINNER = new LinkedHashMap<>();
    private static final Identifier AIR = new Identifier("minecraft:air");

    private HiddenItemRegistry() {}

    public static void put(String loser, String winner) {
        LOSER_TO_WINNER.put(new Identifier(loser), new Identifier(winner));
    }

    public static Map<Identifier, Identifier> map() {
        return LOSER_TO_WINNER;
    }

    public static List<Item> loadedLoserItems() {
        List<Item> out = new ArrayList<>();
        Item air = Registries.ITEM.get(AIR);
        for (Identifier id : LOSER_TO_WINNER.keySet()) {
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
