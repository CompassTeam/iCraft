package iCraft.client.gui;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import iCraft.core.ICraft;
import iCraft.core.entity.EntityPizzaDelivery;
import iCraft.core.inventory.container.ContainerPizzaDelivery;
import iCraft.core.utils.ICraftClientUtils;
import iCraft.core.utils.ICraftClientUtils.ResourceType;
import iCraft.core.utils.ICraftUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

@SideOnly(Side.CLIENT)
public class GuiPizzaDelivery extends GuiContainer
{
	private EntityPizzaDelivery delivery;
	public GuiPizzaDelivery(InventoryPlayer inventory, EntityPizzaDelivery delivery)
	{
		super(new ContainerPizzaDelivery(inventory, delivery));
		this.delivery = delivery;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		fontRendererObj.drawString(ICraftUtils.localize("container.inventory"), 8, ySize - 96 + 3, 0x404040);

		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY)
	{
		mc.renderEngine.bindTexture(ICraftClientUtils.getResource(ResourceType.GUI, "GuiPizzaDelivery.png"));
		int guiWidth = (width - xSize) / 2;
		int guiHeight = (height - ySize) / 2;
		drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick)
	{
		super.drawScreen(mouseX, mouseY, partialTick);

		int guiWidth = (width - xSize) / 2;
		int guiHeight = (height - ySize) / 2;
		GL11.glPushMatrix();
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_LIGHTING);
		itemRender.zLevel = 100.0F;

		ItemStack iron = new ItemStack(Items.iron_ingot, (delivery.getQuantity() * 2));
		ItemStack pizza = new ItemStack(ICraft.pizza, delivery.getQuantity());

		itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), iron, guiWidth + 62, guiHeight + 24);
		itemRender.renderItemOverlayIntoGUI(fontRendererObj, mc.getTextureManager(), iron, guiWidth + 62, guiHeight + 24);

		itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), pizza, guiWidth + 120, guiHeight + 24);
		itemRender.renderItemOverlayIntoGUI(fontRendererObj, mc.getTextureManager(), pizza, guiWidth + 120, guiHeight + 24);

		itemRender.zLevel = 0.0F;
		GL11.glDisable(GL11.GL_LIGHTING);
		if (func_146978_c(62, 24, 16, 16, mouseX, mouseY))
		{
			renderToolTip(iron, mouseX, mouseY);
		}
		else if (func_146978_c(120, 24, 16, 16, mouseX, mouseY))
		{
			renderToolTip(pizza, mouseX, mouseY);
		}
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		RenderHelper.enableStandardItemLighting();
	}
}