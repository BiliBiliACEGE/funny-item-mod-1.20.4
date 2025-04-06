package net.ace.funnyitemmod.command.custom;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.ace.funnyitemmod.item.custom.ChunkPickaxeItem;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ToggleCommand {
    // 提供自动补全的提示
    private static final SuggestionProvider<ServerCommandSource> TOGGLE_SUGGESTION_PROVIDER = (context, builder) ->
            CommandSource.suggestMatching(new String[]{"direction", "mode"}, builder);

    private static final SuggestionProvider<ServerCommandSource> DIRECTION_SUGGESTION_PROVIDER = (context, builder) ->
            CommandSource.suggestMatching(new String[]{"up", "down"}, builder);


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        // 注册合并后的 toggle 指令
        dispatcher.register(CommandManager.literal("toggle")
                .then(CommandManager.argument("type", StringArgumentType.word())
                        .suggests(TOGGLE_SUGGESTION_PROVIDER)
                        .then(CommandManager.argument("value", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    String type = StringArgumentType.getString(context, "type");
                                    if (type.equals("direction")) {
                                        return DIRECTION_SUGGESTION_PROVIDER.getSuggestions(context, builder);
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(ToggleCommand::executeToggle)
                        )
                )
        );
    }

    // 统一处理 toggle 逻辑
    private static int executeToggle(CommandContext<ServerCommandSource> context) {
        String type = StringArgumentType.getString(context, "type");
        String value = StringArgumentType.getString(context, "value");

        if (type.equals("direction")) {
            return toggleDirection(context, value);
        }
         else {
            context.getSource().sendFeedback(() -> Text.translatable("command.error"), false);
            return 0;
        }
    }

    // 切换挖掘方向的逻辑
    private static int toggleDirection(CommandContext<ServerCommandSource> context, String value) {
        if (value.equals("up")) {
            ChunkPickaxeItem.setDirection(true);
            context.getSource().sendFeedback(() -> Text.translatable("command.up"), false);
        } else if (value.equals("down")) {
            ChunkPickaxeItem.setDirection(false);
            context.getSource().sendFeedback(() -> Text.translatable("command.down"), false);
        } else {
            context.getSource().sendFeedback(() -> Text.translatable("command.error"), false);
            return 0;
        }
        return 1;
    }
}
