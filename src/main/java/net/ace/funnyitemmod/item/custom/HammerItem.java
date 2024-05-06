package net.ace.funnyitemmod.item.custom;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.ace.funnyitemmod.block.ModBlocks;
import net.ace.funnyitemmod.block.custom.RuneAnvilBlock;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HammerItem extends Item {
    public HammerItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();

        // 如果右键点击的是原版铁砧
        if (world.getBlockState(pos).isOf(Blocks.ANVIL)) {
            // 播放铁砧的声音效果
            world.playSound(player, pos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1.0f, 1.0f);

            // 替换为你自定义的铁砧
            world.setBlockState(pos, ModBlocks.Rune_anvil.getDefaultState());

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

}