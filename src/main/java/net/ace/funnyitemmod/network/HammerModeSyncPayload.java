package net.ace.funnyitemmod.network;

import net.ace.funnyitemmod.FunnyItemMod;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * 服务器 → 客户端：同步玩家锤子模式
 */
public record HammerModeSyncPayload(int mode) implements CustomPayload {
    public static final CustomPayload.Id<HammerModeSyncPayload> ID =
            new CustomPayload.Id<>(Identifier.of(FunnyItemMod.MOD_ID, "hammer_mode_s2c"));

    public static final PacketCodec<RegistryByteBuf, HammerModeSyncPayload> CODEC =
            PacketCodec.tuple(PacketCodecs.INTEGER, HammerModeSyncPayload::mode, HammerModeSyncPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
