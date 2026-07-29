package net.cozystudios.theultimatecompatmod.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.runtime.IJeiRuntime;
import net.cozystudios.theultimatecompatmod.HiddenItemRegistry;
import net.cozystudios.theultimatecompatmod.TheUltimateCompatMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
