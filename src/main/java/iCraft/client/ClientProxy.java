package iCraft.client;

import com.google.common.io.Files;
import iCraft.client.gui.*;
import iCraft.client.mp3.MP3Player;
import iCraft.client.render.RenderFallingCase;
import iCraft.client.render.RenderPackingCase;
import iCraft.client.render.RenderPizzaDelivery;
import iCraft.core.CommonProxy;
import iCraft.core.ICraft;
import iCraft.core.entity.EntityPackingCase;
import iCraft.core.entity.EntityPizzaDelivery;
import iCraft.core.tile.TilePackingCase;
import iCraft.core.utils.ICraftClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.File;

public class ClientProxy extends CommonProxy
{
    @Override
    public void loadConfiguration()
    {
        super.loadConfiguration();
        ICraftClientUtils.homePage = ICraft.configuration.get("general", "Web Home Page", "mod://mcef/home.html").getString();
        if (ICraft.configuration.hasChanged())
            ICraft.configuration.save();
    }

    @Override
    public void onConfigSync()
    {
        super.onConfigSync();

        if (ICraft.isVoiceEnabled && ICraft.voiceClient != null)
        {
            ICraft.voiceClient.start();
        }
        if (ICraft.mp3Folder.listFiles().length != 0)
        {
            for (File file : ICraft.mp3Folder.listFiles())
            {
                if (!file.isDirectory() && Files.getFileExtension(file.getAbsolutePath()).equals("mp3"))
                {
                    ICraft.musics.add(file);
                    String patchedName = file.getName().replaceAll(".mp3", "");
                    ICraft.musicNames.add(patchedName);
                }
            }
        }
        ICraft.mp3Player = new MP3Player();
    }

    @Override
    public EntityPlayer getClientPlayer()
    {
        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public void registerUtilities()
    {
        FMLCommonHandler.instance().bus().register(new ICraftClientEventHandler());
        FMLCommonHandler.instance().bus().register(new ClientTickHandler());
        new ICraftKeyHandler();
    }

    @Override
    public void registerNetHandler()
    {
        new InternetHandler();
    }

    @Override
    public void registerRenders()
    {
        ItemModelMesher itemModelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

        // Blocks/Items
        itemModelMesher.register(ICraft.iCraft, 0, new ModelResourceLocation("icraft:iCraft", "inventory"));
        itemModelMesher.register(ICraft.pizza, 0, new ModelResourceLocation("icraft:pizza", "inventory"));
        itemModelMesher.register(Item.getItemFromBlock(ICraft.caseBlock), 0, new ModelResourceLocation("icraft:caseBlock", "inventory"));
        // Entitiess
        RenderingRegistry.registerEntityRenderingHandler(EntityPackingCase.class, new RenderFallingCase(Minecraft.getMinecraft().getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(EntityPizzaDelivery.class, new RenderPizzaDelivery(Minecraft.getMinecraft().getRenderManager()));

        ICraft.logger.info("Render registrations complete.");
    }

    @Override
    public void registerTiles()
    {
        ClientRegistry.registerTileEntity(TilePackingCase.class, "PackingCase", new RenderPackingCase());
    }

    @Override
    public GuiScreen getClientGui(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (ID)
        {
            case 0:
                return new GuiiCraft("GuiiCraft");
            case 1:
                return new GuiiCraftCalc("GuiiCraftCalc");
            case 2://GPS
                break;
            case 3:
                return new GuiiCraftClock("GuiiCraftClock");
            case 4:
                return new GuiiCraftSettings("GuiiCraftSettings");
            case 5:
                return new GuiiCraftNumPad("GuiiCraftNumPad");
            case 6:
                return new GuiiCraftIncomingCall("whatever");
            case 7:
                return new GuiiCraftInCall("GuiiCraftInCall");
            case 8://SMS
                break;
            case 9:
                return new GuiiCraftShopping("GuiiCraftShopping");
            case 10:
                return new GuiiCraftMP3Player("GuiiCraftMP3Player");
            case 11:
                EntityPizzaDelivery pizza = (EntityPizzaDelivery) world.getEntityByID(x);
                if (pizza != null)
                    return new GuiPizzaDelivery(player.inventory, pizza);
            case 12:
                return new GuiiCraftDelivery("GuiiCraftDelivery");
            case 13:
                return new GuiiCraftBlacklist("GuiiCraftClock");
            case 14:
                return new GuiiCraftContacts("GuiiCraftContacts");
        }
        return null;
    }

    @Override
    public File getMinecraftDir()
    {
        return Minecraft.getMinecraft().mcDataDir;
    }

    @Override
    public void stopPhoneRingSound()
    {
        Minecraft.getMinecraft().getSoundHandler().stopSound(ClientTickHandler.phoneRing);
    }
}