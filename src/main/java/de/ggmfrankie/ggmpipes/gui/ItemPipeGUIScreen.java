package de.ggmfrankie.ggmpipes.gui;

import de.ggmfrankie.ggmpipes.ggmPipes;
import de.ggmfrankie.ggmpipes.gui.widget.ToggleButton;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

public class ItemPipeGUIScreen extends ScreenBase<ItemPipeGUIMenu> {
    public static final Identifier BACKGROUND_TEXTURE = Identifier.fromNamespaceAndPath(ggmPipes.MODID, "textures/gui/pipe/pipe_gui.png");

    private ToggleButton toggleInsertButton;
    private ToggleButton toggleExtractButton;

    public ItemPipeGUIScreen(ItemPipeGUIMenu container, Inventory inventory, Component title) {
        super(BACKGROUND_TEXTURE, container, inventory, title, 176, 166);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
    }

    @Override
    @NullMarked
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
    }

    @Override
    protected void init() {
        super.init();
        this.toggleInsertButton = new ToggleButton(leftPos + 100, topPos + 20, 50, 20,
                menu.blockEntity.
        );
    }
}
