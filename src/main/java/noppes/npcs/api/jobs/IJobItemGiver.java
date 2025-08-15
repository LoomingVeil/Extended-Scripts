package noppes.npcs.api.jobs;

import noppes.npcs.api.item.IItemStack;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.handler.data.IAvailability;

public interface IJobItemGiver extends IJob {

    void setCooldown(int cooldown);

    void setCooldownType(int type);

    int getCooldownType();

    void setGivingMethod(int method);

    int getGivingMethod();

    void setLines(String[] lines);

    String[] getLines();

    void setAvailability(IAvailability availability);

    IAvailability getAvailability();

    void setItem(int slot, IItemStack item);

    IItemStack[] getItems();

    boolean giveItems(IPlayer player);

    boolean canPlayerInteract(IPlayer player);
}
