package net.ace.funnyitemmod.util;

import net.ace.funnyitemmod.network.HammerModePayload;
import net.ace.funnyitemmod.network.HammerModeSyncPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理玩家锤子模式（Hammer Mode）的同步与存储
 */
public final class HammerModeManager {
    // 服务端存储各玩家的模式
    private static final Map<ServerPlayerEntity, Integer> SERVER_MODES = new HashMap<>();

    // 客户端本地当前模式（单机时也适用）
    private static int clientMode = 1;

    /* ================= 服务端逻辑 ================= */

    /** 获取玩家当前模式（服务端） */
    public static int get(ServerPlayerEntity player) {
        return SERVER_MODES.getOrDefault(player, 1);
    }

    /** 设置玩家模式（服务端） */
    public static void set(ServerPlayerEntity player, int mode) {
        mode = Math.min(Math.max(mode, 1), 255); // 限制 1~255
        SERVER_MODES.put(player, mode);
        sync(player);
    }

    /** 移除玩家记录（退出时调用） */
    public static void remove(ServerPlayerEntity player) {
        SERVER_MODES.remove(player);
    }

    /** 将玩家当前模式同步给客户端 */
    public static void sync(ServerPlayerEntity player) {
        int mode = get(player);
        ServerPlayNetworking.send(player, new HammerModeSyncPayload(mode));
    }

    /* ================= 客户端逻辑 ================= */

    /** 设置客户端本地模式（由服务器下发时调用） */
    public static void setClientMode(int mode) {
        clientMode = Math.min(Math.max(mode, 1), 255);
    }

    /** 获取客户端当前模式（客户端用） */
    public static int getClientMode() {
        return clientMode;
    }

    /** 切换模式（客户端主动发请求时调用） */
    public static void requestChangeMode(int newMode) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.getNetworkHandler() != null) {
            ClientPlayNetworking.send(new HammerModePayload(newMode));

        }
    }
}
