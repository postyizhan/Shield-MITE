package com.postyizhan.shieldmite.mixin;

import com.postyizhan.shieldmite.client.ShieldModelRenderer;
import com.postyizhan.shieldmite.item.ShieldItem;
import net.minecraft.EntityLivingBase;
import net.minecraft.ItemRenderer;
import net.minecraft.ItemStack;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {
    @Inject(
        method = "renderItem(Lnet/minecraft/EntityLivingBase;Lnet/minecraft/ItemStack;I)V",
        at = @At("HEAD"),
        cancellable = true)
    private void shieldMite$renderHeldShield(
        EntityLivingBase entity,
        ItemStack stack,
        int pass,
        CallbackInfo ci) {
        if (stack == null || !(stack.getItem() instanceof ShieldItem)) {
            return;
        }

        GL11.glPushMatrix();
        // Center the shield model in the rendering space
        // The model is defined with origin at center, spanning from -6 to 6 in X,
        // -11 to 11 in Y, and -2 to 5 in Z (shield face at -2 to -1, handle at -1 to 5)
        GL11.glTranslatef(0.5F, 0.5F, 0.0F);
        // Scale from model pixels to world units (16 pixels = 1 unit)
        GL11.glScalef(1.0F / 16.0F, 1.0F / 16.0F, 1.0F / 16.0F);
        ShieldModelRenderer.render((ShieldItem) stack.getItem());
        GL11.glPopMatrix();
        ci.cancel();
    }
}
