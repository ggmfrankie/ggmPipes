package de.ggmfrankie.ggmpipes.items.block;

import de.ggmfrankie.ggmpipes.items.tileentity.ItemPipeEntity;
import de.ggmfrankie.ggmpipes.utils.CapabilityHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
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

    @Override
    protected boolean canConnect(Level level, BlockPos pos, Direction dir){
        BlockState state = level.getBlockState(pos);

        return state.getBlock() instanceof PipeBlock || CapabilityHelper.hasItemCapability(level, pos, dir.getOpposite());
    }

    @Override
    protected boolean hasMachineConnection(Level level, BlockPos pos) {
        return 0 != CapabilityHelper.getMachineConnections(level, pos, CapabilityHelper::hasItemCapability);
    }
}
