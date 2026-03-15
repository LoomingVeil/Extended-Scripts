import { IObject } from "./../../../../../IObject";

import { ResourceLocation } from "./../../../../../missingTypes";

export interface ICustomProjectileRenderProperties extends IObject {
	getRenderType(): number;

	setRenderType(renderType: number): void;

	getTexturePath(): string;

	setTexture(texturePath: string): void;

	getTexture(): ResourceLocation;

	getNumSimpleRenderPlanes(): number;

	setNumSimpleRenderPlanes(numSimpleRenderPlanes: number): void;

	getRollOffset(): number;

	setRollOffset(rollOffset: number): void;

	getRotationOffset(): number;

	setRotationOffset(rotationOffset: number): void;

	getRotatingRotation(): number;

	setRotatingRotation(rotatingRotation: number): void;

	getForwardOffset(): number;

	setForwardOffset(forwardOffset: number): void;

	getRotationSpeed(): number;

	setRotationSpeed(rotationSpeed: number): void;

	getScale(): number;

	setScale(scale: number): void;

	shouldStopRotatingOnImpact(): boolean;

	setStopRotatingOnImpact(stopRotatingOnImpact: boolean): void;

	shouldOnImpactSnapToInitRotation(): boolean;

	setOnImpactSnapToInitRotation(onImpactSnapToInitRotation: boolean): void;

}

