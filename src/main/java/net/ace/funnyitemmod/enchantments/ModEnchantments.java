package net.ace.funnyitemmod.enchantments;

import net.ace.funnyitemmod.FunnyItemMod;

public class ModEnchantments {
    //这里注册新附魔

//   private static Enchantment register(String name, Enchantment enchantment){
//       return Registry.register(Registries.ENCHANTMENT_PROVIDER_TYPE, Identifier.of(FunnyItemMod.MOD_ID, name), enchantment);
//   }

    public static void registerModEnchantments() {
        System.out.println("模组附魔注册"+ FunnyItemMod.MOD_ID);
    }
}
