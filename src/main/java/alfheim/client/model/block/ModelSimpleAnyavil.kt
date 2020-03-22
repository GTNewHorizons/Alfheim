package alfheim.client.model.block

import net.minecraft.client.model.*

class ModelSimpleAnyavil: ModelBase() {
	
	val shape1: ModelRenderer
	val shape2: ModelRenderer
	val shape3: ModelRenderer
	val shape4: ModelRenderer
	
	init {
		textureWidth = 52
		textureHeight = 50
		shape1 = ModelRenderer(this, 0, 16)
		shape1.setRotationPoint(-6f, 20f, -6f)
		shape1.addBox(0f, 0f, 0f, 12, 4, 12, 0f)
		shape2 = ModelRenderer(this, 0, 32)
		shape2.setRotationPoint(-5f, 19f, -4f)
		shape2.addBox(0f, 0f, 0f, 10, 1, 8, 0f)
		shape3 = ModelRenderer(this, 0, 41)
		shape3.setRotationPoint(-4f, 14f, -2f)
		shape3.addBox(0f, 0f, 0f, 8, 5, 4, 0f)
		shape4 = ModelRenderer(this, 0, 0)
		shape4.setRotationPoint(-8f, 8f, -5f)
		shape4.addBox(0f, 0f, 0f, 16, 6, 10, 0f)
	}
	
	fun renderAll() {
		shape1.render(0.0625f)
		shape2.render(0.0625f)
		shape3.render(0.0625f)
		shape4.render(0.0625f)
	}
}
