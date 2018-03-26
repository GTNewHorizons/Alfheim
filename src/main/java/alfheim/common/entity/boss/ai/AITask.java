package alfheim.common.entity.boss.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

public enum AITask {
	NONE, INVUL, REGEN, TP, CHASE, DEATHRAY;
	
	static Random rng = new Random();
	static List<AITask> l;
	static {
		(l = Lists.newArrayList(AITask.values())).removeAll(Lists.newArrayList(NONE, INVUL, DEATHRAY));
	}
	
	public static AITask getRand() {
		return l.get(rng.nextInt(l.size()));
	}
}