package de.ggmfrankie.ggmpipes.items.block;

import de.ggmfrankie.ggmpipes.items.tileentity.PipeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NullMarked;


public abstract class PipeEntityBlock extends PipeBlock implements EntityBlock {

    public PipeEntityBlock(Properties properties) {
        super(properties);
    }

    private Direction getClickedArm(BlockState state, BlockHitResult hitResult) {
        Vec3 hitLoc = hitResult.getLocation();

        double x = (hitLoc.x - Math.floor(hitLoc.x)) * 16;
        double y = (hitLoc.y - Math.floor(hitLoc.y)) * 16;
        double z = (hitLoc.z - Math.floor(hitLoc.z)) * 16;

        boolean inCenterX = x >= 5.5 && x <= 10.5;
        boolean inCenterY = y >= 5.5 && y <= 10.5;
        boolean inCenterZ = z >= 5.5 && z <= 10.5;

        if (inCenterX && inCenterY && inCenterZ) {
            return null;
        }

        if (z < 5.5 && inCenterX && inCenterY && state.getValue(NORTH))  return Direction.NORTH;
        if (z > 10.5 && inCenterX && inCenterY && state.getValue(SOUTH)) return Direction.SOUTH;
        if (x < 5.5 && inCenterY && inCenterZ && state.getValue(WEST))   return Direction.WEST;
        if (x > 10.5 && inCenterY && inCenterZ && state.getValue(EAST))  return Direction.EAST;
        if (y < 5.5 && inCenterX && inCenterZ && state.getValue(DOWN))   return Direction.DOWN;
        if (y > 10.5 && inCenterX && inCenterZ && state.getValue(UP))    return Direction.UP;

        return null;
    }

    @Override
    @NullMarked
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        if (level.getBlockEntity(pos) instanceof PipeEntity pipeEntity) {
            Direction clickedSide = getClickedArm(state, hitResult);
            if (clickedSide == null) return InteractionResult.PASS;

            pipeEntity.setClickedDirection(clickedSide);
            ((ServerPlayer) player).openMenu(pipeEntity,
            buf -> {
                    buf.writeBlockPos(pos);
                    buf.writeByte(clickedSide.get3DDataValue());
                }
            );
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    @NullMarked
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    @NullMarked
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, ItemStack toolStack, boolean willHarvest, FluidState fluid) {
        return super.onDestroyedByPlayer(state, level, pos, player, toolStack, willHarvest, fluid);
    }

    @Override
    @NullMarked
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return getPlacementState(context.getLevel(), context.getClickedPos());
    }

    @Override
    @NullMarked
    public abstract BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState);
}
