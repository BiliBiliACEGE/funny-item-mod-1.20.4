package net.ace.funnyitemmod.client.network;

import net.ace.funnyitemmod.network.HammerModeSyncPayload;
import net.ace.funnyitemmod.util.HammerModeManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

public class HammerModeNetworkClient {

    public static void init() {
        // 注册服务器发往客户端的包（S2C）
        ClientPlayNetworking.registerGlobalReceiver(HammerModeSyncPayload.ID,
                (payload, context) -> {
                    int newMode = payload.mode();
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.player != null) {
                        HammerModeManager.setClientMode(newMode);
                    }
                });
    }
}
