package net.cozystudios.theultimatecompatmod.unlocks;

import net.cozystudios.theultimatecompatmod.workbench.CompatConvertRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class RecipeUnlockHelper {

    private RecipeUnlockHelper() {}

    public static void unlockRecipesFor(ServerPlayerEntity player, Set<Identifier> familyItems) {
        if (familyItems.isEmpty()) return;
        RecipeManager rm = player.server.getRecipeManager();
        List<Recipe<?>> toUnlock = new ArrayList<>();

        for (Recipe<?> recipe : rm.values()) {
            if (recipe instanceof CompatConvertRecipe) continue;
            if (matches(recipe, familyItems, player)) toUnlock.add(recipe);
        }

        if (!toUnlock.isEmpty()) {
            player.unlockRecipes(toUnlock);
        }
    }

    private static boolean matches(Recipe<?> recipe, Set<Identifier> familyItems, ServerPlayerEntity player) {
        try {
            ItemStack out = recipe.getOutput(player.server.getRegistryManager());
            if (!out.isEmpty() && familyItems.contains(Registries.ITEM.getId(out.getItem()))) {
                return true;
            }
        } catch (Throwable ignored) {}

        try {
            for (Ingredient ing : recipe.getIngredients()) {
                for (ItemStack stack : ing.getMatchingStacks()) {
                    if (familyItems.contains(Registries.ITEM.getId(stack.getItem()))) return true;
                }
            }
        } catch (Throwable ignored) {}

        return false;
    }
}
