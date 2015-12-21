package iCraft.core.network;

import iCraft.core.ICraft;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MessageConfigSync extends MessageBase<MessageConfigSync>
{
    public MessageConfigSync() {}

    @Override
    public void fromBytes(ByteBuf buf)
    {
        ICraft.isVoiceEnabled = buf.readBoolean();
        ICraft.VOICE_PORT = buf.readInt();
        ICraft.isIBayActive = (buf.readBoolean());
        int size = buf.readInt();
        ICraft.buyableItems = new String[size];
        for (int i = 0; i < size; i++)
        {
            ICraft.buyableItems[i] = ByteBufUtils.readUTF8String(buf);
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(ICraft.isVoiceEnabled);
        buf.writeInt(ICraft.VOICE_PORT);
        buf.writeBoolean(ICraft.isIBayActive);
        buf.writeInt(ICraft.buyableItems.length);
        for (String s : ICraft.buyableItems)
        {
            ByteBufUtils.writeUTF8String(buf, s);
        }
    }

    @Override
    public void handleClientSide(MessageConfigSync message, EntityPlayer player)
    {
        ICraft.proxy.onConfigSync();
    }

    @Override
    public void handleServerSide(MessageConfigSync message, EntityPlayer player) {}
}