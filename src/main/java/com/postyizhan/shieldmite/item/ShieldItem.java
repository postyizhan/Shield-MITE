package com.postyizhan.shieldmite.item;

import com.postyizhan.shieldmite.api.IShieldPlayer;
import net.minecraft.Block;
import net.minecraft.CreativeTabs;
import net.minecraft.EntityPlayer;
import net.minecraft.EnumItemInUseAction;
import net.minecraft.IDamageableItem;
import net.minecraft.Item;
import net.minecraft.ItemStack;
import net.minecraft.Material;
import net.xiaoyu233.fml.reload.utils.IdUtil;

import java.util.Objects;

public class ShieldItem extends Item implements IDamageableItem {
    private final String iconTexture;
    private final String modelTexture;
    private final int shieldDurability;
    private final Item repairItem;
    private final Material repairMaterial;

    public ShieldItem(
        int durability,
        String iconTexture,
        String modelTexture,
        Item repairItem,
        Material repairMaterial) {
        super(IdUtil.getNextItemID(), Objects.requireNonNull(iconTexture, "iconTexture"));
        if (durability <= 0) {
            throw new IllegalArgumentException("durability must be positive");
        }
        this.iconTexture = iconTexture;
        this.modelTexture = Objects.requireNonNull(modelTexture, "modelTexture");
        this.shieldDurability = durability;
        this.repairItem = repairItem;
        this.repairMaterial = Objects.requireNonNull(repairMaterial, "repairMaterial");
        setMaxStackSize(1);
        setMaxDamage(durability);
        setMaterial(Material.wood);
        setFull3D();
        setCreativeTab(CreativeTabs.tabCombat);
    }

    public ShieldItem(int durability, String iconTexture, Item repairItem, Material repairMaterial) {
        this(durability, iconTexture, "shieldmite:textures/models/shield_base_nopattern.png", repairItem, repairMaterial);
    }

    public ShieldItem(int durability, String iconTexture) {
        this(durability, iconTexture, Item.getItem(Block.planks), Material.wood);
    }

    public String getIconTexture() {
        return this.iconTexture;
    }

    public String getModelTexture() {
        return this.modelTexture;
    }

    public int getShieldDurability() {
        return this.shieldDurability;
    }

    @Override
    public boolean onItemRightClick(EntityPlayer player, float partialTick, boolean ctrlDown) {
        if (player instanceof IShieldPlayer
            && ((IShieldPlayer) player).shieldMite$getDisableTicks() > 0) {
            return false;
        }
        return player.setHeldItemInUse();
    }

    @Override
    public EnumItemInUseAction getItemInUseAction(ItemStack stack, EntityPlayer player) {
        return EnumItemInUseAction.BLOCK;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public int getNumComponentsForDurability() {
        return 1;
    }

    @Override
    public int getRepairCost() {
        return 2;
    }

    @Override
    public int getItemEnchantability() {
        return 9;
    }

    @Override
    public float getCraftingDifficultyAsComponent(ItemStack stack) {
        float difficulty = super.getCraftingDifficultyAsComponent(stack);
        return difficulty >= 0.0F ? difficulty : getLowestCraftingDifficultyToProduce();
    }

    @Override
    public float getLowestCraftingDifficultyToProduce() {
        float difficulty = super.getLowestCraftingDifficultyToProduce();
        return difficulty == Float.MAX_VALUE ? 0.0F : difficulty;
    }

    @Override
    public Item getRepairItem() {
        return this.repairItem != null ? this.repairItem : Item.getItem(Block.planks);
    }

    @Override
    public Material getMaterialForRepairs() {
        return this.repairMaterial;
    }
}
