package iCraft.core;

import iCraft.core.item.ItemiCraft;
import iCraft.core.network.MessageConfigSync;
import iCraft.core.network.NetworkHandler;

import java.util.Arrays;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class CommonPlayerTracker
{
	public CommonPlayerTracker()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event)
	{
		if(!event.player.worldObj.isRemote)
		{
			NetworkHandler.sendTo(new MessageConfigSync(), (EntityPlayerMP)event.player);
			ICraft.logger.info("Sent config to '" + event.player.getDisplayName() + ".'");
		}

		List<ItemStack> itemStacks = Arrays.asList(event.player.inventory.mainInventory);
		for (ItemStack itemStack : itemStacks)
		{
			if (itemStack != null && itemStack.getItem() instanceof ItemiCraft)
			{
				if (itemStack.stackTagCompound != null && itemStack.stackTagCompound.hasKey("called"))
				{
					itemStack.stackTagCompound.setInteger("called", 0);
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent event)
	{
		List<ItemStack> itemStacks = Arrays.asList(event.player.inventory.mainInventory);
		for (ItemStack itemStack : itemStacks)
		{
			if (itemStack != null && itemStack.getItem() instanceof ItemiCraft)
			{
				if (itemStack.stackTagCompound != null && itemStack.stackTagCompound.hasKey("called") && itemStack.stackTagCompound.getInteger("called") != 0 && itemStack.stackTagCompound.hasKey("isCalling"))
				{
					for (EntityPlayerMP players : (List<EntityPlayerMP>)FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList)
					{
						if (players.getCommandSenderName().equals((itemStack.stackTagCompound.getBoolean("isCalling") ? itemStack.stackTagCompound.getString("calledPlayer") : itemStack.stackTagCompound.getString("callingPlayer"))))
						{
							List<ItemStack> stacks = Arrays.asList(players.inventory.mainInventory);
							for (ItemStack stack : stacks)
							{
								if (stack != null && stack.getItem() instanceof ItemiCraft)
								{
									ItemiCraft iCraft = (ItemiCraft) stack.getItem();
									if (stack.stackTagCompound != null && iCraft.getNumber(stack) == (itemStack.stackTagCompound.getBoolean("isCalling") ? itemStack.stackTagCompound.getInteger("calledNumber") : itemStack.stackTagCompound.getInteger("callingNumber")))
									{
										stack.stackTagCompound.setInteger("called", 0);
										players.closeScreen();
									}
								}
							}
						}
					}
				}
			}
		}
	}
}