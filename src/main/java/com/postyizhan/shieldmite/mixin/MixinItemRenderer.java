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
        // Apply vanilla shield transformations for first-person rendering
        // Based on shield.json: "firstperson_righthand":
        // translation [-10, 2, -10], rotation [0, 180, 5], scale [1.25, 1.25, 1.25]

        // OpenGL applies transformations in reverse order (last specified = first applied)
        // So we write: translate -> rotate -> scale, which executes as: scale -> rotate -> translate

        // Step 1 (applied last): Translate in world space
        GL11.glTranslatef(-5.0F / 16.0F, 2.0F / 16.0F, -10.0F / 16.0F);

        // Step 2 (applied middle): Rotate
        GL11.glRotatef(0.0F, 1.0F, 0.0F, 0.0F);   // X-axis: 0 degrees
        GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F); // Y-axis: 180 degrees
        GL11.glRotatef(5.0F, 0.0F, 0.0F, 1.0F);   // Z-axis: 5 degrees

        // Step 3 (applied first): Scale
        float scale = 1.25F / 16.0F; // 1.25 * (1/16) for model pixels to world units
        GL11.glScalef(scale, scale, scale);

        ShieldModelRenderer.render((ShieldItem) stack.getItem());
        GL11.glPopMatrix();
        ci.cancel();
    }
}
