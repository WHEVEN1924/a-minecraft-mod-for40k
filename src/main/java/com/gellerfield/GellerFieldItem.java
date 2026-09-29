package com.gellerfield;

import java.util.function.Consumer;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.SimpleMenuProvider;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

/**
 * 盖勒力场 (Geller Field)
 * Curios 饰品。手持右键打开电池管理界面；装入电池后，在下界中每 1 分钟消耗 1% 电量，
 * 有电时持续刷新「盖勒力场」增益，耗尽则失效。
 */
public class GellerFieldItem extends Item implements ICurioItem {
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
                    stack.getHoverName()), buf -> buf.writeEnum(hand));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay tooltipDisplay,
            Consumer<Component> tooltip, TooltipFlag flag) {
        ItemStack battery = stack.getOrDefault(ModDataComponents.STORED_BATTERY.get(), ItemStack.EMPTY);
        if (!battery.isEmpty()) {
            int energy = battery.getOrDefault(ModDataComponents.ENERGY.get(), 0);
            tooltip.accept(Component.translatable("tooltip.gellerfield.battery_charge", energy));
        } else {
            tooltip.accept(Component.translatable("tooltip.gellerfield.no_battery"));
        }
        tooltip.accept(Component.translatable("tooltip.gellerfield.geller_field"));
        super.appendHoverText(stack, context, tooltipDisplay, tooltip, flag);
    }
}
