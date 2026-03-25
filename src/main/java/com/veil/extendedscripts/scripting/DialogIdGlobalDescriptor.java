package com.veil.extendedscripts.scripting;

import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.data.Dialog;

import java.util.ArrayList;
import java.util.List;

public class DialogIdGlobalDescriptor implements ScriptGlobalDescriptor {
    @Override
    public String getTypeName() {
        return "number";
    }

    @Override
    public String getGlobalName() {
        return "DialogID";
    }

    @Override
    public List<MemberEntry> getMembers() {
        DialogController controller = DialogController.Instance;
        List<MemberEntry> members = new ArrayList<MemberEntry>(controller.dialogs.size());

        for (Dialog dialog : controller.dialogs.values()) {
            String key = ScriptGlobalRegistry.toIdentifier(dialog.getName());
            members.add(new MemberEntry(
                key,
                dialog.getId(),
                getTypeName(),
                buildDocumentation(dialog)
            ));
        }

        return members;
    }

    public String buildDocumentation(Dialog dialog) {
        return "Name: " + dialog.getName()
            + "\nID: " + dialog.getId();
    }
}
