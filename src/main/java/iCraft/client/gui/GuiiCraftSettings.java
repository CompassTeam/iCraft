package iCraft.client.gui;

import iCraft.core.ICraft;

public class GuiiCraftSettings extends GuiiCraftBase
{
	public GuiiCraftSettings(String resource)
	{
		super(resource);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick)
	{
		super.drawScreen(mouseX, mouseY, partialTick);

		int xAxis = mouseX - guiWidth;
		int yAxis = mouseY - guiHeight;
		boolean mouseOver = xAxis >= 51 && xAxis <= 124 && yAxis >= 54 && yAxis <= 67;
		drawTexturedModalRect(guiWidth + 51, guiHeight + 54, mouseOver ? 0 : 74, 166, 74, 14);
		//drawString("Settings", 1, 4, 0xffffff); TODO
		//drawString("Blacklist", -58, 20, 0xffffff); TODO
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
			// Exit
			if (xAxis >= 80 && xAxis <= 95 && yAxis >= 143 && yAxis <= 158)
			{
				mc.thePlayer.openGui(ICraft.instance, 0, mc.theWorld, 0, 0, 0);
			}
			// Blacklist
			if (xAxis >= 51 && xAxis <= 124 && yAxis >= 54 && yAxis <= 67)
			{
				mc.thePlayer.openGui(ICraft.instance, 13, mc.theWorld, 0, 0, 0);
			}
		}
	}
}