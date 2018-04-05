package alfheim.common.entity.boss.ai;

import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alfheim.common.entity.EntityCharge;
import alfheim.common.entity.boss.EntityFlugel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import vazkii.botania.common.core.helper.Vector3;

public class AIEnergy extends AIBase {

	int left = 5;
	Vector3 oY = new Vector3(0, 1, 0);
	
	public AIEnergy(EntityFlugel flugel, AITask task) {
		super(flugel, task);
	}

	@Override
	public void startExecuting() {
		left = 5;
		flugel.setAITaskTimer(100);
	}

	@Override
	public boolean continueExecuting() {
		if (flugel.getAITaskTimer() % 20 == 0) {
			--left;
			Vector3 look = new Vector3(flugel.getLookVec()).multiply(1.5).rotate(Math.toRadians(-45 + left * 22.5), oY);
			List<EntityPlayer> list = flugel.getPlayersAround();
			if (list.isEmpty()) return false;
			EntityPlayer target = list.get(flugel.worldObj.rand.nextInt(list.size()));
			
			Vector3 targetVector = Vector3.fromEntityCenter(target);
			Vector3 pos = Vector3.fromEntityCenter(flugel).add(look);
			Vector3 motion = targetVector.copy().sub(pos).copy().normalize();
			
			EntityCharge charge = new EntityCharge(flugel.worldObj, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);
			flugel.worldObj.spawnEntityInWorld(charge);
		}
		return canContinue();
	}
}