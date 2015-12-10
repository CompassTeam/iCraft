package iCraft.client.render;

import org.lwjgl.opengl.GL11;

import iCraft.client.model.ModelPackingCase;
import iCraft.core.ICraft;
import iCraft.core.utils.ICraftClientUtils;
import iCraft.core.utils.ICraftClientUtils.ResourceType;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class ItemRenderHandler implements IItemRenderer
{
	private ModelPackingCase modelPackingCase = new ModelPackingCase();

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		if(type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON)
		{
			GL11.glTranslatef(0.5F, 0, 0);
		}

		if(item.getItem() == Item.getItemFromBlock(ICraft.caseBlock))
		{
			GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			GL11.glTranslatef(0, 2.0F, 1.0F);
			GL11.glScalef(1.0F, -1F, -1F);

			Minecraft.getMinecraft().renderEngine.bindTexture(ICraftClientUtils.getResource(ResourceType.RENDER, "PackingCase.png"));

			modelPackingCase.render(0.0625F);
		}
	}
}