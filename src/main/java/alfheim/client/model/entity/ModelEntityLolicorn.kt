package alfheim.client.model.entity

import alexsocol.asjlib.ASJUtilities
import alfheim.common.entity.EntityLolicorn
import net.minecraft.client.Minecraft
import net.minecraft.client.model.*
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.*
import net.minecraft.util.MathHelper
import org.lwjgl.opengl.GL11.*
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara
import kotlin.math.*

class ModelEntityLolicorn: ModelBase() {
	
	private val head: ModelRenderer
	private val mouthTop: ModelRenderer
	private val mouthBottom: ModelRenderer
	private val horseLeftEar: ModelRenderer
	private val horseRightEar: ModelRenderer
	private val neck: ModelRenderer
	/**
	 * The box for the horse's ropes on its face.
	 */
	private val horseFaceRopes: ModelRenderer
	private val mane: ModelRenderer
	private val body: ModelRenderer
	private val tailBase: ModelRenderer
	private val tailMiddle: ModelRenderer
	private val tailTip: ModelRenderer
	private val backLeftLeg: ModelRenderer
	private val backLeftShin: ModelRenderer
	private val backLeftHoof: ModelRenderer
	private val backRightLeg: ModelRenderer
	private val backRightShin: ModelRenderer
	private val backRightHoof: ModelRenderer
	private val frontLeftLeg: ModelRenderer
	private val frontLeftShin: ModelRenderer
	private val frontLeftHoof: ModelRenderer
	private val frontRightLeg: ModelRenderer
	private val frontRightShin: ModelRenderer
	private val frontRightHoof: ModelRenderer
	private val horseSaddleBottom: ModelRenderer
	private val horseSaddleFront: ModelRenderer
	private val horseSaddleBack: ModelRenderer
	private val horseLeftSaddleRope: ModelRenderer
	private val horseLeftSaddleMetal: ModelRenderer
	private val horseRightSaddleRope: ModelRenderer
	private val horseRightSaddleMetal: ModelRenderer
	/**
	 * The left metal connected to the horse's face ropes.
	 */
	private val horseLeftFaceMetal: ModelRenderer
	/**
	 * The right metal connected to the horse's face ropes.
	 */
	private val horseRightFaceMetal: ModelRenderer
	private val horseLeftRein: ModelRenderer
	private val horseRightRein: ModelRenderer
	
	init {
		textureWidth = 128
		textureHeight = 128
		body = ModelRenderer(this, 0, 34)
		body.addBox(-5f, -8f, -19f, 10, 10, 24)
		body.setRotationPoint(0f, 11f, 9f)
		tailBase = ModelRenderer(this, 44, 0)
		tailBase.addBox(-1f, -1f, 0f, 2, 2, 3)
		tailBase.setRotationPoint(0f, 3f, 14f)
		setBoxRotation(tailBase, -1.134464f, 0f, 0f)
		tailMiddle = ModelRenderer(this, 38, 7)
		tailMiddle.addBox(-1.5f, -2f, 3f, 3, 4, 7)
		tailMiddle.setRotationPoint(0f, 3f, 14f)
		setBoxRotation(tailMiddle, -1.134464f, 0f, 0f)
		tailTip = ModelRenderer(this, 24, 3)
		tailTip.addBox(-1.5f, -4.5f, 9f, 3, 4, 7)
		tailTip.setRotationPoint(0f, 3f, 14f)
		setBoxRotation(tailTip, -1.40215f, 0f, 0f)
		backLeftLeg = ModelRenderer(this, 78, 29)
		backLeftLeg.addBox(-2.5f, -2f, -2.5f, 4, 9, 5)
		backLeftLeg.setRotationPoint(4f, 9f, 11f)
		backLeftShin = ModelRenderer(this, 78, 43)
		backLeftShin.addBox(-2f, 0f, -1.5f, 3, 5, 3)
		backLeftShin.setRotationPoint(4f, 16f, 11f)
		backLeftHoof = ModelRenderer(this, 78, 51)
		backLeftHoof.addBox(-2.5f, 5.1f, -2f, 4, 3, 4)
		backLeftHoof.setRotationPoint(4f, 16f, 11f)
		backRightLeg = ModelRenderer(this, 96, 29)
		backRightLeg.addBox(-1.5f, -2f, -2.5f, 4, 9, 5)
		backRightLeg.setRotationPoint(-4f, 9f, 11f)
		backRightShin = ModelRenderer(this, 96, 43)
		backRightShin.addBox(-1f, 0f, -1.5f, 3, 5, 3)
		backRightShin.setRotationPoint(-4f, 16f, 11f)
		backRightHoof = ModelRenderer(this, 96, 51)
		backRightHoof.addBox(-1.5f, 5.1f, -2f, 4, 3, 4)
		backRightHoof.setRotationPoint(-4f, 16f, 11f)
		frontLeftLeg = ModelRenderer(this, 44, 29)
		frontLeftLeg.addBox(-1.9f, -1f, -2.1f, 3, 8, 4)
		frontLeftLeg.setRotationPoint(4f, 9f, -8f)
		frontLeftShin = ModelRenderer(this, 44, 41)
		frontLeftShin.addBox(-1.9f, 0f, -1.6f, 3, 5, 3)
		frontLeftShin.setRotationPoint(4f, 16f, -8f)
		frontLeftHoof = ModelRenderer(this, 44, 51)
		frontLeftHoof.addBox(-2.4f, 5.1f, -2.1f, 4, 3, 4)
		frontLeftHoof.setRotationPoint(4f, 16f, -8f)
		frontRightLeg = ModelRenderer(this, 60, 29)
		frontRightLeg.addBox(-1.1f, -1f, -2.1f, 3, 8, 4)
		frontRightLeg.setRotationPoint(-4f, 9f, -8f)
		frontRightShin = ModelRenderer(this, 60, 41)
		frontRightShin.addBox(-1.1f, 0f, -1.6f, 3, 5, 3)
		frontRightShin.setRotationPoint(-4f, 16f, -8f)
		frontRightHoof = ModelRenderer(this, 60, 51)
		frontRightHoof.addBox(-1.6f, 5.1f, -2.1f, 4, 3, 4)
		frontRightHoof.setRotationPoint(-4f, 16f, -8f)
		head = ModelRenderer(this, 0, 0)
		head.addBox(-2.5f, -10f, -1.5f, 5, 5, 7)
		head.setRotationPoint(0f, 4f, -10f)
		setBoxRotation(head, 0.5235988f, 0f, 0f)
		mouthTop = ModelRenderer(this, 24, 18)
		mouthTop.addBox(-2f, -10f, -7f, 4, 3, 6)
		mouthTop.setRotationPoint(0f, 3.95f, -10f)
		setBoxRotation(mouthTop, 0.5235988f, 0f, 0f)
		mouthBottom = ModelRenderer(this, 24, 27)
		mouthBottom.addBox(-2f, -7f, -6.5f, 4, 2, 5)
		mouthBottom.setRotationPoint(0f, 4f, -10f)
		setBoxRotation(mouthBottom, 0.5235988f, 0f, 0f)
		head.addChild(mouthTop)
		head.addChild(mouthBottom)
		horseLeftEar = ModelRenderer(this, 0, 0)
		horseLeftEar.addBox(0.45f, -12f, 4f, 2, 3, 1)
		horseLeftEar.setRotationPoint(0f, 4f, -10f)
		setBoxRotation(horseLeftEar, 0.5235988f, 0f, 0f)
		horseRightEar = ModelRenderer(this, 0, 0)
		horseRightEar.addBox(-2.45f, -12f, 4f, 2, 3, 1)
		horseRightEar.setRotationPoint(0f, 4f, -10f)
		setBoxRotation(horseRightEar, 0.5235988f, 0f, 0f)
		neck = ModelRenderer(this, 0, 12)
		neck.addBox(-2.05f, -9.8f, -2f, 4, 14, 8)
		neck.setRotationPoint(0f, 4f, -10f)
		setBoxRotation(neck, 0.5235988f, 0f, 0f)
		horseSaddleBottom = ModelRenderer(this, 80, 0)
		horseSaddleBottom.addBox(-5f, 0f, -3f, 10, 1, 8)
		horseSaddleBottom.setRotationPoint(0f, 2f, 2f)
		horseSaddleFront = ModelRenderer(this, 106, 9)
		horseSaddleFront.addBox(-1.5f, -1f, -3f, 3, 1, 2)
		horseSaddleFront.setRotationPoint(0f, 2f, 2f)
		horseSaddleBack = ModelRenderer(this, 80, 9)
		horseSaddleBack.addBox(-4f, -1f, 3f, 8, 1, 2)
		horseSaddleBack.setRotationPoint(0f, 2f, 2f)
		horseLeftSaddleMetal = ModelRenderer(this, 74, 0)
		horseLeftSaddleMetal.addBox(-0.5f, 6f, -1f, 1, 2, 2)
		horseLeftSaddleMetal.setRotationPoint(5f, 3f, 2f)
		horseLeftSaddleRope = ModelRenderer(this, 70, 0)
		horseLeftSaddleRope.addBox(-0.5f, 0f, -0.5f, 1, 6, 1)
		horseLeftSaddleRope.setRotationPoint(5f, 3f, 2f)
		horseRightSaddleMetal = ModelRenderer(this, 74, 4)
		horseRightSaddleMetal.addBox(-0.5f, 6f, -1f, 1, 2, 2)
		horseRightSaddleMetal.setRotationPoint(-5f, 3f, 2f)
		horseRightSaddleRope = ModelRenderer(this, 80, 0)
		horseRightSaddleRope.addBox(-0.5f, 0f, -0.5f, 1, 6, 1)
		horseRightSaddleRope.setRotationPoint(-5f, 3f, 2f)
		horseLeftFaceMetal = ModelRenderer(this, 74, 13)
		horseLeftFaceMetal.addBox(1.5f, -8f, -4f, 1, 2, 2)
		horseLeftFaceMetal.setRotationPoint(0f, 4f, -10f)
		setBoxRotation(horseLeftFaceMetal, 0.5235988f, 0f, 0f)
		horseRightFaceMetal = ModelRenderer(this, 74, 13)
		horseRightFaceMetal.addBox(-2.5f, -8f, -4f, 1, 2, 2)
		horseRightFaceMetal.setRotationPoint(0f, 4f, -10f)
		setBoxRotation(horseRightFaceMetal, 0.5235988f, 0f, 0f)
		horseLeftRein = ModelRenderer(this, 44, 10)
		horseLeftRein.addBox(2.6f, -6f, -6f, 0, 3, 16)
		horseLeftRein.setRotationPoint(0f, 4f, -10f)
		horseRightRein = ModelRenderer(this, 44, 5)
		horseRightRein.addBox(-2.6f, -6f, -6f, 0, 3, 16)
		horseRightRein.setRotationPoint(0f, 4f, -10f)
		mane = ModelRenderer(this, 58, 0)
		mane.addBox(-1f, -11.5f, 5f, 2, 16, 4)
		mane.setRotationPoint(0f, 4f, -10f)
		setBoxRotation(mane, 0.5235988f, 0f, 0f)
		horseFaceRopes = ModelRenderer(this, 80, 12)
		horseFaceRopes.addBox(-2.5f, -10.1f, -7f, 5, 5, 12, 0.2f)
		horseFaceRopes.setRotationPoint(0f, 4f, -10f)
		setBoxRotation(horseFaceRopes, 0.5235988f, 0f, 0f)
	}
	
	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	override fun render(entity: Entity, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
		horseFaceRopes.render(f5)
		horseSaddleBottom.render(f5)
		horseSaddleFront.render(f5)
		horseSaddleBack.render(f5)
		horseLeftSaddleRope.render(f5)
		horseLeftSaddleMetal.render(f5)
		horseRightSaddleRope.render(f5)
		horseRightSaddleMetal.render(f5)
		horseLeftFaceMetal.render(f5)
		horseRightFaceMetal.render(f5)
		
		if (entity.riddenByEntity != null) {
			horseLeftRein.render(f5)
			horseRightRein.render(f5)
		}
		
		backLeftLeg.render(f5)
		backLeftShin.render(f5)
		backLeftHoof.render(f5)
		backRightLeg.render(f5)
		backRightShin.render(f5)
		backRightHoof.render(f5)
		frontLeftLeg.render(f5)
		frontLeftShin.render(f5)
		frontLeftHoof.render(f5)
		frontRightLeg.render(f5)
		frontRightShin.render(f5)
		frontRightHoof.render(f5)
		
		body.render(f5)
		tailBase.render(f5)
		tailMiddle.render(f5)
		tailTip.render(f5)
		neck.render(f5)
		mane.render(f5)
		
		horseLeftEar.render(f5)
		horseRightEar.render(f5)
		
		head.render(f5)
	}
	
	/**
	 * Sets the rotations for a ModelRenderer in the ModelHorse class.
	 */
	private fun setBoxRotation(model: ModelRenderer, x: Float, y: Float, z: Float) {
		model.rotateAngleX = x
		model.rotateAngleY = y
		model.rotateAngleZ = z
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
	
	/**
	 * Used for easily adding entity-dependent animations. The second and third float params here are the same second
	 * and third as in the setRotationAngles method.
	 */
	override fun setLivingAnimations(entity: EntityLivingBase?, limb: Float, prevLimb: Float, ticks: Float) {
		super.setLivingAnimations(entity, limb, prevLimb, ticks)
		val lolicorn = entity as EntityLolicorn
		
		val f3 = updateHorseRotation(entity.prevRenderYawOffset, entity.renderYawOffset, ticks)
		val f4 = updateHorseRotation(entity.prevRotationYawHead, entity.rotationYawHead, ticks)
		val f5 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * ticks
		var f6 = f4 - f3
		if (f6 > 20f) f6 = 20f
		if (f6 < -20f) f6 = -20f
		var f7 = f5 / (180f / Math.PI.toFloat())
		if (prevLimb > 0.2f) f7 += MathHelper.cos(limb * 0.4f) * 0.15f * prevLimb
		
		val f8 = 0f
//		val f9 = abs(sin(entity.worldObj.totalWorldTime / 40f))
		val f9 = ASJUtilities.interpolate(lolicorn.prevRearingAmount.toDouble(), lolicorn.rearingAmount.toDouble()).toFloat()
		val f10 = 1f - f9
		val f11 = ASJUtilities.interpolate(lolicorn.prevMouthOpenness.toDouble(), lolicorn.mouthOpenness.toDouble()).toFloat()
		val flag = lolicorn.tailMovement != 0
		val flag2 = lolicorn.riddenByEntity != null
		val f12 = entity.ticksExisted.toFloat() + ticks
		val f13 = MathHelper.cos(limb * 0.6662f + Math.PI.toFloat())
		val f14 = f13 * 0.8f * prevLimb
		head.rotationPointY = 4f
		head.rotationPointZ = -10f
		tailBase.rotationPointY = 3f
		tailMiddle.rotationPointZ = 14f
		body.rotateAngleX = 0f
		head.rotateAngleX = 0.5235988f + f7
		head.rotateAngleY = f6 / (180f / Math.PI.toFloat())
		head.rotateAngleX = f9 * (0.2617994f + f7) + f8 * 2.18166f + (1f - max(f9, f8)) * head.rotateAngleX
		head.rotateAngleY = f9 * (f6 / (180f / Math.PI.toFloat())) + (1f - max(f9, f8)) * head.rotateAngleY
		head.rotationPointY = f9 * -6f + f8 * 11f + (1f - max(f9, f8)) * head.rotationPointY
		head.rotationPointZ = f9 * -1f + f8 * -10f + (1f - max(f9, f8)) * head.rotationPointZ
		tailBase.rotationPointY = f9 * 9f + f10 * tailBase.rotationPointY
		tailMiddle.rotationPointZ = f9 * 18f + f10 * tailMiddle.rotationPointZ
		body.rotateAngleX = f9 * -(Math.PI.toFloat() / 4f) + f10 * body.rotateAngleX
		horseLeftEar.rotationPointY = head.rotationPointY
		horseRightEar.rotationPointY = head.rotationPointY
		neck.rotationPointY = head.rotationPointY
		mouthTop.rotationPointY = 0.02f
		mouthBottom.rotationPointY = 0f
		mane.rotationPointY = head.rotationPointY
		horseLeftEar.rotationPointZ = head.rotationPointZ
		horseRightEar.rotationPointZ = head.rotationPointZ
		neck.rotationPointZ = head.rotationPointZ
		mouthTop.rotationPointZ = 0.02f - f11 * 1f
		mouthBottom.rotationPointZ = 0f + f11 * 1f
		mane.rotationPointZ = head.rotationPointZ
		horseLeftEar.rotateAngleX = head.rotateAngleX
		horseRightEar.rotateAngleX = head.rotateAngleX
		neck.rotateAngleX = head.rotateAngleX
		mouthTop.rotateAngleX = 0f - 0.09424778f * f11
		mouthBottom.rotateAngleX = 0f + 0.15707964f * f11
		mane.rotateAngleX = head.rotateAngleX
		horseLeftEar.rotateAngleY = head.rotateAngleY
		horseRightEar.rotateAngleY = head.rotateAngleY
		neck.rotateAngleY = head.rotateAngleY
		mouthTop.rotateAngleY = 0f
		mouthBottom.rotateAngleY = 0f
		mane.rotateAngleY = head.rotateAngleY
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
			
			glPushMatrix()
			glEnable(GL_BLEND)
			glDisable(GL_CULL_FACE)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			
			val f = icon.minU
			val f0 = icon.minV
			val f1 = icon.maxU
			val f2 = icon.maxV
			val sr = 1f / s
			
			val bx = (body.rotationPointX - body.offsetX) / 16f
			val by = (body.rotationPointY - body.offsetY) / 16f
			val bz = (body.rotationPointZ - body.offsetZ) / 16f
			
			glTranslatef(bx, by, bz)
			glRotated(Math.toDegrees(body.rotateAngleX.toDouble()), 1.0, 0.0, 0.0)
			glTranslatef(-bx, -by, -bz)
			
			glTranslatef(g, h, i)
			
			glRotatef(rz, 0f, 0f, 1f)
			glRotatef(ry, 0f, 1f, 0f)
			glRotatef(rx, 1f, 0f, 0f)
			glScalef(s, s, s)
			ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f0, f, f2, icon.iconWidth, icon.iconHeight, 1f / 32f)
			glScalef(sr, sr, sr)
			glRotatef(-rx, 1f, 0f, 0f)
			glRotatef(-ry, 0f, 1f, 0f)
			glRotatef(-rz, 0f, 0f, 1f)
			
			glScalef(-1f, 1f, 1f)
			
			glTranslatef(g*2, 0f, 0f)
			glRotatef(rz, 0f, 0f, 1f)
			glRotatef(ry, 0f, 1f, 0f)
			glRotatef(rx, 1f, 0f, 0f)
			glScalef(s, s, s)
			ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f0, f, f2, icon.iconWidth, icon.iconHeight, 1f / 32f)
			glScalef(sr, sr, sr)
			glRotatef(-rx, 1f, 0f, 0f)
			glRotatef(-ry, 1f, 0f, 0f)
			glRotatef(-rz, 0f, 0f, 1f)
			
			glColor4d(1.0, 1.0, 1.0, 1.0)
			glDisable(GL_BLEND)
			glEnable(GL_CULL_FACE)
			glPopMatrix()
		}
	}
}