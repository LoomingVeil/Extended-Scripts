package com.veil.extendedscripts;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.extendedapi.handler.data.IPlayerAttributes;
import noppes.npcs.scripted.NpcAPI;

public class AttributeCore extends Item {
    public AttributeCore() {
        this.setUnlocalizedName("attribute_core");
        this.setTextureName("minecraft:nether_star");
        this.setCreativeTab(CustomItems.tab);
    }

    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player) {
        if (canBeRedeemed(itemStackIn)) {
            player.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
        }

        return itemStackIn;
    }

    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
        if (world.isRemote) return stack;

        IPlayer npcPlayer = NpcAPI.Instance().getPlayer(player.getDisplayName());
        IPlayerAttributes attributes = (IPlayerAttributes) npcPlayer.getAttributes();
        IItemStack npcItem = NpcAPI.Instance().getIItemStack(stack);

        for (String attrKey : npcItem.getCustomAttributeKeys()) {
            attributes.modifyCoreAttribute(attrKey, npcItem.getCustomAttribute(attrKey));
        }

        if (!player.capabilities.isCreativeMode) {
            --stack.stackSize;
        }

        return stack;
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int unused) {
        return ExtendedScripts.SIGNATURE_COLOR;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.eat;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
        return 32;
    }

    public static boolean canBeRedeemed(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return true;
        }

        NBTTagCompound nbt = stack.getTagCompound();

        if (!nbt.hasKey("canBeRedeemed")) {
            return true;
        }

        return nbt.getBoolean("canBeRedeemed");
    }
}
