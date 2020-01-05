package alfmod.client.render.model

import net.minecraft.client.model.*
import net.minecraft.entity.Entity
import net.minecraft.entity.monster.*
import net.minecraft.util.MathHelper
import org.lwjgl.opengl.GL11

/** Thaumic Fortress Armor model by @Azanor  */
class ModelSnowArmor(f: Float): ModelBiped(f, 0.0f, 128, 64) {
	
	var Helmet: ModelRenderer
	var HelmetR: ModelRenderer
	var HelmetL: ModelRenderer
	var HelmetB: ModelRenderer
	var capsthingy: ModelRenderer
	var flapR: ModelRenderer
	var flapL: ModelRenderer
	var Mask: ModelRenderer
	var BeltR: ModelRenderer
	var Mbelt: ModelRenderer
	var MbeltL: ModelRenderer
	var MbeltR: ModelRenderer
	var BeltL: ModelRenderer
	var Chestplate: ModelRenderer
	var Backplate: ModelRenderer
	var ShoulderR: ModelRenderer
	var GauntletR: ModelRenderer
	var GauntletstrapR1: ModelRenderer
	var GauntletstrapR2: ModelRenderer
	var ShoulderplateRtop: ModelRenderer
	var ShoulderplateR1: ModelRenderer
	var ShoulderplateR2: ModelRenderer
	var ShoulderplateR3: ModelRenderer
	var ShoulderL: ModelRenderer
	var GauntletL: ModelRenderer
	var Gauntletstrapl1: ModelRenderer
	var GauntletstrapL2: ModelRenderer
	var ShoulderplateLtop: ModelRenderer
	var ShoulderplateL1: ModelRenderer
	var ShoulderplateL2: ModelRenderer
	var ShoulderplateL3: ModelRenderer
	var LegpanelR1: ModelRenderer
	var LegpanelR2: ModelRenderer
	var LegpanelR3: ModelRenderer
	var LegpanelR4: ModelRenderer
	var LegpanelR5: ModelRenderer
	var LegpanelR6: ModelRenderer
	var SidepanelR1: ModelRenderer
	var SidepanelR2: ModelRenderer
	var SidepanelR3: ModelRenderer
	var BackpanelR1: ModelRenderer
	var BackpanelR2: ModelRenderer
	var BackpanelR3: ModelRenderer
	var BackpanelL3: ModelRenderer
	var LegpanelL1: ModelRenderer
	var LegpanelL2: ModelRenderer
	var LegpanelL3: ModelRenderer
	var LegpanelL4: ModelRenderer
	var LegpanelL5: ModelRenderer
	var LegpanelL6: ModelRenderer
	var SidepanelL1: ModelRenderer
	var SidepanelL2: ModelRenderer
	var SidepanelL3: ModelRenderer
	var BackpanelL1: ModelRenderer
	var BackpanelL2: ModelRenderer
	
	init {
		textureWidth = 128
		textureHeight = 64
		
		Mask = ModelRenderer(this, 52, 2)
		Mask.addBox(-4.5f, -5.0f, -4.6f, 9, 5, 1)
		Mask.setRotationPoint(0.0f, 0.0f, 0.0f)
		Mask.setTextureSize(128, 64)
		setRotation(Mask, 0.0f, 0.0f, 0.0f)
		Helmet = ModelRenderer(this, 41, 8)
		Helmet.addBox(-4.5f, -9.0f, -4.5f, 9, 4, 9)
		Helmet.setRotationPoint(0.0f, 0.0f, 0.0f)
		Helmet.setTextureSize(128, 64)
		setRotation(Helmet, 0.0f, 0.0f, 0.0f)
		HelmetR = ModelRenderer(this, 21, 13)
		HelmetR.addBox(-6.5f, -3.0f, -4.5f, 1, 5, 9)
		HelmetR.setRotationPoint(0.0f, 0.0f, 0.0f)
		HelmetR.setTextureSize(128, 64)
		setRotation(HelmetR, 0.0f, 0.0f, 0.5235988f)
		HelmetL = ModelRenderer(this, 21, 13)
		HelmetL.mirror = true
		HelmetL.addBox(5.5f, -3.0f, -4.5f, 1, 5, 9)
		HelmetL.setRotationPoint(0.0f, 0.0f, 0.0f)
		HelmetL.setTextureSize(128, 64)
		setRotation(HelmetL, 0.0f, 0.0f, -0.5235988f)
		HelmetB = ModelRenderer(this, 41, 21)
		HelmetB.addBox(-4.5f, -3.0f, 5.5f, 9, 5, 1)
		HelmetB.setRotationPoint(0.0f, 0.0f, 0.0f)
		HelmetB.setTextureSize(128, 64)
		setRotation(HelmetB, 0.5235988f, 0.0f, 0.0f)
		capsthingy = ModelRenderer(this, 21, 0)
		capsthingy.addBox(-4.5f, -6.0f, -6.5f, 9, 1, 2)
		capsthingy.setRotationPoint(0.0f, 0.0f, 0.0f)
		capsthingy.setTextureSize(128, 64)
		setRotation(capsthingy, 0.0f, 0.0f, 0.0f)
		flapR = ModelRenderer(this, 59, 10)
		flapR.addBox(-10.0f, -2.0f, -1.0f, 3, 3, 1)
		flapR.setRotationPoint(0.0f, 0.0f, 0.0f)
		flapR.setTextureSize(128, 64)
		setRotation(flapR, 0.0f, -0.5235988f, 0.5235988f)
		flapL = ModelRenderer(this, 59, 10)
		flapL.mirror = true
		flapL.addBox(7.0f, -2.0f, -1.0f, 3, 3, 1)
		flapL.setRotationPoint(0.0f, 0.0f, 0.0f)
		flapL.setTextureSize(128, 64)
		setRotation(flapL, 0.0f, 0.5235988f, -0.5235988f)
		BeltR = ModelRenderer(this, 76, 44)
		BeltR.addBox(-5.0f, 4.0f, -3.0f, 1, 3, 6)
		BeltR.setRotationPoint(0.0f, 0.0f, 0.0f)
		BeltR.setTextureSize(128, 64)
		setRotation(BeltR, 0.0f, 0.0f, 0.0f)
		Mbelt = ModelRenderer(this, 56, 55)
		Mbelt.addBox(-4.0f, 8.0f, -3.0f, 8, 4, 1)
		Mbelt.setRotationPoint(0.0f, 0.0f, 0.0f)
		Mbelt.setTextureSize(128, 64)
		setRotation(Mbelt, 0.0f, 0.0f, 0.0f)
		MbeltL = ModelRenderer(this, 76, 44)
		MbeltL.addBox(4.0f, 8.0f, -3.0f, 1, 3, 6)
		MbeltL.setRotationPoint(0.0f, 0.0f, 0.0f)
		MbeltL.setTextureSize(128, 64)
		setRotation(MbeltL, 0.0f, 0.0f, 0.0f)
		MbeltR = ModelRenderer(this, 76, 44)
		MbeltR.addBox(-5.0f, 8.0f, -3.0f, 1, 3, 6)
		MbeltR.setRotationPoint(0.0f, 0.0f, 0.0f)
		MbeltR.setTextureSize(128, 64)
		setRotation(MbeltR, 0.0f, 0.0f, 0.0f)
		BeltL = ModelRenderer(this, 76, 44)
		BeltL.addBox(4.0f, 4.0f, -3.0f, 1, 3, 6)
		BeltL.setRotationPoint(0.0f, 0.0f, 0.0f)
		BeltL.setTextureSize(128, 64)
		setRotation(BeltL, 0.0f, 0.0f, 0.0f)
		Chestplate = ModelRenderer(this, 56, 45)
		Chestplate.addBox(-4.0f, 1.0f, -4.0f, 8, 7, 2)
		Chestplate.setRotationPoint(0.0f, 0.0f, 0.0f)
		Chestplate.setTextureSize(128, 64)
		setRotation(Chestplate, 0.0f, 0.0f, 0.0f)
		Backplate = ModelRenderer(this, 36, 45)
		Backplate.addBox(-4.0f, 1.0f, 2.0f, 8, 11, 2)
		Backplate.setRotationPoint(0.0f, 0.0f, 0.0f)
		Backplate.setTextureSize(128, 64)
		setRotation(Backplate, 0.0f, 0.0f, 0.0f)
		ShoulderR = ModelRenderer(this, 56, 35)
		ShoulderR.addBox(-3.5f, -2.5f, -2.5f, 5, 5, 5)
		ShoulderR.setRotationPoint(0.0f, 0.0f, 0.0f)
		ShoulderR.setTextureSize(128, 64)
		setRotation(ShoulderR, 0.0f, 0.0f, 0.0f)
		GauntletR = ModelRenderer(this, 100, 26)
		GauntletR.addBox(-3.5f, 3.5f, -2.5f, 2, 6, 5)
		GauntletR.setRotationPoint(0.0f, 0.0f, 0.0f)
		GauntletR.setTextureSize(128, 64)
		setRotation(GauntletR, 0.0f, 0.0f, 0.0f)
		GauntletstrapR1 = ModelRenderer(this, 84, 31)
		GauntletstrapR1.addBox(-1.5f, 3.5f, -2.5f, 3, 1, 5)
		GauntletstrapR1.setRotationPoint(0.0f, 0.0f, 0.0f)
		GauntletstrapR1.setTextureSize(128, 64)
		setRotation(GauntletstrapR1, 0.0f, 0.0f, 0.0f)
		GauntletstrapR2 = ModelRenderer(this, 84, 31)
		GauntletstrapR2.addBox(-1.5f, 6.5f, -2.5f, 3, 1, 5)
		GauntletstrapR2.setRotationPoint(0.0f, 0.0f, 0.0f)
		GauntletstrapR2.setTextureSize(128, 64)
		setRotation(GauntletstrapR2, 0.0f, 0.0f, 0.0f)
		ShoulderplateRtop = ModelRenderer(this, 110, 37)
		ShoulderplateRtop.addBox(-5.5f, -2.5f, -3.5f, 2, 1, 7)
		ShoulderplateRtop.setRotationPoint(0.0f, 0.0f, 0.0f)
		ShoulderplateRtop.setTextureSize(128, 64)
		setRotation(ShoulderplateRtop, 0.0f, 0.0f, 0.4363323f)
		ShoulderplateR1 = ModelRenderer(this, 110, 45)
		ShoulderplateR1.addBox(-4.5f, -1.5f, -3.5f, 1, 4, 7)
		ShoulderplateR1.setRotationPoint(0.0f, 0.0f, 0.0f)
		ShoulderplateR1.setTextureSize(128, 64)
		setRotation(ShoulderplateR1, 0.0f, 0.0f, 0.4363323f)
		ShoulderplateR2 = ModelRenderer(this, 94, 45)
		ShoulderplateR2.addBox(-3.5f, 1.5f, -3.5f, 1, 3, 7)
		ShoulderplateR2.setRotationPoint(0.0f, 0.0f, 0.0f)
		ShoulderplateR2.setTextureSize(128, 64)
		setRotation(ShoulderplateR2, 0.0f, 0.0f, 0.4363323f)
		ShoulderplateR3 = ModelRenderer(this, 94, 45)
		ShoulderplateR3.addBox(-2.5f, 3.5f, -3.5f, 1, 3, 7)
		ShoulderplateR3.setRotationPoint(0.0f, 0.0f, 0.0f)
		ShoulderplateR3.setTextureSize(128, 64)
		setRotation(ShoulderplateR3, 0.0f, 0.0f, 0.4363323f)
		ShoulderL = ModelRenderer(this, 56, 35)
		ShoulderL.mirror = true
		ShoulderL.addBox(-1.5f, -2.5f, -2.5f, 5, 5, 5)
		ShoulderL.setRotationPoint(0.0f, 0.0f, 0.0f)
		ShoulderL.setTextureSize(128, 64)
		setRotation(ShoulderL, 0.0f, 0.0f, 0.0f)
		GauntletL = ModelRenderer(this, 114, 26)
		GauntletL.addBox(1.5f, 3.5f, -2.5f, 2, 6, 5)
		GauntletL.setRotationPoint(0.0f, 0.0f, 0.0f)
		GauntletL.setTextureSize(128, 64)
		setRotation(GauntletL, 0.0f, 0.0f, 0.0f)
		Gauntletstrapl1 = ModelRenderer(this, 84, 31)
		Gauntletstrapl1.mirror = true
		Gauntletstrapl1.addBox(-1.5f, 3.5f, -2.5f, 3, 1, 5)
		Gauntletstrapl1.setRotationPoint(0.0f, 0.0f, 0.0f)
		Gauntletstrapl1.setTextureSize(128, 64)
		setRotation(Gauntletstrapl1, 0.0f, 0.0f, 0.0f)
		GauntletstrapL2 = ModelRenderer(this, 84, 31)
		GauntletstrapL2.mirror = true
		GauntletstrapL2.addBox(-1.5f, 6.5f, -2.5f, 3, 1, 5)
		GauntletstrapL2.setRotationPoint(0.0f, 0.0f, 0.0f)
		GauntletstrapL2.setTextureSize(128, 64)
		setRotation(GauntletstrapL2, 0.0f, 0.0f, 0.0f)
		ShoulderplateLtop = ModelRenderer(this, 110, 37)
		ShoulderplateLtop.mirror = true
		ShoulderplateLtop.addBox(3.5f, -2.5f, -3.5f, 2, 1, 7)
		ShoulderplateLtop.setRotationPoint(0.0f, 0.0f, 0.0f)
		ShoulderplateLtop.setTextureSize(128, 64)
		setRotation(ShoulderplateLtop, 0.0f, 0.0f, -0.4363323f)
		ShoulderplateL1 = ModelRenderer(this, 110, 45)
		ShoulderplateL1.mirror = true
		ShoulderplateL1.addBox(3.5f, -1.5f, -3.5f, 1, 4, 7)
		ShoulderplateL1.setRotationPoint(0.0f, 0.0f, 0.0f)
		ShoulderplateL1.setTextureSize(128, 64)
		setRotation(ShoulderplateL1, 0.0f, 0.0f, -0.4363323f)
		ShoulderplateL2 = ModelRenderer(this, 94, 45)
		ShoulderplateL2.mirror = true
		ShoulderplateL2.addBox(2.5f, 1.5f, -3.5f, 1, 3, 7)
		ShoulderplateL2.setRotationPoint(0.0f, 0.0f, 0.0f)
		ShoulderplateL2.setTextureSize(128, 64)
		setRotation(ShoulderplateL2, 0.0f, 0.0f, -0.4363323f)
		ShoulderplateL3 = ModelRenderer(this, 94, 45)
		ShoulderplateL3.mirror = true
		ShoulderplateL3.addBox(1.5f, 3.5f, -3.5f, 1, 3, 7)
		ShoulderplateL3.setRotationPoint(0.0f, 0.0f, 0.0f)
		ShoulderplateL3.setTextureSize(128, 64)
		setRotation(ShoulderplateL3, 0.0f, 0.0f, -0.4363323f)
		LegpanelR1 = ModelRenderer(this, 0, 51)
		LegpanelR1.addBox(-1.0f, 0.5f, -3.5f, 3, 4, 1)
		LegpanelR1.setRotationPoint(0.0f, 0.0f, 0.0f)
		LegpanelR1.setTextureSize(128, 64)
		setRotation(LegpanelR1, -0.4363323f, 0.0f, 0.0f)
		LegpanelR2 = ModelRenderer(this, 8, 51)
		LegpanelR2.addBox(-1.0f, 3.5f, -2.5f, 3, 4, 1)
		LegpanelR2.setRotationPoint(0.0f, 0.0f, 0.0f)
		LegpanelR2.setTextureSize(128, 64)
		setRotation(LegpanelR2, -0.4363323f, 0.0f, 0.0f)
		LegpanelR3 = ModelRenderer(this, 0, 56)
		LegpanelR3.addBox(-1.0f, 6.5f, -1.5f, 3, 3, 1)
		LegpanelR3.setRotationPoint(0.0f, 0.0f, 0.0f)
		LegpanelR3.setTextureSize(128, 64)
		setRotation(LegpanelR3, -0.4363323f, 0.0f, 0.0f)
		LegpanelR4 = ModelRenderer(this, 0, 43)
		LegpanelR4.addBox(-3.0f, 0.5f, -3.5f, 2, 3, 1)
		LegpanelR4.setRotationPoint(0.0f, 0.0f, 0.0f)
		LegpanelR4.setTextureSize(128, 64)
		setRotation(LegpanelR4, -0.4363323f, 0.0f, 0.0f)
		LegpanelR5 = ModelRenderer(this, 0, 47)
		LegpanelR5.addBox(-3.0f, 2.5f, -2.5f, 2, 3, 1)
		LegpanelR5.setRotationPoint(0.0f, 0.0f, 0.0f)
		LegpanelR5.setTextureSize(128, 64)
		setRotation(LegpanelR5, -0.4363323f, 0.0f, 0.0f)
		LegpanelR6 = ModelRenderer(this, 6, 43)
		LegpanelR6.addBox(-3.0f, 4.5f, -1.5f, 2, 3, 1)
		LegpanelR6.setRotationPoint(0.0f, 0.0f, 0.0f)
		LegpanelR6.setTextureSize(128, 64)
		setRotation(LegpanelR6, -0.4363323f, 0.0f, 0.0f)
		SidepanelR1 = ModelRenderer(this, 0, 22)
		SidepanelR1.addBox(-2.5f, 0.5f, -2.5f, 1, 4, 5)
		SidepanelR1.setRotationPoint(0.0f, 0.0f, 0.0f)
		SidepanelR1.setTextureSize(128, 64)
		setRotation(SidepanelR1, 0.0f, 0.0f, 0.4363323f)
		SidepanelR2 = ModelRenderer(this, 0, 31)
		SidepanelR2.addBox(-1.5f, 3.5f, -2.5f, 1, 3, 5)
		SidepanelR2.setRotationPoint(0.0f, 0.0f, 0.0f)
		SidepanelR2.setTextureSize(128, 64)
		setRotation(SidepanelR2, 0.0f, 0.0f, 0.4363323f)
		SidepanelR3 = ModelRenderer(this, 12, 31)
		SidepanelR3.addBox(-0.5f, 5.5f, -2.5f, 1, 3, 5)
		SidepanelR3.setRotationPoint(0.0f, 0.0f, 0.0f)
		SidepanelR3.setTextureSize(128, 64)
		setRotation(SidepanelR3, 0.0f, 0.0f, 0.4363323f)
		BackpanelR1 = ModelRenderer(this, 0, 18)
		BackpanelR1.addBox(-3.0f, 0.5f, 2.5f, 5, 3, 1)
		BackpanelR1.setRotationPoint(0.0f, 0.0f, 0.0f)
		BackpanelR1.setTextureSize(128, 64)
		setRotation(BackpanelR1, 0.4363323f, 0.0f, 0.0f)
		BackpanelR2 = ModelRenderer(this, 0, 18)
		BackpanelR2.addBox(-3.0f, 2.5f, 1.5f, 5, 3, 1)
		BackpanelR2.setRotationPoint(0.0f, 0.0f, 0.0f)
		BackpanelR2.setTextureSize(128, 64)
		setRotation(BackpanelR2, 0.4363323f, 0.0f, 0.0f)
		BackpanelR3 = ModelRenderer(this, 0, 18)
		BackpanelR3.addBox(-3.0f, 4.5f, 0.5f, 5, 3, 1)
		BackpanelR3.setRotationPoint(0.0f, 0.0f, 0.0f)
		BackpanelR3.setTextureSize(128, 64)
		setRotation(BackpanelR3, 0.4363323f, 0.0f, 0.0f)
		BackpanelL3 = ModelRenderer(this, 0, 18)
		BackpanelL3.mirror = true
		BackpanelL3.addBox(-2.0f, 4.5f, 0.5f, 5, 3, 1)
		BackpanelL3.setRotationPoint(0.0f, 0.0f, 0.0f)
		BackpanelL3.setTextureSize(128, 64)
		setRotation(BackpanelL3, 0.4363323f, 0.0f, 0.0f)
		LegpanelL1 = ModelRenderer(this, 0, 51)
		LegpanelL1.mirror = true
		LegpanelL1.addBox(-2.0f, 0.5f, -3.5f, 3, 4, 1)
		LegpanelL1.setRotationPoint(0.0f, 0.0f, 0.0f)
		LegpanelL1.setTextureSize(128, 64)
		setRotation(LegpanelL1, -0.4363323f, 0.0f, 0.0f)
		LegpanelL2 = ModelRenderer(this, 8, 51)
		LegpanelL2.mirror = true
		LegpanelL2.addBox(-2.0f, 3.5f, -2.5f, 3, 4, 1)
		LegpanelL2.setRotationPoint(0.0f, 0.0f, 0.0f)
		LegpanelL2.setTextureSize(128, 64)
		setRotation(LegpanelL2, -0.4363323f, 0.0f, 0.0f)
		LegpanelL3 = ModelRenderer(this, 0, 56)
		LegpanelL3.mirror = true
		LegpanelL3.addBox(-2.0f, 6.5f, -1.5f, 3, 3, 1)
		LegpanelL3.setRotationPoint(0.0f, 0.0f, 0.0f)
		LegpanelL3.setTextureSize(128, 64)
		setRotation(LegpanelL3, -0.4363323f, 0.0f, 0.0f)
		LegpanelL4 = ModelRenderer(this, 0, 43)
		LegpanelL4.mirror = true
		LegpanelL4.addBox(1.0f, 0.5f, -3.5f, 2, 3, 1)
		LegpanelL4.setRotationPoint(0.0f, 0.0f, 0.0f)
		LegpanelL4.setTextureSize(128, 64)
		setRotation(LegpanelL4, -0.4363323f, 0.0f, 0.0f)
		LegpanelL5 = ModelRenderer(this, 0, 47)
		LegpanelL5.mirror = true
		LegpanelL5.addBox(1.0f, 2.5f, -2.5f, 2, 3, 1)
		LegpanelL5.setRotationPoint(0.0f, 0.0f, 0.0f)
		LegpanelL5.setTextureSize(128, 64)
		setRotation(LegpanelL5, -0.4363323f, 0.0f, 0.0f)
		LegpanelL6 = ModelRenderer(this, 6, 43)
		LegpanelL6.mirror = true
		LegpanelL6.addBox(1.0f, 4.5f, -1.5f, 2, 3, 1)
		LegpanelL6.setRotationPoint(0.0f, 0.0f, 0.0f)
		LegpanelL6.setTextureSize(128, 64)
		setRotation(LegpanelL6, -0.4363323f, 0.0f, 0.0f)
		SidepanelL1 = ModelRenderer(this, 0, 22)
		SidepanelL1.mirror = true
		SidepanelL1.addBox(1.5f, 0.5f, -2.5f, 1, 4, 5)
		SidepanelL1.setRotationPoint(0.0f, 0.0f, 0.0f)
		SidepanelL1.setTextureSize(128, 64)
		setRotation(SidepanelL1, 0.0f, 0.0f, -0.4363323f)
		SidepanelL2 = ModelRenderer(this, 0, 31)
		SidepanelL2.mirror = true
		SidepanelL2.addBox(0.5f, 3.5f, -2.5f, 1, 3, 5)
		SidepanelL2.setRotationPoint(0.0f, 0.0f, 0.0f)
		SidepanelL2.setTextureSize(128, 64)
		setRotation(SidepanelL2, 0.0f, 0.0f, -0.4363323f)
		SidepanelL3 = ModelRenderer(this, 12, 31)
		SidepanelL3.mirror = true
		SidepanelL3.addBox(-0.5f, 5.5f, -2.5f, 1, 3, 5)
		SidepanelL3.setRotationPoint(0.0f, 0.0f, 0.0f)
		SidepanelL3.setTextureSize(128, 64)
		setRotation(SidepanelL3, 0.0f, 0.0f, -0.4363323f)
		BackpanelL1 = ModelRenderer(this, 0, 18)
		BackpanelL1.mirror = true
		BackpanelL1.addBox(-2.0f, 0.5f, 2.5f, 5, 3, 1)
		BackpanelL1.setRotationPoint(0.0f, 0.0f, 0.0f)
		BackpanelL1.setTextureSize(128, 64)
		setRotation(BackpanelL1, 0.4363323f, 0.0f, 0.0f)
		BackpanelL2 = ModelRenderer(this, 0, 18)
		BackpanelL2.mirror = true
		BackpanelL2.addBox(-2.0f, 2.5f, 1.5f, 5, 3, 1)
		BackpanelL2.setRotationPoint(0.0f, 0.0f, 0.0f)
		BackpanelL2.setTextureSize(128, 64)
		setRotation(BackpanelL2, 0.4363323f, 0.0f, 0.0f)
		bipedHeadwear.cubeList.clear()
		bipedHead.cubeList.clear()
		bipedHead.addChild(Helmet)
		bipedHead.addChild(HelmetR)
		bipedHead.addChild(HelmetL)
		bipedHead.addChild(HelmetB)
		bipedHead.addChild(capsthingy)
		bipedHead.addChild(flapR)
		bipedHead.addChild(flapL)
		bipedHead.addChild(Mask)
		bipedBody.cubeList.clear()
		
		if (f < 1.0f) {
			bipedBody.addChild(Mbelt)
			bipedBody.addChild(MbeltL)
			bipedBody.addChild(MbeltR)
		} else {
			bipedBody.addChild(BeltR)
			bipedBody.addChild(BeltL)
			bipedBody.addChild(Chestplate)
			bipedBody.addChild(Backplate)
		}
		
		bipedRightArm.cubeList.clear()
		bipedRightArm.addChild(ShoulderR)
		bipedRightArm.addChild(GauntletR)
		bipedRightArm.addChild(GauntletstrapR1)
		bipedRightArm.addChild(GauntletstrapR2)
		bipedRightArm.addChild(ShoulderplateRtop)
		bipedRightArm.addChild(ShoulderplateR1)
		bipedRightArm.addChild(ShoulderplateR2)
		bipedRightArm.addChild(ShoulderplateR3)
		bipedLeftArm.cubeList.clear()
		bipedLeftArm.addChild(ShoulderL)
		bipedLeftArm.addChild(GauntletL)
		bipedLeftArm.addChild(Gauntletstrapl1)
		bipedLeftArm.addChild(GauntletstrapL2)
		bipedLeftArm.addChild(ShoulderplateLtop)
		bipedLeftArm.addChild(ShoulderplateL1)
		bipedLeftArm.addChild(ShoulderplateL2)
		bipedLeftArm.addChild(ShoulderplateL3)
		bipedRightLeg.cubeList.clear()
		bipedRightLeg.addChild(LegpanelR1)
		bipedRightLeg.addChild(LegpanelR2)
		bipedRightLeg.addChild(LegpanelR3)
		bipedRightLeg.addChild(LegpanelR4)
		bipedRightLeg.addChild(LegpanelR5)
		bipedRightLeg.addChild(LegpanelR6)
		bipedRightLeg.addChild(SidepanelR1)
		bipedRightLeg.addChild(SidepanelR2)
		bipedRightLeg.addChild(SidepanelR3)
		bipedRightLeg.addChild(BackpanelR1)
		bipedRightLeg.addChild(BackpanelR2)
		bipedRightLeg.addChild(BackpanelR3)
		bipedLeftLeg.cubeList.clear()
		bipedLeftLeg.addChild(BackpanelL3)
		bipedLeftLeg.addChild(LegpanelL1)
		bipedLeftLeg.addChild(LegpanelL2)
		bipedLeftLeg.addChild(LegpanelL3)
		bipedLeftLeg.addChild(LegpanelL4)
		bipedLeftLeg.addChild(LegpanelL5)
		bipedLeftLeg.addChild(LegpanelL6)
		bipedLeftLeg.addChild(SidepanelL1)
		bipedLeftLeg.addChild(SidepanelL2)
		bipedLeftLeg.addChild(SidepanelL3)
		bipedLeftLeg.addChild(BackpanelL1)
		bipedLeftLeg.addChild(BackpanelL2)
	}
	
	override fun render(entity: Entity?, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
		if (entity !is EntitySkeleton && entity !is EntityZombie) {
			setRotationAngles(f, f1, f2, f3, f4, f5, entity)
		} else {
			setRotationAnglesZombie(f, f1, f2, f3, f4, f5, entity)
		}
		
		if (isChild) {
			val var11 = 2.0f
			GL11.glPushMatrix()
			GL11.glScalef(1.5f / var11, 1.5f / var11, 1.5f / var11)
			GL11.glTranslatef(0.0f, 16.0f * f5, 0.0f)
			bipedHead.render(f5)
			GL11.glPopMatrix()
			GL11.glPushMatrix()
			GL11.glScalef(1.0f / var11, 1.0f / var11, 1.0f / var11)
			GL11.glTranslatef(0.0f, 24.0f * f5, 0.0f)
			bipedBody.render(f5)
			bipedRightArm.render(f5)
			bipedLeftArm.render(f5)
			bipedRightLeg.render(f5)
			bipedLeftLeg.render(f5)
			bipedHeadwear.render(f5)
			GL11.glPopMatrix()
		} else {
			GL11.glPushMatrix()
			GL11.glScalef(1.01f, 1.01f, 1.01f)
			bipedHead.render(f5)
			GL11.glPopMatrix()
			bipedBody.render(f5)
			bipedRightArm.render(f5)
			bipedLeftArm.render(f5)
			bipedRightLeg.render(f5)
			bipedLeftLeg.render(f5)
			bipedHeadwear.render(f5)
		}
	}
	
	private fun setRotation(model: ModelRenderer, x: Float, y: Float, z: Float) {
		model.rotateAngleX = x
		model.rotateAngleY = y
		model.rotateAngleZ = z
	}
	
	fun setRotationAnglesZombie(p_78087_1_: Float, p_78087_2_: Float, p_78087_3_: Float, p_78087_4_: Float, p_78087_5_: Float, p_78087_6_: Float, p_78087_7_: Entity) {
		super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_)
		val f6 = MathHelper.sin(onGround * 3.1415927f)
		val f7 = MathHelper.sin((1.0f - (1.0f - onGround) * (1.0f - onGround)) * 3.1415927f)
		bipedRightArm.rotateAngleZ = 0.0f
		bipedLeftArm.rotateAngleZ = 0.0f
		bipedRightArm.rotateAngleY = -(0.1f - f6 * 0.6f)
		bipedLeftArm.rotateAngleY = 0.1f - f6 * 0.6f
		bipedRightArm.rotateAngleX = -1.5707964f
		bipedLeftArm.rotateAngleX = -1.5707964f
		bipedRightArm.rotateAngleX -= f6 * 1.2f - f7 * 0.4f
		bipedLeftArm.rotateAngleX -= f6 * 1.2f - f7 * 0.4f
		bipedRightArm.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09f) * 0.05f + 0.05f
		bipedLeftArm.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09f) * 0.05f + 0.05f
		bipedRightArm.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067f) * 0.05f
		bipedLeftArm.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067f) * 0.05f
	}
}