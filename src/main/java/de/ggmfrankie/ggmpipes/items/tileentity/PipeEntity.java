package de.ggmfrankie.ggmpipes.items.tileentity;

import de.ggmfrankie.ggmpipes.items.block.PipeBlock;
import de.ggmfrankie.ggmpipes.utils.DirectionMask;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;


public abstract class PipeEntity extends BlockEntity {

    private int connectionMask;
    private int disabledMask;

    protected UUID memberNetwork;

    public PipeEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        connectionMask = PipeBlock.calculateMask(state);
        disabledMask = 0;
    }

    public void disableSide(Direction direction){
        switch (direction){
            case NORTH -> disabledMask |= DirectionMask.NORTH;
            case SOUTH -> disabledMask |= DirectionMask.SOUTH;

            case EAST  -> disabledMask |= DirectionMask.EAST;
            case WEST  -> disabledMask |= DirectionMask.WEST;

            case UP    -> disabledMask |= DirectionMask.UP;
            case DOWN  -> disabledMask |= DirectionMask.DOWN;
        }
    }

    public Direction[] getPipeConnections(){
        var directions = new Direction[6];
        int mask = this.connectionMask;
        int count = 0;

        if ((mask & DirectionMask.NORTH) != 0) directions[count++] = Direction.NORTH;
        if ((mask & DirectionMask.SOUTH) != 0) directions[count++] = Direction.SOUTH;
        if ((mask & DirectionMask.EAST) != 0)  directions[count++] = Direction.EAST;
        if ((mask & DirectionMask.WEST) != 0)  directions[count++] = Direction.WEST;
        if ((mask & DirectionMask.UP) != 0)    directions[count++] = Direction.UP;
        if ((mask & DirectionMask.DOWN) != 0)  directions[count]   = Direction.DOWN;

        return directions;
    }

    protected UUID getOrCreateNetwork(Level level, BlockPos start){
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()){
            BlockPos curr = queue.remove();
            BlockState state = level.getBlockState(curr);
            Direction[] pipeConnections = PipeBlock.getPipeConnections(state);

            for (var dir : pipeConnections){
                BlockPos neighbor = curr.relative(dir);

                if (visited.contains(neighbor)) continue;

                if (level.getBlockEntity(neighbor) instanceof PipeEntity entity){
                    return entity.getMemberNetwork();
                }

                visited.add(neighbor);
                queue.add(neighbor);
            }
        }
        return null;
    }

    public UUID getMemberNetwork(){
        return this.memberNetwork;
    }

    public int getMask(){
        return this.connectionMask;
    }





























}
