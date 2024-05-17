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

public class Get_Block_StaffItem extends Item {

    private BlockPos selectedBlockPos = null;

    public Get_Block_StaffItem(Settings settings) {
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
            if (user.isSneaking()) {
                selectedBlockPos = blockPos;
                BlockState blockState = world.getBlockState(blockPos);
                String blockName = blockState.getBlock().getName().getString();
                String message = Text.translatable("item.funny-item-mod.get_block_staff.use").getString() + " " + blockName;
                user.sendMessage(Text.literal(message), true);
                user.sendMessage((Text.literal("X: " + blockPos.getX() + " " + "Y: " + blockPos.getY() + " " + "Z: " + blockPos.getZ())));
            } else if (selectedBlockPos != null) {
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
                    ParseResults<ServerCommandSource> parseResults = null;
                    if (dispatcher != null) {
                        parseResults = dispatcher.parse("setblock " + selectedBlockPos.getX() + " " + selectedBlockPos.getY() + " " + selectedBlockPos.getZ() + " minecraft:air destroy", server.getCommandSource().withWorld((ServerWorld) world));
                    }
                    // 执行指令
                    if (dispatcher != null) {
                        dispatcher.execute(parseResults);
                    }
                    String message = Text.translatable("item.funny-item-mod.get_block_staff.success").getString() + blockName ;
                    user.sendMessage(Text.literal(message), true);
                    selectedBlockPos = null;
                } catch (CommandSyntaxException e) {
                    // 指令语法异常处理
                    e.printStackTrace();
                }
            } else {
                user.sendMessage(Text.translatable("item.funny-item-mod.get_block_staff.pl"), true);
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}