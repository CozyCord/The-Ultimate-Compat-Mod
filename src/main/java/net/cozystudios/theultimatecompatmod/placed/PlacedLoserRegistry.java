package net.cozystudios.theultimatecompatmod.placed;

import net.cozystudios.theultimatecompatmod.HiddenItemRegistry;
import net.cozystudios.theultimatecompatmod.TheUltimateCompatMod;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;

public final class PlacedLoserRegistry {

    private PlacedLoserRegistry() {}

    private static Set<Item> loserItemsCache;

    public static void register() {
        PlacedLoserCondition.TYPE = Registry.register(
                Registries.LOOT_CONDITION_TYPE,
                new Identifier(TheUltimateCompatMod.MOD_ID, "is_player_placed_loser"),
                new LootConditionType(new PlacedLoserCondition.Serializer())
        );

        PlacedLoserTracker.register();
    }

    public static boolean isLoserItem(Item item) {
        if (item == null) return false;
        Set<Item> cache = loserItemsCache;
        if (cache == null) {
            cache = new HashSet<>(HiddenItemRegistry.loadedLoserItems());
            loserItemsCache = cache;
        }
        return cache.contains(item);
    }

    public static boolean isLoserBlock(Block block) {
        if (block == null) return false;
        return isLoserItem(block.asItem());
    }

    public static boolean isLoserStack(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        return isLoserItem(stack.getItem());
    }
}
