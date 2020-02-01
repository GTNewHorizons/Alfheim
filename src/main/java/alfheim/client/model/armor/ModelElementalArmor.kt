package alfheim.client.model.armor

import net.minecraft.client.model.*
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumAction

class ModelElementalArmor(internal val slot: Int): ModelBiped() {
	
	val helm: ModelRenderer
	val body: ModelRenderer
	val armR: ModelRenderer
	val armL: ModelRenderer
	val belt: ModelRenderer
	val bootR: ModelRenderer
	val bootL: ModelRenderer
	val helm1: ModelRenderer
	val helm2: ModelRenderer
	val helm3: ModelRenderer
	val fairy: ModelRenderer
	val helmWing1: ModelRenderer
	val helmWing2: ModelRenderer
	val helmWing3: ModelRenderer
	val helmWing4: ModelRenderer
	val body2: ModelRenderer
	val armRpauldron: ModelRenderer
	val wing1: ModelRenderer
	val wing2: ModelRenderer
	val armLpauldron: ModelRenderer
	val wing1_1: ModelRenderer
	val wing2_1: ModelRenderer
	val legR: ModelRenderer
	val legL: ModelRenderer
	val bootR1: ModelRenderer
	val wing1_2: ModelRenderer
	val wing2_2: ModelRenderer
	val bootL1: ModelRenderer
	val wing1_3: ModelRenderer
	val wing2_3: ModelRenderer
	val rightagem: ModelRenderer
	val leftagem: ModelRenderer
	val rightfgem: ModelRenderer
	val leftfgem: ModelRenderer
	
	init {
		
		textureWidth = 64
		textureHeight = 128
		val s = 0.2f
		fairy = ModelRenderer(this, 34, 32)
		fairy.setRotationPoint(0f, 0f, 0f)
		fairy.addBox(-2f, -8.5f, -7f, 4, 4, 4, s)
		setRotateAngle(fairy, -0.17453292519943295f, 0f, 0f)
		helm3 = ModelRenderer(this, 0, 32)
		helm3.setRotationPoint(0f, 0f, 0f)
		helm3.addBox(-1f, -5.5f, -5.5f, 2, 3, 1, s)
		setRotateAngle(helm3, -0.17453292519943295f, 0f, 0f)
		wing1_2 = ModelRenderer(this, 56, 43)
		wing1_2.mirror = true
		wing1_2.setRotationPoint(-2.5f, 9f, 0f)
		wing1_2.addBox(0.5f, -2f, 0f, 0, 2, 3, s)
		setRotateAngle(wing1_2, 0.2617993877991494f, -0.7853981633974483f, -0.2617993877991494f)
		helm1 = ModelRenderer(this, 50, 32)
		helm1.setRotationPoint(0f, 0f, 0f)
		helm1.addBox(-4f, -5f, -4.5f, 1, 5, 4, s)
		legL = ModelRenderer(this, 12, 79)
		legL.mirror = true
		legL.setRotationPoint(1.9f, 12f, 0f)
		legL.addBox(-2f, 0f, -2f, 4, 6, 4, s)
		setRotateAngle(legL, 0f, 0f, 0f)
		armL = ModelRenderer(this, 0, 79)
		armL.mirror = true
		armL.setRotationPoint(5f, 2f, -0f)
		armL.addBox(1.5f, 6f, -2f, 2, 4, 4, s)
		setRotateAngle(armL, 0f, 0f, 0f)
		armRpauldron = ModelRenderer(this, 0, 67)
		armRpauldron.setRotationPoint(0f, 0f, 0f)
		armRpauldron.addBox(-4f, -2.5f, -3f, 5, 6, 6, s)
		legR = ModelRenderer(this, 12, 79)
		legR.setRotationPoint(-1.9f, 12f, 0f)
		legR.addBox(-2f, 0f, -2f, 4, 6, 4, s)
		setRotateAngle(legR, 0f, 0f, 0f)
		helmWing2 = ModelRenderer(this, 46, 45)
		helmWing2.mirror = true
		helmWing2.setRotationPoint(-4f, -4f, -1f)
		helmWing2.addBox(-0.5f, 0f, 0f, 1, 3, 4, s)
		setRotateAngle(helmWing2, -0.2617993877991494f, -0.2617993877991494f, 0.2617993877991494f)
		bootL1 = ModelRenderer(this, 12, 79)
		bootL1.mirror = true
		bootL1.setRotationPoint(0f, 0f, 0f)
		bootL1.addBox(-2f, 7f, -2f, 4, 1, 4, s)
		armR = ModelRenderer(this, 0, 79)
		armR.setRotationPoint(-5f, 2f, 0f)
		armR.addBox(-3.5f, 6f, -2f, 2, 4, 4, s)
		setRotateAngle(armR, 0f, 0f, 0f)
		bootR1 = ModelRenderer(this, 12, 79)
		bootR1.setRotationPoint(0f, 0f, 0f)
		bootR1.addBox(-2f, 7f, -2f, 4, 1, 4, s)
		bootR = ModelRenderer(this, 12, 79)
		bootR.setRotationPoint(-1.9f, 12f, 0f)
		bootR.addBox(-2f, 8f, -3f, 4, 4, 5, s)
		setRotateAngle(bootR, 0f, 0f, 0f)
		wing2_1 = ModelRenderer(this, 56, 42)
		wing2_1.setRotationPoint(4.5f, 0f, 0f)
		wing2_1.addBox(0f, 0f, -0.5f, 0, 2, 3, s)
		setRotateAngle(wing2_1, 0.08726646259971647f, 0.7853981633974483f, 0.2617993877991494f)
		wing2_2 = ModelRenderer(this, 56, 44)
		wing2_2.mirror = true
		wing2_2.setRotationPoint(-2.5f, 9f, 0f)
		wing2_2.addBox(0.5f, 0f, 0f, 0, 1, 2, s)
		setRotateAngle(wing2_2, 0.08726646259971647f, -0.7853981633974483f, -0.2617993877991494f)
		bootL = ModelRenderer(this, 12, 79)
		bootL.mirror = true
		bootL.setRotationPoint(1.9f, 12f, 0f)
		bootL.addBox(-2f, 8f, -3f, 4, 4, 5, s)
		setRotateAngle(bootL, 0f, 0f, 0f)
		body = ModelRenderer(this, 0, 44)
		body.setRotationPoint(0f, 0f, 0f)
		body.addBox(-4.5f, 0f, -4f, 9, 5, 7, s)
		setRotateAngle(body, 0.08726646259971647f, 0f, 0f)
		belt = ModelRenderer(this, 22, 56)
		belt.setRotationPoint(0f, 0f, 0f)
		belt.addBox(-4.5f, 9.5f, -3f, 9, 3, 5, s)
		helm = ModelRenderer(this, 0, 32)
		helm.setRotationPoint(0f, 0f, 0f)
		helm.addBox(-4f, -8f, -4.5f, 8, 3, 9, s)
		setRotateAngle(helm, 0.08726646259971647f, 0f, 0f)
		helmWing4 = ModelRenderer(this, 46, 45)
		helmWing4.setRotationPoint(4f, -4f, -1f)
		helmWing4.addBox(-0.5f, 0f, 0f, 1, 3, 4, s)
		setRotateAngle(helmWing4, -0.2617993877991494f, 0.2617993877991494f, -0.2617993877991494f)
		armLpauldron = ModelRenderer(this, 0, 67)
		armLpauldron.mirror = true
		armLpauldron.setRotationPoint(0f, 0f, -0f)
		armLpauldron.addBox(-1f, -2.5f, -3f, 5, 6, 6, s)
		wing1_1 = ModelRenderer(this, 56, 41)
		wing1_1.setRotationPoint(4.5f, 0f, 0f)
		wing1_1.addBox(0f, -3f, -0.5f, 0, 3, 4, s)
		setRotateAngle(wing1_1, 0.2617993877991494f, 0.7853981633974483f, 0.2617993877991494f)
		helm2 = ModelRenderer(this, 50, 32)
		helm2.mirror = true
		helm2.setRotationPoint(0f, 0f, 0f)
		helm2.addBox(3f, -5f, -4.5f, 1, 5, 4, s)
		wing2_3 = ModelRenderer(this, 56, 44)
		wing2_3.setRotationPoint(2.5f, 9f, 0f)
		wing2_3.addBox(0f, 0f, -0.5f, 0, 1, 2, s)
		setRotateAngle(wing2_3, 0.08726646259971647f, 0.7853981633974483f, 0.2617993877991494f)
		wing1 = ModelRenderer(this, 56, 41)
		wing1.mirror = true
		wing1.setRotationPoint(-4.5f, 0f, 0f)
		wing1.addBox(0.5f, -3f, 0f, 0, 3, 4, s)
		setRotateAngle(wing1, 0.2617993877991494f, -0.7853981633974483f, -0.2617993877991494f)
		body2 = ModelRenderer(this, 0, 56)
		body2.setRotationPoint(0f, 0f, 0f)
		body2.addBox(-3f, 4f, -3f, 6, 6, 5, s)
		setRotateAngle(body2, -0.08726646259971647f, 0f, 0f)
		helmWing3 = ModelRenderer(this, 32, 45)
		helmWing3.setRotationPoint(4f, -4f, -1f)
		helmWing3.addBox(-0.5f, -5f, 0f, 1, 5, 6, s)
		setRotateAngle(helmWing3, 0.2617993877991494f, 0.5235987755982988f, 0.08726646259971647f)
		helmWing1 = ModelRenderer(this, 32, 45)
		helmWing1.mirror = true
		helmWing1.setRotationPoint(-4f, -4f, -1f)
		helmWing1.addBox(-0.5f, -5f, 0f, 1, 5, 6, s)
		setRotateAngle(helmWing1, 0.2617993877991494f, -0.5235987755982988f, -0.08726646259971647f)
		wing2 = ModelRenderer(this, 56, 42)
		wing2.mirror = true
		wing2.setRotationPoint(-4.5f, 0f, 0f)
		wing2.addBox(0.5f, 0f, 0f, 0, 2, 3, s)
		setRotateAngle(wing2, 0.08726646259971647f, -0.7853981633974483f, -0.2617993877991494f)
		wing1_3 = ModelRenderer(this, 56, 43)
		wing1_3.setRotationPoint(2.5f, 9f, 0f)
		wing1_3.addBox(0f, -2f, -0.5f, 0, 2, 3, s)
		setRotateAngle(wing1_3, 0.2617993877991494f, 0.7853981633974483f, 0.2617993877991494f)
		rightagem = ModelRenderer(this, 28, 64)
		rightagem.addBox(1f, -3f, -3.5f, 2, 2, 1)
		rightagem.setRotationPoint(-2f, 12f, 0f)
		setRotateAngle(rightagem, 0f, 0f, 0f)
		leftagem = ModelRenderer(this, 28, 64)
		leftagem.addBox(-3f, -3f, -3.5f, 2, 2, 1)
		leftagem.setRotationPoint(2f, 12f, 0f)
		setRotateAngle(leftagem, 0f, 0f, 0f)
		rightfgem = ModelRenderer(this, 34, 64)
		rightfgem.addBox(1f, -10f, -3f, 2, 2, 1)
		rightfgem.setRotationPoint(-2f, 12f, 0f)
		setRotateAngle(rightfgem, 0f, 0f, 0f)
		leftfgem = ModelRenderer(this, 34, 64)
		leftfgem.addBox(-3f, -10f, -3f, 2, 2, 1)
		leftfgem.setRotationPoint(2f, 12f, 0f)
		setRotateAngle(leftfgem, 0f, 0f, 0f)
		
		helm.addChild(fairy)
		helm.addChild(helm3)
		bootR.addChild(wing1_2)
		helm.addChild(helm1)
		belt.addChild(legL)
		armR.addChild(armRpauldron)
		belt.addChild(legR)
		helm.addChild(helmWing2)
		bootL.addChild(bootL1)
		bootR.addChild(bootR1)
		armLpauldron.addChild(wing2_1)
		bootR.addChild(wing2_2)
		helm.addChild(helmWing4)
		armL.addChild(armLpauldron)
		armLpauldron.addChild(wing1_1)
		helm.addChild(helm2)
		bootL.addChild(wing2_3)
		armRpauldron.addChild(wing1)
		body.addChild(body2)
		helm.addChild(helmWing3)
		helm.addChild(helmWing1)
		armRpauldron.addChild(wing2)
		bootL.addChild(wing1_3)
		bootR.addChild(rightagem)
		bootL.addChild(leftagem)
		legR.addChild(rightfgem)
		legL.addChild(leftfgem)
	}
	
	override fun render(entity: Entity?, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
		helm.showModel = slot == 0
		body.showModel = slot == 1
		armR.showModel = slot == 1
		armL.showModel = slot == 1
		legR.showModel = slot == 2
		legL.showModel = slot == 2
		bootL.showModel = slot == 3
		bootR.showModel = slot == 3
		bipedHeadwear.showModel = false
		
		bipedHead = helm
		bipedBody = body
		bipedRightArm = armR
		bipedLeftArm = armL
		if (slot == 2) {
			bipedRightLeg = legR
			bipedLeftLeg = legL
		} else {
			bipedRightLeg = bootR
			bipedLeftLeg = bootL
		}
		
		prepareForRender(entity)
		super.render(entity, f, f1, f2, f3, f4, f5)
	}
	
	fun prepareForRender(entity: Entity?) {
		val living = entity as EntityLivingBase?
		isSneak = living != null && living.isSneaking
		if (living is EntityPlayer) {
			val player = living as EntityPlayer?
			
			val itemstack = player!!.inventory.getCurrentItem()
			heldItemRight = if (itemstack != null) 1 else 0
			
			aimedBow = false
			if (itemstack != null && player.itemInUseCount > 0) {
				val enumaction = itemstack.itemUseAction
				
				if (enumaction == EnumAction.block)
					heldItemRight = 3
				else if (enumaction == EnumAction.bow)
					aimedBow = true
			}
		}
	}
	
	fun setRotateAngle(modelRenderer: ModelRenderer, x: Float, y: Float, z: Float) {
		modelRenderer.rotateAngleX = x
		modelRenderer.rotateAngleY = y
		modelRenderer.rotateAngleZ = z
	}
}