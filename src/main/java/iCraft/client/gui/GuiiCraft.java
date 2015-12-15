package iCraft.client.gui;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import iCraft.client.InternetHandler;
import iCraft.core.ICraft;
import iCraft.core.utils.ICraftUtils;
import net.minecraft.util.ChatComponentText;

@SideOnly(Side.CLIENT)
public class GuiiCraft extends GuiiCraftBase
{
    public GuiiCraft(String resource)
    {
        super(resource);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY)
    {
        super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);

        int xAxis = mouseX - guiWidth;
        int yAxis = mouseY - guiHeight;

        if (xAxis >= 51 && xAxis <= 67 && yAxis >= 38 && yAxis <= 54)
            drawTexturedModalRect(guiWidth + 51, guiHeight + 38, 176, 38, 17, 17);
        if (xAxis >= 70 && xAxis <= 86 && yAxis >= 38 && yAxis <= 54)
            drawTexturedModalRect(guiWidth + 70, guiHeight + 38, 176, 55, 17, 17);
        if (xAxis >= 89 && xAxis <= 105 && yAxis >= 38 && yAxis <= 54)
            drawTexturedModalRect(guiWidth + 89, guiHeight + 38, 176, 72, 17, 17);
        if (xAxis >= 108 && xAxis <= 124 && yAxis >= 38 && yAxis <= 54)
            drawTexturedModalRect(guiWidth + 108, guiHeight + 38, 176, 89, 17, 17);
        if (xAxis >= 51 && xAxis <= 67 && yAxis >= 58 && yAxis <= 74)
            drawTexturedModalRect(guiWidth + 51, guiHeight + 58, 193, 38, 17, 17);
        if (xAxis >= 70 && xAxis <= 86 && yAxis >= 58 && yAxis <= 74)
            drawTexturedModalRect(guiWidth + 70, guiHeight + 58, 193, 55, 17, 17);
        if (xAxis >= 89 && xAxis <= 105 && yAxis >= 58 && yAxis <= 74)
            drawTexturedModalRect(guiWidth + 89, guiHeight + 58, 193, 72, 17, 17);
        if (xAxis >= 108 && xAxis <= 124 && yAxis >= 58 && yAxis <= 74)
            drawTexturedModalRect(guiWidth + 108, guiHeight + 58, 193, 89, 17, 17);
        if (xAxis >= 51 && xAxis <= 67 && yAxis >= 78 && yAxis <= 94)
            drawTexturedModalRect(guiWidth + 51, guiHeight + 78, 210, 38, 17, 17);
    }

    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        drawTime();

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int x, int y, int button)
    {
        super.mouseClicked(x, y, button);

        if (button == 0)
        {
            int xAxis = x - guiWidth;
            int yAxis = y - guiHeight;
            // Calc
            if (xAxis >= 51 && xAxis <= 67 && yAxis >= 38 && yAxis <= 54)
                mc.thePlayer.openGui(ICraft.instance, 1, mc.theWorld, 0, 0, 0);
            // Internet
            if (xAxis >= 70 && xAxis <= 86 && yAxis >= 38 && yAxis <= 54) {
                if (Loader.isModLoaded("MCEF") && !(mc.currentScreen instanceof GuiiCraftBrowser))
                {
                    mc.displayGuiScreen(InternetHandler.hasBackup() ? InternetHandler.backup : new GuiiCraftBrowser());
                    InternetHandler.backup = null;
                }
                else
                    mc.thePlayer.addChatMessage(new ChatComponentText("[iCraft] You need MCEF in order to use this function."));
            }
            // Clock
            if (xAxis >= 89 && xAxis <= 105 && yAxis >= 38 && yAxis <= 54)
                mc.thePlayer.openGui(ICraft.instance, 3, mc.theWorld, 0, 0, 0);
            // Settings
            if (xAxis >= 108 && xAxis <= 124 && yAxis >= 38 && yAxis <= 54)
                mc.thePlayer.openGui(ICraft.instance, 4, mc.theWorld, 0, 0, 0);
            // NumPad
            if (xAxis >= 51 && xAxis <= 67 && yAxis >= 58 && yAxis <= 74)
                mc.thePlayer.openGui(ICraft.instance, 5, mc.theWorld, 0, 0, 0);
            // SMS --> WIP
            if (xAxis >= 70 && xAxis <= 86 && yAxis >= 58 && yAxis <= 74)
                mc.thePlayer.openGui(ICraft.instance, 8, mc.theWorld, 0, 0, 0);
            // Online Buy
            if (xAxis >= 89 && xAxis <= 105 && yAxis >= 58 && yAxis <= 74)
            {
                if (ICraft.isIBayActive && !ICraftUtils.items.isEmpty())
                    mc.thePlayer.openGui(ICraft.instance, 9, mc.theWorld, 0, 0, 0);
            }
            // MP3 Player
            if (xAxis >= 108 && xAxis <= 124 && yAxis >= 58 && yAxis <= 74)
                mc.thePlayer.openGui(ICraft.instance, 10, mc.theWorld, 0, 0, 0);
            // Pizza Delivery
            if (xAxis >= 51 && xAxis <= 67 && yAxis >= 78 && yAxis <= 94)
                mc.thePlayer.openGui(ICraft.instance, 12, mc.theWorld, 0, 0, 0);
        }
    }
}