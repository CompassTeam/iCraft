package iCraft.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@SideOnly(Side.CLIENT)
public class GuiNewTextField extends GuiTextField
{
    private final FontRenderer fontRendererObj;
    private String text;
    private int cursorCounter;
    private boolean isEnabled;
    private int lineScrollOffset;
    private int enabledColor;
    private int disabledColor;

    public GuiNewTextField(FontRenderer fontRenderer, int startX, int startY, int sizeX, int sizeY)
    {
        super(fontRenderer, startX, startY, sizeX, sizeY);
        fontRendererObj = fontRenderer;
    }

    private void initializeVars()
    {
        Class textField = getClass().getSuperclass();
        try {
            text = getText();

            Field cursorC = GuiTextField.class.getDeclaredField("cursorCounter");
            cursorC.setAccessible(true);
            cursorCounter = (Integer) cursorC.get(textField);

            Field isE = GuiTextField.class.getDeclaredField("isEnabled");
            isE.setAccessible(true);
            isEnabled = (Boolean) isE.get(textField);

            Field lineScroll = GuiTextField.class.getDeclaredField("lineScrollOffset");
            lineScroll.setAccessible(true);
            lineScrollOffset = (Integer) lineScroll.get(textField);

            Field enabledCl = GuiTextField.class.getDeclaredField("enabledColor");
            enabledCl.setAccessible(true);
            enabledColor = (Integer) enabledCl.get(textField);

            Field disabledCl = GuiTextField.class.getDeclaredField("disabledColor");
            disabledCl.setAccessible(true);
            disabledColor = (Integer) disabledCl.get(textField);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void drawTextBox()
    {
        initializeVars();

        if (getVisible())
        {
            if (getEnableBackgroundDrawing())
            {
                drawRect(xPosition - 1, yPosition - 1, xPosition + width + 1, yPosition + height + 1, -6250336);
                drawRect(xPosition, yPosition, xPosition + width, yPosition + height, -16777216);
            }

            int i = isEnabled ? enabledColor : disabledColor;
            int j = getCursorPosition() - lineScrollOffset;
            int k = getSelectionEnd() - lineScrollOffset;
            String s = fontRendererObj.trimStringToWidth(text.substring(lineScrollOffset), getWidth());
            boolean flag = j >= 0 && j <= s.length();
            boolean flag1 = isFocused() && cursorCounter / 6 % 2 == 0 && flag;
            int l = getEnableBackgroundDrawing() ? xPosition + 4 : xPosition;
            int i1 = getEnableBackgroundDrawing() ? yPosition + (height - 8) / 2 : yPosition;
            int j1 = l;

            if (k > s.length())
                k = s.length();

            if (s.length() > 0)
            {
                String s1 = flag ? s.substring(0, j) : s;
                j1 = fontRendererObj.drawString(s1, l, i1, i);
            }

            boolean flag2 = getCursorPosition() < text.length() || text.length() >= getMaxStringLength();
            int k1 = j1;

            if (!flag)
                k1 = j > 0 ? l + width : l;
            else if (flag2)
            {
                k1 = j1 - 1;
                --j1;
            }

            if (s.length() > 0 && flag && j < s.length())
                fontRendererObj.drawString(s.substring(j), j1, i1, i);

            if (flag1)
            {
                if (flag2)
                    Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + fontRendererObj.FONT_HEIGHT, -3092272);
                else
                    fontRendererObj.drawString("_", k1, i1, i);
            }

            if (k != j)
            {
                int l1 = l + fontRendererObj.getStringWidth(s.substring(0, k));
                Class textField = getClass().getSuperclass();
                try {
                    Method drawCursor = GuiTextField.class.getDeclaredMethod("drawCursorVertical", Integer.class, Integer.class, Integer.class, Integer.class);
                    drawCursor.setAccessible(true);
                    drawCursor.invoke(textField, k1, i1 - 1, l1 - 1, i1 + 1 + fontRendererObj.FONT_HEIGHT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}