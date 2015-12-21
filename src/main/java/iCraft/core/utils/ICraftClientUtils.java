package iCraft.core.utils;

import iCraft.core.ICraft;
import iCraft.core.item.ItemiCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.tritonus.share.sampled.file.TAudioFileFormat;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ICraftClientUtils
{
    public static boolean hour24 = true;
    public static String homePage = "mod://mcef/home.html";
    private static Minecraft mc = FMLClientHandler.instance().getClient();

    public static void checkUpdates(EntityPlayer player)
    {
        if (ICraft.newestVersion != null)
        {
            try {
                double version = Double.parseDouble("@VERSION@");
                double latestVersion = Double.parseDouble(ICraft.newestVersion);
                if (latestVersion > version)
                {
                    player.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "----------------- " + EnumChatFormatting.BLUE + "[" + EnumChatFormatting.GOLD + "iCraft" + EnumChatFormatting.BLUE + "]" + EnumChatFormatting.AQUA + " -----------------"));
                    player.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + ICraftUtils.localize("update.outdated") + " " + EnumChatFormatting.GOLD + latestVersion + EnumChatFormatting.GREEN + "."));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static EntityLivingBase getClientPlayer(WorldClient clientWorld, boolean isCalling)
    {
        List<ItemStack> itemStacks = Arrays.asList(mc.thePlayer.inventory.mainInventory);
        for (ItemStack itemStack : itemStacks)
        {
            if (itemStack != null && itemStack.getItem() instanceof ItemiCraft && itemStack.getTagCompound() != null)
                return (isCalling ? clientWorld.getPlayerEntityByName(itemStack.getTagCompound().getString("calledPlayer")) : (EntityLivingBase) clientWorld.getPlayerEntityByName(itemStack.getTagCompound().getString("callingPlayer")));
        }
        return null;
    }

    public static int getPlayerNumber(boolean isCalling)
    {
        List<ItemStack> itemStacks = Arrays.asList(mc.thePlayer.inventory.mainInventory);
        for (ItemStack itemStack : itemStacks)
        {
            if (itemStack != null && itemStack.getItem() instanceof ItemiCraft && itemStack.getTagCompound() != null)
                return (isCalling ? itemStack.getTagCompound().getInteger("calledNumber") : itemStack.getTagCompound().getInteger("callingNumber"));
        }
        return 0;
    }

    public enum ResourceType
    {
        GUI("gui"),
        SOUND("sounds"),
        TEXTURE_BLOCKS("textures/blocks"),
        TEXTURE_ITEMS("textures/items"),
        RENDER("render");

        private String prefix;

        ResourceType(String s) {
            prefix = s;
        }

        public String getPrefix() {
            return prefix + "/";
        }
    }

    /**
     * Gets a ResourceLocation with a defined resource type and name.
     *
     * @param type - type of resource to retrieve
     * @param name - simple name of file to retrieve as a ResourceLocation
     * @return the corresponding ResourceLocation
     */
    public static ResourceLocation getResource(ResourceType type, String name)
    {
        return new ResourceLocation("icraft", type.getPrefix() + name);
    }

    public static String getTime()
    {
        long time = (mc.theWorld.getWorldTime() + 6000L) % (hour24 ? 24000L : 12000L);

        long hours = time / 1000L;
        long seconds = (long) (time % 1000L * 0.06D);

        return String.format("%02d", new Object[]{Long.valueOf(hours)}) + ":" + String.format("%02d", new Object[]{Long.valueOf(seconds)});
    }

    public static String getAuthor(File file)
    {
        try {
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
            if (fileFormat instanceof TAudioFileFormat)
            {
                Map<?, ?> properties = fileFormat.properties();
                String key = "author";
                return (String) properties.get(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}