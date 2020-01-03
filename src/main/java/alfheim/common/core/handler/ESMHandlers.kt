package alfheim.common.core.handler

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.entity.*
import alfheim.api.entity.EnumRace.*
import alfheim.api.event.PlayerChangedRaceEvent
import alfheim.client.core.handler.CardinalSystemClient.PlayerSegmentClient
import alfheim.common.core.helper.*
import alfheim.common.core.util.mfloor
import alfheim.common.network.*
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.*
import cpw.mods.fml.common.gameevent.PlayerEvent.*
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.entity.passive.*
import net.minecraft.entity.player.*
import net.minecraft.init.*
import net.minecraft.item.ItemStack
import net.minecraft.potion.*
import net.minecraft.server.MinecraftServer
import net.minecraft.util.MovingObjectPosition
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraftforge.event.entity.player.*
import net.minecraftforge.oredict.OreDictionary
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.Botania
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara
import vazkii.botania.common.item.equipment.tool.ToolCommons
import kotlin.math.*
import cpw.mods.fml.common.gameevent.TickEvent.Phase as TickPhase

object ESMHandler {
	
	init {
		MinecraftForge.EVENT_BUS.register(this)
		FMLCommonHandler.instance().bus().register(this)
		
		ElvenFlightHandler
		ElvenRaceHandler
	}
	
	fun checkAddAttrs() {
		if (!AlfheimCore.enableElvenStory) return
		for (o in MinecraftServer.getServer().configurationManager.playerEntityList) {
			val player = o as EntityPlayerMP
			EnumRace.ensureExistance(player)
			ElvenFlightHelper.ensureExistence(player)
		}
	}
	
	// EVENTS
	
	@SubscribeEvent
	fun onInteractWithBlock(event: PlayerInteractEvent) {
		if (AlfheimCore.enableElvenStory && !Botania.gardenOfGlassLoaded) {
			val equipped = event.entityPlayer.currentEquippedItem
			if (equipped != null && equipped.item === Items.bowl && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && !event.world.isRemote) {
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
	
	@SubscribeEvent
	fun onPlayerUpdate(e: PlayerTickEvent) {
		if (AlfheimCore.enableElvenStory) {
			doRaceAbility(e.player)
		}
		
		if (!ASJUtilities.isServer) fixSpriggan()
	}
	
	fun doRaceAbility(player: EntityPlayer) {
		when (player.race) {
			SALAMANDER -> doSalamander(player)
			SYLPH      -> doSylph(player)
			CAITSITH   -> Unit // below
			POOKA      -> doPooka(player) // also hook  scaring away creepers
			GNOME      -> doGnome(player)
			LEPRECHAUN -> Unit // below
			SPRIGGAN   -> doSpriggan(player)
			UNDINE     -> doUndine(player)
			IMP        -> Unit // hook: +20% mana discount for tools
			else       -> Unit // NO-OP
		}
	}
	
	fun doSalamander(player: EntityPlayer) {
		if (isAbilityDisabled(player)) return
		
		if (checkRemove(player, Potion.blindness)
		||  checkRemove(player, Potion.poison)
		||  checkRemove(player, Potion.confusion)) return
	}
	
	fun checkRemove(player: EntityPlayer, potion: Potion): Boolean {
		val effect = player.getActivePotionEffect(potion) ?: return false
		var dur = effect.duration
		var amp = effect.amplifier
		
		if (player.rng.nextInt(max(1, dur*(amp+1)*5)) == 0) {
			--amp
			if (amp < 0) {
				amp = 0
				dur = 0
			}
			
			effect.amplifier = amp
			effect.duration = dur
			
			AlfheimCore.network.sendToAll(MessageEffect(player.entityId, potion.id, dur, amp))
		}
		
		return false
	}
	
	fun doSylph(player: EntityPlayer) {
		if (isAbilityDisabled(player)) return
		
		if (player.capabilities.isFlying) {
			if (player.moveForward > 1e-4f)
				player.moveFlying(0f, 1f, 0.005f)
		} else {
			if (player.ticksExisted % 5 == 0)
				ElvenFlightHelper.regen(player, 1)
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	fun doCaitSith1(e: EntityInteractEvent) {
		if (AlfheimCore.enableElvenStory && e.entityPlayer.race === CAITSITH) doCaitSith2(e.entityPlayer, e.target)
	}
	
	fun doCaitSith2(player: EntityPlayer, tg: Entity) {
		if (isAbilityDisabled(player)) return
		
		var done = false
		
		if (tg is EntityTameable && !tg.isTamed) {
			tg.isTamed = true
			tg.func_152115_b(player.uniqueID.toString())
			done = true
		} else if (tg is EntityHorse) {
			tg.setTamedBy(player)
			tg.worldObj.setEntityState(tg, 7.toByte())
			done = true
		}
		
		if (done) {
			player.addPotionEffect(PotionEffect(Potion.regeneration.id, 600, 0, false))
			player.addPotionEffect(PotionEffect(Potion.field_76444_x.id, 1800, 0, false))
		}
	}
	
	fun doPooka(player: EntityPlayer) {
		if (player.worldObj.isRemote || isAbilityDisabled(player)) return
		
		val seg = CardinalSystem.forPlayer(player)
		if (seg.standingStill > 200) {
			if (player.ticksExisted % 50 == 0) if (player.health < player.maxHealth * 0.75 + 0.5) player.heal(0.5f)
			if (player.ticksExisted % 100 == 0) if (player.foodStats.foodLevel < 16) player.foodStats.addStats(1, 0.05f)
		}
	}
	
	fun doGnome(player: EntityPlayer) {
		if (ASJUtilities.isServer || !player.isSneaking) return
		if (isAbilityDisabled(player)) return
		
		val x = player.posX.mfloor() - 8
		val y = player.posY.mfloor() - 8
		val z = player.posZ.mfloor() - 8
		
		Botania.proxy.setWispFXDepthTest(false)
		
		for (i in x until (x+16)) {
			val j = y + player.ticksExisted % 17
			for (k in z until (z+16)) {
				val block = player.worldObj.getBlock(i, j, k)
				if (block === Blocks.air) continue
				
				val meta = player.worldObj.getBlockMetadata(i, j, k)
				
				if (OreDictionary.getOreIDs(ItemStack(block, 1, meta)).any { OreDictionary.getOreName(it).startsWith("ore") })
					Botania.proxy.wispFX(player.worldObj, i + 0.5, j + 0.5, k + 0.5, 0f, 1f, 0f, 0.25f)
			}
		}
		
		Botania.proxy.setWispFXDepthTest(true)
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun doLeprechaun(e: LivingHurtEvent) {
		val player = e.source.entity as? EntityPlayer ?: return
		
		if (AlfheimCore.enableElvenStory && player.race === LEPRECHAUN && e.source.damageType == "player" && !isAbilityDisabled(player))
			e.ammount *= 1.1f
	}
	
	fun doSpriggan(player: EntityPlayer) {
		if (isAbilityDisabled(player)) return
		
		if (ASJUtilities.isServer) {
			player.removePotionEffect(Potion.nightVision.id)
			player.removePotionEffect(Potion.blindness.id)
		} else {
			player.removePotionEffectClient(Potion.nightVision.id)
			player.removePotionEffectClient(Potion.blindness.id)
			Minecraft.getMinecraft().gameSettings.gammaSetting = 10f
		}
	}
	
	fun fixSpriggan() {
		if (AlfheimCore.enableElvenStory) {
			if (Minecraft.getMinecraft().thePlayer.race !== SPRIGGAN) {
				Minecraft.getMinecraft().gameSettings.gammaSetting = min(1f, Minecraft.getMinecraft().gameSettings.gammaSetting)
			}
		} else {
			Minecraft.getMinecraft().gameSettings.gammaSetting = min(1f, Minecraft.getMinecraft().gameSettings.gammaSetting)
		}
	}
	
	fun doUndine(player: EntityPlayer) {
		if (!isAbilityDisabled(player)) {
			player.air = 300
			
			if (player.ticksExisted % 20 == 0 && player.isInWater) ManaItemHandler.dispatchManaExact(ItemStack(Blocks.stone), player, 1, true)
		}
	}
	
	fun isAbilityDisabled(player: EntityPlayer) =
		if (ASJUtilities.isServer)
			!CardinalSystem.forPlayer(player).esmAbility
		else
			!PlayerSegmentClient.esmAbility
}

object ElvenFlightHandler {
	
	init {
		MinecraftForge.EVENT_BUS.register(this)
		FMLCommonHandler.instance().bus().register(this)
	}
	
	@SubscribeEvent
	fun onEntityConstructing(e: EntityConstructing) {
		if (AlfheimCore.enableElvenStory) {
			if (e.entity is EntityPlayer) {
				ElvenFlightHelper.ensureExistence(e.entity as EntityPlayer)
			}
		}
	}
	
	@SubscribeEvent
	fun onClonePlayer(e: PlayerEvent.Clone) {
		if (AlfheimCore.enableElvenStory) {
			e.entityPlayer.flight = if (e.wasDeath) 0.0 else e.original.flight
		}
	}
	
	@SubscribeEvent
	fun onPlayerRespawn(e: PlayerRespawnEvent) {
		if (AlfheimCore.enableElvenStory) {
			if (AlfheimConfigHandler.wingsBlackList.contains(e.player.worldObj.provider.dimensionId)) return
			e.player.capabilities.allowFlying = e.player.race != HUMAN
		}
	}
	
	@SubscribeEvent
	fun onPlayerChangeDimension(e: PlayerChangedDimensionEvent) {
		if (AlfheimCore.enableElvenStory) {
			if (e.player is EntityPlayerMP) {
				AlfheimCore.network.sendTo(Message2d(Message2d.m2d.ATTRIBUTE, 1.0, e.player.flight), e.player as EntityPlayerMP)
			}
		}
	}
	
	@SubscribeEvent
	fun onPlayerUpdate(e: PlayerTickEvent) {
		if (e.phase == TickPhase.START) return
		val player = e.player
		
		if (AlfheimCore.enableElvenStory) {
			if (!(ModItems.flightTiara as ItemFlightTiara).shouldPlayerHaveFlight(player)) {
				if (player.flight >= 0 && player.flight <= ElvenFlightHelper.max) {
					if (player.capabilities.isFlying) {
						if (player.isSprinting) player.moveFlying(0f, 1f, 0.00625f)
						ElvenFlightHelper.sub(player, if (player.isSprinting) 3 else if (abs(player.motionX) > 1e-4f || player.motionY > 1e-4f || abs(player.motionZ) > 1e-4f) 2 else 1)
					} else {
						ElvenFlightHelper.regen(player, if (player.moveForward == 0f && player.moveStrafing == 0f && player.onGround && player.isSneaking) 2 else 1)
					}
				}
				
				if (player.flight <= 0) player.capabilities.isFlying = false
			} else ElvenFlightHelper.regen(player, if (player.moveForward == 0f && player.moveStrafing == 0f && player.onGround && player.isSneaking) 2 else 1)
		}
	}
	
	@SubscribeEvent
	fun onPlayerSleeped(e: PlayerWakeUpEvent) {
		if (AlfheimCore.enableElvenStory) {
			e.entityPlayer.flight = ElvenFlightHelper.max
		}
	}
}

object ElvenRaceHandler {
	
	init {
		MinecraftForge.EVENT_BUS.register(this)
		FMLCommonHandler.instance().bus().register(this)
	}
	
	@SubscribeEvent
	fun onEntityConstructing(e: EntityConstructing) {
		if (AlfheimCore.enableElvenStory) {
			if (e.entity is EntityPlayer) {
				EnumRace.ensureExistance(e.entity as EntityPlayer)
			}
		}
	}
	
	@SubscribeEvent
	fun onClonePlayer(e: PlayerEvent.Clone) {
		if (AlfheimCore.enableElvenStory) {
			e.entityPlayer.raceID = e.original.raceID
		}
	}
	
	@SubscribeEvent
	fun onPlayerChangeDimension(e: PlayerChangedDimensionEvent) {
		if (AlfheimCore.enableElvenStory) {
			if (e.player is EntityPlayerMP) {
				AlfheimCore.network.sendTo(Message2d(Message2d.m2d.ATTRIBUTE, 0.0, e.player.raceID.toDouble()), e.player as EntityPlayerMP)
			}
		}
	}
	
	@SubscribeEvent
	fun onPlayerChangedRace(e: PlayerChangedRaceEvent) {
		if (ASJUtilities.isServer && AlfheimCore.enableMMO) {
			val pt = CardinalSystem.forPlayer(e.entityPlayer).party
			pt.setType(pt.indexOf(e.entityPlayer), e.raceTo.ordinal)
		}
	}
}