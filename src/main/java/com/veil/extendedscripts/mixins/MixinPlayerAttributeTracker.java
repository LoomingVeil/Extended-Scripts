package com.veil.extendedscripts.mixins;

import com.veil.extendedscripts.ExtendedScripts;
import com.veil.extendedscripts.properties.ExtendedScriptPlayerProperties;
import kamkeel.npcs.controllers.data.attribute.tracker.PlayerAttributeTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.handler.data.ICustomAttribute;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.extendedapi.handler.data.IPlayerAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(value={PlayerAttributeTracker.class})
public abstract class MixinPlayerAttributeTracker implements IPlayerAttributes {
    @Shadow
    public abstract ICustomAttribute[] getAttributes();

    @Unique
    EntityPlayer player;

    @Inject(
        method = "recalcAttributes(Lnet/minecraft/entity/player/EntityPlayer;)V",
        at = @At("HEAD"),
        remap = false
    )
    private void onRecalcAttributes(EntityPlayer player, CallbackInfo ci) {
        this.player = player;
    }

    @ModifyVariable(
        method = "recalcAttributes(Lnet/minecraft/entity/player/EntityPlayer;)V",
        at = @At(
            value = "STORE",
            ordinal = 0
        ),
        index = 3,
        remap = false
    )
    private ItemStack[] modifyEquipmentArray(ItemStack[] original) {
        ItemStack[] newArray = Arrays.copyOf(original, original.length + 1);
        newArray[original.length] = ExtendedScripts.getPlayerProperties(player).getAttributeCore();
        return newArray;
    }

    /**
     * Gives attributes to the player. These attributes are the same that can be applied to item except these attributes are always active until removed.
     */
    @Unique
    public void setCoreAttribute(String key, float value) {
        ExtendedScriptPlayerProperties props = ExtendedScripts.getPlayerProperties(player);
        props.setCoreAttribute(key, value);
    }

    /**
     * Gets core attributes. These attributes are separate from equipment attributes.
     */
    @Unique
    public float getCoreAttribute(String key) {
        ExtendedScriptPlayerProperties props = ExtendedScripts.getPlayerProperties(player);
        return props.getCoreAttribute(key);
    }

    @Unique
    public void resetCoreAttributes() {
        ExtendedScripts.getPlayerProperties(player).setAttributeCore(null);
    }

    /**
     * Gets the attribute core as an item that can be given to the player.
     */
    @Unique
    public IItemStack getAttributeCore() {
        return AbstractNpcAPI.Instance().getIItemStack(ExtendedScripts.getPlayerProperties(player).getAttributeCore());
    }

    @Unique
    public boolean hasCoreAttribute(String key) {
        return Arrays.asList(getAttributeKeys()).contains(key);
    }

    @Unique
    public String[] getCoreAttributeKeys() {
        IItemStack attributeCore = getAttributeCore();
        return attributeCore.getCustomAttributeKeys();
    }

    @Unique
    public String[] getAttributeKeys() {
        ICustomAttribute[] attrs = getAttributes();
        String[] keys = new String[attrs.length];
        for (int i = 0; i < attrs.length; i++) {
            keys[i] = attrs[i].getAttribute().getKey();
        }

        return keys;
    }
}


