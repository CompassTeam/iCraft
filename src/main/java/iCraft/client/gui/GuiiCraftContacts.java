package iCraft.client.gui;

import iCraft.core.ICraft;
import iCraft.core.network.MessageContacts;
import iCraft.core.network.MessageIncomeCalling;
import iCraft.core.network.NetworkHandler;
import iCraft.core.utils.ICraftUtils;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.math.NumberUtils;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

public class GuiiCraftContacts extends GuiiCraftBase
{
    private boolean isInEditMode = false;
    private float scroll;
    private boolean isDragging = false;
    private boolean canScroll;
    private int dragOffset = 0;
    private List<Integer> list = new ArrayList<Integer>();
    private GuiTextField textField;

    public GuiiCraftContacts(String resource)
    {
        super(resource);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        textField = new GuiTextField(fontRendererObj, guiWidth + 52, guiHeight + 54, 72, 10);
        textField.setMaxStringLength(8);
        textField.setFocused(false);
        list = (mc.thePlayer.getCurrentEquippedItem().stackTagCompound != null ? readNBT(mc.thePlayer.getCurrentEquippedItem().stackTagCompound) : list);
        canScroll = list.size() > 5;
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        textField.updateCursorCounter();
        list = (mc.thePlayer.getCurrentEquippedItem().stackTagCompound != null ? readNBT(mc.thePlayer.getCurrentEquippedItem().stackTagCompound) : list);
        canScroll = list.size() > 5;
    }

    public int getScroll()
    {
        return Math.max(Math.min((int)(scroll * 55), 55), 0);
    }

    public int getIndex()
    {
        if(list.size() <= 5)
        {
            return 0;
        }

        return (int)((list.size() * scroll) - ((5 / (float)list.size())) * scroll);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY)
    {
        super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);
        drawTexturedModalRect(guiWidth + 113, guiHeight + 67 + getScroll(), (canScroll ? 0 : 11), 180, 11, 15);

        drawTexturedModalRect(guiWidth + 114, guiHeight + 36, 190, (isInEditMode ? 51 : 42), 9, 9);

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
        textField.drawTextBox();

        if (isMouseOver(114, 36, 9, 9, mouseX, mouseY))
        {
            List<String> tooltip = new ArrayList<String>();
            tooltip.add(ICraftUtils.localize("iCraft.gui.contactListMode"));

            drawHoveringText(tooltip, mouseX, mouseY, fontRendererObj);
        }
    }

    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        fontRendererObj.drawString(ICraftUtils.localize("iCraft.gui.Contacts"), 58, 40, 0xffffff);
        for (int i = 0; i < 5; i++)
        {
            if (getIndex() + i < list.size())
            {
                int yStart = i * 28 + 69;

                drawResizedString(String.valueOf(list.get(getIndex() + i)), 110, yStart + 73, 0xffffff, 0.5F);
            }
        }
        drawTime();

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int x, int y, int button)
    {
        super.mouseClicked(x, y, button);
        textField.mouseClicked(x, y, button);

        if (button == 0)
        {
            int xAxis = x - guiWidth;
            int yAxis = y - guiHeight;
            // Exit
            if (xAxis >= 80 && xAxis <= 95 && yAxis >= 143 && yAxis <= 158)
            {
                mc.thePlayer.openGui(ICraft.instance, 0, mc.theWorld, 0, 0, 0);
            }
            // Contact List Mode
            if (xAxis >= 114 && xAxis <= 122 && yAxis >= 36 && yAxis <= 44)
            {
                isInEditMode = !isInEditMode;
            }
            // Slider
            if (xAxis >= 113 && xAxis <= 123 && yAxis >= getScroll() + 67 && yAxis <= getScroll() + 67 + 15)
            {
                if (canScroll)
                {
                    dragOffset = yAxis - (getScroll() + 67);
                    isDragging = true;
                }
            }
            for (int i = 0; i < 5; i++)
            {

                if (getIndex() + i < list.size())
                {
                    int yStart = i * 14 + 67;

                    if(xAxis >= 52 && xAxis <= 111 && yAxis >= yStart && yAxis <= yStart + 14)
                    {
                        if (!isInEditMode)
                            NetworkHandler.sendToServer(new MessageIncomeCalling(mc.thePlayer.getCurrentEquippedItem().stackTagCompound.getInteger("number"), list.get(getIndex() + i)));
                        else
                            NetworkHandler.sendToServer(new MessageContacts(1, list.get(getIndex() + i)));
                    }
                }
            }
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int button, long ticks)
    {
        super.mouseClickMove(mouseX, mouseY, button, ticks);

        int yAxis = mouseY - guiHeight;

        if(isDragging)
            scroll = Math.min(Math.max((float)(yAxis - 67 - dragOffset) / 55, 0), 1);
    }

    @Override
    protected void mouseMovedOrUp(int x, int y, int type)
    {
        super.mouseMovedOrUp(x, y, type);

        if(type == 0 && isDragging)
        {
            dragOffset = 0;
            isDragging = false;
        }
    }

    @Override
    public void handleMouseInput()
    {
        super.handleMouseInput();

        int i = Mouse.getEventDWheel();
        if (i != 0 && canScroll)
        {
            if (i > 0)
                i = 1;
            if (i < 0)
                i = -1;

            scroll = Math.min(Math.max((float)((double)scroll - (double)i / (double)20), 0), 1);
        }
    }

    @Override
    protected void keyTyped(char ch, int keyCode)
    {
        textField.textboxKeyTyped(ch, keyCode);
        if (textField.isFocused() && keyCode != 1)
        {
            if (keyCode == 28)
            {
                if (!list.contains(Integer.parseInt(textField.getText())) && NumberUtils.isNumber(textField.getText()))
                    NetworkHandler.sendToServer(new MessageContacts(0, Integer.parseInt(textField.getText())));

                textField.setText("");
            }
        }
        else
            super.keyTyped(ch, keyCode);
    }

    private List<Integer> readNBT(NBTTagCompound nbtTags)
    {
        NBTTagList tagList = nbtTags.getTagList("contacts", 10);
        List<Integer> contacts = new ArrayList<Integer>();
        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
            int j = tagCompound.getInteger("number" + i);
            contacts.add(i, j);
        }
        return contacts;
    }
}