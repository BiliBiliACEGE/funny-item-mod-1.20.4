package net.ace.funnyitemmod.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;

public interface CommandTemplate {
    void register(CommandDispatcher<ServerCommandSource> dispatcher); // 所有命令类必须实现注册方法
}
