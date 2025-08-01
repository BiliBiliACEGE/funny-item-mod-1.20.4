package net.ace.funnyitemmod.villager;

import com.google.common.collect.ImmutableSet;
import net.ace.funnyitemmod.FunnyItemMod;
import net.ace.funnyitemmod.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

public class ModVillagers {
    public static final RegistryKey<PointOfInterestType> RUNE_POI_KEY = poiKey("rune_poi");
    public static final PointOfInterestType RUNE_POI = registerPoi("rune_poi", ModBlocks.Rune_anvil.getDefaultState().getBlock());

    public static final VillagerProfession RUNE_SMITH = registerProfession("rune_smith");

    private static VillagerProfession registerProfession(String name){
        return Registry.register(Registries.VILLAGER_PROFESSION,Identifier.of(FunnyItemMod.MOD_ID,name),
                new VillagerProfession(Text.translatable("entity.minecraft.villager." + name), entry -> entry.matchesKey(ModVillagers.RUNE_POI_KEY), entry -> entry.matchesKey(ModVillagers.RUNE_POI_KEY),
                        ImmutableSet.of(),ImmutableSet.of(),SoundEvents.ENTITY_VILLAGER_WORK_TOOLSMITH));
    }

    private static PointOfInterestType registerPoi(String name, Block block){
        return PointOfInterestHelper.register(Identifier.of(FunnyItemMod.MOD_ID,name),1,1,block);
    }

    private static RegistryKey<PointOfInterestType> poiKey(String name){
        return RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, Identifier.of(FunnyItemMod.MOD_ID,name));
    }

    public static void registerVillagers() {
        System.out.println("模组村民注册"+ FunnyItemMod.MOD_ID);
    }
}
