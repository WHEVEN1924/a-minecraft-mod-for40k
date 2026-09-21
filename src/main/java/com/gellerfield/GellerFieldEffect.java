package com.gellerfield;

import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 * 盖勒力场 (Geller Field) - the single positive effect granted while the accessory is equipped.
 * Beacon-style: kept alive by periodic refresh, so milk cannot meaningfully remove it.
 *
 * One effect with three benefits:
 *  - +1 armor (attribute modifier)
 *  - periodic regeneration, approximating "10% faster health recovery"
 *  - 10% fire damage reduction (applied in GameEvents via damage event)
 */
public class GellerFieldEffect extends MobEffect {
    /** Ticks between each regeneration tick. */
    private static final int HEAL_INTERVAL = 40; // ticks (2s)
    /** Health restored per heal tick. */
    private static final float HEAL_AMOUNT = 1.0F;

    public GellerFieldEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xF5D76E);
        // +1 armor while the field holds.
        this.addAttributeModifier(Attributes.ARMOR,
                Identifier.fromNamespaceAndPath(GellerFieldMod.MODID, "geller_field_armor"),
                1.0, AttributeModifier.Operation.ADD_VALUE);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % HEAL_INTERVAL == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.getHealth() < entity.getMaxHealth()) {
            entity.heal(HEAL_AMOUNT);
        }
    }
}
