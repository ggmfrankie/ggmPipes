package de.ggmfrankie.ggmpipes.registry;

import de.ggmfrankie.ggmpipes.items.item.ItemPipeItem;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static de.ggmfrankie.ggmpipes.ggmPipes.MODID;
import static de.ggmfrankie.ggmpipes.registry.ModBlocks.ITEM_PIPE_BLOCK;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);


    /*
    public static final DeferredItem<ItemPipeItem> ITEM_PIPE_BLOCK_ITEM = ITEMS.register(
            "item_pipe_item",
            registryName -> new ItemPipeItem(
                    ModBlocks.ITEM_PIPE_BLOCK.get(),
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, registryName))
            )
    );
    */

    public static final DeferredItem<BlockItem> ITEM_PIPE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(ITEM_PIPE_BLOCK);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("ggmpipes_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.ggmpipes")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> new ItemStack(ITEM_PIPE_BLOCK_ITEM.get()))
            .displayItems((parameters, output) -> {
                output.accept(ITEM_PIPE_BLOCK_ITEM.get());
            }).build());

    public static void register(IEventBus modEventBus){
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
