package iCraft.client.gui;

import iCraft.core.ICraft;
import iCraft.core.utils.ICraftClientUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiiCraftClock extends GuiiCraftBase
{
    private float scroll;
    private boolean isDragging = false;
    private boolean canScroll;
    private int dragOffset = 0;
    private List<String> list = new ArrayList<String>();

    public GuiiCraftClock(String resource)
    {
        super(resource);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        canScroll = list.size() > 5;
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        canScroll = list.size() > 5;
    }

    public int getScroll()
    {
        return Math.max(Math.min((int) (scroll * 55), 55), 0);
    }

    public int getIndex()
    {
        if (list.size() <= 5)
            return 0;

        return (int) ((list.size() * scroll) - ((5 / (float) list.size())) * scroll);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY)
    {
        super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);

        drawTexturedModalRect(guiWidth + 113, guiHeight + 67 + getScroll(), (canScroll ? 0 : 11), 180, 11, 15);

        int xAxis = mouseX - guiWidth;
        int yAxis = mouseY - guiHeight;

        for (int i = 0; i < 5; i++)
        {
            if (getIndex() + i < list.size())
            {
                int yStart = i * 14 + 67;
                boolean mouseOver = xAxis >= 52 && xAxis <= 111 && yAxis >= yStart && yAxis <= yStart + 14;

                drawTexturedModalRect(guiWidth + 52, guiHeight + yStart, mouseOver ? 0 : 60, 166, 60, 14);
            }
        }
    }

    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        for (int i = 0; i < 5; i++)
        {
            if (getIndex() + i < list.size())
            {
                int yStart = i * 28 + 66;

                drawResizedString(list.get(getIndex() + i), 110, yStart + 73, 0xffffff, 0.5F);
            }
        }

        GL11.glPushMatrix();
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glTranslatef((float) guiWidth, (float) guiHeight, 0.0F);
        fontRendererObj.drawString(ICraftClientUtils.getTime(), 75, 46, 0xffffff);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();

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
                mc.thePlayer.openGui(ICraft.instance, 0, mc.theWorld, 0, 0, 0);
            // Slider
            if (xAxis >= 113 && xAxis <= 123 && yAxis >= getScroll() + 67 && yAxis <= getScroll() + 67 + 15)
            {
                if (canScroll)
                {
                    dragOffset = yAxis - (getScroll() + 67);
                    isDragging = true;
                }
            }
            // Change Time Format
            if (xAxis >= 80 && xAxis <= 95 && yAxis >= 143 && yAxis <= 158)
                ICraftClientUtils.hour24 = !ICraftClientUtils.hour24;
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int button, long ticks)
    {
        super.mouseClickMove(mouseX, mouseY, button, ticks);

        int yAxis = mouseY - guiHeight;

        if (isDragging)
            scroll = Math.min(Math.max((float) (yAxis - 67 - dragOffset) / 55, 0), 1);
    }

    @Override
    protected void mouseReleased(int x, int y, int type)
    {
        super.mouseReleased(x, y, type);

        if (type == 0 && isDragging)
        {
            dragOffset = 0;
            isDragging = false;
        }
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();

        int i = Mouse.getEventDWheel();
        if (i != 0 && canScroll)
        {
            if (i > 0)
                i = 1;
            if (i < 0)
                i = -1;

            scroll = Math.min(Math.max((float) ((double) scroll - (double) i / (double) 20), 0), 1);
        }
    }
}