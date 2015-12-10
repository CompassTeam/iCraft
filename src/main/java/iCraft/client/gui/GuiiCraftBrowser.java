package iCraft.client.gui;

import org.lwjgl.opengl.GL11;

import iCraft.client.InternetHandler;
import iCraft.core.utils.ICraftClientUtils;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.montoyo.mcef.api.API;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.MCEFApi;

public class GuiiCraftBrowser extends GuiScreen
{
	public IBrowser browser = null;
	private GuiButton back = null;
	private GuiButton fwd = null;
	private GuiButton go = null;
	private GuiButton min = null;
	private GuiNewTextField url = null;

	@Override
	public void initGui()
	{
		if (browser == null)
		{
			API api = MCEFApi.getAPI();
			if (api == null)
				return;

			browser = api.createBrowser(ICraftClientUtils.homePage);
		}
		if (browser != null)
			browser.resize(mc.displayWidth, mc.displayHeight - scaleY(40));

		Keyboard.enableRepeatEvents(true);
		buttonList.clear();

		if (url == null)
		{
			buttonList.add(back = (new GuiButton(0, 0, 20, 20, 20, "<")));
			buttonList.add(fwd = (new GuiButton(1, 20, 20, 20, 20, ">")));
			buttonList.add(go = (new GuiButton(2, width - 40, 20, 20, 20, "Go")));
			buttonList.add(min = (new GuiButton(3, width - 20, 20, 20, 20, "_")));

			url = new GuiNewTextField(fontRendererObj, 45, 26, width - 92, 20);
			url.setMaxStringLength(65535);
			url.setEnableBackgroundDrawing(false);
			url.setText(ICraftClientUtils.homePage);
		}
		else
		{
			buttonList.add(back);
			buttonList.add(fwd);
			buttonList.add(go);
			buttonList.add(min);

			go.xPosition = width - 40;
			min.xPosition = width - 20;

			String old = url.getText();
			url = new GuiNewTextField(fontRendererObj, 45, 26, width - 92, 20);
			url.setMaxStringLength(65535);
			url.setEnableBackgroundDrawing(false);
			url.setText(old);
		}
	}

	public int scaleY(int y)
	{
		double sy = ((double) y) / ((double) height) * ((double) mc.displayHeight);
		return (int) sy;
	}

	public void loadURL(String url)
	{
		if (url.contains("."))
			browser.loadURL(url);
		else
			browser.loadURL("https://www.google.com/search?q=" + url);
	}

	@Override
	public void updateScreen()
	{
		if (!url.isFocused() && !browser.getURL().equals(url.getText()))
			url.setText(browser.getURL());
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick)
	{
		drawRect(0, 0, width, 21, 0xff000000);
		drawRect(0, 21, width, 40, 0xff0066ff);
		drawRect(0, 22, width, 39, 0xffffffff);
		drawString(fontRendererObj, ICraftClientUtils.getTime(), width / 2, 6, 0xffffff);
		url.drawTextBox();

		super.drawScreen(mouseX, mouseY, partialTick);

		if (browser != null)
		{
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			browser.draw(0, height, width, 40.0D);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		}
	}

	@Override
	public void onGuiClosed()
	{
		if (!InternetHandler.hasBackup() && browser != null)
			browser.close();

		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public void handleInput()
	{
		while (Keyboard.next())
		{
			if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
			{
				mc.displayGuiScreen(null);
				return;
			}

			boolean pressed = Keyboard.getEventKeyState();
			boolean focused = url.isFocused();
			char key = Keyboard.getEventCharacter();
			int num = Keyboard.getEventKey();

			if (browser != null && !focused)
			{
				if (key != '.' && key != ';' && key != ',')
				{
					if (pressed)
						browser.injectKeyPressed(key, 0);
					else
						browser.injectKeyReleased(key, 0);
				}
				if (key != Keyboard.CHAR_NONE)
					browser.injectKeyTyped(key, 0);
			}
			if (!pressed && focused && num == Keyboard.KEY_RETURN)
				actionPerformed(go);
			else
				url.textboxKeyTyped(key, num);
		}
		while (Mouse.next())
		{
			int btn = Mouse.getEventButton();
			boolean pressed = Mouse.getEventButtonState();
			int sx = Mouse.getEventX();
			int sy = Mouse.getEventY();

			if (browser != null)
			{
				int y = mc.displayHeight - sy - scaleY(40);
				if (btn == -1)
					browser.injectMouseMove(sx, y, 0, y < 0);
				else
					browser.injectMouseButton(sx, y, 0, btn + 1, pressed, 1);
			}
			if (pressed)
			{
				int x = sx * width / mc.displayWidth;
				int y = height - (sy * height / mc.displayHeight) - 1;

				try {
					mouseClicked(x, y, btn);
				} catch (Exception e) {
					e.printStackTrace();
				}

				url.mouseClicked(x, y, btn);
			}
		}
	}

	public void onUrlChanged(IBrowser b, String nurl)
	{
		if (b == browser && url != null)
			url.setText(nurl);
	}

	@Override
	protected void actionPerformed(GuiButton src)
	{
		if (browser == null)
			return;

		if (src.id == 0)
		{
			browser.goBack();
		}
		else if (src.id == 1)
			browser.goForward();
		else if (src.id == 2)
			loadURL(url.getText());
		else if (src.id == 3) {
			InternetHandler.setBackup(this);
			mc.displayGuiScreen(null);
		}
	}

	@Override
	public boolean doesGuiPauseGame()
    {
        return false;
    }
}