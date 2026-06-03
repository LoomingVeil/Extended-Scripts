package com.veil.extendedscripts.extendedapi.event;

import noppes.npcs.api.event.IPlayerEvent;

/**
 * Fired at various times including switching the held item, equipping armor, and logging in.
 *
 * @hookName attributeRecalculate
 */
public interface IAttributeRecalculateEvent extends IPlayerEvent {
}
