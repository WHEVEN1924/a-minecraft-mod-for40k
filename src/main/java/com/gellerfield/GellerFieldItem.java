package com.gellerfield;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.type.capability.ICurio;

/**
 * 盖勒力场 (Geller Field)
 * Curios 饰品。手持右键打开电池管理界面；装入电池后，在下界中每 1 分钟消耗 1% 电量，
 * 有电时持续刷新「盖勒力场」增益，耗尽则失效。
 */
public class GellerFieldItem extends Item implements ICurio {
    public GellerFieldItem(Properties properties) {
        super(properties);
    }

    // 手持右键 -> 打开电池管理 UI（服务端打开，客户端收到同步）
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(new SimpleMenuProvider(
                    (id, inv, access) -> new GellerFieldMenu(id, inv, stack),
                    stack.getHoverName()), hand);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        ItemStack battery = stack.getOrDefault(ModDataComponents.STORED_BATTERY.get(), ItemStack.EMPTY);
        if (!battery.isEmpty()) {
            int energy = battery.getOrDefault(ModDataComponents.ENERGY.get(), 0);
            tooltip.add(Component.translatable("tooltip.gellerfield.battery_charge", energy));
        } else {
            tooltip.add(Component.translatable("tooltip.gellerfield.no_battery"));
        }
        tooltip.add(Component.translatable("tooltip.gellerfield.geller_field"));
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
