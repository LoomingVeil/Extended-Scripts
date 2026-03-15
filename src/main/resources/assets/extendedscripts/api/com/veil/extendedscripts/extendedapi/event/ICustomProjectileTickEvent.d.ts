import { ICustomProjectile } from "./../entity/ICustomProjectile";
import { IObject } from "./../../../../../IObject";


export interface ICustomProjectileTickEvent extends IObject {
	getHookName(): string;

	getProjectile(): ICustomProjectile;

	getID(): number;

}

