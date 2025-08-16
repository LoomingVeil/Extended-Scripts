package com.veil.extendedscripts;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import noppes.npcs.CustomItems;
import noppes.npcs.api.AbstractNpcAPI;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.scripted.NpcAPI;
import noppes.npcs.scripted.constants.EntityType;
import noppes.npcs.scripted.entity.ScriptNpc;

import java.util.List;
import java.util.UUID;


public class WorldClippers extends ItemSword {
    public WorldClippers() {
        super(ToolMaterial.WOOD);
        this.setUnlocalizedName("world_clippers");
        this.setTextureName("extendedscripts:world_clippers");
        this.setCreativeTab(CustomItems.tab);
        this.setMaxStackSize(1);
        this.setFull3D();
    }

    @Override
    public boolean func_150897_b(Block block) {
        // false = not effective against any block. No idea what this function is
        if (block == Blocks.web) return true;
        return false;
    }

    @Override
    public float func_150931_i() { // I believe this getDamageToEntity;
        return 0;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.none;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase p_77644_2_, EntityLivingBase p_77644_3_) {
        return false;
    }

    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta) {
        if (block != Blocks.web) {
            return 15;
        }
        return 0.0F;
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack stack) {
        if (block == Blocks.web) return true;
        return false;
    }

    @Override
    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        setBlockTarget(item, world, player, x, y, z);
        return true;
    }

    public void setBlockTarget(ItemStack item, World world, EntityPlayer player, int x, int y, int z) {
        if (!world.isRemote) {
            NBTTagCompound inspectTag = getInspectTag(item);

            // Store Block Target Data
            inspectTag.setByte("TargetType", (byte) 1); // 1 = Block target
            inspectTag.setInteger("TargetX", x);
            inspectTag.setInteger("TargetY", y);
            inspectTag.setInteger("TargetZ", z);
            inspectTag.setInteger("TargetDimension", world.provider.dimensionId);

            // Clear any old entity data
            inspectTag.removeTag("TargetUUID");

            // Lazy way to go about it, but it works
            AbstractNpcAPI.Instance().getIItemStack(item).setLore(new String[] {
                "§f" + world.getBlock(x, y ,z).getLocalizedName(),
                "§fAt: " + x + ", " + y + ", " + z,
                "§fIn Dimension: " + world.provider.getDimensionName()
            });

            player.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Block Target Acquired"));
        }
    }


    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        if (!world.isRemote) {
            if (player.isSneaking()) {
                clearTarget(item);
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Target Cleared."));
                return item;
            }
        }
        return item;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack item, EntityPlayer player, EntityLivingBase target) {
        if (!player.worldObj.isRemote) {
            setEntityTarget(item, player, target);
        }
        return true;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack item, EntityPlayer player, Entity target) {
        if (!player.worldObj.isRemote) {
            setEntityTarget(item, player, target);
        }
        return true;
    }

    public void setEntityTarget(ItemStack stack, EntityPlayer player, Entity target) {
        ItemStack heldItem = player.getCurrentEquippedItem();
        NBTTagCompound inspectTag = getInspectTag(heldItem);

        Entity targetEntity = target;
        inspectTag.setByte("TargetType", (byte) 2); // 2 = Entity target
        inspectTag.setString("TargetUUID", targetEntity.getUniqueID().toString());
        inspectTag.setInteger("TargetDimension", target.worldObj.provider.dimensionId);

        inspectTag.removeTag("TargetX");
        inspectTag.removeTag("TargetY");
        inspectTag.removeTag("TargetZ");

        NBTTagCompound entityNBT = new NBTTagCompound();
        target.writeToNBT(entityNBT);
        String customName = entityNBT.getString("CustomName");
        if (customName == null) customName = "";
        if (!customName.equals("")) {
            customName = " named \"" + customName + "\"";
        }

        IEntity npcEntity = AbstractNpcAPI.Instance().getIEntity(targetEntity);
        if (npcEntity.getType() == EntityType.NPC) {
            ScriptNpc npc = (ScriptNpc) npcEntity;
            customName = " named \"" + npc.getName() + "\"";
        }

        AbstractNpcAPI.Instance().getIItemStack(heldItem).setLore(new String[] {
            "§f" + EntityList.getEntityString(targetEntity) + customName,
            "§fIn Dimension: " + targetEntity.worldObj.provider.getDimensionName()
        });

        player.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Entity Target Acquired"));
    }

    @Override
    public void onUpdate(ItemStack item, World world, Entity _entity, int slot, boolean isHeld) {
        super.onUpdate(item, world, _entity, slot, isHeld);
        NBTTagCompound inspectTag = getInspectTag(item);

        WorldServer targetWorld = MinecraftServer.getServer().worldServerForDimension(inspectTag.getInteger("TargetDimension"));
        if (targetWorld == null) {
            return;
        }

        if (!inspectTag.hasKey("TargetType")) return;

        if (inspectTag.getByte("TargetType") == 1) { // Block Type
            int x = inspectTag.getInteger("TargetX");
            int y = inspectTag.getInteger("TargetY");
            int z = inspectTag.getInteger("TargetZ");
            Block block = targetWorld.getBlock(x, y, z);
            String blockName = block.getLocalizedName();

            IItemStack npcItem = NpcAPI.Instance().getIItemStack(item);

            if (blockName == null || blockName.equals("tile.air.name")) {
                if (!npcItem.getLore()[0].equals("§fAir")) {
                    npcItem.setLore(new String[] {
                        "§fAir",
                        "§fAt: " + x + ", " + y + ", " + z,
                        "§fIn Dimension: " + world.provider.getDimensionName()
                    });
                }
            } else {
                if (!npcItem.getLore()[0].equals("§f" + blockName)) {
                    npcItem.setLore(new String[] {
                        "§f" + blockName,
                        "§fAt: " + x + ", " + y + ", " + z,
                        "§fIn Dimension: " + world.provider.getDimensionName()
                    });
                }
            }
        } else if (inspectTag.getByte("TargetType") == 2) { // Entity Type
            String UUIDString = inspectTag.getString("TargetUUID");
            UUID targetUUID;
            try {
                targetUUID = UUID.fromString(UUIDString); // Parse the UUID string
            } catch (IllegalArgumentException e) {
                return;
            }

            Entity foundEntity = null;
            List<Entity> allLoadedEntities = targetWorld.loadedEntityList;
            for (Object obj : allLoadedEntities) {
                if (obj instanceof Entity) {
                    Entity entity = (Entity) obj;
                    if (entity.getUniqueID().equals(targetUUID)) {
                        foundEntity = entity;
                        break;
                    }
                }
            }

            IItemStack npcItem = NpcAPI.Instance().getIItemStack(item);
            String[] currentLore = npcItem.getLore();

            if (foundEntity == null) {
                if (!currentLore[0].equals("§4Target has despawned or been killed")) {
                    npcItem.setLore(new String[]{
                        "§4Target has despawned or been killed",
                        currentLore[0],
                        currentLore[1]
                    });
                }
            } else {
                if (currentLore[0].equals("§4Target has despawned or been killed")) {
                    npcItem.setLore(new String[]{
                        currentLore[1],
                        currentLore[2]
                    });
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        super.addInformation(stack, player, list, advanced);
        list.add("§cCut open the world and see whats inside §f/inspect.");
    }

    @Override
    public boolean hasEffect(ItemStack item, int pass) {
        return hasInspectTag(item);
    }

    public static boolean hasInspectTag(ItemStack item) {
        if (item.hasTagCompound()) {
            NBTTagCompound tag = item.getTagCompound();
            if (tag.hasKey("InspectTarget")) {
                NBTTagCompound inspectTag = tag.getCompoundTag("InspectTarget");
                return inspectTag.getByte("TargetType") != (byte) 0; // True if type is 1 (block) or 2 (entity)
            }
        }
        return false;
    }

    /**
     * Helper method to get or create the NBTTagCompound for our inspection data on the ItemStack.
     */
    public static NBTTagCompound getInspectTag(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound tag = stack.getTagCompound();

        if (!tag.hasKey("InspectTarget")) {
            tag.setTag("InspectTarget", new NBTTagCompound());
        }
        return tag.getCompoundTag("InspectTarget");
    }

    /**
     * Clears the current target data from the item's NBT.
     */
    private void clearTarget(ItemStack stack) {
        if (stack.hasTagCompound()) {
            stack.getTagCompound().removeTag("InspectTarget");
        }
    }
}
