package com.veil.extendedscripts.scripting;

import kamkeel.npcs.controllers.AbilityController;
import noppes.npcs.api.ability.IAbility;
import noppes.npcs.api.handler.data.IAnimation;

import java.util.ArrayList;
import java.util.List;

public class AbilityUUIDGlobalDescriptor implements ScriptGlobalDescriptor {
    @Override
    public String getTypeName() {
        return "string";
    }

    @Override
    public String getGlobalName() {
        return "AbilityUUID";
    }

    @Override
    public List<MemberEntry> getMembers() {
        AbilityController controller = AbilityController.Instance;
        List<MemberEntry> members = new ArrayList<MemberEntry>(controller.getAbilityNameArray().length);

        for (String abilityKey : controller.getAbilityKeys()) {
            IAbility ability = controller.getCustomAbilityByUUID(abilityKey);
            if (ability == null) continue;
            String key = ScriptGlobalRegistry.toIdentifier(ability.getName());

            members.add(new MemberEntry(
                key,
                ability.getId(),
                getTypeName(),
                buildDocumentation(ability)
            ));
        }

        return members;
    }

    private String buildDocumentation(IAbility ability) {
        return "Display Name: "+ability.getDisplayName()
            + "\nName: " + ability.getName()
            + "\nUUID: " + ability.getId()
            + "\nType: " + ability.getTypeId();
    }
}
