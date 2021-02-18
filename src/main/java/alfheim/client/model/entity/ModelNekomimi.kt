package alfheim.client.model.entity

import net.minecraft.client.model.*

/**
 * ModelBiped - Either Mojang or a mod author
 * Created using Tabula 4.1.1
 */
object ModelNekomimi: ModelBase() {
	
	var shape9: ModelRenderer
	var shape10: ModelRenderer
	var shape11: ModelRenderer
	var shape12: ModelRenderer
	var shape13: ModelRenderer
	var shape14: ModelRenderer
	var shape15: ModelRenderer
	var shape16: ModelRenderer
	var shape17: ModelRenderer
	var shape18: ModelRenderer
	var shape19: ModelRenderer
	var shape20: ModelRenderer
	var shape21: ModelRenderer
	
	init {
		textureWidth = 36
		textureHeight = 14
		
		shape19 = ModelRenderer(this, 20, 6)
		shape19.setRotationPoint(-1.5f, -1.0f, -0.2f)
		shape19.addBox(0.0f, 0.0f, 0.0f, 3, 1, 1, 0.0f)
		shape11 = ModelRenderer(this, 0, 0)
		shape11.setRotationPoint(-0.5f, -0.5f, 3.0f)
		shape11.addBox(0.0f, 0.0f, 0.0f, 4, 4, 6, 0.0f)
		setRotateAngle(shape11, -0.4553564018453205f, 0.0f, 0.0f)
		shape14 = ModelRenderer(this, 14, 0)
		shape14.setRotationPoint(3.0f, -10.0f, -2.0f)
		shape14.addBox(0.0f, 0.0f, 0.0f, 1, 2, 1, 0.0f)
		shape15 = ModelRenderer(this, 14, 0)
		shape15.setRotationPoint(-4.0f, -10.0f, -2.0f)
		shape15.addBox(0.0f, 0.0f, 0.0f, 1, 2, 1, 0.0f)
		shape10 = ModelRenderer(this, 16, 0)
		shape10.setRotationPoint(-0.5f, -0.5f, 2.0f)
		shape10.addBox(0.0f, 0.0f, 0.0f, 3, 3, 3, 0.0f)
		setRotateAngle(shape10, -0.41015237421866746f, 0.0f, 0.0f)
		shape21 = ModelRenderer(this, 0, 0)
		shape21.setRotationPoint(2.0f, 0.0f, 0.0f)
		shape21.addBox(0.0f, 0.0f, 0.0f, 1, 2, 1, 0.0f)
		shape9 = ModelRenderer(this, 28, 4)
		shape9.setRotationPoint(-1.5f, 10.0f, 2.0f)
		shape9.addBox(0.0f, 0.0f, 0.0f, 2, 2, 2, 0.0f)
		setRotateAngle(shape9, -0.5235987755982988f, 0.0f, 0.0f)
		shape17 = ModelRenderer(this, 9, 10)
		shape17.mirror = true
		shape17.setRotationPoint(1.5f, 1.0f, 0.0f)
		shape17.addBox(0.0f, 0.0f, 0.0f, 1, 3, 1, 0.0f)
		shape20 = ModelRenderer(this, 9, 10)
		shape20.setRotationPoint(0.5f, 1.0f, 0.0f)
		shape20.addBox(0.0f, 0.0f, 0.0f, 1, 3, 1, 0.0f)
		shape12 = ModelRenderer(this, 21, 9)
		shape12.setRotationPoint(0.5f, 0.5f, 5.0f)
		shape12.addBox(0.0f, 0.0f, 0.0f, 3, 3, 2, 0.0f)
		setRotateAngle(shape12, 0.18203784098300857f, 0.0f, 0.0f)
		shape18 = ModelRenderer(this, 0, 0)
		shape18.setRotationPoint(-2.0f, 0.0f, 0.0f)
		shape18.addBox(0.0f, 0.0f, 0.0f, 1, 2, 1, 0.0f)
		shape13 = ModelRenderer(this, 28, 0)
		shape13.setRotationPoint(0.5f, 0.5f, 1.1f)
		shape13.addBox(0.0f, 0.0f, 0.0f, 2, 2, 2, 0.0f)
		setRotateAngle(shape13, 0.136659280431156f, 0.0f, 0.0f)
		shape16 = ModelRenderer(this, 20, 6)
		shape16.mirror = true
		shape16.setRotationPoint(-0.5f, -1.0f, -0.2f)
		shape16.addBox(0.0f, 0.0f, 0.0f, 3, 1, 1, 0.0f)
		shape15.addChild(shape19)
		shape10.addChild(shape11)
		shape9.addChild(shape10)
		shape20.addChild(shape21)
		shape16.addChild(shape17)
		shape19.addChild(shape20)
		shape11.addChild(shape12)
		shape17.addChild(shape18)
		shape12.addChild(shape13)
		shape14.addChild(shape16)
	}
	
	fun renderTail(f: Float) {
		shape9.render(f)
	}
	
	fun renderMimi(f: Float) {
		shape14.render(f)
		shape15.render(f)
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