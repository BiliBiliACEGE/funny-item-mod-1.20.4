package net.ace.funnyitemmod.item.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;

public class AxeItem extends net.minecraft.item.AxeItem {
    public AxeItem(Settings settings) {
        super(ToolMaterial.NETHERITE, 11, -2.8F,settings);
    }
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient && entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (selected || player.getOffHandStack().getItem() == this) {
                if (player.experienceLevel % 10 == 0 && player.experienceLevel != 0 && !player.hasStatusEffect(StatusEffects.STRENGTH)) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0, false, false, true));
                }
            }
        }
    }
}