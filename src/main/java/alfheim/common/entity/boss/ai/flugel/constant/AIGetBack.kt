package alfheim.common.entity.boss.ai.flugel.constant

import alexsocol.asjlib.math.Vector3
import alfheim.common.entity.boss.EntityFlugel
import alfheim.common.entity.boss.ai.flugel.*
import net.minecraft.entity.Entity
import net.minecraft.util.ChunkCoordinates

class AIGetBack(flugel: EntityFlugel): AIConstantExecutable(flugel) {
	
	override fun execute() {
		val src = flugel.source
		getBack(flugel, src)
		if (Vector3.pointDistanceSpace(flugel.posX, flugel.posY, flugel.posZ, src.posX, src.posY, src.posZ) > EntityFlugel.RANGE + 2)
			AITeleport.teleportTo(flugel, src.posX + 0.5, src.posY + 1.6, src.posZ + 0.5)
		
		flugel.playersAround.forEach { getBack(it, src) }
	}
	
	fun getBack(player: Entity, source: ChunkCoordinates) {
		if (Vector3.pointDistanceSpace(player.posX, player.posY, player.posZ, source.posX + 0.5, source.posY + 0.5, source.posZ + 0.5) >= EntityFlugel.RANGE) {
			val motion = Vector3(source.posX + 0.5, source.posY + 0.5, source.posZ + 0.5).sub(Vector3.fromEntityCenter(player)).normalize()
			
			player.motionX = motion.x
			player.motionY = motion.y
			player.motionZ = motion.z
		}
	}
}