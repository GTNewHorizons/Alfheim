package alfheim.common.entity.boss.ai;

import alfheim.common.entity.boss.EntityFlugel;
import net.minecraft.entity.ai.EntityAIBase;

public class AIRegen extends AIBase {

	public AIRegen(EntityFlugel flugel, AITask task) {
		super(flugel, task);
	}

	@Override
	public boolean shouldExecute() {
		if (flugel.getHealth() / flugel.getMaxHealth() < 0.6F) return true;
		return super.shouldExecute();
	}
	
	@Override
	public void startExecuting() {
		flugel.setAITaskTimer(flugel.worldObj.rand.nextInt(flugel.SPAWN_TICKS / 10) + flugel.SPAWN_TICKS / 10);
	}

	@Override
	public boolean continueExecuting() {
		flugel.setHealth(flugel.getHealth() + (flugel.getMaxHealth() - 1F) / flugel.SPAWN_TICKS);
		flugel.motionX = flugel.motionY = flugel.motionZ = 0;
		return canContinue();
	}
}