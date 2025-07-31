package net.ace.funnyitemmod.block.custom;

import com.mojang.serialization.MapCodec;
import net.ace.funnyitemmod.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.*;
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

import java.util.ArrayList;
import java.util.List;

public class RuneAnvilBlock extends HorizontalFacingBlock implements Inventory, RuneAnvilBlockOnUse {
    private final SimpleInventory inventory = new SimpleInventory(1);

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
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        Direction direction = context.getHorizontalPlayerFacing().getOpposite();
        return this.getDefaultState().with(FACING, direction);
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return null;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, Hand hand) {
        if (world.isClient) {
            return ActionResult.CONSUME;
        }

        ItemStack heldItem = player.getStackInHand(hand);

        if (heldItem.getItem() == ModItems.Hammer) {
            if (!inventory.isEmpty()) {
                ItemStack item = inventory.getStack(0);
                if (item.getItem() == Items.ENCHANTED_BOOK) {
                    ItemStack book = inventory.removeStack(0);
                    book = upgradeEnchantmentLevel(book);
                    world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), book));
                    world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                } else if (isUpgradeable(item)) {
                    ItemStack upgradedItem = upgradeEnchantmentLevel(item);
                    inventory.setStack(0, upgradedItem);
                    world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                }
            }
        } else if (heldItem.isEmpty()) {
            if (!inventory.isEmpty()) {
                ItemStack item = inventory.removeStack(0);
                player.setStackInHand(hand, item);
                world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_FALL, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
        } else if (heldItem.getItem() == Items.ENCHANTED_BOOK || isUpgradeable(heldItem)) {
            if (inventory.isEmpty()) {
                inventory.setStack(0, heldItem.split(1));
                world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
        }

        return ActionResult.SUCCESS;
    }

    private boolean isUpgradeable(ItemStack item) {
        // 在 1.21.7 中推荐用组件判断，而不是类型判断
        return item.contains(DataComponentTypes.ENCHANTMENTS) ||
                item.getItem() instanceof RangedWeaponItem ||
                item.getItem() instanceof BowItem ||
                item.getItem() instanceof TridentItem;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        return switch (direction) {
            case SOUTH -> SHAPE_SOUTH;
            case EAST -> SHAPE_EAST;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }

    // Inventory 接口实现
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
        // no-op
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    // 通用附魔升级（支持书/护甲/武器）
    private ItemStack upgradeEnchantmentLevel(ItemStack item) {
        List<EnchantmentLevelEntry> entries = (List<EnchantmentLevelEntry>) item.getOrDefault(DataComponentTypes.ENCHANTMENTS, List.of());
        if (entries.isEmpty() && item.getItem() == Items.ENCHANTED_BOOK) {
            entries = (List<EnchantmentLevelEntry>) item.getOrDefault(DataComponentTypes.STORED_ENCHANTMENTS, List.of());
        }
        if (entries.isEmpty()) {
            return item;
        }

        List<EnchantmentLevelEntry> upgraded = new ArrayList<>();
        for (EnchantmentLevelEntry entry : entries) {
            upgraded.add(new EnchantmentLevelEntry(entry.enchantment(), entry.level() + 1));
        }

        ItemStack upgradedItem;
        if (item.getItem() == Items.ENCHANTED_BOOK) {
            upgradedItem = new ItemStack(Items.ENCHANTED_BOOK);
            ItemEnchantmentsComponent emptyStored = item.getOrDefault(DataComponentTypes.STORED_ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
            ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(emptyStored);
            for (EnchantmentLevelEntry entry : upgraded) {
                builder.add(entry.enchantment(), entry.level());
            }
            ItemEnchantmentsComponent newComponent = builder.build();
            upgradedItem.set(DataComponentTypes.STORED_ENCHANTMENTS, newComponent);
        } else {
            upgradedItem = new ItemStack(item.getItem());
            ItemEnchantmentsComponent empty = item.getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
            ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(empty);
            for (EnchantmentLevelEntry entry : upgraded) {
                builder.add(entry.enchantment(), entry.level());
            }
            ItemEnchantmentsComponent newComponent = builder.build();
            upgradedItem.set(DataComponentTypes.ENCHANTMENTS, newComponent);
        }

        return upgradedItem;
    }

}
