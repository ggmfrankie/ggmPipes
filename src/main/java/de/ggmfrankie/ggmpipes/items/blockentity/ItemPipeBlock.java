package de.ggmfrankie.ggmpipes.items.blockentity;

import de.ggmfrankie.ggmpipes.items.tileentity.ItemPipeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

public class ItemPipeBlock extends PipeBlock {

    public ItemPipeBlock(Properties properties) {
        super(properties);
    }

    @Override
    @NullMarked
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return blockState.getValue(HAS_MACHINE_CONNECTION) ? new ItemPipeEntity(blockPos, blockState) : null;
    }
}
