package net.ace.funnyitemmod.item.custom;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Time_FreezerItem extends Item {
    public Time_FreezerItem(Settings settings) {
        super(settings);
    }
    private static boolean startStop = false;
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if(!world.isClient){
            startStop=!startStop;

            // 执行指令
            if (startStop) {
                if (user instanceof ServerPlayerEntity) {
                    MinecraftServer server = user.getServer();
                    // 获取服务器命令调度程序
                    CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();
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
                        user.sendMessage(Text.translatable("item.funny-item-mod.time_freezer.start"),true);
                        // 解析指令并获取命令源
                        ParseResults<ServerCommandSource> parseResults
                                = dispatcher.parse("tick freeze", server.getCommandSource());
                        // 执行指令
                        dispatcher.execute(parseResults);

                        // 在控制台输出提示信息
                    } catch (CommandSyntaxException e) {
                        // 指令语法异常处理
                        e.printStackTrace();
                    }
                }
            }else {
                MinecraftServer server = user.getServer();
                // 获取服务器命令调度程序
                CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();
                try {
                    // 解析指令并获取命令源
                        user.sendMessage(Text.translatable(("item.funny-item-mod.time_freezer.stop")), true);
                    ParseResults<ServerCommandSource> parseResults
                            = dispatcher.parse("tick unfreeze", server.getCommandSource());
                    // 执行指令
                    dispatcher.execute(parseResults);

                    // 在控制台输出提示信息
                } catch (CommandSyntaxException e) {
                    // 指令语法异常处理
                    e.printStackTrace();
                }
            }
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }
}
