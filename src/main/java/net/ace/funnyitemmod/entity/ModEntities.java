package net.ace.funnyitemmod.entity;

import net.ace.FunnyItemMod;
import net.ace.funnyitemmod.entity.projectile.Clone_ballEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<Clone_ballEntity> CLONE_PROJECTILE = Registry.register(Registries.ENTITY_TYPE,
    new Identifier(FunnyItemMod.MOD_ID, "clone_projectile"), FabricEntityTypeBuilder.<Clone_ballEntity>create(SpawnGroup.MISC, Clone_ballEntity::new)
            .dimensions(EntityDimensions.fixed(0.25f,0.25f)).build());
    public static void registerModEntities() {
        System.out.println("模组实体注册"+FunnyItemMod.MOD_ID);
    }
}
