package net.ace.funnyitemmod.block.custom;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface RuneAnvilBlockOnUse {
    ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, Hand hand);
}
