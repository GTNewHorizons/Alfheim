package alfmod.client.render.model

import net.minecraft.client.model.*
import net.minecraft.entity.Entity

class ModelSnowSprite: ModelBase() {
	
	var body: ModelRenderer
	
	init {
		textureWidth = 16
		textureHeight = 8
		body = ModelRenderer(this, 0, 0)
		body.addBox(0.0f, 0.0f, 0.0f, 4, 4, 4)
		body.setRotationPoint(-2.0f, 16.0f, -2.0f)
		body.setTextureSize(16, 8)
		setRotation(body, 0.0f, 0.0f, 0.0f)
	}
	
	override fun render(entity: Entity, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
		super.render(entity, f, f1, f2, f3, f4, f5)
		setRotationAngles(f, f1, f2, f3, f4, f5, entity)
		body.render(f5)
	}
	
	fun setRotation(model: ModelRenderer, x: Float, y: Float, z: Float) {
		model.rotateAngleX = x
		model.rotateAngleY = y
		model.rotateAngleZ = z
	}
}