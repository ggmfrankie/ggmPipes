package de.ggmfrankie.ggmpipes.items.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static de.ggmfrankie.ggmpipes.items.blockentity.PipeBlock.*;


public abstract class PipeEntity extends BlockEntity {

    private int connectionMask;

    public PipeEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        connectionMask = calculateMask(state);
    }

    private static int calculateMask(BlockState state){
        int mask = 0;
        if (state.getValue(NORTH)) mask |= 1;
        if (state.getValue(SOUTH)) mask |= 2;
        if (state.getValue(EAST))  mask |= 4;
        if (state.getValue(WEST))  mask |= 8;
        if (state.getValue(UP))    mask |= 16;
        if (state.getValue(DOWN))  mask |= 32;

        return mask;
    }

    public int getMask(){
        return this.connectionMask;
    }
}
