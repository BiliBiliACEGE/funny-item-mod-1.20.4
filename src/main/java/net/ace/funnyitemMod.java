package net.ace;

import net.ace.funnyitemmod.block.ModBlocks;
import net.ace.funnyitemmod.command.custom.ToggleDirectionCommand;
import net.ace.funnyitemmod.item.ModItemGroups;
import net.ace.funnyitemmod.item.ModItems;
import net.ace.funnyitemmod.item.custom.AxeItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class funnyitemMod implements ModInitializer {
	public static final String MOD_ID = "funny-item-mod";
	public static final Logger LOGGER = LoggerFactory.getLogger("funny-item-mod");

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();
		ModBlocks.registerModBlocks();
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			server.getPlayerManager().getPlayerList().forEach(player -> {
				if (player.getMainHandStack().getItem() instanceof AxeItem || player.getOffHandStack().getItem() instanceof AxeItem) {
					int expLevel = player.experienceLevel;
					// 检查玩家经验等级是否为 10 的倍数且不为 0
					if (expLevel > 0 && expLevel % 10 == 0 && !player.hasStatusEffect(StatusEffects.STRENGTH)) {
						// 给予力量效果，持续时间为 30 秒（600 ticks）
						player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0, false, false, true));
					}
				}
			});
			CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> {
				ToggleDirectionCommand.register(dispatcher);

			});
		});
	}
}