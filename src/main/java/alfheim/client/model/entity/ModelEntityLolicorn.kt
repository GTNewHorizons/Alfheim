package alfheim.client.model.entity

import alexsocol.asjlib.ASJUtilities
import alfheim.common.entity.EntityLolicorn
import net.minecraft.client.Minecraft
import net.minecraft.client.model.*
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.*
import net.minecraft.util.MathHelper
import org.lwjgl.opengl.GL11
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara
import kotlin.math.*

/**
 * ModelEntityLolicorn - Either Mojang or a mod author
 * Created using Tabula 4.1.1
 */
class ModelEntityLolicorn: ModelBase() {
	
	var horseSaddleBack: ModelRenderer
	var tailBase: ModelRenderer
	var frontLeftHoof: ModelRenderer
	var horseLeftSaddleMetal: ModelRenderer
	var body2: ModelRenderer
	var tailMiddle: ModelRenderer
	var backRightShin: ModelRenderer
	var frontRightLeg: ModelRenderer
	var horseSaddleFront: ModelRenderer
	var backLeftShin: ModelRenderer
	var backLeftLeg: ModelRenderer
	var frontRightHoof: ModelRenderer
	var backLeftHoof: ModelRenderer
	var horseRightSaddleRope: ModelRenderer
	var backRightLeg: ModelRenderer
	var backRightHoof: ModelRenderer
	var horn: ModelRenderer
	var frontLeftLeg: ModelRenderer
	var horseSaddleBottom: ModelRenderer
	var horseLeftSaddleRope: ModelRenderer
	var tailTip: ModelRenderer
	var frontRightShin: ModelRenderer
	var horseRightSaddleMetal: ModelRenderer
	var frontLeftShin: ModelRenderer
	var body: ModelRenderer
	var chest: ModelRenderer
	var head: ModelRenderer
	var rightarm: ModelRenderer
	var leftarm: ModelRenderer
	var hair: ModelRenderer
	var rightGlove: ModelRenderer
	var leftGlove: ModelRenderer
	
	init {
		textureWidth = 128
		textureHeight = 128
		horseLeftSaddleRope = ModelRenderer(this, 70, 0)
		horseLeftSaddleRope.setRotationPoint(5.0f, 3.0f, 2.0f)
		horseLeftSaddleRope.addBox(-0.5f, 0.0f, -0.5f, 1, 6, 1, 0.0f)
		frontLeftHoof = ModelRenderer(this, 44, 51)
		frontLeftHoof.setRotationPoint(4.0f, 16.0f, -8.0f)
		frontLeftHoof.addBox(-2.4000000953674316f, 5.099999904632568f, -2.0999999046325684f, 4, 3, 4, 2.384185791015625E-7f)
		frontRightHoof = ModelRenderer(this, 60, 51)
		frontRightHoof.setRotationPoint(-4.0f, 16.0f, -8.0f)
		frontRightHoof.addBox(-1.600000023841858f, 5.099999904632568f, -2.0999999046325684f, 4, 3, 4, 2.384185791015625E-7f)
		backRightHoof = ModelRenderer(this, 96, 51)
		backRightHoof.setRotationPoint(-4.0f, 16.0f, 11.0f)
		backRightHoof.addBox(-1.5f, 5.099999904632568f, -2.0f, 4, 3, 4, 2.384185791015625E-7f)
		backLeftLeg = ModelRenderer(this, 78, 29)
		backLeftLeg.setRotationPoint(4.0f, 9.0f, 11.0f)
		backLeftLeg.addBox(-2.5f, -2.0f, -2.5f, 4, 9, 5, 0.0f)
		horseRightSaddleRope = ModelRenderer(this, 80, 0)
		horseRightSaddleRope.setRotationPoint(-5.0f, 3.0f, 2.0f)
		horseRightSaddleRope.addBox(-0.5f, 0.0f, -0.5f, 1, 6, 1, 0.0f)
		backLeftShin = ModelRenderer(this, 78, 43)
		backLeftShin.setRotationPoint(4.0f, 16.0f, 11.0f)
		backLeftShin.addBox(-2.0f, 0.0f, -1.5f, 3, 5, 3, 0.0f)
		head = ModelRenderer(this, 0, 68)
		head.setRotationPoint(0.0f, 0.0f, 0.0f)
		head.addBox(-4.0f, -28.0f, -21.0f, 8, 8, 8, 0.0f)
		rightarm = ModelRenderer(this, 64, 84)
		rightarm.setRotationPoint(-5.0f, 2.0f, 0.0f)
		rightarm.addBox(-5.0f, -22.0f, -19.0f, 4, 12, 4, 0.0f)
		tailBase = ModelRenderer(this, 44, 0)
		tailBase.setRotationPoint(0.0f, 3.0f, 14.0f)
		tailBase.addBox(-1.0f, -1.0f, 0.0f, 2, 2, 3, 0.0f)
		setRotateAngle(tailBase, -1.1344640254974365f, 0.0f, 0.0f)
		tailTip = ModelRenderer(this, 24, 3)
		tailTip.setRotationPoint(0.0f, 3.0f, 14.0f)
		tailTip.addBox(-1.5f, -4.5f, 9.0f, 3, 4, 7, 0.0f)
		setRotateAngle(tailTip, -1.40215003490448f, 0.0f, 0.0f)
		tailMiddle = ModelRenderer(this, 38, 7)
		tailMiddle.setRotationPoint(0.0f, 3.0f, 14.0f)
		tailMiddle.addBox(-1.5f, -2.0f, 3.0f, 3, 4, 7, 0.0f)
		setRotateAngle(tailMiddle, -1.1344640254974365f, 0.0f, 0.0f)
		frontLeftShin = ModelRenderer(this, 44, 41)
		frontLeftShin.setRotationPoint(4.0f, 16.0f, -8.0f)
		frontLeftShin.addBox(-1.899999976158142f, 0.0f, -1.600000023841858f, 3, 5, 3, 0.0f)
		leftarm = ModelRenderer(this, 80, 84)
		leftarm.setRotationPoint(5.0f, 2.0f, -0.0f)
		leftarm.addBox(1.0f, -22.0f, -19.0f, 4, 12, 4, 0.0f)
		rightGlove = ModelRenderer(this, 96, 84)
		rightGlove.setRotationPoint(0.0f, 0.0f, 0.0f)
		rightGlove.addBox(-3.0f, -22.0f, -19.0f, 4, 12, 4, 0.0f)
		horn = ModelRenderer(this, 0, 0)
		horn.setRotationPoint(0.0f, 0.0f, 0.0f)
		horn.addBox(0.0f, 0.0f, 0.0f, 0, 0, 0, 0.0f)
		horseRightSaddleMetal = ModelRenderer(this, 74, 4)
		horseRightSaddleMetal.setRotationPoint(-5.0f, 3.0f, 2.0f)
		horseRightSaddleMetal.addBox(-0.5f, 6.0f, -1.0f, 1, 2, 2, 0.0f)
		backRightLeg = ModelRenderer(this, 96, 29)
		backRightLeg.setRotationPoint(-4.0f, 9.0f, 11.0f)
		backRightLeg.addBox(-1.5f, -2.0f, -2.5f, 4, 9, 5, 0.0f)
		frontLeftLeg = ModelRenderer(this, 44, 29)
		frontLeftLeg.setRotationPoint(4.0f, 9.0f, -8.0f)
		frontLeftLeg.addBox(-1.899999976158142f, -1.0f, -2.0999999046325684f, 3, 8, 4, 0.0f)
		backRightShin = ModelRenderer(this, 96, 43)
		backRightShin.setRotationPoint(-4.0f, 16.0f, 11.0f)
		backRightShin.addBox(-1.0f, 0.0f, -1.5f, 3, 5, 3, 0.0f)
		body = ModelRenderer(this, 16, 84)
		body.setRotationPoint(0.0f, 0.0f, 0.0f)
		body.addBox(-4.0f, -20.0f, -19.0f, 8, 12, 4, 0.0f)
		hair = ModelRenderer(this, 32, 68)
		hair.setRotationPoint(0.0f, 0.0f, 0.0f)
		hair.addBox(-4.0f, -28.0f, -21.0f, 8, 8, 8, 0.0f)
		frontRightLeg = ModelRenderer(this, 60, 29)
		frontRightLeg.setRotationPoint(-4.0f, 9.0f, -8.0f)
		frontRightLeg.addBox(-1.100000023841858f, -1.0f, -2.0999999046325684f, 3, 8, 4, 0.0f)
		horseSaddleBottom = ModelRenderer(this, 80, 0)
		horseSaddleBottom.setRotationPoint(0.0f, 2.0f, 2.0f)
		horseSaddleBottom.addBox(-5.0f, 0.0f, -3.0f, 10, 1, 8, 0.0f)
		horseSaddleBack = ModelRenderer(this, 80, 9)
		horseSaddleBack.setRotationPoint(0.0f, 2.0f, 2.0f)
		horseSaddleBack.addBox(-4.0f, -1.0f, 3.0f, 8, 1, 2, 0.0f)
		chest = ModelRenderer(this, 40, 84)
		chest.setRotationPoint(0.0f, 0.0f, 0.0f)
		chest.addBox(-4.0f, -20.0f, -19.0f, 8, 12, 4, 0.0f)
		leftGlove = ModelRenderer(this, 112, 84)
		leftGlove.setRotationPoint(0.0f, 0.0f, 0.0f)
		leftGlove.addBox(-1.0f, -22.0f, -19.0f, 4, 12, 4, 0.0f)
		horseSaddleFront = ModelRenderer(this, 106, 9)
		horseSaddleFront.setRotationPoint(0.0f, 2.0f, 2.0f)
		horseSaddleFront.addBox(-1.5f, -1.0f, -3.0f, 3, 1, 2, 0.0f)
		backLeftHoof = ModelRenderer(this, 78, 51)
		backLeftHoof.setRotationPoint(4.0f, 16.0f, 11.0f)
		backLeftHoof.addBox(-2.5f, 5.099999904632568f, -2.0f, 4, 3, 4, 2.384185791015625E-7f)
		frontRightShin = ModelRenderer(this, 60, 41)
		frontRightShin.setRotationPoint(-4.0f, 16.0f, -8.0f)
		frontRightShin.addBox(-1.100000023841858f, 0.0f, -1.600000023841858f, 3, 5, 3, 0.0f)
		horseLeftSaddleMetal = ModelRenderer(this, 74, 0)
		horseLeftSaddleMetal.setRotationPoint(5.0f, 3.0f, 2.0f)
		horseLeftSaddleMetal.addBox(-0.5f, 6.0f, -1.0f, 1, 2, 2, 0.0f)
		body2 = ModelRenderer(this, 0, 34)
		body2.setRotationPoint(0.0f, 11.0f, 9.0f)
		body2.addBox(-5.0f, -8.0f, -19.0f, 10, 10, 24, 0.0f)
		body.addChild(head)
		body.addChild(rightarm)
		body.addChild(leftarm)
		rightarm.addChild(rightGlove)
		body2.addChild(body)
		head.addChild(hair)
		body.addChild(chest)
		leftarm.addChild(leftGlove)
	}
	
	override fun render(entity: Entity?, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
		setRotationAngles(f, f1, f2, f3, f4, f5, entity)
		
		horseLeftSaddleRope.render(f5)
		frontLeftHoof.render(f5)
		frontRightHoof.render(f5)
		backRightHoof.render(f5)
		backLeftLeg.render(f5)
		horseRightSaddleRope.render(f5)
		backLeftShin.render(f5)
		tailBase.render(f5)
		tailTip.render(f5)
		tailMiddle.render(f5)
		frontLeftShin.render(f5)
		horn.render(f5)
		horseRightSaddleMetal.render(f5)
		backRightLeg.render(f5)
		frontLeftLeg.render(f5)
		backRightShin.render(f5)
		frontRightLeg.render(f5)
		horseSaddleBottom.render(f5)
		horseSaddleBack.render(f5)
		horseSaddleFront.render(f5)
		backLeftHoof.render(f5)
		frontRightShin.render(f5)
		horseLeftSaddleMetal.render(f5)
		body2.render(f5)
	}
	
	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	fun setRotateAngle(modelRenderer: ModelRenderer, x: Float, y: Float, z: Float) {
		modelRenderer.rotateAngleX = x
		modelRenderer.rotateAngleY = y
		modelRenderer.rotateAngleZ = z
	}
	
	private fun updateHorseRotation(prevYaw: Float, yaw: Float, ticks: Float): Float {
		var f3 = yaw - prevYaw
		
		while (f3 < -180f) {
			f3 += 360f
		}
		
		while (f3 >= 180f) {
			f3 -= 360f
		}
		
		return prevYaw + f3 * ticks
	}
	
	override fun setRotationAngles(limbSwing: Float, limbAmpl: Float, ticksExisted: Float, yawHead: Float, pitchHead: Float, size: Float, entity: Entity?) {
		head.rotateAngleY = yawHead / (180f / Math.PI.toFloat())
		head.rotateAngleX = pitchHead / (180f / Math.PI.toFloat())
		rightarm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + Math.PI.toFloat()) * 2.0f * limbAmpl * 0.5f
		leftarm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 2.0f * limbAmpl * 0.5f
		rightarm.rotateAngleZ = 0.0f
		leftarm.rotateAngleZ = 0.0f
		
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
		
		body.rotateAngleX = 0.0f
		head.rotationPointY = 0.0f
		hair.rotationPointY = 0.0f
		
		rightarm.rotateAngleZ += MathHelper.cos(ticksExisted * 0.09f) * 0.05f + 0.05f
		leftarm.rotateAngleZ -= MathHelper.cos(ticksExisted * 0.09f) * 0.05f + 0.05f
		rightarm.rotateAngleX += MathHelper.sin(ticksExisted * 0.067f) * 0.05f
		leftarm.rotateAngleX -= MathHelper.sin(ticksExisted * 0.067f) * 0.05f
	}
	
	/**
	 * Used for easily adding entity-dependent animations. The second and third float params here are the same second
	 * and third as in the setRotationAngles method.
	 */
	override fun setLivingAnimations(entity: EntityLivingBase?, limb: Float, prevLimb: Float, ticks: Float) {
		super.setLivingAnimations(entity, limb, prevLimb, ticks)
		val lolicorn = entity as EntityLolicorn
		
		val f5 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * ticks
		var f7 = f5 / (180f / Math.PI.toFloat())
		if (prevLimb > 0.2f) f7 += MathHelper.cos(limb * 0.4f) * 0.15f * prevLimb

//		val f9 = abs(sin(entity.worldObj.totalWorldTime / 40f))
		val f9 = ASJUtilities.interpolate(lolicorn.prevRearingAmount.toDouble(), lolicorn.rearingAmount.toDouble()).toFloat()
		val f10 = 1f - f9
//		val f11 = ASJUtilities.interpolate(lolicorn.prevMouthOpenness.toDouble(), lolicorn.mouthOpenness.toDouble()).toFloat()
		val flag = lolicorn.tailMovement != 0
		val flag2 = lolicorn.riddenByEntity != null
		val f12 = entity.ticksExisted.toFloat() + ticks
		val f13 = MathHelper.cos(limb * 0.6662f + Math.PI.toFloat())
		val f14 = f13 * 0.8f * prevLimb
		tailBase.rotationPointY = 3f
		tailMiddle.rotationPointZ = 14f
		body2.rotateAngleX = 0f
		tailBase.rotationPointY = f9 * 9f + f10 * tailBase.rotationPointY
		tailMiddle.rotationPointZ = f9 * 18f + f10 * tailMiddle.rotationPointZ
		body2.rotateAngleX = f9 * -(Math.PI.toFloat() / 4f) + f10 * body2.rotateAngleX
		val f18 = 0.2617994f * f9
		val f19 = MathHelper.cos(f12 * 0.6f + Math.PI.toFloat())
		frontLeftLeg.rotationPointY = -2f * f9 + 9f * f10
		frontLeftLeg.rotationPointZ = -2f * f9 + -8f * f10
		frontRightLeg.rotationPointY = frontLeftLeg.rotationPointY
		frontRightLeg.rotationPointZ = frontLeftLeg.rotationPointZ
		backLeftShin.rotationPointY = backLeftLeg.rotationPointY + MathHelper.sin(Math.PI.toFloat() / 2f + f18 + f10 * -f13 * 0.5f * prevLimb) * 7f
		backLeftShin.rotationPointZ = backLeftLeg.rotationPointZ + MathHelper.cos(Math.PI.toFloat() * 3f / 2f + f18 + f10 * -f13 * 0.5f * prevLimb) * 7f
		backRightShin.rotationPointY = backRightLeg.rotationPointY + MathHelper.sin(Math.PI.toFloat() / 2f + f18 + f10 * f13 * 0.5f * prevLimb) * 7f
		backRightShin.rotationPointZ = backRightLeg.rotationPointZ + MathHelper.cos(Math.PI.toFloat() * 3f / 2f + f18 + f10 * f13 * 0.5f * prevLimb) * 7f
		val f20 = (-1.0471976f + f19) * f9 + f14 * f10
		val f21 = (-1.0471976f + -f19) * f9 + -f14 * f10
		frontLeftShin.rotationPointY = frontLeftLeg.rotationPointY + MathHelper.sin(Math.PI.toFloat() / 2f + f20) * 7f
		frontLeftShin.rotationPointZ = frontLeftLeg.rotationPointZ + MathHelper.cos(Math.PI.toFloat() * 3f / 2f + f20) * 7f
		frontRightShin.rotationPointY = frontRightLeg.rotationPointY + MathHelper.sin(Math.PI.toFloat() / 2f + f21) * 7f
		frontRightShin.rotationPointZ = frontRightLeg.rotationPointZ + MathHelper.cos(Math.PI.toFloat() * 3f / 2f + f21) * 7f
		backLeftLeg.rotateAngleX = f18 + -f13 * 0.5f * prevLimb * f10
		backLeftShin.rotateAngleX = -0.08726646f * f9 + (-f13 * 0.5f * prevLimb - max(0f, f13 * 0.5f * prevLimb)) * f10
		backLeftHoof.rotateAngleX = backLeftShin.rotateAngleX
		backRightLeg.rotateAngleX = f18 + f13 * 0.5f * prevLimb * f10
		backRightShin.rotateAngleX = -0.08726646f * f9 + (f13 * 0.5f * prevLimb - max(0f, -f13 * 0.5f * prevLimb)) * f10
		backRightHoof.rotateAngleX = backRightShin.rotateAngleX
		frontLeftLeg.rotateAngleX = f20
		frontLeftShin.rotateAngleX = (frontLeftLeg.rotateAngleX + Math.PI.toFloat() * max(0f, 0.2f + f19 * 0.2f)) * f9 + (f14 + max(0f, f13 * 0.5f * prevLimb)) * f10
		frontLeftHoof.rotateAngleX = frontLeftShin.rotateAngleX
		frontRightLeg.rotateAngleX = f21
		frontRightShin.rotateAngleX = (frontRightLeg.rotateAngleX + Math.PI.toFloat() * max(0f, 0.2f - f19 * 0.2f)) * f9 + (-f14 + max(0f, -f13 * 0.5f * prevLimb)) * f10
		frontRightHoof.rotateAngleX = frontRightShin.rotateAngleX
		backLeftHoof.rotationPointY = backLeftShin.rotationPointY
		backLeftHoof.rotationPointZ = backLeftShin.rotationPointZ
		backRightHoof.rotationPointY = backRightShin.rotationPointY
		backRightHoof.rotationPointZ = backRightShin.rotationPointZ
		frontLeftHoof.rotationPointY = frontLeftShin.rotationPointY
		frontLeftHoof.rotationPointZ = frontLeftShin.rotationPointZ
		frontRightHoof.rotationPointY = frontRightShin.rotationPointY
		frontRightHoof.rotationPointZ = frontRightShin.rotationPointZ
		
		horseSaddleBottom.rotationPointY = f9 * 0.5f + f10 * 2f
		horseSaddleBottom.rotationPointZ = f9 * 11f + f10 * 2f
		horseSaddleFront.rotationPointY = horseSaddleBottom.rotationPointY
		horseSaddleBack.rotationPointY = horseSaddleBottom.rotationPointY
		horseLeftSaddleRope.rotationPointY = horseSaddleBottom.rotationPointY
		horseRightSaddleRope.rotationPointY = horseSaddleBottom.rotationPointY
		horseLeftSaddleMetal.rotationPointY = horseSaddleBottom.rotationPointY
		horseRightSaddleMetal.rotationPointY = horseSaddleBottom.rotationPointY
		horseSaddleFront.rotationPointZ = horseSaddleBottom.rotationPointZ
		horseSaddleBack.rotationPointZ = horseSaddleBottom.rotationPointZ
		horseLeftSaddleRope.rotationPointZ = horseSaddleBottom.rotationPointZ
		horseRightSaddleRope.rotationPointZ = horseSaddleBottom.rotationPointZ
		horseLeftSaddleMetal.rotationPointZ = horseSaddleBottom.rotationPointZ
		horseRightSaddleMetal.rotationPointZ = horseSaddleBottom.rotationPointZ
		horseSaddleBottom.rotateAngleX = body2.rotateAngleX
		horseSaddleFront.rotateAngleX = body2.rotateAngleX
		horseSaddleBack.rotateAngleX = body2.rotateAngleX
		
		if (flag2) {
			horseLeftSaddleRope.rotateAngleX = -1.0471976f
			horseLeftSaddleMetal.rotateAngleX = -1.0471976f
			horseRightSaddleRope.rotateAngleX = -1.0471976f
			horseRightSaddleMetal.rotateAngleX = -1.0471976f
			horseLeftSaddleRope.rotateAngleZ = 0f
			horseLeftSaddleMetal.rotateAngleZ = 0f
			horseRightSaddleRope.rotateAngleZ = 0f
			horseRightSaddleMetal.rotateAngleZ = 0f
		} else {
			horseLeftSaddleRope.rotateAngleX = f14 / 3f
			horseLeftSaddleMetal.rotateAngleX = f14 / 3f
			horseRightSaddleRope.rotateAngleX = f14 / 3f
			horseRightSaddleMetal.rotateAngleX = f14 / 3f
			horseLeftSaddleRope.rotateAngleZ = f14 / 5f
			horseLeftSaddleMetal.rotateAngleZ = f14 / 5f
			horseRightSaddleRope.rotateAngleZ = -f14 / 5f
			horseRightSaddleMetal.rotateAngleZ = -f14 / 5f
		}
		
		var f15 = -1.3089f + prevLimb * 1.5f
		
		if (f15 > 0f) {
			f15 = 0f
		}
		
		if (flag) {
			tailBase.rotateAngleY = MathHelper.cos(f12 * 0.7f)
			f15 = 0f
		} else {
			tailBase.rotateAngleY = 0f
		}
		
		tailMiddle.rotateAngleY = tailBase.rotateAngleY
		tailTip.rotateAngleY = tailBase.rotateAngleY
		tailMiddle.rotationPointY = tailBase.rotationPointY
		tailTip.rotationPointY = tailBase.rotationPointY
		tailMiddle.rotationPointZ = tailBase.rotationPointZ
		tailTip.rotationPointZ = tailBase.rotationPointZ
		tailBase.rotateAngleX = f15
		tailMiddle.rotateAngleX = f15
		tailTip.rotateAngleX = -0.2618f + f15
		
		run {
			val icon = ItemFlightTiara.wingIcons[0]
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture)
			
			val flying = !entity.onGround || f9 > 0f
			
			val rz = 120f
			var rx = ((sin((entity.ticksExisted + ticks) * if (flying) 0.4f else 0.2f) + 0.5f) * if (flying) 30f else 5f) - 20f
			val ry = if (flying) -90f else 0f
			if (!flying) rx += 90f
			val g = -0.25f
			val h = 0.4f
			val i = 0.15f
			val s = 1f
			
			GL11.glPushMatrix()
			GL11.glEnable(GL11.GL_BLEND)
			GL11.glDisable(GL11.GL_CULL_FACE)
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
			
			val f = icon.minU
			val f0 = icon.minV
			val f1 = icon.maxU
			val f2 = icon.maxV
			val sr = 1f / s
			
			val bx = (body2.rotationPointX - body2.offsetX) / 16f
			val by = (body2.rotationPointY - body2.offsetY) / 16f
			val bz = (body2.rotationPointZ - body2.offsetZ) / 16f
			
			GL11.glTranslatef(bx, by, bz)
			GL11.glRotated(Math.toDegrees(body2.rotateAngleX.toDouble()), 1.0, 0.0, 0.0)
			GL11.glTranslatef(-bx, -by, -bz)
			
			GL11.glTranslatef(g, h, i)
			
			GL11.glRotatef(rz, 0f, 0f, 1f)
			GL11.glRotatef(ry, 0f, 1f, 0f)
			GL11.glRotatef(rx, 1f, 0f, 0f)
			GL11.glScalef(s, s, s)
			ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f0, f, f2, icon.iconWidth, icon.iconHeight, 1f / 32f)
			GL11.glScalef(sr, sr, sr)
			GL11.glRotatef(-rx, 1f, 0f, 0f)
			GL11.glRotatef(-ry, 0f, 1f, 0f)
			GL11.glRotatef(-rz, 0f, 0f, 1f)
			
			GL11.glScalef(-1f, 1f, 1f)
			
			GL11.glTranslatef(g * 2, 0f, 0f)
			GL11.glRotatef(rz, 0f, 0f, 1f)
			GL11.glRotatef(ry, 0f, 1f, 0f)
			GL11.glRotatef(rx, 1f, 0f, 0f)
			GL11.glScalef(s, s, s)
			ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f0, f, f2, icon.iconWidth, icon.iconHeight, 1f / 32f)
			GL11.glScalef(sr, sr, sr)
			GL11.glRotatef(-rx, 1f, 0f, 0f)
			GL11.glRotatef(-ry, 1f, 0f, 0f)
			GL11.glRotatef(-rz, 0f, 0f, 1f)
			
			GL11.glColor4d(1.0, 1.0, 1.0, 1.0)
			GL11.glDisable(GL11.GL_BLEND)
			GL11.glEnable(GL11.GL_CULL_FACE)
			GL11.glPopMatrix()
		}
	}
}
