package net.cozystudios.theultimatecompatmod.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.cozystudios.theultimatecompatmod.HiddenItemRegistry;
import net.cozystudios.theultimatecompatmod.TheUltimateCompatMod;
import net.cozystudios.theultimatecompatmod.workbench.CompatConvertRecipe;
import net.cozystudios.theultimatecompatmod.workbench.ModRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class TheUltimateCompatModJEIPlugin implements IModPlugin {

    private static final Identifier UID = new Identifier(TheUltimateCompatMod.MOD_ID, "jei_plugin");

    @Override
    public Identifier getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new CompatConvertCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.world == null) return;
        RecipeManager rm = client.world.getRecipeManager();
        List<CompatConvertRecipe> recipes = rm.listAllOfType(ModRegistry.CONVERT_RECIPE_TYPE);
        registration.addRecipes(CompatConvertCategory.RECIPE_TYPE, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModRegistry.COMPAT_WORKBENCH_ITEM), CompatConvertCategory.RECIPE_TYPE);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        List<ItemStack> toHide = new ArrayList<>();
        for (Item item : HiddenItemRegistry.loadedLoserItems()) {
            toHide.add(new ItemStack(item));
        }
        if (!toHide.isEmpty()) {
            jeiRuntime.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, toHide);
            TheUltimateCompatMod.LOGGER.info("[TheUltimateCompatMod/JEI] Hid {} duplicate items from JEI.", toHide.size());
        }
    }
}
