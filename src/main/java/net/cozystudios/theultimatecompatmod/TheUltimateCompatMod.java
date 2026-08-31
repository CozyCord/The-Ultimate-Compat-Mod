package net.cozystudios.theultimatecompatmod;

import net.cozystudios.theultimatecompatmod.farming.FarmingDuplicates;
import net.cozystudios.theultimatecompatmod.unlocks.Families;
import net.cozystudios.theultimatecompatmod.unlocks.PlayerUnlocks;
import net.cozystudios.theultimatecompatmod.unlocks.UnlockNetworking;
import net.cozystudios.theultimatecompatmod.wood.WoodDuplicates;
import net.cozystudios.theultimatecompatmod.workbench.ModRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TheUltimateCompatMod implements ModInitializer {
    public static final String MOD_ID = "theultimatecompatmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        FarmingDuplicates.register();
        WoodDuplicates.register();
        Families.register();
        PlayerUnlocks.register();

        LOGGER.info("[TheUltimateCompatMod] Loaded. {} duplicate items will be hidden across all domains.",
                HiddenItemRegistry.map().size());
        CreativeTabHandler.register();
        ModRegistry.register();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                UnlockNetworking.sendFullSync(handler.player));
    }
}
