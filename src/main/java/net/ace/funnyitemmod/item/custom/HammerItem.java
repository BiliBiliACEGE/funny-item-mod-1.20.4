package net.ace.funnyitemmod.item.custom;

import net.ace.funnyitemmod.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.minecraft.block.AnvilBlock.FACING;

public class HammerItem extends Item {
    public HammerItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        Block block = world.getBlockState(pos).getBlock();

        // 如果右键点击的是铁砧（包括所有损坏程度的铁砧）
        if (block == Blocks.ANVIL || block == Blocks.CHIPPED_ANVIL || block == Blocks.DAMAGED_ANVIL) {
            // 播放铁砧的声音效果
            world.playSound(player, pos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1.0f, 1.0f);

            // 替换为你自定义的铁砧
            world.setBlockState(pos, ModBlocks.Rune_anvil.getDefaultState().with(FACING, context.getHorizontalPlayerFacing().getOpposite()));

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}
