package com.veil.extendedscripts;

import com.veil.extendedscripts.constants.CustomProjectileInvulnerableCollisionType;
import com.veil.extendedscripts.extendedapi.entity.ICustomProjectile;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.*;
import net.minecraft.world.World;
import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.item.IItemStack;

import java.util.List;

public class EntityCustomProjectile extends EntityArrow implements ICustomProjectile {
    public static final int GRAVITY_ID = 17;
    public static final int PARTICLE_TRAIL_ID = 18;
    public static final int SHATTER_PARTICLE_ID = 19;
    public static final int SHATTER_ON_IMPACT_ID = 20;
    public static final int TEXTURE_ID = 21;
    public static final int NUM_PLANES_ID = 22;
    public static final int ROLL_OFFSET_ID = 23;
    public static final int ROTATION_OFFSET_ID = 24;
    public static final int FORWARD_OFFSET_ID = 25;
    public static final int SCALE_ID = 26;
    public static final int HITBOX_X_ID = 27;
    public static final int HITBOX_Y_ID = 28;
    public static final int INVULNERABLE_COLLISION_BEHAVIOR_ID = 29;

    private int x = -1;
    private int y = -1;
    private int z = -1;
    /**
     * Not sure what this is supposed to represent
     */
    private Block field_145790_g;
    private int inData;
    public int arrowShake;
    public boolean inGround;
    private int ticksInGround;
    private int ticksInAir;
    private String particleTrail = "";
    private double damage = 2.0D;
    private boolean doesVelocityAddDamage = true;
    /**
     * The amount of knockback an arrow applies when it hits a mob.
     */
    private int knockbackStrength;
    private boolean pickupable = true;
    private ItemStack pickupItem = new ItemStack(Items.arrow);
    private boolean shatterOnImpact = false;
    private String shatterParticle = "";
    private byte invulnerableCollisionBehavior = CustomProjectileInvulnerableCollisionType.Instance.BOUNCE;
    private int penetrationCount = 0;
    private int numPenetrated = 0;
    private float hitboxSizeX = 0.5F;
    private float hitboxSizeY = 0.5F;
    private float gravity;
    private float initialVelocity = 1.5F;
    private String hitSound = "random.bowhit";
    private CustomProjectileRenderProperties renderProperties;


    public EntityCustomProjectile(World world) {
        super(world);
        renderProperties = new CustomProjectileRenderProperties(this);
        setupDataWatcher();
        System.out.println("Setting hitbox size to "+getHitboxSizeX()+", "+getHitboxSizeY());
        setSize(this.width, this.height);
    }

    public EntityCustomProjectile(World world, EntityLivingBase shooter) {
        super(world, shooter, 1);
        renderProperties = new CustomProjectileRenderProperties(this);
        this.shootingEntity = shooter;
        setupDataWatcher();
        placeInFrontOfEntity(shootingEntity, 1);
    }

    private void setupDataWatcher() {
        this.dataWatcher.addObject(GRAVITY_ID, 0.05F);
        this.dataWatcher.addObject(PARTICLE_TRAIL_ID, particleTrail);
        this.dataWatcher.addObject(SHATTER_PARTICLE_ID, shatterParticle);
        this.dataWatcher.addObject(SHATTER_ON_IMPACT_ID, (shatterOnImpact) ? (byte) 1 : (byte) 0);
        this.dataWatcher.addObject(TEXTURE_ID, "textures/entity/arrow.png");
        this.dataWatcher.addObject(NUM_PLANES_ID, 2);
        this.dataWatcher.addObject(ROLL_OFFSET_ID, 45F);
        this.dataWatcher.addObject(ROTATION_OFFSET_ID, (float) 0);
        this.dataWatcher.addObject(FORWARD_OFFSET_ID, -4.0F);
        this.dataWatcher.addObject(SCALE_ID, 1F);
        this.dataWatcher.addObject(HITBOX_X_ID, 1F);
        this.dataWatcher.addObject(HITBOX_Y_ID, 1F);
        this.dataWatcher.addObject(INVULNERABLE_COLLISION_BEHAVIOR_ID, invulnerableCollisionBehavior);
    }

    public void moveToward(double towardX, double towardY, double towardZ, float speed) {
        if (speed < 0.001) speed = 0.001F;
        double dX = towardX - this.posX;
        double dY = towardY - this.posY;
        double dZ = towardZ - this.posZ;

        double magnitude = MathHelper.sqrt_double(dX * dX + dY * dY + dZ * dZ);
        dX /= magnitude;
        dY /= magnitude;
        dZ /= magnitude;

        this.motionX = dX * speed;
        this.motionY = dY * speed;
        this.motionZ = dZ * speed;

        double horizontalDistance = MathHelper.sqrt_double(dX * dX + dZ * dZ);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(dZ, dX) * 180.0D / Math.PI) - 90.0F;
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(dY, horizontalDistance) * 180.0D / Math.PI);
    }

    public void placeInFrontOfEntity(Entity entity, float distance) {
        double spawnX = entity.posX + shootingEntity.getLookVec().xCoord * distance;
        double spawnY = entity.posY + shootingEntity.getEyeHeight() + shootingEntity.getLookVec().yCoord * distance;
        double spawnZ = entity.posZ + shootingEntity.getLookVec().zCoord * distance;
        this.setPosition(spawnX, spawnY, spawnZ);
    }

    public void placeInFrontOfEntity(IEntity entity, float distance) {
        placeInFrontOfEntity(entity.getMCEntity(), distance);
    }

    public void setVelocity(float velocity) {
        if (velocity < 0.0001) velocity = 0.0001F;
        double mx = this.motionX;
        double my = this.motionY;
        double mz = this.motionZ;
        float magnitude = MathHelper.sqrt_double(mx * mx + my * my + mz * mz);
        if (magnitude < 0.0001) return;
        mx /= magnitude;
        my /= magnitude;
        mz /= magnitude;
        mx *= velocity;
        my *= velocity;
        mz *= velocity;
        this.motionX = mx;
        this.motionY = my;
        this.motionZ = mz;
    }

    @Override
    public void onUpdate() {
        super.onEntityUpdate();

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, (double) f) * 180.0D / Math.PI);
        }

        Block block = this.worldObj.getBlock(this.x, this.y, this.z);

        if (block.getMaterial() != Material.air) {
            block.setBlockBoundsBasedOnState(this.worldObj, this.x, this.y, this.z);
            AxisAlignedBB axisalignedbb = block.getCollisionBoundingBoxFromPool(this.worldObj, this.x, this.y, this.z);

            if (axisalignedbb != null && axisalignedbb.isVecInside(Vec3.createVectorHelper(this.posX, this.posY, this.posZ))) {
                this.inGround = true;
            }
        }

        if (this.arrowShake > 0) {
            --this.arrowShake;
        }

        if (this.inGround) {
            int j = this.worldObj.getBlockMetadata(this.x, this.y, this.z);

            if (block == this.field_145790_g && j == this.inData) {
                ++this.ticksInGround;

                if (this.ticksInGround == 1200 || this.ticksInAir == 1200) {
                    this.setDead();
                }
            } else {
                this.inGround = false;
                this.motionX *= (this.rand.nextFloat() * 0.2F);
                this.motionY *= (this.rand.nextFloat() * 0.2F);
                this.motionZ *= (this.rand.nextFloat() * 0.2F);
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        } else {
            ++this.ticksInAir;
            Vec3 vec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            Vec3 vec3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition movingobjectposition = this.worldObj.func_147447_a(vec31, vec3, false, true, false);
            vec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            vec3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (movingobjectposition != null) {
                vec3 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
            }

            Entity entity = null;
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            int i;
            float gravity;

            for (i = 0; i < list.size(); ++i) {
                Entity entity1 = (Entity) list.get(i);

                if (entity1.canBeCollidedWith() && (entity1 != this.shootingEntity || this.ticksInAir >= 5)) {
                    gravity = 0.3F;
                    AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand((double) gravity, (double) gravity, (double) gravity);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31, vec3);

                    if (movingobjectposition1 != null) {
                        double d1 = vec31.distanceTo(movingobjectposition1.hitVec);

                        if (d1 < d0 || d0 == 0.0D) {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }

            if (movingobjectposition != null && movingobjectposition.entityHit != null && movingobjectposition.entityHit instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer) movingobjectposition.entityHit;

                if (entityplayer.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer && !((EntityPlayer) this.shootingEntity).canAttackPlayer(entityplayer)) {
                    movingobjectposition = null;
                }
            }

            float velocityMagnatude = 1;
            float knockback;

            if (movingobjectposition != null) {
                if (movingobjectposition.entityHit != null) {
                    // When the projectile hits an entity
                    if (doesVelocityAddDamage) {
                        velocityMagnatude = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    }

                    int damage = MathHelper.ceiling_double_int((double) velocityMagnatude * this.damage);

                    DamageSource damagesource = null;

                    if (this.shootingEntity == null) {
                        damagesource = DamageSource.causeArrowDamage(this, this);
                    } else {
                        damagesource = DamageSource.causeArrowDamage(this, this.shootingEntity);
                    }

                    if (this.isBurning() && !(movingobjectposition.entityHit instanceof EntityEnderman)) {
                        movingobjectposition.entityHit.setFire(5);
                    }

                    if (movingobjectposition.entityHit.attackEntityFrom(damagesource, (float) damage)) {
                        if (movingobjectposition.entityHit instanceof EntityLivingBase) {
                            EntityLivingBase entitylivingbase = (EntityLivingBase) movingobjectposition.entityHit;

                            if (!this.worldObj.isRemote) {
                                entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
                            }

                            if (this.knockbackStrength > 0) {
                                knockback = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);

                                if (knockback > 0.0F) {
                                    movingobjectposition.entityHit.addVelocity(this.motionX * (double) this.knockbackStrength * 0.6000000238418579D / (double) knockback, 0.1D, this.motionZ * (double) this.knockbackStrength * 0.6000000238418579D / (double) knockback);
                                }
                            }

                            if (this.shootingEntity != null && this.shootingEntity instanceof EntityLivingBase) {
                                EnchantmentHelper.func_151384_a(entitylivingbase, this.shootingEntity);
                                EnchantmentHelper.func_151385_b((EntityLivingBase) this.shootingEntity, entitylivingbase);
                            }

                            if (this.shootingEntity != null && movingobjectposition.entityHit != this.shootingEntity && movingobjectposition.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP) {
                                ((EntityPlayerMP) this.shootingEntity).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0.0F));
                            }
                        }

                        this.playSound(hitSound, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));

                        if (!(movingobjectposition.entityHit instanceof EntityEnderman)) {
                            System.out.println("Count: "+numPenetrated+" Max: "+penetrationCount);
                            if (numPenetrated >= penetrationCount) {
                                setDead();
                            }

                            this.numPenetrated += 1;
                        }
                    } else {
                        if (this.invulnerableCollisionBehavior == CustomProjectileInvulnerableCollisionType.Instance.BOUNCE) {
                            this.motionX *= -0.10000000149011612D;
                            this.motionY *= -0.10000000149011612D;
                            this.motionZ *= -0.10000000149011612D;
                            this.rotationYaw += 180.0F;
                            this.prevRotationYaw += 180.0F;
                            this.ticksInAir = 0;
                        } else if (this.invulnerableCollisionBehavior == CustomProjectileInvulnerableCollisionType.Instance.SHATTER) {
                            String shatterParticle = getShatterParticle();
                            if (shatterParticle != null && !shatterParticle.equals("")) {
                                this.worldObj.spawnParticle(shatterParticle, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
                            }

                            setDead();
                        } // If pass through, do nothing
                    }
                } else {
                    // When the projectile hits a block
                    this.x = movingobjectposition.blockX;
                    this.y = movingobjectposition.blockY;
                    this.z = movingobjectposition.blockZ;
                    this.field_145790_g = this.worldObj.getBlock(this.x, this.y, this.z);
                    this.inData = this.worldObj.getBlockMetadata(this.x, this.y, this.z);
                    this.motionX = ((float) (movingobjectposition.hitVec.xCoord - this.posX));
                    this.motionY = ((float) (movingobjectposition.hitVec.yCoord - this.posY));
                    this.motionZ = ((float) (movingobjectposition.hitVec.zCoord - this.posZ));
                    velocityMagnatude = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    this.posX -= this.motionX / (double) velocityMagnatude * 0.05000000074505806D;
                    this.posY -= this.motionY / (double) velocityMagnatude * 0.05000000074505806D;
                    this.posZ -= this.motionZ / (double) velocityMagnatude * 0.05000000074505806D;
                    this.playSound(hitSound, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    this.inGround = true;
                    this.arrowShake = 7;

                    if (this.field_145790_g.getMaterial() != Material.air) {
                        this.field_145790_g.onEntityCollidedWithBlock(this.worldObj, this.x, this.y, this.z, this);
                    }

                    if (this.doesShatterOnImpact()) {
                        this.worldObj.spawnParticle(this.getShatterParticle(), this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
                        this.setDead();
                    }
                }
            }

            boolean hasParticleTrail;
            String particleTrail;
            if (worldObj.isRemote) {
                particleTrail = this.dataWatcher.getWatchableObjectString(PARTICLE_TRAIL_ID);
            } else {
                particleTrail = this.particleTrail;
            }

            if (particleTrail != null && !particleTrail.equals("")) {
                for (i = 0; i < 4; ++i) {
                    this.worldObj.spawnParticle(particleTrail, this.posX + this.motionX * (double) i / 4.0D, this.posY + this.motionY * (double) i / 4.0D, this.posZ + this.motionZ * (double) i / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ);
                }
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;

            velocityMagnatude = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

            for (this.rotationPitch = (float) (Math.atan2(this.motionY, (double) velocityMagnatude) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
                ;
            }

            while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
                this.prevRotationPitch += 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
                this.prevRotationYaw -= 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;

            float drag = 0.99F;

            if (this.isInWater()) {
                for (int l = 0; l < 4; ++l) {
                    knockback = 0.25F;
                    this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double) knockback, this.posY - this.motionY * (double) knockback, this.posZ - this.motionZ * (double) knockback, this.motionX, this.motionY, this.motionZ);
                }

                drag = 0.8F;
            }

            if (this.isWet()) {
                this.extinguish();
            }

            this.motionX *= drag;
            this.motionY *= drag;
            this.motionZ *= drag;

            if (worldObj.isRemote) {
                this.motionY -= this.dataWatcher.getWatchableObjectFloat(17);
            } else {
                this.motionY -= this.gravity;
            }

            this.setPosition(this.posX, this.posY, this.posZ);
            this.func_145775_I();
        }
    }

    public int getType() {
        return 7;
    }

    public CustomProjectileRenderProperties getRenderProperties() {
        return renderProperties;
    }

    public boolean isPickupable() {
        return pickupable;
    }

    public void setPickupable(boolean pickupable) {
        this.pickupable = pickupable;
    }

    public IItemStack getPickupItem() {
        return AbstractNpcAPI.Instance().getIItemStack(pickupItem);
    }

    public void setPickupItem(IItemStack pickupItem) {
        this.pickupItem = pickupItem.getMCItemStack();
    }

    public boolean doesShatterOnImpact() {
        byte value = dataWatcher.getWatchableObjectByte(SHATTER_ON_IMPACT_ID);
        if (value == 1) return true;
        return false;
    }

    public void setShatterOnImpact(boolean shatterOnImpact) {
        this.shatterOnImpact = shatterOnImpact;
        byte value = 0;
        if (shatterOnImpact) value = 1;
        if (!worldObj.isRemote) {
            this.dataWatcher.updateObject(SHATTER_ON_IMPACT_ID, value);
        }
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
        if (!worldObj.isRemote) {
            this.dataWatcher.updateObject(GRAVITY_ID, Float.valueOf(gravity));
        }
    }

    public float getInitialVelocity() {
        return initialVelocity;
    }

    public void setInitialVelocity(float initialVelocity) {
        this.initialVelocity = initialVelocity;

        setVelocity(initialVelocity);
    }

    public String getParticleTrail() {
        return this.dataWatcher.getWatchableObjectString(PARTICLE_TRAIL_ID);
    }

    public void setParticleTrail(String particleTrail) {
        this.particleTrail = particleTrail;
        if (!worldObj.isRemote) {
            this.dataWatcher.updateObject(PARTICLE_TRAIL_ID, particleTrail);
        }
    }

    public boolean getDoesVelocityAddDamage() {
        return doesVelocityAddDamage;
    }

    public void setDoesVelocityAddDamage(boolean doesVelocityAddDamage) {
        this.doesVelocityAddDamage = doesVelocityAddDamage;
    }

    public String getShatterParticle() {
        return dataWatcher.getWatchableObjectString(SHATTER_PARTICLE_ID);
    }

    public void setShatterParticle(String shatterParticle) {
        this.shatterParticle = shatterParticle;
        if (!worldObj.isRemote) {
            this.dataWatcher.updateObject(SHATTER_PARTICLE_ID, shatterParticle);
        }
    }

    public byte getInvulnerableCollisionBehavior() {
        return dataWatcher.getWatchableObjectByte(INVULNERABLE_COLLISION_BEHAVIOR_ID);
    }

    public void setInvulnerableCollisionBehavior(byte invulnerableCollisionBehavior) {
        this.invulnerableCollisionBehavior = invulnerableCollisionBehavior;
        if (!worldObj.isRemote) {
            this.dataWatcher.updateObject(INVULNERABLE_COLLISION_BEHAVIOR_ID, invulnerableCollisionBehavior);
        }
    }

    public int getPenetrationCount() {
        return penetrationCount;
    }

    public void setPenetrationCount(int penetrationCount) {
        this.penetrationCount = penetrationCount;
    }

    public float getHitboxSizeX() {
        return dataWatcher.getWatchableObjectFloat(HITBOX_X_ID);
    }

    public void setHitboxSizeX(float hitboxSizeX) {
        this.hitboxSizeX = hitboxSizeX;
        System.out.println("Setting hitbox size to "+hitboxSizeX+", "+hitboxSizeY);
        this.setSize(hitboxSizeX, hitboxSizeY);
        if (!worldObj.isRemote) {
            this.dataWatcher.updateObject(HITBOX_X_ID, hitboxSizeX);
        }
    }

    public float getHitboxSizeY() {
        return dataWatcher.getWatchableObjectFloat(HITBOX_Y_ID);
    }

    public void setHitboxSizeY(float hitboxSizeY) {
        this.hitboxSizeY = hitboxSizeY;
        System.out.println("Setting hitbox size to "+hitboxSizeX+", "+hitboxSizeY);
        this.setSize(hitboxSizeX, hitboxSizeY);
        if (!worldObj.isRemote) {
            this.dataWatcher.updateObject(HITBOX_Y_ID, hitboxSizeY);
        }
    }

    public String getHitSound() {
        return hitSound;
    }

    public void setHitSound(String hitSound) {
        this.hitSound = hitSound;
    }

    @Override
    public double getDamage() {
        return damage;
    }

    @Override
    public void setDamage(double damage) {
        this.damage = damage;
    }

    public int getKnockbackStrength() {
        return knockbackStrength;
    }

    @Override
    public void setKnockbackStrength(int knockbackStrength) {
        this.knockbackStrength = knockbackStrength;
    }

    public EntityCustomProjectile getMCEntity() {
        return this;
    }

    public Entity getShooter() {
        return getShooter();
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer player) {
        if (!this.worldObj.isRemote && this.inGround && this.arrowShake <= 0 && this.pickupable) {
            if (player.capabilities.isCreativeMode) {
                this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                this.setDead();
                return;
            }

            if (player.inventory.addItemStackToInventory(pickupItem)) {
                this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                player.onItemPickup(this, 1);
                this.setDead();
            }
        }
    }

    @Override
    public void readEntityFromNBT(net.minecraft.nbt.NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        if (nbt.hasKey("texturePath")) {
            this.renderProperties.setTexture(nbt.getString("texturePath"));
        }
        if (nbt.hasKey("numPlanes")) {
            this.renderProperties.setNumSimpleRenderPlanes(nbt.getInteger("numPlanes"));
        }
        if (nbt.hasKey("planeRotationOffset")) {
            this.renderProperties.setRotationOffset(nbt.getFloat("planeRotationOffset"));
        }
    }

    @Override
    public void writeEntityToNBT(net.minecraft.nbt.NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        // Write the texture path to NBT for saving to disk.
        nbt.setString("texturePath", this.dataWatcher.getWatchableObjectString(TEXTURE_ID));
        nbt.setInteger("numPlanes", this.dataWatcher.getWatchableObjectInt(NUM_PLANES_ID));
        nbt.setFloat("planeRotationOffset", this.dataWatcher.getWatchableObjectFloat(ROTATION_OFFSET_ID));
    }
}
