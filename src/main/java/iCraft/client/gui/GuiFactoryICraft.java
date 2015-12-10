package iCraft.client.gui;

import iCraft.core.utils.ICraftUtils;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;

public class GuiFactoryICraft implements IModGuiFactory
{
	@Override
	public void initialize(Minecraft minecraftInstance) {}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass()
	{
		return ConfigGUI.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element)
	{
		return null;
	}

	public static class ConfigGUI extends GuiConfig
	{
		public ConfigGUI(GuiScreen parent)
		{
			super(parent, ICraftUtils.getConfigElements(), "iCraft", false, true, ICraftUtils.localize("iCraft.configgui.title"));
		}
	}
}