package de.ggmfrankie.ggmpipes.items.tileentity;

import de.ggmfrankie.ggmpipes.items.block.PipeBlock;
import de.ggmfrankie.ggmpipes.utils.DirectionMask;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.*;


public abstract class PipeEntity extends BlockEntity implements MenuProvider {

    protected int disabledMask;

    private int inputMask;
    private int outputMask;

    protected UUID memberNetwork;

    public PipeEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        disabledMask = 0;
        memberNetwork = null;
    }

    @Override
    public void onLoad() {
        super.onLoad();

        recalculateConnections();
        this.setChanged();

        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(
                    worldPosition,
                    getBlockState(),
                    getBlockState(),
                    Block.UPDATE_CLIENTS
            );
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

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
    }

    protected abstract int calculateConnectionMask(Level level, BlockPos pos);

    @Override
    @NullMarked
    protected void loadAdditional(ValueInput valueInput){
        super.loadAdditional(valueInput);

        inputMask = valueInput.getIntOr("inputMask", 0);
        outputMask = valueInput.getIntOr("outputMask", 0);
        disabledMask = valueInput.getIntOr("disabledMask", 0);

        //memberNetwork = valueInput.read("memberNetwork", UUIDUtil.CODEC).orElse(null);
    }

    @Override
    @NullMarked
    protected void saveAdditional(ValueOutput valueOutput){
        super.saveAdditional(valueOutput);

        valueOutput.putInt("inputMask", inputMask);
        valueOutput.putInt("outputMask", outputMask);
        valueOutput.putInt("disabledMask", disabledMask);

        //if (memberNetwork != null) valueOutput.store("memberNetwork", UUIDUtil.CODEC, memberNetwork);
    }

    @Override
    @NullMarked
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag updateTag = super.getUpdateTag(provider);
        updateTag.merge(this.saveWithoutMetadata(provider));
        updateTag.merge(this.saveCustomOnly(provider));
        return updateTag;
    }

    @Override
    @NullMarked
    public void handleUpdateTag(ValueInput input) {
        super.handleUpdateTag(input);
        inputMask = input.getIntOr("inputMask", 0);
        outputMask = input.getIntOr("outputMask", 0);
        disabledMask = input.getIntOr("disabledMask", 0);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onNeighborChanged() {
        recalculateConnections();

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

    private void recalculateConnections(){
        int newMask = calculateConnectionMask(level, worldPosition) & ~disabledMask;
        this.inputMask &= newMask;
        this.outputMask &= newMask;
        this.inputMask |= newMask;
    }

    @Override
    @NullMarked
    public Component getDisplayName() {
        return null;
    }

    @Override
    @NullMarked
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return null;
    }

    public UUID getMemberNetwork(){
        return this.memberNetwork;
    }

    public int getInputMask(){
        return this.inputMask;
    }

    public int getOutputMask(){
        return this.outputMask;
    }

    public List<Direction> getInputConnections() {
        return DirectionMask.getDirectionsFromMask(this.inputMask);
    }

    public List<Direction> getOutputConnections() {
        return DirectionMask.getDirectionsFromMask(this.outputMask);
    }
}
