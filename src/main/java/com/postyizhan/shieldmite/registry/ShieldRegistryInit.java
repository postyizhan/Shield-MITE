package com.postyizhan.shieldmite.registry;

import com.postyizhan.shieldmite.ShieldMite;
import com.postyizhan.shieldmite.api.ShieldRegistry;
import com.postyizhan.shieldmite.item.ShieldItem;
import huix.glacier.api.entrypoint.IGameRegistry;
import huix.glacier.api.registry.MinecraftRegistry;
import net.minecraft.Item;

public final class ShieldRegistryInit implements IGameRegistry {
    public static final MinecraftRegistry REGISTRY =
        new MinecraftRegistry(ShieldMite.MOD_ID).initAutoItemRegister();

    public static ShieldItem SHIELD;

    @Override
    public void onGameRegistry() {
        SHIELD = ShieldRegistry.register(
            REGISTRY,
            ShieldMite.MOD_ID + ":shield",
            ShieldMite.MOD_ID + ".shield",
            new ShieldItem(336, ShieldMite.MOD_ID + ":shield"));
    }
}
