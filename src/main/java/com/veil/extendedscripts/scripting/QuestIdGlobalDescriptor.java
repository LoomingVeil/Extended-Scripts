package com.veil.extendedscripts.scripting;

import noppes.npcs.constants.EnumQuestType;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.data.Quest;

import java.util.ArrayList;
import java.util.List;

public class QuestIdGlobalDescriptor implements ScriptGlobalDescriptor {
    @Override
    public String getTypeName() {
        return "number";
    }

    @Override
    public String getGlobalName() {
        return "QuestID";
    }

    @Override
    public List<MemberEntry> getMembers() {
        QuestController controller = QuestController.Instance;
        List<MemberEntry> members = new ArrayList<MemberEntry>(controller.quests.size());

        for (Quest quest : controller.quests.values()) {
            String key = ScriptGlobalRegistry.toIdentifier(quest.getName());
            members.add(new MemberEntry(
               key,
               quest.getId(),
               getTypeName(),
               buildQuestDocumentation(key, quest.getId(), quest)
            ));
        }

        return members;
    }

    private static String buildQuestDocumentation(String key, int questId, Quest quest) {
        if (quest == null) {
            return "Quest Key: " + key + "\nQuest ID: " + questId;
        }

        String category = (quest.category != null && quest.category.title != null && !quest.category.title.isEmpty())
            ? quest.category.title
            : "Uncategorized";
        String type = safeQuestTypeName(quest.type);

        return "Quest Key: " + key
            + "\nQuest ID: " + questId
            + "\nTitle: " + quest.title
            + "\nCategory: " + category
            + "\nType: " + type;
    }

    private static String safeQuestTypeName(EnumQuestType questType) {
        return questType != null ? questType.name() : "Unknown";
    }
}
