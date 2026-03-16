/**
 * Generated from Java file for CustomNPC+ Minecraft Mod 1.7.10
 * Package: com.veil.extendedscripts.extendedapi
 */

/**
 * @javaFqn com.veil.extendedscripts.extendedapi.IPotionEffect
 */
export interface IPotionEffect {
    getName(): String;
    getID(): import('./int').int;
    setID(ID: import('./int').int): import('./void').void;
    getDuration(): import('./int').int;
    setDuration(duration: import('./int').int): import('./void').void;
    getAmplifier(): import('./int').int;
    setAmplifier(amplifier: import('./int').int): import('./void').void;
}
