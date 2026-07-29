package net.cozystudios.theultimatecompatmod;

import net.cozystudios.theultimatecompatmod.farming.FarmingDuplicates;
import net.cozystudios.theultimatecompatmod.wood.WoodDuplicates;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TheUltimateCompatMod implements ModInitializer {
    public static final String MOD_ID = "theultimatecompatmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        FarmingDuplicates.register();
        WoodDuplicates.register();

        LOGGER.info("[TheUltimateCompatMod] Loaded. {} duplicate items will be hidden across all domains.",
                HiddenItemRegistry.map().size());
        CreativeTabHandler.register();
    }
}
