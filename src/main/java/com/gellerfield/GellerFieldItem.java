package com.gellerfield;

import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.type.capability.ICurio;

/**
 * 盖勒力场 (Geller Field)
 * A Curios accessory. While equipped in a charm slot, the wearer is immune to Nether Corruption.
 */
public class GellerFieldItem extends Item implements ICurio {
    public GellerFieldItem(Item.Properties properties) {
        super(properties);
    }
}
