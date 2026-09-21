package com.gellerfield;

import net.neoforged.neoforge.common.ModConfigSpec;

// Mod config. Effect levels and grace period will be defined here once gameplay code lands.
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    static final ModConfigSpec SPEC = BUILDER.build();
}
