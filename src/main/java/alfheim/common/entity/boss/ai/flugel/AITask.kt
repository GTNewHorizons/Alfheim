package alfheim.common.entity.boss.ai.flugel

import alfheim.common.entity.boss.EntityFlugel

private var pr_count = 0

enum class AITask constructor(
	/** Chance to activate (random value MUST be lower than that value) */
	val chance: Double,
	/** Insta-AIs can't be selected twice in a row  */
	val instant: Boolean,
	/** Stage required for execurtion  */
	val stage: Int,
	/** AI implementation */
	val ai: AIBase,
	/** AI activation priority */
	val priority: Int
) {
	INIT(1.0, false, EntityFlugel.STAGE_INIT, AIInit, pr_count++),
	
	WAIT(1.0, false, EntityFlugel.STAGE_INITED, AIWait, pr_count++),
	
	DEATHRAY(1.0, false, EntityFlugel.STAGE_DEATHRAY, AIDeathray, pr_count++),
	
	REGEN(0.1, true, EntityFlugel.STAGE_MAGIC, AIRegen, pr_count),
	LIGHTNING(0.1, true, EntityFlugel.STAGE_MAGIC, AILightning, pr_count),
	RAYS(0.2, true, EntityFlugel.STAGE_MAGIC, AIRays, pr_count),
	DARK(0.2, true, EntityFlugel.STAGE_MAGIC, AIEnergy, pr_count++),
	
	TP(0.2, false, EntityFlugel.STAGE_AGGRO, AITeleport, pr_count),
	CHASE(0.3, false, EntityFlugel.STAGE_AGGRO, AIChase, pr_count++),
	;
	
	companion object {
		val prioritySorted: HashMap<Int, ArrayList<AITask>> = hashMapOf(*values().groupBy { it.priority }.map { it.key to ArrayList<AITask>(it.value) }.toTypedArray())
		val priorityList = prioritySorted.keys.sorted()
	}
}