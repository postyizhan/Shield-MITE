package com.postyizhan.shieldmite.mixin;

import com.postyizhan.shieldmite.api.IShieldPlayer;
import net.minecraft.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer implements IShieldPlayer {
    @Unique
    private int shieldMite$disableTicks;

    @Override
    public int shieldMite$getDisableTicks() {
        return this.shieldMite$disableTicks;
    }

    @Override
    public void shieldMite$disable(int ticks) {
        if (ticks <= 0) {
            return;
        }
        this.shieldMite$disableTicks = Math.max(this.shieldMite$disableTicks, ticks);
        ((EntityPlayer) (Object) this).stopUsingItem();
    }

    @Inject(method = "onUpdate()V", at = @At("TAIL"))
    private void shieldMite$tickDisableCooldown(CallbackInfo ci) {
        if (this.shieldMite$disableTicks > 0) {
            this.shieldMite$disableTicks--;
        }
    }
}
