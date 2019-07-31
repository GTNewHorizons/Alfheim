package alfheim.common.entity

import alfheim.common.item.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityThrowable
import net.minecraft.item.ItemStack
import net.minecraft.potion.*
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World
import kotlin.math.*

class EntityThrownPotion: EntityThrowable {
	
	constructor(world: World) : super(world) {
		stack = ItemStack(ShadowFoxItems.splashPotion)
		effects = emptyList()
		
		color = 0xFFFFFF
	}
	
	constructor(player: EntityPlayer, st: ItemStack) : super(player.worldObj, player) {
		stack = st
		val brew = stack.item as ItemSplashPotion
		effects = brew.getBrew(stack).getPotionEffects(stack)
		color = brew.getColor(stack)
	}
	
	val effects: List<PotionEffect>
	val stack: ItemStack
	
	var color
		get() = dataWatcher.getWatchableObjectInt(31)
		set(value) {
			dataWatcher.updateObject(31, value)
		}
	
	override fun entityInit() {
		super.entityInit()
		dataWatcher.addObject(30, 0.3f)
		dataWatcher.setObjectWatched(30)
		
		dataWatcher.addObject(31, 0)
		dataWatcher.setObjectWatched(31)
	}
	
	override fun onImpact(movingObject: MovingObjectPosition?) {
		if (!worldObj.isRemote && movingObject != null && effects.isNotEmpty()) {
			val axisalignedbb = boundingBox.expand(5.0, 2.5, 5.0)
			val list1 = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, axisalignedbb)
			
			if (list1 != null && list1.isNotEmpty()) {
				val iterator = list1.iterator()
				
				while (iterator.hasNext()) {
					val entitylivingbase = iterator.next() as EntityLivingBase
					val d0 = getDistanceSqToEntity(entitylivingbase)
					
					if (d0 < 16.0) {
						var d1 = 1.0 - sqrt(d0) / 4.0
						
						if (entitylivingbase === movingObject.entityHit) {
							d1 = 1.0
						}
						
						for (e: PotionEffect in effects) {
							
							if (Potion.potionTypes[e.potionID].isInstant) {
								Potion.potionTypes[e.potionID].affectEntity(thrower, entitylivingbase, e.amplifier, d1)
							} else {
								val j = (d1 * e.duration.toDouble() + 0.5).toInt()
								
								if (j > 20) {
									entitylivingbase.addPotionEffect(PotionEffect(e.potionID, j, e.amplifier))
								}
							}
						}
					}
				}
			}
		}
		
		worldObj.playAuxSFX(2002, posX.roundToLong().toInt(), posY.roundToLong().toInt(), posZ.roundToLong().toInt(), 0)
		setDead()
	}
	
	override fun getGravityVelocity() = dataWatcher.getWatchableObjectFloat(30)
}
