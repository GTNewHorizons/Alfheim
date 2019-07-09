package alfheim.client.model.entity

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
		head.addBox(-4f, -8f, -4f, 8, 8, 8)
		head.setRotationPoint(0f, 0f, 0f)
		
		hair = ModelRenderer(this, 32, 0)
		hair.addBox(-4f, -8f, -4f, 8, 8, 8, 0.5f)
		head.addChild(hair)
		
		body = ModelRenderer(this, 16, 16)
		body.addBox(-4f, 0f, -2f, 8, 12, 4)
		body.setRotationPoint(0f, 0f, 0f)
		
		chest = ModelRenderer(this, 16, 32)
		chest.addBox(-4f, 0f, -2f, 8, 12, 4, 0.5f)
		body.addChild(chest)
		
		rightarm = ModelRenderer(this, 40, 16)
		rightarm.addBox(-3f, -2f, -2f, 4, 12, 4)
		rightarm.setRotationPoint(-5f, 2f, 0f)
		
		rightglove = ModelRenderer(this, 40, 32)
		rightglove.addBox(-3f, -2f, -2f, 4, 12, 4, 0.5f)
		rightarm.addChild(rightglove)
		
		leftarm = ModelRenderer(this, 32, 48)
		leftarm.addBox(-1f, -2f, -2f, 4, 12, 4)
		leftarm.setRotationPoint(5f, 2f, 0f)
		
		leftglove = ModelRenderer(this, 48, 48)
		leftglove.addBox(-1f, -2f, -2f, 4, 12, 4, 0.5f)
		leftarm.addChild(leftglove)
		
		rightleg = ModelRenderer(this, 0, 16)
		rightleg.addBox(-2f, 0f, -2f, 4, 12, 4)
		rightleg.setRotationPoint(-2f, 12f, 0f)
		
		rightboot = ModelRenderer(this, 0, 32)
		rightboot.addBox(-2f, 0f, -2f, 4, 12, 4, 0.5f)
		rightleg.addChild(rightboot)
		
		leftleg = ModelRenderer(this, 16, 48)
		leftleg.addBox(-2f, 0f, -2f, 4, 12, 4)
		leftleg.setRotationPoint(2f, 12f, 0f)
		
		leftboot = ModelRenderer(this, 0, 48)
		leftboot.addBox(-2f, 0f, -2f, 4, 12, 4, 0.5f)
		leftleg.addChild(leftboot)
	}
	
	override fun render(entity: Entity?, time: Float, amplitude: Float, ticksExisted: Float, yawHead: Float, pitchHead: Float, size: Float) {
		setRotationAngles(time, amplitude, ticksExisted, yawHead, pitchHead, size, entity)
		
		if (isChild) {
			glPushMatrix()
			glScalef(0.5f, 0.5f, 0.5f)
			glTranslatef(0.0f, 24.0f * size, 0.0f)
		}
		
		render(size)
		if (isChild) glPopMatrix()
	}
	
	fun render(size: Float) {
		head.render(size)
		body.render(size)
		rightarm.render(size)
		leftarm.render(size)
		rightleg.render(size)
		leftleg.render(size)
	}
	
	override fun setRotationAngles(limbSwing: Float, limbAmpl: Float, ticksExisted: Float, yawHead: Float, pitchHead: Float, size: Float, entity: Entity?) {
		head.rotateAngleY = yawHead / (180f / Math.PI.toFloat())
		head.rotateAngleX = pitchHead / (180f / Math.PI.toFloat())
		rightarm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + Math.PI.toFloat()) * 2.0f * limbAmpl * 0.5f
		leftarm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 2.0f * limbAmpl * 0.5f
		rightarm.rotateAngleZ = 0.0f
		leftarm.rotateAngleZ = 0.0f
		rightleg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 1.4f * limbAmpl
		leftleg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + Math.PI.toFloat()) * 1.4f * limbAmpl
		rightleg.rotateAngleY = 0.0f
		leftleg.rotateAngleY = 0.0f
		
		if (entity?.isRiding == true) {
			rightarm.rotateAngleX += -(Math.PI.toFloat() / 5f)
			leftarm.rotateAngleX += -(Math.PI.toFloat() / 5f)
			rightleg.rotateAngleX = -(Math.PI.toFloat() * 2f / 5f)
			leftleg.rotateAngleX = -(Math.PI.toFloat() * 2f / 5f)
			rightleg.rotateAngleY = Math.PI.toFloat() / 10f
			leftleg.rotateAngleY = -(Math.PI.toFloat() / 10f)
		}
		
		//if (heldItemLeft != 0) leftarm.rotateAngleX = leftarm.rotateAngleX * 0.5F - ((float)Math.PI / 10F) * (float)heldItemLeft;
		//if (heldItemRight != 0) rightarm.rotateAngleX = rightarm.rotateAngleX * 0.5F - ((float)Math.PI / 10F) * (float)heldItemRight;
		
		rightarm.rotateAngleY = 0.0f
		leftarm.rotateAngleY = 0.0f
		var f6: Float
		val f7: Float
		
		if (onGround > -9990.0f) {
			f6 = onGround
			body.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f6) * Math.PI.toFloat() * 2.0f) * 0.2f
			rightarm.rotationPointZ = MathHelper.sin(body.rotateAngleY) * 5.0f
			rightarm.rotationPointX = -MathHelper.cos(body.rotateAngleY) * 5.0f
			leftarm.rotationPointZ = -MathHelper.sin(body.rotateAngleY) * 5.0f
			leftarm.rotationPointX = MathHelper.cos(body.rotateAngleY) * 5.0f
			rightarm.rotateAngleY += body.rotateAngleY
			leftarm.rotateAngleY += body.rotateAngleY
			leftarm.rotateAngleX += body.rotateAngleY
			f6 = 1.0f - onGround
			f6 *= f6
			f6 *= f6
			f6 = 1.0f - f6
			f7 = MathHelper.sin(f6 * Math.PI.toFloat())
			val f8 = MathHelper.sin(onGround * Math.PI.toFloat()) * -(head.rotateAngleX - 0.7f) * 0.75f
			rightarm.rotateAngleX = (rightarm.rotateAngleX.toDouble() - (f7.toDouble() * 1.2 + f8.toDouble())).toFloat()
			rightarm.rotateAngleY += body.rotateAngleY * 2.0f
			rightarm.rotateAngleZ = MathHelper.sin(onGround * Math.PI.toFloat()) * -0.4f
		}
		
		if (entity?.isSneaking == true) {
			body.rotateAngleX = 0.5f
			rightarm.rotateAngleX += 0.4f
			leftarm.rotateAngleX += 0.4f
			rightleg.rotationPointZ = 4.0f
			leftleg.rotationPointZ = 4.0f
			rightleg.rotationPointY = 9.0f
			leftleg.rotationPointY = 9.0f
			head.rotationPointY = 1.0f
			hair.rotationPointY = 1.0f
		} else {
			body.rotateAngleX = 0.0f
			rightleg.rotationPointZ = 0.1f
			leftleg.rotationPointZ = 0.1f
			rightleg.rotationPointY = 12.0f
			leftleg.rotationPointY = 12.0f
			head.rotationPointY = 0.0f
			hair.rotationPointY = 0.0f
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