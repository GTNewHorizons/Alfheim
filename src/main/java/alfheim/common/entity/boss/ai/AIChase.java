package alfheim.common.entity.boss.ai;

import java.util.Collections;

import alexsocol.asjlib.ASJUtilities;
import alfheim.common.entity.boss.EntityFlugel;
import net.minecraft.entity.player.EntityPlayer;
import vazkii.botania.common.core.helper.Vector3;

public class AIChase extends AIBase {

	public AIChase(EntityFlugel flugel, AITask task) {
		super(flugel, task);
	}

	@Override
	public void startExecuting() {
		int s = flugel.getStage();
		int i = s == 1 ? 300 : s == 2 ? 150 : 50;
		flugel.setAITaskTimer(flugel.worldObj.rand.nextInt(i) + i);
	}

	@Override
	public boolean continueExecuting() {
		flugel.checkCollision();
		if (flugel.getAITaskTimer() % 10 == 0) {
			String name = "";
			try {
				name = ASJUtilities.mapGetKeyOrDefault(flugel.playersWhoAttacked, Collections.max(flugel.playersWhoAttacked.values()), "");
			} catch (Throwable e) {
				e.printStackTrace();
				return canContinue();
			}
			
			EntityPlayer target = flugel.worldObj.getPlayerEntityByName(name);
		
			if (target != null) {
				Vector3 mot = new Vector3(target.posX - flugel.posX, target.posY - flugel.posY, target.posZ - flugel.posZ).normalize();
				flugel.motionX = mot.x;
				flugel.motionY = mot.y;
				flugel.motionZ = mot.z;
			} else {
				flugel.playersWhoAttacked.remove(name);
			}
		}
		
		return canContinue();
	}
}