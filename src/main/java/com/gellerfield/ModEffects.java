package com.gellerfield;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, GellerFieldMod.MODID);

    /** 盖勒力场 - the positive field effect granted by the accessory. */
    public static final DeferredHolder<MobEffect, GellerFieldEffect> GELLER_FIELD =
            MOB_EFFECTS.register("geller_field", GellerFieldEffect::new);

    /** 下界腐败 - the corruption suffered in the Nether without the field. */
    public static final DeferredHolder<MobEffect, NetherCorruptionEffect> NETHER_CORRUPTION =
            MOB_EFFECTS.register("nether_corruption", NetherCorruptionEffect::new);
}
