package alfheim.client.model.entity

import alexsocol.asjlib.F
import alfheim.common.entity.boss.EntityRook
import net.minecraft.client.model.*
import net.minecraft.entity.*
import net.minecraft.util.MathHelper
import org.lwjgl.opengl.GL11.glRotated

/**
 * Rook  - XJustaguyX
 * Created using Tabula 4.1.1
 */
object ModelEntityRook: ModelBase() {
	
	val lef_side_bodypiece: ModelRenderer
	val head2: ModelRenderer
	val lef_arm1: ModelRenderer
	val righ_side_bodypiece: ModelRenderer
	val crotch: ModelRenderer
	val upper_body_deco: ModelRenderer
	val lef_tower_side: ModelRenderer
	val head1: ModelRenderer
	val righ_leg1: ModelRenderer
	val body_main: ModelRenderer
	val lef_lower_body_side: ModelRenderer
	val top_body_piece: ModelRenderer
	val righ_lower_body_side: ModelRenderer
	val lef_upper_body_side: ModelRenderer
	val lef_lower_body_side_1: ModelRenderer
	val lower_top_body_piece: ModelRenderer
	val lower_body_deco: ModelRenderer
	val righ_arm1: ModelRenderer
	val lef_leg1: ModelRenderer
	val main_towers_base: ModelRenderer
	val righ_tower1: ModelRenderer
	val righ_tower1_1: ModelRenderer
	val mid_tower_base: ModelRenderer
	val mid_tower_lower: ModelRenderer
	val righ_tower_side: ModelRenderer
	val catapult_front_base: ModelRenderer
	val mid_tower_upper: ModelRenderer
	val lef_arm2: ModelRenderer
	val lef_arm_deco_upper2: ModelRenderer
	val lef_arm_deco_upper1: ModelRenderer
	val lef_arm_deco_lower1: ModelRenderer
	val lef_arm_deco_lower2: ModelRenderer
	val hummer_handle: ModelRenderer
	val hummer_head: ModelRenderer
	val hummer_head2: ModelRenderer
	val lef_arm_deco3: ModelRenderer
	val lef_arm_deco4: ModelRenderer
	val lef_arm_deco2: ModelRenderer
	val lef_arm_deco5: ModelRenderer
	val lef_arm_deco6: ModelRenderer
	val lef_arm_deco1: ModelRenderer
	val righ_leg2: ModelRenderer
	val righ_leg_deco_upper1: ModelRenderer
	val righ_leg_deco_upper2: ModelRenderer
	val righ_leg_knee_deco_mid: ModelRenderer
	val righ_leg_deco_lower: ModelRenderer
	val righ_foot: ModelRenderer
	val righ_leg_knee_deco_upper: ModelRenderer
	val righ_fing1: ModelRenderer
	val righ_fing3: ModelRenderer
	val righ_fing2: ModelRenderer
	val righ_arm2: ModelRenderer
	val righ_arm_deco_upper2: ModelRenderer
	val righ_arm_deco_upper1: ModelRenderer
	val righ_arm_deco: ModelRenderer
	val righ_arm_deco_lower1: ModelRenderer
	val righ_arm_deco_lower2: ModelRenderer
	val lef_leg2: ModelRenderer
	val lef_leg_deco_upper1: ModelRenderer
	val lef_leg_deco_upper2: ModelRenderer
	val lef_leg_knee_deco_mid: ModelRenderer
	val lef_leg_deco_lower: ModelRenderer
	val lef_foot: ModelRenderer
	val lef_leg_knee_deco_upper: ModelRenderer
	val lef_fing1: ModelRenderer
	val lef_fing3: ModelRenderer
	val lef_fing2: ModelRenderer
	val righ_tower2: ModelRenderer
	val righ_tower3: ModelRenderer
	val righ_tower_flagpole1: ModelRenderer
	val lef_tower_deco: ModelRenderer
	val righ_tower_flagpole2: ModelRenderer
	val righ_tower_flagpole4: ModelRenderer
	val righ_tower_flagpole_mid: ModelRenderer
	val righ_tower_flagpole3: ModelRenderer
	val righ_tower_flag: ModelRenderer
	val righ_tower2_1: ModelRenderer
	val righ_tower3_1: ModelRenderer
	val righ_tower_flagpole1_1: ModelRenderer
	val righ_tower_flagpole2_1: ModelRenderer
	val righ_tower_flagpole4_1: ModelRenderer
	val righ_tower_flagpole_mid_1: ModelRenderer
	val righ_tower_flagpole3_1: ModelRenderer
	val righ_tower_flag_1: ModelRenderer
	val catapult_back_base: ModelRenderer
	val catapult_stick_holder: ModelRenderer
	val catapult_stick: ModelRenderer
	
	init {
		textureWidth = 440
		textureHeight = 220
		righ_arm_deco_upper2 = ModelRenderer(this, 111, 105)
		righ_arm_deco_upper2.setRotationPoint(0f, 0f, 0f)
		righ_arm_deco_upper2.addBox(-10.5f, 9.9f, -6.5f, 13, 5, 15, 0f)
		lef_leg_deco_upper1 = ModelRenderer(this, 234, 82)
		lef_leg_deco_upper1.setRotationPoint(0f, 0f, 0f)
		lef_leg_deco_upper1.addBox(-5.8f, 7.3f, -8f, 13, 4, 16, 0f)
		righ_arm_deco = ModelRenderer(this, 102, 141)
		righ_arm_deco.setRotationPoint(0f, 0f, 0f)
		righ_arm_deco.addBox(-15f, -2.1f, -1f, 4, 6, 2, 0f)
		setRotateAngle(righ_arm_deco, -0.016231562043547264f, 0.0609119908946021f, -0.5202826500195096f)
		upper_body_deco = ModelRenderer(this, 201, 131)
		upper_body_deco.setRotationPoint(0f, 0f, 0f)
		upper_body_deco.addBox(-7.5f, -19f, -8.5f, 14, 2, 17, 0f)
		setRotateAngle(upper_body_deco, -0.03490658503988659f, 0f, 0f)
		lower_top_body_piece = ModelRenderer(this, 268, 128)
		lower_top_body_piece.setRotationPoint(0f, 0f, 0f)
		lower_top_body_piece.addBox(-11f, -24.2f, -8.5f, 21, 5, 17, 0f)
		setRotateAngle(lower_top_body_piece, -0.03490658503988659f, 0f, 0f)
		hummer_head2 = ModelRenderer(this, 336, 183)
		hummer_head2.setRotationPoint(-3.93f, -5.6f, -0.8f)
		hummer_head2.addBox(48f, -26.2f, 27.9f, 8, 18, 8, 0f)
		setRotateAngle(hummer_head2, -0.03752457891787808f, 5.235987755982988E-4f, 0.01710422666954443f)
		main_towers_base = ModelRenderer(this, 132, 4)
		main_towers_base.setRotationPoint(0f, 0f, 0f)
		main_towers_base.addBox(-25f, -41f, -8f, 50, 11, 16, 0f)
		setRotateAngle(main_towers_base, -0.03490658503988659f, 0f, 0f)
		catapult_front_base = ModelRenderer(this, 352, 128)
		catapult_front_base.setRotationPoint(0f, 0f, 0f)
		catapult_front_base.addBox(-5f, -61f, 1f, 10, 7, 2, 0f)
		setRotateAngle(catapult_front_base, -0.03490658503988659f, 0f, 0f)
		catapult_stick = ModelRenderer(this, 352, 100)
		catapult_stick.setRotationPoint(0f, 0f, 0f)
		catapult_stick.addBox(12f, -56.5f, -1f, 11, 2, 2, 0f)
		setRotateAngle(catapult_stick, -0.0027729119270238902f, 0.013636742376135986f, -0.4012066063980263f)
		righ_leg1 = ModelRenderer(this, 0, 164)
		righ_leg1.setRotationPoint(-4f, -14f, 0f)
		righ_leg1.addBox(-6.3f, -1.7f, -7f, 11, 20, 14, 0f)
		setRotateAngle(righ_leg1, 0.12217304763960307f, 0f, 0.15707963267948966f)
		righ_lower_body_side = ModelRenderer(this, 135, 39)
		righ_lower_body_side.setRotationPoint(0f, 0f, 0f)
		righ_lower_body_side.addBox(-9.3f, -25.2f, -8.5f, 6, 4, 17, 0f)
		setRotateAngle(righ_lower_body_side, -0.03490658503988659f, 0f, -0.14364059743913332f)
		righ_leg2 = ModelRenderer(this, 53, 169)
		righ_leg2.setRotationPoint(0f, 0f, 0f)
		righ_leg2.addBox(-6.2f, 15.9f, -2.5f, 11, 15, 14, 0f)
		setRotateAngle(righ_leg2, -0.2617993877991494f, 0f, 0f)
		righ_tower_flagpole3 = ModelRenderer(this, 350, 144)
		righ_tower_flagpole3.setRotationPoint(0f, 0f, 0f)
		righ_tower_flagpole3.addBox(8.15f, -63f, -0.5f, 1, 7, 1, 0f)
		setRotateAngle(righ_tower_flagpole3, -0.0029670597283903604f, 0.014137166941154071f, -0.41870448755343964f)
		lef_tower_side = ModelRenderer(this, 268, 11)
		lef_tower_side.setRotationPoint(0f, 0f, 0f)
		lef_tower_side.addBox(13.2f, -52.7f, -6.05f, 3, 11, 12, 0f)
		setRotateAngle(lef_tower_side, -0.03490658503988659f, 0f, -0.17453292519943295f)
		lef_leg1 = ModelRenderer(this, 0, 164)
		lef_leg1.setRotationPoint(4f, -14f, 0f)
		lef_leg1.addBox(-4.8f, -1.7f, -7f, 11, 20, 14, 0f)
		setRotateAngle(lef_leg1, 0.12217304763960307f, 0f, -0.15707963267948966f)
		mid_tower_lower = ModelRenderer(this, 264, 54)
		mid_tower_lower.setRotationPoint(0f, 0f, 0f)
		mid_tower_lower.addBox(-7f, -50f, -7f, 14, 6, 14, 0f)
		setRotateAngle(mid_tower_lower, -0.03490658503988659f, 0f, 0f)
		lef_leg_deco_lower = ModelRenderer(this, 298, 103)
		lef_leg_deco_lower.setRotationPoint(0f, 0f, 0f)
		lef_leg_deco_lower.addBox(-5.3f, 23.9f, -3.05f, 12, 7, 15, 0f)
		righ_arm2 = ModelRenderer(this, 55, 92)
		righ_arm2.setRotationPoint(0f, 0f, 0f)
		righ_arm2.addBox(-10f, 16.3f, -11.2f, 12, 19, 14, 0f)
		setRotateAngle(righ_arm2, 0.296705972839036f, 0f, 0f)
		righ_tower_flagpole4 = ModelRenderer(this, 350, 144)
		righ_tower_flagpole4.setRotationPoint(0f, 0f, 0f)
		righ_tower_flagpole4.addBox(-18.25f, -57f, -23.5f, 1, 6, 1, 0f)
		setRotateAngle(righ_tower_flagpole4, -0.3869394951671429f, 0.014137166941154071f, -0.41870448755343964f)
		mid_tower_base = ModelRenderer(this, 191, 53)
		mid_tower_base.setRotationPoint(0f, 0f, 0f)
		mid_tower_base.addBox(-9f, -44f, -8.01f, 18, 4, 16, 0f)
		setRotateAngle(mid_tower_base, -0.03490658503988659f, 0f, 0f)
		righ_arm_deco_lower2 = ModelRenderer(this, 170, 105)
		righ_arm_deco_lower2.setRotationPoint(0f, 0f, 0f)
		righ_arm_deco_lower2.addBox(-10.5f, 24.3f, -11.7f, 13, 6, 15, 0f)
		lef_lower_body_side = ModelRenderer(this, 135, 39)
		lef_lower_body_side.setRotationPoint(0f, 0f, 0f)
		lef_lower_body_side.addBox(3f, -25.2f, -8.5f, 6, 4, 17, 0f)
		setRotateAngle(lef_lower_body_side, -0.03490658503988659f, 0f, 0.14364059743913332f)
		righ_leg_knee_deco_mid = ModelRenderer(this, 122, 141)
		righ_leg_knee_deco_mid.setRotationPoint(0f, 0f, 0f)
		righ_leg_knee_deco_mid.addBox(-4.8f, 18.9f, 10.95f, 8, 7, 2, 0f)
		body_main = ModelRenderer(this, 2, 41)
		body_main.setRotationPoint(0f, 0f, 0f)
		body_main.addBox(-10.65f, -30f, -8f, 21, 19, 16, 0f)
		setRotateAngle(body_main, -0.03490658503988659f, 0f, 0f)
		lef_arm_deco4 = ModelRenderer(this, 0, 129)
		lef_arm_deco4.setRotationPoint(-19.42f, 26.11f, 3.21f)
		lef_arm_deco4.addBox(26.5f, -18.3f, -11.7f, 2, 2, 19, 0f)
		righ_tower1 = ModelRenderer(this, 120, 179)
		righ_tower1.setRotationPoint(34.9f, 0f, 0f)
		righ_tower1.addBox(-24.5f, -47f, -7f, 14, 6, 14, 0f)
		setRotateAngle(righ_tower1, -0.03490658503988659f, 0f, 0f)
		lef_arm_deco1 = ModelRenderer(this, 50, 146)
		lef_arm_deco1.setRotationPoint(0f, 0f, 0f)
		lef_arm_deco1.addBox(-5f, 7.8f, 1f, 18, 2, 2, 0f)
		setRotateAngle(lef_arm_deco1, -0.016231562043547264f, 0.0609119908946021f, -0.0033161255787892262f)
		lef_fing2 = ModelRenderer(this, 180, 135)
		lef_fing2.setRotationPoint(0f, 0f, 0f)
		lef_fing2.addBox(4.6f, 28.5f, 6.7f, 2, 9, 4, 0f)
		righ_tower_flagpole2_1 = ModelRenderer(this, 350, 144)
		righ_tower_flagpole2_1.setRotationPoint(0f, 0f, 0f)
		righ_tower_flagpole2_1.addBox(-41.55f, -48.5f, 0f, 1, 7, 1, 0f)
		setRotateAngle(righ_tower_flagpole2_1, -0.42114794850623166f, 0.16615534478986016f, 0.3862413634663451f)
		hummer_handle = ModelRenderer(this, 304, 0)
		hummer_handle.setRotationPoint(-19.42f, 26.21f, -0.43f)
		hummer_handle.addBox(23f, 0f, -13.2f, 3, 3, 40, 0f)
		setRotateAngle(hummer_handle, -0.40142572795869574f, 0.003839724354387525f, 0.017453292519943295f)
		lef_arm_deco_lower2 = ModelRenderer(this, 170, 105)
		lef_arm_deco_lower2.setRotationPoint(0f, 0f, 0f)
		lef_arm_deco_lower2.addBox(-2.5f, 24.3f, -11.7f, 13, 6, 15, 0f)
		lef_fing1 = ModelRenderer(this, 180, 135)
		lef_fing1.setRotationPoint(0f, 0f, 0f)
		lef_fing1.addBox(1f, 28.5f, 6.7f, 2, 9, 4, 0f)
		righ_arm1 = ModelRenderer(this, 0, 88)
		righ_arm1.setRotationPoint(-13f, -30f, 0f)
		righ_arm1.addBox(-10f, -4.1f, -6f, 12, 23, 14, 0f)
		setRotateAngle(righ_arm1, -0.12217304763960307f, 0f, 0.22689280275926282f)
		lef_arm_deco5 = ModelRenderer(this, 0, 129)
		lef_arm_deco5.setRotationPoint(-19.42f, 26.11f, 3.21f)
		lef_arm_deco5.addBox(22.5f, -18.3f, -11.7f, 2, 2, 19, 0f)
		lef_leg2 = ModelRenderer(this, 53, 169)
		lef_leg2.setRotationPoint(0f, 0f, 0f)
		lef_leg2.addBox(-4.8f, 15.9f, -2.5f, 11, 15, 14, 0f)
		setRotateAngle(lef_leg2, -0.2617993877991494f, 0f, 0f)
		lef_foot = ModelRenderer(this, 378, 174)
		lef_foot.setRotationPoint(0f, 0f, 0f)
		lef_foot.addBox(-1f, 28.9f, -7f, 13, 8, 17, 0f)
		setRotateAngle(lef_foot, 0.10297442586766545f, -0.02181661564992912f, 0.15550883635269477f)
		righ_tower_flagpole_mid = ModelRenderer(this, 350, 144)
		righ_tower_flagpole_mid.setRotationPoint(0f, 0f, 0f)
		righ_tower_flagpole_mid.addBox(-18.25f, -65f, -0.5f, 1, 7, 1, 0f)
		setRotateAngle(righ_tower_flagpole_mid, 0.3839724354387525f, 0f, 0f)
		righ_tower1_1 = ModelRenderer(this, 120, 179)
		righ_tower1_1.setRotationPoint(0f, 0f, 0f)
		righ_tower1_1.addBox(-24.5f, -47f, -7f, 14, 6, 14, 0f)
		setRotateAngle(righ_tower1_1, -0.03490658503988659f, 0f, 0f)
		righ_arm_deco_lower1 = ModelRenderer(this, 168, 81)
		righ_arm_deco_lower1.setRotationPoint(0f, 0f, 0f)
		righ_arm_deco_lower1.addBox(-11f, 19.3f, -12.2f, 14, 5, 16, 0f)
		lef_arm_deco_lower1 = ModelRenderer(this, 168, 81)
		lef_arm_deco_lower1.setRotationPoint(0f, 0f, 0f)
		lef_arm_deco_lower1.addBox(-3f, 19.3f, -12.2f, 14, 5, 16, 0f)
		righ_tower_flagpole4_1 = ModelRenderer(this, 350, 144)
		righ_tower_flagpole4_1.setRotationPoint(0f, 0f, 0f)
		righ_tower_flagpole4_1.addBox(-18.25f, -57f, -23.5f, 1, 6, 1, 0f)
		setRotateAngle(righ_tower_flagpole4_1, -0.3869394951671429f, 0.014137166941154071f, -0.41870448755343964f)
		righ_leg_knee_deco_upper = ModelRenderer(this, 148, 145)
		righ_leg_knee_deco_upper.setRotationPoint(0f, 0f, 0f)
		righ_leg_knee_deco_upper.addBox(-4.3f, 16.9f, 10.96f, 7, 3, 2, 0f)
		lef_tower_deco = ModelRenderer(this, 371, 139)
		lef_tower_deco.setRotationPoint(2f, 0f, 0f)
		lef_tower_deco.addBox(-22.92f, -62f, -3.5f, 7, 6, 7, 0f)
		catapult_back_base = ModelRenderer(this, 352, 128)
		catapult_back_base.setRotationPoint(0f, 0f, 0f)
		catapult_back_base.addBox(-5f, -61f, -3f, 10, 7, 2, 0f)
		top_body_piece = ModelRenderer(this, 269, 153)
		top_body_piece.setRotationPoint(0f, 0f, 0f)
		top_body_piece.addBox(-11f, -27f, -9f, 21, 4, 18, 0f)
		setRotateAngle(top_body_piece, -0.03490658503988659f, 0f, 0f)
		lef_arm_deco2 = ModelRenderer(this, 50, 146)
		lef_arm_deco2.setRotationPoint(-19.42f, 26.11f, 3.21f)
		lef_arm_deco2.addBox(14.5f, -18.3f, 1f, 18, 2, 2, 0f)
		righ_tower_flagpole1 = ModelRenderer(this, 350, 144)
		righ_tower_flagpole1.setRotationPoint(0f, -3f, 0f)
		righ_tower_flagpole1.addBox(-18.25f, -55f, 26.4f, 1, 6, 1, 0f)
		setRotateAngle(righ_tower_flagpole1, 0.45378560551852565f, 0f, 0f)
		mid_tower_upper = ModelRenderer(this, 325, 56)
		mid_tower_upper.setRotationPoint(0f, 0f, 0f)
		mid_tower_upper.addBox(-7f, -55f, -6f, 14, 6, 12, 0f)
		setRotateAngle(mid_tower_upper, -0.03490658503988659f, 0f, 0f)
		righ_tower_side = ModelRenderer(this, 268, 11)
		righ_tower_side.setRotationPoint(0f, 0f, 0f)
		righ_tower_side.addBox(-16.2f, -52.7f, -6.05f, 3, 11, 12, 0f)
		setRotateAngle(righ_tower_side, -0.03490658503988659f, 0f, 0.17453292519943295f)
		righ_fing1 = ModelRenderer(this, 180, 135)
		righ_fing1.setRotationPoint(0f, 0f, 0f)
		righ_fing1.addBox(-10f, 28.5f, 6.7f, 2, 9, 4, 0f)
		righ_tower3 = ModelRenderer(this, 239, 182)
		righ_tower3.setRotationPoint(0f, 0f, 0f)
		righ_tower3.addBox(-23f, -58f, -5.5f, 11, 6, 11, 0f)
		righ_tower_flagpole3_1 = ModelRenderer(this, 350, 144)
		righ_tower_flagpole3_1.setRotationPoint(0f, 0f, 0f)
		righ_tower_flagpole3_1.addBox(8.15f, -63f, -0.5f, 1, 7, 1, 0f)
		setRotateAngle(righ_tower_flagpole3_1, -0.0029670597283903604f, 0.014137166941154071f, -0.41870448755343964f)
		lef_arm_deco_upper2 = ModelRenderer(this, 111, 105)
		lef_arm_deco_upper2.setRotationPoint(0f, 0f, 0f)
		lef_arm_deco_upper2.addBox(-2.5f, 9.9f, -6.5f, 13, 5, 15, 0f)
		righ_leg_deco_upper1 = ModelRenderer(this, 234, 82)
		righ_leg_deco_upper1.setRotationPoint(0f, 0f, 0f)
		righ_leg_deco_upper1.addBox(-7.3f, 7.3f, -8f, 13, 4, 16, 0f)
		lef_leg_knee_deco_upper = ModelRenderer(this, 148, 145)
		lef_leg_knee_deco_upper.setRotationPoint(0f, 0f, 0f)
		lef_leg_knee_deco_upper.addBox(-2.8f, 16.9f, 10.96f, 7, 3, 2, 0f)
		head2 = ModelRenderer(this, 67, 0)
		head2.setRotationPoint(0f, 0f, 0f)
		head2.addBox(-7.5f, -40.5f, -4.01f, 15, 14, 16, 0f)
		setRotateAngle(head2, -0.03490658503988659f, 0f, 0f)
		righ_side_bodypiece = ModelRenderer(this, 88, 40)
		righ_side_bodypiece.setRotationPoint(0f, 2f, 0f)
		righ_side_bodypiece.addBox(-9.02f, -34f, -8.06f, 4, 20, 16, 0f)
		setRotateAngle(righ_side_bodypiece, -0.03490658503988659f, 0f, -0.14364059743913332f)
		lower_body_deco = ModelRenderer(this, 303, 74)
		lower_body_deco.setRotationPoint(0f, 0f, 0f)
		lower_body_deco.addBox(-2.5f, -19f, -8.51f, 4, 10, 17, 0f)
		setRotateAngle(lower_body_deco, -0.03490658503988659f, 0f, 0f)
		righ_arm_deco_upper1 = ModelRenderer(this, 105, 82)
		righ_arm_deco_upper1.setRotationPoint(0f, 0f, 0f)
		righ_arm_deco_upper1.addBox(-11f, 6.9f, -7f, 14, 4, 16, 0f)
		righ_tower2 = ModelRenderer(this, 182, 182)
		righ_tower2.setRotationPoint(0f, 0f, 0f)
		righ_tower2.addBox(-24f, -52f, -6.5f, 13, 5, 13, 0f)
		righ_tower_flagpole_mid_1 = ModelRenderer(this, 350, 144)
		righ_tower_flagpole_mid_1.setRotationPoint(0f, 0f, 0f)
		righ_tower_flagpole_mid_1.addBox(-18.25f, -65f, -0.5f, 1, 7, 1, 0f)
		setRotateAngle(righ_tower_flagpole_mid_1, 0.3839724354387525f, 0f, 0f)
		righ_fing2 = ModelRenderer(this, 180, 135)
		righ_fing2.setRotationPoint(0f, 0f, 0f)
		righ_fing2.addBox(-6.2f, 28.5f, 6.7f, 2, 9, 4, 0f)
		head1 = ModelRenderer(this, 0, 0)
		head1.setRotationPoint(0f, 0f, 0f)
		head1.addBox(-7f, -41f, -3.99f, 14, 15, 16, 0f)
		setRotateAngle(head1, -0.03490658503988659f, 0f, 0f)
		lef_leg_deco_upper2 = ModelRenderer(this, 237, 105)
		lef_leg_deco_upper2.setRotationPoint(0f, 0f, 0f)
		lef_leg_deco_upper2.addBox(-5.3f, 12.3f, -7.5f, 12, 6, 15, 0f)
		righ_tower_flag = ModelRenderer(this, 353, 145)
		righ_tower_flag.setRotationPoint(0f, 0f, 0f)
		righ_tower_flag.addBox(-17.25f, -65f, -0.25f, 5, 3, 0, 0f)
		setRotateAngle(righ_tower_flag, -0.0029670597283903604f, -0.014137166941154071f, 0.41870448755343964f)
		lef_upper_body_side = ModelRenderer(this, 135, 39)
		lef_upper_body_side.setRotationPoint(0f, 0f, 0f)
		lef_upper_body_side.addBox(3f, -28.2f, -9f, 6, 4, 18, 0f)
		setRotateAngle(lef_upper_body_side, -0.03490658503988659f, 0f, 0.14364059743913332f)
		righ_leg_deco_lower = ModelRenderer(this, 298, 103)
		righ_leg_deco_lower.setRotationPoint(0f, 0f, 0f)
		righ_leg_deco_lower.addBox(-6.7f, 23.9f, -3.05f, 12, 7, 15, 0f)
		lef_arm_deco6 = ModelRenderer(this, 0, 129)
		lef_arm_deco6.setRotationPoint(-19.42f, 26.11f, 3.21f)
		lef_arm_deco6.addBox(18.5f, -18.3f, -11.7f, 2, 2, 19, 0f)
		righ_tower3_1 = ModelRenderer(this, 239, 182)
		righ_tower3_1.setRotationPoint(0f, 0f, 2f)
		righ_tower3_1.addBox(-23f, -58f, -5.5f, 11, 6, 11, 0f)
		setRotateAngle(righ_tower3_1, 0.03490658503988659f, 0f, 0f)
		righ_tower2_1 = ModelRenderer(this, 182, 182)
		righ_tower2_1.setRotationPoint(0f, 0f, 0f)
		righ_tower2_1.addBox(-24f, -52f, -6.5f, 13, 5, 13, 0f)
		lef_side_bodypiece = ModelRenderer(this, 88, 40)
		lef_side_bodypiece.setRotationPoint(0f, 0f, 0f)
		lef_side_bodypiece.addBox(5.01f, -32f, -7.99f, 4, 20, 16, 0f)
		setRotateAngle(lef_side_bodypiece, -0.03490658503988659f, 0f, 0.14364059743913332f)
		crotch = ModelRenderer(this, 9, 199)
		crotch.setRotationPoint(0f, 0f, 0f)
		crotch.addBox(-3f, -11f, -8f, 5, 4, 16, 0f)
		setRotateAngle(crotch, -0.03490658503988659f, 0f, 0f)
		lef_arm_deco_upper1 = ModelRenderer(this, 105, 82)
		lef_arm_deco_upper1.setRotationPoint(0f, 0f, 0f)
		lef_arm_deco_upper1.addBox(-3f, 6.9f, -7f, 14, 4, 16, 0f)
		lef_leg_knee_deco_mid = ModelRenderer(this, 122, 141)
		lef_leg_knee_deco_mid.setRotationPoint(0f, 0f, 0f)
		lef_leg_knee_deco_mid.addBox(-3.3f, 18.9f, 10.95f, 8, 7, 2, 0f)
		lef_arm_deco3 = ModelRenderer(this, 50, 146)
		lef_arm_deco3.setRotationPoint(-19.42f, 26.11f, 3.21f)
		lef_arm_deco3.addBox(14.5f, -18.3f, -7f, 18, 2, 2, 0f)
		righ_tower_flag_1 = ModelRenderer(this, 353, 145)
		righ_tower_flag_1.setRotationPoint(0f, 0f, 0f)
		righ_tower_flag_1.addBox(-17.25f, -65f, -0.25f, 5, 3, 0, 0f)
		setRotateAngle(righ_tower_flag_1, -0.0029670597283903604f, -0.014137166941154071f, 0.41870448755343964f)
		righ_foot = ModelRenderer(this, 378, 174)
		righ_foot.setRotationPoint(0f, 0f, 0f)
		righ_foot.addBox(-11.4f, 28.9f, -7f, 13, 8, 17, 0f)
		setRotateAngle(righ_foot, 0.10297442586766545f, -0.02181661564992912f, -0.15865042900628454f)
		lef_fing3 = ModelRenderer(this, 180, 135)
		lef_fing3.setRotationPoint(0f, 0f, 0f)
		lef_fing3.addBox(8f, 28.5f, 6.7f, 2, 9, 4, 0f)
		catapult_stick_holder = ModelRenderer(this, 357, 110)
		catapult_stick_holder.setRotationPoint(0f, 0f, 0f)
		catapult_stick_holder.addBox(-1f, -61.5f, -5f, 2, 4, 10, 0f)
		lef_arm1 = ModelRenderer(this, 0, 87)
		lef_arm1.setRotationPoint(13f, -30f, 0f)
		lef_arm1.addBox(-2f, -4.1f, -6f, 12, 23, 14, 0f)
		setRotateAngle(lef_arm1, -0.2792526803190927f, 0f, -0.22689280275926282f)
		lef_lower_body_side_1 = ModelRenderer(this, 135, 39)
		lef_lower_body_side_1.setRotationPoint(0f, 0f, 0f)
		lef_lower_body_side_1.addBox(-9.3f, -28.4f, -9f, 6, 4, 18, 0f)
		setRotateAngle(lef_lower_body_side_1, -0.03490658503988659f, 0f, -0.14364059743913332f)
		hummer_head = ModelRenderer(this, 289, 181)
		hummer_head.setRotationPoint(-24.44f, 21.57f, -1.28f)
		hummer_head.addBox(43.5f, -29f, 26.8f, 10, 17, 10, 0f)
		setRotateAngle(hummer_head, -0.01727875959474386f, 0.008377580409572781f, -1.7453292519943296E-4f)
		righ_tower_flagpole1_1 = ModelRenderer(this, 350, 144)
		righ_tower_flagpole1_1.setRotationPoint(0f, 0f, 0f)
		righ_tower_flagpole1_1.addBox(-18.25f, -55f, 26.3f, 1, 6, 1, 0f)
		setRotateAngle(righ_tower_flagpole1_1, 0.45378560551852565f, 0f, 0f)
		righ_fing3 = ModelRenderer(this, 180, 135)
		righ_fing3.setRotationPoint(0f, 0f, 0f)
		righ_fing3.addBox(-2.3f, 28.5f, 6.7f, 2, 9, 4, 0f)
		righ_tower_flagpole2 = ModelRenderer(this, 350, 144)
		righ_tower_flagpole2.setRotationPoint(0f, 0f, 0f)
		righ_tower_flagpole2.addBox(-41.45f, -48.6f, -0.2f, 1, 7, 1, 0f)
		setRotateAngle(righ_tower_flagpole2, -0.42114794850623166f, 0.16615534478986016f, 0.3862413634663451f)
		lef_arm2 = ModelRenderer(this, 55, 92)
		lef_arm2.setRotationPoint(0f, 0.1f, 0f)
		lef_arm2.addBox(-2f, 16.3f, -11.2f, 12, 19, 14, 0f)
		setRotateAngle(lef_arm2, 0.296705972839036f, 0f, 0f)
		righ_leg_deco_upper2 = ModelRenderer(this, 237, 105)
		righ_leg_deco_upper2.setRotationPoint(0f, 0f, 0f)
		righ_leg_deco_upper2.addBox(-6.8f, 12.3f, -7.5f, 12, 6, 15, 0f)
		righ_arm1.addChild(righ_arm_deco_upper2)
		lef_leg1.addChild(lef_leg_deco_upper1)
		righ_arm1.addChild(righ_arm_deco)
		hummer_head.addChild(hummer_head2)
		catapult_stick_holder.addChild(catapult_stick)
		righ_leg1.addChild(righ_leg2)
		righ_tower_flagpole_mid.addChild(righ_tower_flagpole3)
		lef_leg2.addChild(lef_leg_deco_lower)
		righ_arm1.addChild(righ_arm2)
		righ_tower_flagpole2.addChild(righ_tower_flagpole4)
		righ_arm2.addChild(righ_arm_deco_lower2)
		righ_leg2.addChild(righ_leg_knee_deco_mid)
		lef_arm_deco_upper1.addChild(lef_arm_deco4)
		lef_arm_deco_upper1.addChild(lef_arm_deco1)
		lef_foot.addChild(lef_fing2)
		righ_tower_flagpole1_1.addChild(righ_tower_flagpole2_1)
		lef_arm2.addChild(hummer_handle)
		lef_arm2.addChild(lef_arm_deco_lower2)
		lef_foot.addChild(lef_fing1)
		lef_arm_deco_upper1.addChild(lef_arm_deco5)
		lef_leg1.addChild(lef_leg2)
		lef_leg2.addChild(lef_foot)
		righ_tower_flagpole4.addChild(righ_tower_flagpole_mid)
		righ_arm2.addChild(righ_arm_deco_lower1)
		lef_arm2.addChild(lef_arm_deco_lower1)
		righ_tower_flagpole2_1.addChild(righ_tower_flagpole4_1)
		righ_leg_knee_deco_mid.addChild(righ_leg_knee_deco_upper)
		righ_tower3.addChild(lef_tower_deco)
		catapult_front_base.addChild(catapult_back_base)
		lef_arm_deco_upper1.addChild(lef_arm_deco2)
		righ_tower3.addChild(righ_tower_flagpole1)
		righ_foot.addChild(righ_fing1)
		righ_tower2.addChild(righ_tower3)
		righ_tower_flagpole_mid_1.addChild(righ_tower_flagpole3_1)
		lef_arm1.addChild(lef_arm_deco_upper2)
		righ_leg1.addChild(righ_leg_deco_upper1)
		lef_leg_knee_deco_mid.addChild(lef_leg_knee_deco_upper)
		righ_arm1.addChild(righ_arm_deco_upper1)
		righ_tower1.addChild(righ_tower2)
		righ_tower_flagpole4_1.addChild(righ_tower_flagpole_mid_1)
		righ_foot.addChild(righ_fing2)
		lef_leg1.addChild(lef_leg_deco_upper2)
		righ_tower_flagpole3.addChild(righ_tower_flag)
		righ_leg2.addChild(righ_leg_deco_lower)
		lef_arm_deco_upper1.addChild(lef_arm_deco6)
		righ_tower2_1.addChild(righ_tower3_1)
		righ_tower1_1.addChild(righ_tower2_1)
		lef_arm1.addChild(lef_arm_deco_upper1)
		lef_leg2.addChild(lef_leg_knee_deco_mid)
		lef_arm_deco_upper1.addChild(lef_arm_deco3)
		righ_tower_flagpole3_1.addChild(righ_tower_flag_1)
		righ_leg2.addChild(righ_foot)
		lef_foot.addChild(lef_fing3)
		catapult_back_base.addChild(catapult_stick_holder)
		hummer_handle.addChild(hummer_head)
		righ_tower3_1.addChild(righ_tower_flagpole1_1)
		righ_foot.addChild(righ_fing3)
		righ_tower_flagpole1.addChild(righ_tower_flagpole2)
		lef_arm1.addChild(lef_arm2)
		righ_leg1.addChild(righ_leg_deco_upper2)
	}
	
	override fun render(entity: Entity?, time: Float, amplitude: Float, ticksExisted: Float, yawHead: Float, pitchHead: Float, size: Float) {
		glRotated(180.0, 0.0, 1.0, 0.0)
		setRotationAngles(time, amplitude, ticksExisted, yawHead, pitchHead, size, entity)
		upper_body_deco.render(size)
		lower_top_body_piece.render(size)
		main_towers_base.render(size)
		catapult_front_base.render(size)
		righ_leg1.render(size)
		righ_lower_body_side.render(size)
		lef_tower_side.render(size)
		lef_leg1.render(size)
		mid_tower_lower.render(size)
		mid_tower_base.render(size)
		lef_lower_body_side.render(size)
		body_main.render(size)
		righ_tower1.render(size)
		righ_arm1.render(size)
		righ_tower1_1.render(size)
		top_body_piece.render(size)
		mid_tower_upper.render(size)
		righ_tower_side.render(size)
		head2.render(size)
		righ_side_bodypiece.render(size)
		lower_body_deco.render(size)
		head1.render(size)
		lef_upper_body_side.render(size)
		lef_side_bodypiece.render(size)
		crotch.render(size)
		lef_arm1.render(size)
		lef_lower_body_side_1.render(size)
		glRotated(-180.0, 0.0, 1.0, 0.0)
	}
	
	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	fun setRotateAngle(modelRenderer: ModelRenderer, x: Float, y: Float, z: Float) {
		modelRenderer.rotateAngleX = x
		modelRenderer.rotateAngleY = y
		modelRenderer.rotateAngleZ = z
	}
	
	override fun setRotationAngles(time: Float, amplitude: Float, ticksExisted: Float, yawHead: Float, pitchHead: Float, size: Float, entity: Entity?) {
		righ_arm1.rotateAngleX = MathHelper.cos(time * 0.6662f + Math.PI.F) * 2f * amplitude * 0.25f - Math.toRadians(7.0).F
		righ_arm1.rotateAngleZ = Math.toRadians(13.0).F
		lef_arm1.rotateAngleZ = Math.toRadians(-13.0).F
		righ_leg1.rotateAngleX = MathHelper.cos(time * 0.6662f) * 1.4f * amplitude * 0.5f + Math.toRadians(7.0).F
		lef_leg1.rotateAngleX = MathHelper.cos(time * 0.6662f + Math.PI.F) * 1.4f * amplitude * 0.5f + Math.toRadians(7.0).F
		righ_leg1.rotateAngleZ = Math.toRadians(9.0).F
		lef_leg1.rotateAngleZ = Math.toRadians(-9.0).F
	}
	
	override fun setLivingAnimations(living: EntityLivingBase?, time: Float, amplitude: Float, ticksExisted: Float) {
		if (living is EntityRook) {
			val rook = living as EntityRook?
			val i = rook!!.attackTimer
			
			if (i > 0) {
				lef_arm1.rotateAngleX = -2f + 1.5f * interpolate(i.F - ticksExisted, 20f)
				lef_arm1.rotateAngleX /= -2f
				lef_arm1.rotateAngleX += Math.toRadians(16.0).F
			} else {
				lef_arm1.rotateAngleX = MathHelper.cos(time * 0.6662f) * 2f * amplitude * 0.5f + Math.toRadians(16.0).F
			}
		}
	}
	
	fun interpolate(angle: Float, mod: Float): Float {
		return (Math.abs(angle % mod - mod * 0.5f) - mod * 0.25f) / (mod * 0.25f)
	}
}