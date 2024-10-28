package net.ace.funnyitemmod.item;

import net.ace.FunnyItemMod;
import net.ace.funnyitemmod.item.custom.*;
import net.ace.funnyitemmod.item.custom.AxeItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item Bullet_time = registerItem("bullet_time",
            new Bullet_timeItem(new FabricItemSettings().maxCount(1)));
    public static final Item Get_Block_Staff = registerItem("get_block_staff",
            new Get_Block_StaffItem(new FabricItemSettings().maxCount(1)));
    public static final  Item Simple_Wooden_Axe = registerItem("simple_wooden_axe",
            new Simple_wooden_axeItem(new FabricItemSettings().maxCount(1)));
    public static final  Item Axe = registerItem("axe",
            new AxeItem(new FabricItemSettings().maxCount(1)));
    public static final Item Clone = registerItem("clone",
            new CloneItem(new FabricItemSettings().maxCount(1)));
    public static final Item Hammer = registerItem("rune_smith_hammer",
            new HammerItem(new FabricItemSettings().maxCount(1)));
    public static final Item Hammer_head = registerItem("rune_smith_hammer_head",
            new Item(new FabricItemSettings().maxCount(1)));
    public static final Item Hammer_handle = registerItem("rune_smith_hammer_handle",
            new Item(new FabricItemSettings().maxCount(1)));
    public static final Item Chunk_pickaxe = registerItem("chunk_pickaxe",
            new ChunkPickaxeItem(new FabricItemSettings().maxCount(1)));
    public static final Item Clone_ball = registerItem("clone_ball",
    new clone_ball_Item(new FabricItemSettings().maxCount(64)));

    private static void addItemsItemGroup(FabricItemGroupEntries entries) {
    }
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(FunnyItemMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        System.out.println("模组物品注册"+ FunnyItemMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsItemGroup);
    }
}