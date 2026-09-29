package com.gellerfield;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, GellerFieldMod.MODID);

    public static final DeferredHolder<MobEffect, NetherCorruptionEffect> NETHER_CORRUPTION =
            MOB_EFFECTS.register("nether_corruption", NetherCorruptionEffect::new);
}
