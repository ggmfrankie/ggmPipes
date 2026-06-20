package de.ggmfrankie.ggmpipes.items.block.pipes;

import com.mojang.serialization.MapCodec;
import de.ggmfrankie.ggmpipes.items.block.PipeEntityBlock;
import de.ggmfrankie.ggmpipes.items.tileentity.ItemPipeEntity;
import de.ggmfrankie.ggmpipes.items.tileentity.PipeEntity;
import de.ggmfrankie.ggmpipes.registry.ModBlocks;
import de.ggmfrankie.ggmpipes.utils.CapabilityHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

public class ItemPipeEntityBlock extends PipeEntityBlock {

    public static final MapCodec<ItemPipeEntityBlock> CODEC = simpleCodec(ItemPipeEntityBlock::new);

    public ItemPipeEntityBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void update(Level level, BlockPos pos) {
        if (level.isClientSide()) return;

        boolean hasConnection = hasMachineConnection(level, pos);
        BlockEntity entity = level.getBlockEntity(pos);


        if (entity instanceof PipeEntity pipeEntity) {
            if (hasConnection) {
                BlockState state = level.getBlockState(pos);
                BlockState newState = getPlacementState(level, pos);
                pipeEntity.onNeighborChanged();
                level.setBlock(pos, newState, Block.UPDATE_ALL);
                level.sendBlockUpdated(pos, state, newState, Block.UPDATE_CLIENTS);
            } else {
                BlockState newState = ModBlocks.ITEM_PIPE_BLOCK.get().getPlacementState(level, pos);
                level.setBlock(pos, newState, Block.UPDATE_ALL);
            }

        }
    }

    @Override
    @NullMarked
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ItemPipeEntity(blockPos, blockState);
    }

    @Override
    protected boolean canConnect(Level level, BlockPos pos, Direction dir){
        BlockState state = level.getBlockState(pos);

        return state.getBlock() instanceof ItemPipeEntityBlock || state.getBlock() instanceof ItemPipeBlock || CapabilityHelper.hasItemCapability(level, pos, dir.getOpposite());
    }

    @Override
    protected boolean hasMachineConnection(Level level, BlockPos pos) {
        return 0 != (CapabilityHelper.getMachineConnections(level, pos, CapabilityHelper::hasItemCapability));
    }

    @Override
    @NullMarked
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
