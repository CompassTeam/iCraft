package iCraft.core.utils;

import iCraft.core.ICraft;
import iCraft.core.item.ItemiCraft;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.ConfigElement;

public class ICraftUtils
{
	public static final HashMap<Integer, HashMap<ItemStack, ItemStack>> items = new HashMap<Integer, HashMap<ItemStack, ItemStack>>();

	/**
	 * Localizes the defined string.
	 * @param s - string to localized
	 * @return localized string
	 */
	public static String localize(String s)
	{
		return StatCollector.translateToLocal(s);
	}

	public static void changeCalledStatus(ItemStack itemStack, int status, int status2, boolean isCalling)
	{
		if (itemStack != null && itemStack.stackTagCompound != null)
		{
			itemStack.stackTagCompound.setInteger("called", status);
			if (status == 2)
			{
				itemStack.stackTagCompound.setInteger("callCode", itemStack.stackTagCompound.getInteger("number"));
			}
			else if (status == 0)
			{
				itemStack.stackTagCompound.setInteger("callCode", 0);
				if (itemStack.stackTagCompound.hasKey("isCalling"))
					itemStack.stackTagCompound.setBoolean("isCalling", false);
			}

			search:
			for (EntityPlayerMP players : (List<EntityPlayerMP>)FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList)
			{
				if (players.getCommandSenderName().equals((isCalling ? itemStack.stackTagCompound.getString("calledPlayer") : itemStack.stackTagCompound.getString("callingPlayer"))))
				{
					List<ItemStack> stacks = Arrays.asList(players.inventory.mainInventory);
					for (ItemStack stack : stacks)
					{
						if (stack != null && stack.getItem() instanceof ItemiCraft)
						{
							ItemiCraft iCraft = (ItemiCraft) stack.getItem();
							if (stack.stackTagCompound != null && iCraft.getNumber(stack) == (isCalling ? itemStack.stackTagCompound.getInteger("calledNumber") : itemStack.stackTagCompound.getInteger("callingNumber")))
							{
								stack.stackTagCompound.setInteger("called", status2);
								if (status2 == 2)
								{
									stack.stackTagCompound.setInteger("callCode", itemStack.stackTagCompound.getInteger("number"));
								}
								else if (status2 == 0)
								{
									stack.stackTagCompound.setInteger("callCode", 0);
									if (stack.stackTagCompound.hasKey("isCalling"))
										stack.stackTagCompound.setBoolean("isCalling", false);
								}
								break search;
							}
						}
					}
				}
			}
		}
	}

	public static void addBuyableItems(int index, ItemStack buyStack, ItemStack sellStack)
    {
        boolean repeated = false;
        for (int i : items.keySet())
        {
            if (items.get(i).get(buyStack) != null)
            {
                repeated = true;
                break;
            }
        }

		if (buyStack != null && !repeated)
		{
            HashMap<ItemStack, ItemStack> map = new HashMap<ItemStack, ItemStack>();
            map.put(buyStack, sellStack);
			items.put(index, map);
		}
	}

	public static void getNewestVersion()
	{
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;

		try {
			url = new URL("https://dl.dropbox.com/s/8sczowv9foi0d8h/iCraft.txt");
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			ICraft.newestVersion = rd.readLine();
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<IConfigElement> getConfigElements()
	{
		List<IConfigElement> list = new ArrayList<IConfigElement>();
		list.addAll(new ConfigElement(ICraft.configuration.getCategory("general")).getChildElements());
		list.addAll(new ConfigElement(ICraft.configuration.getCategory("voice settings")).getChildElements());
		return list;
	}
}