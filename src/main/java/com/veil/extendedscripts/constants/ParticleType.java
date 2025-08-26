package com.veil.extendedscripts.constants;

/**
 * {@link net.minecraft.client.renderer.RenderGlobal#spawnParticle(String, double, double, double, double, double, double) for particle list}
 */
public class ParticleType {
    public static final ParticleType Instance = new ParticleType();
    public final String HUGE_EXPLOSION = "hugeexplosion";
    public final String LARGE_EXPLOSION = "largeexplode";
    public final String FIREWORKS_SPARK = "fireworksSpark";
    public final String BUBBLE = "bubble";
    public final String SUSPENDED = "suspended";
    public final String DEPTH_SUSPENDED = "depthsuspend";
    public final String TOWN_AURA = "townaura";
    public final String CRIT = "crit";
    public final String MAGIC_CRIT = "magicCrit";
}
