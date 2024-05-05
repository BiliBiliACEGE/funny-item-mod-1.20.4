package net.ace.funnyitemmod.item.custom;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class CloneItem extends Item {
    private BlockPos selectedBlockPos1 = null;
    private BlockPos selectedBlockPos2 = null;
    private BlockPos selectedBlockPos3 = null;

    public CloneItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }

        BlockHitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.NONE);
        BlockPos blockPos = hitResult.getBlockPos();

        if (selectedBlockPos1 == null) {
            selectedBlockPos1 = blockPos;
            user.sendMessage(Text.literal("已标记点1"), true);
            user.sendMessage((Text.literal("X: " + blockPos.getX() + " Y: " + blockPos.getY() + " Z: " + blockPos.getZ())));
        } else if (selectedBlockPos2 == null) {
            selectedBlockPos2 = blockPos;
            user.sendMessage(Text.literal("已标记点2"), true);
            user.sendMessage((Text.literal("X: " + blockPos.getX() + " Y: " + blockPos.getY() + " Z: " + blockPos.getZ())));
        } else {
            selectedBlockPos3 = blockPos;
            executeFillCommand(world.getServer(), user);

        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    private void executeFillCommand(MinecraftServer server, PlayerEntity user) {
        CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();
        if (dispatcher == null) return;

        try {
            // 解析指令并获取命令源
            ParseResults<ServerCommandSource> parseResults
                    = dispatcher.parse("gamerule sendCommandFeedback false", server.getCommandSource());
            // 执行指令
            dispatcher.execute(parseResults);

            // 在控制台输出提示信息
        } catch (CommandSyntaxException e) {
            // 指令语法异常处理
            e.printStackTrace();
        }

        try {
            // 解析指令并获取命令源
            ParseResults<ServerCommandSource> parseResults
                    = dispatcher.parse("gamerule commandModificationBlockLimit 999999999", server.getCommandSource());
            // 执行指令
            dispatcher.execute(parseResults);

            // 在控制台输出提示信息
        } catch (CommandSyntaxException e) {
            // 指令语法异常处理
            e.printStackTrace();
        }

        try {
            String fillCommand = "clone " +
                    selectedBlockPos1.getX() + " " + selectedBlockPos1.getY() + " " + selectedBlockPos1.getZ() + " " +
                    selectedBlockPos2.getX() + " " + selectedBlockPos2.getY() + " " + selectedBlockPos2.getZ() + " " +
                    selectedBlockPos3.getX() + " " + selectedBlockPos3.getY() + " " + selectedBlockPos3.getZ() + " masked normal";

            ParseResults<ServerCommandSource> parseResults = dispatcher.parse(fillCommand, server.getCommandSource().withWorld((ServerWorld) user.getWorld()));
            dispatcher.execute(parseResults);
            user.sendMessage(Text.literal("已成功克隆方块到区域: " + selectedBlockPos3), true);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            user.sendMessage(Text.literal("克隆方块时发生错误，请检查选择的区域是否有效"), true);
        }

        // 重置选择方块效果
        selectedBlockPos1 = null;
        selectedBlockPos2 = null;
        selectedBlockPos3 = null;
    }
}
