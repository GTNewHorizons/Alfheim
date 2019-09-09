package alfheim.common.core.handler

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.entity.*
import alfheim.api.event.*
import alfheim.client.render.world.SpellEffectHandlerClient
import alfheim.client.render.world.SpellEffectHandlerClient.Spells
import alfheim.common.achievement.AlfheimAchievements
import alfheim.common.core.handler.CardinalSystem.playerSegments
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.core.util.*
import alfheim.common.entity.*
import alfheim.common.entity.boss.EntityFlugel
import alfheim.common.item.AlfheimItems
import alfheim.common.item.relic.ItemTankMask
import alfheim.common.network.*
import alfheim.common.network.Message2d.m2d
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent
import cpw.mods.fml.common.gameevent.TickEvent.*
import net.minecraft.enchantment.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.boss.IBossDisplayData
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.*
import net.minecraft.item.ItemStack
import net.minecraft.potion.*
import net.minecraft.server.MinecraftServer
import net.minecraft.util.*
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.*
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent
import net.minecraftforge.event.world.BlockEvent
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.api.recipe.ElvenPortalUpdateEvent
import vazkii.botania.common.block.tile.TileAlfPortal
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.equipment.tool.elementium.ItemElementiumAxe
import kotlin.math.max

@Suppress("unused")
object EventHandler {
	
	init {
		MinecraftForge.EVENT_BUS.register(this)
		FMLCommonHandler.instance().bus().register(this)
	}
	
	@SubscribeEvent
	fun onPlayerLoggedIn(e: PlayerLoggedInEvent) {
		if (InfoLoader.doneChecking && !InfoLoader.triedToWarnPlayer) {
			InfoLoader.triedToWarnPlayer = true
			for (s in InfoLoader.info) {
				if (s.startsWith("\$json"))
					e.player.addChatComponentMessage(IChatComponent.Serializer.func_150699_a(s.replace("\$json", "")))
				else
					e.player.addChatMessage(ChatComponentText(s))
			}
		}
		
		if (e.player is EntityPlayerMP) {
			AlfheimCore.network.sendTo(Message2d(m2d.MODES, (if (AlfheimCore.enableElvenStory) 1 else 0).toDouble(), (if (AlfheimCore.enableMMO) 1 else 0).toDouble()), e.player as EntityPlayerMP)
			CardinalSystem.transfer(e.player as EntityPlayerMP)
			if (AlfheimCore.enableElvenStory) {
				AlfheimCore.network.sendTo(Message1d(Message1d.m1d.CL_SLOWDOWN, if (AlfheimConfigHandler.slowDownClients) 1.0 else 0.0), e.player as EntityPlayerMP)
				AlfheimCore.network.sendTo(Message1d(Message1d.m1d.ELVEN_FLIGHT_MAX, AlfheimConfigHandler.flightTime.toDouble()), e.player as EntityPlayerMP)
				AlfheimCore.network.sendTo(Message1d(Message1d.m1d.DEATH_TIMER, AlfheimConfigHandler.deathScreenAddTime.toDouble()), e.player as EntityPlayerMP)
				if (!(e.player as EntityPlayerMP).func_147099_x().hasAchievementUnlocked(AlfheimAchievements.alfheim) && e.player.dimension != AlfheimConfigHandler.dimensionIDAlfheim) {
					ASJUtilities.sendToDimensionWithoutPortal(e.player, AlfheimConfigHandler.dimensionIDAlfheim, 0.5, 250.0, 0.5)
					e.player.rotationYaw = 180f
					e.player.rotationPitch = 0f
					e.player.triggerAchievement(AlfheimAchievements.alfheim)
					e.player.addChatComponentMessage(ChatComponentTranslation("elvenstory.welcome0"))
					e.player.addChatComponentMessage(ChatComponentTranslation("elvenstory.welcome1"))
					e.player.inventory.addItemStackToInventory(ItemStack(ModItems.lexicon))
					e.player.setSpawnChunk(ChunkCoordinates(0, 250, 0), true, AlfheimConfigHandler.dimensionIDAlfheim)
				}
			}
		}
	}
	
	@SubscribeEvent
	fun onNetherPortalActivation(e: NetherPortalActivationEvent) {
		if (e.worldObj.provider.dimensionId == AlfheimConfigHandler.dimensionIDAlfheim) e.isCanceled = true
	}
	
	@SubscribeEvent
	fun onAlfPortalUpdate(e: ElvenPortalUpdateEvent) {
		if (e.portalTile.worldObj.provider.dimensionId == AlfheimConfigHandler.dimensionIDAlfheim && (e.portalTile as TileAlfPortal).ticksOpen >= 0) (e.portalTile as TileAlfPortal).ticksOpen = 0
	}
	
	@SubscribeEvent
	fun onEntityDrops(event: LivingDropsEvent) {
		if (event.recentlyHit && event.source.entity != null && event.source.entity is EntityPlayer) {
			val weapon = (event.source.entity as EntityPlayer).currentEquippedItem
			if (weapon != null && weapon.item is ItemElementiumAxe && event.entityLiving is EntityFlugel && event.entity.worldObj.rand.nextInt(13) < 1 + EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, weapon)) {
				val entityitem = EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, ItemStack(if ((event.entityLiving as EntityFlugel).customNameTag == "Hatsune Miku") AlfheimItems.flugelHead2 else AlfheimItems.flugelHead))
				entityitem.delayBeforeCanPickup = 10
				event.drops.add(entityitem)
			}
		}
	}
	
	// ################################### POTIONS & STUFF ####################################
	// not decentralized because of importance of the order
	
	@SubscribeEvent
	fun onEntityAttacked(e: LivingAttackEvent) {
		var ammount = e.ammount
		
		if (e.source.entity != null && e.source.entity is EntityLivingBase)
			if ((e.source.entity as EntityLivingBase).isPotionActive(AlfheimRegistry.berserk))
				ammount *= 1.2f
			else if ((e.source.entity as EntityLivingBase).isPotionActive(AlfheimRegistry.ninja))
				ammount *= 0.8f
		
		if (AlfheimCore.enableMMO) {
			if (e.source.entity != null && e.source.entity is EntityLivingBase && (e.source.entity as EntityLivingBase).isPotionActive(AlfheimRegistry.quadDamage))
				ammount *= 4.0f
			
			if (e.source.entity != null && e.source.entity is EntityLivingBase && (e.source.entity as EntityLivingBase).isPotionActive(AlfheimRegistry.leftFlame) || e.entityLiving.isPotionActive(AlfheimRegistry.leftFlame)) {
				e.isCanceled = true
				return
			}
			if ((e.source.damageType.equals(DamageSource.inWall.damageType, ignoreCase = true) || e.source.damageType.equals(DamageSource.drown.damageType, ignoreCase = true)) && e.entityLiving.isPotionActive(AlfheimRegistry.noclip)) {
				e.isCanceled = true
				return
			}
		}
		if (e.entityLiving is EntityAlfheimPixie && e.source.getDamageType() == DamageSource.inWall.getDamageType()) {
			e.isCanceled = true
			return
		}
		if (AlfheimCore.enableElvenStory && e.entityLiving is EntityPlayer && e.source.getDamageType() == DamageSource.fall.getDamageType() && (e.entityLiving as EntityPlayer).race != EnumRace.HUMAN) {
			e.isCanceled = true
			return
		}
		if (CardinalSystem.PartySystem.friendlyFire(e.entityLiving, e.source)) {
			e.isCanceled = true
			return
		}
		if (e.source.isFireDamage && !e.source.isUnblockable && e.entityLiving is EntityPlayer && (e.entityLiving as EntityPlayer).getCurrentArmor(1)?.item === AlfheimItems.elementalLeggings && ManaItemHandler.requestManaExact((e.entityLiving as EntityPlayer).getCurrentArmor(1), e.entityLiving as EntityPlayer, MathHelper.ceiling_float_int(10 * ammount), !e.entityLiving.worldObj.isRemote)) {
			e.isCanceled = true
			return
		}
		
		// ################################################################ NOT CANCELING ################################################################
		
		if (AlfheimCore.enableMMO && e.entityLiving.isPotionActive(AlfheimRegistry.decay) && !e.source.isFireDamage && !e.source.isMagicDamage && e.source !is DamageSourceSpell && e.source.damageType != DamageSourceSpell.bleeding.damageType)
			e.entityLiving.addPotionEffect(PotionEffect(AlfheimRegistry.bleeding.id, 120, 0, true))
	}
	
	@SubscribeEvent
	fun onEntityHurt(e: LivingHurtEvent) {
		if (CardinalSystem.PartySystem.friendlyFire(e.entityLiving, e.source)) {
			e.isCanceled = true
			return
		}
		
		if (e.source.entity != null && e.source.entity is EntityLivingBase)
			if ((e.source.entity as EntityLivingBase).isPotionActive(AlfheimRegistry.berserk))
				e.ammount *= 1.2f
			else if ((e.source.entity as EntityLivingBase).isPotionActive(AlfheimRegistry.ninja))
				e.ammount *= 0.8f
		
		if (AlfheimCore.enableMMO) {
			if (e.source.entity != null && e.source.entity is EntityLivingBase && (e.source.entity as EntityLivingBase).isPotionActive(AlfheimRegistry.quadDamage)) {
				e.ammount *= 4.0f
				SpellEffectHandler.sendPacket(Spells.QUADH, e.source.entity)
			}
			
			var pe: PotionEffect? = e.entityLiving.getActivePotionEffect(AlfheimRegistry.nineLifes)
			run nl@{
				if (pe != null) {
					val blockable = e.source.damageType == DamageSource.fall.damageType ||
									e.source.damageType == DamageSource.drown.damageType ||
									e.source.damageType == DamageSource.inFire.damageType ||
									e.source.damageType == DamageSource.onFire.damageType ||
									e.source.damageType == DamageSourceSpell.poison.damageType ||
									e.source.damageType == DamageSource.wither.damageType
					
					if (blockable) {
						if (pe!!.amplifier == 4) {
							if (ASJUtilities.willEntityDie(e)) {
								if (e.source.damageType == DamageSource.wither.damageType && e.entityLiving.worldObj.rand.nextBoolean()) return@nl
								pe!!.amplifier = 0
								pe!!.duration = 100
								if (ASJUtilities.isServer) AlfheimCore.network.sendToAll(MessageEffect(e.entityLiving.entityId, pe!!.potionID, pe!!.duration, pe!!.amplifier))
								e.isCanceled = true
								return
							}
						} else if (pe!!.amplifier == 0) {
							e.isCanceled = true
							return
						}
					} else if (e.source.entity != null && e.source.entity is EntityLivingBase && e.source.entity.isEntityAlive && e.entityLiving.worldObj.rand.nextInt(3) == 0) {
						e.source.entity.attackEntityFrom(e.source, e.ammount / 2)
					}
				}
			}
			
			pe = e.entityLiving.getActivePotionEffect(AlfheimRegistry.stoneSkin)
			if (pe != null && !e.source.isMagicDamage && !e.source.isDamageAbsolute) {
				e.isCanceled = true
				e.entityLiving.removePotionEffect(AlfheimRegistry.stoneSkin.id)
				e.entityLiving.addPotionEffect(PotionEffect(Potion.field_76444_x.id, pe.duration, 3, true))
				return
			}
			
			pe = e.entityLiving.getActivePotionEffect(AlfheimRegistry.butterShield)
			if (pe != null && pe.duration > 0 && e.source.isMagicDamage && !e.source.isDamageAbsolute) {
				e.isCanceled = true
				if (--pe.amplifier <= 0) pe.duration = 0 // e.entityLiving.removePotionEffect(AlfheimRegistry.butterShield.id); <- ConcurrentModificationException :(
				if (ASJUtilities.isServer) AlfheimCore.network.sendToAll(MessageEffect(e.entityLiving.entityId, pe.potionID, pe.duration, pe.amplifier))
				return
			}
			
			// ################################################################ NOT CANCELING ################################################################
			
			pe = e.entityLiving.getActivePotionEffect(AlfheimRegistry.butterShield)
			if (!e.source.isMagicDamage && !e.source.isDamageAbsolute && pe != null && pe.duration > 0) {
				e.ammount /= 2f
				pe.duration -= (e.ammount * 20).toInt()
				val dur = max(pe.duration, 0)
				if (ASJUtilities.isServer) AlfheimCore.network.sendToAll(MessageEffect(e.entityLiving.entityId, pe.potionID, dur, pe.amplifier))
			}
		}
	}
	
	@SubscribeEvent
	fun onEntityDeath(e: LivingDeathEvent) {
		if (AlfheimCore.enableMMO) {
			if (e.entityLiving is EntityPlayer && !MinecraftServer.getServer().isSinglePlayer && !ItemTankMask.canBeSaved(e.entityLiving as EntityPlayer)) {
				e.entityLiving.clearActivePotions()
				e.entityLiving.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDLeftFlame, AlfheimConfigHandler.deathScreenAddTime, 0, true))
				e.entityLiving.dataWatcher.updateObject(6, 1f)
			}
			
			CardinalSystem.PartySystem.getMobParty(e.entityLiving)?.setDead(e.entityLiving, true)
		}
	}
	
	@SubscribeEvent
	fun onServerTick(e: ServerTickEvent) {
		EntityLolicorn.tick()
		
		if (AlfheimCore.enableMMO) {
			if (e.phase == Phase.START) {
				CardinalSystem.SpellCastingSystem.tick()
				CardinalSystem.TimeStopSystem.tick()
			}
			for (name in playerSegments.keys) {
				val player = MinecraftServer.getServer().configurationManager.func_152612_a(name)
				if (player == null) {
					val s = playerSegments[name]
					s?.target = null
					s?.isParty = false
				} else {
					val tg = CardinalSystem.TargetingSystem.getTarget(player)
					
					if (tg.target?.isEntityAlive == false || tg.target?.let { Vector3.entityDistance(player, it) } ?: 0.0 > if (tg.target?.let { it is IBossDisplayData } == true) 128.0 else 32.0)
						CardinalSystem.TargetingSystem.setTarget(player, null, false)
				}
			}
		}
	}
	
	@SubscribeEvent
	fun onBlockBreak(e: BlockEvent.BreakEvent) {
		val item = e.player.currentEquippedItem?.item ?: return
		if (item === AlfheimItems.flugelSoul) e.isCanceled = true
	}
	
	@SubscribeEvent
	fun onLivingUpdate(e: LivingUpdateEvent) {
		if (AlfheimCore.enableMMO) {
			if (e.entityLiving.isPotionActive(AlfheimRegistry.leftFlame)) {
				val pe = e.entityLiving.getActivePotionEffect(AlfheimRegistry.leftFlame)
				pe.duration--
				if (!ASJUtilities.isServer) SpellEffectHandlerClient.onDeathTick(e.entityLiving)
				if (pe.duration <= 0)
					e.entityLiving.removePotionEffect(pe.potionID)
				else
					e.isCanceled = true
			}
			
			if (e.entityLiving.isDead) {
				val pt = CardinalSystem.PartySystem.getMobParty(e.entityLiving)
				pt?.setDead(e.entityLiving, true)
			}
		}
	}
	
	@SubscribeEvent
	fun onPlayerUpdate(e: PlayerTickEvent) {
		if (e.phase == Phase.START) return
//		val player = e.player

//		player.rotationYaw = 0f
//		player.rotationPitch = 0f
	}
	
	@SubscribeEvent
	fun onNewPotionEffect(e: LivingPotionEvent.Add.Post) {
		if (ASJUtilities.isServer) AlfheimCore.network.sendToAll(MessageEffect(e.entityLiving.entityId, e.effect.potionID, e.effect.duration, e.effect.amplifier, false, 1))
	}
	
	@SubscribeEvent
	fun onChangedPotionEffect(e: LivingPotionEvent.Change.Post) {
		if (ASJUtilities.isServer) AlfheimCore.network.sendToAll(MessageEffect(e.entityLiving.entityId, e.effect.potionID, e.effect.duration, e.effect.amplifier, e.update, 0))
	}
	
	@SubscribeEvent
	fun onFinishedPotionEffect(e: LivingPotionEvent.Remove.Post) {
		if (ASJUtilities.isServer) AlfheimCore.network.sendToAll(MessageEffect(e.entityLiving.entityId, e.effect.potionID, e.effect.duration, e.effect.amplifier, false, -1))
	}
	
	@SubscribeEvent
	fun onEntityUpdate(e: EntityUpdateEvent) {
		// slowdown anomaly
		if (!e.entity.isEntityAlive) return
		if ((ASJUtilities.isServer || AlfheimConfigHandler.slowDownClients) && !e.entity.canEntityUpdate) {
			e.isCanceled = true
			e.entity.canEntityUpdate = true
		}
	}
}