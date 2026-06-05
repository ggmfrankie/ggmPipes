package de.ggmfrankie.ggmpipes.items.tileentity;

import de.ggmfrankie.ggmpipes.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ItemPipeEntity extends PipeEntity {
    public ItemPipeEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.ITEM_PIPE_ENTITY.get(), worldPosition, blockState);
    }
}
