package iCraft.core.coremod;

import net.minecraftforge.fml.relauncher.FMLInjectionData;
import net.minecraftforge.fml.relauncher.IFMLCallHook;

import java.io.File;
import java.util.Map;

public class Setup implements IFMLCallHook
{
    private File mcDir;

    @Override
    public Void call() throws Exception
    {
        Library.init(this.mcDir, (String) FMLInjectionData.data()[4]);

        return null;
    }

    @Override
    public void injectData(Map<String, Object> data)
    {
        mcDir = ((File) data.get("mcLocation"));
    }
}