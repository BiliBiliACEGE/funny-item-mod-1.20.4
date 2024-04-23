package net.ace.funnyitemmod.item;

import net.ace.funnyitemMod;
import net.ace.funnyitemmod.item.custom.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item Time_freezer = registerItem("time_freezer",
            new Time_FreezerItem(new FabricItemSettings().maxCount(1)));
    public  static final Item Time_Control = registerItem("time_control",
            new Time_ControlItem(new FabricItemSettings().maxCount(1)));
    public static final Item Difficulty_Switch = registerItem("difficulty_switch",
            new Difficulty_SwitchItem(new FabricItemSettings().maxCount(1)));
    public static final Item Get_Block_Staff = registerItem("get_block_staff",
            new Get_Block_StaffItem(new FabricItemSettings().maxCount(1)));
    public static final Item Get_Block_Staff_Handle = registerItem("get_block_staff_handle",
            new Item(new FabricItemSettings().maxCount(64)));
    public static final Item Get_Block_Staff_Head = registerItem("get_block_staff_head",
            new Item(new FabricItemSettings().maxCount(64)));
    public static final Item Fill_Block = registerItem("fill_block",
            new Fill_BlockItem(new FabricItemSettings().maxCount(1)));
    public static final  Item Simple_Wooden_Axe = registerItem("simple_wooden_axe",
            new Simple_wooden_axeItem(new FabricItemSettings().maxCount(1)));
    private static void addItemsItemGroup(FabricItemGroupEntries entries) {
    }
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(funnyitemMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        funnyitemMod.LOGGER.info("模组物品注册" + funnyitemMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsItemGroup);
    }
}