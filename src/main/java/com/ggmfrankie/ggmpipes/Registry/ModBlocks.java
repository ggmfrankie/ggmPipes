package com.ggmfrankie.ggmpipes.Registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.ggmfrankie.ggmpipes.ggmPipes.MODID;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items BLOCK_ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredBlock<Block> MY_BETTER_BLOCK = BLOCKS.register(
            "my_better_block",
            registryName -> new Block(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, registryName))
                    .destroyTime(2.0f)
                    .explosionResistance(10.0f)
                    .sound(SoundType.GRAVEL)
                    .lightLevel(_ -> 7)
            ));
    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", p -> p.mapColor(MapColor.STONE));

    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = BLOCK_ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);

    public static void register(IEventBus modEventBus){
        BLOCKS.register(modEventBus);
    }

}
