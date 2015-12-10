package iCraft.client;

import iCraft.client.voice.VoiceClient;
import iCraft.core.ICraft;
import iCraft.core.item.ItemiCraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemTossEvent;

import java.net.InetSocketAddress;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ICraftClientEventHandler
{
	public ICraftClientEventHandler()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onConnection(ClientConnectedToServerEvent event)
	{
		if (ICraft.isVoiceEnabled)
		{
			if (event.isLocal)
			{
				// If the client is connecting to its own corresponding integrated server.
				try {
					ICraft.voiceClient = new VoiceClient("127.0.0.1");
					// Will probably not work when multiple integrateds are running on one computer
				} catch (Throwable e) {
					ICraft.logger.error("Unable to establish VoiceClient on local connection.");
					e.printStackTrace();
				}
			}
			else
			{
				// If the client is connecting to a foreign integrated or dedicated server.
				try {
					ICraft.voiceClient = new VoiceClient(((InetSocketAddress) event.manager.getSocketAddress()).getHostString());
				} catch (Throwable e) {
					ICraft.logger.error("Unable to establish VoiceClient on remote connection.");
					e.printStackTrace();
				}
			}
		}
	}

	@SubscribeEvent
	public void onDisconnection(ClientDisconnectionFromServerEvent event)
	{
		if (ICraft.mp3Player != null)
		{
			ICraft.mp3Player.setRepeatType(0);
			ICraft.mp3Player.close();
			ICraft.logger.info("MP3 Player: Successfully stopped the player.");
		}
	}

	@SubscribeEvent
	public void onItemToss(ItemTossEvent event)
	{
		if (event.entityItem.getEntityItem() != null && event.entityItem.getEntityItem().getItem() instanceof ItemiCraft)
		{
			ItemStack iCraft = event.entityItem.getEntityItem();
			if (iCraft.stackTagCompound != null && iCraft.stackTagCompound.hasKey("called") && iCraft.stackTagCompound.getInteger("called") != 0)
			{
				event.player.inventory.addItemStackToInventory(iCraft);
				event.setCanceled(true);
			}
			if (ICraft.mp3Player != null)
			{
				ICraft.mp3Player.setRepeatType(0);
				ICraft.mp3Player.close();
				ICraft.logger.info("MP3 Player: Successfully stopped the player.");
			}
		}
	}
}