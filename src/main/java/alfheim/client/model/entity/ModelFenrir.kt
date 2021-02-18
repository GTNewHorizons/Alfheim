package alfheim.client.model.entity

import alexsocol.asjlib.F
import alfheim.common.entity.boss.EntityFenrir
import net.minecraft.client.model.*
import net.minecraft.entity.*
import net.minecraft.util.MathHelper

object ModelFenrir: ModelBase() {
	
	var wolfHeadMain: ModelRenderer
	var wolfBody: ModelRenderer
	var wolfLeg1: ModelRenderer
	var wolfLeg2: ModelRenderer
	var wolfLeg3: ModelRenderer
	var wolfLeg4: ModelRenderer
	var wolfTail: ModelRenderer
	var wolfMane: ModelRenderer
	
	init {
		val f = 0f
		val f1 = 13.5f
		wolfHeadMain = ModelRenderer(this, 0, 0)
		wolfHeadMain.addBox(-3f, -3f, -2f, 6, 6, 4, f)
		wolfHeadMain.setRotationPoint(-1f, f1, -7f)
		wolfBody = ModelRenderer(this, 18, 14)
		wolfBody.addBox(-4f, -2f, -3f, 6, 9, 6, f)
		wolfBody.setRotationPoint(0f, 14f, 2f)
		wolfMane = ModelRenderer(this, 21, 0)
		wolfMane.addBox(-4f, -3f, -3f, 8, 6, 7, f)
		wolfMane.setRotationPoint(-1f, 14f, 2f)
		wolfLeg1 = ModelRenderer(this, 0, 18)
		wolfLeg1.addBox(-1f, 0f, -1f, 2, 8, 2, f)
		wolfLeg1.setRotationPoint(-2.5f, 16f, 7f)
		wolfLeg2 = ModelRenderer(this, 0, 18)
		wolfLeg2.addBox(-1f, 0f, -1f, 2, 8, 2, f)
		wolfLeg2.setRotationPoint(0.5f, 16f, 7f)
		wolfLeg3 = ModelRenderer(this, 0, 18)
		wolfLeg3.addBox(-1f, 0f, -1f, 2, 8, 2, f)
		wolfLeg3.setRotationPoint(-2.5f, 16f, -4f)
		wolfLeg4 = ModelRenderer(this, 0, 18)
		wolfLeg4.addBox(-1f, 0f, -1f, 2, 8, 2, f)
		wolfLeg4.setRotationPoint(0.5f, 16f, -4f)
		wolfTail = ModelRenderer(this, 9, 18)
		wolfTail.addBox(-1f, 0f, -1f, 2, 8, 2, f)
		wolfTail.setRotationPoint(-1f, 12f, 8f)
		wolfHeadMain.setTextureOffset(16, 14).addBox(-3f, -5f, 0f, 2, 2, 1, f)
		wolfHeadMain.setTextureOffset(16, 14).addBox(1f, -5f, 0f, 2, 2, 1, f)
		wolfHeadMain.setTextureOffset(0, 10).addBox(-1.5f, 0f, -5f, 3, 3, 4, f)
	}
	
	override fun render(entity: Entity, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
		super.render(entity, f, f1, f2, f3, f4, f5)
		setRotationAngles(f, f1, f2, f3, f4, f5, entity)
		wolfHeadMain.renderWithRotation(f5)
		wolfBody.render(f5)
		wolfLeg1.render(f5)
		wolfLeg2.render(f5)
		wolfLeg3.render(f5)
		wolfLeg4.render(f5)
		wolfTail.renderWithRotation(f5)
		wolfMane.render(f5)
	}
	
	/**
	 * Used for easily adding entity-dependent animations. The second and third float params here are the same second
	 * and third as in the setRotationAngles method.
	 */
	override fun setLivingAnimations(entity: EntityLivingBase, f1: Float, f2: Float, f3: Float) {
		val entitywolf = entity as EntityFenrir
		wolfTail.rotateAngleY = 0f
		wolfBody.setRotationPoint(0f, 14f, 2f)
		wolfBody.rotateAngleX = (Math.PI.F / 2f)
		wolfMane.setRotationPoint(-1f, 14f, -3f)
		wolfMane.rotateAngleX = wolfBody.rotateAngleX
		wolfTail.setRotationPoint(-1f, 12f, 8f)
		wolfLeg1.setRotationPoint(-2.5f, 16f, 7f)
		wolfLeg2.setRotationPoint(0.5f, 16f, 7f)
		wolfLeg3.setRotationPoint(-2.5f, 16f, -4f)
		wolfLeg4.setRotationPoint(0.5f, 16f, -4f)
		wolfLeg1.rotateAngleX = MathHelper.cos(f1 * 0.6662f) * 1.4f * f2
		wolfLeg2.rotateAngleX = MathHelper.cos(f1 * 0.6662f + Math.PI.F) * 1.4f * f2
		wolfLeg3.rotateAngleX = MathHelper.cos(f1 * 0.6662f + Math.PI.F) * 1.4f * f2
		wolfLeg4.rotateAngleX = MathHelper.cos(f1 * 0.6662f) * 1.4f * f2
		wolfHeadMain.rotateAngleZ = entitywolf.getShakeAngle(f3, 0f)
		wolfMane.rotateAngleZ = entitywolf.getShakeAngle(f3, -0.08f)
		wolfBody.rotateAngleZ = entitywolf.getShakeAngle(f3, -0.16f)
		wolfTail.rotateAngleZ = entitywolf.getShakeAngle(f3, -0.2f)
	}
	
	override fun setRotationAngles(f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float, entity: Entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity)
		wolfHeadMain.rotateAngleX = f4 / (180f / Math.PI.F)
		wolfHeadMain.rotateAngleY = f3 / (180f / Math.PI.F)
		wolfTail.rotateAngleX = f2
	}
}
