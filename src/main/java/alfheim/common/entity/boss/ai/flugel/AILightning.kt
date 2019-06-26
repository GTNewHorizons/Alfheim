package alfheim.common.entity.boss.ai.flugel

import java.util.HashSet

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.common.entity.EntityLightningMark
import alfheim.common.entity.boss.EntityFlugel
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ChunkCoordinates

class AILightning(flugel: EntityFlugel, task: AITask): AIBase(flugel, task) {
	
	val randomPlayers: Set<EntityPlayer>
		get() {
			val players = flugel.playersAround
			if (players.isEmpty()) return HashSet(0)
			var count = flugel.worldObj.rand.nextInt(players.size) + 1
			val set = HashSet<EntityPlayer>(count)
			while (count > 0) {
				val player = flugel.worldObj.rand.nextInt(players.size)
				if (!set.contains(players[player])) {
					set.add(players[player])
					--count
				}
			}
			return set
		}
	
	override fun startExecuting() {
		flugel.aiTaskTimer = 20
		for (player in randomPlayers) player.worldObj.spawnEntityInWorld(EntityLightningMark(player.worldObj, player.posX, player.posY, player.posZ))
		if (flugel.isHardMode) {
			val src = flugel.source
			for (i in 0 until ASJUtilities.randInBounds(flugel.worldObj.rand, 5, 10)) {
				val vec3 = Vector3(ASJUtilities.randInBounds(flugel.worldObj.rand, -EntityFlugel.RANGE, EntityFlugel.RANGE).toDouble(), ASJUtilities.randInBounds(flugel.worldObj.rand, -EntityFlugel.RANGE, EntityFlugel.RANGE).toDouble(), ASJUtilities.randInBounds(flugel.worldObj.rand, -EntityFlugel.RANGE, EntityFlugel.RANGE).toDouble()).normalize().mul(EntityFlugel.RANGE.toDouble())
				flugel.worldObj.spawnEntityInWorld(EntityLightningMark(flugel.worldObj, src.posX + vec3.x, src.posY + vec3.y, src.posZ + vec3.z))
			}
		}
	}
	
	override fun continueExecuting(): Boolean {
		return canContinue()
	}
}