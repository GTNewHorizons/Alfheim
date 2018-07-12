package alfheim.common.entity.boss.ai;

import alfheim.common.entity.boss.EntityFlugel;

public enum AITask {
	
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
	public final double chance;
	/** Insta-AIs can't be selected twice in a row */
	public final boolean instant;
	/** Stage required for execurtion */
	public final int stage;
	
	AITask(double c, boolean i, int s) {
		chance = 1. - c;
		instant = i;
		stage = s;
	}
}