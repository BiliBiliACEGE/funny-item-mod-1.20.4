package net.ace.funnyitemmod;

import net.ace.funnyitemmod.entity.ModEntities;
import net.ace.funnyitemmod.renderer.CloneBallEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class FunnyitemModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.println("客户端初始化，注册CloneBallEntity渲染器");
        EntityRendererRegistry.register(ModEntities.CLONE_PROJECTILE, CloneBallEntityRenderer::new);
    }
}
