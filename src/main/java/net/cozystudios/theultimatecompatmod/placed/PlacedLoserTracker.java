package net.cozystudios.theultimatecompatmod.placed;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.cozystudios.theultimatecompatmod.TheUltimateCompatMod;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;

public final class PlacedLoserTracker {

    private PlacedLoserTracker() {}

    private static final Codec<LongSet> LONG_SET_CODEC =
            Codec.LONG.listOf().xmap(LongOpenHashSet::new, java.util.ArrayList::new);

    public static final AttachmentType<LongSet> ATTACHMENT =
            AttachmentRegistry.<LongSet>builder()
                    .persistent(LONG_SET_CODEC)
                    .initializer(LongOpenHashSet::new)
                    .buildAndRegister(new Identifier(TheUltimateCompatMod.MOD_ID, "placed_losers"));

    public static void mark(World world, BlockPos pos) {
        if (world.isClient) return;
        Chunk chunk = world.getChunk(pos);
        if (!(chunk instanceof WorldChunk wc)) return;
        LongSet set = wc.getAttachedOrCreate(ATTACHMENT);
        if (set.add(pos.asLong())) {
            wc.setAttached(ATTACHMENT, set);
            wc.setNeedsSaving(true);
        }
    }

    public static boolean isMarked(World world, BlockPos pos) {
        Chunk chunk = world.getChunk(pos);
        if (!(chunk instanceof WorldChunk wc)) return false;
        LongSet set = wc.getAttached(ATTACHMENT);
        if (set == null) return false;
        return set.contains(pos.asLong());
    }

    public static void unmark(World world, BlockPos pos) {
        if (world.isClient) return;
        Chunk chunk = world.getChunk(pos);
        if (!(chunk instanceof WorldChunk wc)) return;
        LongSet set = wc.getAttached(ATTACHMENT);
        if (set == null || set.isEmpty()) return;
        if (set.remove(pos.asLong())) {
            wc.setAttached(ATTACHMENT, set);
            wc.setNeedsSaving(true);
        }
    }

    public static void register() {
    }
}
