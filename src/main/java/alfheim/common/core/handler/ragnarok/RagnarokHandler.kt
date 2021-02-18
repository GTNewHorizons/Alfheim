package alfheim.common.core.handler.ragnarok

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.common.achievement.AlfheimAchievements
import alfheim.common.block.BlockSnowGrass
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.entity.boss.EntityFenrir
import alfheim.common.item.AlfheimItems
import alfheim.common.item.equipment.bauble.ItemPriestEmblem
import alfheim.common.item.equipment.bauble.faith.ItemRagnarokEmblem
import alfheim.common.network.*
import alfheim.common.world.dim.alfheim.biome.BiomeAlfheim
import alfmod.common.entity.EntityMuspellsun
import alfmod.common.entity.boss.EntityDedMoroz
import alfmod.common.item.AlfheimModularItems
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.common.eventhandler.*
import cpw.mods.fml.common.gameevent.*
import cpw.mods.fml.relauncher.*
import net.minecraft.entity.*
import net.minecraft.entity.monster.*
import net.minecraft.entity.passive.EntityWolf
import net.minecraft.entity.player.*
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.potion.Potion
import net.minecraft.server.MinecraftServer
import net.minecraft.util.DamageSource
import net.minecraft.world.storage.DerivedWorldInfo
import net.minecraftforge.client.event.EntityViewRenderEvent
import net.minecraftforge.event.entity.living.*
import net.minecraftforge.event.entity.player.PlayerDropsEvent
import java.io.*

object RagnarokHandler {
	
	var isSeasonDeadly = false
	
	var winter = false
		set(value) {
			AlfheimCore.winter = value
			BiomeAlfheim.alfheimBiomes.forEach { it.temperature = if (value) 0f else 0.5f }
			field = value
		}
	var winterTicks = 0
	const val MAX_WINTER_TICKS = 1752000 // 3 game years divided by 15 ~= 24.3 real hours
	
	var summer = false
		set(value) {
			BlockSnowGrass.meltDelay = if (value) 1 else 20
			field = value
		}
	var summerTicks = 0
	const val MAX_SUMMER_TICKS = 584000 // 1 game year divided by 15 ~= 8.1 real hours
	
	var ragnarok = false
	var fogFade = 1f
	
	var handsSet: MutableSet<Vector3> = HashSet()
	
	init {
		eventForge()
		eventFML()
		
		RagnarokEndHandler
		RagnarokEmblemCraftHandler
		RagnarokEmblemStabilizationHandler
	}
	
	fun load(save: String) {
		val file = File("$save/data/Ragnarok.sys")
		if (!file.exists()) {
			ASJUtilities.log("Ragnarok data file not found. Using default values...")
			return
		}
		
		try {
			ObjectInputStream(FileInputStream(file)).use { oin ->
				isSeasonDeadly = oin.readObject() as Boolean
				winter = oin.readObject() as Boolean
				winterTicks = oin.readObject() as Int
				summer = oin.readObject() as Boolean
				summerTicks = oin.readObject() as Int
				ragnarok = oin.readObject() as Boolean
				
				handsSet = oin.readObject() as HashSet<Vector3>
			}
		} catch (e: Throwable) {
			ASJUtilities.error("Unable to read whole Ragnarok data. Using default values...")
			e.printStackTrace()
			isSeasonDeadly = false
			winter = false
			winterTicks = 0
			summer = false
			summerTicks = 0
			ragnarok = false
			
			handsSet = HashSet()
		}
	}
	
	@SubscribeEvent
	fun tick(e: TickEvent.ServerTickEvent) {
		if (e.phase != TickEvent.Phase.START) return
		
		if (winter) ++winterTicks
		if (summer) ++summerTicks
		
		if (canStartRagnarok()) {
			summer = false
			summerTicks = 0
			
			ragnarok = true
			AlfheimCore.network.sendToAll(Message1d(Message1d.m1d.RAGNAROK, 0.999))
			
			ASJUtilities.sayToAllOnline("alfheimmisc.ragnarok.startedRagnarok")
			
			MinecraftServer.getServer().configurationManager.playerEntityList.forEach { player ->
				player as EntityPlayer
				
				if (player.dimension != AlfheimConfigHandler.dimensionIDAlfheim) return@forEach
				if (!player.hasAchievement(AlfheimAchievements.ragnarok)) return@forEach // can't equip emblem w/o achievement, but let it be here just in case
				val emblem = ItemRagnarokEmblem.getEmblem(player) ?: return@forEach
				
				ItemNBTHelper.setBoolean(emblem, ItemRagnarokEmblem.TAG_BOUND, true)
				player.triggerAchievement(AlfheimAchievements.theEND)
				
				ASJUtilities.say(player, "alfheimmisc.ragnarok.startedRagnarok.special")
			}
		}
	}
	
	fun save(save: String) {
		try {
			ObjectOutputStream(FileOutputStream("$save/data/Ragnarok.sys")).use { oos ->
				oos.writeObject(isSeasonDeadly)
				oos.writeObject(winter)
				oos.writeObject(winterTicks)
				oos.writeObject(summer)
				oos.writeObject(summerTicks)
				oos.writeObject(ragnarok)
				
				oos.writeObject(handsSet)
			}
		} catch (e: Throwable) {
			ASJUtilities.error("Unable to save whole Ragnarok data. Discarding. Sorry :(")
			e.printStackTrace()
		}
	}
	
	fun canStartWinter() = !winter && !summer && !ragnarok
	
	fun canStartSummer() = winter && !summer && !ragnarok && winterTicks >= MAX_WINTER_TICKS * if (isSeasonDeadly) 10 else 1
	
	fun canStartRagnarok() = !winter && summer && !ragnarok && summerTicks >= MAX_SUMMER_TICKS * if (isSeasonDeadly) 5 else 1
	
	fun canEndRagnarok() = !winter && !summer && ragnarok
	
	// ################################################################
	// ################### Consume Priests' emblems ###################
	// ################################################################
	
	@SubscribeEvent(priority = EventPriority.LOWEST) // let it be canceled
	fun onPlayerDied(e: LivingDeathEvent) {
		if (!ragnarok) return
		
		val ragnar = e.source.entity as? EntityPlayer ?: return
		val priest = e.entityLiving as? EntityPlayer ?: return
		
		val emblemDark = ItemRagnarokEmblem.getEmblem(ragnar) ?: return
		val emblemLight = ItemPriestEmblem.getEmblem(-1, priest) ?: return
		
		if (ragnar.heldItem?.item !== AlfheimItems.soulSword) return
		
		when (emblemLight.item) {
			AlfheimItems.priestEmblem -> {
				val arr = ItemNBTHelper.getByteArray(emblemDark, ItemRagnarokEmblem.TAG_CONSUMED, ByteArray(6))
				if (arr[emblemLight.meta] > 0) return
				arr[emblemLight.meta] = 1
				ItemNBTHelper.setByteArray(emblemDark, ItemRagnarokEmblem.TAG_CONSUMED, arr)
			}
			
			AlfheimItems.aesirEmblem  -> {
				val arr = ItemNBTHelper.getByteArray(emblemDark, ItemRagnarokEmblem.TAG_CONSUMED, ByteArray(6))
				val id = arr.indexOfFirst { it < 1 }
				if (id == -1) return
				arr[id] = 1
				ItemNBTHelper.setByteArray(emblemDark, ItemRagnarokEmblem.TAG_CONSUMED, arr)
			}
			
			else                      -> return
		}
		
		ragnar.playSoundAtEntity("mob.enderdragon.growl", 10f, 0.1f)
		ASJUtilities.say(ragnar, "alfheimmisc.ragnarok.consumed")
		PlayerHandler.getPlayerBaubles(priest)[0] = null
	}
	
	// technical stuff
	
	@SubscribeEvent
	fun spawnSeasonalMobs(e: LivingEvent.LivingUpdateEvent) {
		val player = e.entityLiving as? EntityPlayer ?: return
		val world = player.worldObj
		
		if (world.provider.dimensionId != AlfheimConfigHandler.dimensionIDAlfheim) return
		if (!world.gameRules.getGameRuleBooleanValue("doMobSpawning")) return
		
		val rand = world.rand
		if (rand.nextInt(6000) != 0) return
		
		if (winter) {
			val x = player.posX.mfloor() + ASJUtilities.randInBounds(-64, 64, rand)
			val z = player.posZ.mfloor() + ASJUtilities.randInBounds(-64, 64, rand)
			val y = world.getTopSolidOrLiquidBlock(x, z)
			
			world.spawnEntityInWorld(EntityDedMoroz(world, x.D, y.D, z.D))
			return
		}
		
		if (summer) {
			val x = player.posX.mfloor() + ASJUtilities.randInBounds(-64, 64, rand)
			val z = player.posZ.mfloor() + ASJUtilities.randInBounds(-64, 64, rand)
			val y = world.getTopSolidOrLiquidBlock(x, z)
			
			when (rand.nextInt(5)) {
				1, 2 -> for (i in 0..rand.nextInt(3)) world.spawnEntityInWorld(EntityBlaze(world).apply { setPosition(x.D, y.D, z.D) })
				3, 4 -> for (i in 0..rand.nextInt(3)) world.spawnEntityInWorld(EntityMagmaCube(world).apply { setPosition(x.D, y.D, z.D) })
				else -> world.spawnEntityInWorld(EntityMuspellsun(world).apply { setPosition(x.D, y.D, z.D) })
			}
		}
	}
	
	@SubscribeEvent
	fun doNotBetrayTheEnd(e: LivingDeathEvent) {
		val victim = e.entityLiving
		if (!(victim is EntityFenrir || victim is EntityDedMoroz || victim is EntityMuspellsun)) return
		
		val killer = e.source.entity as? EntityPlayer ?: return
		if (killer.isPotionActive(Potion.invisibility)) return
		
		if (ItemRagnarokEmblem.getEmblem(killer) == null) return
		
		val world = killer.worldObj
		when (victim) {
			is EntityFenrir     -> {
				for (i in 0..ASJUtilities.randInBounds(12, 18, killer.rng))
					world.spawnEntityInWorld(
						EntityWolf(world).apply {
							setPosition(killer)
							// not sure which one so both
							attackTarget = killer
							setRevengeTarget(killer)
							getEntityAttribute(SharedMonsterAttributes.maxHealth).baseValue = ASJUtilities.randInBounds(26, 38, killer.rng).D
						}
					)
			}
			
			is EntityDedMoroz   -> {
				if (!(winter || summer || ragnarok)) return
				for (i in 0..ASJUtilities.randInBounds(3, 5, killer.rng))
					world.spawnEntityInWorld(
						EntityDedMoroz(world).apply {
							setPosition(killer)
							// not sure which one so both
							attackTarget = killer
							setRevengeTarget(killer)
							getEntityAttribute(SharedMonsterAttributes.maxHealth).baseValue = ASJUtilities.randInBounds(420, 480, killer.rng).D
						}
					)
			}
			
			is EntityMuspellsun -> {
				if (!(summer || ragnarok)) return
				for (i in 0..ASJUtilities.randInBounds(10, 16, killer.rng))
					world.spawnEntityInWorld(
						@Suppress("IMPLICIT_CAST_TO_ANY") // not EntityLiving because possible IMob (?)
						(when (killer.rng.nextInt(5)) {
							in 1..2 -> EntityBlaze(world)
							in 3..4 -> EntityMagmaCube(world)
							else    -> EntityMuspellsun(world)
						} as EntityLiving).apply {
							setPosition(killer)
							// not sure which one so both
							attackTarget = killer
							setRevengeTarget(killer)
							val baseHealth = if (this is EntityMuspellsun) 60 else 24
							getEntityAttribute(SharedMonsterAttributes.maxHealth).baseValue = ASJUtilities.randInBounds(baseHealth, (baseHealth * 1.5).I, killer.rng).D
							if (this is EntityMagmaCube) slimeSize = killer.rng.nextInt(3) + 2
						}
					)
			}
		}
		
		ASJUtilities.say(killer, "alfheimmisc.ragnarok.betrayer")
	}
	
	@SubscribeEvent
	fun controlWeather(e: TickEvent.WorldTickEvent) {
		if (e.world.provider.dimensionId != AlfheimConfigHandler.dimensionIDAlfheim) return
		
		val time = if (winter) 0 else if (summer) Int.MAX_VALUE else return
		
		var info = e.world.worldInfo
		if (info is DerivedWorldInfo) info = info.theWorldInfo
		
		e.world.setRainStrength(if (time == 0) 1f else 0f)
		info.rainTime = time
		info.isRaining = time == 0
		
		// no thunder though
		e.world.setThunderStrength(0f)
		info.thunderTime = 0
		info.isThundering = false
		
		AlfheimCore.network.sendToDimension(Message3d(Message3d.m3d.WAETHER, if (time == 0) 1.0 else 0.0, time.D, time.D), e.world.provider.dimensionId)
	}
	
	val fenrirSet = arrayOf(AlfheimItems.fenrirHelmet, AlfheimItems.fenrirChestplate, AlfheimItems.fenrirLeggings, AlfheimItems.fenrirBoots).apply { reverse() }
	val leatherSet = arrayOf(Items.leather_helmet, Items.leather_chestplate, Items.leather_leggings, Items.leather_boots).apply { reverse() }
	val iceSet = arrayOf(AlfheimModularItems.snowHelmet, AlfheimModularItems.snowChest, AlfheimModularItems.snowLeggings, AlfheimModularItems.snowBoots).apply { reverse() }
	
	@SubscribeEvent
	fun deathFromDeadlySeason(e: LivingEvent.LivingUpdateEvent) {
		if (!isSeasonDeadly) return
		if (e.entityLiving.dimension != AlfheimConfigHandler.dimensionIDAlfheim) return
		if (e.entityLiving.ticksExisted % 50 != 0) return
		
		if (winter) {
			if (checkSet(e.entityLiving, fenrirSet)) return
			@Suppress("UNCHECKED_CAST") // supertype you bitch
			e.entityLiving.attackEntityFrom(DamageSource("frost").apply { if (!checkSet(e.entityLiving, leatherSet as Array<Item>)) setDamageBypassesArmor() }, 1f)
			return
		}
		
		if (summer) {
			if (checkSet(e.entityLiving, iceSet, 2)) return
			e.entityLiving.attackEntityFrom(DamageSource.onFire, 1f)
			return
		}
	}
	
	fun checkSet(e: EntityLivingBase, set: Array<Item>, dmg: Int = 0): Boolean {
		for (i in 1..4) {
			val stack = e.getEquipmentInSlot(i) ?: return false
			if (stack.item != set[i - 1])
				return false
			else if (dmg > 0) {
				stack.damageItem(dmg, e)
				
				if (stack.stackSize == 0)
					e.setCurrentItemOrArmor(i, null)
			}
		}
		
		return true
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST) // for Baubles to add it's contents
	fun lockEmblemForever(e: PlayerDropsEvent) {
		if (!e.entityPlayer.hasAchievement(AlfheimAchievements.theEND)) return
		val i = e.drops.indexOfFirst { ItemNBTHelper.getBoolean(it.entityItem, ItemRagnarokEmblem.TAG_BOUND, false) }
		if (i == -1) return
		
		val entity = e.drops.removeAt(i) ?: return
		PlayerHandler.getPlayerBaubles(e.entityPlayer)[0] = entity.entityItem?.copy() ?: return
		entity.setEntityItemStack(null)
		entity.setDead()
	}
	
	@SubscribeEvent
	fun informAboutRagnarok(e: PlayerEvent.PlayerLoggedInEvent) {
		AlfheimCore.network.sendTo(Message1d(Message1d.m1d.RAGNAROK, if (ragnarok) 0.0 else 1.0), e.player as EntityPlayerMP)
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun makeTheHorizonRed(e: EntityViewRenderEvent.FogColors) {
		if (!ragnarok) return
		
		if (e.entity.dimension == 1) return // not sure
		
		if (fogFade > 0) fogFade -= 0.001f
		
		e.red += (1f - e.red) * (1 - fogFade)
		e.green *= fogFade
		e.blue *= fogFade
	}
}