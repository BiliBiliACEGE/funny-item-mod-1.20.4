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

public class Fill_BlockItem extends Item {
    private BlockPos selectedBlockPos = null;
    private String selectedBlockId = null;
    private String selectedBlockName = null;
    public Fill_BlockItem(Settings settings) {
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
            String locale = user.getGameProfile().getProperties().get("locale").toString();
            if (user.isSneaking()) {
                selectedBlockPos = blockPos;
                BlockState blockState = world.getBlockState(blockPos);
                selectedBlockName = blockState.getBlock().getName().getString();
                selectedBlockId = blockState.getBlock().getTranslationKey().substring(6).replace(".", ":");
                if (locale.equals("zh_cn")) {
                    user.sendMessage(Text.literal("已标记方块: " + selectedBlockName), true);
                }else {
                    user.sendMessage(Text.literal("Selected block: " + selectedBlockName), true);
                }
                user.sendMessage((Text.literal("X: " + blockPos.getX() + " " + "Y: " + blockPos.getY() + " " + "Z: " + blockPos.getZ())));
            } else if (selectedBlockPos != null && selectedBlockId != null && selectedBlockName != null) {
                MinecraftServer server = world.getServer();
                CommandDispatcher<ServerCommandSource> dispatcher = null;
                if (server != null) {
                    dispatcher = server.getCommandManager().getDispatcher();
                }
                try {
                    // 解析指令并获取命令源
                    ParseResults<ServerCommandSource> parseResults = null;
                    if (dispatcher != null) {
                        parseResults = dispatcher.parse("gamerule sendCommandFeedback false", server.getCommandSource().withWorld((ServerWorld) world));
                    }
                    // 执行指令
                    if (dispatcher != null) {
                        dispatcher.execute(parseResults);
                    }

                    // 在控制台输出提示信息
                } catch (CommandSyntaxException e) {
                    // 指令语法异常处理
                    e.printStackTrace();
                }
                try {
                    // 解析指令并获取命令源
                    BlockState blockState = world.getBlockState(blockPos);
                    String blockName = blockState.getBlock().getName().getString();
                    //String blockId = blockState.getBlock().getTranslationKey().substring(6).replace(".", ":");
                    ParseResults<ServerCommandSource> parseResults = null;
                    if (dispatcher != null) {
                        parseResults = dispatcher.parse("setblock " + blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ() + " " + selectedBlockId, server.getCommandSource().withWorld((ServerWorld) world));
                    }
                    // 执行指令
                    if (dispatcher != null) {
                        dispatcher.execute(parseResults);
                    }
                    if (locale.equals("zh_cn")) {
                        user.sendMessage(Text.literal("已成功将: " + selectedBlockName + "填充到: " + blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ()), true);
                        user.sendMessage((Text.literal("目标方块ID: " + selectedBlockId + " " + "目标方块名称: " + selectedBlockName)));
                    }else {
                        user.sendMessage(Text.literal("Successfully filled: " + selectedBlockName + " to: " + blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ()), true);
                        user.sendMessage((Text.literal("Target block ID: " + selectedBlockId + " " + "Target block name: " + selectedBlockName)));
                    }
                    //重置选择
                    selectedBlockPos = null;
                    selectedBlockId  = null;
                    selectedBlockName = null;
                } catch (CommandSyntaxException e) {
                    // 指令语法异常处理
                    e.printStackTrace();
                }
            } else {
                if (locale.equals("zh_cn")) {
                    user.sendMessage(Text.literal("请先潜行加右键选择方块！"), true);
                }else {
                    user.sendMessage(Text.literal("Please sneak and right-click to select a block!"), true);
                }
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
