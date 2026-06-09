package de.ggmfrankie.ggmpipes.items.block;

import com.mojang.serialization.MapCodec;
import de.ggmfrankie.ggmpipes.items.tileentity.ItemPipeEntity;
import de.ggmfrankie.ggmpipes.items.tileentity.PipeEntity;
import de.ggmfrankie.ggmpipes.utils.CapabilityHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

public class ItemPipeBlock extends PipeBlock {

    public static final MapCodec<ItemPipeBlock> CODEC = simpleCodec(ItemPipeBlock::new);

    public ItemPipeBlock(Properties properties) {
        super(properties);
    }

    @Override
    @NullMarked
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return blockState.getValue(HAS_MACHINE_CONNECTION) ? new ItemPipeEntity(blockPos, blockState) : null;
    }

    @Override
    protected boolean canConnect(Level level, BlockPos pos, Direction dir){
        BlockState state = level.getBlockState(pos);

        return state.getBlock() instanceof PipeBlock || CapabilityHelper.hasItemCapability(level, pos, dir.getOpposite());
    }

    @Override
    protected boolean hasMachineConnection(Level level, BlockPos pos) {
        return 0 != (CapabilityHelper.getMachineConnections(level, pos, CapabilityHelper::hasItemCapability));
    }

    @Override
    @NullMarked
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(level.isClientSide()) return InteractionResult.SUCCESS;

        BlockEntity entity = level.getBlockEntity(pos);
        if(entity instanceof PipeEntity pipeEntity){
            player.openMenu( new SimpleMenuProvider(pipeEntity, Component.literal("Item Pipe")), pos);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    @NullMarked
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
