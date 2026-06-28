package de.ggmfrankie.ggmpipes.items.block.pipes;

import de.ggmfrankie.ggmpipes.items.block.PipeBlock;
import de.ggmfrankie.ggmpipes.registry.ModBlocks;
import de.ggmfrankie.ggmpipes.utils.CapabilityHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ItemPipeBlock extends PipeBlock {
    public ItemPipeBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean canConnect(Level level, BlockPos pos, Direction dir) {
        Block block = level.getBlockState(pos).getBlock();
        return block instanceof ItemPipeBlock ||
               block instanceof ItemPipeEntityBlock;
    }

    @Override
    protected boolean hasMachineConnection(Level level, BlockPos pos) {
        return 0 != (CapabilityHelper.getMachineConnections(level, pos, CapabilityHelper::hasItemCapability));
    }

    @Override
    protected void update(Level level, BlockPos pos){
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
