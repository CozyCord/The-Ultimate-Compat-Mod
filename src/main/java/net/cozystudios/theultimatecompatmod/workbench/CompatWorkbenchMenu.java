package net.cozystudios.theultimatecompatmod.workbench;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public class CompatWorkbenchMenu extends ScreenHandler {

    public static final ExtendedScreenHandlerType<CompatWorkbenchMenu> TYPE =
            new ExtendedScreenHandlerType<>(CompatWorkbenchMenu::fromBuf);

    private final ScreenHandlerContext context;
    private final PropertyDelegate selectedIndex = new ArrayPropertyDelegate(1);
    private List<CompatConvertRecipe> availableRecipes = Collections.emptyList();
    private ItemStack lastInput = ItemStack.EMPTY;
    long lastSoundTime;
    final Slot inputSlot;
    final Slot outputSlot;
    private final World world;
    private Runnable contentsChangedListener = () -> {};

    private final SimpleInventory input = new SimpleInventory(1) {
        @Override
        public void markDirty() {
            super.markDirty();
            CompatWorkbenchMenu.this.onContentChanged(this);
            CompatWorkbenchMenu.this.contentsChangedListener.run();
        }
    };
    private final CraftingResultInventory output = new CraftingResultInventory();

    private static CompatWorkbenchMenu fromBuf(int syncId, PlayerInventory inv, PacketByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        return new CompatWorkbenchMenu(syncId, inv, ScreenHandlerContext.create(inv.player.getWorld(), pos));
    }

    public CompatWorkbenchMenu(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(TYPE, syncId);
        this.context = context;
        this.world = playerInventory.player.getWorld();

        this.inputSlot = this.addSlot(new Slot(this.input, 0, 20, 33));
        this.outputSlot = this.addSlot(new Slot(this.output, 1, 143, 33) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                stack.onCraft(player.getWorld(), player, stack.getCount());
                CompatWorkbenchMenu.this.output.unlockLastRecipe(player, List.of(CompatWorkbenchMenu.this.inputSlot.getStack()));
                ItemStack in = CompatWorkbenchMenu.this.inputSlot.takeStack(1);
                if (!in.isEmpty()) {
                    CompatWorkbenchMenu.this.populateResult();
                }
                context.run((world, pos) -> {
                    long l = world.getTime();
                    if (CompatWorkbenchMenu.this.lastSoundTime != l) {
                        world.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        CompatWorkbenchMenu.this.lastSoundTime = l;
                    }
                });
                super.onTakeItem(player, stack);
            }
        });

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }
        for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 142));
        }

        this.addProperties(this.selectedIndex);
    }

    public void setContentsChangedListener(Runnable listener) {
        this.contentsChangedListener = listener;
    }

    public int getSelectedRecipe() {
        return this.selectedIndex.get(0);
    }

    public List<CompatConvertRecipe> getAvailableRecipes() {
        return this.availableRecipes;
    }

    public int getAvailableRecipeCount() {
        return this.availableRecipes.size();
    }

    public boolean canCraft() {
        return this.inputSlot.hasStack() && !this.availableRecipes.isEmpty();
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, ModRegistry.COMPAT_WORKBENCH_BLOCK);
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (this.isInBounds(id)) {
            this.selectedIndex.set(0, id);
            this.populateResult();
        }
        return true;
    }

    private boolean isInBounds(int id) {
        return id >= 0 && id < this.availableRecipes.size();
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        ItemStack current = this.inputSlot.getStack();
        if (!current.isOf(this.lastInput.getItem())) {
            this.lastInput = current.copy();
            this.updateInput(inventory, current);
        }
    }

    private void updateInput(Inventory inventory, ItemStack stack) {
        this.availableRecipes = Collections.emptyList();
        this.selectedIndex.set(0, -1);
        this.output.setStack(0, ItemStack.EMPTY);
        if (!stack.isEmpty()) {
            this.availableRecipes = this.world.getRecipeManager()
                    .getAllMatches(ModRegistry.CONVERT_RECIPE_TYPE, inventory, this.world);
        }
    }

    void populateResult() {
        if (!this.availableRecipes.isEmpty() && this.isInBounds(this.selectedIndex.get(0))) {
            CompatConvertRecipe recipe = this.availableRecipes.get(this.selectedIndex.get(0));
            ItemStack result = recipe.craft(this.input, this.world.getRegistryManager());
            if (result.isItemEnabled(this.world.getEnabledFeatures())) {
                this.output.setLastRecipe(recipe);
                this.output.setStack(0, result);
            } else {
                this.output.setStack(0, ItemStack.EMPTY);
            }
        } else {
            this.output.setStack(0, ItemStack.EMPTY);
        }
        this.sendContentUpdates();
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.output && super.canInsertIntoSlot(stack, slot);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack empty = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack stack = slot.getStack();
            empty = stack.copy();
            if (index == 1) {
                stack.getItem().onCraft(stack, player.getWorld(), player);
                if (!this.insertItem(stack, 2, 38, true)) return ItemStack.EMPTY;
                slot.onQuickTransfer(stack, empty);
            } else if (index == 0) {
                if (!this.insertItem(stack, 2, 38, false)) return ItemStack.EMPTY;
            } else if (this.world.getRecipeManager()
                    .getFirstMatch(ModRegistry.CONVERT_RECIPE_TYPE, new SimpleInventory(stack), this.world)
                    .isPresent()) {
                if (!this.insertItem(stack, 0, 1, false)) return ItemStack.EMPTY;
            } else if (index >= 2 && index < 29) {
                if (!this.insertItem(stack, 29, 38, false)) return ItemStack.EMPTY;
            } else if (index >= 29 && index < 38) {
                if (!this.insertItem(stack, 2, 29, false)) return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) slot.setStack(ItemStack.EMPTY);
            slot.markDirty();
            if (stack.getCount() == empty.getCount()) return ItemStack.EMPTY;
            slot.onTakeItem(player, stack);
            this.sendContentUpdates();
        }
        return empty;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.output.removeStack(1);
        this.context.run((world, pos) -> this.dropInventory(player, this.input));
    }
}
