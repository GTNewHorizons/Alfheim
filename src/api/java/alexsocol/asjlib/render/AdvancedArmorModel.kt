package alexsocol.asjlib.render

import alexsocol.asjlib.F
import net.minecraft.client.model.ModelBiped
import net.minecraft.entity.*
import net.minecraft.entity.monster.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumAction
import net.minecraft.util.MathHelper
import org.lwjgl.opengl.GL11.*

/**
 * Advanced Armor Model Wrapping
 * @author Timaxa007
 */
abstract class AdvancedArmorModel: ModelBiped() {
	
	var color = -1//��������� ����� � ����.
	abstract fun pre(entity: Entity) //�� ���� ������.
	abstract fun post(entity: Entity) //����� ���� ������.
	abstract fun partHead(entity: Entity) //�����: ������.
	abstract fun partBody(entity: Entity) //�����: ����.
	abstract fun partRightArm(entity: Entity) //�����: ������ ����.
	abstract fun partLeftArm(entity: Entity) //�����: ����� ����.
	abstract fun partRightLeg(entity: Entity) //�����: ������ ����.
	abstract fun partLeftLeg(entity: Entity) //�����: ����� ����.
	
	abstract fun hasOffhand(entity: Entity): Boolean
	
	override fun render(entity: Entity, x: Float, y: Float, z: Float, yaw: Float, pitch: Float, parTicks: Float) {
		bipedHead.showModel = false
		bipedBody.showModel = false
		bipedHeadwear.showModel = false
		bipedLeftArm.showModel = false
		bipedLeftLeg.showModel = false
		bipedRightArm.showModel = false
		bipedRightLeg.showModel = false
		
		glPushMatrix()
		
		run {
			if (entity is EntityLivingBase) {
				val living = entity as EntityLivingBase?
				isSneak = living!!.isSneaking
				isChild = living.isChild
				isRiding = living.isRiding
				
				val itemstack = living.heldItem
				heldItemRight = if (itemstack != null) 1 else 0
				heldItemLeft = if (hasOffhand(entity)) 1 else 0
				
				if (entity is EntityPlayer) {
					val player = entity as EntityPlayer?
					
					aimedBow = false
					if (itemstack != null && player!!.itemInUseCount > 0) {
						val enumaction = itemstack.itemUseAction
						
						if (enumaction == EnumAction.block) heldItemRight = 3
						else if (enumaction == EnumAction.bow) aimedBow = true
					}
				}
			}
		}
		
		super.render(entity, x, y, z, yaw, pitch, parTicks)
		
		if (entity is EntityZombie || entity is EntitySkeleton || entity is EntityGiantZombie) {
			val f6 = MathHelper.sin(onGround * Math.PI.F)
			val f7 = MathHelper.sin((1f - (1f - onGround) * (1f - onGround)) * Math.PI.F)
			
			bipedRightArm.rotateAngleZ = 0f
			bipedRightArm.rotateAngleY = -(0.1f - f6 * 0.6f)
			bipedRightArm.rotateAngleX = -(Math.PI.F / 2f)
			bipedRightArm.rotateAngleX -= f6 * 1.2f - f7 * 0.4f
			bipedRightArm.rotateAngleZ += MathHelper.cos(z * 0.09f) * 0.05f + 0.05f
			bipedRightArm.rotateAngleX += MathHelper.sin(z * 0.067f) * 0.05f
			
			bipedLeftArm.rotateAngleZ = 0f
			bipedLeftArm.rotateAngleY = 0.1f - f6 * 0.6f
			bipedLeftArm.rotateAngleX = -(Math.PI.F / 2f)
			bipedLeftArm.rotateAngleX -= f6 * 1.2f - f7 * 0.4f
			bipedLeftArm.rotateAngleZ -= MathHelper.cos(z * 0.09f) * 0.05f + 0.05f
			bipedLeftArm.rotateAngleX -= MathHelper.sin(z * 0.067f) * 0.05f
			
//			if (entity is EntitySkeleton && entity.skeletonType == 1)
//				glScalef(1.2f, 1.2f, 1.2f)
//			else if (entity is EntityGiantZombie)
//				glScalef(6f, 6f, 6f)
		}
		
		if (color != -1) {
			val red = (color shr 16 and 255).F / 255f
			val blue = (color shr 8 and 255).F / 255f
			val green = (color and 255).F / 255f
			glColor3f(red, blue, green)
		}
		
		pre(entity)
		
		val f6 = 2f
		
		run {
			//partHead
			glPushMatrix()
			if (isChild) {
				glScalef(1.5f / f6, 1.5f / f6, 1.5f / f6)
				glTranslatef(0f, 16f * parTicks, 0f)
			}
			glTranslatef(bipedHead.rotationPointX * parTicks, bipedHead.rotationPointY * parTicks, bipedHead.rotationPointZ * parTicks)
			glRotatef(bipedHead.rotateAngleZ * (180f / Math.PI.F), 0f, 0f, 1f)
			glRotatef(bipedHead.rotateAngleY * (180f / Math.PI.F), 0f, 1f, 0f)
			glRotatef(bipedHead.rotateAngleX * (180f / Math.PI.F), 1f, 0f, 0f)
			glRotatef(180f, 1f, 0f, 0f)
			partHead(entity)
			glPopMatrix()
		}
		
		if (isChild) {
			glPushMatrix()
			glScalef(1f / f6, 1f / f6, 1f / f6)
			glTranslatef(0f, 24f * parTicks, 0f)
		}
		
		run {
			//partBody
			glPushMatrix()
			glTranslatef(bipedBody.rotationPointX * parTicks, bipedBody.rotationPointY * parTicks, bipedBody.rotationPointZ * parTicks)
			glRotatef(bipedBody.rotateAngleZ * (180f / Math.PI.F), 0f, 0f, 1f)
			glRotatef(bipedBody.rotateAngleY * (180f / Math.PI.F), 0f, 1f, 0f)
			glRotatef(bipedBody.rotateAngleX * (180f / Math.PI.F), 1f, 0f, 0f)
			glRotatef(180f, 1f, 0f, 0f)
			partBody(entity)
			glPopMatrix()
		}
		
		run {
			//partRightArm
			glPushMatrix()
			glTranslatef(bipedRightArm.rotationPointX * parTicks, bipedRightArm.rotationPointY * parTicks, bipedRightArm.rotationPointZ * parTicks)
			glRotatef(bipedRightArm.rotateAngleZ * (180f / Math.PI.F), 0f, 0f, 1f)
			glRotatef(bipedRightArm.rotateAngleY * (180f / Math.PI.F), 0f, 1f, 0f)
			glRotatef(bipedRightArm.rotateAngleX * (180f / Math.PI.F), 1f, 0f, 0f)
			glRotatef(180f, 1f, 0f, 0f)
			partRightArm(entity)
			glPopMatrix()
		}
		
		run {
			//partLeftArm
			glPushMatrix()
			glTranslatef(bipedLeftArm.rotationPointX * parTicks, bipedLeftArm.rotationPointY * parTicks, bipedLeftArm.rotationPointZ * parTicks)
			glRotatef(bipedLeftArm.rotateAngleZ * (180f / Math.PI.F), 0f, 0f, 1f)
			glRotatef(bipedLeftArm.rotateAngleY * (180f / Math.PI.F), 0f, 1f, 0f)
			glRotatef(bipedLeftArm.rotateAngleX * (180f / Math.PI.F), 1f, 0f, 0f)
			glRotatef(180f, 1f, 0f, 0f)
			partLeftArm(entity)
			glPopMatrix()
		}
		
		run {
			//partRightLeg
			glPushMatrix()
			glTranslatef(bipedRightLeg.rotationPointX * parTicks, bipedRightLeg.rotationPointY * parTicks, bipedRightLeg.rotationPointZ * parTicks)
			glRotatef(bipedRightLeg.rotateAngleZ * (180f / Math.PI.F), 0f, 0f, 1f)
			glRotatef(bipedRightLeg.rotateAngleY * (180f / Math.PI.F), 0f, 1f, 0f)
			glRotatef(bipedRightLeg.rotateAngleX * (180f / Math.PI.F), 1f, 0f, 0f)
			glRotatef(180f, 1f, 0f, 0f)
			partRightLeg(entity)
			glPopMatrix()
		}
		
		run {
			//partLeftLeg
			glPushMatrix()
			glTranslatef(bipedLeftLeg.rotationPointX * parTicks, bipedLeftLeg.rotationPointY * parTicks, bipedLeftLeg.rotationPointZ * parTicks)
			glRotatef(bipedLeftLeg.rotateAngleZ * (180f / Math.PI.F), 0f, 0f, 1f)
			glRotatef(bipedLeftLeg.rotateAngleY * (180f / Math.PI.F), 0f, 1f, 0f)
			glRotatef(bipedLeftLeg.rotateAngleX * (180f / Math.PI.F), 1f, 0f, 0f)
			glRotatef(180f, 1f, 0f, 0f)
			partLeftLeg(entity)
			glPopMatrix()
		}
		
		if (isChild) {
			glPopMatrix()
		}
		
		post(entity)
		
		glColor3f(1f, 1f, 1f)
		glPopMatrix()
	}
}
