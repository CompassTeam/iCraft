package iCraft.core.network;

import iCraft.core.ICraft;
import iCraft.core.item.ItemiCraft;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageIncomeCalling extends MessageBase<MessageIncomeCalling>
{
    private int number;
    private int calledNumber;

    public MessageIncomeCalling() {}

    public MessageIncomeCalling(int number, int calledNumber)
    {
        this.number = number;
        this.calledNumber = calledNumber;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        number = buf.readInt();
        calledNumber = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(number);
        buf.writeInt(calledNumber);
    }

    @Override
    public void handleClientSide(MessageIncomeCalling message, EntityPlayer player)
    {
        player.closeScreen();
        player.openGui(ICraft.instance, 6, player.worldObj, 0, 0, 0);
    }

    @Override
    public void handleServerSide(MessageIncomeCalling message, EntityPlayer player)
    {
        World world = player.worldObj;

        search:
        for (EntityPlayer players : world.playerEntities)
        {
            if (players != player)
            {
                List<ItemStack> itemStacks = Arrays.asList(players.inventory.mainInventory);
                for (ItemStack itemStack : itemStacks)
                {
                    if (itemStack != null && itemStack.getItem() instanceof ItemiCraft && itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey("number") && itemStack.getTagCompound().getInteger("number") == message.calledNumber)
                    {
                        List<String> blackList = readNBT(itemStack.getTagCompound());
                        if (!blackList.contains(player.getCommandSenderName()) && (!itemStack.getTagCompound().hasKey("called") || itemStack.getTagCompound().getInteger("called") == 0))
                        {
                            itemStack.getTagCompound().setInteger("called", 1);
                            itemStack.getTagCompound().setInteger("callingNumber", message.number);
                            itemStack.getTagCompound().setString("callingPlayer", player.getCommandSenderName());
                            itemStack.getTagCompound().setBoolean("isCalling", false);

                            List<ItemStack> stacks = Arrays.asList(player.inventory.mainInventory);
                            for (ItemStack stack : stacks)
                            {
                                if (stack != null && stack.getItem() instanceof ItemiCraft && stack.getTagCompound() != null && stack.getTagCompound().hasKey("number") && stack.getTagCompound().getInteger("number") == message.number)
                                {
                                    stack.getTagCompound().setInteger("called", 1);
                                    stack.getTagCompound().setInteger("calledNumber", itemStack.getTagCompound().getInteger("number"));
                                    stack.getTagCompound().setString("calledPlayer", players.getCommandSenderName());
                                    stack.getTagCompound().setBoolean("isCalling", true);
                                    NetworkHandler.sendTo(this, (EntityPlayerMP) player);
                                    break search;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private List<String> readNBT(NBTTagCompound nbtTags)
    {
        NBTTagList tagList = nbtTags.getTagList("blacklist", 10);
        List<String> blackList = new ArrayList<String>();
        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
            String str = tagCompound.getString("player" + i);
            blackList.add(i, str);
        }
        return blackList;
    }
}