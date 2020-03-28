package alfheim.client.model.item

import net.minecraft.client.model.*

class ModelAkashicBox: ModelBase() {
	
	var Back: ModelRenderer
	var Bottom: ModelRenderer
	var Top: ModelRenderer
	var Right: ModelRenderer
	var Left: ModelRenderer
	
	init {
		textureWidth = 67
		textureHeight = 33
		Back = ModelRenderer(this, 0, 0)
		Back.setRotationPoint(0.0f, 0.0f, 0.0f)
		Back.addBox(-7.0f, -5.0f, 0.0f, 14, 12, 1, 0.0f)
		Bottom = ModelRenderer(this, 25, 12)
		Bottom.setRotationPoint(0.0f, 0.0f, 0.0f)
		Bottom.addBox(-7.0f, 7.0f, 1.0f, 14, 1, 7, 0.0f)
		Left = ModelRenderer(this, 0, 13)
		Left.setRotationPoint(0.0f, 0.0f, 0.0f)
		Left.addBox(-8.0f, -5.0f, 1.0f, 1, 12, 7, 0.0f)
		Top = ModelRenderer(this, 25, 25)
		Top.setRotationPoint(0.0f, 0.0f, 0.0f)
		Top.addBox(-7.0f, -6.0f, 1.0f, 14, 1, 7, 0.0f)
		Right = ModelRenderer(this, 16, 13)
		Right.setRotationPoint(0.0f, 0.0f, 0.0f)
		Right.addBox(7.0f, -5.0f, 1.0f, 1, 12, 7, 0.0f)
	}
	
	fun render(f5: Float) {
		Back.render(f5)
		Bottom.render(f5)
		Left.render(f5)
		Top.render(f5)
		Right.render(f5)
	}
}