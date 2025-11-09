package net.ace.funnyitemmod.network;

import net.ace.funnyitemmod.util.HammerModeManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class HammerModeNetworkServer {

    public static void init() {
        // 注册客户端发往服务器的包（C2S）
        ServerPlayNetworking.registerGlobalReceiver(HammerModePayload.ID,
                (payload, context) -> {
                    ServerPlayerEntity player = context.player();
                    int newMode = payload.mode();
                    HammerModeManager.set(player, newMode);
                    HammerModeManager.sync(player); // 同步给客户端
                });
    }

    public static void onPlayerJoin(ServerPlayerEntity player) {
        HammerModeManager.sync(player);
    }
}
