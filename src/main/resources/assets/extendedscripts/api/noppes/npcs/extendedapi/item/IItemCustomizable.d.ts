/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: noppes.npcs.extendedapi.item
 */

/**
 * @javaFqn noppes.npcs.extendedapi.item.IItemCustomizable
 */
export interface IItemCustomizable {
    /**
     * Sets the harvest level for a toolClass
     * @param toolClass vanilla tool classes include pickaxe, axe, shove, and hoe (hoe may only be in newer versions). Other mods may add their own toolClasses.
     * @param level -1: Nothing; Same as mining with a stick, 0: wood/gold tools, 1: stone tools, 2: iron tools, 3: diamond tools
     */
    setHarvestLevel(toolClass: String, level: import('./int').int): import('./void').void;
    getHarvestLevel(toolClass: String): import('./int').int;
    getArmorColor(): import('./int').int;
    setArmorColor(armorColor: Integer): import('./void').void;
    getArmorTexture1(): String;
    /**
     * Texture for helmet, chestplate, and boots
     */
    setArmorTexture1(armorTexture1: String): import('./void').void;
    getArmorTexture2(): String;
    /**
     * Texture for leggings
     */
    setArmorTexture2(armorTexture2: String): import('./void').void;
    /**
     * You can ignore this function. There are likely no cases where it would be useful.
     */
    getArmorResource(slot: import('./int').int): ResourceLocation;
    getArmorOverlayTexture1(): String;
    setArmorOverlayTexture1(armorOverlayTexture1: String): import('./void').void;
    getArmorOverlayTexture2(): String;
    setArmorOverlayTexture2(armorOverlayTexture2: String): import('./void').void;
    getArmorOverlayResource(slot: import('./int').int): ResourceLocation;
    usesFirstPersonOverrides(): import('./boolean').boolean;
    setUseFirstPersonOverrides(useFirstPersonOverrides: import('./boolean').boolean): import('./void').void;
    getFirstPersonTranslateX(): Float;
    getFirstPersonTranslateY(): Float;
    getFirstPersonTranslateZ(): Float;
    getFirstPersonScaleX(): Float;
    getFirstPersonScaleY(): Float;
    getFirstPersonScaleZ(): Float;
    getFirstPersonRotationX(): Float;
    getFirstPersonRotationY(): Float;
    getFirstPersonRotationZ(): Float;
    setFirstPersonTranslate(x: Float, y: Float, z: Float): import('./void').void;
    setFirstPersonScale(x: Float, y: Float, z: Float): import('./void').void;
    setFirstPersonRotation(x: Float, y: Float, z: Float): import('./void').void;
}
