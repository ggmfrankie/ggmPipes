package de.ggmfrankie.ggmpipes.items.tileentity.network;

import de.ggmfrankie.ggmpipes.items.tileentity.PipeEntity;
import de.ggmfrankie.ggmpipes.items.tileentity.filter.BasicItemFilter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jspecify.annotations.NullMarked;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemPipeNetwork extends PipeNetwork {
    private List<ItemConnection> connections;

    public ItemPipeNetwork(){
        connections = new ArrayList<>(16);
    }

    public void update(){
        for (var connection : connections){

        }
    }

    @Override
    @NullMarked
    public void addNode(PipeEntity entity) {
        if (!(entity.getLevel() instanceof ServerLevel serverLevel)) {
            return;
        }
        BlockPos pos = entity.getBlockPos();
        for (var dir : entity.getPipeConnections()){
            connections.add(
                    new ItemConnection(
                            serverLevel,
                            pos.relative(dir),
                            dir,
                            ItemConnection.ItemConnectionMode.INSERT,
                            pos
                    )
            );
        }
    }

    @Override
    @NullMarked
    public void removeNode(PipeEntity entity) {
        if (entity.getLevel() == null || entity.getLevel().isClientSide()) {
            return;
        }
        BlockPos pos = entity.getBlockPos();
        connections.removeIf(connection -> connection.pipePos == pos);
    }

    public static class ItemConnection {
        public enum ItemConnectionMode {
            INSERT,
            EXTRACT,
            ALL
        }

        public int sleepTicks;

        private final Direction direction;

        private ItemConnectionMode mode;

        @Nullable private BasicItemFilter insertFilter;
        @Nullable private BasicItemFilter extractFilter;

        private final BlockPos pipePos;
        private final BlockPos connectionPos;
        private BlockCapabilityCache<ResourceHandler<ItemResource>, Direction> itemHandler;

        public ItemConnection(ServerLevel level, BlockPos connection, Direction direction, ItemConnectionMode mode,BlockPos pipePos){
            this.pipePos = pipePos;
            this.connectionPos = connection;
            this.itemHandler = BlockCapabilityCache.create(Capabilities.Item.BLOCK, level, connection, direction);
            this.mode = mode;
            this.direction = direction;
            this.sleepTicks = 0;
        }

        public BlockPos getPos() {
            return connectionPos;
        }

        public Direction getDirection() {
            return direction;
        }

        @Nullable
        public ResourceHandler<ItemResource> getItemHandler() {
            return itemHandler.getCapability();
        }

    }
}
