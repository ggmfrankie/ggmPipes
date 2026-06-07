package de.ggmfrankie.ggmpipes.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.function.Predicate;

public class CapabilityHelper {

    @FunctionalInterface
    public interface TriPredicate<T, U, V> {
        boolean run(T t, U u, V v);
    }

    public static boolean hasItemCapability(Level level, BlockPos pos, Direction dir){
        ResourceHandler<ItemResource> handler = level.getCapability(Capabilities.Item.BLOCK, pos, dir.getOpposite());
        return handler != null;
    }

    public static int getMachineConnections(Level level, BlockPos pos, TriPredicate<Level, BlockPos, Direction> filter) {
        int mask = 0;
        if (filter.run(level, pos.north(), Direction.NORTH)) mask |= DirectionMask.NORTH;
        if (filter.run(level, pos.south(), Direction.SOUTH)) mask |= DirectionMask.SOUTH;
        if (filter.run(level, pos.east(),  Direction.EAST))  mask |= DirectionMask.EAST;
        if (filter.run(level, pos.west(),  Direction.WEST))  mask |= DirectionMask.WEST;
        if (filter.run(level, pos.above(), Direction.UP))    mask |= DirectionMask.UP;
        if (filter.run(level, pos.below(), Direction.DOWN))  mask |= DirectionMask.DOWN;

        return mask;
    }
}
