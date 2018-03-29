package alfheim.common.entity.boss.ai;

import java.util.Collections;

import alexsocol.asjlib.ASJUtilities;
import alfheim.common.entity.boss.EntityFlugel;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import vazkii.botania.common.core.helper.Vector3;

public class AIChase extends AIBase {

	public AIChase(EntityFlugel flugel, AITask task) {
		super(flugel, task);
	}

	@Override
	public void startExecuting() {
		flugel.setAITaskTimer(flugel.worldObj.rand.nextInt(300) + 300);
	}

	@Override
	public boolean continueExecuting() {
		flugel.checkCollision();
		if (flugel.getAITaskTimer() % 5 == 0) {
			String name = ASJUtilities.mapGetKey(flugel.playersWhoAttacked, Collections.max(flugel.playersWhoAttacked.values()));
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