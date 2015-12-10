package iCraft.client.gui;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;

import iCraft.core.utils.ICraftClientUtils;
import iCraft.core.utils.ICraftClientUtils.ResourceType;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL12;

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
        drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        super.drawScreen(mouseX, mouseY, partialTick);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glPushMatrix();
        GL11.glTranslatef((float) guiWidth, (float) guiHeight, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        short short1 = 240;
        short short2 = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) short1 / 1.0F, (float) short2 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glDisable(GL11.GL_LIGHTING);
        drawGuiContainerForegroundLayer(mouseX, mouseY);
        GL11.glEnable(GL11.GL_LIGHTING);

        GL11.glPopMatrix();

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
    }

    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY)
    {
        mc.renderEngine.bindTexture(ICraftClientUtils.getResource(ResourceType.GUI, resource + ".png"));
        drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);
    }

    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {}

	protected void drawResizedString(String toRender, int posX, int posY, int color, float size)
	{
		GL11.glPushMatrix();
        GL11.glScalef(size, size, size);
        fontRendererObj.drawString(toRender, posX, posY, color);
		GL11.glPopMatrix();
	}

	protected void drawTime()
	{
        drawResizedString(ICraftClientUtils.getTime(), 164, 58, 0xffffff, 0.5F);
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