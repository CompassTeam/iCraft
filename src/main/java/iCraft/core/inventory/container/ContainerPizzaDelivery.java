package iCraft.core.inventory.container;

import iCraft.core.entity.EntityPizzaDelivery;
import iCraft.core.inventory.slot.SlotDelivery;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerPizzaDelivery extends ContainerBase
{
	private EntityPizzaDelivery delivery;

	public ContainerPizzaDelivery(InventoryPlayer inventory, EntityPizzaDelivery delivery)
	{
		this.delivery = delivery;
		this.delivery.openInventory();

		addSlotToContainer(new Slot(this.delivery, 0, 62, 53));
		addSlotToContainer(new SlotDelivery(this.delivery, 1, 120, 53));
		addPlayerSlots(inventory, 8, 84);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventory)
	{
		delivery.resetSlotContents();
		super.onCraftMatrixChanged(inventory);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotID)
	{
		ItemStack stack = null;
		Slot currentSlot = (Slot) inventorySlots.get(slotID);

		if (currentSlot != null && currentSlot.getHasStack())
		{
			ItemStack slotStack = currentSlot.getStack();
			stack = slotStack.copy();

			if (slotID == 1)
			{
				if (!mergeItemStack(slotStack, 2, 38, true))
				{
					return null;
				}

				currentSlot.onSlotChange(slotStack, stack);
			}
			else if (slotID != 0)
			{
				if (slotID >= 2 && slotID < 29)
				{
					if (!mergeItemStack(slotStack, 29, 38, false))
					{
						return null;
					}
				}
				else if (slotID >= 29 && slotID < 38 && !mergeItemStack(slotStack, 2, 29, false))
				{
					return null;
				}
			}
			else if (!mergeItemStack(slotStack, 2, 38, false))
			{
				return null;
			}
			if (slotStack.stackSize == 0)
			{
				currentSlot.putStack(null);
			}
			else
			{
				currentSlot.onSlotChanged();
			}
			if (slotStack.stackSize == stack.stackSize)
			{
				return null;
			}
			currentSlot.onPickupFromSlot(player, slotStack);
		}
		return stack;
	}

	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		super.onContainerClosed(player);
		delivery.closeInventory();
	}
}