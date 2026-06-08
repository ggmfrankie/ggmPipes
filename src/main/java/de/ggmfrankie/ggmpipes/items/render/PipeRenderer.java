package de.ggmfrankie.ggmpipes.items.render;

import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.math.Axis;
import de.ggmfrankie.ggmpipes.items.tileentity.PipeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import de.ggmfrankie.ggmpipes.registry.ModBlockEntities;
import de.ggmfrankie.ggmpipes.utils.DirectionMask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.model.quad.MutableQuad;
import org.joml.Quaternionf;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@EventBusSubscriber(value = Dist.CLIENT)
public abstract class PipeRenderer implements BlockEntityRenderer<PipeEntity, PipeRenderer.PipeRenderState> {

    public static class PipeRenderState extends BlockEntityRenderState {
        public int connectionMask;
    }

    protected Minecraft minecraft;
    protected final AtomicReference<QuadCollection> cachedModel;
    protected BlockEntityRendererProvider.Context renderer;

    public PipeRenderer(BlockEntityRendererProvider.Context renderer, AtomicReference<QuadCollection> cachedModel) {
        this.renderer = renderer;
        minecraft = Minecraft.getInstance();
        this.cachedModel = cachedModel;
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
        state.connectionMask = entity.getInputMask() | entity.getOutputMask();
    }


    private Quaternionf getRotation(Direction direction) {
        return switch (direction) {
            case NORTH -> new Quaternionf();
            case SOUTH -> Axis.YP.rotationDegrees(180F);
            case WEST  -> Axis.YP.rotationDegrees(90F);
            case EAST  -> Axis.YP.rotationDegrees(270F);
            case UP    -> Axis.XP.rotationDegrees(90F);
            case DOWN  -> Axis.XP.rotationDegrees(270F);
        };
    }

    private void renderConnection(QuadCollection model, Direction direction, PoseStack stack, SubmitNodeCollector collector, int combinedLight, int combinedOverlay){
        stack.pushPose();

        // rotate around block center
        stack.translate(0.5D, 0.5D, 0.5D);
        stack.mulPose(getRotation(direction));
        stack.translate(-0.5D, -0.5D, -0.5D);

        List<BakedQuad> quads = model.getQuads(null);
        QuadInstance instance = new QuadInstance();

        collector.submitCustomGeometry(stack, RenderTypes.solidMovingBlock(), (pose, vc) -> {

            instance.setColor(0xFFFFFFFF);
            instance.setLightCoords(combinedLight);
            instance.setOverlayCoords(combinedOverlay);

            for (BakedQuad quad : quads) {
                MutableQuad mq = new MutableQuad();
                mq.setFrom(quad);
                vc.putMutableQuad(pose, mq, instance);
            }
        });

        stack.popPose();
    }


    @Override
    @NullMarked
    public void submit(PipeRenderState pipeRenderState, PoseStack stack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        QuadCollection model = cachedModel.get();
        if (model == null) {
            return;
        }
        int mask = pipeRenderState.connectionMask;

        if ((mask & DirectionMask.NORTH) != 0) renderConnection(model, Direction.NORTH, stack, submitNodeCollector, pipeRenderState.lightCoords, OverlayTexture.NO_OVERLAY);
        if ((mask & DirectionMask.SOUTH) != 0) renderConnection(model, Direction.SOUTH, stack, submitNodeCollector, pipeRenderState.lightCoords, OverlayTexture.NO_OVERLAY);
        if ((mask & DirectionMask.EAST) != 0)  renderConnection(model, Direction.EAST, stack, submitNodeCollector, pipeRenderState.lightCoords, OverlayTexture.NO_OVERLAY);
        if ((mask & DirectionMask.WEST) != 0)  renderConnection(model, Direction.WEST, stack, submitNodeCollector, pipeRenderState.lightCoords, OverlayTexture.NO_OVERLAY);
        if ((mask & DirectionMask.UP) != 0)    renderConnection(model, Direction.UP, stack, submitNodeCollector, pipeRenderState.lightCoords, OverlayTexture.NO_OVERLAY);
        if ((mask & DirectionMask.DOWN) != 0)  renderConnection(model, Direction.DOWN, stack, submitNodeCollector, pipeRenderState.lightCoords, OverlayTexture.NO_OVERLAY);

    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
                ModBlockEntities.ITEM_PIPE_ENTITY.get(),
                ItemPipeRenderer::new
        );
    }
}
