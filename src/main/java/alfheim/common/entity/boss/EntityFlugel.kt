package alfheim.common.entity.boss

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.api.ModInfo
import alfheim.api.block.tile.SubTileEntity
import alfheim.common.achievement.AlfheimAchievements
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.tile.TileAnomaly
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.*
import alfheim.common.entity.boss.ai.flugel.*
import alfheim.common.item.AlfheimItems
import alfheim.common.item.material.ElvenResourcesMetas
import alfheim.common.item.relic.ItemFlugelSoul
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.*
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry
import net.minecraft.entity.ai.EntityAIWatchClosest
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.*
import net.minecraft.init.Items
import net.minecraft.item.*
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.potion.*
import net.minecraft.tileentity.TileEntityBeacon
import net.minecraft.util.*
import net.minecraft.world.*
import net.minecraftforge.common.util.FakePlayer
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL
import vazkii.botania.api.boss.IBotaniaBoss
import vazkii.botania.client.core.handler.BossBarHandler
import vazkii.botania.common.Botania
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.core.handler.ConfigHandler
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.core.helper.MathHelper.*
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.relic.*
import java.awt.Rectangle
import java.util.*
import java.util.regex.Pattern
import kotlin.math.*

@Suppress("UNCHECKED_CAST")
class EntityFlugel(world: World): EntityCreature(world), IBotaniaBoss { // EntityDoppleganger
	
	val playersDamage: HashMap<String, Float> = HashMap()
	
	var maxHit = 1f
	var hurtTimeActual: Int = 0
	
	val playersAround: List<EntityPlayer>
		get() {
			val source = source
			val range = RANGE + 3
			return worldObj.getEntitiesWithinAABB(EntityPlayer::class.java, AxisAlignedBB.getBoundingBox(source.posX + 0.5 - range, source.posY + 0.5 - range, source.posZ + 0.5 - range, source.posX.toDouble() + 0.5 + range.toDouble(), source.posY.toDouble() + 0.5 + range.toDouble(), source.posZ.toDouble() + 0.5 + range.toDouble())) as List<EntityPlayer>
		}
	
	var stage: Int
		get() = dataWatcher.getWatchableObjectByte(21).toInt()
		set(stage) {
			dataWatcher.updateObject(21, stage.toByte())
			
			regens = when (stage) {
				1    -> 3
				2    -> 5
				3    -> 8
				else -> 0
			}
		}
	
	var isHardMode: Boolean
		get() = dataWatcher.getWatchableObjectByte(22) > 0
		set(hard) = dataWatcher.updateObject(22, if (hard) 1.toByte() else 0.toByte())
	
	var isUltraMode: Boolean
		get() = dataWatcher.getWatchableObjectByte(22) > 1
		set(ultra) = dataWatcher.updateObject(22, if (ultra) 2.toByte() else 0.toByte())
	
	val source: ChunkCoordinates
		get() = dataWatcher.getWatchedObject(23).getObject() as ChunkCoordinates
	
	var playerCount: Int
		get() = dataWatcher.getWatchableObjectInt(24)
		set(count) = dataWatcher.updateObject(24, count)
	
	var aiTaskTimer: Int
		get() = dataWatcher.getWatchableObjectInt(25)
		set(time) = dataWatcher.updateObject(25, time)
	
	var summoner: String
		get() = dataWatcher.getWatchableObjectString(26)
		set(summoner) = dataWatcher.updateObject(26, summoner)
	
	var regens: Int
		get() = dataWatcher.getWatchableObjectInt(28)
		set(regens) = dataWatcher.updateObject(28, regens)
	
	var aiTask: AITask
		get() = AITask.values()[dataWatcher.getWatchableObjectInt(27)]
		set(ai) {
			if (ModInfo.DEV) for (player in playersAround) ASJUtilities.say(player, "Set AI command to $ai")
			dataWatcher.updateObject(27, ai.ordinal)
		}
	
	// --------------------------------------------------------
	
	val isAggroed: Boolean
		get() = dataWatcher.getWatchableObjectByte(21) > 0
	
	val isAlive: Boolean
		get() = health > 0 && worldObj.difficultySetting != EnumDifficulty.PEACEFUL && !worldObj.isRemote && ASJUtilities.isServer
	
	val isDying: Boolean
		get() = aiTask != AITask.INVUL && health / maxHealth <= 0.125
	
	val isCasting: Boolean
		get() = aiTask.instant
	
	init {
		setSize(0.6f, 1.8f)
		navigator.setCanSwim(true)
		initAI()
		isImmuneToFire = true
		experienceValue = 1325
		hurtTimeActual = 0
	}
	
	override fun attackEntityFrom(source: DamageSource, damage: Float): Boolean {
		val e = source.entity ?: return false
		if ((source.damageType == "player" || source is DamageSourceSpell) && isTruePlayer(e) && !isEntityInvulnerable) {
			val player = e as EntityPlayer
			
			val crit = player.fallDistance > 0f && !player.onGround && !player.isOnLadder && !player.isInWater && !player.isPotionActive(Potion.blindness) && player.ridingEntity == null
			
			maxHit = if (player.capabilities.isCreativeMode) Float.MAX_VALUE else if (crit) 60f else 40f
			var dmg = min(maxHit, damage) * if (isUltraMode) 0.3f else if (isHardMode) 0.6f else 1f
			
			playersDamage[player.commandSenderName] = playersDamage.getOrDefault(player.commandSenderName, 0f) + dmg
			
			if (aiTask == AITask.REGEN || aiTask == AITask.TP) {
				dmg /= 2f
				if (aiTask == AITask.REGEN) {
					aiTaskTimer = 0
					e.attackEntityFrom(source, dmg / 2f)
				}
			}
			
			reUpdate()
			return super.attackEntityFrom(source, dmg)
		}
		return false
	}
	
	override fun damageEntity(source: DamageSource, damage: Float) {
		super.damageEntity(source, damage)
		
		if (source.entity != null && health > 0) {
			val motionVector = Vector3.fromEntityCenter(this).sub(Vector3.fromEntityCenter(source.entity)).normalize().mul(0.25)
			
			motionX = motionVector.x
			motionY = motionVector.y
			motionZ = motionVector.z
		}
	}
	
	override fun setHealth(set: Float) {
		var hp = set
		prevHealth = health
		hp = max(prevHealth - maxHit * if (isUltraMode) 0.1f else if (isHardMode) 0.6f else 1f, hp)
		
		if (aiTask != AITask.INVUL && hp < prevHealth) if (hurtTimeActual > 0) return
		
		hurtTimeActual = 20
		super.setHealth(hp)
		
		if (aiTask == AITask.INVUL) return
		if (health < prevHealth && (health / (maxHealth / 10)).toInt() < (prevHealth / (maxHealth / 10)).toInt()) {
			if (!aiTask.instant && aiTask != AITask.DEATHRAY)
				aiTaskTimer = 0
		}
	}
	
	override fun onDeath(source: DamageSource) {
		if (isAlive) {
			//ASJUtilities.sayToAllOPs(EnumChatFormatting.DARK_RED + "Alive onDeath. Check console.");
			ASJUtilities.warn("Alive onDeath")
			ASJUtilities.printStackTrace()
			return
		}
		
		super.onDeath(source)
		
		worldObj.playSoundAtEntity(this, "random.explode", 20f, (1f + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2f) * 0.7f)
		worldObj.spawnParticle("hugeexplosion", posX, posY, posZ, 1.0, 0.0, 0.0)
		
		if (isHardMode) {
			for (player in playersAround) player.triggerAchievement(AlfheimAchievements.flugelKill)
			
			if (worldObj.isRemote) return
			
			if (worldObj.rand.nextInt(5) != 0) return
			
			val x = posX.mfloor()
			val y = posY.mfloor()
			val z = posZ.mfloor()
			
			while (worldObj.setBlock(x, y, z, AlfheimBlocks.anomaly)) {
				(worldObj.getTileEntity(x, y, z) as? TileAnomaly ?: break).addSubTile(SubTileEntity.forName("Lightning")
																					  ?: break, "Lightning")
				return
			}
			
			worldObj.setBlockToAir(x, y, z)
		}
	}
	
	public override fun dropFewItems(byPlayer: Boolean, looting: Int) {
		if (isAlive) {
			// ASJUtilities.sayToAllOPs(EnumChatFormatting.DARK_RED + "Alive dropFewItems. Check console.");
			ASJUtilities.warn("Alive dropFewItems")
			ASJUtilities.printStackTrace()
			return
		}
		if (worldObj.isRemote) return
		if (byPlayer) {
			val hard = isHardMode
			val ultra = isUltraMode
			var lot = true
			// For everyone
			for (name in playersDamage.keys) {
				if (worldObj.getPlayerEntityByName(name) == null) continue
				var droppedRecord = false
				
				if (hard) {
					if (ConfigHandler.relicsEnabled && name == summoner) {
						var relic = ItemStack(AlfheimItems.elvenResource, ASJUtilities.randInBounds(rand, 4, 6) / if (ultra) 1 else 2, ElvenResourcesMetas.IffesalDust)
						
						if (!ultra && (worldObj.getPlayerEntityByName(name) as? EntityPlayerMP)?.func_147099_x()?.hasAchievementUnlocked(AlfheimAchievements.mask) == false) {
							relic = ItemStack(AlfheimItems.mask)
						} else if (ultra) {
							val player = (worldObj.getPlayerEntityByName(name) as? EntityPlayerMP)
							val stat = player?.func_147099_x()
							relic = when {
								stat?.hasAchievementUnlocked(AlfheimAchievements.excaliber) == false    -> ItemStack(AlfheimItems.excaliber)		.also { player.triggerAchievement(AlfheimAchievements.excaliber) }
								stat?.hasAchievementUnlocked(AlfheimAchievements.subspace) == false     -> ItemStack(AlfheimItems.subspaceSpear)	.also { player.triggerAchievement(AlfheimAchievements.subspace) }
								stat?.hasAchievementUnlocked(AlfheimAchievements.moonlightBow) == false -> ItemStack(AlfheimItems.moonlightBow)		.also { player.triggerAchievement(AlfheimAchievements.moonlightBow) }
								else                                                                    -> relic
							}
						}
						
						worldObj.getPlayerEntityByName(name).addStat(AlfheimAchievements.mask, 1)
						ItemRelic.bindToUsernameS(name, relic)
						entityDropItem(relic, 1f)
						lot = false
					}
					
					entityDropItem(ItemStack(ModItems.ancientWill, 1, rand.nextInt(6)), 1f)
					val count = if (lot) {
						if (ultra) 10 else if (hard) 6 else 3
					} else {
						if (ultra) 6 else if (hard) 4 else 2
					}
					entityDropItem(ItemStack(AlfheimItems.elvenResource, count, ElvenResourcesMetas.MuspelheimEssence), 1f)
					entityDropItem(ItemStack(AlfheimItems.elvenResource, count, ElvenResourcesMetas.NiflheimEssence), 1f)
					lot = false
					if (Math.random() < 0.9) entityDropItem(ItemStack(ModItems.manaResource, 16 + rand.nextInt(12)), 1f)     // Manasteel
					if (Math.random() < 0.7) entityDropItem(ItemStack(ModItems.manaResource, 8 + rand.nextInt(6), 1), 1f)    // Manapearl
					if (Math.random() < 0.5) entityDropItem(ItemStack(ModItems.manaResource, 4 + rand.nextInt(3), 2), 1f)    // Manadiamond
					if (Math.random() < 0.25) entityDropItem(ItemStack(ModItems.overgrowthSeed, rand.nextInt(3) + 1), 1f)
					
					if (Math.random() < 0.5) {
						val voidLotus = Math.random() < 0.3f
						entityDropItem(ItemStack(ModItems.blackLotus, if (voidLotus) 1 else rand.nextInt(3) + 1, if (voidLotus) 1 else 0), 1f)
					}
					
					val runes = rand.nextInt(6) + 1
					for (i in 0 until runes) if (Math.random() < 0.3) entityDropItem(ItemStack(ModItems.rune, 2 + rand.nextInt(3), rand.nextInt(16)), 1f)
					if (Math.random() < 0.2) entityDropItem(ItemStack(ModItems.pinkinator), 1f)
					
					if (Math.random() < 0.3) {
						val i = Item.getIdFromItem(Items.record_13)
						val j = Item.getIdFromItem(Items.record_wait)
						val k = i + rand.nextInt(j - i + 1)
						entityDropItem(ItemStack(Item.getItemById(k)), 1f)
						droppedRecord = true
					}
					
					if (!droppedRecord && Math.random() < 0.2) entityDropItem(ItemStack(if (customNameTag == "Hatsune Miku") AlfheimItems.flugelDisc2 else AlfheimItems.flugelDisc), 1f)
				}
			}
			
			if (ConfigHandler.relicsEnabled && !hard) {
				val relic = ItemStack(AlfheimItems.flugelSoul)
				if (worldObj.getPlayerEntityByName(summoner) != null) worldObj.getPlayerEntityByName(summoner).addStat(AlfheimAchievements.flugelSoul, 1)
				ItemRelic.bindToUsernameS(summoner, relic)
				entityDropItem(relic, 1f)
			}
		}
	}
	
	override fun setDead() {
		if (isAlive) {
			// ASJUtilities.sayToAllOPs(EnumChatFormatting.DARK_RED + "Alive setDead. Check console");
			ASJUtilities.warn("Someone tried to force flugel to die. They failed.")
			ASJUtilities.printStackTrace()
			ASJUtilities.warn("If the server'd crashed next tick - report this to mod author, ignore otherwise.")
			AITeleport.tryToTP(this)
			return
		}
		Botania.proxy.playRecordClientSided(worldObj, source.posX, source.posY, source.posZ, null)
		isPlayingMusic = false
		if (worldObj.isRemote) BossBarHandler.setCurrentBoss(null)
		
		super.setDead()
	}
	
	override fun onLivingUpdate() {
		super.onLivingUpdate()
		
		if (ridingEntity != null) {
			if (ridingEntity.riddenByEntity != null)
				ridingEntity.riddenByEntity = null
			ridingEntity = null
		}
		
		if (!worldObj.isRemote && worldObj.difficultySetting == EnumDifficulty.PEACEFUL) setDead()
		
		if (!worldObj.isRemote) {
			val posXInt = MathHelper.floor_double(posX)
			val posYInt = MathHelper.floor_double(posY)
			val posZInt = MathHelper.floor_double(posZ)
			for (i in -RANGE..RANGE)
				for (j in -RANGE..RANGE)
					for (k in -RANGE..RANGE) {
						val xp = posXInt + i
						val yp = posYInt + j
						val zp = posZInt + k
						if (isCheatyBlock(worldObj, xp, yp, zp)) {
							val block = worldObj.getBlock(xp, yp, zp)
							val items = block.getDrops(worldObj, xp, yp, zp, 0, 0)
							for (stack in items) {
								if (ConfigHandler.blockBreakParticles) worldObj.playAuxSFX(2001, xp, yp, zp, Block.getIdFromBlock(block) + (worldObj.getBlockMetadata(xp, yp, zp) shl 12))
								worldObj.spawnEntityInWorld(EntityItem(worldObj, xp + 0.5, yp + 0.5, zp + 0.5, stack))
							}
							worldObj.setBlockToAir(xp, yp, zp)
						}
					}
		}
		
		if (playersDamage.isEmpty()) playersDamage[summoner] = 0.1f
		val source = source
		var players = playersAround
		if (players.isNotEmpty() && worldObj.isRemote && AlfheimConfigHandler.flugelBossBar) BossBarHandler.setCurrentBoss(this)
		if (players.isEmpty() && aiTask != AITask.NONE) dropState()
		
		if (worldObj.isRemote && !isPlayingMusic && !isDead && players.isNotEmpty()) {
			Botania.proxy.playRecordClientSided(worldObj, source.posX, source.posY, source.posZ, (if (customNameTag == "Hatsune Miku") AlfheimItems.flugelDisc2 else AlfheimItems.flugelDisc) as ItemRecord)
			isPlayingMusic = true
		}
		
		if (ticksExisted % 20 == 0) {
			// PARTYKLZ!!!
			val mod = 10
			var pitch = 0
			while (pitch <= 180) {
				run {
					var yaw = 0
					while (yaw < 360) {
						// angle in rads
						val radY = yaw * PI.toFloat() / 180f
						val radP = pitch * PI.toFloat() / 180f
						
						// world coords
						val wX = source.posX + 0.5
						val wY = source.posY + 0.5
						val wZ = source.posZ + 0.5
						
						// local coords
						val x = sin(radP.toDouble()) * cos(radY.toDouble()) * RANGE.toDouble()
						val y = cos(radP.toDouble()) * RANGE
						val z = sin(radP.toDouble()) * sin(radY.toDouble()) * RANGE.toDouble()
						
						// particle source position
						val nrm = Vector3(x, y, z).normalize()
						
						// noraml to pos
						val radp = (pitch + 90f) * PI.toFloat() / 180f
						val kx = sin(radp.toDouble()) * cos(radY.toDouble())
						val ky = cos(radp.toDouble())
						val kz = sin(radp.toDouble()) * sin(radY.toDouble())
						val kos = Vector3(kx, ky, kz).normalize().rotate(Math.toRadians(PI * 2.0 * Math.random()), nrm).mul(0.1)
						
						val motX = kos.x.toFloat()
						val motY = kos.y.toFloat()
						val motZ = kos.z.toFloat()
						
						if (customNameTag == "Hatsune Miku") Botania.proxy.wispFX(worldObj, wX - x, wY - y, wZ - z, 0f, 0.75f, 1f, 1f, motX, motY, motZ)
						else Botania.proxy.wispFX(worldObj, wX - x, wY - y, wZ - z, 0.5f, 0f, 1f, 1f, motX, motY, motZ)
						yaw += mod
					}
				}
				pitch += mod
			}
		}
		
		for (player in players) {
			// No beacon potions allowed!
			(player.activePotionEffects as MutableCollection<PotionEffect>).removeIf {
				it.getDuration() < 200 && it.getIsAmbient() && !Potion.potionTypes[it.getPotionID()].isBadEffect
			}
			
			// remove player
			val baubles = PlayerHandler.getPlayerBaubles(player)
			val tiara = baubles.getStackInSlot(0)
			if (tiara != null && tiara.item == ModItems.flightTiara && tiara.itemDamage == 1)
				ItemNBTHelper.setInt(tiara, TAG_TIME_LEFT, 1200)
			else {
				if (!worldObj.isRemote) {
					ASJUtilities.say(player, "alfheimmisc.notallowed")
					
					fun isTooNear(bed: ChunkCoordinates?) =
						if (bed == null) true
						else pointDistanceSpace(bed.posX.D, bed.posY.D, bed.posZ.D, source.posX.D, source.posY.D, source.posZ.D) <= RANGE + 3
					
					if (isTooNear(player.getBedLocation(player.dimension))) {
						if (isTooNear(player.worldObj.spawnPoint)) {
							val v = Vector3(Math.random() * 100 + RANGE, 0.0, 0.0).rotate(Math.random() * 360, Vector3.oY)
							val newPosY = ASJUtilities.getTopLevel(worldObj, v.x.mfloor(), v.z.mfloor())
							player.setPositionAndUpdate(v.x, newPosY.D, v.z)
						} else {
							val bed = player.worldObj.spawnPoint
							player.setPositionAndUpdate(bed.posX.D, bed.posY.D, bed.posZ.D)
						}
					} else {
						val bed = player.getBedLocation(player.dimension)
						player.setPositionAndUpdate(bed.posX.D, bed.posY.D, bed.posZ.D)
					}
					continue
				}
			}
			
			// Get player back!
			if (pointDistanceSpace(player.posX, player.posY, player.posZ, source.posX + 0.5, source.posY + 0.5, source.posZ + 0.5) >= RANGE) {
				val motion = Vector3(source.posX + 0.5, source.posY + 0.5, source.posZ + 0.5).sub(Vector3.fromEntityCenter(player)).normalize()
				
				player.motionX = motion.x
				player.motionY = motion.y
				player.motionZ = motion.z
			}
		}
		
		if (isDead) return
		
		if (!onGround) motionY += 0.075
		
		val invul = if (isEntityInvulnerable) aiTaskTimer else 0
		
		if (invul > 10) spawnPatyklz(false)
		
		if (invul <= 0) {
			if (pointDistanceSpace(posX, posY, posZ, source.posX.toDouble(), source.posY.toDouble(), source.posZ.toDouble()) > RANGE) teleportTo(source.posX + 0.5, source.posY + 1.6, source.posZ + 0.5)
			if (isAggroed) {
				ASJUtilities.faceEntity(this, worldObj.getPlayerEntityByName(playersDamage.maxBy { it.value }?.key ?: "Notch"), 360f, 360f)
				
				if (aiTask == AITask.NONE) reUpdate()
				if (aiTask != AITask.INVUL && health / maxHealth <= 0.6 && stage < STAGE_MAGIC) stage = STAGE_MAGIC
				if (isDying && stage < STAGE_DEATHRAY && aiTask != AITask.DEATHRAY) {
					aiTask = AITask.DEATHRAY
					aiTaskTimer = 0
				}
			} else {
				val range = 3
				players = worldObj.getEntitiesWithinAABB(EntityPlayer::class.java, AxisAlignedBB.getBoundingBox(posX - range, posY - range, posZ - range, posX + range, posY + range, posZ + range)) as List<EntityPlayer>
				if (players.isNotEmpty()) attackEntityFrom(DamageSource.causePlayerDamage(players[0]), 0f)
			}
		}
		
		hurtTimeActual = max(0, --hurtTimeActual)
	}
	
	// from pylons
	fun spawnPatyklz(deathRay: Boolean) {
		val source = source
		val pos = Vector3.fromEntityCenter(this).sub(0.0, 0.2, 0.0)
		
		val miku = customNameTag == "Hatsune Miku"
		
		for (arr in PYLON_LOCATIONS) {
			val x = arr[0]
			val y = arr[1]
			val z = arr[2]
			
			val pylonPos = Vector3((source.posX + x).toDouble(), (source.posY + y).toDouble(), (source.posZ + z).toDouble())
			var worldTime = ticksExisted.toDouble()
			worldTime /= 5.0
			
			val rad = 0.75f + Math.random().toFloat() * 0.05f
			val xp = pylonPos.x + 0.5 + cos(worldTime) * rad
			val zp = pylonPos.z + 0.5 + sin(worldTime) * rad
			
			val partPos = Vector3(xp, pylonPos.y, zp)
			val mot = pos.copy().sub(partPos).mul(0.04)
			
			var r = (if (deathRay) 0.2f else 0.7f) + Math.random().toFloat() * 0.3f
			var g = Math.random().toFloat() * 0.3f
			var b = (if (deathRay) 0.7f else 0.2f) + Math.random().toFloat() * 0.3f
			
			if (miku) {
				r = Math.random().toFloat() * 0.3f
				g = (if (deathRay) 0.2f else 0.7f) + Math.random().toFloat() * 0.3f
				b = 0.7f + Math.random().toFloat() * 0.3f
			}
			
			Botania.proxy.wispFX(worldObj, partPos.x, partPos.y, partPos.z, r, g, b, 0.25f + Math.random().toFloat() * 0.1f, -0.075f - Math.random().toFloat() * 0.015f)
			Botania.proxy.wispFX(worldObj, partPos.x, partPos.y, partPos.z, r, g, b, 0.4f, mot.x.toFloat(), mot.y.toFloat(), mot.z.toFloat())
		}
	}
	
	/*	================================	AI and Data STUFF	================================	*/
	
	public override fun applyEntityAttributes() {
		super.applyEntityAttributes()
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).baseValue = 0.5
		getEntityAttribute(SharedMonsterAttributes.maxHealth).baseValue = MAX_HP.toDouble()
		getEntityAttribute(SharedMonsterAttributes.knockbackResistance).baseValue = 1.0
	}
	
	public override fun canDespawn() = false
	
	public override fun isAIEnabled() = true
	
	public override fun entityInit() {
		super.entityInit()
		dataWatcher.addObject(21, 0.toByte())						// Stage
		dataWatcher.addObject(22, 0.toByte())						// Hard mode
		dataWatcher.addObject(23, ChunkCoordinates(0, 0, 0))		// Source position
		dataWatcher.addObject(24, 0)								// Player count
		dataWatcher.addObject(25, 0)								// AI task timer
		dataWatcher.addObject(26, "")								// Summoner
		dataWatcher.addObject(27, 0)								// Current AI task
		dataWatcher.addObject(28, 0)								// Regens count
	}
	
	override fun isEntityInvulnerable() = playersAround.isNotEmpty() && aiTask == AITask.INVUL && aiTaskTimer > 0
	
	fun initAI() {
		tasks.taskEntries.clear()
		var i = 0
/*0*/    tasks.addTask(i, EntityAIWatchClosest(this, EntityPlayer::class.java, java.lang.Float.MAX_VALUE))
/*1*/    tasks.addTask(++i, AITeleport(this, AITask.TP))
/*1*/    tasks.addTask(i, AIChase(this, AITask.CHASE))
/*1*/    tasks.addTask(i, AIRegen(this, AITask.REGEN))
/*1*/    tasks.addTask(i, AILightning(this, AITask.LIGHTNING))
/*1*/    tasks.addTask(i, AIRays(this, AITask.RAYS))
/*1*/    tasks.addTask(i, AIEnergy(this, AITask.DARK))
/*1*/    tasks.addTask(i, AIDeathray(this, AITask.DEATHRAY))
/*2*/    tasks.addTask(++i, AIInvul(this, AITask.INVUL))
/*3*/    tasks.addTask(++i, AIWait(this, AITask.NONE))
	}
	
	// --------------------------------------------------------
	
	override fun setCustomNameTag(name: String) {
		if (health == 1f) dataWatcher.updateObject(10, name)
	}
	
	fun setSource(x: Int, y: Int, z: Int) {
		dataWatcher.updateObject(23, ChunkCoordinates(x, y, z))
	}
	
	fun dropState() {
		if (worldObj.isRemote) return
		val source = source
		teleportTo(source.posX + 0.5, source.posY + 1.6, source.posZ + 0.5)
		stage = 0
		health = maxHealth
		aiTask = AITask.NONE
		aiTaskTimer = 0
		playersDamage.clear()
		playersDamage[summoner] = 0.1f
	}
	
	fun reUpdate() {
		if (worldObj.isRemote) return
		if (stage < 0)
			stage = -stage
		else if (stage == 0) stage = STAGE_AGGRO
		if (aiTask == AITask.NONE) {
			aiTaskTimer = 0
			aiTask = nextTask()
		}
	}
	
	fun nextTask(): AITask {
		if (stage < STAGE_AGGRO) return AITask.NONE
		val next = AITask.values()[rand.nextInt(AITask.values().size)]
		if (/*next.instant && getAITask().instant &&*/ aiTask == next) return nextTask()
		if (Math.random() < next.chance) return nextTask()
		return if (stage < next.stage) nextTask() else next
	}
	
	override fun writeEntityToNBT(nbt: NBTTagCompound) {
		super.writeEntityToNBT(nbt)
		if (hasCustomNameTag()) nbt.setString(TAG_NAME, customNameTag)
		
		nbt.setInteger(TAG_STAGE, stage)
		nbt.setBoolean(TAG_HARDMODE, isHardMode)
		nbt.setBoolean(TAG_ULTRAMODE, isUltraMode)
		
		val source = source
		nbt.setInteger(TAG_SOURCE_X, source.posX)
		nbt.setInteger(TAG_SOURCE_Y, source.posY)
		nbt.setInteger(TAG_SOURCE_Z, source.posZ)
		
		nbt.setInteger(TAG_PLAYER_COUNT, playerCount)
		nbt.setInteger(TAG_AI_TASK, aiTask.ordinal)
		nbt.setInteger(TAG_AI_TIMER, aiTaskTimer)
		
		for (ai in tasks.executingTaskEntries)
			if ((ai as EntityAITaskEntry).action is AIBase) {
				val path = ai.action.javaClass.name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
				nbt.setString(TAG_AI, path[path.size - 1])
			}
		
		nbt.setString(TAG_SUMMONER, summoner)
		
		val map = NBTTagCompound()
		for ((key, value) in playersDamage) map.setFloat(key, value)
		nbt.setTag(TAG_ATTACKED, map)
	}
	
	override fun readEntityFromNBT(nbt: NBTTagCompound) {
		super.readEntityFromNBT(nbt)
		if (nbt.hasKey(TAG_PLAYER_COUNT)) dataWatcher.updateObject(10, nbt.getString(TAG_NAME))
		
		stage = nbt.getInteger(TAG_STAGE)
		var f = nbt.getBoolean(TAG_HARDMODE)
		if (f) isHardMode = f
		f = nbt.getBoolean(TAG_ULTRAMODE)
		if (f) isUltraMode = f
		
		val x = nbt.getInteger(TAG_SOURCE_X)
		val y = nbt.getInteger(TAG_SOURCE_Y)
		val z = nbt.getInteger(TAG_SOURCE_Z)
		setSource(x, y, z)
		
		playerCount = if (nbt.hasKey(TAG_PLAYER_COUNT))
			nbt.getInteger(TAG_PLAYER_COUNT)
		else
			1
		
		aiTask = AITask.values()[nbt.getInteger(TAG_AI_TASK)]
		
		//if (ModInfo.DEV) ASJUtilities.log("Scrolling AIs for " + nbt.getString(TAG_AI));
		for (e in tasks.taskEntries) {
			//if (ModInfo.DEV) ASJUtilities.log("At " + ((EntityAITaskEntry) e).action.getClass().getName());
			val path = (e as EntityAITaskEntry).action.javaClass.name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
			if (e.action is AIBase && path[path.size - 1] == nbt.getString(TAG_AI)) {
				tasks.executingTaskEntries.add(e)
			}
		}
		
		aiTaskTimer = nbt.getInteger(TAG_AI_TIMER)
		summoner = nbt.getString(TAG_SUMMONER)
		
		val map = nbt.getCompoundTag(TAG_ATTACKED)
		for (o in map.func_150296_c()) playersDamage[o as String] = map.getFloat(o)
	}
	
	// EntityEnderman code below ============================================================================
	
	fun teleportRandomly(): Boolean {
		val d0 = posX + (rand.nextDouble() - 0.5) * RANGE / 2.0
		val d1 = posY + (rand.nextInt(64) - 32)
		val d2 = posZ + (rand.nextDouble() - 0.5) * RANGE / 2.0
		return teleportTo(d0, d1, d2)
	}
	
	fun teleportTo(par1: Double, par3: Double, par5: Double): Boolean {
		val d3 = posX
		val d4 = posY
		val d5 = posZ
		posX = par1
		posY = par3
		posZ = par5
		var flag = false
		val i = MathHelper.floor_double(posX)
		val j = MathHelper.floor_double(posY)
		val k = MathHelper.floor_double(posZ)
		
		if (worldObj.blockExists(i, j, k)) {
			setPosition(posX, posY, posZ)
			motionZ = 0.0
			motionY = motionZ
			motionX = motionY
			
			if (worldObj.getCollidingBoundingBoxes(this, boundingBox).isEmpty() && !worldObj.isAnyLiquid(boundingBox)) flag = true
			
			// Prevent out of bounds teleporting
			val source = source
			if (pointDistanceSpace(posX, posY, posZ, source.posX.toDouble(), source.posY.toDouble(), source.posZ.toDouble()) > RANGE) flag = false
		}
		
		if (!flag) {
			setPosition(d3, d4, d5)
			return false
		}
		
		val short1: Short = 128
		
		for (l in 0 until short1) {
			val d6 = l / (short1 - 1.0)
			val f = (rand.nextFloat() - 0.5f) * 0.2f
			val f1 = (rand.nextFloat() - 0.5f) * 0.2f
			val f2 = (rand.nextFloat() - 0.5f) * 0.2f
			val d7 = d3 + (posX - d3) * d6 + (rand.nextDouble() - 0.5) * width.toDouble() * 2.0
			val d8 = d4 + (posY - d4) * d6 + rand.nextDouble() * height
			val d9 = d5 + (posZ - d5) * d6 + (rand.nextDouble() - 0.5) * width.toDouble() * 2.0
			worldObj.spawnParticle("portal", d7, d8, d9, f.toDouble(), f1.toDouble(), f2.toDouble())
		}
		
		worldObj.playSoundEffect(d3, d4, d5, "mob.endermen.portal", 1.0f, 1.0f)
		playSound("mob.endermen.portal", 1.0f, 1.0f)
		return true
	}
	
	// EntityFireball code below ============================================================================
	
	fun checkCollision() {
		var vec3 = Vec3.createVectorHelper(posX, posY, posZ)
		var vec31 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ)
		var mop: MovingObjectPosition? = worldObj.rayTraceBlocks(vec3, vec31)
		vec3 = Vec3.createVectorHelper(posX, posY, posZ)
		vec31 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ)
		
		if (mop != null) {
			vec31 = Vec3.createVectorHelper(mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord)
		}
		
		var entity: Entity? = null
		val list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0, 1.0, 1.0))
		var d0 = 0.0
		
		for (o in list) {
			val entity1 = o as Entity
			
			if (entity1.canBeCollidedWith()) {
				val f = 0.3f
				val axisalignedbb = entity1.boundingBox.expand(f.toDouble(), f.toDouble(), f.toDouble())
				val movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31)
				
				if (movingobjectposition1 != null) {
					val d1 = vec3.distanceTo(movingobjectposition1.hitVec)
					
					if (d1 < d0 || d0 == 0.0) {
						entity = entity1
						d0 = d1
					}
				}
			}
		}
		
		if (entity != null) {
			mop = MovingObjectPosition(entity)
		}
		
		if (mop != null) {
			onImpact(mop)
		}
	}
	
	fun onImpact(mop: MovingObjectPosition) {
		when (mop.typeOfHit) {
			MovingObjectPosition.MovingObjectType.BLOCK  -> Unit
			
			MovingObjectPosition.MovingObjectType.ENTITY -> {
				if (worldObj.rand.nextInt(5) == 0)
					mop.entityHit.attackEntityFrom(DamageSourceSpell.shadow(this), if (isUltraMode) 10f else if (isHardMode) 2f else 0.5f)
				else
					mop.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this), if (isUltraMode) 20f else if (isHardMode) 15f else 10f)
			}
			
			else                                         -> Unit
		}
	}
	
	/*	================================	HEALTHBAR STUFF	================================	*/
	
	@SideOnly(Side.CLIENT)
	override fun getBossBarTexture() = BossBarHandler.defaultBossBar!!
	
	@SideOnly(Side.CLIENT)
	override fun getBossBarTextureRect(): Rectangle {
		if (barRect == null)
			barRect = Rectangle(0, 0, 185, 15)
		return barRect!!
	}
	
	@SideOnly(Side.CLIENT)
	override fun getBossBarHPTextureRect(): Rectangle {
		if (hpBarRect == null)
			hpBarRect = Rectangle(0, barRect!!.y + barRect!!.height, 181, 7)
		return hpBarRect!!
	}
	
	@SideOnly(Side.CLIENT)
	override fun bossBarRenderCallback(res: ScaledResolution, x: Int, y: Int) {
		glPushMatrix()
		val px = x + 160
		val py = y + 12
		
		val mc = Minecraft.getMinecraft()
		val stack = ItemStack(Items.skull, 1, 3)
		mc.renderEngine.bindTexture(TextureMap.locationItemsTexture)
		net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting()
		glEnable(GL_RESCALE_NORMAL)
		RenderItem.getInstance().renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, stack, px, py)
		net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting()
		
		val unicode = mc.fontRenderer.unicodeFlag
		mc.fontRenderer.unicodeFlag = true
		mc.fontRenderer.drawStringWithShadow("" + playerCount, px + 15, py + 4, 0xFFFFFF)
		mc.fontRenderer.unicodeFlag = unicode
		glPopMatrix()
	}
	
	companion object {
		
		const val TAG_TIME_LEFT = "timeLeft" // from vazkii.botania.common.item.equipment.bauble.ItemFlightTiara
		
		const val SPAWN_TICKS = 160
		const val DEATHRAY_TICKS = 200
		const val RANGE = 24
		const val MAX_HP = 800f
		
		const val TAG_NAME = "name"
		const val TAG_STAGE = "stage"
		const val TAG_HARDMODE = "hardmode"
		const val TAG_ULTRAMODE = "ultramode"
		const val TAG_SOURCE_X = "sourceX"
		const val TAG_SOURCE_Y = "sourceY"
		const val TAG_SOURCE_Z = "sourceZ"
		const val TAG_PLAYER_COUNT = "playerCount"
		const val TAG_AI_TASK = "task"
		const val TAG_AI = "ai"
		const val TAG_AI_TIMER = "aiTime"
		const val TAG_SUMMONER = "summoner"
		const val TAG_ATTACKED = "attacked"
		
		const val STAGE_AGGRO = 1    //100%	hp
		const val STAGE_MAGIC = 2    //60%	hp
		const val STAGE_DEATHRAY = 3    //12.5%	hp
		var isPlayingMusic = false
		
		fun spawn(player: EntityPlayer, stack: ItemStack, world: World, x: Int, y: Int, z: Int, hard: Boolean, ultra: Boolean): Boolean {
			if (world.getTileEntity(x, y, z) is TileEntityBeacon) {
				if (isTruePlayer(player)) {
					if (world.difficultySetting == EnumDifficulty.PEACEFUL) {
						if (!world.isRemote) ASJUtilities.say(player, "alfheimmisc.peacefulNoob")
						return false
					}
					
					if ((world.getTileEntity(x, y, z) as TileEntityBeacon).levels < 1) {
						if (!world.isRemote) ASJUtilities.say(player, "alfheimmisc.inactive")
						return false
					}
					
					for (coords in PYLON_LOCATIONS) { // TODO change structure
						val i = x + coords[0]
						val j = y + coords[1]
						val k = z + coords[2]
						
						val blockat = world.getBlock(i, j, k)
						val meta = world.getBlockMetadata(i, j, k)
						if (blockat !== ModBlocks.pylon || meta != 2) {
							if (!world.isRemote) ASJUtilities.say(player, "alfheimmisc.needsCatalysts")
							return false
						}
						
					}
					
					if (!ModInfo.DEV) {
						if (!hasProperArena(world, x, y, z)) {
							if (!world.isRemote) ASJUtilities.say(player, "alfheimmisc.badArena")
							return false
						}
					}
					
					var miku = false
					var crds: ChunkCoordinates? = null
					
					if (stack.item === ModItems.flugelEye) {
						crds = (ModItems.flugelEye as ItemFlugelEye).getBinding(stack)
					} else if (stack.item === AlfheimItems.flugelSoul) {
						crds = ItemFlugelSoul.getFirstCoords(stack)
					}
					
					if (crds != null) miku = crds.posX == 39 && crds.posY == 39 && crds.posZ == 39
					
					if (!hard) stack.stackSize--
					if (world.isRemote) return true
					
					val e = EntityFlugel(world)
					e.setPosition(x + 0.5, (y + 3).toDouble(), z + 0.5)
					e.aiTask = AITask.INVUL
					e.aiTaskTimer = 0
					do {
						e.health = 1f
					} while (e.health > 1f)
					e.setSource(x, y, z)
					
					if (hard) e.isHardMode = hard
					if (ultra) e.isUltraMode = ultra
					
					e.summoner = player.commandSenderName
					e.playersDamage[player.commandSenderName] = 0.1f
					
					if (miku) {
						e.alwaysRenderNameTag = true
						e.customNameTag = "Hatsune Miku"
					}
					
					val players = e.playersAround
					val playerCount = players.count { isTruePlayer(it) }
					
					e.playerCount = playerCount
					e.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth).baseValue = (MAX_HP * playerCount * if (hard) 2 else 1).toDouble()
					e.noClip = true
					
					world.playSoundAtEntity(e, "mob.enderdragon.growl", 10f, 0.1f)
					world.spawnEntityInWorld(e)
					return true
				}
				ASJUtilities.say(player, "alfheimmisc.fakeplayer")
				return false
			}
			
			ASJUtilities.say(player, "alfheimmisc.notbeacon")
			return false
		}
		
		/*	================================	UTILITY STUFF	================================	*/
		
		val PYLON_LOCATIONS = arrayOf(intArrayOf(4, 1, 4), intArrayOf(4, 1, -4), intArrayOf(-4, 1, 4), intArrayOf(-4, 1, -4))
		
		val CHEATY_BLOCKS = listOf("OpenBlocks:beartrap", "ThaumicTinkerer:magnet")
		
		fun hasProperArena(world: World, sx: Int, sy: Int, sz: Int): Boolean {
			var proper = true
			Botania.proxy.setWispFXDepthTest(false)
			for (i in -RANGE until RANGE + 1)
				for (j in -RANGE until RANGE + 1)
					for (k in -RANGE until RANGE + 1) {
						if (k == -1 && i > -2 && i < 2 && j > -2 && j < 2 || k == 1 && abs(i) == 4 && abs(j) == 4 || k == 0 && i == 0 && j == 0 || pointDistancePlane(i.toDouble(), j.toDouble(), 0.0, 0.0) > RANGE)
							continue // Ignore pylons, beacon and out of circle
						
						val x = sx + i
						val y = sy + k
						val z = sz + j
						val isAir = world.getBlock(x, y, z).getCollisionBoundingBoxFromPool(world, x, y, z) == null
						if (!isAir) {
							proper = false
							Botania.proxy.wispFX(world, x + 0.5, y + 0.5, z + 0.5, 1f, 0f, 0f, 0.5f, 0f, 10f)
						}
					}
			Botania.proxy.setWispFXDepthTest(true)
			return proper
		}
		
		fun isCheatyBlock(world: World, x: Int, y: Int, z: Int): Boolean {
			val block = world.getBlock(x, y, z)
			val name = Block.blockRegistry.getNameForObject(block)
			return CHEATY_BLOCKS.contains(name)
		}
		
		val FAKE_PLAYER_PATTERN = Pattern.compile("^(?:\\[.*])|(?:ComputerCraft)$")
		
		fun isTruePlayer(e: Entity): Boolean {
			if (e !is EntityPlayer) return false
			
			val name = e.commandSenderName
			return !(e is FakePlayer || FAKE_PLAYER_PATTERN.matcher(name).matches())
		}
		
		@SideOnly(Side.CLIENT)
		var barRect: Rectangle? = null
		@SideOnly(Side.CLIENT)
		var hpBarRect: Rectangle? = null
	}
}