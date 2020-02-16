package alfheim.client.model.entity

import alfheim.client.core.util.glScaled
import alfheim.common.core.util.*
import net.minecraft.client.model.*
import net.minecraft.entity.Entity
import net.minecraft.util.MathHelper
import org.lwjgl.opengl.GL11.*

open class ModelBipedNew: ModelBase() {
	
	val head: ModelRenderer
	val hair: ModelRenderer
	val body: ModelRenderer
	val chest: ModelRenderer
	val rightarm: ModelRenderer
	val rightglove: ModelRenderer
	val leftarm: ModelRenderer
	val leftglove: ModelRenderer
	val rightleg: ModelRenderer
	val rightboot: ModelRenderer
	val leftleg: ModelRenderer
	val leftboot: ModelRenderer
	
	init { // ModelBiped
		textureWidth = 64
		textureHeight = 64
		isChild = false
		
		head = ModelRenderer(this, 0, 0)
		head.addBox(-4f, -8.001f, -4f, 8, 8, 8)
		head.setRotationPoint(0f, 0f, 0f)
		
		hair = ModelRenderer(this, 32, 0)
		hair.addBox(-4f, -8.001f, -4f, 8, 8, 8, 0.5f)
		head.addChild(hair)
		
		body = ModelRenderer(this, 16, 16)
		body.addBox(-4f, 0f, -2f, 8, 12, 4)
		body.setRotationPoint(0f, 0f, 0f)
		
		chest = ModelRenderer(this, 16, 32)
		chest.addBox(-4f, 0f, -2f, 8, 12, 4, 0.5f)
		body.addChild(chest)
		
		rightarm = ModelRenderer(this, 40, 16)
		rightarm.addBox(-3.001f, -2f, -2f, 4, 12, 4)
		rightarm.setRotationPoint(-5f, 2f, 0f)
		
		rightglove = ModelRenderer(this, 40, 32)
		rightglove.addBox(-3.001f, -2f, -2f, 4, 12, 4, 0.5f)
		rightarm.addChild(rightglove)
		
		leftarm = ModelRenderer(this, 32, 48)
		leftarm.addBox(-1.001f, -2f, -2f, 4, 12, 4)
		leftarm.setRotationPoint(5f, 2f, 0f)
		
		leftglove = ModelRenderer(this, 48, 48)
		leftglove.addBox(-1.001f, -2f, -2f, 4, 12, 4, 0.5f)
		leftarm.addChild(leftglove)
		
		rightleg = ModelRenderer(this, 0, 16)
		rightleg.addBox(-2f, 0.001f, -2f, 4, 12, 4)
		rightleg.setRotationPoint(-2f, 12f, 0f)
		
		rightboot = ModelRenderer(this, 0, 32)
		rightboot.addBox(-2f, 0.001f, -2f, 4, 12, 4, 0.5f)
		rightleg.addChild(rightboot)
		
		leftleg = ModelRenderer(this, 16, 48)
		leftleg.addBox(-2f, 0.001f, -2f, 4, 12, 4)
		leftleg.setRotationPoint(2f, 12f, 0f)
		
		leftboot = ModelRenderer(this, 0, 48)
		leftboot.addBox(-2f, 0.001f, -2f, 4, 12, 4, 0.5f)
		leftleg.addChild(leftboot)
	}
	
	override fun render(entity: Entity, time: Float, amplitude: Float, ticksExisted: Float, yawHead: Float, pitchHead: Float, size: Float) {
		setRotationAngles(time, amplitude, ticksExisted, yawHead, pitchHead, size, entity)
		
		if (isChild) {
			glPushMatrix()
			glScaled(0.5)
			glTranslatef(0f, 24f * size, 0f)
		}
		
		render(size)
		if (isChild) glPopMatrix()
	}
	
	fun render(size: Float) {
		glDisable(GL_CULL_FACE)
		head.render(size)
		body.render(size)
		rightarm.render(size)
		leftarm.render(size)
		rightleg.render(size)
		leftleg.render(size)
		glEnable(GL_CULL_FACE)
	}
	
	override fun setRotationAngles(limbSwing: Float, limbAmpl: Float, ticksExisted: Float, yawHead: Float, pitchHead: Float, size: Float, entity: Entity?) {
		head.rotateAngleY = yawHead / (180f / Math.PI.F)
		head.rotateAngleX = pitchHead / (180f / Math.PI.F)
		rightarm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + Math.PI.F) * 2f * limbAmpl * 0.5f
		leftarm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 2f * limbAmpl * 0.5f
		rightarm.rotateAngleZ = 0f
		leftarm.rotateAngleZ = 0f
		rightleg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 1.4f * limbAmpl
		leftleg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + Math.PI.F) * 1.4f * limbAmpl
		rightleg.rotateAngleY = 0f
		leftleg.rotateAngleY = 0f
		
		if (entity?.isRiding == true) {
			rightarm.rotateAngleX += -(Math.PI.F / 5f)
			leftarm.rotateAngleX += -(Math.PI.F / 5f)
			rightleg.rotateAngleX = -(Math.PI.F * 2f / 5f)
			leftleg.rotateAngleX = -(Math.PI.F * 2f / 5f)
			rightleg.rotateAngleY = Math.PI.F / 10f
			leftleg.rotateAngleY = -(Math.PI.F / 10f)
		}
		
		//if (heldItemLeft != 0) leftarm.rotateAngleX = leftarm.rotateAngleX * 0.5F - ((float)Math.PI / 10F) * (float)heldItemLeft;
		//if (heldItemRight != 0) rightarm.rotateAngleX = rightarm.rotateAngleX * 0.5F - ((float)Math.PI / 10F) * (float)heldItemRight;
		
		rightarm.rotateAngleY = 0f
		leftarm.rotateAngleY = 0f
		var f6: Float
		val f7: Float
		
		if (onGround > -9990f) {
			f6 = onGround
			body.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f6) * Math.PI.F * 2f) * 0.2f
			rightarm.rotationPointZ = MathHelper.sin(body.rotateAngleY) * 5f
			rightarm.rotationPointX = -MathHelper.cos(body.rotateAngleY) * 5f
			leftarm.rotationPointZ = -MathHelper.sin(body.rotateAngleY) * 5f
			leftarm.rotationPointX = MathHelper.cos(body.rotateAngleY) * 5f
			rightarm.rotateAngleY += body.rotateAngleY
			leftarm.rotateAngleY += body.rotateAngleY
			leftarm.rotateAngleX += body.rotateAngleY
			f6 = 1f - onGround
			f6 *= f6
			f6 *= f6
			f6 = 1f - f6
			f7 = MathHelper.sin(f6 * Math.PI.F)
			val f8 = MathHelper.sin(onGround * Math.PI.F) * -(head.rotateAngleX - 0.7f) * 0.75f
			rightarm.rotateAngleX = (rightarm.rotateAngleX.D - (f7.D * 1.2 + f8.D)).F
			rightarm.rotateAngleY += body.rotateAngleY * 2f
			rightarm.rotateAngleZ = MathHelper.sin(onGround * Math.PI.F) * -0.4f
		}
		
		if (entity?.isSneaking == true) {
			body.rotateAngleX = 0.5f
			rightarm.rotateAngleX += 0.4f
			leftarm.rotateAngleX += 0.4f
			rightleg.rotationPointZ = 4f
			leftleg.rotationPointZ = 4f
			rightleg.rotationPointY = 9f
			leftleg.rotationPointY = 9f
			head.rotationPointY = 1f
			hair.rotationPointY = 1f
		} else {
			body.rotateAngleX = 0f
			rightleg.rotationPointZ = 0.1f
			leftleg.rotationPointZ = 0.1f
			rightleg.rotationPointY = 12f
			leftleg.rotationPointY = 12f
			head.rotationPointY = 0f
			hair.rotationPointY = 0f
		}
		
		rightarm.rotateAngleZ += MathHelper.cos(ticksExisted * 0.09f) * 0.05f + 0.05f
		leftarm.rotateAngleZ -= MathHelper.cos(ticksExisted * 0.09f) * 0.05f + 0.05f
		rightarm.rotateAngleX += MathHelper.sin(ticksExisted * 0.067f) * 0.05f
		leftarm.rotateAngleX -= MathHelper.sin(ticksExisted * 0.067f) * 0.05f
	}
	
	companion object {
		
		val model = ModelBipedNew()
	}
}