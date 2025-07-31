package net.ace.funnyitemmod.entity.projectile;

import net.ace.funnyitemmod.entity.ModEntities;
import net.ace.funnyitemmod.item.ModItems;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;

public class Clone_ballEntity extends ThrownItemEntity {

    public Clone_ballEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.Clone_ball;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() == this.getOwner()) return;

        if (!(entityHitResult.getEntity() instanceof LivingEntity targetEntity)) return;

        if (!this.getWorld().isClient && this.getWorld() instanceof ServerWorld serverWorld) {
            BlockPos pos = targetEntity.getBlockPos();

            LivingEntity cloneEntity = (LivingEntity) targetEntity.getType().create(
                    serverWorld,
                    e -> {},
                    pos,
                    SpawnReason.TRIGGERED,
                    false,
                    false
            );

            if (cloneEntity != null) {
                // 复制名称和名字显示状态
                if (targetEntity.hasCustomName()) {
                    cloneEntity.setCustomName(targetEntity.getCustomName());
                    cloneEntity.setCustomNameVisible(targetEntity.isCustomNameVisible());
                }

                cloneEntity.setGlowing(targetEntity.isGlowing());
                cloneEntity.setSilent(targetEntity.isSilent());
                cloneEntity.setHealth(Math.min(targetEntity.getHealth(), cloneEntity.getMaxHealth()));
                cloneEntity.setSneaking(targetEntity.isSneaking());

                // 复制装备
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    cloneEntity.equipStack(slot, targetEntity.getEquippedStack(slot).copy());
                }

                // 设置位置和旋转
                cloneEntity.refreshPositionAndAngles(
                        targetEntity.getX(),
                        targetEntity.getY(),
                        targetEntity.getZ(),
                        targetEntity.getYaw(),
                        targetEntity.getPitch()
                );

                // 生成实体
                serverWorld.spawnEntity(cloneEntity);
                System.out.println("克隆实体已生成");

                // 给拥有者发送成功消息
                if (this.getOwner() instanceof ServerPlayerEntity owner) {
                    owner.sendMessage(Text.of("克隆成功!"), false);
                }
            } else {
                System.out.println("克隆实体生成失败");
            }

            this.discard();
        }

        super.onEntityHit(entityHitResult);
    }
}
