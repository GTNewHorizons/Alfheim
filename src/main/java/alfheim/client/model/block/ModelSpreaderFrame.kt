package alfheim.client.model.block

import net.minecraft.client.model.*

object ModelSpreaderFrame: ModelBase() {
	
	var cubeHole5: ModelRenderer
	var cubeHole6: ModelRenderer
	var cubeHole7: ModelRenderer
	var cubeHole8: ModelRenderer
	var cubeHole9: ModelRenderer
	var cubeHole10: ModelRenderer
	var cubeHole11: ModelRenderer
	var cubeHole12: ModelRenderer
	var cubeHole1: ModelRenderer
	var cubeHole2: ModelRenderer
	var cubeHole3: ModelRenderer
	var cubeHole4: ModelRenderer
	var cubeHole13: ModelRenderer
	var cubeHole14: ModelRenderer
	var cubeHole15: ModelRenderer
	var cubeHole16: ModelRenderer
	
	init {
		textureWidth = 4
		textureHeight = 16
		cubeHole7 = ModelRenderer(this, 0, 0)
		cubeHole7.setRotationPoint(6.0f, 10.0f, -6.0f)
		cubeHole7.addBox(0.0f, 0.0f, 0.0f, 1, 12, 1, 0.0f)
		setRotateAngle(cubeHole7, 1.5707963267948966f, 0.0f, 0.0f)
		cubeHole1 = ModelRenderer(this, 0, 0)
		cubeHole1.setRotationPoint(6.0f, 9.0f, 6.0f)
		cubeHole1.addBox(0.0f, 0.0f, 0.0f, 1, 14, 1, 0.0f)
		cubeHole2 = ModelRenderer(this, 0, 0)
		cubeHole2.setRotationPoint(-7.0f, 9.0f, 6.0f)
		cubeHole2.addBox(0.0f, 0.0f, 0.0f, 1, 14, 1, 0.0f)
		cubeHole4 = ModelRenderer(this, 0, 0)
		cubeHole4.setRotationPoint(6.0f, 9.0f, -7.0f)
		cubeHole4.addBox(0.0f, 0.0f, 0.0f, 1, 14, 1, 0.0f)
		cubeHole12 = ModelRenderer(this, 0, 0)
		cubeHole12.setRotationPoint(6.0f, 22.0f, 6.0f)
		cubeHole12.addBox(0.0f, 0.0f, 0.0f, 1, 12, 1, 0.0f)
		setRotateAngle(cubeHole12, 0.0f, 0.0f, 1.5707963267948966f)
		cubeHole8 = ModelRenderer(this, 0, 0)
		cubeHole8.setRotationPoint(6.0f, 9.0f, 6.0f)
		cubeHole8.addBox(0.0f, 0.0f, 0.0f, 1, 12, 1, 0.0f)
		setRotateAngle(cubeHole8, 0.0f, 0.0f, 1.5707963267948966f)
		cubeHole3 = ModelRenderer(this, 0, 0)
		cubeHole3.setRotationPoint(-7.0f, 9.0f, -7.0f)
		cubeHole3.addBox(0.0f, 0.0f, 0.0f, 1, 14, 1, 0.0f)
		cubeHole9 = ModelRenderer(this, 0, 0)
		cubeHole9.setRotationPoint(-7.0f, 22.0f, 6.0f)
		cubeHole9.addBox(0.0f, 0.0f, 0.0f, 1, 12, 1, 0.0f)
		setRotateAngle(cubeHole9, -1.5707963267948966f, 0.0f, 0.0f)
		cubeHole6 = ModelRenderer(this, 0, 0)
		cubeHole6.setRotationPoint(-6.0f, 10.0f, -7.0f)
		cubeHole6.addBox(0.0f, 0.0f, 0.0f, 1, 12, 1, 0.0f)
		setRotateAngle(cubeHole6, 0.0f, 0.0f, -1.5707963267948966f)
		cubeHole13 = ModelRenderer(this, 0, 4)
		cubeHole13.setRotationPoint(-2.5f, 14.5f, 5.0f)
		cubeHole13.addBox(0.0f, 0.0f, 0.0f, 1, 5, 2, 0.0f)
		setRotateAngle(cubeHole13, 0.0f, 0.0f, -1.5707963267948966f)
		cubeHole14 = ModelRenderer(this, 0, 4)
		cubeHole14.setRotationPoint(-2.5f, 18.5f, 5.0f)
		cubeHole14.addBox(0.0f, 0.0f, 0.0f, 1, 5, 2, 0.0f)
		setRotateAngle(cubeHole14, 0.0f, 0.0f, -1.5707963267948966f)
		cubeHole10 = ModelRenderer(this, 0, 0)
		cubeHole10.setRotationPoint(-6.0f, 23.0f, -7.0f)
		cubeHole10.addBox(0.0f, 0.0f, 0.0f, 1, 12, 1, 0.0f)
		setRotateAngle(cubeHole10, 0.0f, 0.0f, -1.5707963267948966f)
		cubeHole5 = ModelRenderer(this, 0, 0)
		cubeHole5.setRotationPoint(-7.0f, 9.0f, 6.0f)
		cubeHole5.addBox(0.0f, 0.0f, 0.0f, 1, 12, 1, 0.0f)
		setRotateAngle(cubeHole5, -1.5707963267948966f, 0.0f, 0.0f)
		cubeHole15 = ModelRenderer(this, 0, 4)
		cubeHole15.setRotationPoint(-2.5f, 14.5f, 5.0f)
		cubeHole15.addBox(0.0f, 0.0f, 0.0f, 1, 3, 2, 0.0f)
		cubeHole16 = ModelRenderer(this, 0, 4)
		cubeHole16.setRotationPoint(1.5f, 14.5f, 5.0f)
		cubeHole16.addBox(0.0f, 0.0f, 0.0f, 1, 3, 2, 0.0f)
		cubeHole11 = ModelRenderer(this, 0, 0)
		cubeHole11.setRotationPoint(6.0f, 23.0f, -6.0f)
		cubeHole11.addBox(0.0f, 0.0f, 0.0f, 1, 12, 1, 0.0f)
		setRotateAngle(cubeHole11, 1.5707963267948966f, 0.0f, 0.0f)
	}
	
	fun render() {
		val f5 = 1f / 16f
		
		cubeHole1.render(f5)
		cubeHole2.render(f5)
		cubeHole3.render(f5)
		cubeHole4.render(f5)
		cubeHole5.render(f5)
		cubeHole6.render(f5)
		cubeHole7.render(f5)
		cubeHole8.render(f5)
		cubeHole9.render(f5)
		cubeHole10.render(f5)
		cubeHole11.render(f5)
		cubeHole12.render(f5)
		cubeHole13.render(f5)
		cubeHole14.render(f5)
		cubeHole15.render(f5)
		cubeHole16.render(f5)
	}
	
	fun setRotateAngle(modelRenderer: ModelRenderer, x: Float, y: Float, z: Float) {
		modelRenderer.rotateAngleX = x
		modelRenderer.rotateAngleY = y
		modelRenderer.rotateAngleZ = z
	}
}