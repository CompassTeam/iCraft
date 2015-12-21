package iCraft.core.tile;

import iCraft.core.network.DescriptionHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public abstract class TileBase extends TileEntity
{
    @Override
    public Packet getDescriptionPacket()
    {
        ByteBuf buf = Unpooled.buffer();
        PacketBuffer buffer = new PacketBuffer(buf);
        buf.writeInt(getPos().getX());
        buf.writeInt(getPos().getY());
        buf.writeInt(getPos().getZ());
        writeToPacket(buf);
        return new FMLProxyPacket(buffer, DescriptionHandler.CHANNEL);
    }

    public void writeToPacket(ByteBuf buf) {}

    public void readFromPacket(ByteBuf buf) {}
}