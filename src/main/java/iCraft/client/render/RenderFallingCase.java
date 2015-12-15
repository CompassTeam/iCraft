package iCraft.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import iCraft.client.model.ModelFallingCase;
import iCraft.core.entity.EntityPackingCase;
import iCraft.core.utils.ICraftClientUtils;
import iCraft.core.utils.ICraftClientUtils.ResourceType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderFallingCase extends Render
{
    private Minecraft mc = Minecraft.getMinecraft();

    public ModelFallingCase model = new ModelFallingCase();

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return ICraftClientUtils.getResource(ResourceType.RENDER, "PackingCase.png");
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float f, float partialTick)
    {
        EntityPackingCase packingCase = (EntityPackingCase) entity;

        if (!packingCase.isDead)
            render(x, y, z);
    }

    public void render(double x, double y, double z)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glRotatef(180, 1, 0, 0);
        GL11.glTranslatef(0, 0.9F, 0);

        mc.renderEngine.bindTexture(ICraftClientUtils.getResource(ResourceType.RENDER, "PackingCase.png"));

        model.render(0.0625F);

        GL11.glPopMatrix();
    }
}