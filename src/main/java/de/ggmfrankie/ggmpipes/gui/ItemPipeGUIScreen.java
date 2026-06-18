package de.ggmfrankie.ggmpipes.gui;

import de.ggmfrankie.ggmpipes.ggmPipes;
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

    public ItemPipeGUIScreen(ItemPipeGUIMenu container, Inventory inventory, Component title) {
        super(BACKGROUND_TEXTURE, container, inventory, title, 176, 166);
    }
}
