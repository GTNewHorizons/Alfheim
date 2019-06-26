package alexsocol.asjlib.render;

import static org.lwjgl.opengl.GL11.*;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

/**
 * Advanced Armor Model Wrapping
 * @author Timaxa007
 * */
public abstract class AdvancedArmorModel extends ModelBiped {
	public final int color = -1;//��������� ����� � ����.
	public abstract void pre(Entity entity);//�� ���� ������.
	public abstract void post(Entity entity);//����� ���� ������.
	public abstract void partHead(Entity entity);//�����: ������.
	public abstract void partBody(Entity entity);//�����: ����.
	public abstract void partRightArm(Entity entity);//�����: ������ ����.
	public abstract void partLeftArm(Entity entity);//�����: ����� ����.
	public abstract void partRightLeg(Entity entity);//�����: ������ ����.
	public abstract void partLeftLeg(Entity entity);//�����: ����� ����.
	
	public void render(Entity entity, float x, float y, float z, float yaw, float pitch, float parTicks) {
		this.bipedHead.showModel = false;
		this.bipedBody.showModel = false;
		this.bipedHeadwear.showModel = false;
		this.bipedLeftArm.showModel = false;
		this.bipedLeftLeg.showModel = false;
		this.bipedRightArm.showModel = false;
		this.bipedRightLeg.showModel = false;
		
		glPushMatrix();
		
		{
			if(entity instanceof EntityLivingBase) {
				EntityLivingBase living = (EntityLivingBase) entity;
				isSneak = living.isSneaking();
				isChild = living.isChild();
				isRiding = living.isRiding();
				
				ItemStack itemstack = living.getHeldItem();
				heldItemRight = itemstack != null ? 1 : 0;
				
				if(entity instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) entity;
					
					aimedBow = false;
					if (itemstack != null && player.getItemInUseCount() > 0) {
						EnumAction enumaction = itemstack.getItemUseAction();
						
						if (enumaction == EnumAction.block)
							heldItemRight = 3;
						else if(enumaction == EnumAction.bow)
							aimedBow = true;
					}
				}
			}
		}
		
		super.render(entity, x, y, z, yaw, pitch, parTicks);
		
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
				glScalef(1.2F, 1.2F, 1.2F);
			
			else if (entity instanceof EntityGiantZombie)
				glScalef(6F, 6F, 6F);
		
		}
		
		if (color != -1) {
			float red = (float)(color >> 16 & 255) / 255F;
			float blue = (float)(color >> 8 & 255) / 255F;
			float green = (float)(color & 255) / 255F;
			glColor3f(red, blue, green);
		}
		
		pre(entity);
		
		float f6 = 2.0F;
		
		{//partHead
			glPushMatrix();
			if (isChild) {
				glScalef(1.5F / f6, 1.5F / f6, 1.5F / f6);
				glTranslatef(0.0F, 16.0F * parTicks, 0.0F);
			}
			glTranslatef(bipedHead.rotationPointX * parTicks, bipedHead.rotationPointY * parTicks, bipedHead.rotationPointZ * parTicks);
			glRotatef(bipedHead.rotateAngleZ * (180F / (float)Math.PI), 0, 0, 1);
			glRotatef(bipedHead.rotateAngleY * (180F / (float)Math.PI), 0, 1, 0);
			glRotatef(bipedHead.rotateAngleX * (180F / (float)Math.PI), 1, 0, 0);
			glRotatef(180F, 1, 0, 0);
			partHead(entity);
			glPopMatrix();
		}
		
		if (isChild) {
			glPushMatrix();
			glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
			glTranslatef(0.0F, 24.0F * parTicks, 0.0F);
		}
		
		{//partBody
			glPushMatrix();
			glTranslatef(bipedBody.rotationPointX * parTicks, bipedBody.rotationPointY * parTicks, bipedBody.rotationPointZ * parTicks);
			glRotatef(bipedBody.rotateAngleZ * (180F / (float)Math.PI), 0, 0, 1);
			glRotatef(bipedBody.rotateAngleY * (180F / (float)Math.PI), 0, 1, 0);
			glRotatef(bipedBody.rotateAngleX * (180F / (float)Math.PI), 1, 0, 0);
			glRotatef(180F, 1, 0, 0);
			partBody(entity);
			glPopMatrix();
		}
		
		{//partRightArm
			glPushMatrix();
			glTranslatef(bipedRightArm.rotationPointX * parTicks, bipedRightArm.rotationPointY * parTicks, bipedRightArm.rotationPointZ * parTicks);
			glRotatef(bipedRightArm.rotateAngleZ * (180F / (float)Math.PI), 0, 0, 1);
			glRotatef(bipedRightArm.rotateAngleY * (180F / (float)Math.PI), 0, 1, 0);
			glRotatef(bipedRightArm.rotateAngleX * (180F / (float)Math.PI), 1, 0, 0);
			glRotatef(180F, 1, 0, 0);
			partRightArm(entity);
			glPopMatrix();
		}
		
		{//partLeftArm
			glPushMatrix();
			glTranslatef(bipedLeftArm.rotationPointX * parTicks, bipedLeftArm.rotationPointY * parTicks, bipedLeftArm.rotationPointZ * parTicks);
			glRotatef(bipedLeftArm.rotateAngleZ * (180F / (float)Math.PI), 0, 0, 1);
			glRotatef(bipedLeftArm.rotateAngleY * (180F / (float)Math.PI), 0, 1, 0);
			glRotatef(bipedLeftArm.rotateAngleX * (180F / (float)Math.PI), 1, 0, 0);
			glRotatef(180F, 1, 0, 0);
			partLeftArm(entity);
			glPopMatrix();
		}
		
		{//partRightLeg
			glPushMatrix();
			glTranslatef(bipedRightLeg.rotationPointX * parTicks, bipedRightLeg.rotationPointY * parTicks, bipedRightLeg.rotationPointZ * parTicks);
			glRotatef(bipedRightLeg.rotateAngleZ * (180F / (float)Math.PI), 0, 0, 1);
			glRotatef(bipedRightLeg.rotateAngleY * (180F / (float)Math.PI), 0, 1, 0);
			glRotatef(bipedRightLeg.rotateAngleX * (180F / (float)Math.PI), 1, 0, 0);
			glRotatef(180F, 1, 0, 0);
			partRightLeg(entity);
			glPopMatrix();
		}
		
		{//partLeftLeg
			glPushMatrix();
			glTranslatef(bipedLeftLeg.rotationPointX * parTicks, bipedLeftLeg.rotationPointY * parTicks, bipedLeftLeg.rotationPointZ * parTicks);
			glRotatef(bipedLeftLeg.rotateAngleZ * (180F / (float)Math.PI), 0, 0, 1);
			glRotatef(bipedLeftLeg.rotateAngleY * (180F / (float)Math.PI), 0, 1, 0);
			glRotatef(bipedLeftLeg.rotateAngleX * (180F / (float)Math.PI), 1, 0, 0);
			glRotatef(180F, 1, 0, 0);
			partLeftLeg(entity);
			glPopMatrix();
		}
		
		if (isChild) {
			glPopMatrix();
		}
		
		post(entity);
		
		glColor3f(1F, 1, 1);
		glPopMatrix();
	}
}
