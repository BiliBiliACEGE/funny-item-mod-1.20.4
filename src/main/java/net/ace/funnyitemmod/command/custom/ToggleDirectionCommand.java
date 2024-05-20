package net.ace.funnyitemmod.command.custom;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.ace.funnyitemmod.item.custom.ChunkPickaxeItem;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ToggleDirectionCommand {
    // 添加补全提供者
    private static final SuggestionProvider<ServerCommandSource> DIRECTION_SUGGESTION_PROVIDER = (context, builder) ->
            CommandSource.suggestMatching(new String[]{"up", "down"}, builder);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("toggledirection")
                .then(CommandManager.argument("direction", StringArgumentType.word())
                        // 添加补全配置
                        .suggests(DIRECTION_SUGGESTION_PROVIDER)
                        .executes(ToggleDirectionCommand::toggleDirection)));
    }

    private static int toggleDirection(CommandContext<ServerCommandSource> context) {
        String direction = StringArgumentType.getString(context, "direction");
        if (direction.equals("up")) {
            ChunkPickaxeItem.setDirection(true);
            context.getSource().sendFeedback(() -> Text.translatable("command.up"), false);
        } else if (direction.equals("down")) {
            ChunkPickaxeItem.setDirection(false);
            context.getSource().sendFeedback(() -> Text.translatable("command.down"), false);
        } else {
            context.getSource().sendFeedback(() -> Text.translatable("command.error"), false);
            return 0;
        }
        return 1;
    }
}
