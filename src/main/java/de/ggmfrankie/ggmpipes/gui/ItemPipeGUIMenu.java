package de.ggmfrankie.ggmpipes.gui;

import de.ggmfrankie.ggmpipes.items.tileentity.ItemPipeEntity;
import de.ggmfrankie.ggmpipes.items.tileentity.PipeEntity;
import de.ggmfrankie.ggmpipes.items.tileentity.filter.BasicItemFilter;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
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
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));

    }

    public ItemPipeGUIMenu(int containerId, Inventory inv, BlockEntity blockEntity) {
        super(, containerId, inv, blockEntity);
        this.blockEntity = ((ItemPipeEntity) blockEntity);
        this.level = inv.player.level();

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        BasicItemFilter insertFilter = ((ItemPipeEntity) blockEntity).getInsertFilters().get(Direction.EAST);
        BasicItemFilter extractFilter = ((ItemPipeEntity) blockEntity).getExtractFilters().get(Direction.EAST);

        addFilter(insertFilter, 0);
        addFilter(extractFilter, 100);
    }

    @Override
    @NullMarked
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    @NullMarked
    public boolean stillValid(Player player) {
        return false;
    }

    private void addPlayerInventory(Inventory playerInventory){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory){
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    private void addFilter(Container filterInv, int offset) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new GhostSlot(filterInv, j + i * 9 + 9, 8 + j * 18 + offset, 16 + i * 18));
            }
        }
    }
}
