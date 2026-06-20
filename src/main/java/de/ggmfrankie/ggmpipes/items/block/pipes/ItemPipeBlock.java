package de.ggmfrankie.ggmpipes.items.block.pipes;

import de.ggmfrankie.ggmpipes.items.block.PipeBlock;
import de.ggmfrankie.ggmpipes.registry.ModBlocks;
import de.ggmfrankie.ggmpipes.utils.CapabilityHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

public class ItemPipeBlock extends PipeBlock {
    public ItemPipeBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean canConnect(Level level, BlockPos pos, Direction dir) {
        BlockState state = level.getBlockState(pos);
        return state.getBlock() instanceof ItemPipeBlock ||
               state.getBlock() instanceof ItemPipeEntityBlock;
    }

    @Override
    protected boolean hasMachineConnection(Level level, BlockPos pos) {
        return 0 != (CapabilityHelper.getMachineConnections(level, pos, CapabilityHelper::hasItemCapability));
    }

    @Override
    @NullMarked
    public void neighborChanged(
            BlockState state,
            Level level,
            BlockPos pos,
            Block neighborBlock,
            @Nullable Orientation orientation,
            boolean movedByPiston
    ) {
        if (level.isClientSide()) return;

        boolean hasMachine = hasMachineConnection(level, pos);
        if (hasMachine) {
            // Upgrade to BlockEntity when Machine is connected
            BlockState newState = ModBlocks.ITEM_PIPE_ENTITY_BLOCK.get().getPlacementState(level, pos);
            level.setBlock(pos, newState, Block.UPDATE_ALL);
        } else {
            BlockState newState = getPlacementState(level, pos);
            level.setBlock(pos, newState, Block.UPDATE_CLIENTS);
        }
    }
}
