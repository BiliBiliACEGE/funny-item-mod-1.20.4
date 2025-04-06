package net.ace.funnyitemmod.sounds;

import net.ace.FunnyItemMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

    public static final SoundEvent CLONE_BALL_HIT = registerSoundEvent("clone_ball_hit");



    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(FunnyItemMod.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }


    public static void registerSounds() {
        System.out.println("模组声音注册"+FunnyItemMod.MOD_ID);

    }
}
