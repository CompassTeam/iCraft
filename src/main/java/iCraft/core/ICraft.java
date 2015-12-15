package iCraft.core;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import iCraft.client.mp3.MP3Player;
import iCraft.client.voice.VoiceClient;
import iCraft.core.block.BlockPackingCase;
import iCraft.core.entity.EntityPackingCase;
import iCraft.core.entity.EntityPizzaDelivery;
import iCraft.core.item.ItemPizza;
import iCraft.core.item.ItemiCraft;
import iCraft.core.network.DescriptionHandler;
import iCraft.core.network.NetworkHandler;
import iCraft.core.utils.ICraftUtils;
import iCraft.core.voice.VoiceManager;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Mod(modid = "iCraft", name = "iCraft", version = "@VERSION@", guiFactory = "iCraft.client.gui.GuiFactoryICraft", dependencies = "after:MCEF")
public class ICraft
{
    /** iCraft logger */
    public static Logger logger = LogManager.getLogger("iCraft");

    /** iCraft proxy instance */
    @SidedProxy(clientSide = "iCraft.client.ClientProxy", serverSide = "iCraft.core.CommonProxy")
    public static CommonProxy proxy;

    /** iCraft mod instance */
    @Instance("iCraft")
    public static ICraft instance;

    /** iCraft configuration instance */
    public static Configuration configuration;

    /** The latest version of iCraft */
    public static String newestVersion;

    /** iCraft Creative Tab - I don't think this is someway util, but I'll maintain it here :P */
    //public static CreativeTabiCraft tabiCraft = new CreativeTabiCraft();

    /** iCraft Voice Managers */
    public static VoiceManager voiceManager;
    public static VoiceClient voiceClient;

    /** iCraft MP3 Player */
    public static MP3Player mp3Player;

    /** iCraft mp3 folder */
    public static File mp3Folder;

    /** MP3 player miscellaneous */
    public static List<File> musics = new ArrayList<File>();
    public static List<String> musicNames = new ArrayList<String>();
    public static int currentMusicId = 0;

    // Blocks
    public static Block caseBlock;
    // Items
    public static Item iCraft;
    public static Item pizza;

    // Configurations
    public static boolean isVoiceEnabled = true;
    public static int VOICE_PORT = 2550;
    public static boolean isIBayActive = true;
    public static String[] buyableItems = new String[0];

    public void addItems()
    {
        // Declarations
        iCraft = new ItemiCraft().setUnlocalizedName("iCraft");
        pizza = new ItemPizza().setUnlocalizedName("pizza");
        // Registrations
        GameRegistry.registerItem(iCraft, "iCraft");
        GameRegistry.registerItem(pizza, "pizza");
    }

    public void addBlocks()
    {
        // Declarations
        caseBlock = new BlockPackingCase().setBlockName("CaseBlock");
        // Registrations
        GameRegistry.registerBlock(caseBlock, "CaseBlock");
    }

    public void addRecipes()
    {
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(iCraft, new Object[]{
                "III", "IGI", "III", Character.valueOf('I'), Items.iron_ingot, Character.valueOf('G'), Blocks.glass_pane
        }));
    }

    public void addEntities()
    {
        // Entities
        EntityRegistry.registerModEntity(EntityPackingCase.class, "FallingPackingCase", 0, instance, 150, 5, true);
        EntityRegistry.registerModEntity(EntityPizzaDelivery.class, "PizzaDelivery", 1, instance, 150, 5, true);
        // Tile Entities
        proxy.registerTiles();
    }

    @EventHandler
    public void onServerStart(FMLServerStartingEvent event)
    {
        if (isVoiceEnabled)
            voiceManager.start();
    }

    @EventHandler
    public void serverStopping(FMLServerStoppingEvent event)
    {
        if (isVoiceEnabled)
            voiceManager.stop();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        File config = event.getSuggestedConfigurationFile();
        mp3Folder = new File(proxy.getMinecraftDir(), "mods/iCraft/mp3");

        // Set the mod's configuration
        configuration = new Configuration(config);

        if (!mp3Folder.exists())
            mp3Folder.mkdirs();

        FMLCommonHandler.instance().bus().register(new ICraftEventHandler());

        addItems();
        addBlocks();
        addRecipes();
        addEntities();

        proxy.loadConfiguration();

        FMLCommonHandler.instance().bus().register(new CommonPlayerTracker());

        proxy.registerUtilities();
        proxy.registerRenders();

        // NETworking
        NetworkHandler.init();
        DescriptionHandler.init();

        logger.info("Carrier has arrived.");
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // Register the mod's GUI handler
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        // Checks the newest version
        ICraftUtils.getNewestVersion();

        // Initializes Voice Manager
        if (isVoiceEnabled)
            voiceManager = new VoiceManager();

        if (Loader.isModLoaded("MCEF"))
            proxy.registerNetHandler();

        logger.info("Prismatic core online.");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        logger.info("Ready to roll out!");
    }
}