package de.ggmfrankie.ggmpipes.network;

import de.ggmfrankie.ggmpipes.items.tileentity.ItemPipeEntity;
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

public class ItemPipeNetwork extends PipeNetwork<ItemPipeEntity> {
    private List<ItemInputConnection> inputConnections;
    private List<ItemOutputConnection> outputConnections;

    public ItemPipeNetwork(){
        inputConnections = new ArrayList<>(16);
        outputConnections = new ArrayList<>(16);
    }

    public void update(){
        for (var extract : outputConnections) {
            for (var insert : inputConnections) {

            }
        }
    }

    @Override
    @NullMarked
    public void addAllNodes(ItemPipeEntity entity) {
        if (!(entity.getLevel() instanceof ServerLevel serverLevel)) {
            return;
        }
        this.removeAllNodes(entity);

        BlockPos pos = entity.getBlockPos();
        for (var dir : entity.getInputConnections()){
            inputConnections.add(
                    new ItemInputConnection(
                            serverLevel,
                            pos.relative(dir),
                            dir,
                            pos,
                            entity.getInsertFilters().get(dir).copy()
                    )
            );
        }

        for (var dir : entity.getOutputConnections()){
            outputConnections.add(
                    new ItemOutputConnection(
                            serverLevel,
                            pos.relative(dir),
                            dir,
                            pos,
                            entity.getExtractFilters().get(dir).copy()
                    )
            );
        }
    }

    @Override
    @NullMarked
    public void removeAllNodes(ItemPipeEntity entity) {
        if (entity.getLevel() == null || entity.getLevel().isClientSide()) {
            return;
        }
        BlockPos pos = entity.getBlockPos();
        inputConnections.removeIf(connection -> connection.getPipePos().equals(pos));
        outputConnections.removeIf(connection -> connection.getPipePos().equals(pos));
    }

    public static abstract class ItemConnection {
        @Nullable protected final BasicItemFilter filter;

        protected final Direction direction;
        protected final BlockPos pipePos;
        protected final BlockPos connectionPos;
        protected final BlockCapabilityCache<ResourceHandler<ItemResource>, Direction> itemHandler;

        protected ItemConnection(ServerLevel level, BlockPos connection, Direction direction, BlockPos pipePos, BasicItemFilter filter) {
            this.pipePos = pipePos;
            this.connectionPos = connection;
            this.itemHandler = BlockCapabilityCache.create(Capabilities.Item.BLOCK, level, connection, direction);
            this.direction = direction;
            this.filter = filter;
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
        public BasicItemFilter getFilter() {
            return filter;
        }

        @Nullable
        public ResourceHandler<ItemResource> getItemHandler() {
            return itemHandler.getCapability();
        }
    }

    public static class ItemInputConnection extends ItemConnection {
        public ItemInputConnection(ServerLevel level, BlockPos connection, Direction direction, BlockPos pipePos, BasicItemFilter filter){
            super(level, connection, direction, pipePos, filter);

        }
    }

    public static class ItemOutputConnection extends ItemConnection {
        public int sleepTicks;
        public int extractionLimit;

        public ItemOutputConnection(ServerLevel level, BlockPos connection, Direction direction, BlockPos pipePos, BasicItemFilter filter){
            super(level, connection, direction, pipePos, filter);
            this.sleepTicks = 0;
            this.extractionLimit = 64;
        }
    }
}
