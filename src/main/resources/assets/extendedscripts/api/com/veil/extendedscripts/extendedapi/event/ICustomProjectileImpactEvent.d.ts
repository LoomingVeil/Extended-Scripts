import { ICustomProjectile } from "./../entity/ICustomProjectile";
import { IBlock } from "./../../../../../noppes/npcs/api/IBlock";
import { IObject } from "./../../../../../IObject";
import { IEntity } from "./../../../../../noppes/npcs/api/entity/IEntity";


export interface ICustomProjectileImpactEvent extends IObject {
	getHookName(): string;

	getProjectile(): ICustomProjectile;

	getID(): number;

	getTarget(): IEntity;

	getBlock(): IBlock;

	didShatter(): boolean;

	hitEntity(): boolean;

	hitBlock(): boolean;

}

