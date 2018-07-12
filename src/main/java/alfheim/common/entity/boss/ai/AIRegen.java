package alfheim.common.entity.boss.ai;

import alfheim.common.entity.boss.EntityFlugel;

public class AIRegen extends AIBase {
	
	public AIRegen(EntityFlugel flugel, AITask task) {
		super(flugel, task);
	}
	
	@Override
	public void startExecuting() {
		int div = flugel.isHardMode() ? 8 : 10;
		flugel.setAITaskTimer(flugel.worldObj.rand.nextInt(EntityFlugel.SPAWN_TICKS / div) + EntityFlugel.SPAWN_TICKS / div);
	}

	@Override 
	public boolean continueExecuting() {
		flugel.setHealth(flugel.getHealth() + (flugel.getMaxHealth() - 1F) / EntityFlugel.SPAWN_TICKS);
		flugel.motionX = flugel.motionY = flugel.motionZ = 0;
		return canContinue();
	}
}