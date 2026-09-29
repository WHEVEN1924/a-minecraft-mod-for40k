package com.gellerfield;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

/**
 * 把本 mod 的物品加进原版「战斗」创造模式物品栏标签。
 * BuildCreativeModeTabContentsEvent 实现 IModBusEvent，@EventBusSubscriber 会自动注册到 mod 事件总线。
 */
@EventBusSubscriber(modid = GellerFieldMod.MODID)
public class ModCreativeTabs {
    @SubscribeEvent
    public static void addToCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.GELLER_FIELD.get());
            event.accept(ModItems.BATTERY.get());
        }
    }
}
