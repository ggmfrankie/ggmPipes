package de.ggmfrankie.ggmpipes.items.tileentity;

import de.ggmfrankie.ggmpipes.NetworkHandler;
import de.ggmfrankie.ggmpipes.gui.ItemPipeGUIMenu;
import de.ggmfrankie.ggmpipes.items.tileentity.filter.BasicItemFilter;
import de.ggmfrankie.ggmpipes.registry.ModBlockEntities;
import de.ggmfrankie.ggmpipes.utils.CapabilityHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStackResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class ItemPipeEntity extends PipeEntity {


    private final EnumMap<Direction, BasicItemFilter> insertFilters;
    private final EnumMap<Direction, BasicItemFilter> extractFilters;


    public ItemPipeEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.ITEM_PIPE_ENTITY.get(), worldPosition, blockState);
        this.insertFilters = new EnumMap<>(Direction.class);
        this.extractFilters = new EnumMap<>(Direction.class);

        for (var dir : Direction.values()) {
            insertFilters.put(dir, new BasicItemFilter(this));
            extractFilters.put(dir, new BasicItemFilter(this));
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.memberNetwork = getOrCreateNetwork(level, worldPosition);
        NetworkHandler.addToNetwork(memberNetwork, this);
    }

    @Override
    @NullMarked
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if (memberNetwork != null) NetworkHandler.removeFromNetwork(memberNetwork, this);
    }

    @Override
    protected UUID getOrCreateNetwork(Level level, BlockPos start){
        UUID id = super.getOrCreateNetwork(level, start);

        return (id == null) ? NetworkHandler.createNewItemNetwork() : id;
    }

    @Override
    public void onChunkUnloaded() {
        if (memberNetwork != null) NetworkHandler.removeFromNetwork(memberNetwork, this);
    }

    @Override
    protected int calculateConnectionMask(Level level, BlockPos pos) {
        return CapabilityHelper.getMachineConnections(level, pos, CapabilityHelper::hasItemCapability);
    }

    @Override
    public void updateConnectionsInNetwork() {
        if (memberNetwork != null){
            NetworkHandler.removeFromNetwork(memberNetwork, this);
            NetworkHandler.addToNetwork(memberNetwork, this);
        }
    }

    @Override
    @NullMarked
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ItemPipeGUIMenu(i, inventory, this.getBlockPos(), getClickedDirection());
    }

    public EnumMap<Direction, BasicItemFilter> getInsertFilters(){
        return this.insertFilters;
    }

    public EnumMap<Direction, BasicItemFilter> getExtractFilters() {
        return this.extractFilters;
    }
}
