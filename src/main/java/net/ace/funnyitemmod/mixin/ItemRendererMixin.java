package net.ace.funnyitemmod.mixin;

import net.ace.funnyitemMod;
import net.ace.funnyitemmod.block.ModBlocks;
import net.ace.funnyitemmod.item.ModItems;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useModel(BakedModel value, ItemStack itemStack, ModelTransformationMode renderMode, boolean lefthanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (itemStack.isOf(ModItems.Get_Block_Staff) && renderMode != ModelTransformationMode.GUI) {
            return ((ItemRendererAccessor) this).mccoursc$getModels().getModelManager().getModel(new ModelIdentifier(funnyitemMod.MOD_ID, "get_block_staff", "inventory"));
        }
        if (itemStack.isOf(ModItems.Hammer) && renderMode != ModelTransformationMode.GUI) {
            return ((ItemRendererAccessor) this).mccoursc$getModels().getModelManager().getModel(new ModelIdentifier(funnyitemMod.MOD_ID, "rune_smith_hammer", "inventory"));
        }
        if (itemStack.isOf(ModItems.Axe) && renderMode != ModelTransformationMode.GUI) {
            return ((ItemRendererAccessor) this).mccoursc$getModels().getModelManager().getModel(new ModelIdentifier(funnyitemMod.MOD_ID, "axe", "inventory"));
        }
        if (itemStack.isOf(ModItems.Clone) && renderMode != ModelTransformationMode.GUI) {
            return ((ItemRendererAccessor) this).mccoursc$getModels().getModelManager().getModel(new ModelIdentifier(funnyitemMod.MOD_ID, "clone", "inventory"));
        }
        if (itemStack.isOf(ModItems.Hammer_head) && renderMode != ModelTransformationMode.GUI) {
            return ((ItemRendererAccessor) this).mccoursc$getModels().getModelManager().getModel(new ModelIdentifier(funnyitemMod.MOD_ID, "rune_smith_hammer_head", "inventory"));
        }
        if (itemStack.isOf(ModItems.Hammer_handle) && renderMode != ModelTransformationMode.GUI) {
            return ((ItemRendererAccessor) this).mccoursc$getModels().getModelManager().getModel(new ModelIdentifier(funnyitemMod.MOD_ID, "rune_smith_hammer_handle", "inventory"));
        }
        if (itemStack.isOf(ModItems.Chunk_pickaxe) && renderMode != ModelTransformationMode.GUI) {
            return ((ItemRendererAccessor) this).mccoursc$getModels().getModelManager().getModel(new ModelIdentifier(funnyitemMod.MOD_ID, "chunk_pickaxe", "inventory"));
        }
        return value;
    }
}