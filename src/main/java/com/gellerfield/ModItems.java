package com.gellerfield;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GellerFieldMod.MODID);

    // 26.2: registerItem 由框架构建 Properties（已带 id），工厂在传入的 Properties 上定制。
    // 注意：不能在 supplier 里 new Item.Properties()（缺 id，Item 构造器会抛 "Item id not set"）。
    public static final DeferredItem<GellerFieldItem> GELLER_FIELD =
            ITEMS.registerItem("geller_field", properties -> new GellerFieldItem(properties.stacksTo(1)));

    public static final DeferredItem<BatteryItem> BATTERY =
            ITEMS.registerItem("battery", properties -> new BatteryItem(properties.stacksTo(16)));
}
