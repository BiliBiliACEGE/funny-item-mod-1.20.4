package net.ace.funnyitemmod.block.custom;

import com.mojang.serialization.MapCodec;
import net.ace.funnyitemmod.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RuneAnvilBlock extends HorizontalFacingBlock implements Inventory {
    private final SimpleInventory inventory = new SimpleInventory(1);

    // 定义不同方向的碰撞箱
    private static final VoxelShape SHAPE_NORTH = VoxelShapes.cuboid(0, 0.01, 0.25, 1, 0.81, 0.75);
    private static final VoxelShape SHAPE_SOUTH = VoxelShapes.cuboid(0, 0.01, 0.25, 1, 0.81, 0.75);
    private static final VoxelShape SHAPE_EAST = VoxelShapes.cuboid(0.25, 0.01, 0, 0.75, 0.81, 1);
    private static final VoxelShape SHAPE_WEST = VoxelShapes.cuboid(0.25, 0.01, 0, 0.75, 0.81, 1);

    public RuneAnvilBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return this.getDefaultState().with(FACING, context.getPlayerLookDirection().getOpposite());
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return null;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getStackInHand(hand);

        if (!world.isClient) {
            if (heldItem.getItem() == ModItems.Hammer) {
                if (!inventory.isEmpty()) {
                    ItemStack item = inventory.getStack(0);
                    if (item.getItem() == Items.ENCHANTED_BOOK) {
                        ItemStack book = inventory.removeStack(0);
                        book = upgradeEnchantmentLevel(book);
                        world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), book));
                        world.playSound(player, pos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    } else if (item.getItem() instanceof ArmorItem || item.getItem() instanceof SwordItem) {
                        ItemStack upgradedItem = item.getItem() instanceof ArmorItem ?
                                upgradeArmorEnchantmentLevel(item) :
                                upgradeWeaponEnchantmentLevel(item);
                        inventory.setStack(0, upgradedItem);
                        world.playSound(player, pos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    }
                }
            } else if (heldItem.isEmpty()) {
                if (!inventory.isEmpty()) {
                    ItemStack item = inventory.removeStack(0);
                    player.setStackInHand(hand, item);
                    world.playSound(player, pos, SoundEvents.BLOCK_ANVIL_FALL, SoundCategory.BLOCKS, 1.0f, 1.0f);
                }
            } else if (heldItem.getItem() == Items.ENCHANTED_BOOK || heldItem.getItem() instanceof ArmorItem || heldItem.getItem() instanceof SwordItem) {
                if (inventory.isEmpty()) {
                    inventory.setStack(0, heldItem.split(1));
                    world.playSound(player, pos, SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 1.0f, 1.0f);
                }
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        // 根据方块的朝向返回不同的碰撞箱
        Direction direction = state.get(FACING);
        return switch (direction) {
            case NORTH -> SHAPE_NORTH;
            case SOUTH -> SHAPE_SOUTH;
            case EAST -> SHAPE_EAST;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }

    // Inventory methods
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
        return inventory.getStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return inventory.removeStack(slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return inventory.removeStack(slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.setStack(slot, stack);
    }

    @Override
    public void markDirty() {
        // Do nothing
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    private ItemStack upgradeEnchantmentLevel(ItemStack book) {
        NbtCompound tag = book.getOrCreateNbt();
        if (tag != null) {
            NbtList enchantments = tag.getList("StoredEnchantments", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < enchantments.size(); i++) {
                NbtCompound enchantment = enchantments.getCompound(i);
                int level = enchantment.getInt("lvl") + 1;
                enchantment.putInt("lvl", level);
            }
        }
        return book;
    }

    private ItemStack upgradeArmorEnchantmentLevel(ItemStack armor) {
        NbtCompound tag = armor.getOrCreateNbt();
        if (tag != null) {
            NbtList enchantments = tag.getList("Enchantments", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < enchantments.size(); i++) {
                NbtCompound enchantment = enchantments.getCompound(i);
                int level = enchantment.getInt("lvl") + 1;
                enchantment.putInt("lvl", level);
            }
        }
        return armor;
    }

    private ItemStack upgradeWeaponEnchantmentLevel(ItemStack weapon) {
        NbtCompound tag = weapon.getOrCreateNbt();
        if (tag != null) {
            NbtList enchantments = tag.getList("Enchantments", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < enchantments.size(); i++) {
                NbtCompound enchantment = enchantments.getCompound(i);
                int level = enchantment.getInt("lvl") + 1;
                enchantment.putInt("lvl", level);
            }
        }
        return weapon;
    }
}
