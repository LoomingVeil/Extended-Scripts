package com.veil.extendedscripts.scripting;

import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.controllers.ServerCloneController;
import noppes.npcs.scripted.entity.ScriptNpc;

import java.util.ArrayList;
import java.util.List;

public class CloneTabDescriptor implements ScriptGlobalDescriptor {
    private int tab;
    public CloneTabDescriptor(int tab) {
        this.tab = tab;
    }

    @Override
    public String getTypeName() {
        return "string";
    }

    @Override
    public String getGlobalName() {
        return "CloneTab_"+tab;
    }

    @Override
    public List<MemberEntry> getMembers() {
        ServerCloneController controller = ServerCloneController.Instance;
        List<MemberEntry> members = new ArrayList<MemberEntry>(controller.getClones(tab).size());

        for (String cloneName : controller.getClones(tab)) {
            String key = ScriptGlobalRegistry.toIdentifier(cloneName);
            members.add(new MemberEntry(
                key,
                cloneName,
                getTypeName(),
                buildDocumentation(cloneName)
            ));
        }

        return members;
    }

    public String buildDocumentation(String cloneName) {
        ScriptNpc npc = (ScriptNpc) ServerCloneController.Instance.get(tab, cloneName, AbstractNpcAPI.Instance().getIWorld(0));

        return "Name: " + npc.getName()
            + "\nTab: " + tab
            + "\nFaction: " +npc.getFaction().getName();
    }
}
