package iCraft.client.gui;

import iCraft.core.ICraft;
import iCraft.core.network.MessageIncomeCalling;
import iCraft.core.network.NetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiiCraftNumPad extends GuiiCraftBase
{
    public static String callNumber = "";

    public GuiiCraftNumPad(String resource)
    {
        super(resource);
    }

    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        drawResizedString(callNumber, 154, 74, 0x404040, 0.5F);
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
            //Exit
            if (xAxis >= 80 && xAxis <= 95 && yAxis >= 143 && yAxis <= 158)
            {
                mc.thePlayer.openGui(ICraft.instance, 0, mc.theWorld, 0, 0, 0);
                callNumber = "";
            }
            if (callNumber.length() < 8)
            {
                //0
                if (xAxis >= 79 && xAxis <= 95 && yAxis >= 104 && yAxis <= 120)
                    callNumber += "0";
                //1
                if (xAxis >= 61 && xAxis <= 77 && yAxis >= 86 && yAxis <= 102)
                    callNumber += "1";
                //2
                if (xAxis >= 79 && xAxis <= 95 && yAxis >= 86 && yAxis <= 102)
                    callNumber += "2";
                //3
                if (xAxis >= 98 && xAxis <= 114 && yAxis >= 86 && yAxis <= 102)
                    callNumber += "3";
                //4
                if (xAxis >= 61 && xAxis <= 77 && yAxis >= 68 && yAxis <= 84)
                    callNumber += "4";
                //5
                if (xAxis >= 79 && xAxis <= 95 && yAxis >= 68 && yAxis <= 84)
                    callNumber += "5";
                //6
                if (xAxis >= 98 && xAxis <= 114 && yAxis >= 68 && yAxis <= 84)
                    callNumber += "6";
                //7
                if (xAxis >= 61 && xAxis <= 77 && yAxis >= 50 && yAxis <= 66)
                    callNumber += "7";
                //8
                if (xAxis >= 79 && xAxis <= 95 && yAxis >= 50 && yAxis <= 66)
                    callNumber += "8";
                //9
                if (xAxis >= 98 && xAxis <= 114 && yAxis >= 50 && yAxis <= 66)
                    callNumber += "9";
            }
            //Clear
            if (xAxis >= 89 && xAxis <= 114 && yAxis >= 124 && yAxis <= 134)
            {
                if (callNumber.length() != 0)
                    callNumber = callNumber.substring(0, callNumber.length() - 1);
            }
            //Call
            if (xAxis >= 61 && xAxis <= 85 && yAxis >= 124 && yAxis <= 134)
            {
                if (callNumber.length() == 8)
                    NetworkHandler.sendToServer(new MessageIncomeCalling(mc.thePlayer.getCurrentEquippedItem().getTagCompound().getInteger("number"), Integer.parseInt(callNumber)));
            }
        }
    }
}