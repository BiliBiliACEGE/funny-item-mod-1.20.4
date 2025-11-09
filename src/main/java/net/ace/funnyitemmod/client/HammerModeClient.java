package net.ace.funnyitemmod.client;

import net.ace.funnyitemmod.item.ModItems;
import net.ace.funnyitemmod.network.HammerModePayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public final class HammerModeClient {

    private static final KeyBinding SCROLL_UP = KeyBindingHelper.registerKeyBinding(
            new KeyBinding("key.funnyitemmod.hammer_up", InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_UNKNOWN, "category.funnyitemmod"));
    private static final KeyBinding SCROLL_DOWN = KeyBindingHelper.registerKeyBinding(
            new KeyBinding("key.funnyitemmod.hammer_down", InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_UNKNOWN, "category.funnyitemmod"));

    public static void init() {
        /* 滚轮监听（用键绑定代替 Mouse.getScrollDelta） */
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || !client.player.isSneaking()) return;
            if (!client.player.getMainHandStack().isOf(ModItems.Rune_Smith_Hammer)) return;

            int delta = 0;
            while (SCROLL_UP.wasPressed()) delta++;
            while (SCROLL_DOWN.wasPressed()) delta--;
            if (delta == 0) return;

            int now = getMode(client.player);
            int next = Math.min(Math.max(now + delta, 1), 255);
            if (next != now) {
                ClientPlayNetworking.send(new HammerModePayload(next));
                System.out.println("Client sending raw mode: " + next);
                client.player.sendMessage(
                        Text.translatable("gui.funnyitemmod.hammer_times", next)
                                .formatted(Formatting.YELLOW),
                        true);  // 显示在快捷栏上方
            }
        });

        /* HUD 绘制（1.21 新签名） */
        HudRenderCallback.EVENT.register((DrawContext context, RenderTickCounter counter) -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null) return;
            if (!mc.player.getMainHandStack().isOf(ModItems.Rune_Smith_Hammer)) return;

            int mode = getMode(mc.player);
            Text text = Text.translatable("gui.funnyitemmod.hammer_times", mode)
                    .formatted(Formatting.YELLOW);

            int x = context.getScaledWindowWidth() / 2 - mc.textRenderer.getWidth(text) / 2;
            int y = context.getScaledWindowHeight() - 35;
            context.drawText(mc.textRenderer, text, x, y, 0xFFFFFF, true);
        });
    }

    /* 客户端本地缓存（仅用于显示） */
    private static int clientMode = 1;
    public static int getMode(net.minecraft.client.network.ClientPlayerEntity player) {
        return clientMode;
    }
    public static void setMode(int mode) {
        clientMode = mode;
    }
}