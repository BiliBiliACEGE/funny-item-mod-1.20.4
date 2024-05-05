package net.ace.funnyitemmod.item.custom;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class Simple_wooden_axeItem extends Item {
    private BlockPos selectedBlockPos = null;
    private BlockPos selectedBlockPos2 = null;
    private String selectedBlockId = null;
    private String selectedBlockName = null;

    public Simple_wooden_axeItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }
        BlockHitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.NONE);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = hitResult.getBlockPos();
            if (selectedBlockPos == null) {
                selectedBlockPos = blockPos;
                BlockState blockState = world.getBlockState(blockPos);
                selectedBlockName = blockState.getBlock().getName().getString();
                selectedBlockId = blockState.getBlock().getTranslationKey().substring(6).replace(".", ":");
                    user.sendMessage(Text.literal("已标记点1"), true);
                user.sendMessage((Text.literal("X: " + blockPos.getX() + " " + "Y: " + blockPos.getY() + " " + "Z: " + blockPos.getZ())));
            } else {
                selectedBlockPos2 = blockPos;
                executeFillCommand(world.getServer(), user);
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    private void executeFillCommand(MinecraftServer server, PlayerEntity user) {
        CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();
        if (dispatcher == null) return;

        try {
            // 获取
            BlockState blockState1 = user.getWorld().getBlockState(selectedBlockPos);
            BlockState blockState2 = user.getWorld().getBlockState(selectedBlockPos2);
            String blockName1 = blockState1.getBlock().getName().getString();
            String blockName2 = blockState2.getBlock().getName().getString();

            String fillCommand = "fill " +
                    selectedBlockPos.getX() + " " + selectedBlockPos.getY() + " " + selectedBlockPos.getZ() + " " +
                    selectedBlockPos2.getX() + " " + selectedBlockPos2.getY() + " " + selectedBlockPos2.getZ() + " " +
                    selectedBlockId;

            // 执行
            ParseResults<ServerCommandSource> parseResults = dispatcher.parse(fillCommand, server.getCommandSource().withWorld((ServerWorld) user.getWorld()));
            dispatcher.execute(parseResults);
                user.sendMessage(Text.literal("已选择点2"), true);
                user.sendMessage(Text.literal("已成功填充方块: " + selectedBlockName + " 到区域: " + selectedBlockPos + " 到 " + selectedBlockPos2));
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }

        // 重置选择方块效果
        selectedBlockPos = null;
        selectedBlockPos2 = null;
        selectedBlockId = null;
        selectedBlockName = null;
    }
}
