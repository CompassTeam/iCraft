package iCraft.core.voice;

import iCraft.core.ICraft;
import iCraft.core.item.ItemiCraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VoiceConnection extends Thread
{
    public Socket socket;

    public String username;

    public boolean open = true;

    public DataInputStream input;
    public DataOutputStream output;

    public MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

    public VoiceConnection(Socket s)
    {
        socket = s;
    }

    @Override
    public void run()
    {
        try {
            input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            synchronized (ICraft.voiceManager)
            {
                int retryCount = 0;

                while (username == null && retryCount <= 100)
                {
                    try {
                        List l = Collections.synchronizedList((List) ((ArrayList) server.getConfigurationManager().playerEntityList).clone());

                        for (Object obj : l)
                        {
                            if (obj instanceof EntityPlayerMP)
                            {
                                EntityPlayerMP playerMP = (EntityPlayerMP) obj;
                                String playerIP = playerMP.getPlayerIP();

                                if (!server.isDedicatedServer() && playerIP.equals("local") && !ICraft.voiceManager.foundLocal)
                                {
                                    ICraft.voiceManager.foundLocal = true;
                                    username = playerMP.getCommandSenderName();
                                    break;
                                }
                                else if (playerIP.equals(socket.getInetAddress().getHostAddress()))
                                {
                                    username = playerMP.getCommandSenderName();
                                    break;
                                }
                            }
                        }

                        retryCount++;
                        Thread.sleep(50);
                    } catch (Exception e) {}
                }

                if (username == null)
                {
                    ICraft.logger.error("VoiceServer: Unable to trace connection's IP address.");
                    kill();
                    return;
                }
                else
                    ICraft.logger.info("VoiceServer: Traced IP in " + retryCount + " attempts.");
            }
        } catch (Exception e) {
            ICraft.logger.error("VoiceServer: Error while starting server-based connection.");
            e.printStackTrace();
            open = false;
        }

        //Main client listen thread
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                while (open)
                {
                    try {
                        short byteCount = VoiceConnection.this.input.readShort();
                        byte[] audioData = new byte[byteCount];
                        VoiceConnection.this.input.readFully(audioData);

                        if (byteCount > 0)
                            ICraft.voiceManager.sendToPlayers(byteCount, audioData, VoiceConnection.this);
                    } catch (Exception e) {
                        open = false;
                    }
                }

                if (!open)
                    kill();
            }
        }).start();
    }

    public void kill()
    {
        try {
            input.close();
            output.close();
            socket.close();

            ICraft.voiceManager.connections.remove(this);
        } catch (Exception e) {
            ICraft.logger.error("VoiceServer: Error while stopping server-based connection.");
            e.printStackTrace();
        }
    }

    public void sendToPlayer(short byteCount, byte[] audioData, VoiceConnection connection)
    {
        if (!open)
            kill();

        try {
            output.writeShort(byteCount);
            output.write(audioData);

            output.flush();
        } catch (Exception e) {
            ICraft.logger.error("VoiceServer: Error while sending data to player.");
            e.printStackTrace();
        }
    }

    public boolean canListen(int callCode)
    {
        for (ItemStack itemStack : getPlayer().inventory.mainInventory)
        {
            if (itemStack != null && itemStack.getItem() instanceof ItemiCraft)
            {
                if (itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey("called") && itemStack.getTagCompound().getInteger("called") == 2)
                {
                    if (itemStack.getTagCompound().hasKey("callCode") && itemStack.getTagCompound().getInteger("callCode") == callCode) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int getCallCode()
    {
        for (ItemStack itemStack : getPlayer().inventory.mainInventory)
        {
            if (itemStack != null && itemStack.getItem() instanceof ItemiCraft)
            {
                if (itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey("number") && itemStack.getTagCompound().hasKey("called") && itemStack.getTagCompound().getInteger("called") == 2 && itemStack.getTagCompound().hasKey("callCode"))
                {
                    return itemStack.getTagCompound().getInteger("callCode");
                }
            }
        }
        return 0;
    }

    public EntityPlayerMP getPlayer()
    {
        return server.getConfigurationManager().getPlayerByUsername(username);
    }
}