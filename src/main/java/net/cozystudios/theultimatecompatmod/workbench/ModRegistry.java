package net.cozystudios.theultimatecompatmod.workbench;

import net.cozystudios.theultimatecompatmod.TheUltimateCompatMod;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public final class ModRegistry {

    private ModRegistry() {}

    public static final Identifier COMPAT_WORKBENCH_ID = new Identifier(TheUltimateCompatMod.MOD_ID, "compat_workbench");
    public static final Identifier CONVERT_ID = new Identifier(TheUltimateCompatMod.MOD_ID, "convert");

    public static final Block COMPAT_WORKBENCH_BLOCK = new CompatWorkbenchBlock(
            FabricBlockSettings.copyOf(Blocks.CRAFTING_TABLE)
    );
    public static final BlockItem COMPAT_WORKBENCH_ITEM = new BlockItem(COMPAT_WORKBENCH_BLOCK, new Item.Settings());

    public static final RecipeType<CompatConvertRecipe> CONVERT_RECIPE_TYPE = new RecipeType<>() {
        @Override
        public String toString() {
            return CONVERT_ID.toString();
        }
    };

    public static final RecipeSerializer<CompatConvertRecipe> CONVERT_SERIALIZER =
            CompatConvertRecipeSerializer.INSTANCE;

    public static final ScreenHandlerType<CompatWorkbenchMenu> COMPAT_WORKBENCH_MENU_TYPE =
            CompatWorkbenchMenu.TYPE;

    public static void register() {
        Registry.register(Registries.BLOCK, COMPAT_WORKBENCH_ID, COMPAT_WORKBENCH_BLOCK);
        Registry.register(Registries.ITEM, COMPAT_WORKBENCH_ID, COMPAT_WORKBENCH_ITEM);
        Registry.register(Registries.RECIPE_TYPE, CONVERT_ID, CONVERT_RECIPE_TYPE);
        Registry.register(Registries.RECIPE_SERIALIZER, CONVERT_ID, CONVERT_SERIALIZER);
        Registry.register(Registries.SCREEN_HANDLER,
                new Identifier(TheUltimateCompatMod.MOD_ID, "compat_workbench_menu"),
                CompatWorkbenchMenu.TYPE);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries ->
                entries.addAfter(Items.CRAFTING_TABLE, COMPAT_WORKBENCH_ITEM));
    }
}
