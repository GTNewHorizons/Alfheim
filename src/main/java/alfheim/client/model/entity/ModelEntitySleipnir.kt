package alfheim.client.model.entity

import alfheim.common.entity.EntityLolicorn
import net.minecraft.client.model.*
import net.minecraft.entity.*
import net.minecraft.util.MathHelper
import kotlin.math.max

class ModelEntitySleipnir: ModelBase() {
	
	var horseRightEar: ModelRenderer
	var horseSaddleBack: ModelRenderer
	var tailBase: ModelRenderer
	var frontLeftMiddleHoof: ModelRenderer
	var horseLeftSaddleMetal: ModelRenderer
	var tailMiddle: ModelRenderer
	var backRightMiddleShin: ModelRenderer
	var body: ModelRenderer
	var head: ModelRenderer
	var frontRightMiddleLeg: ModelRenderer
	var horseLeftEar: ModelRenderer
	var horseRightRein: ModelRenderer
	var horseSaddleFront: ModelRenderer
	var horseRightFaceMetal: ModelRenderer
	var backLeftMiddleShin: ModelRenderer
	var horseFaceRopes: ModelRenderer
	var backLeftMiddleLeg: ModelRenderer
	var frontRightMiddleHoof: ModelRenderer
	var backLeftMiddleHoof: ModelRenderer
	var horseRightSaddleRope: ModelRenderer
	var horseLeftRein: ModelRenderer
	var neck: ModelRenderer
	var horseLeftFaceMetal: ModelRenderer
	var backRightMiddleLeg: ModelRenderer
	var backRightMiddleHoof: ModelRenderer
	var frontLeftMiddleLeg: ModelRenderer
	var horseSaddleBottom: ModelRenderer
	var horseLeftSaddleRope: ModelRenderer
	var mane: ModelRenderer
	var tailTip: ModelRenderer
	var frontRightMiddleShin: ModelRenderer
	var horseRightSaddleMetal: ModelRenderer
	var frontLeftMiddleShin: ModelRenderer
	var frontRightLeg: ModelRenderer
	var frontRightShin: ModelRenderer
	var frontRightHoof: ModelRenderer
	var frontLeftLeg: ModelRenderer
	var frontLeftShin: ModelRenderer
	var frontLeftHoof: ModelRenderer
	var backRightLeg: ModelRenderer
	var backRightShin: ModelRenderer
	var backRightHoof: ModelRenderer
	var backLeftLeg: ModelRenderer
	var backLeftShin: ModelRenderer
	var backLeftHoof: ModelRenderer
	var headChild: ModelRenderer
	var headChild_1: ModelRenderer
	
	init {
		textureWidth = 128
		textureHeight = 128
		frontLeftMiddleHoof = ModelRenderer(this, 44, 51)
		frontLeftMiddleHoof.setRotationPoint(2.5f, 16.0f, -8.0f)
		frontLeftMiddleHoof.addBox(-2.4f, 5.1f, -2.1f, 4, 3, 4, 0.0f)
		backRightHoof = ModelRenderer(this, 96, 51)
		backRightHoof.setRotationPoint(-6.0f, 16.0f, 9.0f)
		backRightHoof.addBox(-1.5f, 5.1f, -2.0f, 4, 3, 4, 0.0f)
		horseLeftEar = ModelRenderer(this, 0, 0)
		horseLeftEar.setRotationPoint(0.0f, 4.0f, -10.0f)
		horseLeftEar.addBox(0.44999998807907104f, -12.0f, 4.0f, 2, 3, 1, 0.0f)
		setRotateAngle(horseLeftEar, 0.5235987901687622f, 0.0f, 0.0f)
		horseSaddleBottom = ModelRenderer(this, 80, 0)
		horseSaddleBottom.setRotationPoint(0.0f, 2.0f, 2.0f)
		horseSaddleBottom.addBox(-5.0f, 0.0f, -3.0f, 10, 1, 8, 0.0f)
		frontLeftMiddleShin = ModelRenderer(this, 44, 41)
		frontLeftMiddleShin.setRotationPoint(2.5f, 16.0f, -8.0f)
		frontLeftMiddleShin.addBox(-1.9f, 0.0f, -1.6f, 3, 5, 3, 0.0f)
		backRightMiddleLeg = ModelRenderer(this, 96, 29)
		backRightMiddleLeg.setRotationPoint(-2.5f, 9.0f, 11.0f)
		backRightMiddleLeg.addBox(-1.5f, -2.0f, -2.5f, 4, 9, 5, 0.0f)
		tailBase = ModelRenderer(this, 44, 0)
		tailBase.setRotationPoint(0.0f, 3.0f, 14.0f)
		tailBase.addBox(-1.0f, -1.0f, 0.0f, 2, 2, 3, 0.0f)
		setRotateAngle(tailBase, -1.308899998664856f, 0.0f, 0.0f)
		frontLeftLeg = ModelRenderer(this, 44, 29)
		frontLeftLeg.setRotationPoint(6.0f, 9.0f, -6.0f)
		frontLeftLeg.addBox(-1.9f, -1.0f, -2.1f, 3, 8, 4, 0.0f)
		frontRightMiddleShin = ModelRenderer(this, 60, 41)
		frontRightMiddleShin.setRotationPoint(-2.5f, 16.0f, -8.0f)
		frontRightMiddleShin.addBox(-1.1f, 0.0f, -1.6f, 3, 5, 3, 0.0f)
		tailMiddle = ModelRenderer(this, 38, 7)
		tailMiddle.setRotationPoint(0.0f, 3.0f, 14.0f)
		tailMiddle.addBox(-1.5f, -2.0f, 3.0f, 3, 4, 7, 0.0f)
		setRotateAngle(tailMiddle, -1.308899998664856f, 0.0f, 0.0f)
		head = ModelRenderer(this, 0, 0)
		head.setRotationPoint(0.0f, 4.0f, -10.0f)
		head.addBox(-2.5f, -10.0f, -1.5f, 5, 5, 7, 0.0f)
		setRotateAngle(head, 0.5235987901687622f, 0.0f, 0.0f)
		frontRightMiddleLeg = ModelRenderer(this, 60, 29)
		frontRightMiddleLeg.setRotationPoint(-2.5f, 9.0f, -8.0f)
		frontRightMiddleLeg.addBox(-1.1f, -1.0f, -2.1f, 3, 8, 4, 0.0f)
		horseRightRein = ModelRenderer(this, 44, 5)
		horseRightRein.setRotationPoint(0.0f, 4.0f, -10.0f)
		horseRightRein.addBox(-2.5999999046325684f, -6.0f, -6.0f, 0, 3, 16, 0.0f)
		headChild_1 = ModelRenderer(this, 24, 27)
		headChild_1.setRotationPoint(0.0f, 0.0f, 0.0f)
		headChild_1.addBox(-2.0f, -7.0f, -6.5f, 4, 2, 5, 0.0f)
		mane = ModelRenderer(this, 58, 0)
		mane.setRotationPoint(0.0f, 4.0f, -10.0f)
		mane.addBox(-1.0f, -11.5f, 5.0f, 2, 16, 4, 0.0f)
		setRotateAngle(mane, 0.5235987901687622f, 0.0f, 0.0f)
		headChild = ModelRenderer(this, 24, 18)
		headChild.setRotationPoint(0.0f, 0.019999999552965164f, 0.019999999552965164f)
		headChild.addBox(-2.0f, -10.0f, -7.0f, 4, 3, 6, 0.0f)
		horseRightFaceMetal = ModelRenderer(this, 74, 13)
		horseRightFaceMetal.setRotationPoint(0.0f, 4.0f, -10.0f)
		horseRightFaceMetal.addBox(-2.5f, -8.0f, -4.0f, 1, 2, 2, 0.0f)
		setRotateAngle(horseRightFaceMetal, 0.5235987901687622f, 0.0f, 0.0f)
		backRightMiddleHoof = ModelRenderer(this, 96, 51)
		backRightMiddleHoof.setRotationPoint(-2.5f, 16.0f, 11.0f)
		backRightMiddleHoof.addBox(-1.5f, 5.1f, -2.0f, 4, 3, 4, 0.0f)
		horseLeftRein = ModelRenderer(this, 44, 10)
		horseLeftRein.setRotationPoint(0.0f, 4.0f, -10.0f)
		horseLeftRein.addBox(2.5999999046325684f, -6.0f, -6.0f, 0, 3, 16, 0.0f)
		backLeftLeg = ModelRenderer(this, 78, 29)
		backLeftLeg.setRotationPoint(6.0f, 9.0f, 9.0f)
		backLeftLeg.addBox(-2.5f, -2.0f, -2.5f, 4, 9, 5, 0.0f)
		backRightShin = ModelRenderer(this, 96, 43)
		backRightShin.setRotationPoint(-6.0f, 16.0f, 9.0f)
		backRightShin.addBox(-1.0f, 0.0f, -1.5f, 3, 5, 3, 0.0f)
		horseSaddleBack = ModelRenderer(this, 80, 9)
		horseSaddleBack.setRotationPoint(0.0f, 2.0f, 2.0f)
		horseSaddleBack.addBox(-4.0f, -1.0f, 3.0f, 8, 1, 2, 0.0f)
		horseLeftSaddleMetal = ModelRenderer(this, 74, 0)
		horseLeftSaddleMetal.setRotationPoint(5.0f, 3.0f, 2.0f)
		horseLeftSaddleMetal.addBox(-0.5f, 6.0f, -1.0f, 1, 2, 2, 0.0f)
		backLeftMiddleHoof = ModelRenderer(this, 78, 51)
		backLeftMiddleHoof.setRotationPoint(2.5f, 16.0f, 11.0f)
		backLeftMiddleHoof.addBox(-2.5f, 5.1f, -2.0f, 4, 3, 4, 0.0f)
		horseLeftFaceMetal = ModelRenderer(this, 74, 13)
		horseLeftFaceMetal.setRotationPoint(0.0f, 4.0f, -10.0f)
		horseLeftFaceMetal.addBox(1.5f, -8.0f, -4.0f, 1, 2, 2, 0.0f)
		setRotateAngle(horseLeftFaceMetal, 0.5235987901687622f, 0.0f, 0.0f)
		horseFaceRopes = ModelRenderer(this, 80, 12)
		horseFaceRopes.setRotationPoint(0.0f, 4.0f, -10.0f)
		horseFaceRopes.addBox(-2.5f, -10.100000381469727f, -7.0f, 5, 5, 12, 0.19999980926513672f)
		setRotateAngle(horseFaceRopes, 0.5235987901687622f, 0.0f, 0.0f)
		horseLeftSaddleRope = ModelRenderer(this, 70, 0)
		horseLeftSaddleRope.setRotationPoint(5.0f, 3.0f, 2.0f)
		horseLeftSaddleRope.addBox(-0.5f, 0.0f, -0.5f, 1, 6, 1, 0.0f)
		horseRightSaddleRope = ModelRenderer(this, 80, 0)
		horseRightSaddleRope.setRotationPoint(-5.0f, 3.0f, 2.0f)
		horseRightSaddleRope.addBox(-0.5f, 0.0f, -0.5f, 1, 6, 1, 0.0f)
		horseRightSaddleMetal = ModelRenderer(this, 74, 4)
		horseRightSaddleMetal.setRotationPoint(-5.0f, 3.0f, 2.0f)
		horseRightSaddleMetal.addBox(-0.5f, 6.0f, -1.0f, 1, 2, 2, 0.0f)
		frontRightShin = ModelRenderer(this, 60, 41)
		frontRightShin.setRotationPoint(-6.0f, 16.0f, -6.0f)
		frontRightShin.addBox(-1.1f, 0.0f, -1.6f, 3, 5, 3, 0.0f)
		backLeftMiddleLeg = ModelRenderer(this, 78, 29)
		backLeftMiddleLeg.setRotationPoint(2.5f, 9.0f, 11.0f)
		backLeftMiddleLeg.addBox(-2.5f, -2.0f, -2.5f, 4, 9, 5, 0.0f)
		horseSaddleFront = ModelRenderer(this, 106, 9)
		horseSaddleFront.setRotationPoint(0.0f, 2.0f, 2.0f)
		horseSaddleFront.addBox(-1.5f, -1.0f, -3.0f, 3, 1, 2, 0.0f)
		frontLeftShin = ModelRenderer(this, 44, 41)
		frontLeftShin.setRotationPoint(6.0f, 16.0f, -6.0f)
		frontLeftShin.addBox(-1.9f, 0.0f, -1.6f, 3, 5, 3, 0.0f)
		backLeftHoof = ModelRenderer(this, 78, 51)
		backLeftHoof.setRotationPoint(6.0f, 16.0f, 9.0f)
		backLeftHoof.addBox(-2.5f, 5.1f, -2.0f, 4, 3, 4, 0.0f)
		frontLeftHoof = ModelRenderer(this, 44, 51)
		frontLeftHoof.setRotationPoint(6.0f, 16.0f, -6.0f)
		frontLeftHoof.addBox(-2.4f, 5.1f, -2.1f, 4, 3, 4, 0.0f)
		frontRightHoof = ModelRenderer(this, 60, 51)
		frontRightHoof.setRotationPoint(-6.0f, 16.0f, -6.0f)
		frontRightHoof.addBox(-1.6f, 5.1f, -2.1f, 4, 3, 4, 0.0f)
		backLeftShin = ModelRenderer(this, 78, 43)
		backLeftShin.setRotationPoint(6.0f, 16.0f, 9.0f)
		backLeftShin.addBox(-2.0f, 0.0f, -1.5f, 3, 5, 3, 0.0f)
		backLeftMiddleShin = ModelRenderer(this, 78, 43)
		backLeftMiddleShin.setRotationPoint(2.5f, 16.0f, 11.0f)
		backLeftMiddleShin.addBox(-2.0f, 0.0f, -1.5f, 3, 5, 3, 0.0f)
		horseRightEar = ModelRenderer(this, 0, 0)
		horseRightEar.setRotationPoint(0.0f, 4.0f, -10.0f)
		horseRightEar.addBox(-2.450000047683716f, -12.0f, 4.0f, 2, 3, 1, 0.0f)
		setRotateAngle(horseRightEar, 0.5235987901687622f, 0.0f, 0.0f)
		frontRightLeg = ModelRenderer(this, 60, 29)
		frontRightLeg.setRotationPoint(-6.0f, 9.0f, -6.0f)
		frontRightLeg.addBox(-1.1f, -1.0f, -2.1f, 3, 8, 4, 0.0f)
		body = ModelRenderer(this, 0, 34)
		body.setRotationPoint(0.0f, 11.0f, 9.0f)
		body.addBox(-5.0f, -8.0f, -19.0f, 10, 10, 24, 0.0f)
		frontLeftMiddleLeg = ModelRenderer(this, 44, 29)
		frontLeftMiddleLeg.setRotationPoint(2.5f, 9.0f, -8.0f)
		frontLeftMiddleLeg.addBox(-1.9f, -1.0f, -2.1f, 3, 8, 4, 0.0f)
		backRightLeg = ModelRenderer(this, 96, 29)
		backRightLeg.setRotationPoint(-6.0f, 9.0f, 9.0f)
		backRightLeg.addBox(-1.5f, -2.0f, -2.5f, 4, 9, 5, 0.0f)
		backRightMiddleShin = ModelRenderer(this, 96, 43)
		backRightMiddleShin.setRotationPoint(-2.5f, 16.0f, 11.0f)
		backRightMiddleShin.addBox(-1.0f, 0.0f, -1.5f, 3, 5, 3, 0.0f)
		tailTip = ModelRenderer(this, 24, 3)
		tailTip.setRotationPoint(0.0f, 3.0f, 14.0f)
		tailTip.addBox(-1.5f, -4.5f, 9.0f, 3, 4, 7, 0.0f)
		setRotateAngle(tailTip, -1.57069993019104f, 0.0f, 0.0f)
		frontRightMiddleHoof = ModelRenderer(this, 60, 51)
		frontRightMiddleHoof.setRotationPoint(-2.5f, 16.0f, -8.0f)
		frontRightMiddleHoof.addBox(-1.6f, 5.1f, -2.1f, 4, 3, 4, 0.0f)
		neck = ModelRenderer(this, 0, 12)
		neck.setRotationPoint(0.0f, 4.0f, -10.0f)
		neck.addBox(-2.049999952316284f, -9.800000190734863f, -2.0f, 4, 14, 8, 0.0f)
		setRotateAngle(neck, 0.5235987901687622f, 0.0f, 0.0f)
		head.addChild(headChild_1)
		head.addChild(headChild)
	}
	
	override fun render(entity: Entity, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
		if (entity.riddenByEntity != null) {
			horseLeftRein.render(f5)
			horseRightRein.render(f5)
		}
		
		frontLeftMiddleHoof.render(f5)
		backRightHoof.render(f5)
		horseLeftEar.render(f5)
		horseSaddleBottom.render(f5)
		frontLeftMiddleShin.render(f5)
		backRightMiddleLeg.render(f5)
		tailBase.render(f5)
		frontLeftLeg.render(f5)
		frontRightMiddleShin.render(f5)
		tailMiddle.render(f5)
		head.render(f5)
		frontRightMiddleLeg.render(f5)
		mane.render(f5)
		horseRightFaceMetal.render(f5)
		backRightMiddleHoof.render(f5)
		backLeftLeg.render(f5)
		backRightShin.render(f5)
		horseSaddleBack.render(f5)
		horseLeftSaddleMetal.render(f5)
		backLeftMiddleHoof.render(f5)
		horseLeftFaceMetal.render(f5)
		horseFaceRopes.render(f5)
		horseLeftSaddleRope.render(f5)
		horseRightSaddleRope.render(f5)
		horseRightSaddleMetal.render(f5)
		frontRightShin.render(f5)
		backLeftMiddleLeg.render(f5)
		horseSaddleFront.render(f5)
		frontLeftShin.render(f5)
		backLeftHoof.render(f5)
		frontLeftHoof.render(f5)
		frontRightHoof.render(f5)
		backLeftShin.render(f5)
		backLeftMiddleShin.render(f5)
		horseRightEar.render(f5)
		frontRightLeg.render(f5)
		body.render(f5)
		frontLeftMiddleLeg.render(f5)
		backRightLeg.render(f5)
		backRightMiddleShin.render(f5)
		tailTip.render(f5)
		frontRightMiddleHoof.render(f5)
		neck.render(f5)
	}
	
	fun setRotateAngle(modelRenderer: ModelRenderer, x: Float, y: Float, z: Float) {
		modelRenderer.rotateAngleX = x
		modelRenderer.rotateAngleY = y
		modelRenderer.rotateAngleZ = z
	}
	
	private fun updateHorseRotation(prev: Float, current: Float, ticks: Float): Float {
		var f3 = current - prev
		while (f3 < -180.0f) f3 += 360.0f
		while (f3 >= 180.0f) f3 -= 360.0f
		return prev + ticks * f3
	}
	
	override fun setLivingAnimations(living: EntityLivingBase, hui: Float, knows: Float, ticks: Float) {
		super.setLivingAnimations(living, hui, knows, ticks)
		val f3 = updateHorseRotation(living.prevRenderYawOffset, living.renderYawOffset, ticks)
		val f4 = updateHorseRotation(living.prevRotationYawHead, living.rotationYawHead, ticks)
		val f5 = living.prevRotationPitch + (living.rotationPitch - living.prevRotationPitch) * ticks
		var f6 = f4 - f3
		var f7 = f5 / (180f / Math.PI.toFloat())
		if (f6 > 20.0f) {
			f6 = 20.0f
		}
		if (f6 < -20.0f) {
			f6 = -20.0f
		}
		if (knows > 0.2f) {
			f7 += MathHelper.cos(hui * 0.4f) * 0.15f * knows
		}
		val entityhorse = living as EntityLolicorn
		val flag2 = entityhorse.riddenByEntity != null
		val f12 = living.ticksExisted.toFloat() + ticks
		val f13 = MathHelper.cos(hui * 0.6662f + Math.PI.toFloat())
		val f14 = f13 * 0.8f * knows
		head.rotationPointY = 4.0f
		head.rotationPointZ = -10.0f
		tailBase.rotationPointY = 3.0f
		tailMiddle.rotationPointZ = 14.0f
		body.rotateAngleX = 0.0f
		head.rotateAngleX = 0.5235988f + f7
		head.rotateAngleY = f6 / (180f / Math.PI.toFloat())
		head.rotateAngleX = 0f * (0.2617994f + f7) + 0f * 2.18166f + (1.0f - max(0f, 0f)) * head.rotateAngleX
		head.rotateAngleY = 0f * (f6 / (180f / Math.PI.toFloat())) + (1.0f - max(0f, 0f)) * head.rotateAngleY
		head.rotationPointY = 0f * -6.0f + 0f * 11.0f + (1.0f - max(0f, 0f)) * head.rotationPointY
		head.rotationPointZ = 0f * -1.0f + 0f * -10.0f + (1.0f - max(0f, 0f)) * head.rotationPointZ
		tailBase.rotationPointY = 0f * 9.0f + 1f * tailBase.rotationPointY
		tailMiddle.rotationPointZ = 0f * 18.0f + 1f * tailMiddle.rotationPointZ
		body.rotateAngleX = 0f * -(Math.PI.toFloat() / 4f) + 1f * body.rotateAngleX
		horseLeftEar.rotationPointY = head.rotationPointY
		horseRightEar.rotationPointY = head.rotationPointY
		neck.rotationPointY = head.rotationPointY
//		this.mouthTop.rotationPointY = 0.02f
//		this.mouthBottom.rotationPointY = 0.0f
		mane.rotationPointY = head.rotationPointY
		horseLeftEar.rotationPointZ = head.rotationPointZ
		horseRightEar.rotationPointZ = head.rotationPointZ
		neck.rotationPointZ = head.rotationPointZ
//		this.mouthTop.rotationPointZ = 0.02f - f11 * 1.0f
//		this.mouthBottom.rotationPointZ = 0.0f + f11 * 1.0f
		mane.rotationPointZ = head.rotationPointZ
		horseLeftEar.rotateAngleX = head.rotateAngleX
		horseRightEar.rotateAngleX = head.rotateAngleX
		neck.rotateAngleX = head.rotateAngleX
//		this.mouthTop.rotateAngleX = 0.0f - 0.09424778f * f11
//		this.mouthBottom.rotateAngleX = 0.0f + 0.15707964f * f11
		mane.rotateAngleX = head.rotateAngleX
		horseLeftEar.rotateAngleY = head.rotateAngleY
		horseRightEar.rotateAngleY = head.rotateAngleY
		neck.rotateAngleY = head.rotateAngleY
//		this.mouthTop.rotateAngleY = 0.0f
//		this.mouthBottom.rotateAngleY = 0.0f
		mane.rotateAngleY = head.rotateAngleY
		val f18 = 0.2617994f * 0f
		val f19 = MathHelper.cos(f12 * 0.6f + Math.PI.toFloat())
		frontLeftLeg.rotationPointY = -2.0f * 0f + 9.0f * 1f
		frontLeftLeg.rotationPointZ = -2.0f * 0f + -8.0f * 1f + 2f
		frontRightLeg.rotationPointY = frontLeftLeg.rotationPointY
		frontRightLeg.rotationPointZ = frontLeftLeg.rotationPointZ
		backLeftShin.rotationPointY = backLeftLeg.rotationPointY + MathHelper.sin(Math.PI.toFloat() / 2f + f18 + 1f * -f13 * 0.5f * knows) * 7.0f
		backLeftShin.rotationPointZ = backLeftLeg.rotationPointZ + MathHelper.cos(Math.PI.toFloat() * 3f / 2f + f18 + 1f * -f13 * 0.5f * knows) * 7.0f
		backRightShin.rotationPointY = backRightLeg.rotationPointY + MathHelper.sin(Math.PI.toFloat() / 2f + f18 + 1f * f13 * 0.5f * knows) * 7.0f
		backRightShin.rotationPointZ = backRightLeg.rotationPointZ + MathHelper.cos(Math.PI.toFloat() * 3f / 2f + f18 + 1f * f13 * 0.5f * knows) * 7.0f
		val f20 = (-1.0471976f + f19) * 0f + f14 * 1f
		val f21 = (-1.0471976f + -f19) * 0f + -f14 * 1f
		frontLeftShin.rotationPointY = frontLeftLeg.rotationPointY + MathHelper.sin(Math.PI.toFloat() / 2f + f20) * 7.0f
		frontLeftShin.rotationPointZ = frontLeftLeg.rotationPointZ + MathHelper.cos(Math.PI.toFloat() * 3f / 2f + f20) * 7.0f
		frontRightShin.rotationPointY = frontRightLeg.rotationPointY + MathHelper.sin(Math.PI.toFloat() / 2f + f21) * 7.0f
		frontRightShin.rotationPointZ = frontRightLeg.rotationPointZ + MathHelper.cos(Math.PI.toFloat() * 3f / 2f + f21) * 7.0f
		
		
		backLeftLeg.rotateAngleX = f18 + -f13 * 0.5f * knows * 1f
		backLeftShin.rotateAngleX = -0.08726646f * 0f + (-f13 * 0.5f * knows - max(0.0f, f13 * 0.5f * knows)) * 1f
		backLeftHoof.rotateAngleX = backLeftShin.rotateAngleX
		
		backRightMiddleLeg.rotateAngleX = f18 + -f13 * 0.5f * knows * 1f
		backRightMiddleShin.rotateAngleX = -0.08726646f * 0f + (-f13 * 0.5f * knows - max(0.0f, f13 * 0.5f * knows)) * 1f
		backRightMiddleHoof.rotateAngleX = backLeftShin.rotateAngleX
		
		
		
		backRightLeg.rotateAngleX = f18 + f13 * 0.5f * knows * 1f
		backRightShin.rotateAngleX = -0.08726646f * 0f + (f13 * 0.5f * knows - max(0.0f, -f13 * 0.5f * knows)) * 1f
		backRightHoof.rotateAngleX = backRightShin.rotateAngleX
		
		backLeftMiddleLeg.rotateAngleX = f18 + f13 * 0.5f * knows * 1f
		backLeftMiddleShin.rotateAngleX = -0.08726646f * 0f + (f13 * 0.5f * knows - max(0.0f, -f13 * 0.5f * knows)) * 1f
		backLeftMiddleHoof.rotateAngleX = backRightShin.rotateAngleX
		
		
		
		frontLeftLeg.rotateAngleX = f20
		frontLeftShin.rotateAngleX = (frontLeftLeg.rotateAngleX + Math.PI.toFloat() * max(0.0f, 0.2f + f19 * 0.2f)) * 0f + (f14 + max(0.0f, f13 * 0.5f * knows)) * 1f
		frontLeftHoof.rotateAngleX = frontLeftShin.rotateAngleX
		
		frontRightMiddleLeg.rotateAngleX = f20
		frontRightMiddleShin.rotateAngleX = (frontLeftLeg.rotateAngleX + Math.PI.toFloat() * max(0.0f, 0.2f + f19 * 0.2f)) * 0f + (f14 + max(0.0f, f13 * 0.5f * knows)) * 1f
		frontRightMiddleHoof.rotateAngleX = frontLeftShin.rotateAngleX
		
		
		
		frontRightLeg.rotateAngleX = f21
		frontRightShin.rotateAngleX = (frontRightLeg.rotateAngleX + Math.PI.toFloat() * max(0.0f, 0.2f - f19 * 0.2f)) * 0f + (-f14 + max(0.0f, -f13 * 0.5f * knows)) * 1f
		frontRightHoof.rotateAngleX = frontRightShin.rotateAngleX
		
		frontLeftMiddleLeg.rotateAngleX = f21
		frontLeftMiddleShin.rotateAngleX = (frontRightLeg.rotateAngleX + Math.PI.toFloat() * max(0.0f, 0.2f - f19 * 0.2f)) * 0f + (-f14 + max(0.0f, -f13 * 0.5f * knows)) * 1f
		frontLeftMiddleHoof.rotateAngleX = frontRightShin.rotateAngleX
		
		
		
		backLeftHoof.rotationPointY = backLeftShin.rotationPointY
		backLeftHoof.rotationPointZ = backLeftShin.rotationPointZ
		backRightHoof.rotationPointY = backRightShin.rotationPointY
		backRightHoof.rotationPointZ = backRightShin.rotationPointZ
		frontLeftHoof.rotationPointY = frontLeftShin.rotationPointY
		frontLeftHoof.rotationPointZ = frontLeftShin.rotationPointZ
		frontRightHoof.rotationPointY = frontRightShin.rotationPointY
		frontRightHoof.rotationPointZ = frontRightShin.rotationPointZ
		horseSaddleBottom.rotationPointY = 0f * 0.5f + 1f * 2.0f
		horseSaddleBottom.rotationPointZ = 0f * 11.0f + 1f * 2.0f
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
		horseSaddleBottom.rotateAngleX = body.rotateAngleX
		horseSaddleFront.rotateAngleX = body.rotateAngleX
		horseSaddleBack.rotateAngleX = body.rotateAngleX
		horseLeftRein.rotationPointY = head.rotationPointY
		horseRightRein.rotationPointY = head.rotationPointY
		horseFaceRopes.rotationPointY = head.rotationPointY
		horseLeftFaceMetal.rotationPointY = head.rotationPointY
		horseRightFaceMetal.rotationPointY = head.rotationPointY
		horseLeftRein.rotationPointZ = head.rotationPointZ
		horseRightRein.rotationPointZ = head.rotationPointZ
		horseFaceRopes.rotationPointZ = head.rotationPointZ
		horseLeftFaceMetal.rotationPointZ = head.rotationPointZ
		horseRightFaceMetal.rotationPointZ = head.rotationPointZ
		horseLeftRein.rotateAngleX = f7
		horseRightRein.rotateAngleX = f7
		horseFaceRopes.rotateAngleX = head.rotateAngleX
		horseLeftFaceMetal.rotateAngleX = head.rotateAngleX
		horseRightFaceMetal.rotateAngleX = head.rotateAngleX
		horseFaceRopes.rotateAngleY = head.rotateAngleY
		horseLeftFaceMetal.rotateAngleY = head.rotateAngleY
		horseLeftRein.rotateAngleY = head.rotateAngleY
		horseRightFaceMetal.rotateAngleY = head.rotateAngleY
		horseRightRein.rotateAngleY = head.rotateAngleY
		if (flag2) {
			horseLeftSaddleRope.rotateAngleX = -1.0471976f
			horseLeftSaddleMetal.rotateAngleX = -1.0471976f
			horseRightSaddleRope.rotateAngleX = -1.0471976f
			horseRightSaddleMetal.rotateAngleX = -1.0471976f
			horseLeftSaddleRope.rotateAngleZ = 0.0f
			horseLeftSaddleMetal.rotateAngleZ = 0.0f
			horseRightSaddleRope.rotateAngleZ = 0.0f
			horseRightSaddleMetal.rotateAngleZ = 0.0f
		} else {
			horseLeftSaddleRope.rotateAngleX = f14 / 3.0f
			horseLeftSaddleMetal.rotateAngleX = f14 / 3.0f
			horseRightSaddleRope.rotateAngleX = f14 / 3.0f
			horseRightSaddleMetal.rotateAngleX = f14 / 3.0f
			horseLeftSaddleRope.rotateAngleZ = f14 / 5.0f
			horseLeftSaddleMetal.rotateAngleZ = f14 / 5.0f
			horseRightSaddleRope.rotateAngleZ = -f14 / 5.0f
			horseRightSaddleMetal.rotateAngleZ = -f14 / 5.0f
		}
		tailBase.rotateAngleY = MathHelper.cos(f12 * 0.7f)
		tailMiddle.rotateAngleY = tailBase.rotateAngleY
		tailTip.rotateAngleY = tailBase.rotateAngleY
		tailMiddle.rotationPointY = tailBase.rotationPointY
		tailTip.rotationPointY = tailBase.rotationPointY
		tailMiddle.rotationPointZ = tailBase.rotationPointZ
		tailTip.rotationPointZ = tailBase.rotationPointZ
		tailBase.rotateAngleX = 0f
		tailMiddle.rotateAngleX = 0f
		tailTip.rotateAngleX = -0.2618f + 0f
	}
}