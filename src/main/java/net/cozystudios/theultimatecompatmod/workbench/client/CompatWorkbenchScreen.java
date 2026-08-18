package net.cozystudios.theultimatecompatmod.workbench.client;

import net.cozystudios.theultimatecompatmod.workbench.CompatConvertRecipe;
import net.cozystudios.theultimatecompatmod.workbench.CompatWorkbenchMenu;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class CompatWorkbenchScreen extends HandledScreen<CompatWorkbenchMenu> {

    private static final Identifier TEXTURE = new Identifier("textures/gui/container/stonecutter.png");

    private float scrollAmount;
    private boolean mouseClicked;
    private int scrollOffset;
    private boolean canCraft;

    public CompatWorkbenchScreen(CompatWorkbenchMenu handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        handler.setContentsChangedListener(this::onInventoryChange);
        this.titleY -= 2;
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(ctx, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext ctx, float delta, int mouseX, int mouseY) {
        this.renderBackground(ctx);
        int x = this.x;
        int y = this.y;
        ctx.drawTexture(TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        int scrollerY = (int) (41.0f * this.scrollAmount);
        ctx.drawTexture(TEXTURE, x + 119, y + 15 + scrollerY,
                176 + (this.shouldScroll() ? 0 : 12), 0, 12, 15);
        int listX = this.x + 52;
        int listY = this.y + 14;
        int endIndex = this.scrollOffset + 12;
        this.renderRecipeBackground(ctx, mouseX, mouseY, listX, listY, endIndex);
        this.renderRecipeIcons(ctx, listX, listY, endIndex);
    }

    @Override
    protected void drawMouseoverTooltip(DrawContext ctx, int x, int y) {
        super.drawMouseoverTooltip(ctx, x, y);
        if (this.canCraft) {
            int i = this.x + 52;
            int j = this.y + 14;
            int k = this.scrollOffset + 12;
            List<CompatConvertRecipe> recipes = this.handler.getAvailableRecipes();
            for (int l = this.scrollOffset; l < k && l < this.handler.getAvailableRecipeCount(); ++l) {
                int m = l - this.scrollOffset;
                int n = i + m % 4 * 16;
                int o = j + m / 4 * 18 + 2;
                if (x < n || x >= n + 16 || y < o || y >= o + 18) continue;
                ctx.drawItemTooltip(this.textRenderer, recipes.get(l).getResult(), x, y);
            }
        }
    }

    private void renderRecipeBackground(DrawContext ctx, int mouseX, int mouseY, int x, int y, int endIndex) {
        for (int i = this.scrollOffset; i < endIndex && i < this.handler.getAvailableRecipeCount(); ++i) {
            int j = i - this.scrollOffset;
            int k = x + j % 4 * 16;
            int m = y + (j / 4) * 18 + 2;
            int v;
            if (i == this.handler.getSelectedRecipe()) {
                v = this.backgroundHeight;
            } else if (mouseX >= k && mouseY >= m && mouseX < k + 16 && mouseY < m + 18) {
                v = this.backgroundHeight + 18;
            } else {
                continue;
            }
            ctx.drawTexture(TEXTURE, k, m - 1, 0, v, 16, 18);
        }
    }

    private void renderRecipeIcons(DrawContext ctx, int x, int y, int endIndex) {
        List<CompatConvertRecipe> recipes = this.handler.getAvailableRecipes();
        for (int i = this.scrollOffset; i < endIndex && i < this.handler.getAvailableRecipeCount(); ++i) {
            int j = i - this.scrollOffset;
            int k = x + j % 4 * 16;
            int m = y + (j / 4) * 18 + 2;
            ctx.drawItem(recipes.get(i).getResult(), k, m);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = false;
        if (this.canCraft) {
            int i = this.x + 52;
            int j = this.y + 14;
            int k = this.scrollOffset + 12;
            for (int l = this.scrollOffset; l < k; ++l) {
                int m = l - this.scrollOffset;
                double d = mouseX - (double) (i + m % 4 * 16);
                double e = mouseY - (double) (j + m / 4 * 18);
                if (d >= 0.0 && e >= 0.0 && d < 16.0 && e < 18.0
                        && this.handler.onButtonClick(this.client.player, l)) {
                    MinecraftClient.getInstance().getSoundManager().play(
                            PositionedSoundInstance.master(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0f));
                    this.client.interactionManager.clickButton(this.handler.syncId, l);
                    return true;
                }
            }
            int scrollerX = this.x + 119;
            int scrollerY = this.y + 9;
            if (mouseX >= scrollerX && mouseX < scrollerX + 12
                    && mouseY >= scrollerY && mouseY < scrollerY + 54) {
                this.mouseClicked = true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.mouseClicked && this.shouldScroll()) {
            int i = this.y + 14;
            int j = i + 54;
            this.scrollAmount = ((float) mouseY - (float) i - 7.5f) / ((float) (j - i) - 15.0f);
            this.scrollAmount = Math.max(0.0f, Math.min(1.0f, this.scrollAmount));
            this.scrollOffset = (int) ((double) (this.scrollAmount * (float) this.getMaxScroll()) + 0.5) * 4;
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (this.shouldScroll()) {
            int i = this.getMaxScroll();
            float f = (float) amount / (float) i;
            this.scrollAmount = Math.max(0.0f, Math.min(1.0f, this.scrollAmount - f));
            this.scrollOffset = (int) ((double) (this.scrollAmount * (float) i) + 0.5) * 4;
        }
        return true;
    }

    private boolean shouldScroll() {
        return this.canCraft && this.handler.getAvailableRecipeCount() > 12;
    }

    protected int getMaxScroll() {
        return (this.handler.getAvailableRecipeCount() + 4 - 1) / 4 - 3;
    }

    private void onInventoryChange() {
        this.canCraft = this.handler.canCraft();
        if (!this.canCraft) {
            this.scrollAmount = 0.0f;
            this.scrollOffset = 0;
        }
    }
}
