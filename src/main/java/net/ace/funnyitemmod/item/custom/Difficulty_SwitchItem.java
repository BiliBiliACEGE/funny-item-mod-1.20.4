package net.ace.funnyitemmod.item.custom;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Random;

public class Difficulty_SwitchItem extends Item {
    private static final String[] DIFFICULTIES = {"peaceful", "easy", "normal", "hard"};
    private static boolean isDifficultySwitching = false;

    public Difficulty_SwitchItem(Settings settings) {
        super(settings);
    }

    @Override
   public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
    ItemStack itemStack = user.getStackInHand(hand);
    if (!world.isClient) {
        MinecraftServer server = user.getServer();
        if (server != null) {
            ServerCommandSource source = server.getCommandSource().withEntity(user);
            CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();
            int currentDifficultyIndex = 0;
            if (!isDifficultySwitching) {
                // 开始循环以切换难度
                isDifficultySwitching = true;
                String locale = user.getGameProfile().getProperties().get("locale").toString();
                // 在聊天栏中显示当前难度
                Random random = new Random();
                currentDifficultyIndex = random.nextInt(DIFFICULTIES.length);
                String command = String.format("difficulty %s", DIFFICULTIES[currentDifficultyIndex]);
                try {
                    dispatcher.execute(command, source);
                    if (locale.equals("zh_cn")) {
                        user.sendMessage(Text.of("当前难度: " + DIFFICULTIES[currentDifficultyIndex]), true);
                    }else
                        user.sendMessage(Text.of("Current difficulty: " + DIFFICULTIES[currentDifficultyIndex]), true);
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                }
            } else {
                // 执行当前随机难度
                String command = String.format("difficulty %s", DIFFICULTIES[currentDifficultyIndex]);
                try {
                    dispatcher.execute(command, source);
                    user.sendMessage(Text.of("当前难度: " + DIFFICULTIES[currentDifficultyIndex]), true);
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                }
                isDifficultySwitching = false;
            }
        }
    }
    return TypedActionResult.success(itemStack);
}
}
