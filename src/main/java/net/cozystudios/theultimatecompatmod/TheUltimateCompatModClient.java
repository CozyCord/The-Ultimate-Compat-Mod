package net.cozystudios.theultimatecompatmod;

import net.cozystudios.theultimatecompatmod.unlocks.JeiUnlockBridge;
import net.cozystudios.theultimatecompatmod.unlocks.UnlockNetworking;
import net.cozystudios.theultimatecompatmod.unlocks.UnlockedClientState;
import net.cozystudios.theultimatecompatmod.workbench.ModRegistry;
import net.cozystudios.theultimatecompatmod.workbench.client.CompatWorkbenchScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;

public class TheUltimateCompatModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModRegistry.COMPAT_WORKBENCH_MENU_TYPE, CompatWorkbenchScreen::new);

        ClientPlayNetworking.registerGlobalReceiver(UnlockNetworking.FULL_SYNC, (client, handler, buf, sender) -> {
            int n = buf.readVarInt();
            Set<Identifier> full = new HashSet<>();
            for (int i = 0; i < n; i++) full.add(buf.readIdentifier());
            client.execute(() -> {
                UnlockedClientState.setAll(full);
                JeiUnlockBridge.unlockItems(full);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(UnlockNetworking.DELTA, (client, handler, buf, sender) -> {
            int n = buf.readVarInt();
            Set<Identifier> delta = new HashSet<>();
            for (int i = 0; i < n; i++) delta.add(buf.readIdentifier());
            client.execute(() -> {
                Set<Identifier> added = UnlockedClientState.addAll(delta);
                JeiUnlockBridge.unlockItems(added);
            });
        });

        net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.CLIENT_STOPPING.register(c -> UnlockedClientState.clear());
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.DISCONNECT.register((h, c) -> UnlockedClientState.clear());
    }
}
