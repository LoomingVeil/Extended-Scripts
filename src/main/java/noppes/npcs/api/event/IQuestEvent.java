package noppes.npcs.api.event;

import noppes.npcs.api.item.IItemStack;
import cpw.mods.fml.common.eventhandler.Cancelable;
import noppes.npcs.api.handler.data.IQuest;

public interface IQuestEvent extends IPlayerEvent {

    IQuest getQuest();

    interface QuestCompletedEvent extends IQuestEvent {
    }

    @Cancelable
    interface QuestStartEvent extends IQuestEvent {
    }

    @Cancelable
    interface QuestTurnedInEvent extends IQuestEvent {
        void setExpReward(int expReward);

        void setItemRewards(IItemStack[] itemRewards);

        int getExpReward();

        IItemStack[] getItemRewards();
    }
}
