/**
 * Centralized global declarations for CustomNPC+ scripting.
 * Auto-generated - do not edit manually.
 */

declare global {
    // ============================================================================
    // TYPE ALIASES - Make all interfaces available globally
    // ============================================================================

    type AbstractAnimationType = import('./com/veil/extendedscripts/extendedapi/constants/AbstractAnimationType').AbstractAnimationType;
    type AbstractArmorType = import('./com/veil/extendedscripts/extendedapi/constants/AbstractArmorType').AbstractArmorType;
    type AbstractAttributeSection = import('./com/veil/extendedscripts/extendedapi/constants/AbstractAttributeSection').AbstractAttributeSection;
    type AbstractEntityType = import('./com/veil/extendedscripts/extendedapi/constants/AbstractEntityType').AbstractEntityType;
    type AbstractExtendedAPI = import('./com/veil/extendedscripts/extendedapi/AbstractExtendedAPI').AbstractExtendedAPI;
    type AbstractShapeMaker = import('./com/veil/extendedscripts/extendedapi/AbstractShapeMaker').AbstractShapeMaker;
    type IArmorChangedEvent = import('./com/veil/extendedscripts/extendedapi/event/IArmorChangedEvent').IArmorChangedEvent;
    type IArmorDamagedEvent = import('./com/veil/extendedscripts/extendedapi/event/IArmorDamagedEvent').IArmorDamagedEvent;
    type IAttributeRecalculateEvent = import('./com/veil/extendedscripts/extendedapi/event/IAttributeRecalculateEvent').IAttributeRecalculateEvent;
    type IAttributeValueType = import('./com/veil/extendedscripts/extendedapi/constants/IAttributeValueType').IAttributeValueType;
    type IBlock = import('./noppes/npcs/extendedapi/IBlock').IBlock;
    type IBlockData = import('./com/veil/extendedscripts/extendedapi/IBlockData').IBlockData;
    type IBlockSide = import('./com/veil/extendedscripts/extendedapi/constants/IBlockSide').IBlockSide;
    type IColorCodes = import('./com/veil/extendedscripts/extendedapi/constants/IColorCodes').IColorCodes;
    type ICustomNpc = import('./noppes/npcs/extendedapi/entity/ICustomNpc').ICustomNpc;
    type ICustomOverlay = import('./noppes/npcs/extendedapi/overlay/ICustomOverlay').ICustomOverlay;
    type ICustomProjectile = import('./com/veil/extendedscripts/extendedapi/entity/ICustomProjectile').ICustomProjectile;
    type ICustomProjectileImpactEvent = import('./com/veil/extendedscripts/extendedapi/event/ICustomProjectileImpactEvent').ICustomProjectileImpactEvent;
    type ICustomProjectileRenderProperties = import('./com/veil/extendedscripts/extendedapi/entity/ICustomProjectileRenderProperties').ICustomProjectileRenderProperties;
    type ICustomProjectileTickEvent = import('./com/veil/extendedscripts/extendedapi/event/ICustomProjectileTickEvent').ICustomProjectileTickEvent;
    type IEffect = import('./com/veil/extendedscripts/extendedapi/constants/IEffect').IEffect;
    type IEntity = import('./noppes/npcs/extendedapi/entity/IEntity').IEntity;
    type IHotbarSlotChangedEvent = import('./com/veil/extendedscripts/extendedapi/event/IHotbarSlotChangedEvent').IHotbarSlotChangedEvent;
    type IItemCustomizable = import('./noppes/npcs/extendedapi/item/IItemCustomizable').IItemCustomizable;
    type IItemFood = import('./com/veil/extendedscripts/extendedapi/item/IItemFood').IItemFood;
    type IItemPotion = import('./com/veil/extendedscripts/extendedapi/item/IItemPotion').IItemPotion;
    type IItemStack = import('./noppes/npcs/extendedapi/item/IItemStack').IItemStack;
    type IItemTool = import('./com/veil/extendedscripts/extendedapi/item/IItemTool').IItemTool;
    type IItemType = import('./com/veil/extendedscripts/extendedapi/constants/IItemType').IItemType;
    type IItemUseAction = import('./com/veil/extendedscripts/extendedapi/constants/IItemUseAction').IItemUseAction;
    type IJobType = import('./com/veil/extendedscripts/extendedapi/constants/IJobType').IJobType;
    type IKeys = import('./com/veil/extendedscripts/extendedapi/constants/IKeys').IKeys;
    type IMouseButton = import('./com/veil/extendedscripts/extendedapi/constants/IMouseButton').IMouseButton;
    type IOverlayLabel = import('./noppes/npcs/extendedapi/overlay/IOverlayLabel').IOverlayLabel;
    type IParticleType = import('./com/veil/extendedscripts/extendedapi/constants/IParticleType').IParticleType;
    type IPlayer = import('./noppes/npcs/extendedapi/entity/IPlayer').IPlayer;
    type IPlayerAttributes = import('./noppes/npcs/extendedapi/handler/data/IPlayerAttributes').IPlayerAttributes;
    type IPotionEffect = import('./com/veil/extendedscripts/extendedapi/IPotionEffect').IPotionEffect;
    type IResolutionChangedEvent = import('./com/veil/extendedscripts/extendedapi/event/IResolutionChangedEvent').IResolutionChangedEvent;
    type IRoleType = import('./com/veil/extendedscripts/extendedapi/constants/IRoleType').IRoleType;
    type IScreenResolution = import('./com/veil/extendedscripts/extendedapi/IScreenResolution').IScreenResolution;
    type ISkinType = import('./com/veil/extendedscripts/extendedapi/constants/ISkinType').ISkinType;
    type IWorld = import('./noppes/npcs/extendedapi/IWorld').IWorld;
}

export {};
