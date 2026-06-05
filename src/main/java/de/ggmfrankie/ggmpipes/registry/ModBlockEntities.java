package de.ggmfrankie.ggmpipes.registry;

import de.ggmfrankie.ggmpipes.items.tileentity.ItemPipeEntity;
import de.ggmfrankie.ggmpipes.items.tileentity.PipeEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static de.ggmfrankie.ggmpipes.ggmPipes.MODID;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);

    public static final Supplier<BlockEntityType<PipeEntity>> ITEM_PIPE_ENTITY =
            BLOCK_ENTITY_TYPES.register("pipe_block_entity",
                    () -> new BlockEntityType<>(
                            ItemPipeEntity::new,
                            false,
                            ModBlocks.ITEM_PIPE_BLOCK.get()
                    )
            );

    public static void register(IEventBus modEventBus){
        BLOCK_ENTITY_TYPES.register(modEventBus);
    }
}
