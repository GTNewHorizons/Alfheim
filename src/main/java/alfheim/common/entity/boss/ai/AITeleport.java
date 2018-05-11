package alfheim.common.entity.boss.ai;

import alfheim.common.entity.boss.EntityFlugel;

public class AITeleport extends AIBase {

	public AITeleport(EntityFlugel flugel, AITask task) {
		super(flugel, task);
	}

	@Override
	public void startExecuting() {
		flugel.setAITaskTimer(flugel.worldObj.rand.nextInt(100) + 100);
	}

	@Override
	public boolean continueExecuting() {
		if (flugel.getAITaskTimer() % (flugel.isDying() ? 60 : 100) == 0) {
			int tries = 0;
			while(!flugel.teleportRandomly() && tries < 50) tries++;
			if(tries >= 50) flugel.teleportTo(flugel.getSource().posX + 0.5, flugel.getSource().posY + 1.6, flugel.getSource().posZ + 0.5);
		}
		return canContinue();
	}
}