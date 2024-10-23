package net.ace.funnyitemmod.command.custom;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.ace.funnyitemmod.item.custom.ChunkPickaxeItem;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public class UndoCommand {
    // This command is used to undo the last chunk pickaxe upgrade.

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("undo")
                .executes(UndoCommand::executeUndo)
        );
    }

    private static int executeUndo(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        // Retrieve the previous block states stored in ChunkPickaxeItem
        for (Map.Entry<BlockPos, BlockState> entry : ChunkPickaxeItem.getPreviousBlockStates().entrySet()) {
            BlockPos pos = entry.getKey();
            BlockState state = entry.getValue();
            // Restore the block state
            source.getWorld().setBlockState(pos, state, 3); // 3 is a flag for notifying neighbors
        }

        // Clear the stored states after restoring
        ChunkPickaxeItem.clearPreviousBlockStates();

        // Send feedback to the player
        context.getSource().sendFeedback(() -> Text.translatable("commands.funnyitemmod.undo.success"), true);
        return 1;
    }
}
