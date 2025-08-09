package net.ace.funnyitemmod.item.custom;

import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import java.util.function.Consumer;

public class Feather_ofSoaringItem extends Item {
    public Feather_ofSoaringItem(Settings settings) {
        super(settings);
    }
    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        user.getStackInHand(hand);

        if (!world.isClient() && user instanceof ServerPlayerEntity player) {

            // 只在生存/冒险模式生效
            if (!player.isCreative() && !player.isSpectator()) {
                // 切换飞行状态
                boolean newState = !isFlightEnabled(player);
                setFlightEnabled(player, newState);

                // 更新玩家能力
                player.getAbilities().allowFlying = newState;
                if (!newState) {
                    player.getAbilities().flying = false; // 关闭时取消飞行状态
                }
                player.sendAbilitiesUpdate();

                // 发送状态提示
                Text message = newState ?
                        Text.translatable("item.funny_item_mod.feather_of_soaring.enabled").styled(style -> style.withColor(0x55FF55)) :
                        Text.translatable("item.funny_item_mod.feather_of_soaring.disabled").styled(style -> style.withColor(0xFF5555));
                player.sendMessage(message, true);
            }
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        textConsumer.accept(Text.translatable("item.funny_item_mod.feather_of_soaring.tooltip").styled(style -> style.withColor(0xAAAAAA)));
        textConsumer.accept(Text.translatable("item.funny_item_mod.feather_of_soaring.tooltip2").styled(style -> style.withColor(0xFFFF55)));
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }

    // 辅助方法：检查飞行状态
    public static boolean isFlightEnabled(PlayerEntity player) {
        return player.getAbilities().allowFlying;
    }

    // 辅助方法：设置飞行状态
    public static void setFlightEnabled(PlayerEntity player, boolean enabled) {
        player.getAbilities().allowFlying = enabled;
    }
}
