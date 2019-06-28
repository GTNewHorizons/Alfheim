package alfheim.client.model.block

import net.minecraft.client.model.*

/**
 * ModelSimpleAnyavil - AlexSocol
 * Created using Tabula 4.1.1
 */
class ModelSimpleAnyavil: ModelBase() {
	
	val shape1: ModelRenderer
	val shape2: ModelRenderer
	val shape3: ModelRenderer
	val shape4: ModelRenderer
	
	init {
		textureWidth = 52
		textureHeight = 50
		shape1 = ModelRenderer(this, 0, 16)
		shape1.setRotationPoint(-6.0f, 20.0f, -6.0f)
		shape1.addBox(0.0f, 0.0f, 0.0f, 12, 4, 12, 0.0f)
		shape2 = ModelRenderer(this, 0, 32)
		shape2.setRotationPoint(-5.0f, 19.0f, -4.0f)
		shape2.addBox(0.0f, 0.0f, 0.0f, 10, 1, 8, 0.0f)
		shape3 = ModelRenderer(this, 0, 41)
		shape3.setRotationPoint(-4.0f, 14.0f, -2.0f)
		shape3.addBox(0.0f, 0.0f, 0.0f, 8, 5, 4, 0.0f)
		shape4 = ModelRenderer(this, 0, 0)
		shape4.setRotationPoint(-8.0f, 8.0f, -5.0f)
		shape4.addBox(0.0f, 0.0f, 0.0f, 16, 6, 10, 0.0f)
	}
	
	fun renderAll() {
		shape1.render(0.0625f)
		shape2.render(0.0625f)
		shape3.render(0.0625f)
		shape4.render(0.0625f)
	}
}
