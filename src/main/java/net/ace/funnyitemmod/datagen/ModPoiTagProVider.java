package net.ace.funnyitemmod.datagen;

import net.ace.funnyitemmod.FunnyItemMod;
import net.minecraft.data.DataOutput;
import net.minecraft.data.tag.TagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.PointOfInterestTypeTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.concurrent.CompletableFuture;

public class ModPoiTagProVider extends TagProvider<PointOfInterestType> {
    public ModPoiTagProVider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture) {
        super(output, RegistryKeys.POINT_OF_INTEREST_TYPE, registryLookupFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
         this.getTagBuilder(PointOfInterestTypeTags.ACQUIRABLE_JOB_SITE)
                 .addOptional(Identifier.of(FunnyItemMod.MOD_ID, "rune_poi"));
    }
}
