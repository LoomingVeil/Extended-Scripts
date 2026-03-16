/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: com.veil.extendedscripts.extendedapi.entity
 */

/**
 * @javaFqn com.veil.extendedscripts.extendedapi.entity.ICustomProjectileRenderProperties
 */
export interface ICustomProjectileRenderProperties {
    getRenderType(): import('./byte').byte;
    setRenderType(renderType: import('./byte').byte): import('./void').void;
    getTexturePath(): String;
    setTexture(texturePath: String): import('./void').void;
    getTexture(): ResourceLocation;
    getNumSimpleRenderPlanes(): import('./int').int;
    setNumSimpleRenderPlanes(numSimpleRenderPlanes: import('./int').int): import('./void').void;
    getRollOffset(): import('./float').float;
    setRollOffset(rollOffset: import('./float').float): import('./void').void;
    getRotationOffset(): import('./float').float;
    setRotationOffset(rotationOffset: import('./float').float): import('./void').void;
    getRotatingRotation(): import('./float').float;
    setRotatingRotation(rotatingRotation: import('./float').float): import('./void').void;
    getForwardOffset(): import('./float').float;
    setForwardOffset(forwardOffset: import('./float').float): import('./void').void;
    getRotationSpeed(): import('./float').float;
    setRotationSpeed(rotationSpeed: import('./float').float): import('./void').void;
    getScale(): import('./float').float;
    setScale(scale: import('./float').float): import('./void').void;
    shouldStopRotatingOnImpact(): import('./boolean').boolean;
    setStopRotatingOnImpact(stopRotatingOnImpact: import('./boolean').boolean): import('./void').void;
    shouldOnImpactSnapToInitRotation(): import('./boolean').boolean;
    setOnImpactSnapToInitRotation(onImpactSnapToInitRotation: import('./boolean').boolean): import('./void').void;
}
