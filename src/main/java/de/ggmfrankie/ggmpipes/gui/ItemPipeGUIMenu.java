package de.ggmfrankie.ggmpipes.gui;

import de.ggmfrankie.ggmpipes.items.tileentity.ItemPipeEntity;
import de.ggmfrankie.ggmpipes.items.tileentity.PipeEntity;
import de.ggmfrankie.ggmpipes.items.tileentity.filter.BasicItemFilter;
import de.ggmfrankie.ggmpipes.registry.ModMenuTypes;
import de.ggmfrankie.ggmpipes.utils.DirectionUtils;
import net.minecraft.core.BlockPos;
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
    private final BlockPos pos;
    private final Direction clickedDirection;
    private final boolean isInserting;
    private final boolean isExtracting;

    public ItemPipeGUIMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, extraData.readBlockPos(), Direction.from3DDataValue(extraData.readByte()));
    }

    public ItemPipeGUIMenu(int containerId, Inventory inventory, BlockPos pos, Direction direction) {
        super(ModMenuTypes.ITEM_PIPE_MENU.get(), containerId);
        this.pos = pos;
        this.clickedDirection = direction;

        if (inventory.player.level().getBlockEntity(pos) instanceof ItemPipeEntity pipe) {
            this.isInserting = pipe.isInserting(direction);
            this.isExtracting = pipe.isExtracting(direction);

            BasicItemFilter insertFilter  = pipe.getInsertFilters().get(direction);
            BasicItemFilter extractFilter = pipe.getExtractFilters().get(direction);

            assert insertFilter  != null;
            assert extractFilter != null;

            addFilter(insertFilter, 0);
            addFilter(extractFilter, 90);

        } else {
            this.isInserting = false;
            this.isExtracting = false;
        }

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
    }

    @Override
    @NullMarked
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    @NullMarked
    public boolean stillValid(Player player) {
        return player.distanceToSqr(pos.getCenter()) <= 64.0;
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

    public Direction getClickedDirection() {
        return clickedDirection;
    }

    public BlockPos getBlockEntityPos(){
        return pos;
    }

    public boolean isInserting() {
        return isInserting;
    }

    public boolean isExtracting() {
        return isExtracting;
    }
}
