package net.ace.funnyitemmod;

import net.ace.funnyitemmod.entity.ModEntities;
import net.ace.funnyitemmod.item.ModItems;
import net.ace.funnyitemmod.renderer.CloneBallEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class FunnyitemModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.println("客户端初始化，注册CloneBallEntity渲染器");
        EntityRendererRegistry.register(ModEntities.CLONE_PROJECTILE, CloneBallEntityRenderer::new);
    }
}
