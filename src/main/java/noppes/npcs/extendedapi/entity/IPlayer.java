package noppes.npcs.extendedapi.entity;

import com.veil.extendedscripts.ExtendedScripts;
import com.veil.extendedscripts.properties.ExtendedScriptPlayerProperties;
import com.veil.extendedscripts.properties.PlayerAttribute;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.InventoryEnderChest;
import noppes.npcs.api.IContainer;
import noppes.npcs.api.INbt;
import noppes.npcs.api.item.IItemStack;
import org.spongepowered.asm.mixin.Unique;

public interface IPlayer {
    /**
     * @return Whether the player is currently flying
     */
    boolean isFlying();

    /**
     * @return Whether the player can fly
     */
    boolean canFly();

    /**
     * Gives/Takes away the ability to fly. Doesn't impact those in creative mode.
     */
    void setCanFly(boolean value);

    void forceFlyState(boolean value);

    /**
     * @deprecated Use {@link #getCoreAttribute(String)}
     */
    float getFlightSpeed();

    /**
     * @deprecated Use {@link #setAttribute(String, float)}
     * Sets the player's horizontal fly speed multiplier. Default is 0. See {@link #setVerticalFlightSpeed(float)} for vertical flight speed.
     */
    void setFlightSpeed(float value);

    /**
     * @deprecated Use {@link #getCoreAttribute(String)}
     */
    float getVerticalFlightSpeed();

    /**
     * @deprecated Use {@link #setAttribute(String, float)}
     */
    void setVerticalFlightSpeed(float value);

    /**
     * Enables keep inventory per player. However, your items may be lost if you close the game between dying and respawning.
     * Use at your own risk.
     */
    public void setKeepInventory(boolean keepInventory);

    public boolean getKeepInventory();

    /**
     * Gives attributes to the player. These attributes are the same that can be applied to item except these attributes are always active until removed.
     */
    void setAttribute(String key, float value);

    /**
     * Gets core attributes. These attributes are separate from equipment attributes.
     */
    float getCoreAttribute(String key);

    public void resetCoreAttributes();

    /**
     * Gets the attribute core as an item that can be given to the player.
     */
    IItemStack getAttributeCore();

    String[] getCoreAttributeKeys();

    /**
     * Only for those who know what they're doing
     */
    InventoryEnderChest getMCEnderChest();

    IContainer getEnderChest();

    IItemStack getEnderChestSlot(int slot);

    void setEnderChest(IContainer contents);

    void setEnderChest(IItemStack[] items);

    void setEnderChestSlot(int slot, IItemStack item);

    void clearEnderChest();

    /**
     * Only for those who know what they're doing
     */
    InventoryPlayer getMCInventory();

    /**
     * @return A 40 slot container where (0 - 8) left to right are hotbar slots. (9 - 35) left - right, top - bottom, (36, 39) is armor
     */
    IContainer getInventoryContainer();

    /**
     * @param slot The slot to get the item from (0 - 8) left to right are hotbar slots. (9 - 35) left - right, top - bottom, (36, 39) is armor
     */
    IItemStack getInventorySlot(int slot);

    void setInventory(IContainer contents);

    void setInventory(IItemStack[] items);

    /**
     * @param slot The slot to get the item from (0 - 8) left to right are hotbar slots. (9 - 35) left - right, top - bottom, (36, 39) is armor
     */
    void setInventorySlot(int slot, IItemStack item);

    /**
     * Opens the player's ender chest for themselves.
     */
    void openEnderChest();

    /**
     * Opens a player's ender chest for a specified player
     * @param enderChestOwner The IPlayer whose ender chest is viewed
     */
    void openEnderChest(noppes.npcs.api.entity.IPlayer enderChestOwner);

    /**
     * Opens a crafting table. Any stored items are returned to the player when closed.
     */
    void openCraftingTable();

    /**
     * Opens a furnace for the player
     * @param persistent When true the furnace will retain its items. Persistent and non-persistent furnaces are separate.
     */
    void openFurnace(boolean persistent);

    /**
     * Returns the specified player's playerdata in NBT form. If you are looking for playerdata related to
     * customNpcs+ then use {@link noppes.npcs.api.entity.IPlayer#getData()}
     */
    INbt getPlayerData();

    int getSelectedHotbarSlot();

    boolean isOperator();
}
