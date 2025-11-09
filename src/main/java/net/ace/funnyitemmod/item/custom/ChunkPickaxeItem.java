package net.ace.funnyitemmod.item.custom;

import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.*;

public class ChunkPickaxeItem extends Item {
    private static final Set<ItemStack> MINERAL_ITEMS = new HashSet<>();
    private static final String DIGGING_UP_KEY = "DiggingUp";
    private static final String MINING_CHUNK_KEY = "MiningChunk";

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

    public ChunkPickaxeItem(Settings settings) {
        super(settings.pickaxe(ToolMaterial.NETHERITE, 1, 1));
    }

    // 从物品组件中获取挖掘方向
    public static boolean getDirection(ItemStack stack) {
        NbtComponent nbtComponent = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (nbtComponent != null) {
            NbtCompound nbt = nbtComponent.copyNbt();
            return nbt.contains(DIGGING_UP_KEY) && nbt.getBoolean(DIGGING_UP_KEY, false);
        }
        return false; // 默认向下挖掘
    }

    // 设置挖掘方向（存储到物品组件中）
    public static void setDirection(ItemStack stack, boolean direction) {
        NbtCompound nbt = getOrCreateNbt(stack);
        nbt.putBoolean(DIGGING_UP_KEY, direction);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
    }

    // 检查是否正在挖掘中
    private static boolean isMining(ItemStack stack) {
        NbtComponent nbtComponent = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (nbtComponent != null) {
            NbtCompound nbt = nbtComponent.copyNbt();
            return nbt.contains(MINING_CHUNK_KEY) && nbt.getBoolean(MINING_CHUNK_KEY, false);
        }
        return false;
    }

    // 设置挖掘状态
    private static void setMining(ItemStack stack, boolean mining) {
        NbtCompound nbt = getOrCreateNbt(stack);
        nbt.putBoolean(MINING_CHUNK_KEY, mining);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
    }

    // 获取或创建NBT数据
    private static NbtCompound getOrCreateNbt(ItemStack stack) {
        NbtComponent nbtComponent = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (nbtComponent != null) {
            return nbtComponent.copyNbt();
        }
        return new NbtCompound();
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        // 防止递归调用
        if (isMining(stack)) {
            return true;
        }

        // 执行挖掘逻辑
        return executeMiningMode(stack, world, pos, miner);
    }

    private boolean executeMiningMode(ItemStack stack, World world, BlockPos pos, LivingEntity miner) {
        if (!world.isClient() && miner instanceof LivingEntity) {
            // 设置挖掘标记防止递归
            setMining(stack, true);

            try {
                int startX = (pos.getX() >> 4) << 4;
                int startZ = (pos.getZ() >> 4) << 4;
                int startY = pos.getY();
                boolean diggingUp = getDirection(stack); // 从当前物品获取方向

                // 添加缓降效果
                miner.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 20 * 30, 0));

                // 获取世界高度范围
                int minY = world.getBottomY();
                int maxY = minY + world.getHeight() - 1; // 最大Y坐标

                // 逐层破坏整个区块直到达到顶部或基岩层
                for (int y = startY; diggingUp ? y <= maxY : y >= minY; y = diggingUp ? y + 1 : y - 1) {
                    boolean bedrockEncountered = false;

                    for (int x = startX; x < startX + 16; x++) {
                        for (int z = startZ; z < startZ + 16; z++) {
                            BlockPos currentPos = new BlockPos(x, y, z);
                            BlockState currentState = world.getBlockState(currentPos);

                            if (currentState.getBlock() == Blocks.BEDROCK) {
                                bedrockEncountered = true;
                                continue; // 跳过基岩
                            }

                            if (!currentState.isAir()) {
                                world.breakBlock(currentPos, true, miner); // 产生掉落物
                            }
                        }
                    }

                    // 清除本层的掉落物
                    clearDroppedItems(world, new BlockPos(startX, y, startZ));

                    // 如果遇到基岩层，切换方向并停止挖掘
                    if (bedrockEncountered && miner.isOnGround()) {
                        setDirection(stack, !diggingUp); // 切换方向并保存到当前物品
                        break;
                    }
                }
            } finally {
                // 清除挖掘标记
                setMining(stack, false);
            }
        }
        return true;
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