package net.cozystudios.theultimatecompatmod.unlocks;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.runtime.IIngredientFilter;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class JeiUnlockBridge {

    private JeiUnlockBridge() {}

    private static volatile IJeiRuntime runtime;

    public static void setRuntime(IJeiRuntime rt) { runtime = rt; }

    public static void clearRuntime() { runtime = null; }

    public static IJeiRuntime getRuntime() { return runtime; }

    public static void unlockItems(Set<Identifier> ids) {
        IJeiRuntime rt = runtime;
        if (rt == null || ids.isEmpty()) return;
        List<ItemStack> stacks = new ArrayList<>();
        for (Identifier id : ids) {
            Item item = Registries.ITEM.get(id);
            if (item != Items.AIR) stacks.add(new ItemStack(item));
        }
        if (!stacks.isEmpty()) {
            rt.getIngredientManager().addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, stacks);
            try {
                IIngredientFilter filter = rt.getIngredientFilter();
                String current = filter.getFilterText();
                filter.setFilterText(current + "​");
                filter.setFilterText(current);
            } catch (Throwable ignored) {}
        }
    }
}
