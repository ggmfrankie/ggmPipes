package de.ggmfrankie.ggmpipes.registry;

import de.ggmfrankie.ggmpipes.items.blockentity.ItemPipeBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static de.ggmfrankie.ggmpipes.ggmPipes.MODID;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    public static final DeferredBlock<ItemPipeBlock> ITEM_PIPE_BLOCK = BLOCKS.register(
            "item_pipe_block",
            registryName -> new ItemPipeBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, registryName))
                    .destroyTime(2.0f)
                    .explosionResistance(10.0f)
                    .sound(SoundType.METAL)
            ));


    public static void register(IEventBus modEventBus){
        BLOCKS.register(modEventBus);
    }

}
