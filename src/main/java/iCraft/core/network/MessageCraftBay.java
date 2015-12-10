package iCraft.core.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import iCraft.core.entity.EntityPackingCase;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MessageCraftBay extends MessageBase<MessageCraftBay>
{
    private Item buyItem;
    private int buyQnt;
    private int buyMeta;
    private Item sellItem;
    private int sellQnt;
    private int sellMeta;
    private ItemStack buyStack;
    private ItemStack sellStack;

	public MessageCraftBay() {}

    public MessageCraftBay(Item buyItem, int buyQnt, int buyMeta, Item sellItem, int sellQnt, int sellMeta)
    {
        this.buyItem = buyItem;
        this.buyQnt = buyQnt;
        this.buyMeta = buyMeta;
        this.sellItem = sellItem;
        this.sellQnt = sellQnt;
        this.sellMeta = sellMeta;
    }

	@Override
	public void fromBytes(ByteBuf buf)
	{
        buyStack = ByteBufUtils.readItemStack(buf);
        sellStack = ByteBufUtils.readItemStack(buf);
    }

	@Override
	public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeItemStack(buf, new ItemStack(buyItem, buyQnt, buyMeta));
        ByteBufUtils.writeItemStack(buf, new ItemStack(sellItem, sellQnt, sellMeta));
	}

	@Override
	public void handleClientSide(MessageCraftBay message, EntityPlayer player) {}

	@Override
	public void handleServerSide(MessageCraftBay message, EntityPlayer player)
	{
		World world = player.getEntityWorld();
		ItemStack[] stack = new ItemStack[1];
		stack[0] = message.buyStack;
		Entity packingCase = new EntityPackingCase(world, stack, message.sellStack, player.getGameProfile().getId(), player.getGameProfile().getName());
		packingCase.setPosition(player.posX, (player.posY + 75 <= 256 ? player.posY + 75 : 75), player.posZ);
		world.spawnEntityInWorld(packingCase);
	}
}