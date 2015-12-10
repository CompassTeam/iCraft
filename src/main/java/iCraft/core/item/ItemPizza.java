package iCraft.core.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;

public class ItemPizza extends ItemFood
{
	public ItemPizza()
	{
		super(8, false);
		setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	public void registerIcons(IIconRegister register)
	{
		itemIcon = register.registerIcon("icraft:" + getUnlocalizedName().replace("item.", ""));
	}
}