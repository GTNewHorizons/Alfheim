package alfheim.common.entity.ai

import alexsocol.asjlib.playSoundAtEntity
import alfheim.api.entity.*
import alfheim.common.core.handler.*
import net.minecraft.entity.Entity
import net.minecraft.entity.ai.*
import net.minecraft.entity.monster.EntityCreeper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.pathfinding.*
import net.minecraft.util.Vec3

class EntityAICreeperAvoidPooka(val creeper: EntityCreeper): EntityAIBase() {
	
	private var closestLivingEntity: Entity? = null
	
	/**
	 * The PathEntity of our entity
	 */
	private var entityPathEntity: PathEntity? = null
	
	/**
	 * The PathNavigate of our entity
	 */
	private val entityPathNavigate: PathNavigate = creeper.navigator
	
	init {
		mutexBits = 1
	}
	
	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	override fun shouldExecute(): Boolean {
		if (!AlfheimConfigHandler.enableElvenStory) return false
		
		closestLivingEntity = creeper.worldObj.getClosestPlayerToEntity(creeper, 6.0) ?: return false
		
		val player = closestLivingEntity as EntityPlayer
		
		if (player.race !== EnumRace.POOKA) return false
		
		if (ESMHandler.isAbilityDisabled(player)) return false
		
		val vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(creeper, 16, 7, Vec3.createVectorHelper(player.posX, player.posY, player.posZ))
		
		return when {
			vec3 == null                                                                                        -> false
			player.getDistanceSq(vec3.xCoord, vec3.yCoord, vec3.zCoord) < player.getDistanceSqToEntity(creeper) -> false
			
			else                                                                                                -> {
				entityPathEntity = entityPathNavigate.getPathToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord) ?: return false
				
				return entityPathEntity!!.isDestinationSame(vec3).also { if (it) player.playSoundAtEntity("mob.cat.meow", 1f, 1f) }
			}
		}
	}
	
	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	override fun continueExecuting(): Boolean {
		return !entityPathNavigate.noPath()
	}
	
	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	override fun startExecuting() {
		entityPathNavigate.setPath(entityPathEntity, 1.0)
	}
	
	/**
	 * Resets the task
	 */
	override fun resetTask() {
		closestLivingEntity = null
	}
	
	/**
	 * Updates the task
	 */
	override fun updateTask() {
		if (closestLivingEntity != null && creeper.getDistanceSqToEntity(closestLivingEntity) < 49.0) {
			creeper.navigator.setSpeed(1.2)
		} else {
			creeper.navigator.setSpeed(1.0)
		}
	}
}