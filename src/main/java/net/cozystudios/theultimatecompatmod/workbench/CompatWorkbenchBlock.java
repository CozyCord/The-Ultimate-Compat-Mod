package net.cozystudios.theultimatecompatmod.workbench;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class CompatWorkbenchBlock extends HorizontalFacingBlock {

    private static final Text TITLE = Text.translatable("container.theultimatecompatmod.compat_workbench");

    public CompatWorkbenchBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player,
                              Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
        player.incrementStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
        return ActionResult.CONSUME;
    }

    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new Factory(pos);
    }

    private static final class Factory implements ExtendedScreenHandlerFactory {
        private final BlockPos pos;

        Factory(BlockPos pos) { this.pos = pos; }

        @Override
        public Text getDisplayName() { return TITLE; }

        @Override
        public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
            return new CompatWorkbenchMenu(syncId, inv, ScreenHandlerContext.create(player.getWorld(), pos));
        }

        @Override
        public void writeScreenOpeningData(net.minecraft.server.network.ServerPlayerEntity player, PacketByteBuf buf) {
            buf.writeBlockPos(pos);
        }
    }
}
