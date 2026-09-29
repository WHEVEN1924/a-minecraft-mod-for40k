package com.gellerfield;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, GellerFieldMod.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<GellerFieldMenu>> GELLER_FIELD =
            MENUS.register("geller_field", () -> IMenuTypeExtension.create((id, inv, buf) ->
                    new GellerFieldMenu(id, inv, buf.readEnum(InteractionHand.class))));
}
