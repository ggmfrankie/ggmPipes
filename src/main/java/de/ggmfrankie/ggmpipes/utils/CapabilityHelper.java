package de.ggmfrankie.ggmpipes.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class CapabilityHelper {

    public static boolean hasItemCapability(Level level, BlockPos pos, Direction dir){
        ResourceHandler<ItemResource> handler = level.getCapability(Capabilities.Item.BLOCK, pos, dir.getOpposite());
        return handler != null;
    }
}
