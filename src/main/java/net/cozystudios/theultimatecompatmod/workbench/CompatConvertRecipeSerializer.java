package net.cozystudios.theultimatecompatmod.workbench;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class CompatConvertRecipeSerializer implements RecipeSerializer<CompatConvertRecipe> {

    public static final CompatConvertRecipeSerializer INSTANCE = new CompatConvertRecipeSerializer();

    @Override
    public CompatConvertRecipe read(Identifier id, JsonObject json) {
        Ingredient ingredient;
        if (JsonHelper.hasArray(json, "ingredient")) {
            ingredient = Ingredient.fromJson(JsonHelper.getArray(json, "ingredient"), false);
        } else {
            ingredient = Ingredient.fromJson(JsonHelper.getObject(json, "ingredient"), false);
        }
        JsonObject resultJson = JsonHelper.getObject(json, "result");
        String itemId = JsonHelper.getString(resultJson, "item");
        int count = JsonHelper.getInt(resultJson, "count", 1);
        ItemStack result = new ItemStack(
                net.minecraft.registry.Registries.ITEM.get(new Identifier(itemId)),
                count
        );
        return new CompatConvertRecipe(id, ingredient, result);
    }

    @Override
    public CompatConvertRecipe read(Identifier id, PacketByteBuf buf) {
        Ingredient ingredient = Ingredient.fromPacket(buf);
        ItemStack result = buf.readItemStack();
        return new CompatConvertRecipe(id, ingredient, result);
    }

    @Override
    public void write(PacketByteBuf buf, CompatConvertRecipe recipe) {
        recipe.getIngredient().write(buf);
        buf.writeItemStack(recipe.getResult());
    }
}
