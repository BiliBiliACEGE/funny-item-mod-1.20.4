package net.ace.funnyitemmod.enchantments;

import net.ace.FunnyItemMod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEnchantments {
    //这里注册新的附魔

   private static Enchantment register(String name, Enchantment enchantment){
       return Registry.register(Registries.ENCHANTMENT, new Identifier(FunnyItemMod.MOD_ID, name), enchantment);
   }

    public static void registerModEnchantments() {
        System.out.println("模组附魔注册"+ FunnyItemMod.MOD_ID);
    }
}
