package com.veil.extendedscripts.mixins;

import net.minecraft.util.math.BlockPos;
import noppes.npcs.api.IPos;
import noppes.npcs.scripted.NpcAPI;
import noppes.npcs.scripted.ScriptBlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = ScriptBlockPos.class)
public abstract class MixinIPosExtension implements noppes.npcs.extendedapi.IPos {


    @Shadow
    public abstract double getXD();

    @Shadow
    public abstract double getYD();

    @Shadow
    public abstract double getZD();

    @Shadow
    public abstract IPos crossProduct(double x, double y, double z);

    @Shadow
    public abstract IPos crossProduct(IPos pos);

    @Shadow
    public BlockPos blockPos;

    @Unique
    public IPos offset(IPos direction, double distance) {
        IPos normDir = direction.normalize();

        if (normDir.getXD() == 0 && normDir.getYD() == 0 && normDir.getZD() == 0) {
            return NpcAPI.Instance().getIPos(this.getXD(), this.getYD(), this.getZD());
        }

        return NpcAPI.Instance().getIPos(
            this.getXD() + (normDir.getXD() * distance),
            this.getYD() + (normDir.getYD() * distance),
            this.getZD() + (normDir.getZD() * distance)
        );
    }

    @Unique
    public IPos multiply(double scalar) {
        return NpcAPI.Instance().getIPos(this.getXD() * scalar, this.getYD() * scalar, this.getZD() * scalar);
    }

    @Unique
    public double dotProduct(IPos other) {
        return (this.getXD() * other.getXD()) +
            (this.getYD() * other.getYD()) +
            (this.getZD() * other.getZD());
    }

    @Unique
    public IPos rotateYaw(double angleDeg) {
        double angleRad = Math.toRadians(angleDeg);
        double cos = Math.cos(angleRad);
        double sin = Math.sin(angleRad);

        double x = this.getXD();
        double z = this.getZD();

        double newX = x * cos - z * sin;
        double newZ = x * sin + z * cos;

        return NpcAPI.Instance().getIPos(newX, this.getYD(), newZ);
    }

    @Unique
    public IPos rotatePitch(double angleDeg) {
        double angleRad = Math.toRadians(angleDeg);
        double cos = Math.cos(angleRad);
        double sin = Math.sin(angleRad);

        IPos right = this.crossProduct(0, 1, 0);

        if (right.getXD() == 0 && right.getYD() == 0 && right.getZD() == 0) {
            right = NpcAPI.Instance().getIPos(1, 0, 0);
        }

        IPos normRight = right.normalize();

        IPos localUp = normRight.crossProduct(this.toNpcPos());

        double newX = this.getXD() * cos + localUp.getXD() * sin;
        double newY = this.getYD() * cos + localUp.getYD() * sin;
        double newZ = this.getZD() * cos + localUp.getZD() * sin;

        return NpcAPI.Instance().getIPos(newX, newY, newZ);
    }

    @Unique
    public IPos rotateRelativeYaw(double angleDeg) {
        // Normalize original direction
        IPos forward = this.toNpcPos().normalize();

        // World up vector
        IPos up = NpcAPI.Instance().getIPos(0, 1, 0);

        // Find perpendicular "right" vector
        IPos right = forward.crossProduct(up).normalize();

        // Fallback if forward is nearly vertical
        if (this.toExtendedPos(right).dotProduct(right) < 0.000001) {
            right = NpcAPI.Instance().getIPos(1, 0, 0);
        }

        // Local up = the axis perpendicular to both forward and right
        IPos localUp = right.crossProduct(forward).normalize();

        // Convert angle to radians
        double rad = Math.toRadians(angleDeg);

        double cos = Math.cos(rad);
        double sin = Math.sin(rad);

        // Rodrigues' rotation formula around localUp axis
        IPos term1 = NpcAPI.Instance().getIPos(
            forward.getXD() * cos,
            forward.getYD() * cos,
            forward.getZD() * cos
        );

        IPos kCrossV = localUp.crossProduct(forward);

        IPos term2 = NpcAPI.Instance().getIPos(
            kCrossV.getXD() * sin,
            kCrossV.getYD() * sin,
            kCrossV.getZD() * sin
        );

        double kDotV = this.toExtendedPos(localUp).dotProduct(forward);

        IPos term3 = NpcAPI.Instance().getIPos(
            localUp.getXD() * kDotV * (1 - cos),
            localUp.getYD() * kDotV * (1 - cos),
            localUp.getZD() * kDotV * (1 - cos)
        );

        return NpcAPI.Instance().getIPos(
            term1.getXD() + term2.getXD() + term3.getXD(),
            term1.getYD() + term2.getYD() + term3.getYD(),
            term1.getZD() + term2.getZD() + term3.getZD()
        ).normalize();
    }

    @Unique
    public IPos rotateRelativePitch(double angleDeg) {
        // Normalize original direction
        IPos forward = this.toNpcPos().normalize();

        // World up vector
        IPos up = NpcAPI.Instance().getIPos(0, 1, 0);

        // Find perpendicular axis ("right" vector)
        IPos right = forward.crossProduct(up).normalize();

        // If direction is vertical, cross product becomes zero
        // so use another fallback axis
        if (this.toExtendedPos(right).dotProduct(right) < 0.000001)
        {
            right = NpcAPI.Instance().getIPos(1, 0, 0);
        }

        // Convert angle to radians
        double rad = Math.toRadians(angleDeg);

        double cos = Math.cos(rad);
        double sin = Math.sin(rad);

        // Rodrigues' rotation formula
        // v_rot = v*cosθ + (k × v)*sinθ + k*(k·v)*(1-cosθ)

        IPos term1 = NpcAPI.Instance().getIPos(
            forward.getXD() * cos,
            forward.getYD() * cos,
            forward.getZD() * cos
        );

        IPos kCrossV = right.crossProduct(forward);

        IPos term2 = NpcAPI.Instance().getIPos(
            kCrossV.getXD() * sin,
            kCrossV.getYD() * sin,
            kCrossV.getZD() * sin
        );

        double kDotV = this.toExtendedPos(right).dotProduct(forward);

        IPos term3 = NpcAPI.Instance().getIPos(
            right.getXD() * kDotV * (1 - cos),
            right.getYD() * kDotV * (1 - cos),
            right.getZD() * kDotV * (1 - cos)
        );

        return NpcAPI.Instance().getIPos(
            term1.getXD() + term2.getXD() + term3.getXD(),
            term1.getYD() + term2.getYD() + term3.getYD(),
            term1.getZD() + term2.getZD() + term3.getZD()
        ).normalize();
    }

    private IPos toNpcPos() {
        return NpcAPI.Instance().getIPos(this.blockPos);
    }

    private noppes.npcs.extendedapi.IPos toExtendedPos(IPos pos) {
        return (noppes.npcs.extendedapi.IPos) pos;
    }
}
