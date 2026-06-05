package de.ggmfrankie.ggmpipes.items.render;

import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

import java.util.concurrent.atomic.AtomicReference;

import static de.ggmfrankie.ggmpipes.ggmPipes.MODID;
import static net.neoforged.neoforgespi.ILaunchContext.LOGGER;

@EventBusSubscriber(value = Dist.CLIENT)
public class ItemPipeModels {

    public enum PipeModel {
        CENTER("block/pipe_center"),
        NORTH_ARM("block/pipe_north"),
        SOUTH_ARM("block/pipe_south"),
        EAST_ARM("block/pipe_east"),
        WEST_ARM("block/pipe_west"),
        UP_ARM("block/pipe_up"),
        DOWN_ARM("block/pipe_down");

        private final Identifier location;
        private final StandaloneModelKey<QuadCollection> key;
        private final AtomicReference<QuadCollection> model;

        PipeModel(String path) {
            this.location = Identifier.fromNamespaceAndPath(MODID, path);
            this.key = new StandaloneModelKey<>(location::toString);
            this.model = new AtomicReference<>();
        }

        public Identifier getLocation() {
            return location;
        }

        public StandaloneModelKey<QuadCollection> getModelKey() {
            return key;
        }

        public AtomicReference<QuadCollection> getModel() {
            return model;
        }
    }

    @SubscribeEvent
    public static void onModelRegister(ModelEvent.RegisterStandalone event){
        for (PipeModel model : PipeModel.values()){
            event.register(
                    model.getModelKey(),
                    new SimpleUnbakedStandaloneModel<>(
                            model.getLocation(),
                            (resModel, baker, name) ->{
                                var resolvedModel = baker.getModel(model.getLocation());
                                var textureSlots = resolvedModel.getTopTextureSlots();
                                return resolvedModel.bakeTopGeometry(textureSlots, baker, BlockModelRotation.IDENTITY);
                            }
                    )
            );
        }
    }

    @SubscribeEvent
    public static void onModelBakeComplete(ModelEvent.BakingCompleted event){
        for (var model : PipeModel.values()) {
            QuadCollection quads = event.getBakingResult().standaloneModels().get(model.getModelKey());

            if (quads == null) {
                LOGGER.warn("Failed to bake model {}", model.getLocation());
            } else {
                LOGGER.info("Loaded model {}", model.getLocation());
            }

            model.getModel().set(quads);
        }
    }
}
