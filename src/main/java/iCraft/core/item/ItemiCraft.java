package iCraft.core.item;

import iCraft.core.ICraft;
import iCraft.core.utils.ICraftUtils;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * The item that names this mod
 */
public class ItemiCraft extends ItemBase
{
	public Random rand = new Random();

	public ItemiCraft()
	{
		super();
		setMaxStackSize(1);
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean flag)
	{
		super.addInformation(itemStack, entityPlayer, list, flag);

		list.add(ICraftUtils.localize("tooltip.number") + ": " + getNumber(itemStack));
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int i, boolean flag)
	{
		if (itemStack != null && !world.isRemote)
		{
			if (getNumber(itemStack) == 0)
			{
				setNumber(itemStack);
			}
		}
	}

	public void setNumber(ItemStack itemStack)
	{
		if (itemStack.stackTagCompound == null)
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}

		int number = 10000000 + rand.nextInt(90000000);
		itemStack.stackTagCompound.setInteger("number", number);
	}

	public int getNumber(ItemStack itemStack)
	{
		if (itemStack.stackTagCompound == null)
		{
			return 0;
		}

		return itemStack.stackTagCompound.getInteger("number");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		if (itemStack.stackTagCompound != null)
		{
			if (!itemStack.stackTagCompound.hasKey("called") || itemStack.stackTagCompound.getInteger("called") == 0)
				entityPlayer.openGui(ICraft.instance, 0, world, 0, 0, 0);
			else if (itemStack.stackTagCompound.getInteger("called") == 1)
				entityPlayer.openGui(ICraft.instance, 6, world, 0, 0, 0);
			else if (itemStack.stackTagCompound.getInteger("called") == 2)
				entityPlayer.openGui(ICraft.instance, 7, world, 0, 0, 0);
		}

		return itemStack;
	}

	@Override
	public EnumRarity getRarity(ItemStack itemStack)
	{
		return EnumRarity.epic;
	}
}