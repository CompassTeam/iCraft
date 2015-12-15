package iCraft.client.render;

import iCraft.client.model.ModelPackingCase;
import iCraft.core.tile.TilePackingCase;
import iCraft.core.utils.ICraftClientUtils;
import iCraft.core.utils.ICraftClientUtils.ResourceType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class RenderPackingCase extends TileEntitySpecialRenderer
{
    private ModelPackingCase model = new ModelPackingCase();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTick)
    {
        render((TilePackingCase) tileEntity, (float) x, (float) y, (float) z, partialTick);
    }

    private void render(TilePackingCase tileEntity, float x, float y, float z, float partialTick)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(x + 0.5F, y + 1.5F, z + 0.5F);
        bindTexture(ICraftClientUtils.getResource(ResourceType.RENDER, "PackingCase.png"));

        switch (tileEntity.facing)
        {
            case 2:
                GL11.glRotatef(0, 0.0F, 1.0F, 0.0F);
                break;
            case 3:
                GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);
                break;
            case 4:
                GL11.glRotatef(90, 0.0F, 1.0F, 0.0F);
                break;
            case 5:
                GL11.glRotatef(270, 0.0F, 1.0F, 0.0F);
                break;
        }

        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        model.render(0.0625F);
        GL11.glPopMatrix();
    }
}