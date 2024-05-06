package net.ace.funnyitemmod.mixin;

import net.ace.funnyitemMod;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin {
    @Shadow
    protected abstract void addModel(ModelIdentifier modelid);

    @Inject(method = "<init>", at = @At("RETURN"))
    private void addgetblock(BlockColors blockColors, Profiler profiler, Map<Identifier, JsonUnbakedModel> jsonUnbakedModels, Map<Identifier, List<ModelLoader.SourceTrackedData>> data, CallbackInfo info) {
        this.addModel(new ModelIdentifier(funnyitemMod.MOD_ID, "get_block_staff", "inventory"));
        this.addModel(new ModelIdentifier(funnyitemMod.MOD_ID, "rune_smith_hammer", "inventory"));
        this.addModel(new ModelIdentifier(funnyitemMod.MOD_ID, "axe", "inventory"));
        this.addModel(new ModelIdentifier(funnyitemMod.MOD_ID, "clone", "inventory"));
    }
}
