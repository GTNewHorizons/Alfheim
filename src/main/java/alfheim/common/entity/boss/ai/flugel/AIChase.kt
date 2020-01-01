package alfheim.common.entity.boss.ai.flugel

import alexsocol.asjlib.math.Vector3
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.*
import alfheim.common.entity.boss.EntityFlugel
import net.minecraft.entity.Entity
import net.minecraft.item.Item
import net.minecraft.util.*

class AIChase(flugel: EntityFlugel, task: AITask): AIBase(flugel, task) {
	
	var lowest = false
	
	override fun startExecuting() {
		flugel.noClip = true
		val s = flugel.stage
		val i = if (s == 1) 200 else if (s == 2) 100 else 50
		flugel.aiTaskTimer = flugel.worldObj.rand.nextInt(i) + i
		
		lowest = flugel.worldObj.rand.nextInt(10) == 0
		
		if (flugel.worldObj.rand.nextInt(4) == 0) {
			val player = flugel.worldObj.getPlayerEntityByName(flugel.playerDamage.keys.random()) ?: return
			
			for (a in 0..9)
				for (slot1 in player.inventory.mainInventory.indices) {
					val slot2 = flugel.worldObj.rand.nextInt(player.inventory.mainInventory.size)
					
					val stack1 = player.inventory.mainInventory[slot1]
					val stack2 = player.inventory.mainInventory[slot2]
					
					if ((stack1 != null && AlfheimConfigHandler.flugelSwapBL.contains(Item.getIdFromItem(stack1.item))) || (stack2 != null && AlfheimConfigHandler.flugelSwapBL.contains(Item.getIdFromItem(stack2.item)))) continue
					
					player.inventory.mainInventory[slot1] = stack2
					player.inventory.mainInventory[slot2] = stack1
				}
		}
	}
	
	override fun continueExecuting(): Boolean {
		checkCollision()
		if (flugel.aiTaskTimer % 10 == 0) {
			val name = if (lowest)
				flugel.playerDamage.minBy { it.value }?.key ?: "Notch"
			else
				flugel.playerDamage.maxBy { it.value }?.key ?: "Notch"
			
			val target = flugel.worldObj.getPlayerEntityByName(name)
			
			if (target != null) {
				val mot = Vector3(target.posX - flugel.posX, target.posY - flugel.posY, target.posZ - flugel.posZ).normalize()
				flugel.motionX = mot.x
				flugel.motionY = mot.y
				flugel.motionZ = mot.z
			
			} else {
				flugel.playerDamage.remove(name)
			}
		}
		return shouldContinue()
	}
	
	// EntityFireball code below ============================================================================
	
	fun checkCollision() {
		var vec3 = Vec3.createVectorHelper(flugel.posX, flugel.posY, flugel.posZ)
		var vec31 = Vec3.createVectorHelper(flugel.posX + flugel.motionX, flugel.posY + flugel.motionY, flugel.posZ + flugel.motionZ)
		var mop: MovingObjectPosition? = flugel.worldObj.rayTraceBlocks(vec3, vec31)
		vec3 = Vec3.createVectorHelper(flugel.posX, flugel.posY, flugel.posZ)
		vec31 = Vec3.createVectorHelper(flugel.posX + flugel.motionX, flugel.posY + flugel.motionY, flugel.posZ + flugel.motionZ)
		
		if (mop != null) {
			vec31 = Vec3.createVectorHelper(mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord)
		}
		
		var entity: Entity? = null
		val list = flugel.worldObj.getEntitiesWithinAABBExcludingEntity(flugel, flugel.boundingBox.addCoord(flugel.motionX, flugel.motionY, flugel.motionZ).expand(1.0, 1.0, 1.0))
		var d0 = 0.0
		
		for (o in list) {
			val entity1 = o as Entity
			
			if (entity1.canBeCollidedWith()) {
				val f = 0.3f
				val axisalignedbb = entity1.boundingBox.expand(f.D, f.D, f.D)
				val movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31)
				
				if (movingobjectposition1 != null) {
					val d1 = vec3.distanceTo(movingobjectposition1.hitVec)
					
					if (d1 < d0 || d0 == 0.0) {
						entity = entity1
						d0 = d1
					}
				}
			}
		}
		
		if (entity != null) {
			mop = MovingObjectPosition(entity)
		}
		
		if (mop != null) {
			onImpact(mop)
		}
	}
	
	fun onImpact(mop: MovingObjectPosition) {
		when (mop.typeOfHit) {
			MovingObjectPosition.MovingObjectType.BLOCK  -> Unit
			
			MovingObjectPosition.MovingObjectType.ENTITY -> {
				if (flugel.worldObj.rand.nextInt(5) == 0)
					mop.entityHit.attackEntityFrom(DamageSourceSpell.shadow(flugel), if (flugel.isUltraMode) 10f else if (flugel.isHardMode) 2f else 0.5f)
				else
					mop.entityHit.attackEntityFrom(DamageSource.causeMobDamage(flugel), if (flugel.isUltraMode) 20f else if (flugel.isHardMode) 15f else 10f)
			}
			
			else                                         -> Unit
		}
	}
}