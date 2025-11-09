package net.ace.funnyitemmod;

import net.ace.funnyitemmod.client.HammerModeClient;
import net.ace.funnyitemmod.client.network.HammerModeNetworkClient;
import net.ace.funnyitemmod.entity.ModEntities;
import net.ace.funnyitemmod.network.HammerModeSyncPayload;
import net.ace.funnyitemmod.renderer.CloneBallEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class FunnyitemModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.println("客户端初始化，注册CloneBallEntity渲染器");
        EntityRendererRegistry.register(ModEntities.CLONE_PROJECTILE, CloneBallEntityRenderer::new);
        ClientPlayNetworking.registerGlobalReceiver(HammerModeSyncPayload.ID, (payload, context) -> {
            int mode = payload.mode();
            context.client().execute(() -> HammerModeClient.setMode(mode));
        });
        HammerModeClient.init();
        HammerModeNetworkClient.init();
    }
}
