package net.cozystudios.theultimatecompatmod;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public final class CreativeTabHandler {

    private CreativeTabHandler() {}

    public static void register() {
        Set<Item> losers = new HashSet<>(HiddenItemRegistry.loadedLoserItems());
        if (losers.isEmpty()) return;

        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((group, entries) -> {
            entries.getDisplayStacks().removeIf(stack -> matches(stack, losers));
            entries.getSearchTabStacks().removeIf(stack -> matches(stack, losers));
        });
    }

    private static boolean matches(ItemStack stack, Set<Item> losers) {
        return !stack.isEmpty() && losers.contains(stack.getItem());
    }
}
