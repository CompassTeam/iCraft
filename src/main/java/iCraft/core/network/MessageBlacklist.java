package iCraft.core.network;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class MessageBlacklist extends MessageBase<MessageBlacklist>
{
	private int status;
	private String player;

	public MessageBlacklist() {}

	public MessageBlacklist(int status)
	{
		this(status, "");
	}

	public MessageBlacklist(int status, String player)
	{
		this.status = status;
		this.player = player;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		status = buf.readInt();
		player = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(status);
		ByteBufUtils.writeUTF8String(buf, player);
	}

	@Override
	public void handleClientSide(MessageBlacklist message, EntityPlayer player) {}

	@Override
	public void handleServerSide(MessageBlacklist message, EntityPlayer player)
	{
		NBTTagCompound nbtTags = player.getCurrentEquippedItem().stackTagCompound;
		switch (message.status)
		{
			case 0:
				writeNBT(nbtTags, message.player);
				break;
			case 1:
				removeNBT(nbtTags, message.player);
				break;
		}
	}

	private List<String> readNBT(NBTTagCompound nbtTags)
	{
		NBTTagList tagList = nbtTags.getTagList("blacklist", NBT.TAG_COMPOUND);
		List<String> blackList = new ArrayList<String>();
		for (int i = 0; i < tagList.tagCount(); i++)
		{
			NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
			String str = tagCompound.getString("player" + i);
			blackList.add(i, str);
		}
		return blackList;
	}

	private void writeNBT(NBTTagCompound nbtTags, String toWrite)
	{
		NBTTagList tagList = nbtTags.getTagList("blacklist", 10);
		if (tagList != null && tagList.tagCount() > 0)
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("player" + tagList.tagCount(), toWrite);
			tagList.appendTag(tag);
		}
		else
		{
			NBTTagCompound tagCompound = new NBTTagCompound();
			tagCompound.setString("player" + 0, toWrite);
			tagList.appendTag(tagCompound);
		}
		nbtTags.setTag("blacklist", tagList);
	}

	// Really dislikes this class, but it works, however, if you have a better idea please tell me :)
	private void removeNBT(NBTTagCompound nbtTags, String toRemove)
	{
		List<String> list = readNBT(nbtTags);
		if (list != null)
		{
			if (!list.isEmpty() && list.contains(toRemove))
				list.remove(list.indexOf(toRemove));

			nbtTags.removeTag("blacklist");
			NBTTagList tagList = new NBTTagList();
			for (int i = 0; i < list.size(); i++)
			{
				String str = list.get(i);
				if (str != null)
				{
					NBTTagCompound tagCompound = new NBTTagCompound();
					tagCompound.setString("player" + i, str);
					tagList.appendTag(tagCompound);
				}
			}
			nbtTags.setTag("blacklist", tagList);
		}
	}
}