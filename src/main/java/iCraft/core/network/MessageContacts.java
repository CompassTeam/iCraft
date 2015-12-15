package iCraft.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;

public class MessageContacts extends MessageBase<MessageContacts>
{
    private int status;
    private int number;

    public MessageContacts() {}

    public MessageContacts(int status)
    {
        this(status, 0);
    }

    public MessageContacts(int status, int number)
    {
        this.status = status;
        this.number = number;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        status = buf.readInt();
        number = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(status);
        buf.writeInt(number);
    }

    @Override
    public void handleClientSide(MessageContacts message, EntityPlayer player)
    {
    }

    @Override
    public void handleServerSide(MessageContacts message, EntityPlayer player)
    {
        NBTTagCompound nbtTags = player.getCurrentEquippedItem().stackTagCompound;
        switch (message.status)
        {
            case 0:
                writeNBT(nbtTags, message.number);
                break;
            case 1:
                removeNBT(nbtTags, message.number);
                break;
        }
    }

    private List<Integer> readNBT(NBTTagCompound nbtTags)
    {
        NBTTagList tagList = nbtTags.getTagList("contacts", 10);
        List<Integer> contacts = new ArrayList<Integer>();
        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
            int j = tagCompound.getInteger("number" + i);
            contacts.add(i, j);
        }
        return contacts;
    }

    private void writeNBT(NBTTagCompound nbtTags, int toWrite)
    {
        NBTTagList tagList = nbtTags.getTagList("contacts", 10);
        if (tagList != null && tagList.tagCount() > 0)
        {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("number" + tagList.tagCount(), toWrite);
            tagList.appendTag(tag);
        }
        else
        {
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setInteger("number" + 0, toWrite);
            tagList.appendTag(tagCompound);
        }
        nbtTags.setTag("contacts", tagList);
    }

    private void removeNBT(NBTTagCompound nbtTags, int toRemove)
    {
        List<Integer> list = readNBT(nbtTags);
        if (list != null)
        {
            if (!list.isEmpty() && list.contains(toRemove))
                list.remove(list.indexOf(toRemove));

            nbtTags.removeTag("contacts");
            NBTTagList tagList = new NBTTagList();
            for (int i = 0; i < list.size(); i++)
            {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setInteger("number" + i, list.get(i));
                tagList.appendTag(tagCompound);
            }
            nbtTags.setTag("contacts", tagList);
        }
    }
}