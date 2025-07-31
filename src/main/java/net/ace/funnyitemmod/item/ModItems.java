package net.ace.funnyitemmod.item;

import net.ace.funnyitemmod.FunnyItemMod;
import net.ace.funnyitemmod.item.custom.*;
import net.ace.funnyitemmod.item.custom.AxeItem;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item Bullet_time = registerItem("bullet_time",
            new Bullet_timeItem(new Item.Settings().maxCount(1).registryKey(RegistryKey.of(RegistryKeys.ITEM,Identifier.of(FunnyItemMod.MOD_ID,"bullet_time")))));
    public static final Item Get_Block_Staff = registerItem("get_block_staff",
            new Get_Block_StaffItem(new Item.Settings().maxCount(1).registryKey(RegistryKey.of(RegistryKeys.ITEM,Identifier.of(FunnyItemMod.MOD_ID,"get_block_staff")))));
    public static final  Item Simple_Wooden_Axe = registerItem("simple_wooden_axe",
            new Simple_wooden_axeItem(new Item.Settings().maxCount(1).registryKey(RegistryKey.of(RegistryKeys.ITEM,Identifier.of(FunnyItemMod.MOD_ID,"simple_wooden_axe")))));
    public static final  Item Axe = registerItem("axe",
            new AxeItem(new Item.Settings().maxCount(1).registryKey(RegistryKey.of(RegistryKeys.ITEM,Identifier.of(FunnyItemMod.MOD_ID,"axe")))));
    public static final Item Clone = registerItem("clone",
            new CloneItem(new Item.Settings().maxCount(1).registryKey(RegistryKey.of(RegistryKeys.ITEM,Identifier.of(FunnyItemMod.MOD_ID,"clone")))));
    public static final Item Hammer = registerItem("rune_smith_hammer",
            new HammerItem(new Item.Settings().maxCount(1).registryKey(RegistryKey.of(RegistryKeys.ITEM,Identifier.of(FunnyItemMod.MOD_ID,"rune_smith_hammer")))));
    public static final Item Hammer_head = registerItem("rune_smith_hammer_head",
            new Item(new Item.Settings().maxCount(1).registryKey(RegistryKey.of(RegistryKeys.ITEM,Identifier.of(FunnyItemMod.MOD_ID,"rune_smith_hammer_head")))));
    public static final Item Hammer_handle = registerItem("rune_smith_hammer_handle",
            new Item(new Item.Settings().maxCount(1).registryKey(RegistryKey.of(RegistryKeys.ITEM,Identifier.of(FunnyItemMod.MOD_ID,"rune_smith_hammer_handle")))));
    public static final Item Chunk_pickaxe = registerItem("chunk_pickaxe",
            new ChunkPickaxeItem(new Item.Settings().maxCount(1).registryKey(RegistryKey.of(RegistryKeys.ITEM,Identifier.of(FunnyItemMod.MOD_ID,"chunk_pickaxe")))));
    public static final Item Clone_ball = registerItem("clone_ball",
            new clone_ball_Item(new Item.Settings().maxCount(64).registryKey(RegistryKey.of(RegistryKeys.ITEM,Identifier.of(FunnyItemMod.MOD_ID,"clone_ball")))));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(FunnyItemMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        System.out.println("模组物品注册"+ FunnyItemMod.MOD_ID);
    }
}