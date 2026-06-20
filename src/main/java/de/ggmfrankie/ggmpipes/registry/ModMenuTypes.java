package de.ggmfrankie.ggmpipes.registry;

import de.ggmfrankie.ggmpipes.ggmPipes;
import de.ggmfrankie.ggmpipes.gui.ItemPipeGUIMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, ggmPipes.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<ItemPipeGUIMenu>> ITEM_PIPE_MENU =
            MENUS.register("item_pipe_menu", () -> IMenuTypeExtension.create(ItemPipeGUIMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
