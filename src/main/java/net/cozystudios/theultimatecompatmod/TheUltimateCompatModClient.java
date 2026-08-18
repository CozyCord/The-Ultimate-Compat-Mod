package net.cozystudios.theultimatecompatmod;

import net.cozystudios.theultimatecompatmod.workbench.ModRegistry;
import net.cozystudios.theultimatecompatmod.workbench.client.CompatWorkbenchScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class TheUltimateCompatModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModRegistry.COMPAT_WORKBENCH_MENU_TYPE, CompatWorkbenchScreen::new);
    }
}
