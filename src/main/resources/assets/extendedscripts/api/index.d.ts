/**
 * Centralized global declarations for CustomNPC+ scripting.
 * Auto-generated - do not edit manually.
 */

declare global {
    // ============================================================================
    // TYPE ALIASES - Make all interfaces available globally
    // ============================================================================

    type AbstractAbilityPhase = import('./com/veil/extendedscripts/extendedapi/constants/AbstractAbilityPhase').AbstractAbilityPhase;
    type AbstractAbilityUserType = import('./com/veil/extendedscripts/extendedapi/constants/AbstractAbilityUserType').AbstractAbilityUserType;
    type AbstractAnimationType = import('./com/veil/extendedscripts/extendedapi/constants/AbstractAnimationType').AbstractAnimationType;
    type AbstractArmorType = import('./com/veil/extendedscripts/extendedapi/constants/AbstractArmorType').AbstractArmorType;
    type AbstractAttributeSection = import('./com/veil/extendedscripts/extendedapi/constants/AbstractAttributeSection').AbstractAttributeSection;
    type AbstractAttributeValueType = import('./com/veil/extendedscripts/extendedapi/constants/AbstractAttributeValueType').AbstractAttributeValueType;
    type AbstractBlockSide = import('./com/veil/extendedscripts/extendedapi/constants/AbstractBlockSide').AbstractBlockSide;
    type AbstractCollisionType = import('./com/veil/extendedscripts/extendedapi/constants/AbstractCollisionType').AbstractCollisionType;
    type AbstractColorCodes = import('./com/veil/extendedscripts/extendedapi/constants/AbstractColorCodes').AbstractColorCodes;
    type AbstractCombatPolicy = import('./com/veil/extendedscripts/extendedapi/constants/AbstractCombatPolicy').AbstractCombatPolicy;
    type AbstractDataType = import('./com/veil/extendedscripts/extendedapi/constants/AbstractDataType').AbstractDataType;
    type AbstractEffect = import('./com/veil/extendedscripts/extendedapi/constants/AbstractEffect').AbstractEffect;
    type AbstractEntityType = import('./com/veil/extendedscripts/extendedapi/constants/AbstractEntityType').AbstractEntityType;
    type AbstractExtendedAPI = import('./com/veil/extendedscripts/extendedapi/AbstractExtendedAPI').AbstractExtendedAPI;
    type AbstractItemType = import('./com/veil/extendedscripts/extendedapi/constants/AbstractItemType').AbstractItemType;
    type AbstractItemUseAction = import('./com/veil/extendedscripts/extendedapi/constants/AbstractItemUseAction').AbstractItemUseAction;
    type AbstractJobType = import('./com/veil/extendedscripts/extendedapi/constants/AbstractJobType').AbstractJobType;
    type AbstractKeys = import('./com/veil/extendedscripts/extendedapi/constants/AbstractKeys').AbstractKeys;
    type AbstractMouseButton = import('./com/veil/extendedscripts/extendedapi/constants/AbstractMouseButton').AbstractMouseButton;
    type AbstractParticleType = import('./com/veil/extendedscripts/extendedapi/constants/AbstractParticleType').AbstractParticleType;
    type AbstractRoleType = import('./com/veil/extendedscripts/extendedapi/constants/AbstractRoleType').AbstractRoleType;
    type AbstractShapeMaker = import('./com/veil/extendedscripts/extendedapi/AbstractShapeMaker').AbstractShapeMaker;
    type AbstractSkinType = import('./com/veil/extendedscripts/extendedapi/constants/AbstractSkinType').AbstractSkinType;
    type AbstractTacticalVariant = import('./com/veil/extendedscripts/extendedapi/constants/AbstractTacticalVariant').AbstractTacticalVariant;
    type IArmorChangedEvent = import('./com/veil/extendedscripts/extendedapi/event/IArmorChangedEvent').IArmorChangedEvent;
    type IArmorDamagedEvent = import('./com/veil/extendedscripts/extendedapi/event/IArmorDamagedEvent').IArmorDamagedEvent;
    type IAttributeRecalculateEvent = import('./com/veil/extendedscripts/extendedapi/event/IAttributeRecalculateEvent').IAttributeRecalculateEvent;
    type IBlock = import('./noppes/npcs/extendedapi/IBlock').IBlock;
    type IBlockData = import('./com/veil/extendedscripts/extendedapi/IBlockData').IBlockData;
    type ICustomNpc = import('./noppes/npcs/extendedapi/entity/ICustomNpc').ICustomNpc;
    type ICustomOverlay = import('./noppes/npcs/extendedapi/overlay/ICustomOverlay').ICustomOverlay;
    type ICustomProjectile = import('./com/veil/extendedscripts/extendedapi/entity/ICustomProjectile').ICustomProjectile;
    type ICustomProjectileImpactEvent = import('./com/veil/extendedscripts/extendedapi/event/ICustomProjectileImpactEvent').ICustomProjectileImpactEvent;
    type ICustomProjectileRenderProperties = import('./com/veil/extendedscripts/extendedapi/entity/ICustomProjectileRenderProperties').ICustomProjectileRenderProperties;
    type ICustomProjectileTickEvent = import('./com/veil/extendedscripts/extendedapi/event/ICustomProjectileTickEvent').ICustomProjectileTickEvent;
    type IEntity = import('./noppes/npcs/extendedapi/entity/IEntity').IEntity;
    type IHotbarSlotChangedEvent = import('./com/veil/extendedscripts/extendedapi/event/IHotbarSlotChangedEvent').IHotbarSlotChangedEvent;
    type IItemCustomizable = import('./noppes/npcs/extendedapi/item/IItemCustomizable').IItemCustomizable;
    type IItemFood = import('./com/veil/extendedscripts/extendedapi/item/IItemFood').IItemFood;
    type IItemPotion = import('./com/veil/extendedscripts/extendedapi/item/IItemPotion').IItemPotion;
    type IItemStack = import('./noppes/npcs/extendedapi/item/IItemStack').IItemStack;
    type IItemTool = import('./com/veil/extendedscripts/extendedapi/item/IItemTool').IItemTool;
    type IOverlayLabel = import('./noppes/npcs/extendedapi/overlay/IOverlayLabel').IOverlayLabel;
    type IPlayer = import('./noppes/npcs/extendedapi/entity/IPlayer').IPlayer;
    type IPlayerAttributes = import('./noppes/npcs/extendedapi/handler/data/IPlayerAttributes').IPlayerAttributes;
    type IPotionEffect = import('./com/veil/extendedscripts/extendedapi/IPotionEffect').IPotionEffect;
    type IResolutionChangedEvent = import('./com/veil/extendedscripts/extendedapi/event/IResolutionChangedEvent').IResolutionChangedEvent;
    type IScreenResolution = import('./com/veil/extendedscripts/extendedapi/IScreenResolution').IScreenResolution;
    type IWorld = import('./noppes/npcs/extendedapi/IWorld').IWorld;
}

export {};
