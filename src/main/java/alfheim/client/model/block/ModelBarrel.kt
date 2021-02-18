package alfheim.client.model.block

import net.minecraft.client.model.*
import net.minecraft.entity.Entity

class ModelBarrel: ModelBase() {
	
	var Shape0: ModelRenderer
	var Shape1: ModelRenderer
	var Shape2: ModelRenderer
	var Shape3: ModelRenderer
	var Shape4: ModelRenderer
	var Shape5: ModelRenderer
	var Shape6: ModelRenderer
	var Shape7: ModelRenderer
	var Shape8: ModelRenderer
	var Shape9: ModelRenderer
	var Shape10: ModelRenderer
	var Shape11: ModelRenderer
	var Shape12: ModelRenderer
	var Shape13: ModelRenderer
	var Shape14: ModelRenderer
	var Shape15: ModelRenderer
	var Shape16: ModelRenderer
	var Shape17: ModelRenderer
	var Shape18: ModelRenderer
	var Shape19: ModelRenderer
	var Shape20: ModelRenderer
	var Shape21: ModelRenderer
	var Shape22: ModelRenderer
	var Shape23: ModelRenderer
	var Shape24: ModelRenderer
	var Shape25: ModelRenderer
	var Shape26: ModelRenderer
	var Shape27: ModelRenderer
	var Shape28: ModelRenderer
	var Shape29: ModelRenderer
	var Shape30: ModelRenderer
	var Shape31: ModelRenderer
	var Shape32: ModelRenderer
	var Shape33: ModelRenderer
	var Shape34: ModelRenderer
	var Shape35: ModelRenderer
	var Shape36: ModelRenderer
	var Shape37: ModelRenderer
	var Shape38: ModelRenderer
	var Shape39: ModelRenderer
	var cover: ModelRenderer
	var bottom: ModelRenderer
	var handle: ModelRenderer
	var redMash: ModelRenderer
	var greenMash: ModelRenderer
	var redWine: ModelRenderer
	var greenWine: ModelRenderer
	
	init {
		val h = 25
		
		textureWidth = 84
		textureHeight = h
		Shape0 = ModelRenderer(this, 0, 0)
		Shape0.addBox(-3f, 12f, -7f, 6, 4, 1)
		Shape0.setRotationPoint(0f, 0f, 0f)
		Shape0.setTextureSize(84, h)
		Shape0.mirror = true
		setRotation(Shape0, 0f, -1.570796f, 0f)
		Shape1 = ModelRenderer(this, 0, 0)
		Shape1.addBox(-3f, 12f, -7f, 6, 4, 1)
		Shape1.setRotationPoint(0f, 0f, 0f)
		Shape1.setTextureSize(84, h)
		Shape1.mirror = true
		setRotation(Shape1, 0f, -3.141593f, 0f)
		Shape2 = ModelRenderer(this, 0, 0)
		Shape2.addBox(-3f, 12f, -7f, 6, 4, 1)
		Shape2.setRotationPoint(0f, 0f, 0f)
		Shape2.setTextureSize(84, h)
		Shape2.mirror = true
		setRotation(Shape2, 0f, 1.570796f, 0f)
		Shape3 = ModelRenderer(this, 0, 0)
		Shape3.addBox(-3f, 12f, -7f, 6, 4, 1)
		Shape3.setRotationPoint(0f, 0f, 0f)
		Shape3.setTextureSize(84, h)
		Shape3.mirror = true
		setRotation(Shape3, 0f, 0f, 0f)
		Shape4 = ModelRenderer(this, 0, 0)
		Shape4.addBox(-3f, 0f, -7f, 6, 4, 1)
		Shape4.setRotationPoint(0f, 0f, 0f)
		Shape4.setTextureSize(84, h)
		Shape4.mirror = true
		setRotation(Shape4, 0f, 0f, 0f)
		Shape5 = ModelRenderer(this, 0, 0)
		Shape5.addBox(-3f, 0f, -7f, 6, 4, 1)
		Shape5.setRotationPoint(0f, 0f, 0f)
		Shape5.setTextureSize(84, h)
		Shape5.mirror = true
		setRotation(Shape5, 0f, 1.570796f, 0f)
		Shape6 = ModelRenderer(this, 0, 0)
		Shape6.addBox(-3f, 0f, -7f, 6, 4, 1)
		Shape6.setRotationPoint(0f, 0f, 0f)
		Shape6.setTextureSize(84, h)
		Shape6.mirror = true
		setRotation(Shape6, 0f, -3.141593f, 0f)
		Shape7 = ModelRenderer(this, 0, 0)
		Shape7.addBox(-3f, 0f, -7f, 6, 4, 1)
		Shape7.setRotationPoint(0f, 0f, 0f)
		Shape7.setTextureSize(84, h)
		Shape7.mirror = true
		setRotation(Shape7, 0f, -1.570796f, 0f)
		Shape8 = ModelRenderer(this, 0, 5)
		Shape8.addBox(3f, 12f, -6f, 2, 4, 1)
		Shape8.setRotationPoint(0f, 0f, 0f)
		Shape8.setTextureSize(84, h)
		Shape8.mirror = true
		setRotation(Shape8, 0f, -1.570796f, 0f)
		Shape9 = ModelRenderer(this, 0, 5)
		Shape9.addBox(-5f, 12f, -6f, 2, 4, 1)
		Shape9.setRotationPoint(0f, 0f, 0f)
		Shape9.setTextureSize(84, h)
		Shape9.mirror = true
		setRotation(Shape9, 0f, -1.570796f, 0f)
		Shape10 = ModelRenderer(this, 0, 5)
		Shape10.addBox(-5f, 12f, -6f, 2, 4, 1)
		Shape10.setRotationPoint(0f, 0f, 0f)
		Shape10.setTextureSize(84, h)
		Shape10.mirror = true
		setRotation(Shape10, 0f, -3.141593f, 0f)
		Shape11 = ModelRenderer(this, 0, 5)
		Shape11.addBox(3f, 12f, -6f, 2, 4, 1)
		Shape11.setRotationPoint(0f, 0f, 0f)
		Shape11.setTextureSize(84, h)
		Shape11.mirror = true
		setRotation(Shape11, 0f, -3.141593f, 0f)
		Shape12 = ModelRenderer(this, 0, 5)
		Shape12.addBox(-5f, 12f, -6f, 2, 4, 1)
		Shape12.setRotationPoint(0f, 0f, 0f)
		Shape12.setTextureSize(84, h)
		Shape12.mirror = true
		setRotation(Shape12, 0f, 1.570796f, 0f)
		Shape13 = ModelRenderer(this, 0, 5)
		Shape13.addBox(3f, 12f, -6f, 2, 4, 1)
		Shape13.setRotationPoint(0f, 0f, 0f)
		Shape13.setTextureSize(84, h)
		Shape13.mirror = true
		setRotation(Shape13, 0f, 1.570796f, 0f)
		Shape14 = ModelRenderer(this, 0, 5)
		Shape14.addBox(-5f, 12f, -6f, 2, 4, 1)
		Shape14.setRotationPoint(0f, 0f, 0f)
		Shape14.setTextureSize(84, h)
		Shape14.mirror = true
		setRotation(Shape14, 0f, 0f, 0f)
		Shape15 = ModelRenderer(this, 0, 5)
		Shape15.addBox(3f, 12f, -6f, 2, 4, 1)
		Shape15.setRotationPoint(0f, 0f, 0f)
		Shape15.setTextureSize(84, h)
		Shape15.mirror = true
		setRotation(Shape15, 0f, 0f, 0f)
		Shape16 = ModelRenderer(this, 28, 0)
		Shape16.addBox(-5f, 4f, -7f, 2, 8, 1)
		Shape16.setRotationPoint(0f, 0f, 0f)
		Shape16.setTextureSize(84, h)
		Shape16.mirror = true
		setRotation(Shape16, 0f, -1.570796f, 0f)
		Shape17 = ModelRenderer(this, 28, 0)
		Shape17.addBox(3f, 4f, -7f, 2, 8, 1)
		Shape17.setRotationPoint(0f, 0f, 0f)
		Shape17.setTextureSize(84, h)
		Shape17.mirror = true
		setRotation(Shape17, 0f, -1.570796f, 0f)
		Shape18 = ModelRenderer(this, 28, 0)
		Shape18.addBox(-5f, 4f, -7f, 2, 8, 1)
		Shape18.setRotationPoint(0f, 0f, 0f)
		Shape18.setTextureSize(84, h)
		Shape18.mirror = true
		setRotation(Shape18, 0f, -3.141593f, 0f)
		Shape19 = ModelRenderer(this, 28, 0)
		Shape19.addBox(3f, 4f, -7f, 2, 8, 1)
		Shape19.setRotationPoint(0f, 0f, 0f)
		Shape19.setTextureSize(84, h)
		Shape19.mirror = true
		setRotation(Shape19, 0f, -3.141593f, 0f)
		Shape20 = ModelRenderer(this, 28, 0)
		Shape20.addBox(-5f, 4f, -7f, 2, 8, 1)
		Shape20.setRotationPoint(0f, 0f, 0f)
		Shape20.setTextureSize(84, h)
		Shape20.mirror = true
		setRotation(Shape20, 0f, 1.570796f, 0f)
		Shape21 = ModelRenderer(this, 28, 0)
		Shape21.addBox(3f, 4f, -7f, 2, 8, 1)
		Shape21.setRotationPoint(0f, 0f, 0f)
		Shape21.setTextureSize(84, h)
		Shape21.mirror = true
		setRotation(Shape21, 0f, 1.570796f, 0f)
		Shape22 = ModelRenderer(this, 28, 0)
		Shape22.addBox(-5f, 4f, -7f, 2, 8, 1)
		Shape22.setRotationPoint(0f, 0f, 0f)
		Shape22.setTextureSize(84, h)
		Shape22.mirror = true
		setRotation(Shape22, 0f, 0f, 0f)
		Shape23 = ModelRenderer(this, 28, 0)
		Shape23.addBox(3f, 4f, -7f, 2, 8, 1)
		Shape23.setRotationPoint(0f, 0f, 0f)
		Shape23.setTextureSize(84, h)
		Shape23.mirror = true
		setRotation(Shape23, 0f, 0f, 0f)
		Shape24 = ModelRenderer(this, 14, 0)
		Shape24.addBox(-3f, 4f, -8f, 6, 8, 1)
		Shape24.setRotationPoint(0f, 0f, 0f)
		Shape24.setTextureSize(84, h)
		Shape24.mirror = true
		setRotation(Shape24, 0f, -1.570796f, 0f)
		Shape25 = ModelRenderer(this, 14, 0)
		Shape25.addBox(-3f, 4f, -8f, 6, 8, 1)
		Shape25.setRotationPoint(0f, 0f, 0f)
		Shape25.setTextureSize(84, h)
		Shape25.mirror = true
		setRotation(Shape25, 0f, -3.141593f, 0f)
		Shape26 = ModelRenderer(this, 14, 0)
		Shape26.addBox(-3f, 4f, -8f, 6, 8, 1)
		Shape26.setRotationPoint(0f, 0f, 0f)
		Shape26.setTextureSize(84, h)
		Shape26.mirror = true
		setRotation(Shape26, 0f, 1.570796f, 0f)
		Shape27 = ModelRenderer(this, 14, 0)
		Shape27.addBox(-3f, 4f, -8f, 6, 8, 1)
		Shape27.setRotationPoint(0f, 0f, 0f)
		Shape27.setTextureSize(84, h)
		Shape27.mirror = true
		setRotation(Shape27, 0f, 0f, 0f)
		Shape28 = ModelRenderer(this, 0, 5)
		Shape28.addBox(-5f, 0f, -6f, 2, 4, 1)
		Shape28.setRotationPoint(0f, 0f, 0f)
		Shape28.setTextureSize(84, h)
		Shape28.mirror = true
		setRotation(Shape28, 0f, 0f, 0f)
		Shape29 = ModelRenderer(this, 0, 5)
		Shape29.addBox(3f, 0f, -6f, 2, 4, 1)
		Shape29.setRotationPoint(0f, 0f, 0f)
		Shape29.setTextureSize(84, h)
		Shape29.mirror = true
		setRotation(Shape29, 0f, 1.570796f, 0f)
		Shape30 = ModelRenderer(this, 0, 5)
		Shape30.addBox(3f, 0f, -6f, 2, 4, 1)
		Shape30.setRotationPoint(0f, 0f, 0f)
		Shape30.setTextureSize(84, h)
		Shape30.mirror = true
		setRotation(Shape30, 0f, -3.141593f, 0f)
		Shape31 = ModelRenderer(this, 0, 5)
		Shape31.addBox(-5f, 0f, -6f, 2, 4, 1)
		Shape31.setRotationPoint(0f, 0f, 0f)
		Shape31.setTextureSize(84, h)
		Shape31.mirror = true
		setRotation(Shape31, 0f, 1.570796f, 0f)
		Shape32 = ModelRenderer(this, 0, 5)
		Shape32.addBox(-5f, 0f, -6f, 2, 4, 1)
		Shape32.setRotationPoint(0f, 0f, 0f)
		Shape32.setTextureSize(84, h)
		Shape32.mirror = true
		setRotation(Shape32, 0f, -3.141593f, 0f)
		Shape33 = ModelRenderer(this, 0, 5)
		Shape33.addBox(3f, 0f, -6f, 2, 4, 1)
		Shape33.setRotationPoint(0f, 0f, 0f)
		Shape33.setTextureSize(84, h)
		Shape33.mirror = true
		setRotation(Shape33, 0f, -1.570796f, 0f)
		Shape34 = ModelRenderer(this, 0, 5)
		Shape34.addBox(3f, 0f, -6f, 2, 4, 1)
		Shape34.setRotationPoint(0f, 0f, 0f)
		Shape34.setTextureSize(84, h)
		Shape34.mirror = true
		setRotation(Shape34, 0f, 0f, 0f)
		Shape35 = ModelRenderer(this, 0, 5)
		Shape35.addBox(-5f, 0f, -6f, 2, 4, 1)
		Shape35.setRotationPoint(0f, 0f, 0f)
		Shape35.setTextureSize(84, h)
		Shape35.mirror = true
		setRotation(Shape35, 0f, -1.570796f, 0f)
		Shape36 = ModelRenderer(this, 34, 0)
		Shape36.addBox(5f, 4f, -6f, 1, 8, 1)
		Shape36.setRotationPoint(0f, 0f, 0f)
		Shape36.setTextureSize(84, h)
		Shape36.mirror = true
		setRotation(Shape36, 0f, -1.570796f, 0f)
		Shape37 = ModelRenderer(this, 34, 0)
		Shape37.addBox(5f, 4f, -6f, 1, 8, 1)
		Shape37.setRotationPoint(0f, 0f, 0f)
		Shape37.setTextureSize(84, h)
		Shape37.mirror = true
		setRotation(Shape37, 0f, -3.141593f, 0f)
		Shape38 = ModelRenderer(this, 34, 0)
		Shape38.addBox(5f, 4f, -6f, 1, 8, 1)
		Shape38.setRotationPoint(0f, 0f, 0f)
		Shape38.setTextureSize(84, h)
		Shape38.mirror = true
		setRotation(Shape38, 0f, 1.570796f, 0f)
		Shape39 = ModelRenderer(this, 34, 0)
		Shape39.addBox(5f, 4f, -6f, 1, 8, 1)
		Shape39.setRotationPoint(0f, 0f, 0f)
		Shape39.setTextureSize(84, h)
		Shape39.mirror = true
		setRotation(Shape39, 0f, 0f, 0f)
		cover = ModelRenderer(this, -14, 10)
		cover.addBox(-7f, 1f, -7f, 14, 1, 14)
		cover.setRotationPoint(0f, 0f, 0f)
		cover.setTextureSize(84, h)
		cover.mirror = true
		setRotation(cover, 0f, 0f, 0f)
		bottom = ModelRenderer(this, -14, 10)
		bottom.addBox(-7f, 14f, -7f, 14, 1, 14)
		bottom.setRotationPoint(0f, 0f, 0f)
		bottom.setTextureSize(84, h)
		bottom.mirror = true
		setRotation(bottom, 0f, 0f, 0f)
		handle = ModelRenderer(this, 38, 0)
		handle.addBox(-1.5f, 0f, -0.5f, 3, 1, 1)
		handle.setRotationPoint(0f, 0f, 0f)
		handle.setTextureSize(84, h)
		handle.mirror = true
		setRotation(handle, 0f, 0f, 0f)
		
		redMash = ModelRenderer(this, 14, 10)
		redMash.addBox(-7f, 13f, -7f, 14, 1, 14)
		redMash.setRotationPoint(0f, 0f, 0f)
		redMash.setTextureSize(84, h)
		redMash.mirror = true
		setRotation(redMash, 0f, 0f, 0f)
		
		greenMash = ModelRenderer(this, 28, 10)
		greenMash.addBox(-7f, 13f, -7f, 14, 1, 14)
		greenMash.setRotationPoint(0f, 0f, 0f)
		greenMash.setTextureSize(84, h)
		greenMash.mirror = true
		setRotation(greenMash, 0f, 0f, 0f)
		
		redWine = ModelRenderer(this, 42, 10)
		redWine.addBox(-7f, 13f, -7f, 14, 1, 14)
		redWine.setRotationPoint(0f, 0f, 0f)
		redWine.setTextureSize(84, h)
		redWine.mirror = true
		setRotation(redWine, 0f, 0f, 0f)
		
		greenWine = ModelRenderer(this, 56, 10)
		greenWine.addBox(-7f, 13f, -7f, 14, 1, 14)
		greenWine.setRotationPoint(0f, 0f, 0f)
		greenWine.setTextureSize(84, h)
		greenWine.mirror = true
		setRotation(greenWine, 0f, 0f, 0f)
	}
	
	fun render(f5: Float) {
		Shape0.render(f5)
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
		Shape19.render(f5)
		Shape20.render(f5)
		Shape21.render(f5)
		Shape22.render(f5)
		Shape23.render(f5)
		Shape24.render(f5)
		Shape25.render(f5)
		Shape26.render(f5)
		Shape27.render(f5)
		Shape28.render(f5)
		Shape29.render(f5)
		Shape30.render(f5)
		Shape31.render(f5)
		Shape32.render(f5)
		Shape33.render(f5)
		Shape34.render(f5)
		Shape35.render(f5)
		Shape36.render(f5)
		Shape37.render(f5)
		Shape38.render(f5)
		Shape39.render(f5)
		
		bottom.render(f5)
	}
	
	fun renderCover(f5: Float) {
		cover.render(f5)
		handle.render(f5)
	}
	
	override fun render(entity: Entity, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
		super.render(entity, f, f1, f2, f3, f4, f5)
		setRotationAngles(f, f1, f2, f3, f4, f5, entity)
		render(f5)
	}
	
	fun setRotation(model: ModelRenderer, x: Float, y: Float, z: Float) {
		model.rotateAngleX = x
		model.rotateAngleY = y
		model.rotateAngleZ = z
	}
}