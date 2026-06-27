package de.ggmfrankie.ggmpipes.net;

import de.ggmfrankie.ggmpipes.items.tileentity.ItemPipeEntity;
import de.ggmfrankie.ggmpipes.items.tileentity.filter.BasicItemFilter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.NullMarked;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemPipeNetwork extends PipeNetwork<ItemPipeEntity> {
    private List<ItemExtractConnection> extractConnections;
    private List<ItemInsertConnection> insertConnections;

    private int cooldown;
    private int maxCooldown;

    public ItemPipeNetwork(){
        extractConnections = new ArrayList<>(16);
        insertConnections = new ArrayList<>(16);

        cooldown = maxCooldown = 20;
    }

    public void update() {
        cooldown--;
        if (cooldown > 0) return;
        cooldown = maxCooldown;

        for (var extract : insertConnections) {
            var extractHandler = extract.getItemHandler();
            if (extractHandler == null) continue;
            for (var insert : extractConnections) {
                List<ItemConnection.Slot> availableItemResource = extract.getAvailableItemResources();
                if (availableItemResource.isEmpty()) continue;

                var insertHandler = insert.getItemHandler();
                if (insertHandler == null) continue;

                for (var resourceAndSlot : availableItemResource){
                    try (var tx = Transaction.openRoot()) {
                        ItemResource resource = resourceAndSlot.resource();
                        int slot = resourceAndSlot.index();

                        int maxAmount = insertHandler.getCapacityAsInt(slot, resource);
                        int amount = extractHandler.extract(resource, maxAmount, tx);

                        if (amount == 0){
                            continue;
                        }

                        insertHandler.insert(resource, amount, tx);
                        tx.commit();
                    }
                }
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
            extractConnections.add(
                    new ItemExtractConnection(
                            serverLevel,
                            pos.relative(dir),
                            dir,
                            pos,
                            entity.getInsertFilters().get(dir).copy()
                    )
            );
        }

        for (var dir : entity.getOutputConnections()){
            insertConnections.add(
                    new ItemInsertConnection(
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
        extractConnections.removeIf(connection -> connection.getPipePos().equals(pos));
        insertConnections.removeIf(connection -> connection.getPipePos().equals(pos));
    }

    public static abstract class ItemConnection {
        public record Slot(int index, ItemResource resource){}
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

        List<Slot> getAvailableItemResources() {
            List<Slot> resources = new ArrayList<>();
            var handler = itemHandler.getCapability();
            assert handler != null;

            for (int i = 0; i < handler.size(); ++i){
                var itemResource = handler.getResource(i);
                if (!itemResource.equals(ItemResource.EMPTY)){
                    resources.add(new Slot(i, itemResource));
                }
            }
            return resources;
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

    public static class ItemExtractConnection extends ItemConnection {
        public ItemExtractConnection(ServerLevel level, BlockPos connection, Direction direction, BlockPos pipePos, BasicItemFilter filter){
            super(level, connection, direction, pipePos, filter);

        }
    }

    public static class ItemInsertConnection extends ItemConnection {
        public int sleepTicks;
        public int extractionLimit;

        public ItemInsertConnection(ServerLevel level, BlockPos connection, Direction direction, BlockPos pipePos, BasicItemFilter filter){
            super(level, connection, direction, pipePos, filter);
            this.sleepTicks = 0;
            this.extractionLimit = 64;
        }
    }
}
