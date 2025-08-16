package com.veil.extendedscripts;

import com.veil.extendedscripts.constants.ExtendedAttributeSection;
import com.veil.extendedscripts.constants.ExtendedAttributeValueType;
import kamkeel.npcs.controllers.data.attribute.AttributeDefinition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.*;

public class ExtendedWorldData extends WorldSavedData {
    public static final String DATA_ID = "ExtendedWorldData";
    private Set<AttributeDefinition> extendedAttributes;

    public ExtendedWorldData() {
        super(DATA_ID);
        this.extendedAttributes = new HashSet<>();
    }

    public ExtendedWorldData(String name) {
        super(name);
    }

    // This method loads the data from the NBT file.
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.extendedAttributes = new HashSet<>();
        NBTTagList list = nbt.getTagList(DATA_ID, Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound compound = list.getCompoundTagAt(i);

            this.extendedAttributes.add(new AttributeDefinition(
                compound.getString("key"),
                compound.getString("displayName"),
                (char) compound.getInteger("color"),
                ExtendedAttributeValueType.toKamAttribute(compound.getInteger("attributeValueType")),
                ExtendedAttributeSection.toKamSection(compound.getInteger("attributeSection"))
            ));
        }
    }

    // This method saves the data to the NBT file.
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagList list = new NBTTagList();

        for (AttributeDefinition attribute : extendedAttributes) {
            NBTTagCompound compound = new NBTTagCompound();

            compound.setString("key", attribute.getKey());
            compound.setString("displayName", attribute.getDisplayName());
            compound.setInteger("color", attribute.getColorCode());
            compound.setInteger("attributeValueType", attribute.getValueType().ordinal());
            compound.setInteger("attributeSection", attribute.getSection().ordinal());

            list.appendTag(compound);
        }

        nbt.setTag(DATA_ID, list);
    }

    public Collection<AttributeDefinition> getExtendedAttributes() {
        return extendedAttributes;
    }

    public void addExtendedAttribute(AttributeDefinition attribute) {
        if (this.extendedAttributes == null) {
            this.extendedAttributes = new HashSet<>();
        }
        this.extendedAttributes.add(attribute);
        markDirty();
    }

    public void removeExtendedAttribute(AttributeDefinition attribute) {
        this.extendedAttributes.remove(attribute);
        markDirty();
    }
}
