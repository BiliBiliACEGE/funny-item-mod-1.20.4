package net.ace.funnyitemmod.command;

import com.mojang.brigadier.CommandDispatcher;
import net.ace.funnyitemmod.command.custom.ToggleCommand;
import net.ace.funnyitemmod.command.custom.UndoCommand;
import net.minecraft.server.command.ServerCommandSource;

import java.util.ArrayList;
import java.util.List;

public class ModCommands {
    private static final List<CommandTemplate> COMMANDS = new ArrayList<>();

    static {
        COMMANDS.add(new ToggleCommand());
        COMMANDS.add(new UndoCommand());
    }

    public static void registerModCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        COMMANDS.forEach(command -> command.register(dispatcher));
        System.out.println("模组指令注册");
    }
}
