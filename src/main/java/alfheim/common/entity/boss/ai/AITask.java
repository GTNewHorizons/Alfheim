package alfheim.common.entity.boss.ai;

import java.util.Random;

public enum AITask {
	NONE(0), INVUL(0), REGEN(0.1), TP(0.2), CHASE(0.3), LIGHTNING(0.1), DEATHRAY(0), RAYS(0.2);
	
	static final Random rng = new Random();
	double chance;
	
	AITask(double c) {
		chance = 1. - c;
	}
	
	public static AITask getRand() {
		//return AITask.RAYS;
		AITask rand = values()[rng.nextInt(values().length)];
		if (Math.random() > rand.chance) return rand;
		return getRand();
	}
}