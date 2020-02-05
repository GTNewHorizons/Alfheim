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
	
	fun render() {
		val f = 1f / 16f
		cubeHole8.render(f)
		cubeHole11.render(f)
		cubeHole12.render(f)
		cubeHole5.render(f)
		cubeHole2.render(f)
		cubeHole3.render(f)
		cubeHole7.render(f)
		cubeHole10.render(f)
		cubeHole6.render(f)
		cubeHole4.render(f)
		cubeHole9.render(f)
		cubeHole1.render(f)
	}
	
	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	fun setRotateAngle(modelRenderer: ModelRenderer, x: Float, y: Float, z: Float) {
		modelRenderer.rotateAngleX = x
		modelRenderer.rotateAngleY = y
		modelRenderer.rotateAngleZ = z
	}
	
	init {
		textureWidth = 4
		textureHeight = 16
		cubeHole8 = ModelRenderer(this, 0, 0)
		cubeHole8.setRotationPoint(6.0f, 9.0f, 6.0f)
		cubeHole8.addBox(0.0f, 0.0f, 0.0f, 1, 12, 1, 0.0f)
		setRotateAngle(cubeHole8, 0.0f, 0.0f, 1.5707963267948966f)
		cubeHole11 = ModelRenderer(this, 0, 0)
		cubeHole11.setRotationPoint(6.0f, 23.0f, -6.0f)
		cubeHole11.addBox(0.0f, 0.0f, 0.0f, 1, 12, 1, 0.0f)
		setRotateAngle(cubeHole11, 1.5707963267948966f, 0.0f, 0.0f)
		cubeHole12 = ModelRenderer(this, 0, 0)
		cubeHole12.setRotationPoint(6.0f, 22.0f, 6.0f)
		cubeHole12.addBox(0.0f, 0.0f, 0.0f, 1, 12, 1, 0.0f)
		setRotateAngle(cubeHole12, 0.0f, 0.0f, 1.5707963267948966f)
		cubeHole5 = ModelRenderer(this, 0, 0)
		cubeHole5.setRotationPoint(-7.0f, 9.0f, 6.0f)
		cubeHole5.addBox(0.0f, 0.0f, 0.0f, 1, 12, 1, 0.0f)
		setRotateAngle(cubeHole5, -1.5707963267948966f, 0.0f, 0.0f)
		cubeHole2 = ModelRenderer(this, 0, 0)
		cubeHole2.setRotationPoint(-7.0f, 9.0f, 6.0f)
		cubeHole2.addBox(0.0f, 0.0f, 0.0f, 1, 14, 1, 0.0f)
		cubeHole3 = ModelRenderer(this, 0, 0)
		cubeHole3.setRotationPoint(-7.0f, 9.0f, -7.0f)
		cubeHole3.addBox(0.0f, 0.0f, 0.0f, 1, 14, 1, 0.0f)
		cubeHole7 = ModelRenderer(this, 0, 0)
		cubeHole7.setRotationPoint(6.0f, 10.0f, -6.0f)
		cubeHole7.addBox(0.0f, 0.0f, 0.0f, 1, 12, 1, 0.0f)
		setRotateAngle(cubeHole7, 1.5707963267948966f, 0.0f, 0.0f)
		cubeHole10 = ModelRenderer(this, 0, 0)
		cubeHole10.setRotationPoint(-6.0f, 23.0f, -7.0f)
		cubeHole10.addBox(0.0f, 0.0f, 0.0f, 1, 12, 1, 0.0f)
		setRotateAngle(cubeHole10, 0.0f, 0.0f, -1.5707963267948966f)
		cubeHole6 = ModelRenderer(this, 0, 0)
		cubeHole6.setRotationPoint(-6.0f, 10.0f, -7.0f)
		cubeHole6.addBox(0.0f, 0.0f, 0.0f, 1, 12, 1, 0.0f)
		setRotateAngle(cubeHole6, 0.0f, 0.0f, -1.5707963267948966f)
		cubeHole4 = ModelRenderer(this, 0, 0)
		cubeHole4.setRotationPoint(6.0f, 9.0f, -7.0f)
		cubeHole4.addBox(0.0f, 0.0f, 0.0f, 1, 14, 1, 0.0f)
		cubeHole9 = ModelRenderer(this, 0, 0)
		cubeHole9.setRotationPoint(-7.0f, 22.0f, 6.0f)
		cubeHole9.addBox(0.0f, 0.0f, 0.0f, 1, 12, 1, 0.0f)
		setRotateAngle(cubeHole9, -1.5707963267948966f, 0.0f, 0.0f)
		cubeHole1 = ModelRenderer(this, 0, 0)
		cubeHole1.setRotationPoint(6.0f, 9.0f, 6.0f)
		cubeHole1.addBox(0.0f, 0.0f, 0.0f, 1, 14, 1, 0.0f)
	}
}