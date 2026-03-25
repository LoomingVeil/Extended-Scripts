package com.veil.extendedscripts.scripting;

import noppes.npcs.controllers.CustomEffectController;
import noppes.npcs.controllers.data.CustomEffect;

import java.util.ArrayList;
import java.util.List;

public class CustomEffectGlobalDescriptor implements ScriptGlobalDescriptor {

    @Override
    public String getTypeName() {
        return "number";
    }

    @Override
    public String getGlobalName() {
        return "CustomEffectName";
    }

    @Override
    public List<MemberEntry> getMembers() {
        CustomEffectController effectHandler = CustomEffectController.getInstance();
        List<MemberEntry> members = new ArrayList<MemberEntry>(effectHandler.getCustomEffects().size());

        for (CustomEffect effect : effectHandler.getCustomEffects().values()) {
            String key = ScriptGlobalRegistry.toIdentifier(effect.getName());
            members.add(new MemberEntry(
                key,
                effect.getName(),
                getTypeName(),
                buildDocumentation(effect)
            ));
        }

        return members;
    }

    private String buildDocumentation(CustomEffect effect) {
        return "Name: "+effect.getName()
            + "ID: " + effect.getID()
            + "Update rate: " + effect.getEveryXTick() + " ticks";
    }
}
