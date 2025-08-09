package net.ace.funnyitemmod.event;

import net.ace.funnyitemmod.item.custom.Feather_ofSoaringItem;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;

public class PlayerEventHandler {

    public static void registerEvents() {
        // 玩家重生时重置飞行状态
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            if (Feather_ofSoaringItem.isFlightEnabled(newPlayer)) {
                Feather_ofSoaringItem.setFlightEnabled(newPlayer, false);
                newPlayer.getAbilities().allowFlying = false;
                newPlayer.getAbilities().flying = false;
                newPlayer.sendAbilitiesUpdate();
            }
        });

        // 玩家死亡时重置飞行状态
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            if (Feather_ofSoaringItem.isFlightEnabled(oldPlayer)) {
                Feather_ofSoaringItem.setFlightEnabled(newPlayer, false);
                newPlayer.getAbilities().allowFlying = false;
                newPlayer.getAbilities().flying = false;
                newPlayer.sendAbilitiesUpdate();
            }
        });
    }
}
