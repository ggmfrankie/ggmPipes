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
    private List<ItemInputConnection> inputConnections;
    private List<ItemOutputConnection> outputConnections;

    public ItemPipeNetwork(){
        inputConnections = new ArrayList<>(16);
        outputConnections = new ArrayList<>(16);
    }

    public void update(){
        for (var connection : outputConnections){

        }
    }

    @Override
    @NullMarked
    public void addAllNodes(PipeEntity entity) {
        if (!(entity.getLevel() instanceof ServerLevel serverLevel)) {
            return;
        }

        BlockPos pos = entity.getBlockPos();
        for (var dir : entity.getInputConnections()){
            inputConnections.add(
                    new ItemInputConnection(
                            serverLevel,
                            pos.relative(dir),
                            dir,
                            pos
                    )
            );
        }

        for (var dir : entity.getOutputConnections()){
            outputConnections.add(
                    new ItemOutputConnection(
                            serverLevel,
                            pos.relative(dir),
                            dir,
                            pos
                    )
            );
        }
    }

    @Override
    @NullMarked
    public void removeAllNodes(PipeEntity entity) {
        if (entity.getLevel() == null || entity.getLevel().isClientSide()) {
            return;
        }
        BlockPos pos = entity.getBlockPos();
        inputConnections.removeIf(connection -> connection.getPipePos() == pos);
        outputConnections.removeIf(connection -> connection.getPipePos() == pos);
    }

    public static abstract class ItemConnection {
        @Nullable protected BasicItemFilter filter;

        protected final Direction direction;
        protected final BlockPos pipePos;
        protected final BlockPos connectionPos;
        protected final BlockCapabilityCache<ResourceHandler<ItemResource>, Direction> itemHandler;

        protected ItemConnection(ServerLevel level, BlockPos connection, Direction direction, BlockPos pipePos) {
            this.pipePos = pipePos;
            this.connectionPos = connection;
            this.itemHandler = BlockCapabilityCache.create(Capabilities.Item.BLOCK, level, connection, direction);
            this.direction = direction;
        }

        public BlockPos getConnectionPos() {
            return connectionPos;
        }

        public BlockPos getPipePos() {
            return pipePos;
        }

        public Direction getDirection() {
            return direction;
        }

        @Nullable
        public ResourceHandler<ItemResource> getItemHandler() {
            return itemHandler.getCapability();
        }
    }

    public static class ItemInputConnection extends ItemConnection{
        public ItemInputConnection(ServerLevel level, BlockPos connection, Direction direction, BlockPos pipePos){
            super(level, connection, direction, pipePos);

        }
    }

    public static class ItemOutputConnection extends ItemConnection{
        public int sleepTicks;
        public int extractionLimit;

        public ItemOutputConnection(ServerLevel level, BlockPos connection, Direction direction, BlockPos pipePos){
            super(level, connection, direction, pipePos);
            this.sleepTicks = 0;
            this.extractionLimit = 64;
        }
    }
}
