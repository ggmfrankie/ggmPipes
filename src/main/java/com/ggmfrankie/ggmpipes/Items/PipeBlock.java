package com.ggmfrankie.ggmpipes.Items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import static net.minecraft.world.level.block.BaseFireBlock.getState;

public abstract class PipeBlock extends Block implements SimpleWaterloggedBlock, EntityBlock {

    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty HAS_DATA = BooleanProperty.create("has_data");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public PipeBlock(Properties properties) {
        super(properties.mapColor(MapColor.COLOR_GRAY).strength(0.5F).sound(SoundType.METAL).pushReaction(PushReaction.BLOCK));

        this.registerDefaultState(stateDefinition.any()
                .setValue(UP, false)
                .setValue(DOWN, false)
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(EAST, false)
                .setValue(WEST, false)
                .setValue(HAS_DATA, false)
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
            if ((mask & (1)) != 0)  shape = Shapes.or(shape, SHAPE_NORTH);
            if ((mask & (2)) != 0)  shape = Shapes.or(shape, SHAPE_SOUTH);
            if ((mask & (4)) != 0)  shape = Shapes.or(shape, SHAPE_EAST);
            if ((mask & (8)) != 0)  shape = Shapes.or(shape, SHAPE_WEST);
            if ((mask & (16)) != 0) shape = Shapes.or(shape, SHAPE_UP);
            if ((mask & (32)) != 0) shape = Shapes.or(shape, SHAPE_DOWN);

            shapes[mask] = shape.optimize();
        }
        return shapes;
    }

    private static int getMask(BlockState state){
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
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPES[getMask(state)];
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
                HAS_DATA,
                WATERLOGGED
        );
    }

    @Override
    @Nullable
    @NullMarked
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return getState(context.getLevel(), context.getClickedPos());
    }

    @Override
    @NullMarked
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return null;
    }
}
