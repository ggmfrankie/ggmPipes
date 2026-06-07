package de.ggmfrankie.ggmpipes.items.render;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.geometry.QuadCollection;

import java.util.EnumMap;
import java.util.concurrent.atomic.AtomicReference;

public class ItemPipeRenderer extends PipeRenderer {

    public ItemPipeRenderer(BlockEntityRendererProvider.Context renderer) {

        super(renderer, ItemPipeModels.PipeModel.CONNECTOR.getModel());
    }
}
