package com.gellerfield;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GellerFieldMod.MODID);

    public static final DeferredItem<GellerFieldItem> GELLER_FIELD =
            ITEMS.register("geller_field", () -> new GellerFieldItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<BatteryItem> BATTERY =
            ITEMS.register("battery", () -> new BatteryItem(new Item.Properties().stacksTo(16)));
}
