package net.ace.funnyitemmod.entity.projectile;

import net.ace.funnyitemmod.entity.ModEntities;
import net.ace.funnyitemmod.item.ModItems;
import net.minecraft.nbt.NbtCompound;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class Clone_ballEntity extends ThrownItemEntity {

    public Clone_ballEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public Clone_ballEntity(LivingEntity LivingEntity, World world) {
        super(ModEntities.CLONE_PROJECTILE, LivingEntity, world);
    }


    @Override
    protected Item getDefaultItem() {
        return ModItems.Clone_ball;
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }
    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() == this.getOwner()) {
            return;
        }

        if (!(entityHitResult.getEntity() instanceof LivingEntity targetEntity)) {
            return;
        }

        World world = this.getEntityWorld();

        if (!world.isClient) {
            // 创建克隆实体
            LivingEntity cloneEntity = (LivingEntity) targetEntity.getType().create(world);
            if (cloneEntity != null) {
                // 复制部分NBT数据
                NbtCompound targetNbt = new NbtCompound();
                targetEntity.writeNbt(targetNbt);

                // 选择性复制NBT数据，避免生成冲突
                // 不复制UUID，避免生成冲突
                targetNbt.remove("UUID");  // 移除UUID，防止冲突

                // 将筛选后的NBT应用到克隆实体
                cloneEntity.readNbt(targetNbt);

                // 设置克隆实体的位置和朝向
                cloneEntity.refreshPositionAndAngles(
                        targetEntity.getX(),
                        targetEntity.getY(),
                        targetEntity.getZ(),
                        targetEntity.getYaw(),
                        targetEntity.getPitch()
                );

                // 将克隆体生成到世界中
                world.spawnEntity(cloneEntity);
                System.out.println("克隆实体已生成");

                // 通知所有者克隆成功
                if (this.getOwner() instanceof LivingEntity owner) {
                    owner.sendMessage(Text.of("克隆成功!"));
                }
            } else {
                System.out.println("克隆实体生成失败");
            }

            // 移除投掷物
            this.discard();
        }

        super.onEntityHit(entityHitResult);
    }




}

