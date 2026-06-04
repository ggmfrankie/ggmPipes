package com.ggmfrankie.ggmpipes.Items.Render;

import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.util.context.ContextMap;
import net.neoforged.neoforge.client.model.ExtendedUnbakedGeometry;
import org.jspecify.annotations.NullMarked;

public class PipeUnbakedGeometry implements ExtendedUnbakedGeometry {
    private final PipeDefinition definition;

    public PipeUnbakedGeometry(PipeDefinition definition) {
        this.definition = definition;
    }

    @Override
    @NullMarked
    public QuadCollection bake(TextureSlots textureSlots, ModelBaker baker, ModelState state, ModelDebugName debugName, ContextMap additionalProperties) {
        // The builder to create the collection
        var builder = new QuadCollection.Builder();
        PipeDefinition def = definition;
        // Build the quads for baking
        builder.addUnculledFace(

        ); // or addCulledFace(Direction, BakedQuad)
        // Create the quad collection
        return builder.build();
    }

    private QuadCollection createCenter(TextureSlots textureSlots){

    }
}
