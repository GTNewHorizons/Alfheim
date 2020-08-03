package alfmod.client.model.armor

import alexsocol.asjlib.F
import alfmod.common.item.equipment.armor.ItemVolcanoArmor
import net.minecraft.client.model.*
import net.minecraft.entity.*
import net.minecraft.entity.monster.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumAction
import net.minecraft.util.MathHelper
import org.lwjgl.opengl.GL11.*

class ModelArmorVolcano(val slot: Int): ModelBiped() {
	
	var mask1: ModelRenderer
	var mask2: ModelRenderer
	var mask3: ModelRenderer
	var mask4: ModelRenderer
	var mask5: ModelRenderer
	var mask6: ModelRenderer
	var mask7: ModelRenderer
	var mask8: ModelRenderer
	var mask9: ModelRenderer
	var mask10: ModelRenderer
	var mask11: ModelRenderer
	var mask12: ModelRenderer
	var mask13: ModelRenderer
	var mask14: ModelRenderer
	
	var RightShoulder1: ModelRenderer
	var RightShoulder2: ModelRenderer
	var RightShoulder3: ModelRenderer
	var RightShoulder4: ModelRenderer
	var RightShoulder5: ModelRenderer
	var RightShoulder6: ModelRenderer
	var RightShoulder7: ModelRenderer
	var RightShoulder8: ModelRenderer
	
	var LeftShoulder1: ModelRenderer
	var LeftShoulder2: ModelRenderer
	var LeftShoulder3: ModelRenderer
	var LeftShoulder4: ModelRenderer
	var LeftShoulder5: ModelRenderer
	var LeftShoulder6: ModelRenderer
	var LeftShoulder7: ModelRenderer
	var LeftShoulder8: ModelRenderer
	
	var chestGem: ModelRendererGem
	
	var chest1: ModelRenderer
	var chest2: ModelRenderer
	var chest3: ModelRenderer
	var chest4: ModelRenderer
	var chest5: ModelRenderer
	var chest6: ModelRenderer
	var chest7: ModelRenderer
	var chest8: ModelRenderer
	var chest9: ModelRenderer
	var chest10: ModelRenderer
	var chest11: ModelRenderer
	var chest12: ModelRenderer
	var chest13: ModelRenderer
	var chest14: ModelRenderer
	var chest15: ModelRenderer
	var chest16: ModelRenderer
	var chest17: ModelRenderer
	
	var rightLeggin1: ModelRenderer
	var rightLeggin2: ModelRenderer
	var rightLeggin3: ModelRenderer
	var rightLeggin4: ModelRenderer
	
	var leftLeggin1: ModelRenderer
	var leftLeggin2: ModelRenderer
	var leftLeggin3: ModelRenderer
	var leftLeggin4: ModelRenderer
	
	init {
		bipedHead.cubeList.clear()
		bipedBody.cubeList.clear()
		bipedLeftArm.cubeList.clear()
		bipedRightArm.cubeList.clear()
		bipedLeftLeg.cubeList.clear()
		bipedRightLeg.cubeList.clear()
		
		textureWidth = 64
		textureHeight = 64
		LeftShoulder2 = ModelRenderer(this, 0, 32)
		LeftShoulder2.mirror = true
		LeftShoulder2.setRotationPoint(0.0f, 0.0f, 0.0f)
		LeftShoulder2.addBox(1.5f, -3.0f, -2.0f, 4, 1, 4, 0.0f)
		setRotateAngle(LeftShoulder2, 0.0f, 0.0f, -0.2617993877991494f)
		chestGem = ModelRendererGem(this, 0, 61)
		chestGem.setRotationPoint(0.0f, 0.0f, 0.0f)
		chestGem.addBox(2.75f, 2.75f, -2.5f, 2, 2, 1, 0.0f)
		setRotateAngle(chestGem, 0.0f, 0.0f, 0.7853981633974483f)
		chest4 = ModelRenderer(this, 0, 32)
		chest4.mirror = true
		chest4.setRotationPoint(0.0f, 0.0f, 0.0f)
		chest4.addBox(1.0f, 6.0f, -2.75f, 4, 3, 2, 0.0f)
		setRotateAngle(chest4, 0.0f, 0.0f, 0.08726646259971647f)
		chest3 = ModelRenderer(this, 0, 32)
		chest3.setRotationPoint(0.0f, 0.0f, 0.0f)
		chest3.addBox(-5.0f, 6.0f, -2.75f, 4, 3, 2, 0.0f)
		setRotateAngle(chest3, 0.0f, 0.0f, -0.08726646259971647f)
		chest17 = ModelRenderer(this, 3, 32)
		chest17.setRotationPoint(0.0f, 0.0f, 0.0f)
		chest17.addBox(-1.0f, 7.5f, 5.4f, 2, 2, 1, 0.0f)
		setRotateAngle(chest17, -0.2617993877991494f, 0.0f, 0.0f)
		RightShoulder1 = ModelRenderer(this, 0, 32)
		RightShoulder1.setRotationPoint(0.0f, 0.0f, 0.0f)
		RightShoulder1.addBox(-4.0f, -2.5f, -3.0f, 4, 1, 6, 0.0f)
		setRotateAngle(RightShoulder1, 0.0f, 0.0f, 0.2617993877991494f)
		chest1 = ModelRenderer(this, 0, 32)
		chest1.setRotationPoint(0.0f, 0.0f, 0.0f)
		chest1.addBox(-4.5f, 0.0f, -3.0f, 4, 6, 2, 0.0f)
		setRotateAngle(chest1, 0.0f, 0.0f, 0.08726646259971647f)
		mask9 = ModelRenderer(this, 0, 43)
		mask9.mirror = true
		mask9.setRotationPoint(0.0f, 0.0f, 0.0f)
		mask9.addBox(3.0f, -10.5f, -4.25f, 1, 5, 1, 0.0f)
		setRotateAngle(mask9, -0.1308996938995747f, 0.0f, 0.2617993877991494f)
		RightShoulder5 = ModelRenderer(this, 0, 32)
		RightShoulder5.setRotationPoint(0.0f, 0.0f, 0.0f)
		RightShoulder5.addBox(-5.0f, -0.5f, -3.0f, 4, 4, 6, 0.0f)
		setRotateAngle(RightShoulder5, 0.0f, 0.0f, -0.17453292519943295f)
		LeftShoulder4 = ModelRenderer(this, 0, 32)
		LeftShoulder4.mirror = true
		LeftShoulder4.setRotationPoint(0.0f, 0.0f, 0.0f)
		LeftShoulder4.addBox(0.0f, -2.5f, -3.5f, 4, 4, 7, 0.0f)
		leftLeggin2 = ModelRenderer(this, 0, 32)
		leftLeggin2.mirror = true
		leftLeggin2.setRotationPoint(0.0f, 0.0f, 0.0f)
		leftLeggin2.addBox(-2.5f, 9.5f, -2.5f, 5, 3, 5, 0.0f)
		chest9 = ModelRenderer(this, 0, 32)
		chest9.mirror = true
		chest9.setRotationPoint(0.0f, 0.0f, 0.0f)
		chest9.addBox(3.5f, -4.0f, 2.0f, 2, 9, 1, 0.0f)
		setRotateAngle(chest9, 0.0f, 0.0f, 0.5235987755982988f)
		mask7 = ModelRenderer(this, 12, 32)
		mask7.mirror = true
		mask7.setRotationPoint(0.0f, 0.0f, 0.0f)
		mask7.addBox(3.0f, -10.0f, -4.0f, 1, 10, 2, 0.0f)
		setRotateAngle(mask7, -0.2617993877991494f, 0.0f, 0.2617993877991494f)
		mask14 = ModelRenderer(this, 18, 32)
		mask14.setRotationPoint(0.0f, 0.0f, 0.0f)
		mask14.addBox(-2.5f, -8.5f, 0.0f, 5, 1, 5, 0.0f)
		setRotateAngle(mask14, 0.2617993877991494f, 0.0f, 0.0f)
		chest14 = ModelRenderer(this, 3, 32)
		chest14.setRotationPoint(0.0f, 0.0f, 0.0f)
		chest14.addBox(-1.0f, 1.5f, 3.6f, 2, 2, 1, 0.0f)
		setRotateAngle(chest14, -0.2617993877991494f, 0.0f, 0.0f)
		rightLeggin4 = ModelRenderer(this, 0, 32)
		rightLeggin4.setRotationPoint(0.0f, 0.0f, 0.0f)
		rightLeggin4.addBox(1.5f, 4.0f, -0.5f, 1, 4, 3, 0.0f)
		mask1 = ModelRenderer(this, 0, 32)
		mask1.setRotationPoint(0.0f, 0.0f, 0.0f)
		mask1.addBox(-1.0f, -10.0f, -4.6f, 2, 10, 1, 0.0f)
		RightShoulder7 = ModelRenderer(this, 0, 32)
		RightShoulder7.setRotationPoint(0.0f, 0.0f, 0.0f)
		RightShoulder7.addBox(-4.0f, 7.5f, -2.5f, 2, 3, 5, 0.0f)
		chest8 = ModelRenderer(this, 0, 32)
		chest8.setRotationPoint(0.0f, 0.0f, 0.0f)
		chest8.addBox(-5.5f, -4.0f, 2.0f, 2, 9, 1, 0.0f)
		setRotateAngle(chest8, 0.0f, 0.0f, -0.5235987755982988f)
		mask11 = ModelRenderer(this, 24, 32)
		mask11.setRotationPoint(0.0f, 0.0f, 0.0f)
		mask11.addBox(-3.5f, -7.5f, 3.6f, 7, 7, 1, 0.0f)
		mask6 = ModelRenderer(this, 12, 32)
		mask6.setRotationPoint(0.0f, 0.0f, 0.0f)
		mask6.addBox(-4.0f, -10.0f, -4.0f, 1, 10, 2, 0.0f)
		setRotateAngle(mask6, -0.2617993877991494f, 0.0f, -0.2617993877991494f)
		mask8 = ModelRenderer(this, 0, 43)
		mask8.setRotationPoint(0.0f, 0.0f, 0.0f)
		mask8.addBox(-4.0f, -10.5f, -4.25f, 1, 5, 1, 0.0f)
		setRotateAngle(mask8, -0.1308996938995747f, 0.0f, -0.2617993877991494f)
		chest11 = ModelRenderer(this, 5, 32)
		chest11.mirror = true
		chest11.setRotationPoint(0.0f, 0.0f, 0.0f)
		chest11.addBox(7.5f, 0.0f, 2.0f, 2, 7, 1, 0.0f)
		setRotateAngle(chest11, 0.0f, 0.0f, 0.7853981633974483f)
		mask12 = ModelRenderer(this, 18, 32)
		mask12.setRotationPoint(0.0f, 0.0f, 0.0f)
		mask12.addBox(-4.6f, -7.5f, 0.0f, 1, 7, 3, 0.0f)
		leftLeggin1 = ModelRenderer(this, 0, 32)
		leftLeggin1.mirror = true
		leftLeggin1.setRotationPoint(0.0f, 0.0f, 0.0f)
		leftLeggin1.addBox(-1.5f, 4.0f, -2.5f, 4, 4, 1, 0.0f)
		chest13 = ModelRenderer(this, 3, 32)
		chest13.setRotationPoint(0.0f, 0.0f, 0.0f)
		chest13.addBox(-1.0f, -0.5f, 3.0f, 2, 2, 1, 0.0f)
		setRotateAngle(chest13, -0.2617993877991494f, 0.0f, 0.0f)
		LeftShoulder8 = ModelRenderer(this, 0, 32)
		LeftShoulder8.mirror = true
		LeftShoulder8.setRotationPoint(0.0f, 0.0f, 0.0f)
		LeftShoulder8.addBox(-2.0f, 2.5f, -2.6f, 2, 6, 5, 0.0f)
		rightLeggin1 = ModelRenderer(this, 0, 32)
		rightLeggin1.setRotationPoint(0.0f, 0.0f, 0.0f)
		rightLeggin1.addBox(-2.5f, 4.0f, -2.5f, 4, 4, 1, 0.0f)
		mask2 = ModelRenderer(this, 6, 32)
		mask2.mirror = true
		mask2.setRotationPoint(0.0f, 0.0f, 0.0f)
		mask2.addBox(-3.5f, -10.5f, -4.6f, 2, 10, 1, 0.0f)
		setRotateAngle(mask2, 0.0f, 0.0f, -0.2617993877991494f)
		RightShoulder4 = ModelRenderer(this, 0, 32)
		RightShoulder4.setRotationPoint(0.0f, 0.0f, 0.0f)
		RightShoulder4.addBox(-4.0f, -2.5f, -3.5f, 4, 4, 7, 0.0f)
		chest12 = ModelRenderer(this, 3, 32)
		chest12.setRotationPoint(0.0f, 0.0f, 0.0f)
		chest12.addBox(-1.6f, -0.5f, 2.5f, 3, 12, 1, 0.0f)
		rightLeggin3 = ModelRenderer(this, 0, 32)
		rightLeggin3.setRotationPoint(0.0f, 0.0f, 0.0f)
		rightLeggin3.addBox(-2.5f, 3.5f, -0.5f, 3, 5, 3, 0.0f)
		leftLeggin3 = ModelRenderer(this, 0, 32)
		leftLeggin3.mirror = true
		leftLeggin3.setRotationPoint(0.0f, 0.0f, 0.0f)
		leftLeggin3.addBox(-0.5f, 3.5f, -0.5f, 3, 5, 3, 0.0f)
		mask13 = ModelRenderer(this, 18, 32)
		mask13.mirror = true
		mask13.setRotationPoint(0.0f, 0.0f, 0.0f)
		mask13.addBox(3.6f, -7.5f, 0.0f, 1, 7, 3, 0.0f)
		mask10 = ModelRenderer(this, 18, 32)
		mask10.setRotationPoint(0.0f, 0.0f, 0.0f)
		mask10.addBox(-3.5f, -8.6f, -3.5f, 7, 1, 7, 0.0f)
		LeftShoulder5 = ModelRenderer(this, 0, 32)
		LeftShoulder5.mirror = true
		LeftShoulder5.setRotationPoint(0.0f, 0.0f, 0.0f)
		LeftShoulder5.addBox(1.0f, -0.5f, -3.0f, 4, 4, 6, 0.0f)
		setRotateAngle(LeftShoulder5, 0.0f, 0.0f, 0.17453292519943295f)
		RightShoulder8 = ModelRenderer(this, 0, 32)
		RightShoulder8.setRotationPoint(0.0f, 0.0f, 0.0f)
		RightShoulder8.addBox(0.0f, 2.5f, -2.6f, 2, 6, 5, 0.0f)
		mask4 = ModelRenderer(this, 0, 43)
		mask4.setRotationPoint(0.0f, 0.0f, 0.0f)
		mask4.addBox(0.75f, -10.0f, -4.6f, 1, 5, 1, 0.0f)
		setRotateAngle(mask4, 0.0f, 0.0f, 0.1308996938995747f)
		LeftShoulder6 = ModelRenderer(this, 0, 32)
		LeftShoulder6.mirror = true
		LeftShoulder6.setRotationPoint(0.0f, 0.0f, 0.0f)
		LeftShoulder6.addBox(2.0f, 1.5f, -2.5f, 4, 4, 5, 0.0f)
		setRotateAngle(LeftShoulder6, 0.0f, 0.0f, 0.3490658503988659f)
		chest10 = ModelRenderer(this, 5, 32)
		chest10.setRotationPoint(0.0f, 0.0f, 0.0f)
		chest10.addBox(-9.5f, 0.0f, 2.0f, 2, 7, 1, 0.0f)
		setRotateAngle(chest10, 0.0f, 0.0f, -0.7853981633974483f)
		RightShoulder6 = ModelRenderer(this, 0, 32)
		RightShoulder6.setRotationPoint(0.0f, 0.0f, 0.0f)
		RightShoulder6.addBox(-6.0f, 1.5f, -2.5f, 4, 4, 5, 0.0f)
		setRotateAngle(RightShoulder6, 0.0f, 0.0f, -0.3490658503988659f)
		mask3 = ModelRenderer(this, 6, 32)
		mask3.setRotationPoint(0.0f, 0.0f, 0.0f)
		mask3.addBox(1.5f, -10.5f, -4.6f, 2, 10, 1, 0.0f)
		setRotateAngle(mask3, 0.0f, 0.0f, 0.2617993877991494f)
		LeftShoulder7 = ModelRenderer(this, 0, 32)
		LeftShoulder7.mirror = true
		LeftShoulder7.setRotationPoint(0.0f, 0.0f, 0.0f)
		LeftShoulder7.addBox(2.0f, 7.5f, -2.5f, 2, 3, 5, 0.0f)
		chest16 = ModelRenderer(this, 3, 32)
		chest16.setRotationPoint(0.0f, 0.0f, 0.0f)
		chest16.addBox(-1.0f, 5.5f, 4.8f, 2, 2, 1, 0.0f)
		setRotateAngle(chest16, -0.2617993877991494f, 0.0f, 0.0f)
		chest6 = ModelRenderer(this, 0, 32)
		chest6.setRotationPoint(0.0f, 0.0f, 0.0f)
		chest6.addBox(-4.5f, 9.5f, -2.5f, 9, 5, 5, 0.0f)
		leftLeggin4 = ModelRenderer(this, 0, 32)
		leftLeggin4.mirror = true
		leftLeggin4.setRotationPoint(0.0f, 0.0f, 0.0f)
		leftLeggin4.addBox(-2.5f, 4.0f, -0.5f, 1, 4, 3, 0.0f)
		mask5 = ModelRenderer(this, 0, 43)
		mask5.setRotationPoint(0.0f, 0.0f, 0.0f)
		mask5.addBox(-1.75f, -10.0f, -4.6f, 1, 5, 1, 0.0f)
		setRotateAngle(mask5, 0.0f, 0.0f, -0.1308996938995747f)
		LeftShoulder3 = ModelRenderer(this, 0, 32)
		LeftShoulder3.mirror = true
		LeftShoulder3.setRotationPoint(0.0f, 0.0f, 0.0f)
		LeftShoulder3.addBox(3.0f, -3.5f, -1.0f, 4, 1, 2, 0.0f)
		setRotateAngle(LeftShoulder3, 0.0f, 0.0f, -0.2617993877991494f)
		LeftShoulder1 = ModelRenderer(this, 0, 32)
		LeftShoulder1.mirror = true
		LeftShoulder1.setRotationPoint(0.0f, 0.0f, 0.0f)
		LeftShoulder1.addBox(0.0f, -2.5f, -3.0f, 4, 1, 6, 0.0f)
		setRotateAngle(LeftShoulder1, 0.0f, 0.0f, -0.2617993877991494f)
		chest15 = ModelRenderer(this, 3, 32)
		chest15.setRotationPoint(0.0f, 0.0f, 0.0f)
		chest15.addBox(-1.0f, 3.5f, 4.2f, 2, 2, 1, 0.0f)
		setRotateAngle(chest15, -0.2617993877991494f, 0.0f, 0.0f)
		RightShoulder3 = ModelRenderer(this, 0, 32)
		RightShoulder3.setRotationPoint(0.0f, 0.0f, 0.0f)
		RightShoulder3.addBox(-7.0f, -3.5f, -1.0f, 4, 1, 2, 0.0f)
		setRotateAngle(RightShoulder3, 0.0f, 0.0f, 0.2617993877991494f)
		chest5 = ModelRenderer(this, 0, 32)
		chest5.setRotationPoint(0.0f, 0.0f, 0.0f)
		chest5.addBox(-0.5f, 0.0f, -3.0f, 1, 4, 1, 0.0f)
		chest7 = ModelRenderer(this, 0, 32)
		chest7.setRotationPoint(0.0f, 0.0f, 0.0f)
		chest7.addBox(-4.5f, -0.5f, 0.5f, 9, 9, 2, 0.0f)
		chest2 = ModelRenderer(this, 0, 32)
		chest2.mirror = true
		chest2.setRotationPoint(0.0f, 0.0f, 0.0f)
		chest2.addBox(0.5f, 0.0f, -3.0f, 4, 6, 2, 0.0f)
		setRotateAngle(chest2, 0.0f, 0.0f, -0.08726646259971647f)
		RightShoulder2 = ModelRenderer(this, 0, 32)
		RightShoulder2.setRotationPoint(0.0f, 0.0f, 0.0f)
		RightShoulder2.addBox(-5.5f, -3.0f, -2.0f, 4, 1, 4, 0.0f)
		setRotateAngle(RightShoulder2, 0.0f, 0.0f, 0.2617993877991494f)
		rightLeggin2 = ModelRenderer(this, 0, 32)
		rightLeggin2.setRotationPoint(0.0f, 0.0f, 0.0f)
		rightLeggin2.addBox(-2.5f, 9.5f, -2.5f, 5, 3, 5, 0.0f)
		bipedLeftArm.addChild(LeftShoulder2)
		bipedBody.addChild(chest4)
		bipedBody.addChild(chest3)
		bipedBody.addChild(chest17)
		bipedRightArm.addChild(RightShoulder1)
		bipedBody.addChild(chest1)
		bipedHead.addChild(mask9)
		bipedRightArm.addChild(RightShoulder5)
		bipedLeftArm.addChild(LeftShoulder4)
		bipedLeftLeg.addChild(leftLeggin2)
		bipedBody.addChild(chest9)
		bipedHead.addChild(mask7)
		bipedHead.addChild(mask14)
		bipedBody.addChild(chest14)
		bipedRightLeg.addChild(rightLeggin4)
		bipedHead.addChild(mask1)
		bipedRightArm.addChild(RightShoulder7)
		bipedBody.addChild(chest8)
		bipedHead.addChild(mask11)
		bipedHead.addChild(mask6)
		bipedHead.addChild(mask8)
		bipedBody.addChild(chest11)
		bipedHead.addChild(mask12)
		bipedLeftLeg.addChild(leftLeggin1)
		bipedBody.addChild(chest13)
		bipedLeftArm.addChild(LeftShoulder8)
		bipedRightLeg.addChild(rightLeggin1)
		bipedHead.addChild(mask2)
		bipedRightArm.addChild(RightShoulder4)
		bipedBody.addChild(chest12)
		bipedRightLeg.addChild(rightLeggin3)
		bipedLeftLeg.addChild(leftLeggin3)
		bipedHead.addChild(mask13)
		bipedHead.addChild(mask10)
		bipedLeftArm.addChild(LeftShoulder5)
		bipedRightArm.addChild(RightShoulder8)
		bipedHead.addChild(mask4)
		bipedLeftArm.addChild(LeftShoulder6)
		bipedBody.addChild(chest10)
		bipedRightArm.addChild(RightShoulder6)
		bipedHead.addChild(mask3)
		bipedLeftArm.addChild(LeftShoulder7)
		bipedBody.addChild(chest16)
		bipedBody.addChild(chest6)
		bipedLeftLeg.addChild(leftLeggin4)
		bipedHead.addChild(mask5)
		bipedLeftArm.addChild(LeftShoulder3)
		bipedLeftArm.addChild(LeftShoulder1)
		bipedBody.addChild(chest15)
		bipedRightArm.addChild(RightShoulder3)
		bipedBody.addChild(chest5)
		bipedBody.addChild(chest7)
		bipedBody.addChild(chest2)
		bipedRightArm.addChild(RightShoulder2)
		bipedRightLeg.addChild(rightLeggin2)
		bipedBody.addChild(chestGem)
	}
	
	override fun render(entity: Entity?, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
		mask1.showModel = slot == 0
		mask2.showModel = slot == 0
		mask3.showModel = slot == 0
		mask4.showModel = slot == 0
		mask5.showModel = slot == 0
		mask6.showModel = slot == 0
		mask7.showModel = slot == 0
		mask8.showModel = slot == 0
		mask9.showModel = slot == 0
		mask10.showModel = slot == 0
		mask11.showModel = slot == 0
		mask12.showModel = slot == 0
		mask13.showModel = slot == 0
		mask14.showModel = slot == 0
		
		chestGem.showModel = slot == 1
		
		chest1.showModel = slot == 1
		chest2.showModel = slot == 1
		chest3.showModel = slot == 1
		chest4.showModel = slot == 1
		chest5.showModel = slot == 1
		chest6.showModel = slot == 1
		chest7.showModel = slot == 1
		chest8.showModel = slot == 1
		chest9.showModel = slot == 1
		chest10.showModel = slot == 1
		chest11.showModel = slot == 1
		chest12.showModel = slot == 1
		chest13.showModel = slot == 1
		chest14.showModel = slot == 1
		chest15.showModel = slot == 1
		chest16.showModel = slot == 1
		chest17.showModel = slot == 1
		
		RightShoulder1.showModel = slot == 1
		RightShoulder2.showModel = slot == 1
		RightShoulder3.showModel = slot == 1
		RightShoulder4.showModel = slot == 1
		RightShoulder5.showModel = slot == 1
		RightShoulder6.showModel = slot == 1
		RightShoulder7.showModel = slot == 1
		RightShoulder8.showModel = slot == 1
		
		LeftShoulder1.showModel = slot == 1
		LeftShoulder2.showModel = slot == 1
		LeftShoulder3.showModel = slot == 1
		LeftShoulder4.showModel = slot == 1
		LeftShoulder5.showModel = slot == 1
		LeftShoulder6.showModel = slot == 1
		LeftShoulder7.showModel = slot == 1
		LeftShoulder8.showModel = slot == 1
		
		leftLeggin1.showModel = slot == 2
		leftLeggin2.showModel = slot == 3
		leftLeggin3.showModel = slot == 2
		leftLeggin4.showModel = slot == 2
		
		rightLeggin1.showModel = slot == 2
		rightLeggin2.showModel = slot == 3
		rightLeggin3.showModel = slot == 2
		rightLeggin4.showModel = slot == 2
		
		bipedHeadwear.showModel = false
		
		prepareForRender(entity, f2)
		
		setRotationAngles(f, f1, f2, f3, f4, f5, entity)
		
		if (entity is EntityZombie || entity is EntitySkeleton || entity is EntityGiantZombie) {
			val f6 = MathHelper.sin(onGround * Math.PI.F)
			val f7 = MathHelper.sin((1f - (1f - onGround) * (1f - onGround)) * Math.PI.F)
			
			bipedRightArm.rotateAngleZ = 0f
			bipedRightArm.rotateAngleY = -(0.1f - f6 * 0.6f)
			bipedRightArm.rotateAngleX = -(Math.PI.F / 2f)
			bipedRightArm.rotateAngleX -= f6 * 1.2f - f7 * 0.4f
			bipedRightArm.rotateAngleZ += MathHelper.cos(f * 0.09f) * 0.05f + 0.05f
			bipedRightArm.rotateAngleX += MathHelper.sin(f * 0.067f) * 0.05f
			
			bipedLeftArm.rotateAngleZ = 0f
			bipedLeftArm.rotateAngleY = 0.1f - f6 * 0.6f
			bipedLeftArm.rotateAngleX = -(Math.PI.F / 2f)
			bipedLeftArm.rotateAngleX -= f6 * 1.2f - f7 * 0.4f
			bipedLeftArm.rotateAngleZ -= MathHelper.cos(f * 0.09f) * 0.05f + 0.05f
			bipedLeftArm.rotateAngleX -= MathHelper.sin(f * 0.067f) * 0.05f
		}
		
		chestGem.charge = (entity as? EntityPlayer)?.let { ItemVolcanoArmor.getCharge(it) } ?: 0f
		
		if (isChild) {
			val f6 = 2.0f
			glPushMatrix()
			glScalef(1.5f / f6, 1.5f / f6, 1.5f / f6)
			glTranslatef(0.0f, 16.0f * f5, 0.0f)
			bipedHead.render(f5)
			glPopMatrix()
			glPushMatrix()
			glScalef(1.0f / f6, 1.0f / f6, 1.0f / f6)
			glTranslatef(0.0f, 24.0f * f5, 0.0f)
			bipedRightArm.render(f5)
			bipedLeftArm.render(f5)
			bipedRightLeg.render(f5)
			bipedLeftLeg.render(f5)
			bipedBody.render(f5)
			glPopMatrix()
		} else {
			bipedHead.render(f5)
			bipedRightArm.render(f5)
			bipedLeftArm.render(f5)
			bipedRightLeg.render(f5)
			bipedLeftLeg.render(f5)
			bipedBody.render(f5)
		}
	}
	
	fun prepareForRender(entity: Entity?, f: Float) {
		val living = entity as? EntityLivingBase ?: return
		
		isSneak = living.isSneaking
		isChild = living.isChild
		isRiding = living.isRiding
		
		val itemstack = living.heldItem
		heldItemRight = if (itemstack != null) 1 else 0
		heldItemLeft = 0
		
		if (entity is EntityPlayer) {
			val player = entity as EntityPlayer?
			
			aimedBow = false
			if (itemstack != null && player!!.itemInUseCount > 0) {
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