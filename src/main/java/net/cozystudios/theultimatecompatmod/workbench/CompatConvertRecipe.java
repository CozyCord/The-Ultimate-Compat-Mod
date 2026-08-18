package net.cozystudios.theultimatecompatmod.workbench;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CompatConvertRecipe implements Recipe<Inventory> {

    private final Identifier id;
    private final Ingredient ingredient;
    private final ItemStack result;

    public CompatConvertRecipe(Identifier id, Ingredient ingredient, ItemStack result) {
        this.id = id;
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        return this.ingredient.test(inventory.getStack(0));
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        return this.result.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return this.result;
    }

    public ItemStack getResult() {
        return this.result;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRegistry.CONVERT_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRegistry.CONVERT_RECIPE_TYPE;
    }
}
