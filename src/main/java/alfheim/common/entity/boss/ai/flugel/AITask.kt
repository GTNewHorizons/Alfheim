package alfheim.common.entity.boss.ai.flugel

import alfheim.common.entity.boss.EntityFlugel

enum class AITask constructor(
	c: Double,
	/** Insta-AIs can't be selected twice in a row  */
	val instant: Boolean,
	/** Stage required for execurtion  */
	val stage: Int,
) {
	
	NONE(0.0, false, 0),
	INVUL(0.0, false, 0),
	TP(0.2, false, EntityFlugel.STAGE_AGGRO),
	CHASE(0.3, false, EntityFlugel.STAGE_AGGRO),
	REGEN(0.1, true, EntityFlugel.STAGE_MAGIC),
	LIGHTNING(0.1, true, EntityFlugel.STAGE_MAGIC),
	RAYS(0.2, true, EntityFlugel.STAGE_MAGIC),
	DARK(0.2, true, EntityFlugel.STAGE_MAGIC),
	DEATHRAY(0.0, false, EntityFlugel.STAGE_MAGIC);
	
	/** Chance to **NOT** to apply  */
	val chance = 1.0 - c
}