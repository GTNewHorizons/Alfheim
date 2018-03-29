package alfheim.common.entity.boss.ai;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import alfheim.common.entity.EntityLightningMark;
import alfheim.common.entity.boss.EntityFlugel;
import net.minecraft.entity.player.EntityPlayer;

public class AILightning extends AIBase {

	public AILightning(EntityFlugel flugel, AITask task) {
		super(flugel, task);
	}

	@Override
	public void startExecuting() {
		flugel.setAITaskTimer(5);
		for (EntityPlayer player : getRandomPlayers()) player.worldObj.spawnEntityInWorld(new EntityLightningMark(player.worldObj, player.posX, player.posY, player.posZ));
	}
	
	public Set<EntityPlayer> getRandomPlayers() {
		List<EntityPlayer> players = flugel.getPlayersAround();
		int count = flugel.worldObj.rand.nextInt(players.size()) + 1;
		Set<EntityPlayer> set = new HashSet<EntityPlayer>(count);
		while (count > 0) {
			int player = flugel.worldObj.rand.nextInt(players.size());
			if (!set.contains(players.get(player))) {
				set.add(players.get(player));
				--count;
			}
		}
		return set;
	}

	@Override
	public boolean continueExecuting() {
		return canContinue();
	}
}