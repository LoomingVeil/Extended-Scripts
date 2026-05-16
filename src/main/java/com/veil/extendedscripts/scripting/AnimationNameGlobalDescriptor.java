package com.veil.extendedscripts.scripting;

import noppes.npcs.api.handler.data.IAnimation;
import noppes.npcs.controllers.AnimationController;
import noppes.npcs.controllers.CustomEffectController;
import noppes.npcs.controllers.data.CustomEffect;

import java.util.ArrayList;
import java.util.List;

public class AnimationNameGlobalDescriptor implements ScriptGlobalDescriptor {
    @Override
    public String getTypeName() {
        return "string";
    }

    @Override
    public String getGlobalName() {
        return "AnimationName";
    }

    @Override
    public List<MemberEntry> getMembers() {
        AnimationController controller = AnimationController.getInstance();
        List<MemberEntry> members = new ArrayList<MemberEntry>(controller.getAnimations().length);

        for (IAnimation animation : controller.getAnimations()) {
            String key = ScriptGlobalRegistry.toIdentifier(animation.getName());
            members.add(new MemberEntry(
                key,
                animation.getName(),
                getTypeName(),
                ""
            ));
        }

        return members;
    }
}
