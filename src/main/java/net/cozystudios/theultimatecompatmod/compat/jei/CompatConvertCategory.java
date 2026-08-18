package net.cozystudios.theultimatecompatmod.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.cozystudios.theultimatecompatmod.TheUltimateCompatMod;
import net.cozystudios.theultimatecompatmod.workbench.CompatConvertRecipe;
import net.cozystudios.theultimatecompatmod.workbench.ModRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CompatConvertCategory implements IRecipeCategory<CompatConvertRecipe> {

    public static final Identifier UID = new Identifier(TheUltimateCompatMod.MOD_ID, "convert");
    public static final RecipeType<CompatConvertRecipe> RECIPE_TYPE =
            new RecipeType<>(UID, CompatConvertRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final Text title;

    public CompatConvertCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(120, 18);
        this.icon = helper.createDrawableItemStack(new ItemStack(ModRegistry.COMPAT_WORKBENCH_ITEM));
        this.title = Text.translatable("block.theultimatecompatmod.compat_workbench");
    }

    @Override
    public RecipeType<CompatConvertRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Text getTitle() {
        return this.title;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CompatConvertRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT,   1,  1).addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 101, 1).addItemStack(recipe.getResult());
    }
}
