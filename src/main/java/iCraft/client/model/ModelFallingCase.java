package iCraft.client.model;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelFallingCase extends ModelBase
{
	// fields
	ModelRenderer Shape1;
	ModelRenderer Shape2;
	ModelRenderer Shape3;
	ModelRenderer Shape4;
	ModelRenderer Shape5;
	ModelRenderer String1;
	ModelRenderer String2;
	ModelRenderer String3;
	ModelRenderer String4;
	ModelRenderer Shape6;
	ModelRenderer Shape7;
	ModelRenderer Shape8;
	ModelRenderer Shape9;
	ModelRenderer Shape10;
	ModelRenderer Shape11;
	ModelRenderer Shape12;
	ModelRenderer Shape13;
	ModelRenderer Shape14;
	ModelRenderer Shape15;
	ModelRenderer Shape16;

	public ModelFallingCase()
	{
		textureWidth = 512;
		textureHeight = 512;

		Shape1 = new ModelRenderer(this, 2, 2);
		Shape1.addBox(-7F, -14F, -7F, 14, 14, 14);
		Shape1.setRotationPoint(0F, 24F, 0F);
		Shape1.setTextureSize(512, 512);
		Shape1.mirror = true;
		setRotation(Shape1, 0F, 0F, 0F);
		Shape2 = new ModelRenderer(this, 0, 58);
		Shape2.addBox(-8F, -15F, -8F, 2, 2, 14);
		Shape2.setRotationPoint(0F, 24F, 1F);
		Shape2.setTextureSize(512, 512);
		Shape2.mirror = true;
		setRotation(Shape2, 0F, 0F, 0F);
		Shape3 = new ModelRenderer(this, 0, 58);
		Shape3.addBox(7F, -15F, -8F, 2, 2, 14);
		Shape3.setRotationPoint(-1F, 24F, 1F);
		Shape3.setTextureSize(512, 512);
		Shape3.mirror = true;
		setRotation(Shape3, 0F, 0F, 0F);
		Shape4 = new ModelRenderer(this, 0, 58);
		Shape4.addBox(-8F, -15F, -7F, 2, 2, 14);
		Shape4.setRotationPoint(0F, 24F, 0F);
		Shape4.setTextureSize(512, 512);
		Shape4.mirror = true;
		setRotation(Shape4, 0F, 1.570796F, 0F);
		Shape5 = new ModelRenderer(this, 0, 58);
		Shape5.addBox(-8F, -15F, -7F, 2, 2, 14);
		Shape5.setRotationPoint(0F, 24F, 0F);
		Shape5.setTextureSize(512, 512);
		Shape5.mirror = true;
		setRotation(Shape5, 0F, -1.570796F, 0F);
		String1 = new ModelRenderer(this, 0, 95);
		String1.addBox(0F, -55F, 0F, 1, 55, 1);
		String1.setRotationPoint(-7F, 10F, -7F);
		String1.setTextureSize(512, 512);
		String1.mirror = true;
		setRotation(String1, 0.4363323F, 0F, -0.4363323F);
		String2 = new ModelRenderer(this, 0, 95);
		String2.addBox(0F, -54F, 0F, 1, 55, 1);
		String2.setRotationPoint(-7.5F, 9F, 7F);
		String2.setTextureSize(512, 512);
		String2.mirror = true;
		setRotation(String2, -0.4363323F, 0F, -0.4363323F);
		String3 = new ModelRenderer(this, 0, 95);
		String3.addBox(0F, -56F, 0F, 1, 55, 1);
		String3.setRotationPoint(6.5F, 10F, 6.5F);
		String3.setTextureSize(512, 512);
		String3.mirror = true;
		setRotation(String3, -0.4363323F, 0F, 0.4363323F);
		String4 = new ModelRenderer(this, 0, 95);
		String4.addBox(0F, -54.5F, 0F, 1, 55, 1);
		String4.setRotationPoint(6.5F, 9F, -7.5F);
		String4.setTextureSize(512, 512);
		String4.mirror = true;
		setRotation(String4, 0.4363323F, 0F, 0.4363323F);
		Shape6 = new ModelRenderer(this, 66, 50);
		Shape6.addBox(0F, 0F, 0F, 66, 1, 27);
		Shape6.setRotationPoint(-33F, -29F, -45F);
		Shape6.setTextureSize(512, 512);
		Shape6.mirror = true;
		setRotation(Shape6, 0.4625123F, 0F, 0F);
		Shape7 = new ModelRenderer(this, 66, 50);
		Shape7.addBox(0F, 0F, 0F, 66, 1, 41);
		Shape7.setRotationPoint(-33F, -41F, -21F);
		Shape7.setTextureSize(512, 512);
		Shape7.mirror = true;
		setRotation(Shape7, 0F, 0F, 0F);
		Shape8 = new ModelRenderer(this, 66, 50);
		Shape8.addBox(0F, 0F, 0F, 66, 1, 27);
		Shape8.setRotationPoint(-33F, -41F, 20F);
		Shape8.setTextureSize(512, 512);
		Shape8.mirror = true;
		setRotation(Shape8, -0.4537856F, 0F, 0F);
		Shape9 = new ModelRenderer(this, 0, 56);
		Shape9.addBox(0F, 0F, 0F, 2, 15, 2);
		Shape9.setRotationPoint(-8F, 9F, -8F);
		Shape9.setTextureSize(512, 512);
		Shape9.mirror = true;
		setRotation(Shape9, 0F, 0F, 0F);
		Shape10 = new ModelRenderer(this, 0, 56);
		Shape10.addBox(0F, 0F, 0F, 2, 15, 2);
		Shape10.setRotationPoint(-8F, 9F, 6F);
		Shape10.setTextureSize(512, 512);
		Shape10.mirror = true;
		setRotation(Shape10, 0F, 0F, 0F);
		Shape11 = new ModelRenderer(this, 0, 56);
		Shape11.addBox(0F, 0F, 0F, 2, 15, 2);
		Shape11.setRotationPoint(6F, 9F, 6F);
		Shape11.setTextureSize(512, 512);
		Shape11.mirror = true;
		setRotation(Shape11, 0F, 0F, 0F);
		Shape12 = new ModelRenderer(this, 0, 56);
		Shape12.addBox(0F, 0F, 0F, 2, 15, 2);
		Shape12.setRotationPoint(6F, 9F, -8F);
		Shape12.setTextureSize(512, 512);
		Shape12.mirror = true;
		setRotation(Shape12, 0F, 0F, 0F);
		Shape13 = new ModelRenderer(this, 0, 58);
		Shape13.addBox(0F, 0F, 0F, 2, 2, 14);
		Shape13.setRotationPoint(7F, 22F, -8F);
		Shape13.setTextureSize(512, 512);
		Shape13.mirror = true;
		setRotation(Shape13, 0F, -1.570796F, 0F);
		Shape14 = new ModelRenderer(this, 0, 58);
		Shape14.addBox(0F, 0F, 0F, 2, 2, 14);
		Shape14.setRotationPoint(7F, 22F, 6F);
		Shape14.setTextureSize(512, 512);
		Shape14.mirror = true;
		setRotation(Shape14, 0F, -1.570796F, 0F);
		Shape15 = new ModelRenderer(this, 0, 58);
		Shape15.addBox(0F, 0F, 0F, 2, 2, 14);
		Shape15.setRotationPoint(6F, 22F, -7F);
		Shape15.setTextureSize(512, 512);
		Shape15.mirror = true;
		setRotation(Shape15, 0F, 0F, 0F);
		Shape16 = new ModelRenderer(this, 0, 58);
		Shape16.addBox(0F, 0F, 0F, 2, 2, 14);
		Shape16.setRotationPoint(-8F, 22F, -7F);
		Shape16.setTextureSize(512, 512);
		Shape16.mirror = true;
		setRotation(Shape16, 0F, 0F, 0F);
	}

	public void render(float size)
	{
		GL11.glPushMatrix();
		GL11.glScalef(1.5F, 1.5F, 1.5F);
		GL11.glTranslatef(0, -0.07F, 0);

		Shape1.render(size);
		Shape2.render(size);
		Shape3.render(size);
		Shape4.render(size);
		Shape5.render(size);
		String1.render(size);
		String2.render(size);
		String3.render(size);
		String4.render(size);
		Shape6.render(size);
		Shape7.render(size);
		Shape8.render(size);
		Shape9.render(size);
		Shape10.render(size);
		Shape11.render(size);
		Shape12.render(size);
		Shape13.render(size);
		Shape14.render(size);
		Shape15.render(size);
		Shape15.render(size);
		Shape16.render(size);

		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glScalef(0.2F, 1, 0.2F);
		GL11.glPopMatrix();
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}