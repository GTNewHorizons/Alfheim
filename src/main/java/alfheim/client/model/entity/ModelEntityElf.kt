package alfheim.client.model.entity

import alfheim.common.entity.EntityElf
import net.minecraft.client.model.*
import net.minecraft.entity.Entity

object ModelEntityElf: ModelBiped(0f, 0f, 128, 64) {
	
	var rightboot: ModelRenderer
	var leftboot: ModelRenderer
	var rightshoulder: ModelRenderer
	var leftshoulder: ModelRenderer
	var frontBelt: ModelRenderer
	var upperBelt: ModelRenderer
	var lowerBelt: ModelRenderer
	var backBag: ModelRenderer
	var frontBag: ModelRenderer
	var helmet: ModelRenderer
	var shape1: ModelRenderer
	var shape2: ModelRenderer
	var shape3: ModelRenderer
	var shape4: ModelRenderer
	var shape5: ModelRenderer
	var shape6: ModelRenderer
	var shape7: ModelRenderer
	var shape8: ModelRenderer
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
	
	init {
		bipedLeftArm = ModelRenderer(this, 40, 16)
		bipedLeftArm.mirror = true
		bipedLeftArm.setRotationPoint(5.0f, 2.0f, -0.0f)
		bipedLeftArm.addBox(-1.0f, -2.0f, -2.0f, 4, 12, 4, 0.0f)
		bipedRightLeg = ModelRenderer(this, 0, 16)
		bipedRightLeg.setRotationPoint(-2.0f, 12.0f, 0.1f)
		bipedRightLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, 0.0f)
		shape11 = ModelRenderer(this, 0, 42)
		shape11.setRotationPoint(0.0f, 0.0f, 0.0f)
		shape11.addBox(-5.0f, -11.199999809265137f, -4.0f, 1, 6, 1, 0.0f)
		setRotateAngle(shape11, -0.5235987901687622f, 0.0f, 0.0f)
		shape12 = ModelRenderer(this, 0, 42)
		shape12.setRotationPoint(0.0f, 0.0f, 0.0f)
		shape12.addBox(-5.0f, -12.0f, -5.0f, 1, 7, 1, 0.0f)
		setRotateAngle(shape12, -0.5235987901687622f, 0.0f, 0.0f)
		shape13 = ModelRenderer(this, 0, 42)
		shape13.setRotationPoint(0.0f, 0.0f, 0.0f)
		shape13.addBox(-5.0f, -13.100000381469727f, -6.0f, 1, 9, 1, 0.0f)
		setRotateAngle(shape13, -0.5235987901687622f, 0.0f, 0.0f)
		shape16 = ModelRenderer(this, 68, 13)
		shape16.setRotationPoint(0.0f, 0.0f, 0.0f)
		shape16.addBox(-4.5f, -2.5f, 2.5f, 9, 2, 2, 0.0f)
		bipedHead = ModelRenderer(this, 0, 0)
		bipedHead.setRotationPoint(0.0f, 0.0f, 0.0f)
		bipedHead.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, 0.0f)
		shape6 = ModelRenderer(this, 0, 32)
		shape6.setRotationPoint(0.0f, 0.0f, 0.0f)
		shape6.addBox(4.0f, -8.300000190734863f, -2.0f, 1, 2, 1, 0.0f)
		setRotateAngle(shape6, -0.5235987901687622f, 0.0f, 0.0f)
		frontBag = ModelRenderer(this, 6, 52)
		frontBag.setRotationPoint(0.0f, 0.0f, 0.0f)
		frontBag.addBox(0.0f, 6.0f, -4.0f, 3, 4, 2, 0.0f)
		setRotateAngle(frontBag, 0.0f, 0.0f, 0.4363322854042054f)
		shape5 = ModelRenderer(this, 0, 32)
		shape5.setRotationPoint(0.0f, 0.0f, 0.0f)
		shape5.addBox(4.0f, -10.0f, -3.0f, 1, 4, 1, 0.0f)
		setRotateAngle(shape5, -0.5235987901687622f, 0.0f, 0.0f)
		backBag = ModelRenderer(this, 16, 32)
		backBag.setRotationPoint(0.0f, 0.0f, 0.0f)
		backBag.addBox(-1.0f, 8.0f, 2.0f, 5, 4, 2, 0.0f)
		bipedLeftLeg = ModelRenderer(this, 0, 16)
		bipedLeftLeg.mirror = true
		bipedLeftLeg.setRotationPoint(2.0f, 12.0f, 0.1f)
		bipedLeftLeg.addBox(-2.0f, 0.0f, -2.0f, 4, 12, 4, 0.0f)
		shape17 = ModelRenderer(this, 68, 10)
		shape17.setRotationPoint(0.0f, 0.0f, 0.0f)
		shape17.addBox(-4.5f, -2.5f, 0.5f, 9, 1, 2, 0.0f)
		shape9 = ModelRenderer(this, 0, 42)
		shape9.setRotationPoint(0.0f, 0.0f, 0.0f)
		shape9.addBox(-5.0f, -8.300000190734863f, -2.0f, 1, 2, 1, 0.0f)
		setRotateAngle(shape9, -0.5235987901687622f, 0.0f, 0.0f)
		shape15 = ModelRenderer(this, 0, 42)
		shape15.setRotationPoint(0.0f, 0.0f, 0.0f)
		shape15.addBox(-5.099999904632568f, -7.0f, -4.099999904632568f, 1, 1, 7, 0.0f)
		shape4 = ModelRenderer(this, 0, 32)
		shape4.setRotationPoint(0.0f, 0.0f, 0.0f)
		shape4.addBox(4.0f, -11.199999809265137f, -4.0f, 1, 6, 1, 0.0f)
		setRotateAngle(shape4, -0.5235987901687622f, 0.0f, 0.0f)
		shape10 = ModelRenderer(this, 0, 42)
		shape10.setRotationPoint(0.0f, 0.0f, 0.0f)
		shape10.addBox(-5.0f, -10.0f, -3.0f, 1, 4, 1, 0.0f)
		setRotateAngle(shape10, -0.5235987901687622f, 0.0f, 0.0f)
		lowerBelt = ModelRenderer(this, 16, 38)
		lowerBelt.setRotationPoint(0.0f, 0.0f, 0.0f)
		lowerBelt.addBox(-4.0f, 10.0f, -2.200000047683716f, 8, 2, 1, 0.0f)
		upperBelt = ModelRenderer(this, 16, 41)
		upperBelt.setRotationPoint(0.0f, 0.0f, 0.0f)
		upperBelt.addBox(-4.0f, 0.0f, -2.200000047683716f, 8, 1, 1, 0.0f)
		shape18 = ModelRenderer(this, 68, 7)
		shape18.setRotationPoint(0.0f, 0.0f, 0.0f)
		shape18.addBox(-4.5f, -4.5f, -2.5f, 9, 1, 2, 0.0f)
		rightboot = ModelRenderer(this, 56, 26)
		rightboot.setRotationPoint(0.0f, 0.0f, 0.0f)
		rightboot.addBox(-2.5f, 8.0f, -2.5f, 5, 4, 5, 0.0f)
		helmet = ModelRenderer(this, 32, 0)
		helmet.setRotationPoint(0.0f, 0.0f, 0.0f)
		helmet.addBox(-4.5f, -8.5f, -4.5f, 9, 4, 9, 0.0f)
		shape2 = ModelRenderer(this, 0, 32)
		shape2.setRotationPoint(0.0f, 0.0f, 0.0f)
		shape2.addBox(4.0f, -12.0f, -5.0f, 1, 7, 1, 0.0f)
		setRotateAngle(shape2, -0.5235987901687622f, 0.0f, 0.0f)
		frontBelt = ModelRenderer(this, 0, 52)
		frontBelt.setRotationPoint(0.0f, 0.0f, 0.0f)
		frontBelt.addBox(0.6000000238418579f, -0.20000000298023224f, -2.0999999046325684f, 1, 11, 1, 0.0f)
		setRotateAngle(frontBelt, 0.0f, 0.0f, 0.4363322854042054f)
		leftboot = ModelRenderer(this, 76, 26)
		leftboot.mirror = true
		leftboot.setRotationPoint(0.0f, 0.0f, 0.0f)
		leftboot.addBox(-2.5f, 8.0f, -2.5f, 5, 4, 5, 0.0f)
		rightshoulder = ModelRenderer(this, 76, 17)
		rightshoulder.setRotationPoint(0.0f, 0.0f, 0.0f)
		rightshoulder.addBox(-3.5f, -2.5f, -2.5f, 5, 4, 5, 0.0f)
		shape8 = ModelRenderer(this, 0, 32)
		shape8.setRotationPoint(0.0f, 0.0f, 0.0f)
		shape8.addBox(4.099999904632568f, -7.0f, -4.099999904632568f, 1, 1, 7, 0.0f)
		shape14 = ModelRenderer(this, 0, 42)
		shape14.setRotationPoint(0.0f, 0.0f, 0.0f)
		shape14.addBox(-5.0f, -11.0f, -7.0f, 1, 7, 1, 0.0f)
		setRotateAngle(shape14, -0.5235987901687622f, 0.0f, 0.0f)
		bipedRightArm = ModelRenderer(this, 40, 16)
		bipedRightArm.setRotationPoint(-5.0f, 2.0f, 0.0f)
		bipedRightArm.addBox(-3.0f, -2.0f, -2.0f, 4, 12, 4, 0.0f)
		shape1 = ModelRenderer(this, 68, 0)
		shape1.setRotationPoint(0.0f, 0.0f, 0.0f)
		shape1.addBox(-4.5f, -4.5f, -0.5f, 9, 2, 5, 0.0f)
		bipedBody = ModelRenderer(this, 16, 16)
		bipedBody.setRotationPoint(0.0f, 0.0f, 0.0f)
		bipedBody.addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4, 0.0f)
		leftshoulder = ModelRenderer(this, 56, 17)
		leftshoulder.setRotationPoint(0.0f, 0.0f, 0.0f)
		leftshoulder.addBox(-1.5f, -2.5f, -2.5f, 5, 4, 5, 0.0f)
		shape3 = ModelRenderer(this, 0, 32)
		shape3.setRotationPoint(0.0f, 0.0f, 0.0f)
		shape3.addBox(4.0f, -13.100000381469727f, -6.0f, 1, 9, 1, 0.0f)
		setRotateAngle(shape3, -0.5235987901687622f, 0.0f, 0.0f)
		shape7 = ModelRenderer(this, 0, 32)
		shape7.setRotationPoint(0.0f, 0.0f, 0.0f)
		shape7.addBox(4.0f, -11.0f, -7.0f, 1, 7, 1, 0.0f)
		setRotateAngle(shape7, -0.5235987901687622f, 0.0f, 0.0f)
		
		bipedHead.addChild(helmet)
		
		bipedBody.addChild(frontBag)
		bipedBody.addChild(backBag)
		bipedBody.addChild(lowerBelt)
		bipedBody.addChild(upperBelt)
		bipedBody.addChild(frontBelt)
		
		bipedRightLeg.addChild(rightboot)
		bipedLeftLeg.addChild(leftboot)
		bipedRightArm.addChild(rightshoulder)
		bipedLeftArm.addChild(leftshoulder)
		
		helmet.addChild(shape1)
		helmet.addChild(shape2)
		helmet.addChild(shape3)
		helmet.addChild(shape4)
		helmet.addChild(shape5)
		helmet.addChild(shape6)
		helmet.addChild(shape7)
		helmet.addChild(shape8)
		helmet.addChild(shape9)
		helmet.addChild(shape10)
		helmet.addChild(shape11)
		helmet.addChild(shape12)
		helmet.addChild(shape13)
		helmet.addChild(shape14)
		helmet.addChild(shape15)
		helmet.addChild(shape16)
		helmet.addChild(shape17)
		helmet.addChild(shape18)
		
		bipedCloak.showModel = false
		bipedEars.showModel = false
		bipedHeadwear.showModel = false
	}
	
	override fun render(entity: Entity, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
		if (entity is EntityElf) {
			helmet.showModel = entity.getEquipmentInSlot(4) == null
			
			leftboot.showModel = entity.getEquipmentInSlot(0) == null
			rightboot.showModel = entity.getEquipmentInSlot(0) == null
			
			rightshoulder.showModel = entity.getEquipmentInSlot(3) == null
			leftshoulder.showModel = entity.getEquipmentInSlot(3) == null
		}
		
		super.render(entity, f, f1, f2, f3, f4, f5)
	}
	
	fun setRotateAngle(modelRenderer: ModelRenderer, x: Float, y: Float, z: Float) {
		modelRenderer.rotateAngleX = x
		modelRenderer.rotateAngleY = y
		modelRenderer.rotateAngleZ = z
	}
}