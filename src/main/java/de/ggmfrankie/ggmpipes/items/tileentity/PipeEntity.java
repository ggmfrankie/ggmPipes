package de.ggmfrankie.ggmpipes.items.tileentity;

import de.ggmfrankie.ggmpipes.items.block.PipeBlock;
import de.ggmfrankie.ggmpipes.utils.DirectionMask;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NullMarked;

import java.util.*;


public abstract class PipeEntity extends BlockEntity {

    private int connectionMask;
    private int disabledMask;

    protected UUID memberNetwork;

    public PipeEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        connectionMask = 0;
        disabledMask = 0;
        memberNetwork = null;
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (level != null) {
            this.connectionMask = calculateConnectionMask(level, worldPosition);
        }
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

    protected UUID getOrCreateNetwork(Level level, BlockPos start){
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()){
            BlockPos curr = queue.remove();
            BlockState state = level.getBlockState(curr);
            List<Direction> pipeConnections = PipeBlock.getPipeConnections(state);

            for (var dir : pipeConnections) {
                BlockPos neighbor = curr.relative(dir);

                if (visited.contains(neighbor)) continue;

                if (level.getBlockEntity(neighbor) instanceof PipeEntity entity) {
                    return entity.getMemberNetwork();
                }

                visited.add(neighbor);

                if (level.getBlockState(neighbor).getBlock() instanceof PipeBlock) queue.add(neighbor);
            }
        }
        return null;
    }

    protected abstract int calculateConnectionMask(Level level, BlockPos pos);

    @Override
    @NullMarked
    protected void loadAdditional(ValueInput valueInput){
        super.loadAdditional(valueInput);

        disabledMask = valueInput.getIntOr("disabledMask", 0);

        //memberNetwork = valueInput.read("memberNetwork", UUIDUtil.CODEC).orElse(null);
    }

    @Override
    @NullMarked
    protected void saveAdditional(ValueOutput valueOutput){
        super.saveAdditional(valueOutput);

        valueOutput.putInt("disabledMask", disabledMask);
        valueOutput.putInt("connectionMask", connectionMask);
        //if (memberNetwork != null) valueOutput.store("memberNetwork", UUIDUtil.CODEC, memberNetwork);
    }

    @Override
    @NullMarked
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    @NullMarked
    public void handleUpdateTag(ValueInput input) {
        super.handleUpdateTag(input);
        connectionMask = input.getIntOr("connectionMask", 0);
        disabledMask = input.getIntOr("disabledMask", 0);
        System.out.println("Hello");
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onNeighborChanged() {
        this.connectionMask = calculateConnectionMask(level, worldPosition);
        System.out.println("connectionMask: " + connectionMask);
        this.setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(
                    worldPosition,
                    getBlockState(),
                    getBlockState(),
                    Block.UPDATE_ALL
            );
        }
    }

    public UUID getMemberNetwork(){
        return this.memberNetwork;
    }

    public int getMask(){
        return this.connectionMask = calculateConnectionMask(level, worldPosition);
    }

    public List<Direction> getPipeConnections() {
        return DirectionMask.getDirectionsFromMask(connectionMask);
    }
}
