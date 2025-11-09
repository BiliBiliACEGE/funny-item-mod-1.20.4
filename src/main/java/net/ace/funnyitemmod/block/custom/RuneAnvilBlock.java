package net.ace.funnyitemmod.block.custom;

import com.mojang.serialization.MapCodec;
import net.ace.funnyitemmod.item.ModItems;
import net.ace.funnyitemmod.util.HammerModeManager;
import net.minecraft.block.*;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
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
        Direction direction = context.getPlayerLookDirection().getOpposite();
        if (direction.getAxis() == Direction.Axis.Y) {
            return this.getDefaultState().with(FACING, Direction.NORTH);
        } else {
            return this.getDefaultState().with(FACING, direction);
        }
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return null;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;

        Hand hand = player.getActiveHand();

        ItemStack held = player.getStackInHand(hand);

        /* ===== 锤子升级逻辑 ===== */
        if (held.isOf(ModItems.Rune_Smith_Hammer)) {
            if (inventory.isEmpty()) return ActionResult.PASS;

            ItemStack item = inventory.getStack(0);
            ItemStack out = item.copy();

            ComponentType<ItemEnchantmentsComponent> comp =
                    out.isOf(Items.ENCHANTED_BOOK)
                            ? DataComponentTypes.STORED_ENCHANTMENTS
                            : DataComponentTypes.ENCHANTMENTS;

            int times = HammerModeManager.get((ServerPlayerEntity) player);
            int actual = 0;
            for (int i = 0; i < times; i++) {
                ItemStack tmp = upgradeEnchantmentsOnStack(out, comp);
                if (ItemStack.areEqual(tmp, out)) break; // 升不动就停
                out = tmp;
                actual++;
            }

            if (!ItemStack.areEqual(out, item)) {
                inventory.removeStack(0);
                world.spawnEntity(new ItemEntity(world,
                        pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, out));
                world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_USE,
                        SoundCategory.BLOCKS, 1.0f, 1.0f);

                // 提示实际升级次数
                player.sendMessage(
                        Text.literal("实际升级了 " + actual + " 次")
                                .formatted(Formatting.GREEN), true);
                return ActionResult.CONSUME;
            }
            return ActionResult.PASS;
        }

        /* ===== 空手取物 / 放置物品 ===== */
        if (held.isEmpty()) {
            if (!inventory.isEmpty()) {
                player.getInventory().offerOrDrop(inventory.removeStack(0));
                world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP,
                        SoundCategory.BLOCKS, 0.5f, 1.0f);
                return ActionResult.CONSUME;
            }
        } else if (held.isOf(Items.ENCHANTED_BOOK) || held.isEnchantable() || held.hasEnchantments()) {
            if (inventory.isEmpty()) {
                inventory.setStack(0, held.split(1));
                world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_LAND,
                        SoundCategory.BLOCKS, 0.5f, 1.0f);
                return ActionResult.CONSUME;
            }
        }

        return ActionResult.PASS;
    }

    private ItemStack upgradeEnchantmentsOnStack(ItemStack stack, ComponentType<ItemEnchantmentsComponent> componentType) {
        ItemEnchantmentsComponent currentEnchantments = stack.get(componentType);
        if (currentEnchantments == null || currentEnchantments.isEmpty()) {
            return stack;
        }
        ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);
        for (RegistryEntry<Enchantment> enchantmentEntry : currentEnchantments.getEnchantments()) {
            int currentLevel = currentEnchantments.getLevel(enchantmentEntry);
            // 添加附魔等级上限检查（不超过255级）
            builder.add(enchantmentEntry, Math.min(currentLevel + 1, 255));
        }
        ItemStack result = stack.copyWithCount(1);
        result.set(componentType, builder.build());
        return result;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case NORTH -> SHAPE_NORTH;
            case SOUTH -> SHAPE_SOUTH;
            case EAST -> SHAPE_EAST;
            case WEST -> SHAPE_WEST;
            default -> VoxelShapes.fullCube();
        };
    }

    // --- Inventory 接口的完整实现 ---

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
        inventory.markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    @Override
    public void clear() {
        inventory.clear();
    }
}