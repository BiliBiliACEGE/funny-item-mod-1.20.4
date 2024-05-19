package net.ace.funnyitemmod.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChunkPickaxeItem extends PickaxeItem {
    private static final Set<ItemStack> MINERAL_ITEMS = new HashSet<>();
    private boolean diggingUp = false; // 挖掘方向，默认为向下

    static {
        MINERAL_ITEMS.add(new ItemStack(Items.DIAMOND));
        MINERAL_ITEMS.add(new ItemStack(Items.IRON_INGOT));
        MINERAL_ITEMS.add(new ItemStack(Items.GOLD_INGOT));
        MINERAL_ITEMS.add(new ItemStack(Items.REDSTONE));
        MINERAL_ITEMS.add(new ItemStack(Items.LAPIS_LAZULI));
        MINERAL_ITEMS.add(new ItemStack(Items.COAL));
        MINERAL_ITEMS.add(new ItemStack(Items.EMERALD));
        MINERAL_ITEMS.add(new ItemStack(Items.NETHERITE_SCRAP));
        // 根据需要添加更多矿物项
    }

    public ChunkPickaxeItem(FabricItemSettings settings) {
        super(ToolMaterials.NETHERITE, 1, -2.8F, settings);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient() && miner instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) miner;
            int startX = (pos.getX() >> 4) << 4;
            int startZ = (pos.getZ() >> 4) << 4;
            int startY = pos.getY();

            // 根据挖掘方向确定循环的起始和结束条件
            int endY = diggingUp ? world.getTopY() : world.getBottomY();

            // 逐层破坏整个区块直到达到顶部或基岩层
            for (int y = startY; diggingUp ? y <= endY : y >= endY; y = diggingUp ? y + 1 : y - 1) {
                for (int x = startX; x < startX + 16; x++) {
                    for (int z = startZ; z < startZ + 16; z++) {
                        BlockPos currentPos = new BlockPos(x, y, z);
                        BlockState currentState = world.getBlockState(currentPos);
                        Block currentBlock = currentState.getBlock();
                        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 20 * 30, 0));

                        if (currentBlock != Blocks.BEDROCK && !currentState.isAir()) {
                            world.breakBlock(currentPos, true, miner); // 产生掉落物
                        }
                        if (currentBlock == Blocks.GRASS_BLOCK || currentBlock == Blocks.TALL_GRASS || currentBlock == Blocks.SHORT_GRASS) {
                            continue;
                        }

                        // 清除周围的掉落物
                        if (currentBlock != Blocks.BEDROCK) {
                            clearDroppedItems(world, new BlockPos(x, y, z));
                        } else {
                            // 如果遇到基岩，停止挖掘并切换挖掘方向
                            diggingUp = !diggingUp;
                            return true;
                        }
                        if (diggingUp){
                            livingEntity.removeStatusEffect(StatusEffects.SLOW_FALLING);
                        }
                    }
                }
            }
        }
        return super.postMine(stack, world, state, pos, miner);
    }

    private static void clearDroppedItems(World world, BlockPos pos) {
        if (world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) world;
            Box searchBox = new Box(
                    pos.getX() - 8, pos.getY() - 8, pos.getZ() - 8,
                    pos.getX() + 8, pos.getY() + 8, pos.getZ() + 8
            );
            List<ItemEntity> itemEntities = serverWorld.getEntitiesByClass(ItemEntity.class, searchBox, entity -> true);
            for (ItemEntity itemEntity : itemEntities) {
                if (!isMineralItem(itemEntity.getStack())) {
                    itemEntity.discard(); // 使用 discard() 方法来删除实体
                }
            }
        }
    }

    private static boolean isMineralItem(ItemStack itemStack) {
        for (ItemStack mineralItem : MINERAL_ITEMS) {
            if (ItemStack.areItemsEqual(itemStack, mineralItem)) {
                return true;
            }
        }
        return false;
    }
}
