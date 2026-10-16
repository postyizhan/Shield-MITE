package com.postyizhan.shieldmite;

import com.postyizhan.shieldmite.registry.ShieldRecipes;
import net.fabricmc.api.ModInitializer;
import net.xiaoyu233.fml.ModResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ShieldMite implements ModInitializer {
    public static final String MOD_ID = "shieldmite";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModResourceManager.addResourcePackDomain(MOD_ID);
        ShieldRecipes.register();
        LOGGER.info("Shield-MITE initialized");
    }
}
