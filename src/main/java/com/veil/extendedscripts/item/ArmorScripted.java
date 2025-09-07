package com.veil.extendedscripts.item;

import com.veil.extendedscripts.ClientProxy;
import com.veil.extendedscripts.ExtendedScripts;
import com.veil.extendedscripts.GuiOpenPacket;
import com.veil.extendedscripts.constants.ArmorType;
import com.veil.extendedscripts.constants.ExtendedEnumGuiType;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kamkeel.npcs.network.packets.data.ChatAlertPacket;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import noppes.npcs.*;
import noppes.npcs.api.item.IItemCustomizable;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.config.ConfigScript;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.data.INpcScriptHandler;
import noppes.npcs.items.ItemRenderInterface;
import noppes.npcs.scripted.NpcAPI;
import noppes.npcs.scripted.event.ItemEvent;
import org.lwjgl.opengl.GL11;

public class ArmorScripted extends ItemArmor implements IScriptedItemVariant, ItemRenderInterface {

    public static final ArmorMaterial CUSTOM_ARMOR_MATERIAL = EnumHelper.addArmorMaterial(
        "CUSTOM_ARMOR", // The name of the material
        50, // Durability factor
        new int[]{3, 8, 6, 3}, // Damage reduction amounts for boots, leggings, chestplate, helmet
        15 // Enchantability factor
    );

    public int getColorFromItemStack(ItemStack item, int p_82790_2_) {
        return ExtendedScripts.SIGNATURE_COLOR;
    }

    public ArmorScripted(int armorType) {
        super(CUSTOM_ARMOR_MATERIAL, 2, armorType);
        ClientProxy.registerItem(this);
        this.setCreativeTab(CustomItems.tab);
    }

    @Override
    public void openScriptGui(World world, EntityPlayer player, boolean ignorePlayerConditions) {
        if (!world.isRemote) {
            if ((player.isSneaking() && player.capabilities.isCreativeMode) || ignorePlayerConditions) {
                if (!ConfigScript.canScript(player, CustomNpcsPermissions.TOOL_SCRIPTED_ITEM)) {
                    ChatAlertPacket.sendChatAlert((EntityPlayerMP) player, "availability.permission");
                } else {
                    if (player instanceof EntityPlayerMP) {
                        NoppesUtilServer.setEditingNpc(player, null);
                        if (CustomNpcs.proxy.getServerGuiElement(EnumGuiType.ScriptItem.ordinal(), player, player.worldObj, 0, 0, 0) != null) {
                            player.openGui(CustomNpcs.instance, EnumGuiType.ScriptItem.ordinal(), player.worldObj, 0, 0, 0);
                        } else { // Opens guis without containers
                            GuiOpenPacket.openGUI((EntityPlayerMP)player, ExtendedEnumGuiType.ScriptArmor, 0, 0, 0);
                        }
                    }
                }
            }
        }
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        openScriptGui(world, player, false);

        player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
        return stack;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        if (slot == 0 || slot == 1 || slot == 3) { // Helmet, Chestplate, Boots
            return "extendedscripts:textures/models/armor/scripted_armor_1.png";
        } else if (slot == 2) { // Leggings
            return "extendedscripts:textures/models/armor/scripted_armor_2.png";
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        switch (armorType) {
            case 0:
                this.itemIcon = Items.iron_helmet.getIconFromDamage(0);
                break;
            case 2:
                this.itemIcon = Items.iron_leggings.getIconFromDamage(0);
                break;
            case 3:
                this.itemIcon = Items.iron_boots.getIconFromDamage(0);
                break;
            default:
                this.itemIcon = Items.iron_chestplate.getIconFromDamage(0);
        }
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLivingBase, ItemStack stack) {
        if (entityLivingBase.worldObj.isRemote) {
            return false;
        }

        // System.out.println("Attacked!");

        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        ItemEvent.AttackEvent event = new ItemEvent.AttackEvent((IItemCustomizable) istack, NpcAPI.Instance().getIEntity(entityLivingBase), 2, null);

        INpcScriptHandler handler = (INpcScriptHandler)((IItemCustomizable) istack).getScriptHandler();
        if (handler != null) {
            handler.callScript(EnumScriptType.ATTACK, event);
        } else {
            System.out.println("Handler is null");
        }

        boolean result = NpcAPI.EVENT_BUS.post(event);
        // System.out.println("Result? "+result);

        return false;
        // return EventHooks.onScriptItemAttack((IItemCustomizable) istack, event);
    }

    public int getMaxItemUseDuration(ItemStack stack) {
        IItemCustomizable customizable = (IItemCustomizable) NpcAPI.Instance().getIItemStack(stack);
        return customizable.getMaxItemUseDuration();
    }

    public boolean showDurabilityBar(ItemStack stack) {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        return istack instanceof IItemCustomizable && ((IItemCustomizable) istack).getDurabilityShow();
    }

    public double getDurabilityForDisplay(ItemStack stack) {

        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        return istack instanceof IItemCustomizable ? 1.0D - ((IItemCustomizable) istack).getDurabilityValue() : 1.0D;
    }

    public int getItemStackLimit(ItemStack stack) {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        return istack instanceof IItemCustomizable ? istack.getMaxStackSize() : super.getItemStackLimit(stack);
    }

    public boolean isItemTool(ItemStack stack) {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        return istack instanceof IItemCustomizable ? ((IItemCustomizable) istack).isTool() : super.isItemTool(stack);
    }

    public float getDigSpeed(ItemStack stack, Block block, int metadata) {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        return istack instanceof IItemCustomizable ? ((IItemCustomizable) istack).getDigSpeed() : super.getDigSpeed(stack, block, metadata);
    }

    public boolean isValidArmor(ItemStack stack, int armorType, Entity entity) {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        if (istack instanceof IItemCustomizable) {
            if (((IItemCustomizable) istack).getArmorType() == -1)
                return true;
            return armorType == ((IItemCustomizable) istack).getArmorType();
        }
        return super.isValidArmor(stack, armorType, entity);
    }

    public int getItemEnchantability(ItemStack stack) {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        return istack instanceof IItemCustomizable ? ((IItemCustomizable) istack).getEnchantability() : super.getItemEnchantability(stack);
    }

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        ItemEvent.AttackEvent eve = new ItemEvent.AttackEvent((IItemCustomizable) istack, NpcAPI.Instance().getIEntity(attacker), 1, NpcAPI.Instance().getIEntity(target));
        return EventHooks.onScriptItemAttack((IItemCustomizable) istack, eve);
    }

    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public Item setUnlocalizedName(String name) {
        GameRegistry.registerItem(this, name);
        return super.setUnlocalizedName(name);
    }

    public EnumAction getItemUseAction(ItemStack stack) {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        if (istack instanceof IItemCustomizable) {
            switch (((IItemCustomizable) istack).getItemUseAction()) {
                case 0:
                    return EnumAction.none;
                case 1:
                    return EnumAction.block;
                case 2:
                    return EnumAction.bow;
                case 3:
                    return EnumAction.eat;
                case 4:
                    return EnumAction.drink;
            }
        }
        return super.getItemUseAction(stack);
    }

    @Override
    public void renderSpecial() {
    }

    public void renderOffset(IItemCustomizable scriptCustomItem) {
        GL11.glTranslatef(0.135F * scriptCustomItem.getScaleX(), 0.2F * scriptCustomItem.getScaleY(), 0.07F * scriptCustomItem.getScaleZ());
    }
}
