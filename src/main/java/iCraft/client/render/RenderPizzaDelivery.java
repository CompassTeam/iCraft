package iCraft.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import iCraft.client.model.ModelPizzaDelivery;
import iCraft.core.utils.ICraftClientUtils;
import iCraft.core.utils.ICraftClientUtils.ResourceType;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderPizzaDelivery extends RenderLiving
{
    public RenderPizzaDelivery()
    {
        super(new ModelPizzaDelivery(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return ICraftClientUtils.getResource(ResourceType.RENDER, "PizzaDelivery.png");
    }
}