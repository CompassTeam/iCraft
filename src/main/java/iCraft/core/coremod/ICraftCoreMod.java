package iCraft.core.coremod;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class ICraftCoreMod implements IFMLLoadingPlugin
{
    public static Logger log;

    public ICraftCoreMod()
    {
        log = LogManager.getLogger("iCraft-core");
    }

    @Override
    public String[] getASMTransformerClass()
    {
        return null;
    }

    @Override
    public String getModContainerClass()
    {
        return null;
    }

    @Override
    public String getSetupClass()
    {
        return "iCraft.core.coremod.Setup";
    }

    @Override
    public void injectData(Map<String, Object> data) {}

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }
}