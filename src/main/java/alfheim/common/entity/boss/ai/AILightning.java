package alfheim.common.entity.boss.ai;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.common.entity.EntityLightningMark;
import alfheim.common.entity.boss.EntityFlugel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;

public class AILightning extends AIBase {

	public AILightning(EntityFlugel flugel, AITask task) {
		super(flugel, task);
	}
	
	public Set<EntityPlayer> getRandomPlayers() {
		List<EntityPlayer> players = flugel.getPlayersAround();
		if (players.isEmpty()) return new HashSet<EntityPlayer>(0);
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
	public void startExecuting() {
		flugel.setAITaskTimer(20);
		for (EntityPlayer player : getRandomPlayers()) player.worldObj.spawnEntityInWorld(new EntityLightningMark(player.worldObj, player.posX, player.posY, player.posZ));
		if (flugel.isHardMode()) {
			ChunkCoordinates src = flugel.getSource();
			for (int i = 0; i < ASJUtilities.randInBounds(flugel.worldObj.rand, 5, 10); i++) {
				Vector3 vec3 = new Vector3(ASJUtilities.randInBounds(flugel.worldObj.rand, -EntityFlugel.RANGE, EntityFlugel.RANGE), ASJUtilities.randInBounds(flugel.worldObj.rand, -EntityFlugel.RANGE, EntityFlugel.RANGE), ASJUtilities.randInBounds(flugel.worldObj.rand, -EntityFlugel.RANGE, EntityFlugel.RANGE)).normalize().mul(EntityFlugel.RANGE);
				flugel.worldObj.spawnEntityInWorld(new EntityLightningMark(flugel.worldObj, src.posX + vec3.x, src.posY + vec3.y, src.posZ + vec3.z));
			}
		}
	}

	@Override
	public boolean continueExecuting() {
		return canContinue();
	}
}