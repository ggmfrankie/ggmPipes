package de.ggmfrankie.ggmpipes.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jspecify.annotations.NullMarked;

public class ScreenBase<T extends AbstractContainerMenu> extends AbstractContainerScreen<@org.jetbrains.annotations.NotNull T> {
    public static final int FONT_COLOR = 0xFF404040;
    protected Identifier texture;

    public ScreenBase(Identifier texture, T container, Inventory inventory, Component title, int width, int height) {
        super(container, inventory, title, width, height);
        this.texture = texture;
    }

    @Override
    @NullMarked
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        extractTooltip(graphics, mouseX, mouseY);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED,
                texture,
                x,
                y,
                0,
                0,
                imageWidth,
                imageHeight,
                256,
                256
        );
    }

    @Override
    @NullMarked
    protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {

    }
}
