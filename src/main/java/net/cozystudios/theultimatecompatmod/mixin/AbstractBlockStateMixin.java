package net.cozystudios.theultimatecompatmod.mixin;

import net.cozystudios.theultimatecompatmod.placed.PlacedLoserRegistry;
import net.cozystudios.theultimatecompatmod.placed.PlacedLoserTracker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class AbstractBlockStateMixin {

    @Inject(method = "onPlaced", at = @At("TAIL"))
    private void theultimatecompatmod$onPlaced(World world, BlockPos pos, BlockState state,
                                                @Nullable LivingEntity placer, ItemStack itemStack,
                                                CallbackInfo ci) {
        if (world.isClient) return;
        if (placer == null) return;
        if (!PlacedLoserRegistry.isLoserBlock(state.getBlock())
                && !PlacedLoserRegistry.isLoserStack(itemStack)) {
            return;
        }
        PlacedLoserTracker.mark(world, pos);
    }

    @Inject(method = "afterBreak", at = @At("TAIL"))
    private void theultimatecompatmod$afterBreak(World world, PlayerEntity player, BlockPos pos,
                                                  BlockState state, @Nullable BlockEntity blockEntity,
                                                  ItemStack stack, CallbackInfo ci) {
        if (world.isClient) return;
        if (!PlacedLoserRegistry.isLoserBlock(state.getBlock())) return;
        PlacedLoserTracker.unmark(world, pos);
    }
}
