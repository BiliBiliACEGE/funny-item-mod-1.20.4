package net.ace.funnyitemmod.item.custom;

import net.ace.funnyitemmod.entity.projectile.Clone_ballEntity;
import net.ace.funnyitemmod.sound.ModSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.ace.funnyitemmod.entity.ModEntities;

public class clone_ball_Item extends Item {

    public clone_ball_Item(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), ModSounds.CLONE_BALL_HIT, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        if (!world.isClient) {
            Clone_ballEntity cloneBallEntity = new Clone_ballEntity(ModEntities.CLONE_PROJECTILE, world);
            cloneBallEntity.setPos(user.getX(), user.getEyeY(), user.getZ());
            cloneBallEntity.setItem(itemStack);
            cloneBallEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 1.0f);
            boolean spawned = world.spawnEntity(cloneBallEntity);
            System.out.println("投掷物实体生成，位置：" + cloneBallEntity.getPos() + " spawnEntity返回: " + spawned);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return ActionResult.SUCCESS;
    }
}
