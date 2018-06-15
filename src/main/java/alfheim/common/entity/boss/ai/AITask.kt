package alfheim.common.entity.boss.ai

import alfheim.common.entity.boss.EntityFlugel

/**
 @param c Chance to <b>NOT</b> to apply
 @param instant Insta-AIs can't be selected twice in a row
 @param stage Stage required for execurtion
 */
enum class AITask(c: Double, val instant: Boolean, val stage: Int) {
    
	NONE(0.0, false, 0),
	INVUL(0.0, false, 0),
	TP(0.2, false, EntityFlugel.STAGE_AGGRO),
	CHASE(0.3, false, EntityFlugel.STAGE_AGGRO),
	REGEN(0.1, true, EntityFlugel.STAGE_MAGIC),
	LIGHTNING(0.1, true, EntityFlugel.STAGE_MAGIC),
	RAYS(0.2, true, EntityFlugel.STAGE_MAGIC),
	DARK(0.2, true, EntityFlugel.STAGE_MAGIC),
	DEATHRAY(0.0, false, EntityFlugel.STAGE_MAGIC);
	
	/** Chance to <b>NOT</b> to apply */
	val chance: Double = 1.0 - c
}