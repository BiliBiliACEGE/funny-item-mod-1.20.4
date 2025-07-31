package net.ace.funnyitemmod.item;

import net.ace.funnyitemmod.FunnyItemMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final RegistryKey<ItemGroup> MOD_GROUP_KEY = RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of(FunnyItemMod.MOD_ID, "time"));

    public static final ItemGroup Mod_GROUP = Registry.register(
            Registries.ITEM_GROUP,
            Identifier.of(FunnyItemMod.MOD_ID, "time"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup.time"))
                    .icon(() -> new net.minecraft.item.ItemStack(net.ace.funnyitemmod.block.ModBlocks.Rune_anvil))
                    .build()
    );

    public static void registerItemGroups() {
        System.out.println("模组物品组注册 " + FunnyItemMod.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(MOD_GROUP_KEY).register(entries -> {
            entries.add(net.ace.funnyitemmod.item.ModItems.Bullet_time);
            entries.add(net.ace.funnyitemmod.item.ModItems.Get_Block_Staff);
            entries.add(net.ace.funnyitemmod.item.ModItems.Simple_Wooden_Axe);
            entries.add(net.ace.funnyitemmod.item.ModItems.Axe);
            entries.add(net.ace.funnyitemmod.item.ModItems.Clone);
            entries.add(net.ace.funnyitemmod.item.ModItems.Hammer);
            entries.add(net.ace.funnyitemmod.item.ModItems.Hammer_head);
            entries.add(net.ace.funnyitemmod.item.ModItems.Hammer_handle);
            entries.add(net.ace.funnyitemmod.block.ModBlocks.Rune_anvil);
            entries.add(net.ace.funnyitemmod.item.ModItems.Chunk_pickaxe);
            entries.add(net.ace.funnyitemmod.item.ModItems.Clone_ball);
        });
    }
}
