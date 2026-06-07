package de.ggmfrankie.ggmpipes.utils;

import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;

public class DirectionMask {
    public static final int NORTH = 1;
    public static final int SOUTH = 2;

    public static final int EAST  = 4;
    public static final int WEST = 8;

    public static final int UP    = 16;
    public static final int DOWN  = 32;

    public static final int ALL = NORTH|SOUTH|EAST| WEST |UP|DOWN;

    public static List<Direction> getDirectionsFromMask(int mask){
        List<Direction> directions = new ArrayList<>();

        if ((mask & DirectionMask.NORTH) != 0) directions.add(Direction.NORTH);
        if ((mask & DirectionMask.SOUTH) != 0) directions.add(Direction.SOUTH);
        if ((mask & DirectionMask.EAST) != 0)  directions.add(Direction.EAST);
        if ((mask & DirectionMask.WEST) != 0)  directions.add(Direction.WEST);
        if ((mask & DirectionMask.UP) != 0)    directions.add(Direction.UP);
        if ((mask & DirectionMask.DOWN) != 0)  directions.add(Direction.DOWN);

        return directions;
    }
}
