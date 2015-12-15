package iCraft.core.network;

import iCraft.core.ICraft;
import iCraft.core.utils.ICraftUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MessageReceivedCall extends MessageBase<MessageReceivedCall>
{
    private int status;
    private boolean isCalling;

    public MessageReceivedCall() {}

    public MessageReceivedCall(int status, boolean isCalling)
    {
        this.status = status;
        this.isCalling = isCalling;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        status = buf.readInt();
        isCalling = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(status);
        buf.writeBoolean(isCalling);
    }

    @Override
    public void handleClientSide(MessageReceivedCall message, EntityPlayer player)
    {
        player.closeScreen();
        player.openGui(ICraft.instance, message.status, player.worldObj, 0, 0, 0);

        ItemStack itemStack = player.getCurrentEquippedItem();
        if (itemStack.stackTagCompound != null && itemStack.stackTagCompound.hasKey("isCalling") && !itemStack.stackTagCompound.getBoolean("isCalling"))
            ICraft.proxy.stopPhoneRingSound();
    }

    @Override
    public void handleServerSide(MessageReceivedCall message, EntityPlayer player)
    {
        ItemStack itemStack = player.getCurrentEquippedItem();
        switch (message.status)
        {
            case 0:
                ICraftUtils.changeCalledStatus(itemStack, 0, 0, message.isCalling);
                updateGui(itemStack, 0, message.isCalling, player.worldObj);
                break;
            case 7:
                ICraftUtils.changeCalledStatus(itemStack, 2, 2, message.isCalling);
                updateGui(itemStack, 7, message.isCalling, player.worldObj);
                break;
            default:
                ICraft.logger.error("Fatal Error while handling Received Call Packet at " + player.posX + ";" + player.posY + ";" + player.posZ + "\n" + "Culpa do Ratao");
                break;
        }
    }

    private void updateGui(ItemStack itemStack, int status, boolean isCalling, World world)
    {
        NetworkHandler.sendTo(new MessageReceivedCall(status, isCalling), (EntityPlayerMP) world.getPlayerEntityByName(isCalling ? itemStack.stackTagCompound.getString("calledPlayer") : itemStack.stackTagCompound.getString("callingPlayer")));
    }
}