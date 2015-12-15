package iCraft.core.tile;

import iCraft.core.utils.ICraftUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public abstract class TileInventory extends TileBlock implements ISidedInventory
{
    /**
     * The inventory slot itemstacks used by this block.
     */
    public ItemStack[] inventory;

    /**
     * The full name of this tile.
     */
    public String fullName;

    /**
     * A simple tile entity with a container and facing state.
     *
     * @param name - full name of this tile entity
     */
    public TileInventory(String name)
    {
        fullName = name;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTags)
    {
        super.readFromNBT(nbtTags);

        NBTTagList tagList = nbtTags.getTagList("Items", NBT.TAG_COMPOUND);
        inventory = new ItemStack[getSizeInventory()];

        for (int tagCount = 0; tagCount < tagList.tagCount(); tagCount++)
        {
            NBTTagCompound tagCompound = tagList.getCompoundTagAt(tagCount);
            byte slotID = tagCompound.getByte("Slot");

            if (slotID >= 0 && slotID < getSizeInventory())
                setInventorySlotContents(slotID, ItemStack.loadItemStackFromNBT(tagCompound));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTags)
    {
        super.writeToNBT(nbtTags);

        NBTTagList tagList = new NBTTagList();

        for (int slotCount = 0; slotCount < getSizeInventory(); slotCount++)
        {
            if (getStackInSlot(slotCount) != null)
            {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) slotCount);
                getStackInSlot(slotCount).writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }

        nbtTags.setTag("Items", tagList);
    }

    @Override
    public int getSizeInventory()
    {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slotID)
    {
        return inventory[slotID];
    }

    @Override
    public ItemStack decrStackSize(int slotID, int amount)
    {
        if (getStackInSlot(slotID) != null)
        {
            ItemStack tempStack;

            if (getStackInSlot(slotID).stackSize <= amount)
            {
                tempStack = getStackInSlot(slotID);
                setInventorySlotContents(slotID, null);
                return tempStack;
            }
            else
            {
                tempStack = getStackInSlot(slotID).splitStack(amount);

                if (getStackInSlot(slotID).stackSize == 0)
                    setInventorySlotContents(slotID, null);

                return tempStack;
            }
        } else
            return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slotID)
    {
        if (getStackInSlot(slotID) != null)
        {
            ItemStack tempStack = getStackInSlot(slotID);
            setInventorySlotContents(slotID, null);
            return tempStack;
        }
        else
            return null;
    }

    @Override
    public void setInventorySlotContents(int slotID, ItemStack itemstack)
    {
        inventory[slotID] = itemstack;

        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
            itemstack.stackSize = getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return !isInvalid();
    }

    @Override
    public String getInventoryName()
    {
        return ICraftUtils.localize(getBlockType().getUnlocalizedName() + "." + fullName + ".name");
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean hasCustomInventoryName()
    {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        return true;
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, int side)
    {
        return isItemValidForSlot(slotID, itemstack);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return new int[]{};
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, int side)
    {
        return true;
    }
}