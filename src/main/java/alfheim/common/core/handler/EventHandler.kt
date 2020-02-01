package alfheim.common.core.handler

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.entity.*
import alfheim.api.event.*
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.VisualEffectHandlerClient
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.achievement.AlfheimAchievements
import alfheim.common.core.handler.CardinalSystem.playerSegments
import alfheim.common.core.helper.ElvenFlightHelper
import alfheim.common.core.util.*
import alfheim.common.entity.EntityLolicorn
import alfheim.common.entity.boss.EntityFlugel
import alfheim.common.item.AlfheimItems
import alfheim.common.item.relic.ItemTankMask
import alfheim.common.network.*
import alfheim.common.network.Message2d.m2d
import alfheim.common.spell.darkness.SpellDecay
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.PlayerEvent
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
import net.minecraftforge.event.entity.EntityJoinWorldEvent
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
			AlfheimCore.network.sendTo(Message2d(m2d.MODES, (if (AlfheimCore.enableElvenStory) 1 else 0).D, (if (AlfheimCore.enableMMO) 1 else 0).D), e.player as EntityPlayerMP)
			CardinalSystem.transfer(e.player as EntityPlayerMP)
			if (AlfheimCore.enableElvenStory) {
				AlfheimCore.network.sendTo(Message1d(Message1d.m1d.DEATH_TIMER, AlfheimConfigHandler.deathScreenAddTime.D), e.player as EntityPlayerMP)
				AlfheimCore.network.sendTo(Message1d(Message1d.m1d.ELVEN_FLIGHT_MAX, ElvenFlightHelper.max), e.player as EntityPlayerMP)
				AlfheimCore.network.sendTo(MessageNI(MessageNI.mni.WINGS_BL, AlfheimConfigHandler.wingsBlackList), e.player as EntityPlayerMP)
				if (!(e.player as EntityPlayerMP).hasAchievement(AlfheimAchievements.alfheim) && e.player.dimension != AlfheimConfigHandler.dimensionIDAlfheim) {
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
	fun onEntityJoinWorld(e: EntityJoinWorldEvent) {
		val player = e.entity as? EntityPlayerMP ?: return
		val seed = player.worldObj.seed
		AlfheimCore.network.sendTo(Message1l(Message1l.m1l.SEED, seed), player)
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
		if (event.recentlyHit && event.source.entity is EntityPlayer) {
			val weapon = (event.source.entity as EntityPlayer).currentEquippedItem
			val target = event.entityLiving
			if (weapon != null && weapon.item is ItemElementiumAxe && target is EntityFlugel && event.entity.worldObj.rand.nextInt(13) < 1 + EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, weapon)) {
				val entityitem = EntityItem(target.worldObj, target.posX, target.posY, target.posZ, ItemStack(if (target.customNameTag == "Hatsune Miku") AlfheimItems.flugelHead2 else AlfheimItems.flugelHead))
				entityitem.delayBeforeCanPickup = 10
				event.drops.add(entityitem)
			}
		}
	}
	
	// ################################### POTIONS & STUFF ####################################
	// not decentralized because of importance of the order
	
	val nineLifesBlockable = arrayOf(DamageSource.fall.damageType, DamageSource.drown.damageType, DamageSource.inFire.damageType, DamageSource.onFire.damageType, DamageSourceSpell.poison.damageType, DamageSourceSpell.poisonMagic.damageType, DamageSource.wither.damageType)
	
	val DamageSource.isMagical: Boolean
		get() = isMagicDamage || this is DamageSourceSpell
	
	@SubscribeEvent
	fun onEntityAttacked(e: LivingAttackEvent) {
		var amount = e.ammount // oh srsly 'mm' ?
		val target = e.entityLiving
		
		if ((e.source.entity as? EntityLivingBase)?.isPotionActive(AlfheimConfigHandler.potionIDBerserk) == true)
			amount *= 1.2f
		if ((e.source.entity as? EntityLivingBase)?.isPotionActive(AlfheimConfigHandler.potionIDOvermage) == true && (e.source is DamageSourceSpell || (e.source.isMagicDamage && (e.source.entity as? EntityPlayer)?.let { SpellBase.consumeMana(it, (amount * 100).I, true) } == true)))
			amount *= 1.2f
		if ((e.source.entity as? EntityLivingBase)?.isPotionActive(AlfheimConfigHandler.potionIDNinja) == true)
			amount *= 0.8f
		
		if (AlfheimCore.enableMMO) {
			if ((e.source.entity as? EntityLivingBase)?.isPotionActive(AlfheimConfigHandler.potionIDQuadDamage) == true)
				amount *= 4f
			
			if ((e.source.entity as? EntityLivingBase)?.isPotionActive(AlfheimConfigHandler.potionIDLeftFlame) == true || target.isPotionActive(AlfheimConfigHandler.potionIDLeftFlame)) {
				e.isCanceled = true
				return
			}
			if ((e.source.damageType.equals(DamageSource.inWall.damageType, ignoreCase = true) || e.source.damageType.equals(DamageSource.drown.damageType, ignoreCase = true)) && target.isPotionActive(AlfheimConfigHandler.potionIDNoclip)) {
				e.isCanceled = true
				return
			}
		}
		
		if (AlfheimCore.enableElvenStory && e.source.damageType == DamageSource.fall.damageType && target is EntityPlayer && target.race != EnumRace.HUMAN) {
			e.isCanceled = true
			return
		}
		if (CardinalSystem.PartySystem.friendlyFire(target, e.source)) {
			e.isCanceled = true
			return
		}
		if (e.source.isFireDamage && !e.source.isUnblockable && (target as? EntityPlayer)?.getCurrentArmor(1)?.item === AlfheimItems.elementalLeggings && ManaItemHandler.requestManaExact(target.getCurrentArmor(1), target, MathHelper.ceiling_float_int(10 * amount), !target.worldObj.isRemote)) {
			e.isCanceled = true
			return
		}
		
		// ################################################################ NOT CANCELING ################################################################
		
		if (AlfheimCore.enableMMO && target.isPotionActive(AlfheimConfigHandler.potionIDDecay) && !e.source.isFireDamage && !e.source.isMagical && e.source.damageType != DamageSourceSpell.bleeding.damageType)
			target.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDBleeding, SpellDecay.duration / 5, SpellDecay.efficiency.I, true))
	}
	
	@SubscribeEvent
	fun onEntityHurt(e: LivingHurtEvent) {
		val target = e.entityLiving
		
		if (CardinalSystem.PartySystem.friendlyFire(target, e.source)) {
			e.isCanceled = true
			return
		}
		
		if ((e.source.entity as? EntityLivingBase)?.isPotionActive(AlfheimConfigHandler.potionIDBerserk) == true)
			e.ammount *= 1.2f
		if ((e.source.entity as? EntityLivingBase)?.isPotionActive(AlfheimConfigHandler.potionIDOvermage) == true && e.source.isMagical)
			e.ammount *= 1.2f
		if ((e.source.entity as? EntityLivingBase)?.isPotionActive(AlfheimConfigHandler.potionIDNinja) == true)
			e.ammount *= 0.8f
		
		if (AlfheimCore.enableMMO) {
			if ((e.source.entity as? EntityLivingBase)?.isPotionActive(AlfheimConfigHandler.potionIDQuadDamage) == true) {
				e.ammount *= 4f
				VisualEffectHandler.sendPacket(VisualEffects.QUADH, e.source.entity)
			}
			
			var pe: PotionEffect? = target.getActivePotionEffect(AlfheimConfigHandler.potionIDNineLifes)
			run nl@{
				@Suppress("NAME_SHADOWING")
				val pe = pe ?: return@nl
				
				val blockable = e.source.damageType in nineLifesBlockable
				
				if (blockable) {
					if (pe.amplifier == 4) {
						if (ASJUtilities.willEntityDie(e)) {
							if (e.source.damageType == DamageSource.wither.damageType && target.worldObj.rand.nextBoolean()) return@nl
							pe.amplifier = 0
							pe.duration = 100
							if (ASJUtilities.isServer) AlfheimCore.network.sendToAll(MessageEffect(target.entityId, pe.potionID, pe.duration, pe.amplifier))
							e.isCanceled = true
							return
						}
					} else if (pe.amplifier == 0) {
						e.isCanceled = true
						return
					}
				} else if (e.source.entity is EntityLivingBase && e.source.entity.isEntityAlive && target.worldObj.rand.nextInt(3) == 0) {
					e.source.entity.attackEntityFrom(e.source, e.ammount / 2)
				}
			}
			
			pe = target.getActivePotionEffect(AlfheimConfigHandler.potionIDStoneSkin)
			if (pe != null && !e.source.isMagical && !e.source.isDamageAbsolute) {
				e.isCanceled = true
				target.removePotionEffect(AlfheimConfigHandler.potionIDStoneSkin)
				target.addPotionEffect(PotionEffect(Potion.field_76444_x.id, pe.duration, 3, true))
				return
			}
			
			pe = target.getActivePotionEffect(AlfheimConfigHandler.potionIDButterShield)
			if (pe != null && pe.duration > 0 && e.source.isMagical && !e.source.isDamageAbsolute) {
				e.isCanceled = true
				if (--pe.amplifier <= 0) pe.duration = 0 // target.removePotionEffect(AlfheimRegistry.butterShield.id) <- ConcurrentModificationException :(
				if (ASJUtilities.isServer) AlfheimCore.network.sendToAll(MessageEffect(target.entityId, pe.potionID, pe.duration, pe.amplifier))
				return
			}
			
			// ################################################################ NOT CANCELING ################################################################
			
			pe = target.getActivePotionEffect(AlfheimConfigHandler.potionIDButterShield)
			if (!e.source.isMagical && !e.source.isDamageAbsolute && pe != null && pe.duration > 0) {
				e.ammount /= 2f
				pe.duration -= (e.ammount * 20).I
				val dur = max(pe.duration, 0)
				if (ASJUtilities.isServer) AlfheimCore.network.sendToAll(MessageEffect(target.entityId, pe.potionID, dur, pe.amplifier))
			}
		}
	}
	
	@SubscribeEvent
	fun onEntityDeath(e: LivingDeathEvent) {
		if (AlfheimCore.enableMMO) {
			if (e.entityLiving is EntityPlayer && !MinecraftServer.getServer().isSinglePlayer && AlfheimConfigHandler.deathScreenAddTime > 0 && !ItemTankMask.canBeSaved(e.entityLiving as EntityPlayer)) {
				e.entityLiving.clearActivePotions()
				e.entityLiving.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDLeftFlame, AlfheimConfigHandler.deathScreenAddTime, 0, true))
				e.entityLiving.dataWatcher.updateObject(6, 1f)
				
				e.isCanceled = true
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
					val tg = CardinalSystem.TargetingSystem.getTarget(player).target ?: continue
					
					if (tg.isDead || Vector3.entityDistance(player, tg) > if (tg is IBossDisplayData) 128.0 else 32.0) {
						CardinalSystem.TargetingSystem.setTarget(player, null, false, -2)
					}
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
			if (e.entityLiving.isPotionActive(AlfheimConfigHandler.potionIDLeftFlame)) {
				val pe = e.entityLiving.getActivePotionEffect(AlfheimConfigHandler.potionIDLeftFlame)!!
				pe.duration--
				if (!ASJUtilities.isServer) VisualEffectHandlerClient.onDeathTick(e.entityLiving)
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
		
		if (AlfheimCore.enableElvenStory && e.player.race == EnumRace.POOKA && !e.player.worldObj.isRemote) {
			val seg = CardinalSystem.forPlayer(e.player)
			val pos = Vector3.fromEntity(e.player)
			
			if (seg.lastPos == pos && e.player.fallDistance == 0f && !e.player.capabilities.isFlying)
				seg.standingStill++
			else
				seg.standingStill = 0
			
			seg.lastPos = pos
		}
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
		if (!e.entity.isEntityAlive) return
	}
	
	@SubscribeEvent
	fun onPlayerChangeDimension(e: PlayerEvent.PlayerChangedDimensionEvent) {
		if (AlfheimCore.enableElvenStory && !e.player.capabilities.isCreativeMode) {
			e.player.capabilities.allowFlying = false
			e.player.capabilities.isFlying = false
			e.player.sendPlayerAbilities()
		}
	}
}