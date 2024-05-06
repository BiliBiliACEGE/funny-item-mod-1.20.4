package net.ace;

import net.ace.funnyitemmod.block.ModBlocks;
import net.ace.funnyitemmod.item.ModItemGroups;
import net.ace.funnyitemmod.item.ModItems;
import net.ace.funnyitemmod.item.custom.AxeItem;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
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
	}
}