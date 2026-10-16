package com.postyizhan.shieldmite.api;

import com.postyizhan.shieldmite.item.ShieldItem;
import huix.glacier.api.registry.MinecraftRegistry;
import net.minecraft.CreativeTabs;
import net.minecraft.Item;
import net.xiaoyu233.fml.api.item.IItem;

import java.util.Objects;

public final class ShieldRegistry {
    private ShieldRegistry() {
    }

    public static <T extends ShieldItem> T register(
        MinecraftRegistry registry,
        String namespacedId,
        String translationKey,
        T shield) {
        Objects.requireNonNull(registry, "registry");
        Objects.requireNonNull(namespacedId, "namespacedId");
        Objects.requireNonNull(translationKey, "translationKey");
        Objects.requireNonNull(shield, "shield");

        Item registered = registry.registerItem(namespacedId, translationKey, shield);
        if (registered != shield) {
            throw new IllegalStateException("Registry returned a different shield instance for " + namespacedId);
        }
        shield.setCreativeTab(CreativeTabs.tabCombat);
        ((IItem) shield).setItemTextureName(shield.getIconTexture());
        return shield;
    }
}
