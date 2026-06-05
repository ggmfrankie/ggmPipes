package de.ggmfrankie.ggmpipes.items.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NullMarked;

import static net.neoforged.neoforgespi.ILaunchContext.LOGGER;

public class ItemPipeItem extends BlockItem {
    public ItemPipeItem(Block block, Properties properties) {
        super(block, properties);
    }


    @NullMarked
    @Override
    public InteractionResult place(BlockPlaceContext placeContext) {
        BlockState state = this.getPlacementState(placeContext);
        if (state == null) {
            return InteractionResult.FAIL;
        }

        boolean placed = this.placeBlock(placeContext, state);

        if (placed) {
            // Optional: Add custom logic here after successful placement
            // e.g., consume item, play extra sound, update neighbors
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }

}