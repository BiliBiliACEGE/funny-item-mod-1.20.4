package net.ace.funnyitemmod.block;

import net.ace.funnyitemmod.FunnyItemMod;
import net.ace.funnyitemmod.block.custom.RuneAnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public final static Block Rune_anvil = registerBlock("rune_anvil",
            new RuneAnvilBlock(Block.Settings.copy(Blocks.ANVIL).registryKey(RegistryKey.of(RegistryKeys.BLOCK,Identifier.of(FunnyItemMod.MOD_ID,"rune_anvil")))));

    private  static  Block registerBlock(String name , Block block) {
        registerBlockItem(name,block);
        return  Registry.register(Registries.BLOCK,Identifier.of(FunnyItemMod.MOD_ID,name),block);
    }
    private  static void registerBlockItem(String name , Block block) {
        Registry.register(Registries.ITEM, Identifier.of(FunnyItemMod.MOD_ID, name),
                new BlockItem(block, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(FunnyItemMod.MOD_ID, name)))));
    }
    public static void registerModBlocks() {
        System.out.println("模组方块注册 " + FunnyItemMod.MOD_ID);
    }
}
