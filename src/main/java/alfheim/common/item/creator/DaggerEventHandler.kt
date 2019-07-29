package alfheim.common.item.creator

import alfheim.api.ModInfo
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.ReflectionHelper
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.potion.*
import net.minecraft.util.*
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingAttackEvent
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.Vector3
import vazkii.botania.common.lib.LibObfuscation

/**
 * @author WireSegal
 * Created at 10:53 AM on 2/8/16.
 */
class DaggerEventHandler {
	
	companion object {
		class DamageSourceOculus(entity: EntityLivingBase): EntityDamageSource("${ModInfo.MODID}.oculus", entity) {
			init {
				setDamageBypassesArmor()
				setMagicDamage()
			}
		}
		
		val instance = DaggerEventHandler()
		
		fun register() {
			MinecraftForge.EVENT_BUS.register(instance)
		}
	}
	
	fun getHeadOrientation(entity: EntityLivingBase): Vector3 {
		val f1 = MathHelper.cos(-entity.rotationYaw * 0.017453292F - Math.PI.toFloat())
		val f2 = MathHelper.sin(-entity.rotationYaw * 0.017453292F - Math.PI.toFloat())
		val f3 = -MathHelper.cos(-(entity.rotationPitch - 90) * 0.017453292F)
		val f4 = MathHelper.sin(-(entity.rotationPitch - 90) * 0.017453292F)
		return Vector3((f2 * f3).toDouble(), f4.toDouble(), (f1 * f3).toDouble())
	}
	
	@SubscribeEvent
	fun onLivingAttacked(e: LivingAttackEvent) {
		val player = e.entityLiving
		val damage = e.source
		if (player is EntityPlayer && damage is EntityDamageSource && player.isUsingItem) {
			val enemyEntity = damage.entity
			if (enemyEntity is EntityLivingBase) {
				// Dammit cpw // FIXME transformer!!!
				val itemInUse = ReflectionHelper.getPrivateValue<ItemStack, EntityPlayer>(EntityPlayer::class.java, player, *LibObfuscation.ITEM_IN_USE)
				val itemInUseCount = itemInUse.maxItemUseDuration - ReflectionHelper.getPrivateValue<Int, EntityPlayer>(EntityPlayer::class.java, player, *LibObfuscation.ITEM_IN_USE_COUNT)
				if (itemInUse.item is ItemTrisDagger && itemInUseCount <= ItemTrisDagger.maxBlockLength && itemInUseCount >= ItemTrisDagger.minBlockLength) {
					// Perfect timing indeed.
					
					val lookVec = Vector3(player.lookVec)
					val targetVec = Vector3.fromEntityCenter(enemyEntity).sub(Vector3.fromEntityCenter(player))
					val epsilon = lookVec.dotProduct(targetVec) / (lookVec.mag() * targetVec.mag())
					if (epsilon > 0.75) {
						e.isCanceled = true
						player.stopUsingItem()
						if (!player.worldObj.isRemote) {
							if (damage !is EntityDamageSourceIndirect) {
								enemyEntity.attackEntityFrom(DamageSourceOculus(player), e.ammount * 2f) // dammit cpw, you misspelled amount
								val xDif = enemyEntity.posX - player.posX
								val zDif = enemyEntity.posZ - player.posZ
								player.worldObj.playSoundAtEntity(enemyEntity, "random.anvil_land", 1f, 0.9f + 0.1f * Math.random().toFloat())
								if (enemyEntity is EntityPlayer && enemyEntity.currentEquippedItem != null)
									enemyEntity.currentEquippedItem.damageItem(30, enemyEntity)
								enemyEntity.knockBack(player, 1f, -xDif, -zDif)
								enemyEntity.addPotionEffect(PotionEffect(Potion.weakness.id, 60, 1, true))
								enemyEntity.addPotionEffect(PotionEffect(Potion.moveSlowdown.id, 60, 2, true))
							}
						} else {
							val mainVec = Vector3.fromEntityCenter(player).add(lookVec)
							val baseVec = getHeadOrientation(player).crossProduct(lookVec).normalize()
							for (i in 0..360 step 15) {
								val rotVec = baseVec.copy().rotate(i * 180 / Math.PI, lookVec)
								val endVec = mainVec.copy().add(rotVec)
								Botania.proxy.lightningFX(player.worldObj, mainVec, endVec, 3f, 0xFF94A1, 0xFBAAB5)
							}
						}
					}
				}
			}
			
		}
	}
}
