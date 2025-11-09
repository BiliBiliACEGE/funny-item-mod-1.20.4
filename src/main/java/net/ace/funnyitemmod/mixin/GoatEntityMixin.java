package net.ace.funnyitemmod.mixin;

import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GoatEntity.class)
public class GoatEntityMixin {
    // 玩家使用剪刀右键山羊，获取山羊角
    @Inject(at = @At("HEAD"), method = "interactMob", cancellable = true)
    public void interact(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        GoatEntity goat = (GoatEntity)(Object)this;
        ItemStack stack = player.getStackInHand(hand);

        // 检查是否是剪刀且山羊不是幼崽
        if (stack.getItem() == Items.SHEARS && !goat.isBaby()) {
            // 尝试掉落山羊角
            boolean droppedHorn = goat.dropHorn();

            if (droppedHorn) {
                // 消耗剪刀耐久度
                stack.damage(1, player);

                // 返回成功，但不取消后续处理
                cir.setReturnValue(ActionResult.SUCCESS);
            }
        }
    }
}