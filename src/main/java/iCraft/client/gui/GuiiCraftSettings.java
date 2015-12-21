package iCraft.client.gui;

import iCraft.core.ICraft;
import iCraft.core.utils.ICraftUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiiCraftSettings extends GuiiCraftBase
{
    public GuiiCraftSettings(String resource)
    {
        super(resource);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY)
    {
        super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);

        int xAxis = mouseX - guiWidth;
        int yAxis = mouseY - guiHeight;
        boolean mouseOver = xAxis >= 51 && xAxis <= 124 && yAxis >= 54 && yAxis <= 67;
        drawTexturedModalRect(guiWidth + 51, guiHeight + 54, (mouseOver ? 0 : 74), 166, 74, 14);
        boolean mouseOver1 = xAxis >= 51 && xAxis <= 124 && yAxis >= 68 && yAxis <= 84;
        drawTexturedModalRect(guiWidth + 51, guiHeight + 68, (mouseOver1 ? 0 : 74), 166, 74, 14);
    }

    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        fontRendererObj.drawString(ICraftUtils.localize("iCraft.gui.Settings"), 58, 40, 0xffffff);
        fontRendererObj.drawString(ICraftUtils.localize("iCraft.gui.Blacklist"), 58, 57, 0xffffff);
        fontRendererObj.drawString(ICraftUtils.localize("iCraft.gui.Contacts"), 58, 71, 0xffffff);
        drawTime();

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException
    {
        super.mouseClicked(x, y, button);

        if (button == 0)
        {
            int xAxis = x - guiWidth;
            int yAxis = y - guiHeight;
            // Exit
            if (xAxis >= 80 && xAxis <= 95 && yAxis >= 143 && yAxis <= 158)
                mc.thePlayer.openGui(ICraft.instance, 0, mc.theWorld, 0, 0, 0);
            // Blacklist
            if (xAxis >= 51 && xAxis <= 124 && yAxis >= 54 && yAxis <= 67)
                mc.thePlayer.openGui(ICraft.instance, 13, mc.theWorld, 0, 0, 0);
            // Contacts
            if (xAxis >= 51 && xAxis <= 124 && yAxis >= 68 && yAxis <= 84)
                mc.thePlayer.openGui(ICraft.instance, 14, mc.theWorld, 0, 0, 0);
        }
    }
}