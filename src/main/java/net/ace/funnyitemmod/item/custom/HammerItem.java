package net.ace.funnyitemmod.item.custom;

import net.ace.funnyitemmod.block.ModBlocks;
import net.ace.funnyitemmod.block.custom.RuneAnvilBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class HammerItem extends Item {
    public HammerItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        // 如果右键点击的是 RuneAnvilBlock，则不消耗锤子
        if (context.getWorld().getBlockState(context.getBlockPos()).isOf(ModBlocks.Rune_anvil)) {
            return ActionResult.SUCCESS; // 返回 SUCCESS 表示操作成功，但不会消耗物品
        }
        // 否则，执行默认操作（消耗物品）
        return null;
    }
}