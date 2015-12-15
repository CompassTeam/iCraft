package iCraft.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

/**
 * This class is entirely based on the original villager model
 */
@SideOnly(Side.CLIENT)
public class ModelPizzaDelivery extends ModelBase
{
    /**
     * The head box of the PizzaDeliveryModel
     */
    public ModelRenderer deliveryHead;
    /**
     * The body of the PizzaDeliveryModel
     */
    public ModelRenderer deliveryBody;
    /**
     * The arms of the PizzaDeliveryModel
     */
    public ModelRenderer deliveryArms;
    /**
     * The right leg of the PizzaDeliveryModel
     */
    public ModelRenderer rightDeliveryLeg;
    /**
     * The left leg of the PizzaDeliveryModel
     */
    public ModelRenderer leftDeliveryLeg;
    public ModelRenderer deliveryNose;

    public ModelPizzaDelivery()
    {
        deliveryHead = (new ModelRenderer(this)).setTextureSize(64, 64);
        deliveryHead.setRotationPoint(0.0F, 0.0F + 0, 0.0F);
        deliveryHead.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, 0);
        deliveryNose = (new ModelRenderer(this)).setTextureSize(64, 64);
        deliveryNose.setRotationPoint(0.0F, 0 - 2.0F, 0.0F);
        deliveryNose.setTextureOffset(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2, 4, 2, 0);
        deliveryHead.addChild(deliveryNose);
        deliveryBody = (new ModelRenderer(this)).setTextureSize(64, 64);
        deliveryBody.setRotationPoint(0.0F, 0.0F + 0, 0.0F);
        deliveryBody.setTextureOffset(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, 0);
        deliveryBody.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, 0 + 0.5F);
        deliveryArms = (new ModelRenderer(this)).setTextureSize(64, 64);
        deliveryArms.setRotationPoint(0.0F, 0.0F + 0 + 2.0F, 0.0F);
        deliveryArms.setTextureOffset(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4, 8, 4, 0);
        deliveryArms.setTextureOffset(44, 22).addBox(4.0F, -2.0F, -2.0F, 4, 8, 4, 0);
        deliveryArms.setTextureOffset(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8, 4, 4, 0);
        rightDeliveryLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(64, 64);
        rightDeliveryLeg.setRotationPoint(-2.0F, 12.0F + 0, 0.0F);
        rightDeliveryLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0);
        leftDeliveryLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(64, 64);
        leftDeliveryLeg.mirror = true;
        leftDeliveryLeg.setRotationPoint(2.0F, 12.0F + 0, 0.0F);
        leftDeliveryLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        deliveryHead.render(f5);
        deliveryBody.render(f5);
        rightDeliveryLeg.render(f5);
        leftDeliveryLeg.render(f5);
        deliveryArms.render(f5);
    }

    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        deliveryHead.rotateAngleY = p_78087_4_ / (180F / (float) Math.PI);
        deliveryHead.rotateAngleX = p_78087_5_ / (180F / (float) Math.PI);
        deliveryArms.rotationPointY = 3.0F;
        deliveryArms.rotationPointZ = -1.0F;
        deliveryArms.rotateAngleX = -0.75F;
        rightDeliveryLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_ * 0.5F;
        leftDeliveryLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + (float) Math.PI) * 1.4F * p_78087_2_ * 0.5F;
        rightDeliveryLeg.rotateAngleY = 0.0F;
        leftDeliveryLeg.rotateAngleY = 0.0F;
    }
}