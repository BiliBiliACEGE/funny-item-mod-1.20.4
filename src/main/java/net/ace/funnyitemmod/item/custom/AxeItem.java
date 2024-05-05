package net.ace.funnyitemmod.item.custom;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class AxeItem extends Item {
    public AxeItem(Settings settings) {
        super(settings);
    }

    // 重写物品使用方法
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        // 检查玩家是否具有足够的经验值并且没有力量效果
        if (player.experienceLevel >= 10 && player.experienceLevel % 10 == 0 && !player.hasStatusEffect(StatusEffects.STRENGTH)) {
            // 给予玩家力量效果
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 200, 0));
        }

        // 调用父类的物品使用方法
        return super.use(world, player, hand);
    }
    
}
