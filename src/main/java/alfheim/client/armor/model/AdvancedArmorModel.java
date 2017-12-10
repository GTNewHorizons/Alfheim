package alfheim.client.armor.model;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public abstract class AdvancedArmorModel extends ModelBiped {
	public int color = -1;//Раскраска брони в цвет.
	private boolean usingParachute;
	public abstract void pre();//До всех частей.
	public abstract void post();//После всех частей.
	public abstract void partHead();//Часть: Голова.
	public abstract void partBody();//Часть: Тело.
	public abstract void partRightArm();//Часть: Правая рука.
	public abstract void partLeftArm();//Часть: Левая рука.
	public abstract void partRightLeg();//Часть: Правая нога.
	public abstract void partLeftLeg();//Часть: Левая нога.

	public void render(Entity entity, float x, float y, float z, float yaw, float pitch, float parTicks) {
		super.render(entity, x, y, z, yaw, pitch, parTicks);
		
		this.bipedHead.showModel = false;
		this.bipedBody.showModel = false;
		this.bipedHeadwear.showModel = false;
		this.bipedLeftArm.showModel = false;
		this.bipedLeftLeg.showModel = false;
		this.bipedRightArm.showModel = false;
		this.bipedRightLeg.showModel = false;
		
		GL11.glPushMatrix();
		
		if (entity instanceof EntityZombie || entity instanceof EntitySkeleton || entity instanceof EntityGiantZombie) {
			float f6 = MathHelper.sin(onGround * (float)Math.PI);
			float f7 = MathHelper.sin((1.0F - (1.0F - onGround) * (1.0F - onGround)) * (float)Math.PI);

			bipedRightArm.rotateAngleZ = 0.0F;
			bipedRightArm.rotateAngleY = -(0.1F - f6 * 0.6F);
			bipedRightArm.rotateAngleX = -((float)Math.PI / 2F);
			bipedRightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
			bipedRightArm.rotateAngleZ += MathHelper.cos(z * 0.09F) * 0.05F + 0.05F;
			bipedRightArm.rotateAngleX += MathHelper.sin(z * 0.067F) * 0.05F;

			bipedLeftArm.rotateAngleZ = 0.0F;
			bipedLeftArm.rotateAngleY = 0.1F - f6 * 0.6F;
			bipedLeftArm.rotateAngleX = -((float)Math.PI / 2F);
			bipedLeftArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
			bipedLeftArm.rotateAngleZ -= MathHelper.cos(z * 0.09F) * 0.05F + 0.05F;
			bipedLeftArm.rotateAngleX -= MathHelper.sin(z * 0.067F) * 0.05F;

			if (entity instanceof EntitySkeleton && ((EntitySkeleton)entity).getSkeletonType() == 1)
				GL11.glScalef(1.2F, 1.2F, 1.2F);

			else if (entity instanceof EntityGiantZombie)
				GL11.glScalef(6F, 6F, 6F);

		}


		if (color != -1) {
			float red = (float)(color >> 16 & 255) / 255F;
			float blue = (float)(color >> 8 & 255) / 255F;
			float green = (float)(color & 255) / 255F;
			GL11.glColor3f(red, blue, green);
		}

		pre();

		float f6 = 2.0F;

		{//partHead
			GL11.glPushMatrix();
			if (isChild) {
				GL11.glScalef(1.5F / f6, 1.5F / f6, 1.5F / f6);
				GL11.glTranslatef(0.0F, 16.0F * parTicks, 0.0F);
			}
			GL11.glTranslatef(bipedHead.rotationPointX * parTicks, bipedHead.rotationPointY * parTicks, bipedHead.rotationPointZ * parTicks);
			GL11.glRotatef(bipedHead.rotateAngleZ * (180F / (float)Math.PI), 0F, 0F, 1F);
			GL11.glRotatef(bipedHead.rotateAngleY * (180F / (float)Math.PI), 0F, 1F, 0F);
			GL11.glRotatef(bipedHead.rotateAngleX * (180F / (float)Math.PI), 1F, 0F, 0F);
			GL11.glRotatef(180F, 1F, 0F, 0F);
			partHead();
			GL11.glPopMatrix();
		}

		if (isChild) {
			GL11.glPushMatrix();
			GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
			GL11.glTranslatef(0.0F, 24.0F * parTicks, 0.0F);
		}

		{//partBody
			GL11.glPushMatrix();
			GL11.glTranslatef(bipedBody.rotationPointX * parTicks, bipedBody.rotationPointY * parTicks, bipedBody.rotationPointZ * parTicks);
			GL11.glRotatef(bipedBody.rotateAngleZ * (180F / (float)Math.PI), 0F, 0F, 1F);
			GL11.glRotatef(bipedBody.rotateAngleY * (180F / (float)Math.PI), 0F, 1F, 0F);
			GL11.glRotatef(bipedBody.rotateAngleX * (180F / (float)Math.PI), 1F, 0F, 0F);
			GL11.glRotatef(180F, 1F, 0F, 0F);
			partBody();
			GL11.glPopMatrix();
		}

		{//partRightArm
			GL11.glPushMatrix();
			GL11.glTranslatef(bipedRightArm.rotationPointX * parTicks, bipedRightArm.rotationPointY * parTicks, bipedRightArm.rotationPointZ * parTicks);
			GL11.glRotatef(bipedRightArm.rotateAngleZ * (180F / (float)Math.PI), 0F, 0F, 1F);
			GL11.glRotatef(bipedRightArm.rotateAngleY * (180F / (float)Math.PI), 0F, 1F, 0F);
			GL11.glRotatef(bipedRightArm.rotateAngleX * (180F / (float)Math.PI), 1F, 0F, 0F);
			GL11.glRotatef(180F, 1F, 0F, 0F);
			partRightArm();
			GL11.glPopMatrix();
		}

		{//partLeftArm
			GL11.glPushMatrix();
			GL11.glTranslatef(bipedLeftArm.rotationPointX * parTicks, bipedLeftArm.rotationPointY * parTicks, bipedLeftArm.rotationPointZ * parTicks);
			GL11.glRotatef(bipedLeftArm.rotateAngleZ * (180F / (float)Math.PI), 0F, 0F, 1F);
			GL11.glRotatef(bipedLeftArm.rotateAngleY * (180F / (float)Math.PI), 0F, 1F, 0F);
			GL11.glRotatef(bipedLeftArm.rotateAngleX * (180F / (float)Math.PI), 1F, 0F, 0F);
			GL11.glRotatef(180F, 1F, 0F, 0F);
			partLeftArm();
			GL11.glPopMatrix();
		}

		{//partRightLeg
			GL11.glPushMatrix();
			GL11.glTranslatef(bipedRightLeg.rotationPointX * parTicks, bipedRightLeg.rotationPointY * parTicks, bipedRightLeg.rotationPointZ * parTicks);
			GL11.glRotatef(bipedRightLeg.rotateAngleZ * (180F / (float)Math.PI), 0F, 0F, 1F);
			GL11.glRotatef(bipedRightLeg.rotateAngleY * (180F / (float)Math.PI), 0F, 1F, 0F);
			GL11.glRotatef(bipedRightLeg.rotateAngleX * (180F / (float)Math.PI), 1F, 0F, 0F);
			GL11.glRotatef(180F, 1F, 0F, 0F);
			partRightLeg();
			GL11.glPopMatrix();
		}

		{//partLeftLeg
			GL11.glPushMatrix();
			GL11.glTranslatef(bipedLeftLeg.rotationPointX * parTicks, bipedLeftLeg.rotationPointY * parTicks, bipedLeftLeg.rotationPointZ * parTicks);
			GL11.glRotatef(bipedLeftLeg.rotateAngleZ * (180F / (float)Math.PI), 0F, 0F, 1F);
			GL11.glRotatef(bipedLeftLeg.rotateAngleY * (180F / (float)Math.PI), 0F, 1F, 0F);
			GL11.glRotatef(bipedLeftLeg.rotateAngleX * (180F / (float)Math.PI), 1F, 0F, 0F);
			GL11.glRotatef(180F, 1F, 0F, 0F);
			partLeftLeg();
			GL11.glPopMatrix();
		}

		if (isChild) {
			GL11.glPopMatrix();
		}

		post();

		GL11.glColor3f(1F, 1F, 1F);

		GL11.glPopMatrix();

	}
}
