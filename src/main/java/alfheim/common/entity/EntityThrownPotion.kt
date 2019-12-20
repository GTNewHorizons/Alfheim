package alfheim.common.entity

import alfheim.client.render.world.VisualEffectHandlerClient
import alfheim.common.core.handler.VisualEffectHandler
import alfheim.common.item.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityThrowable
import net.minecraft.item.ItemStack
import net.minecraft.potion.*
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World
import vazkii.botania.common.brew.ModPotions
import kotlin.math.sqrt

class EntityThrownPotion: EntityThrowable {
	
	constructor(world: World) : super(world) {
		stack = ItemStack(AlfheimItems.splashPotion)
		effects = emptyList()
		
		color = 0xFFFFFF
	}
	
	constructor(world: World, st: ItemStack) : super(world) {
		stack = st
		val brew = stack.item as ItemSplashPotion
		effects = brew.getBrew(stack).getPotionEffects(stack)
		color = brew.getColor(stack)
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
		dataWatcher.addObject(30, 0.1f)
		dataWatcher.setObjectWatched(30)
		
		dataWatcher.addObject(31, 0)
		dataWatcher.setObjectWatched(31)
	}
	
	val peClear = PotionEffect(ModPotions.clear.id, 0, 0)
	
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
			
			VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.POTION, dimension, posX, posY, posZ, color.toDouble(), if (effects.contains(peClear)) 1.0 else 0.0, 0.0)
		}
		
		setDead()
	}
	
	override fun getGravityVelocity() = dataWatcher.getWatchableObjectFloat(30)
	
	public override fun func_70183_g() = -10.0f
	
	public override fun func_70182_d() = 1.0f
}
