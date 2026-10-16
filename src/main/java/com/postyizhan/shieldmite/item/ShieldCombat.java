package com.postyizhan.shieldmite.item;

import net.minecraft.EntityPlayer;
import net.minecraft.ItemStack;

import java.lang.reflect.Method;

public final class ShieldCombat {
    private static final String OFFHAND_PLAYER_API = "moddedmite.offhand.api.core.IOffhandPlayer";
    private static final int BLOCK_DELAY_TICKS = 5;

    private ShieldCombat() {
    }

    public static ItemStack getActiveShield(EntityPlayer player) {
        if (player == null) {
            return null;
        }

        ItemStack offhandShield = getOffhandShield(player);
        if (offhandShield != null) {
            return offhandShield;
        }

        ItemStack activeStack = player.getItemInUse();
        return isShield(activeStack)
            && player.isBlocking()
            && player.getItemInUseDuration() >= BLOCK_DELAY_TICKS
            ? activeStack
            : null;
    }

    public static boolean isShield(ItemStack stack) {
        return stack != null && stack.stackSize > 0 && stack.getItem() instanceof ShieldItem;
    }

    private static ItemStack getOffhandShield(EntityPlayer player) {
        try {
            Class<?> api = Class.forName(OFFHAND_PLAYER_API, false, player.getClass().getClassLoader());
            if (!api.isInstance(player)) {
                return null;
            }

            Method isBlocking = api.getMethod("isOffhandBlocking");
            if (!Boolean.TRUE.equals(isBlocking.invoke(player))) {
                return null;
            }

            Object stack = api.getMethod("getOffhandItemInUse").invoke(player);
            if (!(stack instanceof ItemStack) || !isShield((ItemStack) stack)) {
                return null;
            }

            int remainingTicks = ((Number) api.getMethod("getOffhandItemInUseCount").invoke(player)).intValue();
            int usedTicks = ((ItemStack) stack).getMaxItemUseDuration() - remainingTicks;
            return usedTicks >= BLOCK_DELAY_TICKS ? (ItemStack) stack : null;
        } catch (ReflectiveOperationException | LinkageError ignored) {
            return null;
        }
    }
}
