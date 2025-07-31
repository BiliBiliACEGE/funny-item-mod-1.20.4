package net.ace.funnyitemmod;

import net.ace.funnyitemmod.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class FunnyitemModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.CLONE_PROJECTILE, FlyingItemEntityRenderer::new);
    }
}
