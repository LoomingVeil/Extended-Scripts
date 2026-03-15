import { IScreenResolution } from "./../IScreenResolution";
import { IPlayerEvent } from "./../../../../../noppes/npcs/api/event/IPlayerEvent";


export interface IResolutionChangedEvent extends IPlayerEvent {
	getOldResolution(): IScreenResolution;

	getNewResolution(): IScreenResolution;

}

