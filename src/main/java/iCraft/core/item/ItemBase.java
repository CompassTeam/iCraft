package iCraft.core.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item
{
	public ItemBase()
	{
		super();
		//setCreativeTab(ICraft.tabiCraft);
		setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	public void registerIcons(IIconRegister register)
	{
		itemIcon = register.registerIcon("icraft:" + getUnlocalizedName().replace("item.", ""));
	}
}