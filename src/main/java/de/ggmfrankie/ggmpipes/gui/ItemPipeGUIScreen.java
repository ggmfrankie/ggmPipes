package de.ggmfrankie.ggmpipes.gui;

import de.ggmfrankie.ggmpipes.ggmPipes;
import de.ggmfrankie.ggmpipes.gui.widget.ToggleButton;
import de.ggmfrankie.ggmpipes.networking.SetConnectionsPacket;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jspecify.annotations.NullMarked;

public class ItemPipeGUIScreen extends ScreenBase<ItemPipeGUIMenu> {
    public static final Identifier BACKGROUND_TEXTURE = Identifier.fromNamespaceAndPath(ggmPipes.MODID, "textures/gui/pipe/pipe_gui.png");

    private ToggleButton toggleInsertButton;
    private ToggleButton toggleExtractButton;

    public ItemPipeGUIScreen(ItemPipeGUIMenu menu, Inventory inventory, Component title) {
        super(BACKGROUND_TEXTURE, menu, inventory, title, 176, 166);
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
        BlockPos pos = menu.getBlockEntityPos();
        Direction dir = menu.getClickedDirection();

        this.toggleInsertButton = new ToggleButton(leftPos + 80, topPos + 20, 15, 20, menu.isInserting(), "I",
                (ToggleButton button) -> {
                    boolean state = button.isToggled();
                    ClientPacketDistributor.sendToServer(new SetConnectionsPacket(
                            pos,
                            dir,
                            true,
                            state
                    ));
                }
        );
        this.toggleExtractButton = new ToggleButton(leftPos + 80, topPos + 40, 15, 20, menu.isExtracting(), "E",
                (ToggleButton button) -> {
                    boolean state = button.isToggled();
                    ClientPacketDistributor.sendToServer(new SetConnectionsPacket(
                            pos,
                            dir,
                            false,
                            state
                    ));
                }
        );
        addRenderableWidget(toggleInsertButton);
        addRenderableWidget(toggleExtractButton);
    }
}
