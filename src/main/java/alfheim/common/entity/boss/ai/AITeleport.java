package alfheim.common.entity.boss.ai;

import alfheim.common.entity.boss.EntityFlugel;
import net.minecraft.util.ChunkCoordinates;

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
		if (flugel.getAITaskTimer() % (flugel.isHardMode() ? flugel.isDying() ? 60 : 100 : flugel.isDying() ? 80 : 120) == 0) {
			int tries = 0;
			while (!flugel.teleportRandomly() && tries < 50) tries++;
			if (tries >= 50) {
				ChunkCoordinates src = flugel.getSource();
				flugel.teleportTo(src.posX + 0.5, src.posY + 1.6, src.posZ + 0.5);
			}
		}
		return canContinue();
	}
}