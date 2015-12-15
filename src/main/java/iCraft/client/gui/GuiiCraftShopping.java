package iCraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import iCraft.core.ICraft;
import iCraft.core.network.MessageCraftBay;
import iCraft.core.network.NetworkHandler;
import iCraft.core.utils.ICraftClientUtils;
import iCraft.core.utils.ICraftUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.Collection;

@SideOnly(Side.CLIENT)
public class GuiiCraftShopping extends GuiiCraftBase
{
    private int i = 0;
    private int qnt = 1;

    public GuiiCraftShopping(String resource)
    {
        super(resource);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        Keyboard.enableRepeatEvents(false);
        buttonList.clear();

        GuiButton left = new GuiButton(0, guiWidth + 52, guiHeight + 75, 15, 20, "<");
        GuiButton right = new GuiButton(1, guiWidth + 96, guiHeight + 75, 15, 20, ">");
        GuiButton up = new GuiButton(2, guiWidth + 125, guiHeight + 49, 10, 10, "\u25B2");
        GuiButton down = new GuiButton(3, guiWidth + 125, guiHeight + 58, 10, 10, "\u25BC");
        GuiButton buy = new GuiButton(4, guiWidth + 67, guiHeight + 75, 29, 20, "Buy");

        buttonList.add(left);
        buttonList.add(right);
        buttonList.add(up);
        buttonList.add(down);
        buttonList.add(buy);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        if (getBuyStack(ICraftUtils.items.get(i).keySet()).stackSize * qnt > 64 || getSellStack(ICraftUtils.items.get(i).values()).stackSize * qnt > 64)
            qnt = 1;
    }

    private ItemStack getBuyStack(Collection<ItemStack> col)
    {
        return (ItemStack) col.toArray()[0];
    }

    private ItemStack getSellStack(Collection<ItemStack> col)
    {
        return (ItemStack) col.toArray()[0];
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

        ItemStack buyItem = new ItemStack(getBuyStack(ICraftUtils.items.get(i).keySet()).getItem(), getBuyStack(ICraftUtils.items.get(i).keySet()).stackSize * qnt, getBuyStack(ICraftUtils.items.get(i).keySet()).getItemDamage());
        ItemStack sellItem = new ItemStack(getSellStack(ICraftUtils.items.get(i).values()).getItem(), getSellStack(ICraftUtils.items.get(i).values()).stackSize * qnt, getSellStack(ICraftUtils.items.get(i).values()).getItemDamage());

        itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), buyItem, guiWidth + 98, guiHeight + 51);
        itemRender.renderItemOverlayIntoGUI(fontRendererObj, mc.getTextureManager(), buyItem, guiWidth + 98, guiHeight + 51);
        itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), sellItem, guiWidth + 48, guiHeight + 51);
        itemRender.renderItemOverlayIntoGUI(fontRendererObj, mc.getTextureManager(), sellItem, guiWidth + 48, guiHeight + 51);

        itemRender.zLevel = 0.0F;
        GL11.glDisable(GL11.GL_LIGHTING);
        if (isMouseOver(98, 51, 16, 16, mouseX, mouseY))
            renderToolTip(getBuyStack(ICraftUtils.items.get(i).keySet()), mouseX, mouseY);

        if (isMouseOver(48, 51, 16, 16, mouseX, mouseY))
            renderToolTip(getSellStack(ICraftUtils.items.get(i).values()), mouseX, mouseY);

        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        drawResizedString(ICraftClientUtils.getTime(), 148, 45, 0xffffff, 0.5F);

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton)
    {
        if (!guiButton.enabled)
            return;

        switch (guiButton.id)
        {
            case 0:
                i = (i - 1 >= 0 ? i - 1 : ICraftUtils.items.size() - 1);
                break;
            case 1:
                i = (i + 1 < ICraftUtils.items.size() ? i + 1 : 0);
                break;
            case 2:
                qnt = ((getBuyStack(ICraftUtils.items.get(i).keySet()).stackSize * (qnt + 1) <= 64 && getSellStack(ICraftUtils.items.get(i).values()).stackSize * (qnt + 1) <= 64) ? qnt + 1 : qnt);
                break;
            case 3:
                qnt = (qnt - 1 > 0 ? qnt - 1 : qnt);
                break;
            case 4:
                if (ICraft.isIBayActive)
                {
                    NetworkHandler.sendToServer(new MessageCraftBay(getBuyStack(ICraftUtils.items.get(i).keySet()).getItem(), getBuyStack(ICraftUtils.items.get(i).keySet()).stackSize * qnt, getBuyStack(ICraftUtils.items.get(i).keySet()).getItemDamage(), getSellStack(ICraftUtils.items.get(i).values()).getItem(), getSellStack(ICraftUtils.items.get(i).values()).stackSize * qnt, getSellStack(ICraftUtils.items.get(i).values()).getItemDamage()));
                    mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "[" + EnumChatFormatting.GOLD + "iCraft" + EnumChatFormatting.BLUE + "] " + EnumChatFormatting.GREEN + ICraftUtils.localize("msg.iCraft.iBay")));
                    mc.thePlayer.closeScreen();
                    break;
                }
        }
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
            if (xAxis >= 143 && xAxis <= 158 && yAxis >= 51 && yAxis <= 66)
                mc.thePlayer.openGui(ICraft.instance, 0, mc.theWorld, 0, 0, 0);
        }
    }
}