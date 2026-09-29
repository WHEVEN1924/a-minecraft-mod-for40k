package com.gellerfield;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

/**
 * 下界腐败 (Nether Corruption)
 * A single visible debuff that is composed of four vanilla effects:
 * wither + nausea + mining fatigue + weakness, refreshed periodically while it ticks.
 */
public class NetherCorruptionEffect extends MobEffect {
    /** Duration given to each of the four vanilla sub-effects on every refresh. */
    private static final int SUB_EFFECT_DURATION = 80; // ticks (4s)
    /** Ticks between each application of the sub-effects. */
    private static final int APPLY_INTERVAL = 20; // ticks (1s)

    public NetherCorruptionEffect() {
        super(MobEffectCategory.HARMFUL, 0x5C1A47);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % APPLY_INTERVAL == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        // The four vanilla components of Nether Corruption.
        // Ambient=false, visible=false, showIcon=true: keep the HUD readable with a single row of icons.
        entity.addEffect(new MobEffectInstance(MobEffects.WITHER, SUB_EFFECT_DURATION, 0, false, false, true));
        entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, SUB_EFFECT_DURATION, 0, false, false, true));
        entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, SUB_EFFECT_DURATION, 1, false, false, true));
        entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, SUB_EFFECT_DURATION, 1, false, false, true));
    }
}
