import { IObject } from "./../../../../IObject";


export interface IPotionEffect extends IObject {
	getName(): string;

	getID(): number;

	setID(ID: number): void;

	getDuration(): number;

	setDuration(duration: number): void;

	getAmplifier(): number;

	setAmplifier(amplifier: number): void;

}

