package net.ace.funnyitemmod.item;

import net.ace.funnyitemMod;
import net.ace.funnyitemmod.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup Mod_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(funnyitemMod.MOD_ID, "time"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.time"))
                    .icon(() -> new ItemStack(ModItems.Time_freezer)).entries((displayContext, entries) -> {
                        entries.add(ModItems.Time_freezer);
                        entries.add(ModItems.Get_Block_Staff);
                        entries.add(ModItems.Get_Block_Staff_Handle);
                        entries.add(ModItems.Get_Block_Staff_Head);
                        entries.add(ModItems.Simple_Wooden_Axe);
                        entries.add(ModItems.Clone);
                        entries.add(ModItems.Axe);
                        entries.add(ModItems.Hammer);
                        entries.add(ModBlocks.Rune_anvil);






                    }).build());


    public static void registerItemGroups() {
        funnyitemMod.LOGGER.info("模组物品组注册 " + funnyitemMod.MOD_ID);
    }
}