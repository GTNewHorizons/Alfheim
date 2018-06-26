package alfheim.common.entity.boss.ai;

import alexsocol.asjlib.ASJUtilities
import alfheim.common.entity.EntityLightningMark
import alfheim.common.entity.boss.EntityFlugel
import net.minecraft.entity.player.EntityPlayer
import vazkii.botania.common.core.helper.Vector3
import java.util.HashSet

class AILightning(flugel: EntityFlugel, task: AITask) : AIBase(flugel, task) {

	fun getRandomPlayers(): Set<EntityPlayer> {
		val players = flugel.getPlayersAround()
		if (players.isEmpty()) return HashSet<EntityPlayer>(0)
		var count = flugel.worldObj.rand.nextInt(players.size) + 1
		val set = HashSet<EntityPlayer>(count)
		while (count > 0) {
			val player = flugel.worldObj.rand.nextInt(players.size)
			if (!set.contains(players.get(player))) {
				set.add(players.get(player))
				--count
			}
		}
		return set
	}

	override fun startExecuting() {
		flugel.setAITaskTimer(20)
		for (player in getRandomPlayers()) player.worldObj.spawnEntityInWorld(EntityLightningMark(player.worldObj, player.posX, player.posY, player.posZ))
		if (flugel.isHardMode()) {
			val src = flugel.getSource()
			for (i in 0 until ASJUtilities.randInBounds(flugel.worldObj.rand, 5, 10)) {
				val vec3 = Vector3(ASJUtilities.randInBounds(flugel.worldObj.rand, -EntityFlugel.RANGE, EntityFlugel.RANGE).toDouble(), ASJUtilities.randInBounds(flugel.worldObj.rand, -EntityFlugel.RANGE, EntityFlugel.RANGE).toDouble(), ASJUtilities.randInBounds(flugel.worldObj.rand, -EntityFlugel.RANGE, EntityFlugel.RANGE).toDouble()).normalize().multiply(EntityFlugel.RANGE.toDouble())
				flugel.worldObj.spawnEntityInWorld(EntityLightningMark(flugel.worldObj, src.posX + vec3.x, src.posY + vec3.y, src.posZ + vec3.z))
			}
		}
	}

	override fun continueExecuting(): Boolean = canContinue()
}