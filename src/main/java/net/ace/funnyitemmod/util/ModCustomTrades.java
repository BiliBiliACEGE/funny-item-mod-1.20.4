package net.ace.funnyitemmod.util;

import net.ace.funnyitemmod.item.ModItems;
import net.ace.funnyitemmod.villager.ModVillagers;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;

public class ModCustomTrades {
    public static void registerCustomTrades() {
        TradeOfferHelper.registerVillagerOffers(ModVillagers.RUNE_SMITH,1,
                factories -> factories.add(((entity, random) -> new TradeOffer(
                        new ItemStack(Items.DIAMOND, 10),
                        new ItemStack(ModItems.Hammer,1),
                        1,12,0.075f
                ))));
        TradeOfferHelper.registerVillagerOffers(ModVillagers.RUNE_SMITH,1,
                factories -> factories.add(((entity, random) -> new TradeOffer(
                        new ItemStack(Items.GOLD_INGOT, 10),
                        new ItemStack(Items.DIAMOND,1),
                        9,12,0.075f
                ))));
        TradeOfferHelper.registerVillagerOffers(ModVillagers.RUNE_SMITH,2,
                factories -> factories.add(((entity, random) -> new TradeOffer(
                        new ItemStack(Blocks.IRON_BLOCK.asItem(), 10),
                        new ItemStack(ModItems.Axe,1),
                        3,12,0.075f
                ))));
        TradeOfferHelper.registerVillagerOffers(ModVillagers.RUNE_SMITH,2,
                factories -> factories.add(((entity, random) -> new TradeOffer(
                        new ItemStack(Blocks.DIAMOND_BLOCK.asItem(), 5),
                        new ItemStack(Items.NETHERITE_SCRAP,3),
                        5,12,0.075f
                ))));
    }
}
