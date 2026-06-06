package de.ggmfrankie.ggmpipes.items.tileentity;

import de.ggmfrankie.ggmpipes.NetworkHandler;
import de.ggmfrankie.ggmpipes.items.tileentity.network.ItemPipeNetwork;
import de.ggmfrankie.ggmpipes.items.tileentity.network.PipeNetwork;
import de.ggmfrankie.ggmpipes.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class ItemPipeEntity extends PipeEntity {

    public ItemPipeEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.ITEM_PIPE_ENTITY.get(), worldPosition, blockState);
        this.memberNetwork = getOrCreateNetwork(this.getLevel(), worldPosition);

        NetworkHandler.addToNetwork(this.memberNetwork, this);
    }

    @Override
    protected UUID getOrCreateNetwork(Level level, BlockPos start){
        UUID id = super.getOrCreateNetwork(level, start);

        return (id == null) ? NetworkHandler.createNewItemNetwork() : id;
    }


}
