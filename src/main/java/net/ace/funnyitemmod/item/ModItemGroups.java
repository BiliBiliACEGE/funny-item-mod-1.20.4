package net.ace.funnyitemmod.item;

import net.ace.FunnyItemMod;
import net.ace.funnyitemmod.block.ModBlocks;
import net.ace.funnyitemmod.enchantments.ModEnchantments;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup Mod_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(FunnyItemMod.MOD_ID, "time"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.time"))
                    .icon(() -> new ItemStack(ModBlocks.Rune_anvil)).entries((displayContext, entries) -> {
                        entries.add(ModItems.Bullet_time);
                        entries.add(ModItems.Get_Block_Staff);
                        entries.add(ModItems.Simple_Wooden_Axe);
                        entries.add(ModItems.Clone);
                        entries.add(ModItems.Axe);
                        entries.add(ModItems.Hammer);
                        entries.add(ModItems.Hammer_head);
                        entries.add(ModItems.Hammer_handle);
                        entries.add(ModBlocks.Rune_anvil);
                        entries.add(ModItems.Chunk_pickaxe);
                        entries.add(ModItems.Clone_ball);






                    }).build());


    public static void registerItemGroups() {
        System.out.println("模组物品组注册"+ FunnyItemMod.MOD_ID);
    }
}