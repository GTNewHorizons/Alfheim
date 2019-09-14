package alfheim.common.entity

import alfheim.common.item.*
import net.minecraft.client.renderer.RenderGlobal
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityThrowable
import net.minecraft.item.*
import net.minecraft.potion.*
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World
import vazkii.botania.common.brew.ModPotions
import kotlin.math.*

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
		}
		
		if (worldObj.isRemote) {
			for (acc in worldObj.worldAccesses) {
				if (acc !is RenderGlobal) continue
				
				val d0 = posX
				val d1 = posY
				val d2 = posZ
				val s = "iconcrack_${Item.getIdFromItem(AlfheimItems.splashPotion)}_0"
				
				for (i in 0..8) {
					worldObj.spawnParticle(s, d0, d1, d2, rand.nextGaussian() * 0.15, rand.nextDouble() * 0.2, rand.nextGaussian() * 0.15)
				}
				
				val c = color
				val f = (c shr 16 and 255).toFloat() / 255.0f
				val f1 = (c shr 8 and 255).toFloat() / 255.0f
				val f2 = (c shr 0 and 255).toFloat() / 255.0f
				val s1 = if (effects.contains(peClear)) "instantSpell" else "spell"
				
				for (l2 in 1..100) {
					val d4 = rand.nextDouble() * 4.0
					val d13 = rand.nextDouble() * Math.PI * 2.0
					val d5 = cos(d13) * d4
					val d6 = 0.01 + rand.nextDouble() * 0.5
					val d7 = sin(d13) * d4
					
					val entityfx = acc.doSpawnParticle(s1, d0 + d5 * 0.1, d1 + 0.3, d2 + d7 * 0.1, d5, d6, d7)
					
					if (entityfx != null) {
						val f4 = 0.75f + rand.nextFloat() * 0.25f
						entityfx.setRBGColorF(f * f4, f1 * f4, f2 * f4)
						entityfx.multiplyVelocity(d4.toFloat())
					}
				}
				
				worldObj.playSound(posX + 0.5, posY + 0.5, posZ + 0.5, "game.potion.smash", 1.0f, worldObj.rand.nextFloat() * 0.1f + 0.9f, false)
			}
		}
		
		setDead()
	}
	
	override fun getGravityVelocity() = dataWatcher.getWatchableObjectFloat(30)
	
	public override fun func_70183_g() = -10.0f
	
	public override fun func_70182_d() = 1.0f
}
