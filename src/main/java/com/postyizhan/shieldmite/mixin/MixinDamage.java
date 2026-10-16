package com.postyizhan.shieldmite.mixin;

import com.postyizhan.shieldmite.api.IShieldPlayer;
import com.postyizhan.shieldmite.item.ShieldCombat;
import net.minecraft.Damage;
import net.minecraft.Entity;
import net.minecraft.EntityDamageResult;
import net.minecraft.EntityLivingBase;
import net.minecraft.EntityPlayer;
import net.minecraft.ItemStack;
import net.minecraft.ItemAxe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Damage.class)
public abstract class MixinDamage {
    @Shadow
    private float amount;

    @Shadow
    private net.minecraft.DamageSource source;

    @Inject(
        method = "applyTargetDefenseModifiers(Lnet/minecraft/EntityLivingBase;Lnet/minecraft/EntityDamageResult;)F",
        at = @At("HEAD"),
        cancellable = true)
    private void shieldMite$applyShieldBlock(
        EntityLivingBase target,
        EntityDamageResult result,
        CallbackInfoReturnable<Float> cir) {
        if (!(target instanceof EntityPlayer) || target.onClient() || this.amount <= 0.0F
            || this.source.isUnblockable()) {
            return;
        }

        EntityPlayer player = (EntityPlayer) target;
        ItemStack shield = ShieldCombat.getActiveShield(player);
        if (shield == null || !isWithinShieldArc(player, this.source.getImmediateEntity())) {
            return;
        }

        if (!player.inCreativeMode()) {
            int blockedDamage = (int) Math.floor(this.amount);
            if (blockedDamage >= 3) {
                result.applyHeldItemDamageResult(
                    shield.tryDamageItem(this.source, blockedDamage + 1, player));
            }
        }
        result.setEntityWasAffected();
        player.playSound("random.wood_click", 1.0F, 0.8F);

        Entity attacker = this.source.getResponsibleEntity();
        if (attacker instanceof EntityLivingBase && !this.source.isProjectile()) {
            double dx = attacker.posX - player.posX;
            double dz = attacker.posZ - player.posZ;
            double length = Math.sqrt(dx * dx + dz * dz);
            if (length > 0.0001D) {
                ((EntityLivingBase) attacker).addVelocity(dx / length * 0.5D, 0.1D, dz / length * 0.5D);
                result.setEntityWasKnockedBack();
            }

            ItemStack weapon = ((EntityLivingBase) attacker).getHeldItemStack();
            if (weapon != null && weapon.getItem() instanceof ItemAxe) {
                ((IShieldPlayer) player).shieldMite$disable(100);
                player.playSound("random.break", 0.8F, 0.8F);
            }
        }

        cir.setReturnValue(Float.valueOf(0.0F));
    }

    private static boolean isWithinShieldArc(EntityPlayer player, Entity source) {
        if (source == null || source == player) {
            return false;
        }

        double dx = source.posX - player.posX;
        double dz = source.posZ - player.posZ;
        double length = Math.sqrt(dx * dx + dz * dz);
        if (length <= 0.0001D) {
            return false;
        }

        double yaw = Math.toRadians(player.getRotationYawHead());
        double forwardX = -Math.sin(yaw);
        double forwardZ = Math.cos(yaw);
        return (forwardX * dx + forwardZ * dz) / length >= 0.0D;
    }
}
