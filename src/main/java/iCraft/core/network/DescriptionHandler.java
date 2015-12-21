package iCraft.core.network;

import iCraft.core.ICraft;
import iCraft.core.tile.TileBase;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

@Sharable
public class DescriptionHandler extends SimpleChannelInboundHandler<FMLProxyPacket>
{
    public static final String CHANNEL = "iDescription";

    static
    {
        NetworkRegistry.INSTANCE.newChannel(CHANNEL, new DescriptionHandler());
    }

    public static void init()
    {
        /**
         * Not actually doing anything here, apart from loading the class.
         * If the channel registry goes in here, Netty will throw a duplicate
         * channel error.
         */
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FMLProxyPacket msg) throws Exception
    {
        ByteBuf buf = msg.payload();
        int x = buf.readInt();
        int y = buf.readInt();
        int z = buf.readInt();
        TileEntity te = ICraft.proxy.getClientPlayer().worldObj.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof TileBase)
            ((TileBase) te).readFromPacket(buf);
    }
}