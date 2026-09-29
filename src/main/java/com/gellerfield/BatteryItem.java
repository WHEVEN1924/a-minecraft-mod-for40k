package com.gellerfield;

import net.minecraft.world.item.Item;

/**
 * 力场电池 (Field Battery)
 * 合成即带 100% 电量；装入盖勒力场后在下界中缓慢放电；可拆回，剩余电量随电池保留。
 */
public class BatteryItem extends Item {
    public static final int MAX_ENERGY = 100;

    public BatteryItem(Properties properties) {
        // 新电池默认满电：通过物品默认数据组件设置
        super(properties.component(ModDataComponents.ENERGY.get(), MAX_ENERGY));
    }
}
