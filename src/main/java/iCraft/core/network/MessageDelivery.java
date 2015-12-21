package iCraft.core.network;

import iCraft.core.entity.EntityPizzaDelivery;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class MessageDelivery extends MessageBase<MessageDelivery>
{
    private int qnt;

    public MessageDelivery() {}

    public MessageDelivery(int qnt)
    {
        this.qnt = qnt;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        qnt = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(qnt);
    }

    @Override
    public void handleClientSide(MessageDelivery message, EntityPlayer player) {}

    @Override
    public void handleServerSide(MessageDelivery message, EntityPlayer player)
    {
        World world = player.worldObj;
        EntityPizzaDelivery pizza = new EntityPizzaDelivery(world, player.posX + 40, player.posY, player.posZ + 40);
        pizza.setPlayer(player.getCommandSenderName());
        pizza.setQuantity(message.qnt);
        world.spawnEntityInWorld(pizza);
    }
}