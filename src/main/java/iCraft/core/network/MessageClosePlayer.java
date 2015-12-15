package iCraft.core.network;

import iCraft.core.ICraft;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Definitely the most important class ever...
 */
public class MessageClosePlayer extends MessageBase<MessageClosePlayer>
{
    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    @Override
    public void handleClientSide(MessageClosePlayer message, EntityPlayer player)
    {
        if (ICraft.mp3Player != null)
        {
            ICraft.mp3Player.setRepeatType(0);
            ICraft.mp3Player.close();
        }
    }

    @Override
    public void handleServerSide(MessageClosePlayer message, EntityPlayer player) {}
}