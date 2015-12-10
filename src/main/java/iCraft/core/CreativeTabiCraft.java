package iCraft.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Probably I'll never use this class, but who knows?
 */
public class CreativeTabiCraft extends CreativeTabs
{
	public CreativeTabiCraft()
	{
		super("tabiCraft");
	}

	@Override
	public ItemStack getIconItemStack()
	{
		return new ItemStack(ICraft.iCraft);
	}

	@Override
	public Item getTabIconItem()
	{
		return ICraft.iCraft;
	}
}