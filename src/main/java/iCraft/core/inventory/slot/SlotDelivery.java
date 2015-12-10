package iCraft.core.inventory.slot;

import iCraft.core.entity.EntityPizzaDelivery;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotDelivery extends Slot
{
	private EntityPizzaDelivery delivery;

	public SlotDelivery(IInventory inventory, int index, int x, int y)
	{
		super(inventory, index, x, y);
		delivery = (EntityPizzaDelivery) inventory;
	}

	@Override
	public boolean isItemValid(ItemStack itemStack)
	{
		return false;
	}

	@Override
	public void onPickupFromSlot(EntityPlayer player, ItemStack itemStack)
	{
		ItemStack toSell = delivery.getStackInSlot(0);
		if (canBuy(toSell))
		{
			delivery.setTrade(true);
			delivery.setAngry(false);
			if (toSell != null && toSell.stackSize <= 0)
			{
				toSell = null;
			}
			delivery.setInventorySlotContents(0, toSell);
		}
	}

	private boolean canBuy(ItemStack itemStack)
	{
		if (itemStack != null && itemStack.getItem() == Items.iron_ingot)
		{
			itemStack.stackSize -= (delivery.getQuantity() * 2);
			return true;
		}
		return false;
	}
}