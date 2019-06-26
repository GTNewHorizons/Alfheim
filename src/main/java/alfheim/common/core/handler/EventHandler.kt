package alfheim.common.core.handler

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.event.*
import alfheim.client.render.world.SpellEffectHandlerClient
import alfheim.client.render.world.SpellEffectHandlerClient.Spells
import alfheim.common.core.handler.CardinalSystem.*
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party
import alfheim.common.core.handler.CardinalSystem.TargetingSystem.Target
import alfheim.common.core.registry.*
import alfheim.common.core.util.*
import alfheim.common.entity.EntityAlfheimPixie
import alfheim.common.entity.Flight
import alfheim.common.entity.boss.EntityFlugel
import alfheim.common.network.Message1d
import alfheim.common.network.Message2d
import alfheim.common.network.Message2d.m2d
import alfheim.common.network.MessageEffect
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent
import cpw.mods.fml.common.gameevent.TickEvent.Phase
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent
import net.minecraft.block.material.Material
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.boss.IBossDisplayData
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect
import net.minecraft.server.MinecraftServer
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatComponentTranslation
import net.minecraft.util.ChunkCoordinates
import net.minecraft.util.DamageSource
import net.minecraft.util.IChatComponent
import net.minecraft.util.MathHelper
import net.minecraft.util.MovingObjectPosition
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.living.LivingDropsEvent
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.api.recipe.ElvenPortalUpdateEvent
import vazkii.botania.common.Botania
import vazkii.botania.common.block.tile.TileAlfPortal
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.equipment.tool.ToolCommons
import vazkii.botania.common.item.equipment.tool.elementium.ItemElementiumAxe

class EventHandler {
	
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
			if (AlfheimCore.enableElvenStory) {
				AlfheimCore.network.sendTo(Message1d(Message1d.m1d.DEATH_TIMER, AlfheimConfig.deathScreenAddTime.toDouble()), e.player as EntityPlayerMP)
				AlfheimCore.network.sendTo(Message1d(Message1d.m1d.CL_SLOWDOWN, (if (AlfheimConfig.slowDownClients) 1 else 0).toDouble()), e.player as EntityPlayerMP)
				if (!(e.player as EntityPlayerMP).func_147099_x().hasAchievementUnlocked(AlfheimAchievements.alfheim) && e.player.dimension != AlfheimConfig.dimensionIDAlfheim) {
					ASJUtilities.sendToDimensionWithoutPortal(e.player, AlfheimConfig.dimensionIDAlfheim, 0.5, 253.0, 0.5)
					e.player.triggerAchievement(AlfheimAchievements.alfheim)
					e.player.addChatComponentMessage(ChatComponentTranslation("elvenstory.welcome0"))
					e.player.addChatComponentMessage(ChatComponentTranslation("elvenstory.welcome1"))
					e.player.addChatComponentMessage(ChatComponentTranslation("elvenstory.welcome2"))
					e.player.inventory.addItemStackToInventory(ItemStack(ModItems.lexicon))
					e.player.setSpawnChunk(ChunkCoordinates(0, 253, 0), true, AlfheimConfig.dimensionIDAlfheim)
				}
				if (AlfheimCore.enableMMO) CardinalSystem.transfer(e.player as EntityPlayerMP)
			}
		}
	}
	
	@SubscribeEvent
	fun onNetherPortalActivation(e: NetherPortalActivationEvent) {
		if (e.worldObj.provider.dimensionId == AlfheimConfig.dimensionIDAlfheim) e.isCanceled = true
	}
	
	@SubscribeEvent
	fun onAlfPortalUpdate(e: ElvenPortalUpdateEvent) {
		if (e.portalTile.worldObj.provider.dimensionId == AlfheimConfig.dimensionIDAlfheim && (e.portalTile as TileAlfPortal).ticksOpen >= 0) (e.portalTile as TileAlfPortal).ticksOpen = 0
	}
	
	@SubscribeEvent
	fun onEntityDrops(event: LivingDropsEvent) {
		if (event.recentlyHit && event.source.entity != null && event.source.entity is EntityPlayer) {
			val weapon = (event.source.entity as EntityPlayer).currentEquippedItem
			if (weapon != null && weapon.item is ItemElementiumAxe && event.entityLiving is EntityFlugel && event.entity.worldObj.rand.nextInt(13) < 1 + EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, weapon)) {
				val entityitem = EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, ItemStack(AlfheimItems.flugelHead))
				entityitem.delayBeforeCanPickup = 10
				event.drops.add(entityitem)
			}
		}
	}
	
	@SubscribeEvent
	fun onPlayerInteract(event: PlayerInteractEvent) {
		if (AlfheimCore.enableElvenStory && !Botania.gardenOfGlassLoaded) {
			val equipped = event.entityPlayer.currentEquippedItem
			if (equipped != null && equipped.item === Items.bowl && event.action == Action.RIGHT_CLICK_BLOCK && !event.world.isRemote) {
				val movingobjectposition = ToolCommons.raytraceFromEntity(event.world, event.entityPlayer, true, 4.5)
				
				if (movingobjectposition != null && movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && !event.world.isRemote) {
					val i = movingobjectposition.blockX
					val j = movingobjectposition.blockY
					val k = movingobjectposition.blockZ
					
					if (event.world.getBlock(i, j, k).material === Material.water) {
						--equipped.stackSize
						
						if (equipped.stackSize <= 0)
							event.entityPlayer.inventory.setInventorySlotContents(event.entityPlayer.inventory.currentItem, ItemStack(ModItems.waterBowl))
						else
							event.entityPlayer.dropPlayerItemWithRandomChoice(ItemStack(ModItems.waterBowl), false)
					}
				}
			}
		}
	}
	
	// ################################ ATTRIBUTES ################################
	
	@SubscribeEvent
	fun onEntityConstructing(e: EntityConstructing) {
		if (AlfheimCore.enableElvenStory) {
			if (e.entity is EntityPlayer) {
				EnumRace.ensureExistance(e.entity as EntityPlayer)
				Flight.ensureExistence(e.entity as EntityPlayer)
			}
		}
	}
	
	@SubscribeEvent
	fun onClonePlayer(e: PlayerEvent.Clone) {
		if (AlfheimCore.enableElvenStory) {
			val r = EnumRace.getRaceID(e.original)
			EnumRace.setRaceID(e.entityPlayer, r.toDouble())
			if (!e.wasDeath) Flight.set(e.entityPlayer, Flight.get(e.original))
		}
	}
	
	@SubscribeEvent
	fun onPlayerRespawn(e: PlayerRespawnEvent) {
		if (AlfheimCore.enableElvenStory) {
			if (!AlfheimConfig.enableWingsNonAlfheim && e.player.worldObj.provider.dimensionId != AlfheimConfig.dimensionIDAlfheim) return
			e.player.capabilities.allowFlying = EnumRace.getRace(e.player) != EnumRace.HUMAN
		}
	}
	
	@SubscribeEvent
	fun onPlayerChangeDimension(e: PlayerChangedDimensionEvent) {
		if (AlfheimCore.enableElvenStory) {
			if (e.player is EntityPlayerMP) {
				AlfheimCore.network.sendTo(Message2d(m2d.ATTRIBUTE, 0.0, EnumRace.getRaceID(e.player).toDouble()), e.player as EntityPlayerMP)
				AlfheimCore.network.sendTo(Message2d(m2d.ATTRIBUTE, 1.0, Flight.get(e.player)), e.player as EntityPlayerMP)
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
		if (AlfheimCore.enableElvenStory && e.entityLiving is EntityPlayer && e.source.getDamageType() == DamageSource.fall.getDamageType() && EnumRace.getRace(e.entityLiving as EntityPlayer) != EnumRace.HUMAN) {
			e.isCanceled = true
			return
		}
		if (PartySystem.friendlyFire(e.entityLiving, e.source)) {
			e.isCanceled = true
			return
		}
		if (e.source.isFireDamage && !e.source.isUnblockable && e.entityLiving is EntityPlayer && (e.entityLiving as EntityPlayer).getCurrentArmor(1) != null && (e.entityLiving as EntityPlayer).getCurrentArmor(1).item === AlfheimItems.elementalLeggings && ManaItemHandler.requestManaExact((e.entityLiving as EntityPlayer).getCurrentArmor(1), e.entityLiving as EntityPlayer, MathHelper.ceiling_float_int(10 * ammount), !e.entityLiving.worldObj.isRemote)) {
			e.isCanceled = true
			return
		}
		
		// ################################################################ NOT CANCELING ################################################################
		
		if (AlfheimCore.enableMMO && e.entityLiving.isPotionActive(AlfheimRegistry.decay) && !e.source.isFireDamage && !e.source.isMagicDamage && e.source !is DamageSourceSpell && e.source.damageType != DamageSourceSpell.bleeding.damageType)
			e.entityLiving.addPotionEffect(PotionEffect(AlfheimRegistry.bleeding.id, 120, 0, true))
	}
	
	@SubscribeEvent
	fun onEntityHurt(e: LivingHurtEvent) {
		if (PartySystem.friendlyFire(e.entityLiving, e.source)) {
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
			nl@ if (pe != null) {
				val blockable = e.source.damageType == DamageSource.fall.damageType ||
								e.source.damageType == DamageSource.drown.damageType ||
								e.source.damageType == DamageSource.inFire.damageType ||
								e.source.damageType == DamageSource.onFire.damageType ||
								e.source.damageType == DamageSourceSpell.poison.damageType ||
								e.source.damageType == DamageSource.wither.damageType
				
				if (blockable) {
					if (pe.amplifier == 4) {
						if (ASJUtilities.willEntityDie(e)) {
							if (e.source.damageType == DamageSource.wither.damageType && e.entityLiving.worldObj.rand.nextBoolean()) break@nl
							pe.amplifier = 0
							pe.duration = 100
							if (ASJUtilities.isServer) AlfheimCore.network.sendToAll(MessageEffect(e.entityLiving.entityId, pe.potionID, pe.duration, pe.amplifier))
							e.isCanceled = true
							return
						}
					} else if (pe.amplifier == 0) {
						e.isCanceled = true
						return
					}
				} else if (e.source.entity != null && e.source.entity is EntityLivingBase && e.source.entity.isEntityAlive && e.entityLiving.worldObj.rand.nextInt(3) == 0) {
					e.source.entity.attackEntityFrom(e.source, e.ammount / 2)
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
				val dur = Math.max(pe.duration -= (e.ammount * 20).toInt(), 0)
				if (ASJUtilities.isServer) AlfheimCore.network.sendToAll(MessageEffect(e.entityLiving.entityId, pe.potionID, dur, pe.amplifier))
			}
		}
	}
	
	@SubscribeEvent
	fun onEntityDeath(e: LivingDeathEvent) {
		if (AlfheimCore.enableMMO) {
			if (e.entityLiving is EntityPlayer && !MinecraftServer.getServer().isSinglePlayer) {
				e.entityLiving.clearActivePotions()
				e.entityLiving.addPotionEffect(PotionEffect(AlfheimRegistry.leftFlame.id, AlfheimConfig.deathScreenAddTime, 0, true))
				e.entityLiving.dataWatcher.updateObject(6, 1f)
			}
			
			val pt = PartySystem.getMobParty(e.entityLiving)
			pt?.setDead(e.entityLiving, true)
		}
	}
	
	@SubscribeEvent
	fun onServerTick(e: ServerTickEvent) {
		if (AlfheimCore.enableMMO) {
			if (e.phase == Phase.START) {
				SpellCastingSystem.tick()
				TimeStopSystem.tick()
			}
			for (name in CardinalSystem.playerSegments.keys) {
				val player = MinecraftServer.getServer().configurationManager.func_152612_a(name)
				if (player == null)
					CardinalSystem.playerSegments[name].target = Target(null, false)
				else {
					val tg = TargetingSystem.getTarget(player)
					if (tg != null && tg.target != null && (!tg.target.isEntityAlive || Vector3.entityDistance(player, tg.target) > if (tg.target is IBossDisplayData) 128 else 32))
						TargetingSystem.setTarget(player, null, false)
				}
			}
		}
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
				val pt = PartySystem.getMobParty(e.entityLiving)
				pt?.setDead(e.entityLiving, true)
			}
		}
	}
	
	@SubscribeEvent
	fun onPlayerUpdate(e: PlayerTickEvent) {
		if (e.phase == Phase.START) return
		val player = e.player
		
		//		player.rotationYaw = player.rotationPitch = 90;
		
		if (!player.capabilities.isCreativeMode) {
			if (AlfheimCore.enableElvenStory) {
				if (Flight.get(player) >= 0 && Flight.get(player) <= Flight.max()) {
					if (player.capabilities.isFlying) {
						Flight.sub(player, (if (player.isSprinting) 4 else if (player.motionX != 0.0 || player.motionY > 0.0 || player.motionZ != 0.0) 2 else 1).toDouble())
						if (player.isSprinting) player.moveFlying(0f, 1f, 0.01f)
					} else
						Flight.add(player, (if (Flight.get(player) < Flight.max()) 1 else 0).toDouble())
				}
				
				if (Flight.get(player) <= 0) player.capabilities.isFlying = false
			}
		}
	}
	
	@SubscribeEvent
	fun onNewPotionEffect(e: LivingPotionEvent.Add.Post) {
		if (ASJUtilities.isServer) AlfheimCore.network.sendToAll(MessageEffect(e.entityLiving.entityId, e.effect.potionID, e.effect.duration, e.effect.amplifier))
	}
	
	@SubscribeEvent
	fun onChangedPotionEffect(e: LivingPotionEvent.Change.Post) {
		if (ASJUtilities.isServer) AlfheimCore.network.sendToAll(MessageEffect(e.entityLiving.entityId, e.effect.potionID, e.effect.duration, e.effect.amplifier, e.update))
	}
	
	@SubscribeEvent
	fun onFinishedPotionEffect(e: LivingPotionEvent.Remove.Post) {
		if (ASJUtilities.isServer) AlfheimCore.network.sendToAll(MessageEffect(e.entityLiving.entityId, e.effect.potionID, e.effect.duration, e.effect.amplifier))
	}
	
	@SubscribeEvent
	fun onEntityUpdate(e: EntityUpdateEvent) {
		if (!e.entity.isEntityAlive) return
		if ((ASJUtilities.isServer || AlfheimConfig.slowDownClients) && !e.entity.canEntityUpdate) {
			e.isCanceled = true
			e.entity.canEntityUpdate = true
		}
	}
	
	@SubscribeEvent
	fun onTileUpdate(e: TileUpdateEvent) {
		if (!e.tile.canTileUpdate) {
			e.isCanceled = true
			e.tile.canTileUpdate = true
		}
	}
	
	companion object {
		
		fun checkAddAttrs() {
			if (!AlfheimCore.enableElvenStory) return
			for (o in MinecraftServer.getServer().configurationManager.playerEntityList) {
				val player = o as EntityPlayerMP
				EnumRace.ensureExistance(player)
				Flight.ensureExistence(player)
			}
		}
	}
}