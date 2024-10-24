package net.ace.funnyitemmod.block;

import net.ace.FunnyItemMod;
import net.ace.funnyitemmod.block.custom.RuneAnvilBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public final static Block Rune_anvil = registerBlock("rune_anvil",
            new RuneAnvilBlock(FabricBlockSettings.copyOf(Blocks.ANVIL).nonOpaque()));

    private  static  Block registerBlock(String name , Block block) {
        registerBlockItem(name,block);
        return  Registry.register(Registries.BLOCK,new Identifier(FunnyItemMod.MOD_ID,name),block);
    }
    private  static Item registerBlockItem(String name , Block block) {
        return Registry.register(Registries.ITEM,new Identifier(FunnyItemMod.MOD_ID,name),
                new BlockItem(block,new FabricItemSettings()));
    }
    public static void registerModBlocks() {
        System.out.println("模组方块注册 " + FunnyItemMod.MOD_ID);
    }
}
