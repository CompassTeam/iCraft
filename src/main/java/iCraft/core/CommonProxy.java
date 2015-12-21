package iCraft.core;

import iCraft.core.entity.EntityPizzaDelivery;
import iCraft.core.inventory.container.ContainerPizzaDelivery;
import iCraft.core.tile.TilePackingCase;
import iCraft.core.utils.ICraftUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.FMLInjectionData;

import java.io.File;

public class CommonProxy
{
    public void loadConfiguration()
    {
        ICraft.isVoiceEnabled = ICraft.configuration.get("Voice Settings", "IsVoiceEnabled", true).getBoolean(true);
        ICraft.VOICE_PORT = ICraft.configuration.get("Voice Settings", "VoicePort", 2550, null, 1, 65535).getInt();
        ICraft.isIBayActive = (ICraft.configuration.get("general", "isIBayActive", true).getBoolean(true));
        ICraft.buyableItems = ICraft.configuration.get(Configuration.CATEGORY_GENERAL, "Buyable Items(I know purchasable is more correct, but buyable is funnier :P)", new String[]{Item.getIdFromItem(Items.iron_ingot) + ".1.0-" + Item.getIdFromItem(Items.iron_ingot) + ".1.0", Item.getIdFromItem(Items.gold_ingot) + ".1.0-" + Item.getIdFromItem(Items.gold_ingot) + ".1.0", Item.getIdFromItem(Items.diamond) + ".1.0-" + Item.getIdFromItem(Items.diamond) + ".1.0"}, "To add or modify items of the CraftBay Shop use this pattern: id.quantity.metadata-id.quantity.metadata - And always remember, on the left side of the hyphen(the '-' symbol), put the items you want to be purchasable and on the right side, put items you want to use as payment for the left-side item.").getStringList();

        for (int i = 0; i < ICraft.buyableItems.length; i++)
        {
            String[] str = (ICraft.buyableItems[i].split("-")[0]).split("\\.");
            String[] str1 = (ICraft.buyableItems[i].split("-")[1]).split("\\.");
            ItemStack buyStack = new ItemStack(Item.getItemById(Integer.parseInt(str[0])), Integer.parseInt(str[1]), Integer.parseInt(str[2]));
            ItemStack sellStack = new ItemStack(Item.getItemById(Integer.parseInt(str1[0])), Integer.parseInt(str1[1]), Integer.parseInt(str1[2]));
            ICraftUtils.addBuyableItems(i, buyStack, sellStack);
        }
        if (ICraft.configuration.hasChanged())
            ICraft.configuration.save();
    }

    public void onConfigSync()
    {
        ICraft.logger.info("Received config from server.");
    }

    public EntityPlayer getClientPlayer()
    {
        return null;
    }

    public void registerUtilities() {}

    public void registerNetHandler() {}

    public void registerRenders() {}

    public void registerTiles()
    {
        GameRegistry.registerTileEntity(TilePackingCase.class, "PackingCase");
    }

    /**
     * Get the actual interface for a GUI. Client-only.
     *
     * @param ID     - gui ID
     * @param player - player that opened the GUI
     * @param world  - world the GUI was opened in
     * @param x      - gui's x position
     * @param y      - gui's y position
     * @param z      - gui's z position
     * @return the GuiScreen of the GUI
     */
    public Object getClientGui(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }

    /**
     * Get the container for a GUI. Common.
     *
     * @param ID     - gui ID
     * @param player - player that opened the GUI
     * @param world  - world the GUI was opened in
     * @param x      - gui's x position
     * @param y      - gui's y position
     * @param z      - gui's z position
     * @return the Container of the GUI
     */
    public Container getServerGui(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (ID)
        {
            case 11:
                EntityPizzaDelivery pizza = (EntityPizzaDelivery) world.getEntityByID(x);
                if (pizza != null)
                    return new ContainerPizzaDelivery(player.inventory, pizza);
        }
        return null;
    }

    /**
     * Gets the Minecraft base directory.
     *
     * @return base directory
     */
    public File getMinecraftDir()
    {
        return (File) FMLInjectionData.data()[6];
    }

    public void stopPhoneRingSound() {}
}