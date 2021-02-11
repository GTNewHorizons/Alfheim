package alfheim.client.model.entity

import alexsocol.asjlib.F
import net.minecraft.client.model.*
import net.minecraft.entity.Entity
import net.minecraft.util.MathHelper

/**
 * ModelButterfly - AlexSocol
 * Created using Tabula 4.1.1
 */
class ModelButterfly(val pass: Int): ModelBase() {
	
	var shape1: ModelRenderer
	var shape2: ModelRenderer
	var shape3: ModelRenderer
	var shape4: ModelRenderer
	
	init {
		textureWidth = 28
		textureHeight = 16
		shape1 = ModelRenderer(this, 0, 0)
		shape1.setRotationPoint(0.0f, 0.0f, 0.0f)
		shape1.addBox(-1.0f, -3.0f, -1.0f, 2, 10, 2, 0.0f)
		setRotateAngle(shape1, 1.5707963267948966f, 0.0f, 0.0f)
		shape2 = ModelRenderer(this, -8, 0)
		shape2.setRotationPoint(0.0f, 0.0f, 0.0f)
		shape2.addBox(1.0f, 0.0f, -4.0f, 10, 0, 16, 0.0f)
		shape3 = ModelRenderer(this, -8, 0)
		shape3.mirror = true
		shape3.setRotationPoint(0.0f, 0.0f, 0.0f)
		shape3.addBox(-11.0f, 0.0f, -4.0f, 10, 0, 16, 0.0f)
		shape4 = ModelRenderer(this, -4, 12)
		shape4.setRotationPoint(0.0f, 0.0f, 0.0f)
		shape4.addBox(-2.0f, 0.0f, -7.0f, 4, 0, 4, 0.0f)
		setRotateAngle(shape4, -0.32288591161895097f, 0.0f, 0.0f)
	}
	
	override fun render(entity: Entity, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
		setRotationAngles(f, f1, f2, f3, f4, f5, entity)
		
		if (pass == 0) {
			shape1.render(f5)
			shape4.render(f5)
		} else {
			shape2.render(f5)
			shape3.render(f5)
		}
	}
	
	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	fun setRotateAngle(modelRenderer: ModelRenderer, x: Float, y: Float, z: Float) {
		modelRenderer.rotateAngleX = x
		modelRenderer.rotateAngleY = y
		modelRenderer.rotateAngleZ = z
	}
	
	override fun setRotationAngles(f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float, entity: Entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity)
		shape2.rotateAngleZ = MathHelper.cos(f2) * Math.PI.F * -0.25f
		shape3.rotateAngleZ = MathHelper.cos(f2) * Math.PI.F * 0.25f
	}
}