package alfheim.client.model.entity

import alfheim.common.core.util.F
import net.minecraft.client.model.*
import net.minecraft.entity.Entity
import net.minecraft.util.MathHelper

class ModelEntityElf: ModelBase() {
	internal val head: ModelRenderer
	internal val body: ModelRenderer
	internal val helmet: ModelRenderer
	internal val rightarm: ModelRenderer
	internal val rightshoulder: ModelRenderer
	internal val leftarm: ModelRenderer
	internal val leftshoulder: ModelRenderer
	internal val rightleg: ModelRenderer
	internal val rightboot: ModelRenderer
	internal val leftleg: ModelRenderer
	internal val leftboot: ModelRenderer
	internal val Shape1: ModelRenderer
	internal val Shape2: ModelRenderer
	internal val Shape3: ModelRenderer
	internal val Shape4: ModelRenderer
	internal val Shape5: ModelRenderer
	internal val Shape6: ModelRenderer
	internal val Shape7: ModelRenderer
	internal val Shape8: ModelRenderer
	internal val Shape9: ModelRenderer
	internal val Shape10: ModelRenderer
	internal val Shape11: ModelRenderer
	internal val Shape12: ModelRenderer
	internal val Shape13: ModelRenderer
	internal val Shape14: ModelRenderer
	internal val Shape15: ModelRenderer
	internal val Shape16: ModelRenderer
	internal val Shape17: ModelRenderer
	internal val Shape18: ModelRenderer
	internal val backBag: ModelRenderer
	internal val frontBelt: ModelRenderer
	internal val lowerBelt: ModelRenderer
	internal val upperBelt: ModelRenderer
	internal val frontBag: ModelRenderer
	
	init {
		textureWidth = 128
		textureHeight = 64
		
		head = ModelRenderer(this, 0, 0)
		head.addBox(-4f, -8f, -4f, 8, 8, 8)
		head.setRotationPoint(0f, 0f, 0f)
		head.setTextureSize(128, 64)
		head.mirror = true
		setRotation(head, 0f, 0f, 0f)
		body = ModelRenderer(this, 16, 16)
		body.addBox(-4f, 0f, -2f, 8, 12, 4)
		body.setRotationPoint(0f, 0f, 0f)
		body.setTextureSize(128, 64)
		body.mirror = true
		setRotation(body, 0f, 0f, 0f)
		helmet = ModelRenderer(this, 32, 0)
		helmet.addBox(-4.5f, -8.5f, -4.5f, 9, 4, 9)
		helmet.setRotationPoint(0f, 0f, 0f)
		helmet.setTextureSize(128, 64)
		helmet.mirror = true
		setRotation(helmet, 0f, 0f, 0f)
		rightarm = ModelRenderer(this, 40, 16)
		rightarm.addBox(-3f, -2f, -2f, 4, 12, 4)
		rightarm.setRotationPoint(-5f, 2f, 0f)
		rightarm.setTextureSize(128, 64)
		rightarm.mirror = true
		setRotation(rightarm, 0f, 0f, 0f)
		rightshoulder = ModelRenderer(this, 76, 17)
		rightshoulder.addBox(-3.5f, -2.5f, -2.5f, 5, 4, 5)
		rightshoulder.setRotationPoint(-5f, 2f, 0f)
		rightshoulder.setTextureSize(128, 64)
		rightshoulder.mirror = true
		setRotation(rightshoulder, 0f, 0f, 0f)
		leftarm = ModelRenderer(this, 40, 16)
		leftarm.addBox(-1f, -2f, -2f, 4, 12, 4)
		leftarm.setRotationPoint(5f, 2f, 0f)
		leftarm.setTextureSize(128, 64)
		setRotation(leftarm, 0f, 0f, 0f)
		leftarm.mirror = false
		leftshoulder = ModelRenderer(this, 56, 17)
		leftshoulder.addBox(-1.5f, -2.5f, -2.5f, 5, 4, 5)
		leftshoulder.setRotationPoint(5f, 2f, 0f)
		leftshoulder.setTextureSize(128, 64)
		leftshoulder.mirror = true
		setRotation(leftshoulder, 0f, 0f, 0f)
		rightleg = ModelRenderer(this, 0, 16)
		rightleg.addBox(-2f, 0f, -2f, 4, 12, 4)
		rightleg.setRotationPoint(-2f, 12f, 0f)
		rightleg.setTextureSize(128, 64)
		rightleg.mirror = true
		setRotation(rightleg, 0f, 0f, 0f)
		rightboot = ModelRenderer(this, 56, 26)
		rightboot.addBox(-2.5f, 8f, -2.5f, 5, 4, 5)
		rightboot.setRotationPoint(-2f, 12f, 0f)
		rightboot.setTextureSize(128, 64)
		rightboot.mirror = true
		setRotation(rightboot, 0f, 0f, 0f)
		leftleg = ModelRenderer(this, 0, 16)
		leftleg.addBox(-2f, 0f, -2f, 4, 12, 4)
		leftleg.setRotationPoint(2f, 12f, 0f)
		leftleg.setTextureSize(128, 64)
		setRotation(leftleg, 0f, 0f, 0f)
		leftleg.mirror = false
		leftboot = ModelRenderer(this, 76, 26)
		leftboot.addBox(-2.5f, 8f, -2.5f, 5, 4, 5)
		leftboot.setRotationPoint(2f, 12f, 0f)
		leftboot.setTextureSize(128, 64)
		leftboot.mirror = true
		setRotation(leftboot, 0f, 0f, 0f)
		Shape1 = ModelRenderer(this, 68, 0)
		Shape1.addBox(-4.5f, -4.5f, -0.5f, 9, 2, 5)
		Shape1.setRotationPoint(0f, 0f, 0f)
		Shape1.setTextureSize(128, 64)
		Shape1.mirror = true
		setRotation(Shape1, 0f, 0f, 0f)
		Shape2 = ModelRenderer(this, 0, 32)
		Shape2.addBox(4f, -12f, -5f, 1, 7, 1)
		Shape2.setRotationPoint(0f, 0f, 0f)
		Shape2.setTextureSize(128, 64)
		Shape2.mirror = true
		setRotation(Shape2, -0.5235988f, 0f, 0f)
		Shape3 = ModelRenderer(this, 0, 32)
		Shape3.addBox(4f, -13.1f, -6f, 1, 9, 1)
		Shape3.setRotationPoint(0f, 0f, 0f)
		Shape3.setTextureSize(128, 64)
		Shape3.mirror = true
		setRotation(Shape3, -0.5235988f, 0f, 0f)
		Shape4 = ModelRenderer(this, 0, 32)
		Shape4.addBox(4f, -11.2f, -4f, 1, 6, 1)
		Shape4.setRotationPoint(0f, 0f, 0f)
		Shape4.setTextureSize(128, 64)
		Shape4.mirror = true
		setRotation(Shape4, -0.5235988f, 0f, 0f)
		Shape5 = ModelRenderer(this, 0, 32)
		Shape5.addBox(4f, -10f, -3f, 1, 4, 1)
		Shape5.setRotationPoint(0f, 0f, 0f)
		Shape5.setTextureSize(128, 64)
		Shape5.mirror = true
		setRotation(Shape5, -0.5235988f, 0f, 0f)
		Shape6 = ModelRenderer(this, 0, 32)
		Shape6.addBox(4f, -8.3f, -2f, 1, 2, 1)
		Shape6.setRotationPoint(0f, 0f, 0f)
		Shape6.setTextureSize(128, 64)
		Shape6.mirror = true
		setRotation(Shape6, -0.5235988f, 0f, 0f)
		Shape7 = ModelRenderer(this, 0, 32)
		Shape7.addBox(4f, -11f, -7f, 1, 7, 1)
		Shape7.setRotationPoint(0f, 0f, 0f)
		Shape7.setTextureSize(128, 64)
		Shape7.mirror = true
		setRotation(Shape7, -0.5235988f, 0f, 0f)
		Shape8 = ModelRenderer(this, 0, 32)
		Shape8.addBox(4.1f, -7f, -4.1f, 1, 1, 7)
		Shape8.setRotationPoint(0f, 0f, 0f)
		Shape8.setTextureSize(128, 64)
		Shape8.mirror = true
		setRotation(Shape8, 0f, 0f, 0f)
		Shape9 = ModelRenderer(this, 0, 42)
		Shape9.addBox(-5f, -8.3f, -2f, 1, 2, 1)
		Shape9.setRotationPoint(0f, 0f, 0f)
		Shape9.setTextureSize(128, 64)
		Shape9.mirror = true
		setRotation(Shape9, -0.5235988f, 0f, 0f)
		Shape10 = ModelRenderer(this, 0, 42)
		Shape10.addBox(-5f, -10f, -3f, 1, 4, 1)
		Shape10.setRotationPoint(0f, 0f, 0f)
		Shape10.setTextureSize(128, 64)
		Shape10.mirror = true
		setRotation(Shape10, -0.5235988f, 0f, 0f)
		Shape11 = ModelRenderer(this, 0, 42)
		Shape11.addBox(-5f, -11.2f, -4f, 1, 6, 1)
		Shape11.setRotationPoint(0f, 0f, 0f)
		Shape11.setTextureSize(128, 64)
		Shape11.mirror = true
		setRotation(Shape11, -0.5235988f, 0f, 0f)
		Shape12 = ModelRenderer(this, 0, 42)
		Shape12.addBox(-5f, -12f, -5f, 1, 7, 1)
		Shape12.setRotationPoint(0f, 0f, 0f)
		Shape12.setTextureSize(128, 64)
		Shape12.mirror = true
		setRotation(Shape12, -0.5235988f, 0f, 0f)
		Shape13 = ModelRenderer(this, 0, 42)
		Shape13.addBox(-5f, -13.1f, -6f, 1, 9, 1)
		Shape13.setRotationPoint(0f, 0f, 0f)
		Shape13.setTextureSize(128, 64)
		Shape13.mirror = true
		setRotation(Shape13, -0.5235988f, 0f, 0f)
		Shape14 = ModelRenderer(this, 0, 42)
		Shape14.addBox(-5f, -11f, -7f, 1, 7, 1)
		Shape14.setRotationPoint(0f, 0f, 0f)
		Shape14.setTextureSize(128, 64)
		Shape14.mirror = true
		setRotation(Shape14, -0.5235988f, 0f, 0f)
		Shape15 = ModelRenderer(this, 0, 42)
		Shape15.addBox(-5.1f, -7f, -4.1f, 1, 1, 7)
		Shape15.setRotationPoint(0f, 0f, 0f)
		Shape15.setTextureSize(128, 64)
		Shape15.mirror = true
		setRotation(Shape15, 0f, 0f, 0f)
		Shape16 = ModelRenderer(this, 68, 13)
		Shape16.addBox(-4.5f, -2.5f, 2.5f, 9, 2, 2)
		Shape16.setRotationPoint(0f, 0f, 0f)
		Shape16.setTextureSize(128, 64)
		Shape16.mirror = true
		setRotation(Shape16, 0f, 0f, 0f)
		Shape17 = ModelRenderer(this, 68, 10)
		Shape17.addBox(-4.5f, -2.5f, 0.5f, 9, 1, 2)
		Shape17.setRotationPoint(0f, 0f, 0f)
		Shape17.setTextureSize(128, 64)
		Shape17.mirror = true
		setRotation(Shape17, 0f, 0f, 0f)
		Shape18 = ModelRenderer(this, 68, 7)
		Shape18.addBox(-4.5f, -4.5f, -2.5f, 9, 1, 2)
		Shape18.setRotationPoint(0f, 0f, 0f)
		Shape18.setTextureSize(128, 64)
		Shape18.mirror = true
		setRotation(Shape18, 0f, 0f, 0f)
		backBag = ModelRenderer(this, 16, 32)
		backBag.addBox(-1f, 8f, 2f, 5, 4, 2)
		backBag.setRotationPoint(0f, 0f, 0f)
		backBag.setTextureSize(128, 64)
		backBag.mirror = true
		setRotation(backBag, 0f, 0f, 0f)
		frontBelt = ModelRenderer(this, 0, 52)
		frontBelt.addBox(0.6f, -0.2f, -2.1f, 2, 11, 1)
		frontBelt.setRotationPoint(0f, 0f, 0f)
		frontBelt.setTextureSize(128, 64)
		frontBelt.mirror = true
		setRotation(frontBelt, 0f, 0f, 0.4363323f)
		lowerBelt = ModelRenderer(this, 16, 38)
		lowerBelt.addBox(-4f, 10f, -2.2f, 8, 2, 1)
		lowerBelt.setRotationPoint(0f, 0f, 0f)
		lowerBelt.setTextureSize(128, 64)
		lowerBelt.mirror = true
		setRotation(lowerBelt, 0f, 0f, 0f)
		upperBelt = ModelRenderer(this, 16, 41)
		upperBelt.addBox(-4f, 0f, -2.2f, 8, 1, 1)
		upperBelt.setRotationPoint(0f, 0f, 0f)
		upperBelt.setTextureSize(128, 64)
		upperBelt.mirror = true
		setRotation(upperBelt, 0f, 0f, 0f)
		frontBag = ModelRenderer(this, 6, 52)
		frontBag.addBox(0f, 6f, -4f, 3, 4, 2)
		frontBag.setRotationPoint(0f, 0f, 0f)
		frontBag.setTextureSize(128, 64)
		frontBag.mirror = true
		setRotation(frontBag, 0f, 0f, 0.4363323f)
	}
	
	override fun render(entity: Entity?, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
		super.render(entity, f, f1, f2, f3, f4, f5)
		setRotationAngles(f, f1, f2, f3, f4, f5, entity)
		head.render(f5)
		body.render(f5)
		helmet.render(f5)
		rightarm.render(f5)
		rightshoulder.render(f5)
		leftarm.render(f5)
		leftshoulder.render(f5)
		rightleg.render(f5)
		rightboot.render(f5)
		leftleg.render(f5)
		leftboot.render(f5)
		Shape1.render(f5)
		Shape2.render(f5)
		Shape3.render(f5)
		Shape4.render(f5)
		Shape5.render(f5)
		Shape6.render(f5)
		Shape7.render(f5)
		Shape8.render(f5)
		Shape9.render(f5)
		Shape10.render(f5)
		Shape11.render(f5)
		Shape12.render(f5)
		Shape13.render(f5)
		Shape14.render(f5)
		Shape15.render(f5)
		Shape16.render(f5)
		Shape17.render(f5)
		Shape18.render(f5)
		backBag.render(f5)
		frontBelt.render(f5)
		lowerBelt.render(f5)
		upperBelt.render(f5)
		frontBag.render(f5)
	}
	
	private fun setRotation(model: ModelRenderer, x: Float, y: Float, z: Float) {
		model.rotateAngleX = x
		model.rotateAngleY = y
		model.rotateAngleZ = z
	}
	
	override fun setRotationAngles(f1: Float, f2: Float, f3: Float, f4: Float, f5: Float, f6: Float, entity: Entity?) {
		super.setRotationAngles(f1, f2, f3, f4, f5, f6, entity)
		head.rotateAngleY = f4 / (180f / Math.PI.F)
		head.rotateAngleX = f5 / (180f / Math.PI.F)
		helmet.rotateAngleY = head.rotateAngleY
		helmet.rotateAngleX = head.rotateAngleX
		Shape1.rotateAngleY = head.rotateAngleY
		Shape1.rotateAngleX = head.rotateAngleX
		Shape2.rotateAngleY = head.rotateAngleY
		Shape2.rotateAngleX = head.rotateAngleX - Math.toRadians(30.0).F
		Shape3.rotateAngleY = head.rotateAngleY
		Shape3.rotateAngleX = head.rotateAngleX - Math.toRadians(30.0).F
		Shape4.rotateAngleY = head.rotateAngleY
		Shape4.rotateAngleX = head.rotateAngleX - Math.toRadians(30.0).F
		Shape5.rotateAngleY = head.rotateAngleY
		Shape5.rotateAngleX = head.rotateAngleX - Math.toRadians(30.0).F
		Shape6.rotateAngleY = head.rotateAngleY
		Shape6.rotateAngleX = head.rotateAngleX - Math.toRadians(30.0).F
		Shape7.rotateAngleY = head.rotateAngleY
		Shape7.rotateAngleX = head.rotateAngleX - Math.toRadians(30.0).F
		Shape8.rotateAngleY = head.rotateAngleY
		Shape8.rotateAngleX = head.rotateAngleX
		Shape9.rotateAngleY = head.rotateAngleY
		Shape9.rotateAngleX = head.rotateAngleX - Math.toRadians(30.0).F
		Shape10.rotateAngleY = head.rotateAngleY
		Shape10.rotateAngleX = head.rotateAngleX - Math.toRadians(30.0).F
		Shape11.rotateAngleY = head.rotateAngleY
		Shape11.rotateAngleX = head.rotateAngleX - Math.toRadians(30.0).F
		Shape12.rotateAngleY = head.rotateAngleY
		Shape12.rotateAngleX = head.rotateAngleX - Math.toRadians(30.0).F
		Shape13.rotateAngleY = head.rotateAngleY
		Shape13.rotateAngleX = head.rotateAngleX - Math.toRadians(30.0).F
		Shape14.rotateAngleY = head.rotateAngleY
		Shape14.rotateAngleX = head.rotateAngleX - Math.toRadians(30.0).F
		Shape15.rotateAngleY = head.rotateAngleY
		Shape15.rotateAngleX = head.rotateAngleX
		Shape16.rotateAngleY = head.rotateAngleY
		Shape16.rotateAngleX = head.rotateAngleX
		Shape17.rotateAngleY = head.rotateAngleY
		Shape17.rotateAngleX = head.rotateAngleX
		Shape18.rotateAngleY = head.rotateAngleY
		Shape18.rotateAngleX = head.rotateAngleX
		
		rightarm.rotateAngleX = MathHelper.cos(f1 * 0.6662f + Math.PI.F) * 2f * f2 * 0.5f
		leftarm.rotateAngleX = MathHelper.cos(f1 * 0.6662f) * 2f * f2 * 0.5f
		rightleg.rotateAngleX = MathHelper.cos(f1 * 0.6662f) * 1.4f * f2
		leftleg.rotateAngleX = MathHelper.cos(f1 * 0.6662f + Math.PI.F) * 1.4f * f2
		
		rightshoulder.rotateAngleX = rightarm.rotateAngleX
		leftshoulder.rotateAngleX = leftarm.rotateAngleX
		rightboot.rotateAngleX = rightleg.rotateAngleX
		leftboot.rotateAngleX = leftleg.rotateAngleX
		
		leftboot.rotateAngleY = 0f
		rightboot.rotateAngleY = leftboot.rotateAngleY
		leftshoulder.rotateAngleZ = rightboot.rotateAngleY
		rightshoulder.rotateAngleZ = leftshoulder.rotateAngleZ
		leftleg.rotateAngleY = rightshoulder.rotateAngleZ
		rightleg.rotateAngleY = leftleg.rotateAngleY
		leftarm.rotateAngleZ = rightleg.rotateAngleY
		rightarm.rotateAngleZ = leftarm.rotateAngleZ
	}
}
