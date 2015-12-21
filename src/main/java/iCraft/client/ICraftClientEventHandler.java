package iCraft.client;

import iCraft.client.voice.VoiceClient;
import iCraft.core.ICraft;
import iCraft.core.item.ItemiCraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.net.InetSocketAddress;

@SideOnly(Side.CLIENT)
public class ICraftClientEventHandler
{
    public ICraftClientEventHandler()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onConnection(FMLNetworkEvent.ClientConnectedToServerEvent event)
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
                    ICraft.voiceClient = new VoiceClient(((InetSocketAddress) event.manager.getRemoteAddress()).getHostString());
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
            if (iCraft.getTagCompound() != null && iCraft.getTagCompound().hasKey("called") && iCraft.getTagCompound().getInteger("called") != 0)
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