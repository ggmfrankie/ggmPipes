package de.ggmfrankie.ggmpipes.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

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
        if (filter.run(level, pos.north(), Direction.NORTH)) mask |= DirectionUtils.NORTH;
        if (filter.run(level, pos.south(), Direction.SOUTH)) mask |= DirectionUtils.SOUTH;
        if (filter.run(level, pos.east(),  Direction.EAST))  mask |= DirectionUtils.EAST;
        if (filter.run(level, pos.west(),  Direction.WEST))  mask |= DirectionUtils.WEST;
        if (filter.run(level, pos.above(), Direction.UP))    mask |= DirectionUtils.UP;
        if (filter.run(level, pos.below(), Direction.DOWN))  mask |= DirectionUtils.DOWN;

        return mask;
    }
}
