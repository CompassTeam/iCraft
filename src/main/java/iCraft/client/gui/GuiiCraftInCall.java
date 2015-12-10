package iCraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import iCraft.core.ICraft;
import iCraft.core.network.MessageReceivedCall;
import iCraft.core.network.NetworkHandler;
import iCraft.core.utils.ICraftClientUtils;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemStack;

@SideOnly(Side.CLIENT)
public class GuiiCraftInCall extends GuiiCraftBase
{
	public GuiiCraftInCall(String resource)
	{
		super(resource);
	}

    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        if (isCalling())
        {
            if (ICraftClientUtils.getClientPlayer(mc.theWorld, true) != null)
                GuiInventory.func_147046_a(88, 101, 26, 0.0F, 20.0F, ICraftClientUtils.getClientPlayer(mc.theWorld, true));
        }
        else
        {
            if (ICraftClientUtils.getClientPlayer(mc.theWorld, false) != null)
                GuiInventory.func_147046_a(88, 101, 26, 0.0F, 20.0F, ICraftClientUtils.getClientPlayer(mc.theWorld, false));
        }

        drawResizedString("Talking with " + (isCalling() ? ICraftClientUtils.getPlayerNumber(true) : ICraftClientUtils.getPlayerNumber(false)), 118, 218, 0xffffff, 0.5F);
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
			//Exit
			if (xAxis >= 51 && xAxis <= 67 && yAxis >= 38 && yAxis <= 54)
			{
				mc.thePlayer.openGui(ICraft.instance, 0, mc.theWorld, 0, 0, 0);
				NetworkHandler.sendToServer(new MessageReceivedCall(0, isCalling()));
			}
			//End Call
			if (xAxis >= 72 && xAxis <= 103 && yAxis >= 117 && yAxis <= 127)
			{
				mc.thePlayer.openGui(ICraft.instance, 0, mc.theWorld, 0, 0, 0);
				NetworkHandler.sendToServer(new MessageReceivedCall(0, isCalling()));
			}
		}
	}

	private boolean isCalling()
	{
		ItemStack itemStack = mc.thePlayer.getCurrentEquippedItem();

		return (itemStack != null && itemStack.stackTagCompound != null && itemStack.stackTagCompound.hasKey("isCalling") && itemStack.stackTagCompound.getBoolean("isCalling"));
	}
}