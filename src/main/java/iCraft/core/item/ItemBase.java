package iCraft.core.item;

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
}