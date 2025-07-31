package net.ace.funnyitemmod.util;

import net.ace.funnyitemmod.FunnyItemMod;
import net.ace.funnyitemmod.item.ModItems;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import net.minecraft.village.VillagerProfession;

public class ModCustomTrades {
    public static void registerCustomTrades() {
        RegistryKey<VillagerProfession> runeSmithKey = RegistryKey.of(
                RegistryKeys.VILLAGER_PROFESSION,
                Identifier.of(FunnyItemMod.MOD_ID, "rune_smith")
        );
        TradeOfferHelper.registerVillagerOffers(runeSmithKey,1,
                factories -> factories.add(((entity, random) -> new TradeOffer(
                        new TradedItem(Items.DIAMOND, 10),
                        new ItemStack(ModItems.Hammer,1),
                        1,12,0.075f
                ))));
        TradeOfferHelper.registerVillagerOffers(runeSmithKey,1,
                factories -> factories.add(((entity, random) -> new TradeOffer(
                        new TradedItem(Items.GOLD_INGOT, 10),
                        new ItemStack(Items.DIAMOND,1),
                        9,12,0.075f
                ))));
        TradeOfferHelper.registerVillagerOffers(runeSmithKey,2,
                factories -> factories.add(((entity, random) -> new TradeOffer(
                        new TradedItem(Blocks.IRON_BLOCK.asItem(), 10),
                        new ItemStack(Items.DIAMOND,1),
                        3,12,0.075f
                ))));
        TradeOfferHelper.registerVillagerOffers(runeSmithKey,2,
                factories -> factories.add(((entity, random) -> new TradeOffer(
                        new TradedItem(Blocks.DIAMOND_BLOCK.asItem(), 5),
                        new ItemStack(Items.NETHERITE_SCRAP,3),
                        5,12,0.075f
                ))));
    }
}
