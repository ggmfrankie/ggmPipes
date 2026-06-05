package de.ggmfrankie.ggmpipes.items.render;

import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.ggmfrankie.ggmpipes.items.tileentity.PipeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import de.ggmfrankie.ggmpipes.registry.ModBlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static de.ggmfrankie.ggmpipes.items.render.ItemPipeModels.PipeModel;

@EventBusSubscriber(value = Dist.CLIENT)
public abstract class PipeRenderer implements BlockEntityRenderer<PipeEntity, PipeRenderState> {

    protected Minecraft minecraft;
    protected final EnumMap<ItemPipeModels.PipeModel, AtomicReference<QuadCollection>> cachedModels;
    protected BlockEntityRendererProvider.Context renderer;

    public PipeRenderer(BlockEntityRendererProvider.Context renderer, EnumMap<ItemPipeModels.PipeModel, AtomicReference<QuadCollection>> cachedModels) {
        this.renderer = renderer;
        minecraft = Minecraft.getInstance();
        this.cachedModels = cachedModels;
    }

    @Override
    @NullMarked
    public PipeRenderState createRenderState() {
        return new PipeRenderState();
    }

    @Override
    public void extractRenderState(
            @NonNull PipeEntity entity,
            @NonNull PipeRenderState state,
            float partialTicks,
            @NonNull Vec3 pos,
            ModelFeatureRenderer.CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(entity, state, partialTicks, pos, crumblingOverlay);
        state.connectionMask = entity.getMask();
    }

    @Override
    @NullMarked
    public void submit(PipeRenderState pipeRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        QuadInstance instance = new QuadInstance();
        instance.setColor(0xFFFFFFFF);
        instance.setOverlayCoords(OverlayTexture.NO_OVERLAY);

        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.solidMovingBlock(), (pose, vertexConsumer) -> {
            addQuads(instance, vertexConsumer, pose, cachedModels.get(PipeModel.CENTER).get(), pipeRenderState.lightCoords);

            if ((pipeRenderState.connectionMask & 1) != 0) {
                addQuads(instance, vertexConsumer, pose, cachedModels.get(PipeModel.NORTH_ARM).get(), pipeRenderState.lightCoords);
            }

            if ((pipeRenderState.connectionMask & 2) != 0) {
                addQuads(instance, vertexConsumer, pose, cachedModels.get(PipeModel.SOUTH_ARM).get(), pipeRenderState.lightCoords);
            }

            if ((pipeRenderState.connectionMask & 4) != 0) {
                addQuads(instance, vertexConsumer, pose, cachedModels.get(PipeModel.EAST_ARM).get(), pipeRenderState.lightCoords);
            }

            if ((pipeRenderState.connectionMask & 8) != 0) {
                addQuads(instance, vertexConsumer, pose, cachedModels.get(PipeModel.WEST_ARM).get(), pipeRenderState.lightCoords);
            }

            if ((pipeRenderState.connectionMask & 16) != 0) {
                addQuads(instance, vertexConsumer, pose, cachedModels.get(PipeModel.UP_ARM).get(), pipeRenderState.lightCoords);
            }

            if ((pipeRenderState.connectionMask & 32) != 0) {
                addQuads(instance, vertexConsumer, pose, cachedModels.get(PipeModel.DOWN_ARM).get(), pipeRenderState.lightCoords);
            }
        });
    }

    private void addQuads(QuadInstance instance, VertexConsumer vc, PoseStack.Pose pose, QuadCollection model, int lightCoords) {
        if (model == null) return;

        instance.setLightCoords(lightCoords);

        List<BakedQuad> quads = model.getQuads(null);
        for (BakedQuad quad : quads) {
            vc.putBakedQuad(pose, quad, instance);
        }
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
                ModBlockEntities.ITEM_PIPE_ENTITY.get(),
                ItemPipeRenderer::new
        );
    }
}
