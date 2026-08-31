package net.cozystudios.theultimatecompatmod.unlocks;

import com.mojang.serialization.Codec;
import net.cozystudios.theultimatecompatmod.TheUltimateCompatMod;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public final class PlayerUnlocks {

    private PlayerUnlocks() {}

    private static final Codec<Set<Identifier>> SET_CODEC =
            Identifier.CODEC.listOf().xmap(HashSet::new, java.util.ArrayList::new);

    public static final AttachmentType<Set<Identifier>> ATTACHMENT =
            AttachmentRegistry.<Set<Identifier>>builder()
                    .persistent(SET_CODEC)
                    .initializer(HashSet::new)
                    .buildAndRegister(new Identifier(TheUltimateCompatMod.MOD_ID, "unlocks"));

    public static void register() {
    }

    public static Set<Identifier> get(PlayerEntity player) {
        Set<Identifier> current = player.getAttached(ATTACHMENT);
        return current == null ? Set.of() : current;
    }

    public static Set<Identifier> unlockAll(ServerPlayerEntity player, Set<Identifier> items) {
        Set<Identifier> current = player.getAttachedOrCreate(ATTACHMENT);
        Set<Identifier> updated = new HashSet<>(current);
        Set<Identifier> newlyAdded = new LinkedHashSet<>();
        for (Identifier id : items) {
            if (updated.add(id)) newlyAdded.add(id);
        }
        if (!newlyAdded.isEmpty()) {
            player.setAttached(ATTACHMENT, updated);
        }
        return newlyAdded;
    }
}
