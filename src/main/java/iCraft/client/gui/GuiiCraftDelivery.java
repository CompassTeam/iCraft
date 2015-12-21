package iCraft.client.gui;

import iCraft.core.ICraft;
import iCraft.core.network.MessageDelivery;
import iCraft.core.network.NetworkHandler;
import iCraft.core.utils.ICraftUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiiCraftDelivery extends GuiiCraftBase
{
    private int qnt = 1;
    private GuiButton qntMore;
    private GuiButton qntLess;
    private GuiButton buy;

    public GuiiCraftDelivery(String resource)
    {
        super(resource);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        Keyboard.enableRepeatEvents(false);
        buttonList.clear();

        qntMore = new GuiButton(0, guiWidth + 60, guiHeight + 80, 15, 20, "\u25B2");
        qntLess = new GuiButton(1, guiWidth + 60, guiHeight + 100, 15, 20, "\u25BC");
        buy = new GuiButton(2, guiWidth + 85, guiHeight + 90, 30, 20, "Buy");

        buttonList.add(qntMore);
        buttonList.add(qntLess);
        buttonList.add(buy);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton)
    {
        if (!guiButton.enabled)
            return;

        switch (guiButton.id)
        {
            case 0:
                qnt = (qnt + 1 <= 32 ? qnt + 1 : qnt);
                break;
            case 1:
                qnt = (qnt - 1 >= 1 ? qnt - 1 : qnt);
                break;
            case 2:
                NetworkHandler.sendToServer(new MessageDelivery(qnt));
                mc.thePlayer.closeScreen();
                mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "[" + EnumChatFormatting.GOLD + "iCraft" + EnumChatFormatting.BLUE + "] " + ICraftUtils.localize("msg.iCraft.delivery")));
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTick)
    {
        super.drawScreen(mouseX, mouseY, partialTick);

        GL11.glPushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glEnable(GL11.GL_LIGHTING);
        itemRender.zLevel = 100.0F;

        ItemStack iron = new ItemStack(Items.iron_ingot, (qnt * 2));
        ItemStack pizza = new ItemStack(ICraft.pizza, qnt);

        itemRender.renderItemAndEffectIntoGUI(iron, guiWidth + 53, guiHeight + 45);
        itemRender.renderItemOverlays(fontRendererObj, iron, guiWidth + 53, guiHeight + 45);

        itemRender.renderItemAndEffectIntoGUI(pizza, guiWidth + 107, guiHeight + 45);
        itemRender.renderItemOverlays(fontRendererObj, pizza, guiWidth + 107, guiHeight + 45);

        itemRender.zLevel = 0.0F;
        GL11.glDisable(GL11.GL_LIGHTING);
        if (isMouseOver(53, 45, 16, 16, mouseX, mouseY))
            renderToolTip(iron, mouseX, mouseY);
        else if (isMouseOver(107, 45, 16, 16, mouseX, mouseY))
            renderToolTip(pizza, mouseX, mouseY);

        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
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
        }
    }
}