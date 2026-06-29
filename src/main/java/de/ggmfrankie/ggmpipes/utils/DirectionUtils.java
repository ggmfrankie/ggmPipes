package de.ggmfrankie.ggmpipes.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class DirectionUtils {
    public static final int NORTH = 1;
    public static final int SOUTH = 2;

    public static final int EAST  = 4;
    public static final int WEST  = 8;

    public static final int UP    = 16;
    public static final int DOWN  = 32;

    public static final int ALL = NORTH|SOUTH|EAST|WEST|UP|DOWN;

    public static List<Direction> getDirectionsFromMask(int mask){
        List<Direction> directions = new ArrayList<>();

        if ((mask & DirectionUtils.NORTH) != 0) directions.add(Direction.NORTH);
        if ((mask & DirectionUtils.SOUTH) != 0) directions.add(Direction.SOUTH);
        if ((mask & DirectionUtils.EAST) != 0)  directions.add(Direction.EAST);
        if ((mask & DirectionUtils.WEST) != 0)  directions.add(Direction.WEST);
        if ((mask & DirectionUtils.UP) != 0)    directions.add(Direction.UP);
        if ((mask & DirectionUtils.DOWN) != 0)  directions.add(Direction.DOWN);

        return directions;
    }

    public static int getMaskFromDirection(Direction direction){
        return switch (direction) {
            case NORTH -> DirectionUtils.NORTH;
            case SOUTH -> DirectionUtils.SOUTH;
            case EAST  -> DirectionUtils.EAST;
            case WEST  -> DirectionUtils.WEST;
            case UP    -> DirectionUtils.UP;
            case DOWN  -> DirectionUtils.DOWN;
        };
    }

    public static Direction fromTo(BlockPos from, BlockPos to){
        return Direction.getApproximateNearest(
                to.getX() - from.getX(),
                to.getY() - from.getY(),
                to.getZ() - from.getZ()
        );
    }
}

