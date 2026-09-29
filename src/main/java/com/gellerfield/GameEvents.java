package com.gellerfield;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import top.theillusivec4.curios.api.CuriosApi;

/**
 * Server-side trigger: players in the Nether without the Geller Field equipped
 * suffer from Nether Corruption, refreshed periodically.
 */
@EventBusSubscriber(modid = GellerFieldMod.MODID)
public class GameEvents {
    /** Ticks between corruption refresh checks. */
    private static final int REFRESH_INTERVAL = 40; // ticks (2s)
    /** Duration of each Nether Corruption application. */
    private static final int CORRUPTION_DURATION = 80; // ticks (4s)

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) {
            return;
        }
        if (player.tickCount % REFRESH_INTERVAL != 0) {
            return;
        }
        if (player.level().dimension() != Level.NETHER) {
            return;
        }

        boolean hasField = CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.findFirstCurio(stack -> stack.is(ModItems.GELLER_FIELD.get())).isPresent())
                .orElse(false);

        if (!hasField) {
            player.addEffect(new MobEffectInstance(ModEffects.NETHER_CORRUPTION.get(), CORRUPTION_DURATION, 0, false, true, true));
        }
    }
}
