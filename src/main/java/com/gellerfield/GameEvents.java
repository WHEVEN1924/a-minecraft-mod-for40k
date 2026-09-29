package com.gellerfield;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.CuriosApi;

/**
 * 服务端逻辑（信标式 + 充能）：
 *
 * 1) 装备饰品且电池有电 -> 持续刷新「盖勒力场」效果（护甲/回血/火减）。
 * 2) 装备饰品且处于下界 -> 每 1 分钟从电池扣除 1% 电量（仅下界耗电）。
 * 3) 玩家在下界且没有「盖勒力场」效果 -> 「下界腐败」。
 * 4) 电池耗尽 -> 不再刷新 -> 3 秒后力场消失（饰品保留，可换电池）。
 */
@EventBusSubscriber(modid = GellerFieldMod.MODID)
public class GameEvents {
    /** 效果刷新与电量检查间隔（tick）。 */
    private static final int REFRESH_INTERVAL = 20; // 1s
    /** 每次扣电间隔（tick）= 1 分钟。 */
    private static final int DRAIN_INTERVAL = 1200; // 60s
    /** 每次扣电量。 */
    private static final int DRAIN_AMOUNT = 1;
    /** 效果持续时间（刷新式）。 */
    private static final int EFFECT_DURATION = 60; // 3s
    /** 火焰伤害倍率（盖勒力场生效时）。 */
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

        // 查询装备的盖勒力场饰品与电池
        ItemStack curioStack = CuriosApi.getCuriosInventory(player)
                .flatMap(handler -> handler.findFirstCurio(stack -> stack.is(ModItems.GELLER_FIELD.get())))
                .map(SlotResult::stack)
                .orElse(ItemStack.EMPTY);

        boolean hasPower = false;
        if (!curioStack.isEmpty()) {
            ItemStack battery = curioStack.getOrDefault(ModDataComponents.STORED_BATTERY.get(), ItemStack.EMPTY);
            hasPower = !battery.isEmpty() && battery.getOrDefault(ModDataComponents.ENERGY.get(), 0) > 0;

            // 2) 下界中缓慢耗电
            if (hasPower && player.level().dimension() == Level.NETHER
                    && player.tickCount % DRAIN_INTERVAL == 0) {
                int energy = battery.getOrDefault(ModDataComponents.ENERGY.get(), 0);
                battery.set(ModDataComponents.ENERGY.get(), Math.max(0, energy - DRAIN_AMOUNT));
                curioStack.set(ModDataComponents.STORED_BATTERY.get(), battery); // 组件不可变，需写回
            }

            // 1) 有电 -> 刷新盖勒力场效果
            if (hasPower) {
                player.addEffect(new MobEffectInstance(ModEffects.GELLER_FIELD.get(), EFFECT_DURATION, 0, true, false, true));
            }
        }

        // 3) 下界且无力场 -> 下界腐败
        if (player.level().dimension() == Level.NETHER && !player.hasEffect(ModEffects.GELLER_FIELD)) {
            player.addEffect(new MobEffectInstance(ModEffects.NETHER_CORRUPTION.get(), EFFECT_DURATION, 0, false, true, true));
        }
    }

    // 4) 盖勒力场生效时火焰伤害 -10%
    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        if (event.getSource().is(DamageTypeTags.IS_FIRE)
                && event.getEntity().hasEffect(ModEffects.GELLER_FIELD)) {
            event.setAmount(event.getAmount() * FIRE_REDUCTION);
        }
    }
}
