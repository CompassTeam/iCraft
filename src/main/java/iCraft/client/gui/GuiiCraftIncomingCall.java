package iCraft.client.gui;

import iCraft.core.ICraft;
import iCraft.core.network.MessageReceivedCall;
import iCraft.core.network.NetworkHandler;
import iCraft.core.utils.ICraftClientUtils;
import iCraft.core.utils.ICraftClientUtils.ResourceType;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemStack;

@SideOnly(Side.CLIENT)
public class GuiiCraftIncomingCall extends GuiiCraftBase
{
	public GuiiCraftIncomingCall(String resource)
	{
		super(resource);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick)
    {
		mc.renderEngine.bindTexture(ICraftClientUtils.getResource(ResourceType.GUI, (isCalling() ? "GuiiCraftInCall.png" : "GuiiCraftIncomingCall.png")));
		guiWidth = (width - xSize) / 2;
		guiHeight = (height - ySize) / 2;
		drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);

		if (isCalling())
		{
			if (ICraftClientUtils.getClientPlayer(mc.theWorld, true) != null)
				GuiInventory.func_147046_a(guiWidth + 88, guiHeight + 101, 26, 0.0F, 20.0F, ICraftClientUtils.getClientPlayer(mc.theWorld, true));
			drawString("Calling to " + ICraftClientUtils.getPlayerNumber(true), 118, 218, 0xffffff, true, 0.5F);
		}
		else
		{
			if (ICraftClientUtils.getClientPlayer(mc.theWorld, false) != null)
				GuiInventory.func_147046_a(guiWidth + 88, guiHeight + 101, 26, 0.0F, 20.0F, ICraftClientUtils.getClientPlayer(mc.theWorld, false));
			drawString(ICraftClientUtils.getPlayerNumber(false) + " is calling you", 118, 218, 0xffffff, true, 0.5F);
		}
		drawTime();
    }

	@Override
	protected void mouseClicked(int x, int y, int button)
	{
		super.mouseClicked(x, y, button);

		if (button == 0)
		{
			int xAxis = x - guiWidth;
			int yAxis = y - guiHeight;
			if (isCalling())
			{
				// Cancel
				if(xAxis >= 80 && xAxis <= 95 && yAxis >= 143 && yAxis <= 158)
				{
					mc.thePlayer.openGui(ICraft.instance, 0, mc.theWorld, 0, 0, 0);
					NetworkHandler.sendToServer(new MessageReceivedCall(0, true));
				}
				//Cancel
				if(xAxis >= 72 && xAxis <= 103 && yAxis >= 117 && yAxis <= 127)
				{
					mc.thePlayer.openGui(ICraft.instance, 0, mc.theWorld, 0, 0, 0);
					NetworkHandler.sendToServer(new MessageReceivedCall(0, true));
				}
			}
			else
			{
				// Exit
				if (xAxis >= 80 && xAxis <= 95 && yAxis >= 143 && yAxis <= 158)
				{
					mc.thePlayer.openGui(ICraft.instance, 0, mc.theWorld, 0, 0, 0);
					NetworkHandler.sendToServer(new MessageReceivedCall(0, false));
				}
				// Accept
				if (xAxis >= 53 && xAxis <= 84 && yAxis >= 121 && yAxis <= 131)
				{
					mc.thePlayer.openGui(ICraft.instance, 7, mc.theWorld, 0, 0, 0);
					NetworkHandler.sendToServer(new MessageReceivedCall(7, false));
				}
				// Deny
				if (xAxis >= 90 && xAxis <= 122 && yAxis >= 121 && yAxis <= 131)
				{
					mc.thePlayer.openGui(ICraft.instance, 0, mc.theWorld, 0, 0, 0);
					NetworkHandler.sendToServer(new MessageReceivedCall(0, false));
				}
			}
		}
	}

	private boolean isCalling()
	{
		ItemStack itemStack = mc.thePlayer.getCurrentEquippedItem();

		return (itemStack != null && itemStack.stackTagCompound != null && itemStack.stackTagCompound.hasKey("isCalling") && itemStack.stackTagCompound.getBoolean("isCalling"));
	}
}