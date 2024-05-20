package net.ace;

import net.ace.funnyitemmod.block.ModBlocks;
import net.ace.funnyitemmod.command.custom.ToggleDirectionCommand;
import net.ace.funnyitemmod.item.ModItemGroups;
import net.ace.funnyitemmod.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
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
		CommandRegistrationCallback.EVENT.register((dispatcher,dedicated,environment) -> {
			ToggleDirectionCommand.register(dispatcher);

		});
	}
}