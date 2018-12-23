package alfheim.common.entity.boss.ai;

import java.util.List;

import alfheim.common.entity.EntityCharge;
import alfheim.common.entity.boss.EntityFlugel;
import net.minecraft.entity.player.EntityPlayer;
import vazkii.botania.common.core.helper.Vector3;

public class AIEnergy extends AIBase {

	int left = 0;
	int max = 0;
	Vector3 oY = new Vector3(0, 1, 0);
	
	public AIEnergy(EntityFlugel flugel, AITask task) {
		super(flugel, task);
	}
	
	@Override
	public void startExecuting() {
		max = flugel.isHardMode() ? 10 : 5;
		left = max;
		flugel.setAITaskTimer(100);;
	}
	
	@Override
	public boolean continueExecuting() {
		if (flugel.getAITaskTimer() % 20 == 0) {
			--left;
			Vector3 look = new Vector3(flugel.getLookVec()).multiply(1.5).rotate(Math.toRadians((-45f + left * (90f / max))), oY);
			List<EntityPlayer> list = flugel.getPlayersAround();
			if (list.isEmpty()) return false;
			EntityPlayer target = list.get(flugel.worldObj.rand.nextInt(list.size()));
			
			Vector3 pos = Vector3.fromEntityCenter(flugel).add(look);
			Vector3 motion = Vector3.fromEntityCenter(target).sub(pos).normalize();
			
			EntityCharge charge = new EntityCharge(flugel.worldObj, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);
			flugel.worldObj.spawnEntityInWorld(charge);
		}
		return canContinue();
	}
}