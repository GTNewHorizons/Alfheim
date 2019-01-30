package alfheim.common.entity.boss.ai.flugel;

import alfheim.common.entity.boss.EntityFlugel;

public class AIWait extends AIBase {

	public AIWait(EntityFlugel flugel, AITask task) {
		super(flugel, task);
	}

	@Override
	public void startExecuting() {
		flugel.setAITaskTimer(Integer.MAX_VALUE);
	}

	@Override
	public boolean continueExecuting() {
		return true;
	}
}