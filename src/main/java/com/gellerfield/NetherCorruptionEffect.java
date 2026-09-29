package com.gellerfield;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 * 下界腐败 (Nether Corruption) - a single custom debuff that embodies the behavior of
 * wither + weakness + mining fatigue.
 *
 * Instead of applying four vanilla effects, this one effect carries their behavior:
 *  - wither: periodic wither damage
 *  - weakness: -4 attack damage (aligned with vanilla Weakness I)
 *  - mining fatigue: -30% block break speed (aligned with vanilla Fatigue I)
 */
public class NetherCorruptionEffect extends MobEffect {
    /** Ticks between each wither damage application. */
    private static final int WITHER_INTERVAL = 40; // ticks (2s)
    /** Wither damage per application at amplifier 0. */
    private static final float WITHER_DAMAGE = 1.0F;

    public NetherCorruptionEffect() {
        super(MobEffectCategory.HARMFUL, 0x5C1A47);
        // Weakness: -4 attack damage.
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE,
                Identifier.fromNamespaceAndPath(GellerFieldMod.MODID, "corruption_weakness"),
                -4.0, AttributeModifier.Operation.ADD_VALUE);
        // Mining fatigue: -30% block break speed.
        this.addAttributeModifier(Attributes.BLOCK_BREAK_SPEED,
                Identifier.fromNamespaceAndPath(GellerFieldMod.MODID, "corruption_fatigue"),
                -0.3, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % WITHER_INTERVAL == 0;
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        // Wither: periodic wither damage, scaling with amplifier.
        entity.hurt(entity.damageSources().wither(), WITHER_DAMAGE * (amplifier + 1));
        return true;
    }
}
