package iCraft.core.entity;

import iCraft.core.ICraft;
import iCraft.core.entity.ai.EntityAIAttackCrazy;
import iCraft.core.entity.ai.EntityAIDelivery;
import iCraft.core.entity.ai.EntityAIPizzaFollow;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

public class EntityPizzaDelivery extends EntityCreature implements IInventory
{
	public ItemStack[] inventory = new ItemStack[2];

	public EntityPizzaDelivery(World world)
	{
		super(world);

		setSize(0.6F, 1.8F);
		getNavigator().setBreakDoors(true);
		getNavigator().setAvoidsWater(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIMoveIndoors(this));
		tasks.addTask(2, new EntityAIRestrictOpenDoor(this));
		tasks.addTask(3, new EntityAIOpenDoor(this, true));
		tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 0.6D));
		tasks.addTask(5, new EntityAIPizzaFollow(this, 1.0F, 4.0F));
		tasks.addTask(5, new EntityAIDelivery(this, 4.0F));
		tasks.addTask(5, new EntityAIAttackCrazy(this, 1.0D));
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();

		dataWatcher.addObject(14, new String(""));
		dataWatcher.addObject(15, new Integer(0));
		dataWatcher.addObject(16, new Integer(1200));
		dataWatcher.addObject(17, new Byte((byte) 0));
		dataWatcher.addObject(18, new Byte((byte) 0));
	}

	@Override
	protected boolean isAIEnabled()
	{
		return true;
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();

		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
	}

	@Override
	protected boolean canDespawn()
	{
		return false;
	}

	@Override
	protected String getLivingSound()
	{
		return "mob.villager.idle";
	}

	@Override
	protected String getHurtSound()
	{
		return "mob.villager.hit";
	}

	@Override
	protected String getDeathSound()
	{
		return "mob.villager.death";
	}

	@Override
	protected void dropRareDrop(int i)
	{
		dropItem(ICraft.pizza, 1);
	}

	@Override
	public boolean interact(EntityPlayer player)
	{
		if(!player.isSneaking() && !getAngry())
		{
			player.openGui(ICraft.instance, 11, worldObj, getEntityId(), 0, 0);
		}
		return true;
	}

	public void teste(float hitX, float hitY, float hitZ, int id, int metadata)
	{

	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbtTags)
	{
		super.readEntityFromNBT(nbtTags);
		setPlayer(nbtTags.getString("name"));
		setQuantity(nbtTags.getInteger("quantity"));
		setPatience(nbtTags.getInteger("patience"));
		setTrade(nbtTags.getBoolean("trade"));
		setAngry(nbtTags.getBoolean("angry"));

		final NBTTagList tagList = nbtTags.getTagList("Items", NBT.TAG_COMPOUND);
		inventory = new ItemStack[2];

		for (int tagCount = 0; tagCount < tagList.tagCount(); ++tagCount)
		{
			final NBTTagCompound tagCompound = tagList.getCompoundTagAt(tagCount);
			final int slotID = tagCompound.getByte("Slot") & 255;

			if (slotID < inventory.length)
			{
				inventory[slotID] = ItemStack.loadItemStackFromNBT(tagCompound);
			}
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbtTags)
	{
		super.writeEntityToNBT(nbtTags);
		nbtTags.setString("name", getPlayer());
		nbtTags.setInteger("quantity", getQuantity());
		nbtTags.setInteger("patience", getPatience());
		nbtTags.setBoolean("trade", getTrade());
		nbtTags.setBoolean("angry", getAngry());

		final NBTTagList tagList = new NBTTagList();

        for (int slotCount = 0; slotCount < inventory.length; ++slotCount)
        {
            if (inventory[slotCount] != null)
            {
                final NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) slotCount);
                inventory[slotCount].writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }

        nbtTags.setTag("Items", tagList);
	}

	public String getPlayer()
	{
		return dataWatcher.getWatchableObjectString(14);
	}

	public void setPlayer(String name)
	{
		dataWatcher.updateObject(14, name);
	}

	public int getQuantity()
	{
		return dataWatcher.getWatchableObjectInt(15);
	}

	public void setQuantity(int newQuantity)
	{
		dataWatcher.updateObject(15, newQuantity);
	}

	public int getPatience()
	{
		return dataWatcher.getWatchableObjectInt(16);
	}

	public void setPatience(int i)
	{
		dataWatcher.updateObject(16, i);
	}

	public boolean getTrade()
	{
		return dataWatcher.getWatchableObjectByte(17) == 1;
	}

	public void setTrade(boolean status)
	{
		dataWatcher.updateObject(17, status ? (byte)1 : (byte)0);
	}

	public boolean getAngry()
	{
		return dataWatcher.getWatchableObjectByte(18) == 1;
	}

	public void setAngry(boolean angry)
	{
		dataWatcher.updateObject(18, angry ? (byte)1 : (byte)0);
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
		if (inventory[slotID] != null)
		{
			ItemStack itemstack;

			if (slotID == 1)
			{
				itemstack = inventory[slotID];
				inventory[slotID] = null;
				return itemstack;
			}
			else if (inventory[slotID].stackSize <= amount)
			{
				itemstack = inventory[slotID];
				inventory[slotID] = null;

				if (inventoryResetNeededOnSlotChange(slotID))
				{
					resetSlotContents();
				}

				return itemstack;
			}
			else
			{
				itemstack = inventory[slotID].splitStack(amount);

				if (inventory[slotID].stackSize == 0)
				{
					inventory[slotID] = null;
				}

				if (inventoryResetNeededOnSlotChange(slotID))
				{
					resetSlotContents();
				}

				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}

	private boolean inventoryResetNeededOnSlotChange(int i)
	{
		return i == 0;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slotID)
	{
		if (inventory[slotID] != null)
        {
            ItemStack itemstack = inventory[slotID];
            inventory[slotID] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
	}

	@Override
	public void setInventorySlotContents(int slotID, ItemStack itemStack)
	{
		inventory[slotID] = itemStack;

		if (itemStack != null && itemStack.stackSize > getInventoryStackLimit())
		{
			itemStack.stackSize = getInventoryStackLimit();
		}
		if (inventoryResetNeededOnSlotChange(slotID))
		{
			resetSlotContents();
		}
	}

	@Override
	public String getInventoryName()
	{
		return "Pizza Delivey";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return true;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return (getQuantity() * 2);
	}

	@Override
	public void markDirty()
	{
		resetSlotContents();
	}

	public void resetSlotContents()
	{
		ItemStack toSell = inventory[0];
		if (toSell == null)
			setInventorySlotContents(1, null);
		else if (toSell.getItem() == Items.iron_ingot && toSell.stackSize >= (getQuantity() * 2) && !getTrade())
		{
			setInventorySlotContents(1, new ItemStack(ICraft.pizza, getQuantity()));
		}
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return true;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemStack)
	{
		return true;
	}
}