package net.ace.funnyitemmod.network;

import net.ace.funnyitemmod.FunnyItemMod;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record HammerModePayload(int mode) implements CustomPayload {
    public static final Id<HammerModePayload> ID = new Id<>(net.minecraft.util.Identifier.of(FunnyItemMod.MOD_ID, "hammer_mode_c2s"));
    public static final PacketCodec<RegistryByteBuf, HammerModePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, HammerModePayload::mode,
            HammerModePayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}