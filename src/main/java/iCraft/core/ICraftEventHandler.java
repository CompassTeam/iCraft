package iCraft.core;

import iCraft.core.item.ItemiCraft;
import iCraft.core.network.MessageClosePlayer;
import iCraft.core.network.NetworkHandler;
import iCraft.core.utils.ICraftUtils;

import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ICraftEventHandler
{
	public ICraftEventHandler()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onItemExpire(ItemExpireEvent event)
	{
		if (event.entityItem != null && event.entityItem.getEntityItem().getItem() instanceof ItemiCraft)
		{
			ItemStack iCraft = event.entityItem.getEntityItem();
			if (iCraft.stackTagCompound != null && iCraft.stackTagCompound.hasKey("called") && iCraft.stackTagCompound.getInteger("called") != 0)
			{
				ICraftUtils.changeCalledStatus(iCraft, 0, 0, iCraft.stackTagCompound.getBoolean("isCalling"));
			}
		}
	}

	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent event)
	{
		if (event.entityLiving instanceof EntityPlayer && willEntityDie(event))
		{
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			List<ItemStack> itemStacks = Arrays.asList(player.inventory.mainInventory);
			for (ItemStack itemStack : itemStacks)
			{
				if (itemStack != null && itemStack.getItem() instanceof ItemiCraft)
				{
					if (itemStack.stackTagCompound != null && itemStack.stackTagCompound.hasKey("called") && itemStack.stackTagCompound.getInteger("called") != 0)
					{
						ICraftUtils.changeCalledStatus(itemStack, 0, 0, itemStack.stackTagCompound.getBoolean("isCalling"));
					}
					NetworkHandler.sendTo(new MessageClosePlayer(), (EntityPlayerMP)player);
				}
			}
		}
	}

	public boolean willEntityDie(LivingHurtEvent event)
	{
		float amount = event.ammount;
		DamageSource source = event.source;
		EntityLivingBase living = event.entityLiving;
		if (!source.isUnblockable())
		{
			int armor = 25 - living.getTotalArmorValue();

			amount = amount * armor / 25.0F;
		}
		if (living.isPotionActive(Potion.resistance))
		{
			int resistance = 25 - (living.getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
			amount = amount * resistance / 25.0F;
		}
		return Math.ceil(amount) >= Math.floor(living.getHealth());
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.modID.equals("iCraft"))
		{
			ICraft.proxy.loadConfiguration();
			ICraft.proxy.onConfigSync();
		}
	}
}