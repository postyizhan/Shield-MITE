package com.postyizhan.shieldmite.mixin;

import com.postyizhan.shieldmite.client.ShieldModelRenderer;
import com.postyizhan.shieldmite.item.ShieldItem;
import net.minecraft.EntityLivingBase;
import net.minecraft.ItemRenderer;
import net.minecraft.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
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
        // renderItem applies these transforms before renderItemIn2D.  Since
        // the shield uses its own model, preserve that part of the vanilla
        // item-rendering contract before replacing the quad renderer.
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glTranslatef(0.0F, -0.3F, 0.0F);
        GL11.glScalef(1.5F, 1.5F, 1.5F);
        GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
        // The vanilla item quad starts at (0, 0), while the shield model is
        // centered around its origin. Re-center it before converting model
        // pixels to item-space units.
        GL11.glTranslatef(0.5F, 0.5F, 0.0F);
        GL11.glScalef(1.0F / 16.0F, 1.0F / 16.0F, 1.0F / 16.0F);
        ShieldModelRenderer.render((ShieldItem) stack.getItem());
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        ci.cancel();
    }
}
