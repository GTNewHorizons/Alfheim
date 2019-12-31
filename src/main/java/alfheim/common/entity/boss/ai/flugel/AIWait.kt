package alfheim.common.entity.boss.ai.flugel

import alfheim.common.core.util.boundingBox
import alfheim.common.entity.boss.EntityFlugel
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.DamageSource

@Suppress("UNCHECKED_CAST")
object AIWait: AIBase() {
	
	const val FORCE = "AIWait-force"
	
	override fun shouldStart(flugel: EntityFlugel) = flugel.AI.stage >= EntityFlugel.Companion.STAGE.WAIT && (flugel.AI.extraData[FORCE] as? Boolean ?: true || flugel.playersAround.isEmpty())
	
	override fun startExecuting(flugel: EntityFlugel) {
		flugel.AI.dropState()
		
		flugel.AI.extraData[FORCE] = false
	}
	
	override fun shouldContinue(flugel: EntityFlugel) = !agroToClosest(flugel) && flugel.AI.stage == EntityFlugel.Companion.STAGE.WAIT
	
	override fun continueExecuting(flugel: EntityFlugel) = Unit
	
	fun agroToClosest(flugel: EntityFlugel): Boolean {
		val range = 3.0
		val players = flugel.worldObj.getEntitiesWithinAABB(EntityPlayer::class.java, flugel.boundingBox(range)) as List<EntityPlayer>
		if (players.isNotEmpty()) return flugel.attackEntityFrom(DamageSource.causePlayerDamage(players[0]), 0f)
		return false
	}
}