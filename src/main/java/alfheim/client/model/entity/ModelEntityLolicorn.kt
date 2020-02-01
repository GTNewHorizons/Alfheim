package alfheim.client.model.entity

import alfheim.client.core.util.mc
import alfheim.common.core.util.*
import alfheim.common.entity.EntityLolicorn
import net.minecraft.client.model.*
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.*
import net.minecraft.util.MathHelper
import org.lwjgl.opengl.GL11.*
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara
import kotlin.math.*

@Suppress("MemberVisibilityCanBePrivate")
class ModelEntityLolicorn: ModelBase() {
	
	val head: ModelRenderer
	val hair: ModelRenderer
	val body: ModelRenderer
	val chest: ModelRenderer
	val rightarm: ModelRenderer
	val rightglove: ModelRenderer
	val leftarm: ModelRenderer
	val leftglove: ModelRenderer
	val horn: ModelRenderer
	
	val horseSaddleBack: ModelRenderer
	val tailBase: ModelRenderer
	val frontLeftHoof: ModelRenderer
	val horseLeftSaddleMetal: ModelRenderer
	val body2: ModelRenderer
	val tailMiddle: ModelRenderer
	val backRightShin: ModelRenderer
	val frontRightLeg: ModelRenderer
	val horseSaddleFront: ModelRenderer
	val backLeftShin: ModelRenderer
	val backLeftLeg: ModelRenderer
	val frontRightHoof: ModelRenderer
	val backLeftHoof: ModelRenderer
	val horseRightSaddleRope: ModelRenderer
	val backRightLeg: ModelRenderer
	val backRightHoof: ModelRenderer
	val frontLeftLeg: ModelRenderer
	val horseSaddleBottom: ModelRenderer
	val horseLeftSaddleRope: ModelRenderer
	val tailTip: ModelRenderer
	val frontRightShin: ModelRenderer
	val horseRightSaddleMetal: ModelRenderer
	val frontLeftShin: ModelRenderer
	
	init {
		textureWidth = 128
		textureHeight = 128
		
		head = ModelRenderer(this, 0, 68)
		head.addBox(-4f, -8f, -4f, 8, 8, 8)
		head.setRotationPoint(0f, -9f, -8f)
		
		hair = ModelRenderer(this, 32, 68)
		hair.addBox(-4f, -8f, -4f, 8, 8, 8, 0.5f)
		head.addChild(hair)
		
		body = ModelRenderer(this, 0, 84)
		body.addBox(-4f, 0f, -2f, 8, 12, 4)
		body.setRotationPoint(0f, -20f, -17f)
		
		chest = ModelRenderer(this, 24, 84)
		chest.addBox(-4f, 0f, -2f, 8, 12, 4, 0.5f)
		body.addChild(chest)
		
		rightarm = ModelRenderer(this, 48, 84)
		rightarm.addBox(-3f, -2f, -2f, 4, 12, 4)
		rightarm.setRotationPoint(-5f, -7f, -16f)
		
		rightglove = ModelRenderer(this, 80, 84)
		rightglove.addBox(-3f, -2f, -2f, 4, 12, 4, 0.5f)
		rightarm.addChild(rightglove)
		
		leftarm = ModelRenderer(this, 64, 84)
		leftarm.addBox(-1f, -2f, -2f, 4, 12, 4)
		leftarm.setRotationPoint(5f, -7f, -16f)
		
		leftglove = ModelRenderer(this, 96, 84)
		leftglove.addBox(-1f, -2f, -2f, 4, 12, 4, 0.5f)
		leftarm.addChild(leftglove)
		
		horn = ModelRenderer(this, 0, 0)
		horn.setRotationPoint(0f, 0f, 0f)
		horn.addBox(-0.5f, 7f, -2.5f, 1, 7, 1, 0f)
		setRotateAngle(horn, -2.35f, 0f, 0f)
		head.addChild(horn)
		
		horseLeftSaddleRope = ModelRenderer(this, 70, 0)
		horseLeftSaddleRope.setRotationPoint(5f, 3f, 2f)
		horseLeftSaddleRope.addBox(-0.5f, 0f, -0.5f, 1, 6, 1, 0f)
		frontLeftHoof = ModelRenderer(this, 44, 51)
		frontLeftHoof.setRotationPoint(4f, 16f, -8f)
		frontLeftHoof.addBox(-2.4000000953674316f, 5.099999904632568f, -2.0999999046325684f, 4, 3, 4, 2.384185791015625E-7f)
		frontRightHoof = ModelRenderer(this, 60, 51)
		frontRightHoof.setRotationPoint(-4f, 16f, -8f)
		frontRightHoof.addBox(-1.600000023841858f, 5.099999904632568f, -2.0999999046325684f, 4, 3, 4, 2.384185791015625E-7f)
		backRightHoof = ModelRenderer(this, 96, 51)
		backRightHoof.setRotationPoint(-4f, 16f, 11f)
		backRightHoof.addBox(-1.5f, 5.099999904632568f, -2f, 4, 3, 4, 2.384185791015625E-7f)
		backLeftLeg = ModelRenderer(this, 78, 29)
		backLeftLeg.setRotationPoint(4f, 9f, 11f)
		backLeftLeg.addBox(-2.5f, -2f, -2.5f, 4, 9, 5, 0f)
		horseRightSaddleRope = ModelRenderer(this, 80, 0)
		horseRightSaddleRope.setRotationPoint(-5f, 3f, 2f)
		horseRightSaddleRope.addBox(-0.5f, 0f, -0.5f, 1, 6, 1, 0f)
		backLeftShin = ModelRenderer(this, 78, 43)
		backLeftShin.setRotationPoint(4f, 16f, 11f)
		backLeftShin.addBox(-2f, 0f, -1.5f, 3, 5, 3, 0f)
		tailBase = ModelRenderer(this, 44, 0)
		tailBase.setRotationPoint(0f, 3f, 14f)
		tailBase.addBox(-1f, -1f, 0f, 2, 2, 3, 0f)
		setRotateAngle(tailBase, -1.1344640254974365f, 0f, 0f)
		tailTip = ModelRenderer(this, 24, 3)
		tailTip.setRotationPoint(0f, 3f, 14f)
		tailTip.addBox(-1.5f, -4.5f, 9f, 3, 4, 7, 0f)
		setRotateAngle(tailTip, -1.40215003490448f, 0f, 0f)
		tailMiddle = ModelRenderer(this, 38, 7)
		tailMiddle.setRotationPoint(0f, 3f, 14f)
		tailMiddle.addBox(-1.5f, -2f, 3f, 3, 4, 7, 0f)
		setRotateAngle(tailMiddle, -1.1344640254974365f, 0f, 0f)
		frontLeftShin = ModelRenderer(this, 44, 41)
		frontLeftShin.setRotationPoint(4f, 16f, -8f)
		frontLeftShin.addBox(-1.899999976158142f, 0f, -1.600000023841858f, 3, 5, 3, 0f)
		horseRightSaddleMetal = ModelRenderer(this, 74, 4)
		horseRightSaddleMetal.setRotationPoint(-5f, 3f, 2f)
		horseRightSaddleMetal.addBox(-0.5f, 6f, -1f, 1, 2, 2, 0f)
		backRightLeg = ModelRenderer(this, 96, 29)
		backRightLeg.setRotationPoint(-4f, 9f, 11f)
		backRightLeg.addBox(-1.5f, -2f, -2.5f, 4, 9, 5, 0f)
		frontLeftLeg = ModelRenderer(this, 44, 29)
		frontLeftLeg.setRotationPoint(4f, 9f, -8f)
		frontLeftLeg.addBox(-1.899999976158142f, -1f, -2.0999999046325684f, 3, 8, 4, 0f)
		backRightShin = ModelRenderer(this, 96, 43)
		backRightShin.setRotationPoint(-4f, 16f, 11f)
		backRightShin.addBox(-1f, 0f, -1.5f, 3, 5, 3, 0f)
		frontRightLeg = ModelRenderer(this, 60, 29)
		frontRightLeg.setRotationPoint(-4f, 9f, -8f)
		frontRightLeg.addBox(-1.100000023841858f, -1f, -2.0999999046325684f, 3, 8, 4, 0f)
		horseSaddleBottom = ModelRenderer(this, 80, 0)
		horseSaddleBottom.setRotationPoint(0f, 2f, 2f)
		horseSaddleBottom.addBox(-5f, 0f, -3f, 10, 1, 8, 0f)
		horseSaddleBack = ModelRenderer(this, 80, 9)
		horseSaddleBack.setRotationPoint(0f, 2f, 2f)
		horseSaddleBack.addBox(-4f, -1f, 3f, 8, 1, 2, 0f)
		horseSaddleFront = ModelRenderer(this, 106, 9)
		horseSaddleFront.setRotationPoint(0f, 2f, 2f)
		horseSaddleFront.addBox(-1.5f, -1f, -3f, 3, 1, 2, 0f)
		backLeftHoof = ModelRenderer(this, 78, 51)
		backLeftHoof.setRotationPoint(4f, 16f, 11f)
		backLeftHoof.addBox(-2.5f, 5.099999904632568f, -2f, 4, 3, 4, 2.384185791015625E-7f)
		frontRightShin = ModelRenderer(this, 60, 41)
		frontRightShin.setRotationPoint(-4f, 16f, -8f)
		frontRightShin.addBox(-1.100000023841858f, 0f, -1.600000023841858f, 3, 5, 3, 0f)
		horseLeftSaddleMetal = ModelRenderer(this, 74, 0)
		horseLeftSaddleMetal.setRotationPoint(5f, 3f, 2f)
		horseLeftSaddleMetal.addBox(-0.5f, 6f, -1f, 1, 2, 2, 0f)
		body2 = ModelRenderer(this, 0, 34)
		body2.setRotationPoint(0f, 11f, 9f)
		body2.addBox(-5f, -8f, -19f, 10, 10, 24, 0f)
		
		body2.addChild(body)
	}
	
	override fun render(entity: Entity?, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
		setRotationAngles(f, f1, f2, f3, f4, f5, entity)
		
		head.render(f5)
		rightarm.render(f5)
		leftarm.render(f5)
		
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
	
	fun setRotateAngle(modelRenderer: ModelRenderer, x: Float, y: Float, z: Float) {
		modelRenderer.rotateAngleX = x
		modelRenderer.rotateAngleY = y
		modelRenderer.rotateAngleZ = z
	}
	
	override fun setRotationAngles(limbSwing: Float, limbAmpl: Float, ticksExisted: Float, yawHead: Float, pitchHead: Float, size: Float, entity: Entity?) {
		head.rotateAngleY = yawHead / (180f / Math.PI.F)
		head.rotateAngleX = pitchHead / (180f / Math.PI.F)
		rightarm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + Math.PI.F) * 2f * limbAmpl * 0.5f
		leftarm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 2f * limbAmpl * 0.5f
		rightarm.rotateAngleZ = 0f
		leftarm.rotateAngleZ = 0f
		
		rightarm.rotateAngleY = 0f
		leftarm.rotateAngleY = 0f
		var f6: Float
		val f7: Float
		
		if (onGround > -9990f) {
			f6 = onGround
			body.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f6) * Math.PI.F * 2f) * 0.2f
			rightarm.rotationPointZ = MathHelper.sin(body.rotateAngleY) * 5f - 8f
			rightarm.rotationPointX = -MathHelper.cos(body.rotateAngleY) * 5f
			leftarm.rotationPointZ = -MathHelper.sin(body.rotateAngleY) * 5f - 8f
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
		
		body.rotateAngleX = 0f
		rightarm.rotateAngleZ += MathHelper.cos(ticksExisted * 0.09f) * 0.05f + 0.05f
		leftarm.rotateAngleZ -= MathHelper.cos(ticksExisted * 0.09f) * 0.05f + 0.05f
		rightarm.rotateAngleX += MathHelper.sin(ticksExisted * 0.067f) * 0.05f
		leftarm.rotateAngleX -= MathHelper.sin(ticksExisted * 0.067f) * 0.05f
	}
	
	override fun setLivingAnimations(entity: EntityLivingBase?, limb: Float, prevLimb: Float, pt: Float) {
		val ticks = mc.timer.renderPartialTicks
		super.setLivingAnimations(entity, limb, prevLimb, ticks)
		val lolicorn = entity as EntityLolicorn
		
		val f5 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * ticks
		var f7 = f5 / (180f / Math.PI.F)
		if (prevLimb > 0.2f) f7 += MathHelper.cos(limb * 0.4f) * 0.15f * prevLimb
		val flag = lolicorn.tailMovement != 0
		val flag2 = lolicorn.riddenByEntity != null
		val f12 = entity.ticksExisted.F + ticks
		val f13 = MathHelper.cos(limb * 0.6662f + Math.PI.F)
		val f14 = f13 * 0.8f * prevLimb
		
		tailBase.rotationPointY = 3f
		tailMiddle.rotationPointZ = 14f
		body2.rotateAngleX = 0f
		tailBase.rotationPointY = tailBase.rotationPointY
		tailMiddle.rotationPointZ = 10 * tailMiddle.rotationPointZ
		body2.rotateAngleX = body2.rotateAngleX
		frontLeftLeg.rotationPointY = 9f
		frontLeftLeg.rotationPointZ = -8f
		frontRightLeg.rotationPointY = frontLeftLeg.rotationPointY
		frontRightLeg.rotationPointZ = frontLeftLeg.rotationPointZ
		backLeftShin.rotationPointY = backLeftLeg.rotationPointY + MathHelper.sin(Math.PI.F / 2f + -f13 * 0.5f * prevLimb) * 7f
		backLeftShin.rotationPointZ = backLeftLeg.rotationPointZ + MathHelper.cos(Math.PI.F * 3f / 2f + -f13 * 0.5f * prevLimb) * 7f
		backRightShin.rotationPointY = backRightLeg.rotationPointY + MathHelper.sin(Math.PI.F / 2f + f13 * 0.5f * prevLimb) * 7f
		backRightShin.rotationPointZ = backRightLeg.rotationPointZ + MathHelper.cos(Math.PI.F * 3f / 2f + f13 * 0.5f * prevLimb) * 7f
		frontLeftShin.rotationPointY = frontLeftLeg.rotationPointY + MathHelper.sin(Math.PI.F / 2f + f14) * 7f
		frontLeftShin.rotationPointZ = frontLeftLeg.rotationPointZ + MathHelper.cos(Math.PI.F * 3f / 2f + f14) * 7f
		frontRightShin.rotationPointY = frontRightLeg.rotationPointY + MathHelper.sin(Math.PI.F / 2f - f14) * 7f
		frontRightShin.rotationPointZ = frontRightLeg.rotationPointZ + MathHelper.cos(Math.PI.F * 3f / 2f - f14) * 7f
		backLeftLeg.rotateAngleX = -f13 * 0.5f * prevLimb
		backLeftShin.rotateAngleX = -f13 * 0.5f * prevLimb - max(0f, f13 * 0.5f * prevLimb)
		backLeftHoof.rotateAngleX = backLeftShin.rotateAngleX
		backRightLeg.rotateAngleX = f13 * 0.5f * prevLimb
		backRightShin.rotateAngleX = f13 * 0.5f * prevLimb - max(0f, -f13 * 0.5f * prevLimb)
		backRightHoof.rotateAngleX = backRightShin.rotateAngleX
		frontLeftLeg.rotateAngleX = f14
		frontLeftShin.rotateAngleX = f14 + max(0f, f13 * 0.5f * prevLimb)
		frontLeftHoof.rotateAngleX = frontLeftShin.rotateAngleX
		frontRightLeg.rotateAngleX = -f14
		frontRightShin.rotateAngleX = -f14 + max(0f, -f13 * 0.5f * prevLimb)
		frontRightHoof.rotateAngleX = frontRightShin.rotateAngleX
		backLeftHoof.rotationPointY = backLeftShin.rotationPointY
		backLeftHoof.rotationPointZ = backLeftShin.rotationPointZ
		backRightHoof.rotationPointY = backRightShin.rotationPointY
		backRightHoof.rotationPointZ = backRightShin.rotationPointZ
		frontLeftHoof.rotationPointY = frontLeftShin.rotationPointY
		frontLeftHoof.rotationPointZ = frontLeftShin.rotationPointZ
		frontRightHoof.rotationPointY = frontRightShin.rotationPointY
		frontRightHoof.rotationPointZ = frontRightShin.rotationPointZ
		
		horseSaddleBottom.rotationPointY = 2f
		horseSaddleBottom.rotationPointZ = 2f
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
		
		renderWings(entity, ticks)
	}
	
	fun renderWings(entity: Entity, ticks: Float) {
		val icon = ItemFlightTiara.wingIcons[0]
		mc.renderEngine.bindTexture(TextureMap.locationItemsTexture)
		
		val flying = !entity.onGround
		
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
		
		/*val bx = (body2.rotationPointX - body2.offsetX) / 16f
		val by = (body2.rotationPointY - body2.offsetY) / 16f
		val bz = (body2.rotationPointZ - body2.offsetZ) / 16f
		
		glTranslatef(bx, by, bz)
		glRotated(Math.toDegrees(body2.rotateAngleX.D), 1.0, 0.0, 0.0)
		glTranslatef(-bx, -by, -bz)*/
		
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
		
		glTranslatef(g * 2, 0f, 0f)
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