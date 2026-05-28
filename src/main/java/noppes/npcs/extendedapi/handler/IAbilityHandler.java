package noppes.npcs.extendedapi.handler;

import noppes.npcs.api.ability.IAbility;

public interface IAbilityHandler {
    IAbility getCustomAbilityByUUID(String UUID);

    IAbility getCustomAbilityByName(String name);
}
