package de.ggmfrankie.ggmpipes.gui;

import de.ggmfrankie.ggmpipes.items.tileentity.ItemPipeEntity;
import de.ggmfrankie.ggmpipes.items.tileentity.PipeEntity;
import de.ggmfrankie.ggmpipes.items.tileentity.filter.BasicItemFilter;
import de.ggmfrankie.ggmpipes.registry.ModMenuTypes;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jspecify.annotations.NullMarked;

public class ItemPipeGUIMenu extends AbstractContainerMenu {
    public final PipeEntity blockEntity;
    private final Level level;

    public ItemPipeGUIMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), Direction.from3DDataValue(extraData.readByte()));
    }

    public ItemPipeGUIMenu(int containerId, Inventory inv, BlockEntity blockEntity, Direction direction) {
        super(ModMenuTypes.ITEM_PIPE_MENU.get(), containerId);
        this.blockEntity = ((ItemPipeEntity) blockEntity);
        this.level = inv.player.level();

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        BasicItemFilter insertFilter = ((ItemPipeEntity) blockEntity).getInsertFilters().get(direction);
        BasicItemFilter extractFilter = ((ItemPipeEntity) blockEntity).getExtractFilters().get(direction);

        assert insertFilter != null;
        assert extractFilter != null;

        addFilter(insertFilter, 0);
        addFilter(extractFilter, 90);
    }

    @Override
    @NullMarked
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    @NullMarked
    public boolean stillValid(Player player) {
        if (blockEntity == null) return false;
        return player.distanceToSqr(blockEntity.getBlockPos().getCenter()) <= 64.0;
    }

    private void addPlayerInventory(Inventory playerInventory){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    private void addFilter(Container filterInv, int offset) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                this.addSlot(new GhostSlot(filterInv, j + i * 4, 8 + j * 18 + offset, 18 + i * 18));
            }
        }
    }
}
