package de.ggmfrankie.ggmpipes.items.block;

import com.mojang.serialization.MapCodec;
import de.ggmfrankie.ggmpipes.items.tileentity.PipeEntity;
import de.ggmfrankie.ggmpipes.utils.DirectionMask;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public abstract class PipeBlock extends Block implements SimpleWaterloggedBlock, EntityBlock {



    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty HAS_MACHINE_CONNECTION = BooleanProperty.create("has_machine_connection");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public PipeBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(stateDefinition.any()
                .setValue(UP, false)
                .setValue(DOWN, false)
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(EAST, false)
                .setValue(WEST, false)
                .setValue(HAS_MACHINE_CONNECTION, false)
                .setValue(WATERLOGGED, false)
        );
    }

    // 0, 0, 0 bottom-north-west -> 16, 16, 16 top-south-east
    public static final VoxelShape SHAPE_CENTER = Block.box(5.5, 5.5, 5.5, 10.5, 10.5, 10.5);
    public static final VoxelShape SHAPE_NORTH = Block.box(5.5, 5.5, 0, 10.5, 10.5, 5.5);
    public static final VoxelShape SHAPE_SOUTH = Block.box(5.5, 5.5, 10.5, 10.5, 10.5, 16);
    public static final VoxelShape SHAPE_WEST = Block.box(0, 5.5, 5.5, 5.5, 10.5, 10.5);
    public static final VoxelShape SHAPE_EAST = Block.box(10.5, 5.5, 5.5, 16, 10.5, 10.5);
    public static final VoxelShape SHAPE_DOWN = Block.box(5.5, 0, 5.5, 10.5, 5.5, 10.5);
    public static final VoxelShape SHAPE_UP = Block.box(5.5, 10.5, 5.5, 10.5, 16, 10.5);

    private static final VoxelShape[] SHAPES = makeShapes();

    private static VoxelShape[] makeShapes(){
        VoxelShape[] shapes = new VoxelShape[64];
        for (int mask = 0; mask < 64; mask++){
            VoxelShape shape = SHAPE_CENTER;
            if ((mask & DirectionMask.NORTH) != 0) shape = Shapes.or(shape, SHAPE_NORTH);
            if ((mask & DirectionMask.SOUTH) != 0) shape = Shapes.or(shape, SHAPE_SOUTH);
            if ((mask & DirectionMask.EAST) != 0)  shape = Shapes.or(shape, SHAPE_EAST);
            if ((mask & DirectionMask.WEST) != 0)  shape = Shapes.or(shape, SHAPE_WEST);
            if ((mask & DirectionMask.UP) != 0)    shape = Shapes.or(shape, SHAPE_UP);
            if ((mask & DirectionMask.DOWN) != 0)  shape = Shapes.or(shape, SHAPE_DOWN);

            shapes[mask] = shape.optimize();
        }
        return shapes;
    }

    public static int calculateMask(BlockState state){
        int mask = 0;
        if (state.getValue(NORTH)) mask |= 1;
        if (state.getValue(SOUTH)) mask |= 2;
        if (state.getValue(EAST))  mask |= 4;
        if (state.getValue(WEST))  mask |= 8;
        if (state.getValue(UP))    mask |= 16;
        if (state.getValue(DOWN))  mask |= 32;

        return mask;
    }

    @Override
    @NullMarked
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    @NullMarked
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPES[calculateMask(state)];
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(
                DOWN,
                UP,
                NORTH,
                SOUTH,
                WEST,
                EAST,
                HAS_MACHINE_CONNECTION,
                WATERLOGGED
        );
    }

    protected abstract boolean canConnect(Level level, BlockPos pos, Direction dir);
    protected abstract boolean hasMachineConnection(Level level, BlockPos pos);

    private BlockState getState(Level level, BlockPos pos){
        return this.defaultBlockState()
                .setValue(NORTH, canConnect(level, pos.north(), Direction.NORTH))
                .setValue(SOUTH, canConnect(level, pos.south(), Direction.SOUTH))
                .setValue(EAST,  canConnect(level, pos.east(),  Direction.EAST))
                .setValue(WEST,  canConnect(level, pos.west(),  Direction.WEST))
                .setValue(UP,    canConnect(level, pos.above(), Direction.UP))
                .setValue(DOWN,  canConnect(level, pos.below(), Direction.DOWN))
                .setValue(HAS_MACHINE_CONNECTION, hasMachineConnection(level, pos));
    }

    public static List<Direction> getPipeConnections(BlockState state){
        List<Direction> directions = new ArrayList<>(6);

        if (state.getValue(NORTH)) directions.add(Direction.NORTH);
        if (state.getValue(SOUTH)) directions.add(Direction.SOUTH);
        if (state.getValue(EAST))  directions.add(Direction.EAST);
        if (state.getValue(WEST))  directions.add(Direction.WEST);
        if (state.getValue(UP))    directions.add(Direction.UP);
        if (state.getValue(DOWN))  directions.add(Direction.DOWN);

        return directions;
    }

    @Override
    @NullMarked
    public void neighborChanged(
            BlockState state,
            Level level,
            BlockPos pos,
            Block neighborBlock,
            @Nullable Orientation orientation,
            boolean movedByPiston
    ) {
        if (level.isClientSide()) return;

        BlockState newState = getState(level, pos);

        var entity = level.getBlockEntity(pos);
        if (entity instanceof PipeEntity pipeEntity) {
            newState = pipeEntity.onNeighborChanged(newState);
        }
        level.setBlock(pos, newState, Block.UPDATE_CLIENTS);
    }

    @Override
    @NullMarked
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, ItemStack toolStack, boolean willHarvest, FluidState fluid) {
        return super.onDestroyedByPlayer(state, level, pos, player, toolStack, willHarvest, fluid);
    }

    @Override
    @NullMarked
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return getState(context.getLevel(), context.getClickedPos());
    }

    @Override
    @NullMarked
    public abstract @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState);
}
