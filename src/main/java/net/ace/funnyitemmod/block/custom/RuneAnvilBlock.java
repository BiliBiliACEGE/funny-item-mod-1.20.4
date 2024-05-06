package net.ace.funnyitemmod.block.custom;

import net.ace.funnyitemmod.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;


public class RuneAnvilBlock extends Block implements Inventory {
    private final SimpleInventory inventory = new SimpleInventory(1); // 使用 SimpleInventory
    public RuneAnvilBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getStackInHand(hand);

        if (!world.isClient) {
            if (heldItem.getItem() == ModItems.Hammer) { // 检查是否手持特定物品
                // 玩家手中是特定物品，尝试取出方块中的书
                if (!inventory.isEmpty()) {
                    ItemStack book = inventory.removeStack(0);
                    // 升级附魔书等级
                    book = upgradeEnchantmentLevel(book);
                    // 将取出的附魔书作为掉落物掉落到地面上
                    world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), book));
                    world.playSound(player, pos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                }
            } else if (heldItem.isEmpty()) { // 玩家手持空手
                // 尝试从方块中取出书
                if (!inventory.isEmpty()) {
                    ItemStack book = inventory.removeStack(0);
                    player.setStackInHand(hand, book);
                    world.playSound(player, pos, SoundEvents.BLOCK_ANVIL_FALL, SoundCategory.BLOCKS, 1.0f, 1.0f);
                }
            } else if (heldItem.getItem() == Items.ENCHANTED_BOOK) {
                // 玩家手中是附魔书，尝试放入方块
                if (inventory.isEmpty()) {
                    inventory.setStack(0, heldItem.split(1)); // 不再备份附魔书，而是直接放入方块
                    world.playSound(player, pos, SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 1.0f, 1.0f);
                }
            }
        }
        return ActionResult.SUCCESS;
    }
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0f, 0.3f, 0f, 1.0f, 1f, 1.0f);
    }

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return null;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return null;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return null;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {

    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return false;
    }

    @Override
    public void clear() {

    }
    private ItemStack upgradeEnchantmentLevel(ItemStack book) {
        // 获取附魔书的 NBT 数据
        NbtCompound tag = book.getNbt();
        if (tag != null) {
            // 获取附魔列表
            NbtList enchantments = tag.getList("StoredEnchantments", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < enchantments.size(); i++) {
                NbtCompound enchantment = enchantments.getCompound(i);
                // 获取附魔等级并加 1
                int level = enchantment.getInt("lvl") + 1;
                enchantment.putInt("lvl", level);
            }
        } else {
            // 如果没有 NBT 数据，则创建新的
            tag = new NbtCompound();
            book.setNbt(tag);
        }
        return book;
    }
}
