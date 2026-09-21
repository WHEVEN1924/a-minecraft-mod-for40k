package com.gellerfield;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import top.theillusivec4.curios.api.CuriosApi;

/**
 * Server-side logic, beacon-style:
 *
 * 1) Accessory equipped -> Geller Field effect is kept alive by short periodic refresh
 *    (milk clears it only for <1s, exactly like a beacon).
 * 2) Player in the Nether WITHOUT the Geller Field effect -> Nether Corruption,
 *    also refreshed periodically while the condition holds.
 * 3) Fire damage is reduced by 10% while the Geller Field effect is present.
 */
@EventBusSubscriber(modid = GellerFieldMod.MODID)
public class GameEvents {
    /** Ticks between effect refresh checks. */
    private static final int REFRESH_INTERVAL = 20; // ticks (1s)
    /** Duration of each effect application - long enough to survive a missed refresh,
     *  short enough that removing the cause clears it quickly. */
    private static final int EFFECT_DURATION = 60; // ticks (3s)
    /** Fire damage multiplier while the Geller Field effect is present. */
    private static final float FIRE_REDUCTION = 0.9F;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) {
            return;
        }
        if (player.tickCount % REFRESH_INTERVAL != 0) {
            return;
        }

        // 1) Equipped -> refresh the Geller Field effect (beacon-style).
        boolean equipped = CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.findFirstCurio(stack -> stack.is(ModItems.GELLER_FIELD.get())).isPresent())
                .orElse(false);

        if (equipped) {
            player.addEffect(new MobEffectInstance(ModEffects.GELLER_FIELD.get(), EFFECT_DURATION, 0, true, false, true));
        }

        // 2) In the Nether without the field -> Nether Corruption.
        if (player.level().dimension() == Level.NETHER && !player.hasEffect(ModEffects.GELLER_FIELD)) {
            player.addEffect(new MobEffectInstance(ModEffects.NETHER_CORRUPTION.get(), EFFECT_DURATION, 0, false, true, true));
        }
    }

    // 3) 10% fire damage reduction while under the Geller Field.
    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        if (event.getSource().is(DamageTypeTags.IS_FIRE)
                && event.getEntity().hasEffect(ModEffects.GELLER_FIELD)) {
            event.setAmount(event.getAmount() * FIRE_REDUCTION);
        }
    }
}
