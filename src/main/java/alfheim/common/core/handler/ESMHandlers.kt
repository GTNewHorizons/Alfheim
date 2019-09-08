package alfheim.common.core.handler

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.entity.*
import alfheim.api.entity.EnumRace.*
import alfheim.api.event.PlayerChangedRaceEvent
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
import net.minecraft.entity.passive.EntityTameable
import net.minecraft.entity.player.*
import net.minecraft.init.*
import net.minecraft.item.ItemStack
import net.minecraft.potion.Potion
import net.minecraft.server.MinecraftServer
import net.minecraft.util.MovingObjectPosition
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraftforge.event.entity.player.*
import net.minecraftforge.oredict.OreDictionary
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
	fun onPlayerInteract(event: PlayerInteractEvent) {
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
	
	@SubscribeEvent
	fun onInteract(e: EntityInteractEvent) {
		if (AlfheimCore.enableElvenStory && e.entityPlayer.race === CAITSITH) doCaitSith(e.entityPlayer, e.target)
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun onEntityHurt(e: LivingHurtEvent) {
		val player = e.source.entity as? EntityPlayer ?: return
		
		if (AlfheimCore.enableElvenStory && player.race === LEPRECHAUN && e.source.damageType == "player") {
			e.entityLiving.hurtResistantTime -= max(0f, e.entityLiving.maxHurtResistantTime * 0.2f).toInt()
		}
	}
	
	fun doRaceAbility(player: EntityPlayer) {
		when (player.race) {
			SALAMANDER -> doSalamander(player)
			SYLPH      -> doSylph(player)
			CAITSITH   -> Unit // above
			POOKA      -> Unit // hook: scaring away creepers
			GNOME      -> doGnome(player)
			LEPRECHAUN -> Unit // above
			SPRIGGAN   -> doSpriggan(player)
			UNDINE     -> doUndine(player)
			IMP        -> Unit // hook: +20% mana discount for tools
			else       -> Unit // NO-OP
		}
	}
	
	fun doSalamander(player: EntityPlayer) {
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
		if (player.capabilities.isFlying) {
			if (player.moveForward > 1e-4f)
				player.moveFlying(0f, 1f, 0.01f)
		} else {
			if (player.ticksExisted % 10 == 0)
				ElvenFlightHelper.add(player, 1)
		}
	}
	
	fun doCaitSith(player: EntityPlayer, tg: Entity) {
		if (tg is EntityTameable) {
			tg.isTamed = true
			tg.func_152115_b(player.uniqueID.toString())
		}
	}
	
	fun doUndine(player: EntityPlayer) {
		player.air = 300
	}
	
	fun doSpriggan(player: EntityPlayer) {
		if (ASJUtilities.isServer)
			player.removePotionEffect(Potion.nightVision.id)
		else {
			player.removePotionEffectClient(Potion.nightVision.id)
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
	
	fun doGnome(player: EntityPlayer) {
		if (ASJUtilities.isServer || !player.isSneaking) return
		
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
				
				for (id in OreDictionary.getOreIDs(ItemStack(block, 1, meta))) {
					if (OreDictionary.getOreName(id).startsWith("ore"))
						Botania.proxy.wispFX(player.worldObj, i + 0.5, j + 0.5, k + 0.5, 0f, 1f, 0f, 0.25f)
				}
			}
		}
		
		Botania.proxy.setWispFXDepthTest(true)
	}
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
			if (!AlfheimConfigHandler.enableWingsNonAlfheim && e.player.worldObj.provider.dimensionId != AlfheimConfigHandler.dimensionIDAlfheim) return
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
			if (!player.capabilities.isCreativeMode) {
				if (!(ModItems.flightTiara as ItemFlightTiara).shouldPlayerHaveFlight(player)) {
					if (player.flight >= 0 && player.flight <= ElvenFlightHelper.max) {
						if (player.capabilities.isFlying) {
							ElvenFlightHelper.sub(player, if (player.isSprinting) 3 else if (abs(player.motionX) > 1e-4f || player.motionY > 1e-4f || abs(player.motionZ) > 1e-4f) 2 else 1)
							if (player.isSprinting) player.moveFlying(0f, 1f, 0.1f)
						} else {
							ElvenFlightHelper.add(player, if (player.moveForward == 0f && player.moveStrafing == 0f && player.onGround && player.isSneaking) 2 else 1)
						}
					}
					
					if (player.flight <= 0) player.capabilities.isFlying = false
				} else ElvenFlightHelper.add(player, if (player.moveForward == 0f && player.moveStrafing == 0f && player.onGround && player.isSneaking) 2 else 1)
			} else {
				player.flight = ElvenFlightHelper.max
			}
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