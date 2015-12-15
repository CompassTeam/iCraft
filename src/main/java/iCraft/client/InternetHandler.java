package iCraft.client;

import iCraft.client.gui.GuiiCraftBrowser;
import net.minecraft.client.Minecraft;
import net.montoyo.mcef.api.*;

public class InternetHandler implements IDisplayHandler, IJSQueryHandler
{
    private Minecraft mc = Minecraft.getMinecraft();
    public static GuiiCraftBrowser backup = null;

    public InternetHandler()
    {
        // Initializes Internet Handler
        API api = MCEFApi.getAPI();
        if (api == null)
            return;

        api.registerDisplayHandler(this);
        api.registerJSQueryHandler(this);
    }

    public static void setBackup(GuiiCraftBrowser bu)
    {
        backup = bu;
    }

    public static boolean hasBackup()
    {
        return (backup != null);
    }

    public IBrowser getBrowser()
    {
        if (mc.currentScreen instanceof GuiiCraftBrowser)
            return ((GuiiCraftBrowser) mc.currentScreen).browser;
        else if (backup != null)
            return backup.browser;
        else
            return null;
    }

    @Override
    public boolean handleQuery(IBrowser b, long queryId, String query, boolean persistent, IJSQueryCallback cb)
    {
        if (b == getBrowser() && query.equalsIgnoreCase("username"))
        {
            try {
                String name = mc.getSession().getUsername();
                cb.success(name);
            } catch (Throwable t) {
                cb.failure(500, "Internal error.");
                t.printStackTrace();
            }

            return true;
        }

        return false;
    }

    @Override
    public void cancelQuery(IBrowser b, long queryId) {}

    @Override
    public void onAddressChange(IBrowser browser, String url)
    {
        if (mc.currentScreen instanceof GuiiCraftBrowser)
            ((GuiiCraftBrowser) mc.currentScreen).onUrlChanged(browser, url);
        else if (hasBackup())
            backup.onUrlChanged(browser, url);
    }

    @Override
    public void onTitleChange(IBrowser browser, String title) {}

    @Override
    public boolean onTooltip(IBrowser browser, String text)
    {
        return false;
    }

    @Override
    public void onStatusMessage(IBrowser browser, String value) {}

    @Override
    public boolean onConsoleMessage(IBrowser browser, String message, String source, int line)
    {
        return false;
    }
}