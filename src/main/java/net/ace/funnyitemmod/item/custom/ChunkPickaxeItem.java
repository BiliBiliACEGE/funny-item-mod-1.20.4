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

import java.util.*;

public class ChunkPickaxeItem extends PickaxeItem {
    private static final Set<ItemStack> MINERAL_ITEMS = new HashSet<>();
    private static boolean diggingUp = false; // 挖掘方向，默认为向下
    private static final String currentMode = "Mining"; // 默认模式为挖掘模式
    private static final Stack<Map.Entry<BlockPos, BlockState>> previousBlockStates = new Stack<>(); // 记录多个方块状态

    static {
        MINERAL_ITEMS.add(new ItemStack(Items.DIAMOND));
        MINERAL_ITEMS.add(new ItemStack(Items.IRON_INGOT));
        MINERAL_ITEMS.add(new ItemStack(Items.GOLD_INGOT));
        MINERAL_ITEMS.add(new ItemStack(Items.REDSTONE));
        MINERAL_ITEMS.add(new ItemStack(Items.LAPIS_LAZULI));
        MINERAL_ITEMS.add(new ItemStack(Items.COAL));
        MINERAL_ITEMS.add(new ItemStack(Items.EMERALD));
        MINERAL_ITEMS.add(new ItemStack(Items.NETHERITE_SCRAP));
    }

    public ChunkPickaxeItem(FabricItemSettings settings) {
        super(ToolMaterials.NETHERITE, 1, -2.8F, settings);
    }

    // 用于设置挖掘方向
    public static void setDirection(boolean direction) {
        diggingUp = direction;
    }

    public static Map<BlockPos, BlockState> getPreviousBlockStates() {
        Map<BlockPos, BlockState> blockStateMap = new HashMap<>();
        while (!previousBlockStates.isEmpty()) {
            Map.Entry<BlockPos, BlockState> entry = previousBlockStates.pop();
            blockStateMap.put(entry.getKey(), entry.getValue());
        }
        return blockStateMap; // 将所有方块状态返回
    }

    public static void clearPreviousBlockStates() {
        previousBlockStates.clear();
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        // 挖掘逻辑
        return executeMiningMode(stack, world, state, pos, miner);
    }

    private boolean executeMiningMode(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient() && miner instanceof LivingEntity) {
            int startX = (pos.getX() >> 4) << 4;
            int startZ = (pos.getZ() >> 4) << 4;
            int startY = pos.getY();
            // 添加缓降效果
            miner.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 20 * 30, 0));
            // 逐层破坏整个区块直到达到顶部或基岩层
            for (int y = startY; diggingUp ? y <= world.getTopY() : y >= world.getBottomY(); y = diggingUp ? y + 1 : y - 1) {
                for (int x = startX; x < startX + 16; x++) {
                    for (int z = startZ; z < startZ + 16; z++) {
                        BlockPos currentPos = new BlockPos(x, y, z);
                        BlockState currentState = world.getBlockState(currentPos);
                        Block currentBlock = currentState.getBlock();

                        if (currentBlock != Blocks.BEDROCK && !currentState.isAir()) {
                            // 记录被破坏的方块状态
                            previousBlockStates.push(new AbstractMap.SimpleEntry<>(currentPos, currentState)); // 记录方块状态
                            world.breakBlock(currentPos, true, miner); // 产生掉落物
                        }

                        // 清除周围的掉落物
                        if (currentBlock != Blocks.BEDROCK) {
                            clearDroppedItems(world, new BlockPos(x, y, z));
                        } else if (miner.isOnGround()){
                            diggingUp = !diggingUp; // 如果遇到基岩，停止挖掘并切换挖掘方向
                            return true;
                        }

                    }
                }
            }
        }
        return super.postMine(stack, world, state, pos, miner);
    }

    // 清除掉落物
    private static void clearDroppedItems(World world, BlockPos pos) {
        if (world instanceof ServerWorld serverWorld) {
            Box searchBox = new Box(
                    pos.getX() - 8, pos.getY() - 8, pos.getZ() - 8,
                    pos.getX() + 8, pos.getY() + 8, pos.getZ() + 8
            );
            List<ItemEntity> itemEntities = serverWorld.getEntitiesByClass(ItemEntity.class, searchBox, entity -> true);
            for (ItemEntity itemEntity : itemEntities) {
                if (!isMineralItem(itemEntity.getStack())) {
                    itemEntity.discard(); // 删除非矿物掉落物
                }
            }
        }
    }

    // 判断物品是否为矿物
    private static boolean isMineralItem(ItemStack itemStack) {
        for (ItemStack mineralItem : MINERAL_ITEMS) {
            if (ItemStack.areItemsEqual(itemStack, mineralItem)) {
                return true;
            }
        }
        return false;
    }
}
