package iCraft.core.coremod;

import cpw.mods.fml.relauncher.IFMLCallHook;

import java.io.File;
import java.util.Map;

public class Setup implements IFMLCallHook
{
    private File mcDir;

    @Override
    public Void call() throws Exception
    {
        Library.init(this.mcDir, (String) cpw.mods.fml.relauncher.FMLInjectionData.data()[4]);

        return null;
    }

    @Override
    public void injectData(Map<String, Object> data)
    {
        mcDir = ((File) data.get("mcLocation"));
    }
}