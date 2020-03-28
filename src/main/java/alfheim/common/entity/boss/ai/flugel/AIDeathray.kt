package alfheim.common.entity.boss.ai.flugel

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.api.ModInfo
import alfheim.common.entity.boss.EntityFlugel
import vazkii.botania.common.Botania
import vazkii.botania.common.entity.EntityFallingStar
import kotlin.math.*

class AIDeathray(flugel: EntityFlugel, task: AITask): AIBase(flugel, task) {
	
	override fun isInterruptible(): Boolean {
		return false
	}
	
	override fun startExecuting() {
		flugel.aiTaskTimer = EntityFlugel.DEATHRAY_TICKS
	}
	
	override fun continueExecuting(): Boolean {
		val deathray = flugel.aiTaskTimer
		val source = flugel.source
		val range = EntityFlugel.RANGE.F
		if (ModInfo.DEV) if (!flugel.worldObj.isRemote) for (player in flugel.playersAround) ASJUtilities.chatLog("Deathray in $deathray", player)
		flugel.setPosition(source.posX + 0.5, (source.posY + 3).D, source.posZ + 0.5)
		flugel.motionX = 0.0
		flugel.motionY = 0.0
		flugel.motionZ = 0.0
		
		if (deathray > 10) flugel.spawnPatyklz(true)
		
		if (deathray == 1) {
			val rang = ceil(range.D).I
			
			for (l in 1..(if (flugel.isUltraMode) 6 else 3)) {
				for (i in 0..((if (flugel.isUltraMode) 32 else 16))) {
					val x = flugel.worldObj.rand.nextInt(rang * 2 + 1) - rang
					val z = flugel.worldObj.rand.nextInt(rang * 2 + 1) - rang
					if (Vector3.pointDistancePlane(x, z, 0, 0) <= range) {
						val posVec = Vector3(source.posX + x, source.posY + l * 20 + 10, source.posZ + z)
						val motVec = Vector3((Math.random() - 0.5) * 18, 24, (Math.random() - 0.5) * 18)
						posVec.add(motVec)
						motVec.normalize().negate().mul(1.5)
						
						val star = EntityFallingStar(flugel.worldObj, flugel)
						flugel.worldObj.spawnEntityInWorld(star)
						
						star.setPosition(posVec.x, posVec.y, posVec.z)
						star.motionX = motVec.x
						star.motionY = motVec.y
						star.motionZ = motVec.z
					}
				}
				
				val players = flugel.playersAround
				for (player in players) {
					val target = Vector3.fromEntityCenter(player)
					val motion = Vector3((Math.random() - 0.5) * 16, l * 10, (Math.random() - 0.5) * 16)
					target.add(motion)
					motion.negate().normalize()
					
					val star = EntityFallingStar(flugel.worldObj, flugel)
					
					flugel.worldObj.spawnEntityInWorld(star)
					
					star.posX = target.x
					star.posY = target.y
					star.posZ = target.z
					star.setPositionAndRotation(star.posX, star.posY, star.posZ, 0f, 0f)
					star.motionX = motion.x
					star.motionY = motion.y
					star.motionZ = motion.z
				}
			}
			
			if (flugel.worldObj.isRemote) {
				for (i in 0..359) {
					val r = 0.2f + Math.random().F * 0.3f
					val g = Math.random().F * 0.3f
					val b = 0.2f + Math.random().F * 0.3f
					Botania.proxy.wispFX(flugel.worldObj, flugel.posX, flugel.posY + 1, flugel.posZ, r, g, b, 0.5f, cos(i.D).F * 0.4f, 0f, sin(i.D).F * 0.4f)
					Botania.proxy.wispFX(flugel.worldObj, flugel.posX, flugel.posY + 1, flugel.posZ, r, g, b, 0.5f, cos(i.D).F * 0.3f, 0f, sin(i.D).F * 0.3f)
					Botania.proxy.wispFX(flugel.worldObj, flugel.posX, flugel.posY + 1, flugel.posZ, r, g, b, 0.5f, cos(i.D).F * 0.2f, 0f, sin(i.D).F * 0.2f)
					Botania.proxy.wispFX(flugel.worldObj, flugel.posX, flugel.posY + 1, flugel.posZ, r, g, b, 0.5f, cos(i.D).F * 0.1f, 0f, sin(i.D).F * 0.1f)
				}
			}
		}
		
		return canContinue()
	}
	
	override fun resetTask() {
		flugel.stage = EntityFlugel.STAGE_DEATHRAY
		flugel.aiTaskTimer = 0
		flugel.aiTask = AITask.REGEN
	}
}