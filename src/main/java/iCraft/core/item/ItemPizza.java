package iCraft.core.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;

public class ItemPizza extends ItemFood
{
    public ItemPizza()
    {
        super(8, false);
        setCreativeTab(CreativeTabs.tabFood);
    }
}