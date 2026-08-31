package net.cozystudios.theultimatecompatmod.unlocks;

import io.netty.buffer.Unpooled;
import net.cozystudios.theultimatecompatmod.TheUltimateCompatMod;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Set;

public final class UnlockNetworking {

    public static final Identifier FULL_SYNC = new Identifier(TheUltimateCompatMod.MOD_ID, "unlocks_full");
    public static final Identifier DELTA = new Identifier(TheUltimateCompatMod.MOD_ID, "unlocks_delta");

    private UnlockNetworking() {}

    public static void sendFullSync(ServerPlayerEntity player) {
        Set<Identifier> current = PlayerUnlocks.get(player);
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(current.size());
        for (Identifier id : current) buf.writeIdentifier(id);
        ServerPlayNetworking.send(player, FULL_SYNC, buf);
    }

    public static void sendDelta(ServerPlayerEntity player, Set<Identifier> newlyUnlocked) {
        if (newlyUnlocked.isEmpty()) return;
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeVarInt(newlyUnlocked.size());
        for (Identifier id : newlyUnlocked) buf.writeIdentifier(id);
        ServerPlayNetworking.send(player, DELTA, buf);
    }
}
