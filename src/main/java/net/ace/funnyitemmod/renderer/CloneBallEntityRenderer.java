package net.ace.funnyitemmod.renderer;

import net.ace.funnyitemmod.entity.projectile.Clone_ballEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.state.FlyingItemEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class CloneBallEntityRenderer extends FlyingItemEntityRenderer<Clone_ballEntity> {

    public CloneBallEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(FlyingItemEntityRenderState flyingItemEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        System.out.println("[客户端渲染] CloneBallEntityRenderer.render() 被调用");
        super.render(flyingItemEntityRenderState, matrixStack, vertexConsumerProvider, i);
    }
}

