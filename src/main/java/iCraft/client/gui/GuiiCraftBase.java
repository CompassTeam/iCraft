package iCraft.client.gui;

import org.lwjgl.opengl.GL11;

import iCraft.core.utils.ICraftClientUtils;
import iCraft.core.utils.ICraftClientUtils.ResourceType;
import net.minecraft.client.gui.GuiScreen;

public class GuiiCraftBase extends GuiScreen
{
	private String resource;
	protected int xSize = 176;
	protected int ySize = 166;
	protected int guiWidth;
	protected int guiHeight;

	public GuiiCraftBase(String resource)
	{
		this.resource = resource;
	}

	@Override
	public void initGui()
	{
		guiWidth = (width - xSize) / 2;
		guiHeight = (height - ySize) / 2;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick)
    {
		mc.renderEngine.bindTexture(ICraftClientUtils.getResource(ResourceType.GUI, resource + ".png"));
		drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);

		super.drawScreen(mouseX, mouseY, partialTick);
    }

	protected void drawString(String toRender, int posX, int posY, int color)
	{
		drawString(toRender, posX, posY, color, false, 0);
	}

	protected void drawString(String toRender, int posX, int posY, int color, boolean scale, float size)
	{
		GL11.glPushMatrix();
		if (scale)
			GL11.glScalef(size, size, size);

        GL11.glTranslatef((float)guiWidth, (float)guiHeight, 0.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        fontRendererObj.drawString(toRender, guiWidth + posX, guiHeight + posY, color);
        GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	protected void drawTime()
	{
		drawString(ICraftClientUtils.getTime(), 164, 58, 0xffffff, true, 0.5F);
	}

	protected boolean isMouseOver(int startX, int startY, int width, int height, int mouseX, int mouseY)
    {
        int w = guiWidth;
        int h = guiHeight;
        mouseX -= w;
        mouseY -= h;
        return mouseX >= startX - 1 && mouseX < startX + width + 1 && mouseY >= startY - 1 && mouseY < startY + height + 1;
    }

	@Override
	protected void keyTyped(char ch, int keyCode)
    {
		super.keyTyped(ch, keyCode);
		if (keyCode == mc.gameSettings.keyBindInventory.getKeyCode())
        {
			mc.displayGuiScreen(null);
        }
    }

	@Override
	public boolean doesGuiPauseGame()
    {
        return false;
    }
}