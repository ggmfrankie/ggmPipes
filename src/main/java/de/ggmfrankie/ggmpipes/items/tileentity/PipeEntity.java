package de.ggmfrankie.ggmpipes.items.tileentity;

import de.ggmfrankie.ggmpipes.items.block.PipeEntityBlock;
import de.ggmfrankie.ggmpipes.utils.DirectionUtils;
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
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.*;


public abstract class PipeEntity extends BlockEntity implements MenuProvider {

    protected int disabledMask;

    private int extractMask;
    private int insertMask;

    private Direction clickedDirection;

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
            case NORTH -> disabledMask |= DirectionUtils.NORTH;
            case SOUTH -> disabledMask |= DirectionUtils.SOUTH;

            case EAST  -> disabledMask |= DirectionUtils.EAST;
            case WEST  -> disabledMask |= DirectionUtils.WEST;

            case UP    -> disabledMask |= DirectionUtils.UP;
            case DOWN  -> disabledMask |= DirectionUtils.DOWN;
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
            List<Direction> pipeConnections = PipeEntityBlock.getPipeConnections(state);

            for (var dir : pipeConnections) {
                BlockPos neighbor = curr.relative(dir);

                if (visited.contains(neighbor)) continue;

                if (level.getBlockEntity(neighbor) instanceof PipeEntity entity) {
                    return entity.getMemberNetwork();
                }

                visited.add(neighbor);

                if (level.getBlockState(neighbor).getBlock() instanceof PipeEntityBlock) queue.add(neighbor);
            }
        }
        return null;
    }

    private void setInsertMask(int mask){
        this.insertMask = mask;
    }

    private void setExtractMask(int mask){
        this.extractMask = mask;
    }

    @Override
    public abstract void onChunkUnloaded();

    protected abstract int calculateConnectionMask(Level level, BlockPos pos);

    public abstract void updateConnectionsInNetwork();

    @Override
    @NullMarked
    protected void loadAdditional(ValueInput valueInput){
        super.loadAdditional(valueInput);

        setExtractMask(valueInput.getIntOr("extractMask", 0));
        setInsertMask(valueInput.getIntOr("insertMask", 0));
        disabledMask = valueInput.getIntOr("disabledMask", 0);
    }

    @Override
    @NullMarked
    protected void saveAdditional(ValueOutput valueOutput){
        super.saveAdditional(valueOutput);

        valueOutput.putInt("extractMask", extractMask);
        valueOutput.putInt("insertMask", insertMask);
        valueOutput.putInt("disabledMask", disabledMask);
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
        setExtractMask(input.getIntOr("extractMask", 0));
        setInsertMask(input.getIntOr("insertMask", 0));
        disabledMask = input.getIntOr("disabledMask", 0);
    }

    @Override
    public Packet<@NotNull ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onNeighborChanged() {
        recalculateConnections();
        this.setChanged();
    }

    private void recalculateConnections(){
        int newMask = calculateConnectionMask(level, worldPosition) & ~disabledMask;
        extractMask &= newMask;
        //this.extractMask &= newMask;
        insertMask &= newMask;
        insertMask |= newMask;

    }

    private boolean checkIfConnectionRemoved(Direction dir){
        return !isInserting(dir) && !isExtracting(dir);
    }

    private void handleConnectionRemovalForBlock(Direction dir){

    }

    public void setInsert(Direction dir, boolean set){
        int mask = DirectionUtils.getMaskFromDirection(dir);
        if (set){
            insertMask |= mask;
            disabledMask &= mask;
        } else {
            insertMask &= ~mask;
            disabledMask |= mask;
        }
        updateConnectionsInNetwork();

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

    public void setExtract(Direction dir, boolean set){
        int mask = DirectionUtils.getMaskFromDirection(dir);
        if (set){
            setExtractMask(extractMask | mask);
            //this.extractMask |= mask;
        } else {
            setExtractMask(extractMask & ~mask);
            //this.extractMask &= ~mask;
        }
        updateConnectionsInNetwork();
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

    @Override
    @NullMarked
    public Component getDisplayName() {
        return Component.literal("Pipe");
    }

    @Override
    @NullMarked
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return null;
    }

    public UUID getMemberNetwork(){
        return this.memberNetwork;
    }

    public int getExtractMask(){
        return this.extractMask;
    }

    public int getInsertMask(){
        return this.insertMask;
    }

    public List<Direction> getExtractConnections() {
        return DirectionUtils.getDirectionsFromMask(this.extractMask);
    }

    public List<Direction> getInsertConnections() {
        return DirectionUtils.getDirectionsFromMask(this.insertMask);
    }

    public void setClickedDirection(Direction dir) {
        this.clickedDirection = dir;
    }

    public Direction getClickedDirection() {
        return this.clickedDirection;
    }

    public boolean isInserting(Direction dir){
        return (DirectionUtils.getMaskFromDirection(dir) & insertMask) != 0;
    }

    public boolean isExtracting(Direction dir){
        return (DirectionUtils.getMaskFromDirection(dir) & extractMask) != 0;
    }
}
