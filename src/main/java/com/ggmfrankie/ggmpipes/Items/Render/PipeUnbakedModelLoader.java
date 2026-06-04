package com.ggmfrankie.ggmpipes.Items.Render;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.neoforge.client.model.StandardModelParameters;
import net.neoforged.neoforge.client.model.UnbakedModelLoader;
import org.jspecify.annotations.NullMarked;

import static net.neoforged.neoforge.common.NeoForgeMod.MOD_ID;

public class PipeUnbakedModelLoader implements UnbakedModelLoader<PipeUnbakedModel>, ResourceManagerReloadListener {

    public static final PipeUnbakedModelLoader INSTANCE = new PipeUnbakedModelLoader();
    public static final Identifier ID = Identifier.fromNamespaceAndPath(MOD_ID, "pipe_model_loader");

    private PipeUnbakedModelLoader() {}

    @Override
    @NullMarked
    public PipeUnbakedModel read(JsonObject obj, JsonDeserializationContext context) throws JsonParseException {
        // Use the given JsonObject and, if needed, the JsonDeserializationContext to get properties from the model JSON.
        // The MyUnbakedModel constructor may have constructor parameters (see below).

        // Read the data used to create the quads
        PipeUnbakedGeometry geometry;

        // For the basic parameters provided by vanilla and NeoForge, you can use the StandardModelParameters
        StandardModelParameters params = StandardModelParameters.parse(obj, context);

        return new PipeUnbakedGeometry(params, geometry);
    }

    @Override
    @NullMarked
    public void onResourceManagerReload(ResourceManager resourceManager) {
        // Handle any cache clearing logic
    }
}
