package alfheim.client.model.block

import net.minecraft.client.model.*

class ModelSimpleItemHolder: ModelBase() {
	
	val shape2: ModelRenderer
	val shape3: ModelRenderer
	val shape4: ModelRenderer
	val shape5: ModelRenderer
	val shape6: ModelRenderer
	val shape7: ModelRenderer
	val shape8: ModelRenderer
	val shape9: ModelRenderer
	val shape10: ModelRenderer
	val shape11: ModelRenderer
	val shape12: ModelRenderer
	val shape13: ModelRenderer
	val shape14: ModelRenderer
	val shape15: ModelRenderer
	val shape16: ModelRenderer
	val shape17: ModelRenderer
	val shape18: ModelRenderer
	val shape19: ModelRenderer
	val shape20: ModelRenderer
	val shape21: ModelRenderer
	
	init {
		textureWidth = 16
		textureHeight = 16
		shape17 = ModelRenderer(this, 0, 9)
		shape17.setRotationPoint(0f, 11f, 0f)
		shape17.addBox(-1f, 0f, 4f, 2, 1, 1, 0f)
		setRotateAngle(shape17, 0f, 3.141592653589793f, 0f)
		shape3 = ModelRenderer(this, -2, 13)
		shape3.setRotationPoint(-4f, 11f, -4f)
		shape3.addBox(0f, 0f, 0f, 8, 1, 2, 0f)
		shape9 = ModelRenderer(this, 0, 9)
		shape9.setRotationPoint(0f, 11f, 0f)
		shape9.addBox(-1f, 0f, 4f, 2, 1, 1, 0f)
		shape16 = ModelRenderer(this, 0, 6)
		shape16.setRotationPoint(0f, 11f, 0f)
		shape16.addBox(-1f, 0f, 5f, 2, 2, 1, 0f)
		setRotateAngle(shape16, 0f, 3.141592653589793f, 0f)
		shape5 = ModelRenderer(this, 0, 0)
		shape5.setRotationPoint(-4f, 11f, -2f)
		shape5.addBox(0f, 0f, 0f, 2, 1, 4, 0f)
		shape4 = ModelRenderer(this, 0, 8)
		shape4.setRotationPoint(2f, 11f, -2f)
		shape4.addBox(0f, 0f, 0f, 2, 1, 4, 0f)
		shape6 = ModelRenderer(this, 0, 0)
		shape6.setRotationPoint(0f, 11f, 0f)
		shape6.addBox(-1f, 2f, 7f, 2, 3, 1, 0f)
		shape7 = ModelRenderer(this, 0, 3)
		shape7.setRotationPoint(0f, 11f, 0f)
		shape7.addBox(-1f, 1f, 6f, 2, 2, 1, 0f)
		shape8 = ModelRenderer(this, 0, 6)
		shape8.setRotationPoint(0f, 11f, 0f)
		shape8.addBox(-1f, 0f, 5f, 2, 2, 1, 0f)
		shape12 = ModelRenderer(this, 0, 6)
		shape12.setRotationPoint(0f, 11f, 0f)
		shape12.addBox(-1f, 0f, 5f, 2, 2, 1, 0f)
		setRotateAngle(shape12, 0f, 1.5707963267948966f, 0f)
		shape14 = ModelRenderer(this, 0, 0)
		shape14.setRotationPoint(0f, 11f, 0f)
		shape14.addBox(-1f, 2f, 7f, 2, 3, 1, 0f)
		setRotateAngle(shape14, 0f, 3.141592653589793f, 0f)
		shape20 = ModelRenderer(this, 0, 6)
		shape20.setRotationPoint(0f, 11f, 0f)
		shape20.addBox(-1f, 0f, 5f, 2, 2, 1, 0f)
		setRotateAngle(shape20, 0f, -1.5707963267948966f, 0f)
		shape11 = ModelRenderer(this, 0, 3)
		shape11.setRotationPoint(0f, 11f, 0f)
		shape11.addBox(-1f, 1f, 6f, 2, 2, 1, 0f)
		setRotateAngle(shape11, 0f, 1.5707963267948966f, 0f)
		shape21 = ModelRenderer(this, 0, 9)
		shape21.setRotationPoint(0f, 11f, 0f)
		shape21.addBox(-1f, 0f, 4f, 2, 1, 1, 0f)
		setRotateAngle(shape21, 0f, -1.5707963267948966f, 0f)
		shape2 = ModelRenderer(this, -2, 0)
		shape2.setRotationPoint(-4f, 11f, 2f)
		shape2.addBox(0f, 0f, 0f, 8, 1, 2, 0f)
		shape10 = ModelRenderer(this, 0, 0)
		shape10.setRotationPoint(0f, 11f, 0f)
		shape10.addBox(-1f, 2f, 7f, 2, 3, 1, 0f)
		setRotateAngle(shape10, 0f, 1.5707963267948966f, 0f)
		shape15 = ModelRenderer(this, 0, 3)
		shape15.setRotationPoint(0f, 11f, 0f)
		shape15.addBox(-1f, 1f, 6f, 2, 2, 1, 0f)
		setRotateAngle(shape15, 0f, 3.141592653589793f, 0f)
		shape13 = ModelRenderer(this, 0, 9)
		shape13.mirror = true
		shape13.setRotationPoint(0f, 11f, 0f)
		shape13.addBox(-1f, 0f, 4f, 2, 1, 1, 0f)
		setRotateAngle(shape13, 0f, 1.5707963267948966f, 0f)
		shape18 = ModelRenderer(this, 0, 0)
		shape18.setRotationPoint(0f, 11f, 0f)
		shape18.addBox(-1f, 2f, 7f, 2, 3, 1, 0f)
		setRotateAngle(shape18, 0f, -1.5707963267948966f, 0f)
		shape19 = ModelRenderer(this, 0, 3)
		shape19.setRotationPoint(0f, 11f, 0f)
		shape19.addBox(-1f, 1f, 6f, 2, 2, 1, 0f)
		setRotateAngle(shape19, 0f, -1.5707963267948966f, 0f)
	}
	
	fun renderAll() {
		shape17.render(0.0625f)
		shape3.render(0.0625f)
		shape9.render(0.0625f)
		shape16.render(0.0625f)
		shape5.render(0.0625f)
		shape4.render(0.0625f)
		shape6.render(0.0625f)
		shape7.render(0.0625f)
		shape8.render(0.0625f)
		shape12.render(0.0625f)
		shape14.render(0.0625f)
		shape20.render(0.0625f)
		shape11.render(0.0625f)
		shape21.render(0.0625f)
		shape2.render(0.0625f)
		shape10.render(0.0625f)
		shape15.render(0.0625f)
		shape13.render(0.0625f)
		shape18.render(0.0625f)
		shape19.render(0.0625f)
	}
	
	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	fun setRotateAngle(modelRenderer: ModelRenderer, x: Float, y: Float, z: Float) {
		modelRenderer.rotateAngleX = x
		modelRenderer.rotateAngleY = y
		modelRenderer.rotateAngleZ = z
	}
}
