package alfheim.client.model.entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelBipedNew extends ModelBase {
	
	public ModelRenderer head;
	public ModelRenderer hair;
	public ModelRenderer body;
	public ModelRenderer chest;
	public ModelRenderer rightarm;
	public ModelRenderer rightglove;
	public ModelRenderer leftarm;
	public ModelRenderer leftglove;
	public ModelRenderer rightleg;
	public ModelRenderer rightboot;
	public ModelRenderer leftleg;
	public ModelRenderer leftboot;

	public ModelBipedNew() { // ModelBiped
		textureWidth = 64;
		textureHeight = 64;
		isChild = false;

		head = new ModelRenderer(this, 0, 0);
		head.addBox(-4F, -8F, -4F, 8, 8, 8);
		head.setRotationPoint(0F, 0F, 0F);
		
		hair = new ModelRenderer(this, 32, 0);
		hair.addBox(-4F, -8F, -4F, 8, 8, 8, 0.5F);
		head.addChild(hair);
		
		body = new ModelRenderer(this, 16, 16);
		body.addBox(-4F, 0F, -2F, 8, 12, 4);
		body.setRotationPoint(0F, 0F, 0F);
		
		chest = new ModelRenderer(this, 16, 32);
		chest.addBox(-4F, 0F, -2F, 8, 12, 4, 0.5F);
		body.addChild(chest);
		
		rightarm = new ModelRenderer(this, 40, 16);
		rightarm.addBox(-3F, -2F, -2F, 4, 12, 4);
		rightarm.setRotationPoint(-5F, 2F, 0F);
		
		rightglove = new ModelRenderer(this, 40, 32);
		rightglove.addBox(-3F, -2F, -2F, 4, 12, 4, 0.5F);
		rightarm.addChild(rightglove);
		
		leftarm = new ModelRenderer(this, 32, 48);
		leftarm.addBox(-1F, -2F, -2F, 4, 12, 4);
		leftarm.setRotationPoint(5F, 2F, 0F);
		
		leftglove = new ModelRenderer(this, 48, 48);
		leftglove.addBox(-1F, -2F, -2F, 4, 12, 4, 0.5F);
		leftarm.addChild(leftglove);
		
		rightleg = new ModelRenderer(this, 0, 16);
		rightleg.addBox(-2F, 0F, -2F, 4, 12, 4);
		rightleg.setRotationPoint(-2F, 12F, 0F);
		
		rightboot = new ModelRenderer(this, 0, 32);
		rightboot.addBox(-2F, 0F, -2F, 4, 12, 4, 0.5F);
		rightleg.addChild(rightboot);
		
		leftleg = new ModelRenderer(this, 16, 48);
		leftleg.addBox(-2F, 0F, -2F, 4, 12, 4);
		leftleg.setRotationPoint(2F, 12F, 0F);
		
		leftboot = new ModelRenderer(this, 0, 48);
		leftboot.addBox(-2F, 0F, -2F, 4, 12, 4, 0.5F);
		leftleg.addChild(leftboot);
	}

	public void render(Entity entity, float time, float amplitude, float ticksExisted, float yawHead, float pitchHead, float size) {
		setRotationAngles(time, amplitude, ticksExisted, yawHead, pitchHead, size, entity);
		
		if (this.isChild) {
			GL11.glPushMatrix();
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			GL11.glTranslatef(0.0F, 24.0F * size, 0.0F);
		}
			
		render(size);
		if (this.isChild) GL11.glPopMatrix();
	}
	
	public void render(float size) {
		head.render(size);
		body.render(size);
		rightarm.render(size);
		leftarm.render(size);
		rightleg.render(size);
		leftleg.render(size);
	}

	public void setRotationAngles(float time, float amplitude, float ticksExisted, float yawHead, float pitchHead, float size, Entity entity) {
		head.rotateAngleY = yawHead / (180F / (float)Math.PI);
		head.rotateAngleX = pitchHead / (180F / (float)Math.PI);
		rightarm.rotateAngleX = MathHelper.cos(time * 0.6662F + (float)Math.PI) * 2.0F * amplitude * 0.5F;
		leftarm.rotateAngleX = MathHelper.cos(time * 0.6662F) * 2.0F * amplitude * 0.5F;
		rightarm.rotateAngleZ = 0.0F;
		leftarm.rotateAngleZ = 0.0F;
		rightleg.rotateAngleX = MathHelper.cos(time * 0.6662F) * 1.4F * amplitude;
		leftleg.rotateAngleX = MathHelper.cos(time * 0.6662F + (float)Math.PI) * 1.4F * amplitude;
		rightleg.rotateAngleY = 0.0F;
		leftleg.rotateAngleY = 0.0F;

		if (entity != null && entity.isRiding()) {
			rightarm.rotateAngleX += -((float)Math.PI / 5F);
			leftarm.rotateAngleX += -((float)Math.PI / 5F);
			rightleg.rotateAngleX = -((float)Math.PI * 2F / 5F);
			leftleg.rotateAngleX = -((float)Math.PI * 2F / 5F);
			rightleg.rotateAngleY = ((float)Math.PI / 10F);
			leftleg.rotateAngleY = -((float)Math.PI / 10F);
		}

		//if (heldItemLeft != 0) leftarm.rotateAngleX = leftarm.rotateAngleX * 0.5F - ((float)Math.PI / 10F) * (float)heldItemLeft;
		//if (heldItemRight != 0) rightarm.rotateAngleX = rightarm.rotateAngleX * 0.5F - ((float)Math.PI / 10F) * (float)heldItemRight;

		rightarm.rotateAngleY = 0.0F;
		leftarm.rotateAngleY = 0.0F;
		float f6;
		float f7;

		if (onGround > -9990.0F) {
			f6 = onGround;
			body.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f6) * (float)Math.PI * 2.0F) * 0.2F;
			rightarm.rotationPointZ = MathHelper.sin(body.rotateAngleY) * 5.0F;
			rightarm.rotationPointX = -MathHelper.cos(body.rotateAngleY) * 5.0F;
			leftarm.rotationPointZ = -MathHelper.sin(body.rotateAngleY) * 5.0F;
			leftarm.rotationPointX = MathHelper.cos(body.rotateAngleY) * 5.0F;
			rightarm.rotateAngleY += body.rotateAngleY;
			leftarm.rotateAngleY += body.rotateAngleY;
			leftarm.rotateAngleX += body.rotateAngleY;
			f6 = 1.0F - onGround;
			f6 *= f6;
			f6 *= f6;
			f6 = 1.0F - f6;
			f7 = MathHelper.sin(f6 * (float)Math.PI);
			float f8 = MathHelper.sin(onGround * (float)Math.PI) * -(head.rotateAngleX - 0.7F) * 0.75F;
			rightarm.rotateAngleX = (float)((double)rightarm.rotateAngleX - ((double)f7 * 1.2 + (double)f8));
			rightarm.rotateAngleY += body.rotateAngleY * 2.0F;
			rightarm.rotateAngleZ = MathHelper.sin(onGround * (float)Math.PI) * -0.4F;
		}

		if (entity != null && entity.isSneaking()) {
			body.rotateAngleX = 0.5F;
			rightarm.rotateAngleX += 0.4F;
			leftarm.rotateAngleX += 0.4F;
			rightleg.rotationPointZ = 4.0F;
			leftleg.rotationPointZ = 4.0F;
			rightleg.rotationPointY = 9.0F;
			leftleg.rotationPointY = 9.0F;
			head.rotationPointY = 1.0F;
			hair.rotationPointY = 1.0F;
		} else {
			body.rotateAngleX = 0.0F;
			rightleg.rotationPointZ = 0.1F;
			leftleg.rotationPointZ = 0.1F;
			rightleg.rotationPointY = 12.0F;
			leftleg.rotationPointY = 12.0F;
			head.rotationPointY = 0.0F;
			hair.rotationPointY = 0.0F;
		}

		rightarm.rotateAngleZ += MathHelper.cos(ticksExisted * 0.09F) * 0.05F + 0.05F;
		leftarm.rotateAngleZ -= MathHelper.cos(ticksExisted * 0.09F) * 0.05F + 0.05F;
		rightarm.rotateAngleX += MathHelper.sin(ticksExisted * 0.067F) * 0.05F;
		leftarm.rotateAngleX -= MathHelper.sin(ticksExisted * 0.067F) * 0.05F;
	}
}