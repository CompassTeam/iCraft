package iCraft.core.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler
{
    private static SimpleNetworkWrapper INSTANCE;

    public static void init()
    {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("iCraft");

        INSTANCE.registerMessage(MessageIncomeCalling.class, MessageIncomeCalling.class, 0, Side.CLIENT);
        INSTANCE.registerMessage(MessageIncomeCalling.class, MessageIncomeCalling.class, 0, Side.SERVER);
        INSTANCE.registerMessage(MessageReceivedCall.class, MessageReceivedCall.class, 1, Side.CLIENT);
        INSTANCE.registerMessage(MessageReceivedCall.class, MessageReceivedCall.class, 1, Side.SERVER);
        INSTANCE.registerMessage(MessageCraftBay.class, MessageCraftBay.class, 2, Side.SERVER);
        INSTANCE.registerMessage(MessageConfigSync.class, MessageConfigSync.class, 3, Side.CLIENT);
        INSTANCE.registerMessage(MessageClosePlayer.class, MessageClosePlayer.class, 4, Side.CLIENT);
        INSTANCE.registerMessage(MessageDelivery.class, MessageDelivery.class, 5, Side.SERVER);
        INSTANCE.registerMessage(MessageBlacklist.class, MessageBlacklist.class, 6, Side.SERVER);
        INSTANCE.registerMessage(MessageContacts.class, MessageContacts.class, 7, Side.SERVER);
    }

    public static void sendToServer(IMessage message)
    {
        INSTANCE.sendToServer(message);
    }

    public static void sendTo(IMessage message, EntityPlayerMP player)
    {
        INSTANCE.sendTo(message, player);
    }

    public static void sendToAllAround(IMessage message, TargetPoint point)
    {
        INSTANCE.sendToAllAround(message, point);
    }

    public static void sendToAll(IMessage message)
    {
        INSTANCE.sendToAll(message);
    }

    public static void sendToDimension(IMessage message, int dimensionId)
    {
        INSTANCE.sendToDimension(message, dimensionId);
    }
}