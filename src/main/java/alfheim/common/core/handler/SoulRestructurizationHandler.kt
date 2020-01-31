package alfheim.common.core.handler

import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.block.tile.TileManaInfuser
import alfheim.common.item.AlfheimItems
import alfheim.common.item.relic.ItemFlugelSoul
import cpw.mods.fml.common.eventhandler.*
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.entity.living.*
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent
import vazkii.botania.common.block.tile.TileBrewery
import vazkii.botania.common.entity.EntityDoppleganger

object SoulRestructurizationHandler {
	
	init {
		MinecraftForge.EVENT_BUS.register(this)
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	fun onGaiaSummoned(e: EntityJoinWorldEvent) {
		val gaia = e.entity as? EntityDoppleganger ?: return
		val world = gaia.worldObj
		val infuser = world.getTileEntity(gaia.source.posX, gaia.source.posY + 2, gaia.source.posZ) as? TileManaInfuser ?: return
		
		infuser.deGaiaingTime = 20
		world.setBlockMetadataWithNotify(gaia.source.posX, gaia.source.posY + 2, gaia.source.posZ, 2, 3)
	}
	
	@SubscribeEvent
	fun onGaiaUpdate(e: LivingUpdateEvent) {
		val gaia = e.entity as? EntityDoppleganger ?: return
		val world = gaia.worldObj
		val infuser = world.getTileEntity(gaia.source.posX, gaia.source.posY + 2, gaia.source.posZ) as? TileManaInfuser ?: return
		
		infuser.deGaiaingTime = 20
		if (!gaia.isAggored && gaia.invulTime > 0 && gaia.mobSpawnTicks == EntityDoppleganger.MOB_SPAWN_TICKS) infuser.prepareParticles().also { return }
		
		// TODO make Gaia tougher
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	fun onGaiaHurt(e: LivingHurtEvent) {
		val gaia = e.entity as? EntityDoppleganger ?: return
		
		VisualEffectHandler.sendPacket(VisualEffects.GAIA_SOUL, gaia.dimension, gaia.source.posX.toDouble(), gaia.source.posY + 2.0, gaia.source.posZ.toDouble())
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	fun onGaiaDied(e: LivingDeathEvent) {
		val gaia = e.entity as? EntityDoppleganger ?: return
		val world = gaia.worldObj
		val (x, y, z) = arrayOf(gaia.source.posX, gaia.source.posY, gaia.source.posZ)
		val infuser = world.getTileEntity(x, y + 2, z) as? TileManaInfuser ?: return
		val brewer = world.getTileEntity(x, y + 5, z) as? TileBrewery ?: return
		
		run exp@{
			if (!world.isRemote) {
				if (brewer.getStackInSlot(0)?.item === AlfheimItems.flugelSoul) {
					if (ItemFlugelSoul.getBlocked(brewer.getStackInSlot(0)) > 0) {
						if (gaia.isHardMode || Math.random() > 0.5) {
							ItemFlugelSoul.setDisabled(brewer.getStackInSlot(0), ItemFlugelSoul.getBlocked(brewer.getStackInSlot(0)), false)
							return@exp
						}
					}
				}
				
				world.newExplosion(null, x.toDouble(), y + 2.0, z.toDouble(), 10f, true, false)
				world.setBlockToAir(x, y + 2, z)
			}
		}
		
		infuser.doneParticles()
		
		e.isCanceled = true
		
		run { // from onDeath
			val entity = e.source.entity
			val entitylivingbase = gaia.func_94060_bK()
			if (gaia.scoreValue >= 0 && entitylivingbase != null) {
				entitylivingbase.addToPlayerScore(gaia, gaia.scoreValue)
			}
			entity?.onKillEntity(gaia)
			gaia.dead = true
			gaia.func_110142_aN().func_94549_h()
			gaia.worldObj.setEntityState(gaia, 3.toByte())
		}
	}
}