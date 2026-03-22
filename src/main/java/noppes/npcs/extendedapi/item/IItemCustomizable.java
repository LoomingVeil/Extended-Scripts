package noppes.npcs.extendedapi.item;

import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Unique;

public interface IItemCustomizable {
    /**
     * Sets the harvest level for a toolClass
     * @param toolClass vanilla tool classes include pickaxe, axe, shove, and hoe (hoe may only be in newer versions). Other mods may add their own toolClasses.
     * @param level -1: Nothing; Same as mining with a stick, 0: wood/gold tools, 1: stone tools, 2: iron tools, 3: diamond tools
     */
    void setHarvestLevel(String toolClass, int level);

    int getHarvestLevel(String toolClass);

    int getArmorColor();

    void setArmorColor(Integer armorColor);

    String getArmorTexture1();

    /**
     * Texture for helmet, chestplate, and boots
     */
    void setArmorTexture1(String armorTexture1);

    String getArmorTexture2();

    /**
     * Texture for leggings
     */
    void setArmorTexture2(String armorTexture2);

    /**
     * You can ignore this function. There are likely no cases where it would be useful.
     */
    ResourceLocation getArmorResource(int slot);

    String getArmorOverlayTexture1();

    void setArmorOverlayTexture1(String armorOverlayTexture1);

    String getArmorOverlayTexture2();

    void setArmorOverlayTexture2(String armorOverlayTexture2);

    /**
     * You can ignore this function. There are likely no cases where it would be useful.
     */
    ResourceLocation getArmorOverlayResource(int slot);

    boolean usesFirstPersonOverrides();

    /**
     * When enabled, while rendering this item in first person, the values set by
     * {@link #setFirstPersonRotation(Float, Float, Float)}, {@link #setFirstPersonTranslate(Float, Float, Float)}, and {@link #setFirstPersonRotation(Float, Float, Float)}
     * This can be useful for making bows as the amount of adjustments required to make it look like the vanilla bow in
     * third person can make it look quite awkward in first person.
     * @param useFirstPersonOverrides
     */
    void setUseFirstPersonOverrides(boolean useFirstPersonOverrides);

    boolean isDyeable();

    /**
     * When enabled, this item can be used in a crafting table with dyes to change the color.
     * The color set from {@link noppes.npcs.api.item.IItemCustom#setColor(Integer)} is used as the base and
     * both the item and armor colors are changed to be identical.
     */
    void setDyeable(boolean dyeable);

    Float getFirstPersonTranslateX();
    Float getFirstPersonTranslateY();
    Float getFirstPersonTranslateZ();

    Float getFirstPersonScaleX();
    Float getFirstPersonScaleY();
    Float getFirstPersonScaleZ();

    Float getFirstPersonRotationX();
    Float getFirstPersonRotationY();
    Float getFirstPersonRotationZ();

    void setFirstPersonTranslate(Float x, Float y, Float z);
    void setFirstPersonScale(Float x, Float y, Float z);
    void setFirstPersonRotation(Float x, Float y, Float z);
}
