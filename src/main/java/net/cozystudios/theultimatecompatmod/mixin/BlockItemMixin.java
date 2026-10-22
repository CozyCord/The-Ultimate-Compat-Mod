package net.cozystudios.theultimatecompatmod.mixin;

import net.cozystudios.theultimatecompatmod.placed.PlacedLoserRegistry;
import net.cozystudios.theultimatecompatmod.placed.PlacedLoserTracker;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At("RETURN"))
    private void theultimatecompatmod$afterPlace(ItemPlacementContext ctx,
                                                  CallbackInfoReturnable<ActionResult> cir) {
        ActionResult result = cir.getReturnValue();
        if (result == null || !result.isAccepted()) return;
        World world = ctx.getWorld();
        if (world.isClient) return;
        if (!PlacedLoserRegistry.isLoserStack(ctx.getStack())) return;
        PlacedLoserTracker.mark(world, ctx.getBlockPos());
    }
}
