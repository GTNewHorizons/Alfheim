package alfheim.common.entity.boss.ai.flugel

import alexsocol.asjlib.math.Vector3
import alfheim.common.core.util.D
import alfheim.common.entity.boss.EntityFlugel
import vazkii.botania.common.Botania
import kotlin.math.*

object AIInit: AIBase() {
	
	override fun shouldStart(flugel: EntityFlugel) = flugel.AI.stage == EntityFlugel.Companion.STAGE.INIT
	
	override fun isInterruptible(flugel: EntityFlugel) = false
	
	override fun startExecuting(flugel: EntityFlugel) {
		flugel.AI.timer = EntityFlugel.SPAWN_TICKS
	}
	
	override fun shouldContinue(flugel: EntityFlugel) = --flugel.AI.timer > 0
	
	override fun continueExecuting(flugel: EntityFlugel) {
		flugel.health = flugel.health + (flugel.maxHealth - 1f) / EntityFlugel.SPAWN_TICKS
		flugel.motionZ = 0.0
		flugel.motionY = flugel.motionZ
		flugel.motionX = flugel.motionY
		
		pylonPartickles(false, flugel)
	}
	
	override fun endTask(flugel: EntityFlugel) {
		flugel.AI.stage = EntityFlugel.Companion.STAGE.WAIT
	}
	
	fun pylonPartickles(deathRay: Boolean, flugel: EntityFlugel) {
		val source = flugel.source
		val pos = Vector3.fromEntityCenter(flugel).sub(0.0, 0.2, 0.0)
		
		val miku = flugel.customNameTag == "Hatsune Miku"
		
		for (arr in EntityFlugel.PYLON_LOCATIONS) {
			val x = arr[0]
			val y = arr[1]
			val z = arr[2]
			
			val pylonPos = Vector3((source.posX + x).D, (source.posY + y).D, (source.posZ + z).D)
			var worldTime = flugel.ticksExisted.D
			worldTime /= 5.0
			
			val rad = 0.75f + Math.random().toFloat() * 0.05f
			val xp = pylonPos.x + 0.5 + cos(worldTime) * rad
			val zp = pylonPos.z + 0.5 + sin(worldTime) * rad
			
			val partPos = Vector3(xp, pylonPos.y, zp)
			val mot = pos.copy().sub(partPos).mul(0.04)
			
			var r = (if (deathRay) 0.2f else 0.7f) + Math.random().toFloat() * 0.3f
			var g = Math.random().toFloat() * 0.3f
			var b = (if (deathRay) 0.7f else 0.2f) + Math.random().toFloat() * 0.3f
			
			if (miku) {
				r = Math.random().toFloat() * 0.3f
				g = (if (deathRay) 0.2f else 0.7f) + Math.random().toFloat() * 0.3f
				b = 0.7f + Math.random().toFloat() * 0.3f
			}
			
			Botania.proxy.wispFX(flugel.worldObj, partPos.x, partPos.y, partPos.z, r, g, b, 0.25f + Math.random().toFloat() * 0.1f, -0.075f - Math.random().toFloat() * 0.015f)
			Botania.proxy.wispFX(flugel.worldObj, partPos.x, partPos.y, partPos.z, r, g, b, 0.4f, mot.x.toFloat(), mot.y.toFloat(), mot.z.toFloat())
		}
	}
}