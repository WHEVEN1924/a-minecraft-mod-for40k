package com.gellerfield;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * 盖勒力场电池管理界面（服务端权威）。
 * 唯一的自定义槽位是电池槽：直接读写饰品 ItemStack 的 stored_battery 数据组件，
 * 电池可拖入、shift 装入，也可拆回背包（剩余电量随电池保留）。
 */
public class GellerFieldMenu extends AbstractContainerMenu {
    /** 电池槽在 slots 列表中的下标。 */
    private static final int BATTERY_SLOT = 0;
    /** 背包+快捷栏槽位范围 [1, 37)。 */
    private static final int INVENTORY_FIRST = 1;
    private static final int INVENTORY_LAST_PLUS_1 = 37;

    private final ItemStack gellerStack;

    // 客户端构造（openMenu 的 buf 中带 hand）
    public GellerFieldMenu(int containerId, Inventory playerInventory, InteractionHand hand) {
        this(containerId, playerInventory, playerInventory.player.getItemInHand(hand));
    }

    // 服务端构造
    public GellerFieldMenu(int containerId, Inventory playerInventory, ItemStack gellerStack) {
        super(ModMenus.GELLER_FIELD.get(), containerId);
        this.gellerStack = gellerStack;

        // 电池槽（与熔炉燃料位相同布局习惯）
        this.addSlot(new BatterySlot(80, 35));

        // 玩家背包 3x9
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        // 快捷栏
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    public ItemStack getGellerStack() {
        return gellerStack;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        if (index == BATTERY_SLOT) {
            // 电池槽 -> 背包（拆回电池，剩余电量保留）
            ItemStack copy = stack.copy();
            if (this.moveItemStackTo(stack, INVENTORY_FIRST, INVENTORY_LAST_PLUS_1, false)) {
                slot.set(ItemStack.EMPTY);
                return copy;
            }
        } else {
            // 背包 -> 电池槽（shift 装入）
            if (stack.is(ModItems.BATTERY.get()) && !this.slots.get(BATTERY_SLOT).hasItem()) {
                ItemStack one = stack.copy();
                one.setCount(1);
                if (this.moveItemStackTo(one, BATTERY_SLOT, BATTERY_SLOT + 1, false)) {
                    stack.shrink(1);
                    return one;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        // UI 在手持打开时创建，这里保持宽松：饰品可能仍在手上或饰品槽中
        return true;
    }

    /** 电池槽：读写饰品的 stored_battery 数据组件，无独立 Container。 */
    private class BatterySlot extends Slot {
        BatterySlot(int x, int y) {
            super(new SimpleContainer(0), 0, x, y);
        }

        @Override
        public ItemStack getItem() {
            ItemStack battery = gellerStack.getOrDefault(ModDataComponents.STORED_BATTERY.get(), ItemStack.EMPTY);
            return battery.isEmpty() ? ItemStack.EMPTY : battery;
        }

        @Override
        public void set(ItemStack stack) {
            gellerStack.set(ModDataComponents.STORED_BATTERY.get(), stack.isEmpty() ? ItemStack.EMPTY : stack.copy());
            this.setChanged();
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.is(ModItems.BATTERY.get()) && getItem().isEmpty();
        }

        /**
         * 基类 Slot.remove 走 container.removeItem，而这里用的是一个空容器，
         * 不重写会导致电池永远取不回来（safeTake / tryRemove 全部返回空）。
         */
        @Override
        public ItemStack remove(int amount) {
            ItemStack stored = getItem();
            if (stored.isEmpty() || amount <= 0) {
                return ItemStack.EMPTY;
            }
            ItemStack taken = stored.copyWithCount(Math.min(amount, stored.getCount()));
            int remaining = stored.getCount() - taken.getCount();
            this.set(remaining > 0 ? stored.copyWithCount(remaining) : ItemStack.EMPTY);
            return taken;
        }

        @Override
        public void setChanged() {
            // 数据组件即持久化存储，无需额外处理
        }
    }
}
