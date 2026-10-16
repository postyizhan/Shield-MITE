package com.postyizhan.shieldmite.registry;

import moddedmite.rustedironcore.api.event.Handlers;
import moddedmite.rustedironcore.api.event.listener.IInitializationListener;
import net.minecraft.Block;
import net.minecraft.CraftingManager;
import net.minecraft.Item;
import net.minecraft.ItemStack;
import net.minecraft.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.xiaoyu233.fml.reload.transform.util.CraftingManagerInvoker;

public final class ShieldRecipes {
    private static boolean registered;

    private ShieldRecipes() {
    }

    public static void register() {
        Handlers.Initialization.register(new IInitializationListener() {
            @Override
            public void onServerStarted(MinecraftServer server) {
                registerRecipe();
            }

            @Override
            public void onClientStarted(Minecraft client) {
                registerRecipe();
            }
        });
    }

    private static synchronized void registerRecipe() {
        if (registered || ShieldRegistryInit.SHIELD == null) {
            return;
        }
        ((CraftingManagerInvoker) CraftingManager.getInstance()).addRecipe(
            new ItemStack(ShieldRegistryInit.SHIELD),
            true,
            "pIp",
            "ppp",
            " p ",
            'p', Block.planks,
            'I', Item.ingotIron);
        registered = true;
    }
}
