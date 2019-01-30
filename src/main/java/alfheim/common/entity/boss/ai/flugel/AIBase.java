package alfheim.common.entity.boss.ai.flugel;

import alfheim.common.entity.boss.EntityFlugel;
import net.minecraft.entity.ai.EntityAIBase;

public abstract class AIBase extends EntityAIBase {
	
	EntityFlugel flugel;
	AITask task;
	
	public AIBase(EntityFlugel flugel, AITask task) {
		this.flugel = flugel;
		this.task = task;
		setMutexBits(5);
	}
	
	@Override
	public boolean shouldExecute() {
		return flugel.getHealth() > 0 && flugel.getAITask().equals(task) && flugel.getAITaskTimer() == 0;
	}

	@Override
	public abstract void startExecuting();
	
	public boolean canContinue() {
		if (flugel.getHealth() <= 0 || !flugel.getAITask().equals(task)) return false;
		flugel.setAITaskTimer(flugel.getAITaskTimer() - 1);
		return flugel.getAITaskTimer() > 0;
	}

	@Override
	public abstract boolean continueExecuting();
	
	@Override
	public void resetTask() {
		flugel.setAITaskTimer(0);
		flugel.setAITask(flugel.nextTask());
	}
}