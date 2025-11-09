package net.ace.funnyitemmod;

import net.ace.funnyitemmod.block.ModBlocks;
import net.ace.funnyitemmod.enchantments.ModEnchantments;
import net.ace.funnyitemmod.entity.ModEntities;
import net.ace.funnyitemmod.event.PlayerEventHandler;
import net.ace.funnyitemmod.item.ModItemGroups;
import net.ace.funnyitemmod.item.ModItems;
import net.ace.funnyitemmod.network.HammerModeNetworkServer;
import net.ace.funnyitemmod.network.HammerModePayload;
import net.ace.funnyitemmod.network.HammerModeSyncPayload;
import net.ace.funnyitemmod.sound.ModSounds;
import net.ace.funnyitemmod.util.HammerModeManager;
import net.ace.funnyitemmod.util.ModCustomTrades;
import net.ace.funnyitemmod.villager.ModVillagers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.math.MathHelper;

import java.util.Objects;


public class FunnyItemMod implements ModInitializer {
	public static final String MOD_ID = "funny-item-mod";

	@Override
	public void onInitialize() {
		PayloadTypeRegistry.playC2S().register(HammerModePayload.ID, HammerModePayload.CODEC);
		PayloadTypeRegistry.playS2C().register(HammerModeSyncPayload.ID, HammerModeSyncPayload.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(HammerModePayload.ID, (payload, ctx) -> {
			var player = ctx.player();
			if (player == null) return;
			Objects.requireNonNull(player.getServer()).execute(() -> {
				int clamped = MathHelper.clamp(payload.mode(), 1, 255);
				HammerModeManager.set(player, clamped);

				// 同步给客户端
				ServerPlayNetworking.send(player, new HammerModeSyncPayload(clamped));
			});
		});
		HammerModeNetworkServer.init();
		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();
		ModBlocks.registerModBlocks();
		ModEntities.registerModEntities();
		ModSounds.registerSounds();
		ModEnchantments.registerModEnchantments();
		ModVillagers.registerVillagers();
		ModCustomTrades.registerCustomTrades();
		PlayerEventHandler.registerEvents();
	}
}