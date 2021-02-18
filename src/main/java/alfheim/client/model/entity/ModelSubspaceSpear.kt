package alfheim.client.model.entity

import net.minecraft.client.model.*

/**
 * @author ExtraMeteorP, CKATEPTb
 */
object ModelSubspaceSpear: ModelBase() {
	
	val Shape1: ModelRenderer
	val Shape2: ModelRenderer
	val Shape3: ModelRenderer
	val Shape4: ModelRenderer
	val Shape5: ModelRenderer
	val Shape6: ModelRenderer
	val Shape7: ModelRenderer
	val Shape9: ModelRenderer
	val Shape11: ModelRenderer
	val Shape10: ModelRenderer
	val Shape12: ModelRenderer
	val Shape13: ModelRenderer
	val Shape14: ModelRenderer
	val Shape15: ModelRenderer
	val Shape16: ModelRenderer
	val Shape17: ModelRenderer
	val Shape18: ModelRenderer
	
	init {
		textureWidth = 64
		textureHeight = 32
		
		Shape1 = ModelRenderer(this, 10, 0)
		Shape1.addBox(0f, -0.5f, 0f, 2, 3, 2)
		Shape1.setRotationPoint(0f, 0f, 0f)
		Shape1.setTextureSize(64, 32)
		Shape1.mirror = true
		setRotation(Shape1, 0f, 0f, 0f)
		Shape2 = ModelRenderer(this, 10, 0)
		Shape2.addBox(0f, 0f, -0.5f, 2, 2, 3)
		Shape2.setRotationPoint(0f, 0f, 0f)
		Shape2.setTextureSize(64, 32)
		Shape2.mirror = true
		setRotation(Shape2, 0f, 0f, 0f)
		Shape3 = ModelRenderer(this, 10, 0)
		Shape3.addBox(-0.5f, 0f, 0f, 3, 2, 2)
		Shape3.setRotationPoint(0f, 0f, 0f)
		Shape3.setTextureSize(64, 32)
		Shape3.mirror = true
		setRotation(Shape3, 0f, 0f, 0f)
		Shape4 = ModelRenderer(this, 5, 0)
		Shape4.addBox(0f, -0.5f, -2f, 1, 2, 1)
		Shape4.setRotationPoint(0f, 0f, 0f)
		Shape4.setTextureSize(64, 32)
		Shape4.mirror = true
		setRotation(Shape4, -0.7853982f, 0.2617994f, 0f)
		Shape5 = ModelRenderer(this, 5, 0)
		Shape5.addBox(0f, 0f, -2.5f, 1, 4, 1)
		Shape5.setRotationPoint(0f, 0f, 0f)
		Shape5.setTextureSize(64, 32)
		Shape5.mirror = true
		setRotation(Shape5, 0.1919862f, 0.2617994f, 0f)
		Shape6 = ModelRenderer(this, 21, 0)
		Shape6.addBox(0f, -1.5f, 3f, 1, 4, 1)
		Shape6.setRotationPoint(0f, 0f, 0f)
		Shape6.setTextureSize(64, 32)
		Shape6.mirror = true
		setRotation(Shape6, 0.1919862f, 0.2617994f, 0f)
		Shape7 = ModelRenderer(this, 21, 0)
		Shape7.addBox(0f, -1.5f, 4f, 1, 2, 1)
		Shape7.setRotationPoint(0f, 0f, 0f)
		Shape7.setTextureSize(64, 32)
		Shape7.mirror = true
		setRotation(Shape7, -0.7853982f, 0.2617994f, 0f)
		Shape9 = ModelRenderer(this, 26, 0)
		Shape9.addBox(0f, 8f, 0f, 1, 10, 1)
		Shape9.setRotationPoint(0f, 0f, 0f)
		Shape9.setTextureSize(64, 32)
		Shape9.mirror = true
		setRotation(Shape9, 0f, 0f, 0f)
		Shape11 = ModelRenderer(this, 26, 0)
		Shape11.addBox(1f, 5f, 0f, 1, 9, 1)
		Shape11.setRotationPoint(0f, 0f, 0f)
		Shape11.setTextureSize(64, 32)
		Shape11.mirror = true
		setRotation(Shape11, 0f, 0f, 0f)
		Shape10 = ModelRenderer(this, 26, 0)
		Shape10.addBox(1f, 6f, 1f, 1, 9, 1)
		Shape10.setRotationPoint(0f, 0f, 0f)
		Shape10.setTextureSize(64, 32)
		Shape10.mirror = true
		setRotation(Shape10, 0f, 0f, 0f)
		Shape12 = ModelRenderer(this, 26, 0)
		Shape12.addBox(0f, 7f, 1f, 1, 7, 1)
		Shape12.setRotationPoint(0f, 0f, 0f)
		Shape12.setTextureSize(64, 32)
		Shape12.mirror = true
		setRotation(Shape12, 0f, 0f, 0f)
		Shape13 = ModelRenderer(this, 0, 0)
		Shape13.addBox(0f, -12f, 0f, 1, 10, 1)
		Shape13.setRotationPoint(0f, 0f, 0f)
		Shape13.setTextureSize(64, 32)
		Shape13.mirror = true
		setRotation(Shape13, 0f, 0f, 0f)
		Shape14 = ModelRenderer(this, 0, 0)
		Shape14.addBox(1f, -14f, 0f, 1, 10, 1)
		Shape14.setRotationPoint(0f, 0f, 0f)
		Shape14.setTextureSize(64, 32)
		Shape14.mirror = true
		setRotation(Shape14, 0f, 0f, 0f)
		Shape15 = ModelRenderer(this, 0, 0)
		Shape15.addBox(0f, -16f, 1f, 1, 13, 1)
		Shape15.setRotationPoint(0f, 0f, 0f)
		Shape15.setTextureSize(64, 32)
		Shape15.mirror = true
		setRotation(Shape15, 0f, 0f, 0f)
		Shape16 = ModelRenderer(this, 0, 0)
		Shape16.addBox(1f, -20f, 1f, 1, 14, 1)
		Shape16.setRotationPoint(0f, 0f, 0f)
		Shape16.setTextureSize(64, 32)
		Shape16.mirror = true
		setRotation(Shape16, 0f, 0f, 0f)
		Shape17 = ModelRenderer(this, 0, 0)
		Shape17.addBox(0.5f, -24f, 0.5f, 1, 13, 1)
		Shape17.setRotationPoint(0f, 0f, 0f)
		Shape17.setTextureSize(64, 32)
		Shape17.mirror = true
		setRotation(Shape17, 0f, 0f, 0f)
		Shape18 = ModelRenderer(this, 26, 0)
		Shape18.addBox(0.5f, 7f, 0.5f, 1, 16, 1)
		Shape18.setRotationPoint(0f, 0f, 0f)
		Shape18.setTextureSize(64, 32)
		Shape18.mirror = true
		setRotation(Shape18, 0f, 0f, 0f)
	}
	
	fun render(f5: Float) {
		Shape1.render(f5)
		Shape2.render(f5)
		Shape3.render(f5)
		Shape4.render(f5)
		Shape5.render(f5)
		Shape6.render(f5)
		Shape7.render(f5)
		Shape18.render(f5)
		Shape9.render(f5)
		Shape10.render(f5)
		Shape11.render(f5)
		Shape12.render(f5)
		Shape13.render(f5)
		Shape14.render(f5)
		Shape15.render(f5)
		Shape16.render(f5)
		Shape17.render(f5)
	}
	
	private fun setRotation(model: ModelRenderer, x: Float, y: Float, z: Float) {
		model.rotateAngleX = x
		model.rotateAngleY = y
		model.rotateAngleZ = z
	}
}