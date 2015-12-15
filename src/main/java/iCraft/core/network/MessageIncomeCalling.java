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
        for (EntityPlayerMP players : (List<EntityPlayerMP>) world.playerEntities)
        {
            if (players != player)
            {
                List<ItemStack> itemStacks = Arrays.asList(players.inventory.mainInventory);
                for (ItemStack itemStack : itemStacks)
                {
                    if (itemStack != null && itemStack.getItem() instanceof ItemiCraft && itemStack.stackTagCompound != null && itemStack.stackTagCompound.hasKey("number") && itemStack.stackTagCompound.getInteger("number") == message.calledNumber)
                    {
                        List<String> blackList = readNBT(itemStack.stackTagCompound);
                        if (!blackList.contains(player.getCommandSenderName()) && (!itemStack.stackTagCompound.hasKey("called") || itemStack.stackTagCompound.getInteger("called") == 0))
                        {
                            itemStack.stackTagCompound.setInteger("called", 1);
                            itemStack.stackTagCompound.setInteger("callingNumber", message.number);
                            itemStack.stackTagCompound.setString("callingPlayer", player.getCommandSenderName());
                            itemStack.stackTagCompound.setBoolean("isCalling", false);

                            List<ItemStack> stacks = Arrays.asList(player.inventory.mainInventory);
                            for (ItemStack stack : stacks)
                            {
                                if (stack != null && stack.getItem() instanceof ItemiCraft && stack.stackTagCompound != null && stack.stackTagCompound.hasKey("number") && stack.stackTagCompound.getInteger("number") == message.number)
                                {
                                    stack.stackTagCompound.setInteger("called", 1);
                                    stack.stackTagCompound.setInteger("calledNumber", itemStack.stackTagCompound.getInteger("number"));
                                    stack.stackTagCompound.setString("calledPlayer", players.getCommandSenderName());
                                    stack.stackTagCompound.setBoolean("isCalling", true);
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