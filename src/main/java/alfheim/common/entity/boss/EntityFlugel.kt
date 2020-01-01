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
import alfheim.common.entity.boss.ai.flugel.constant.*
import alfheim.common.item.AlfheimItems
import alfheim.common.item.material.ElvenResourcesMetas
import alfheim.common.item.relic.ItemFlugelSoul
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.*
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry
import net.minecraft.entity.ai.EntityAIWatchClosest
import net.minecraft.entity.player.*
import net.minecraft.init.Items
import net.minecraft.item.*
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.potion.Potion
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
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.relic.*
import java.awt.Rectangle
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.*

@Suppress("UNCHECKED_CAST")
class EntityFlugel(world: World): EntityCreature(world), IBotaniaBoss { // EntityDoppleganger
	
	val rnd = Random()
	
	val playerDamage: HashMap<String, Float> = HashMap()
	
	var maxHit = 1f
	var hurtTimeActual: Int = 0
	
	val playersAround: List<EntityPlayer>
		get() {
			val source = source
			val range = RANGE + 3
			return worldObj.getEntitiesWithinAABB(EntityPlayer::class.java, AxisAlignedBB.getBoundingBox(source.posX + 0.5 - range, source.posY + 0.5 - range, source.posZ + 0.5 - range, source.posX.D + 0.5 + range.D, source.posY.D + 0.5 + range.D, source.posZ.D + 0.5 + range.D)) as List<EntityPlayer>
		}
	
	var regens: Int
		get() = dataWatcher.getWatchableObjectInt(28)
		set(regens) = dataWatcher.updateObject(28, regens)
	
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
	
	var aiTaskTimer: Int
		get() = dataWatcher.getWatchableObjectInt(25)
		set(time) = dataWatcher.updateObject(25, time)
	
	var aiTask: AITask
		get() = AITask.values()[dataWatcher.getWatchableObjectInt(27)]
		set(ai) {
			dataWatcher.updateObject(27, ai.ordinal)
		}
	
	var isHardMode: Boolean
		get() = dataWatcher.getWatchableObjectByte(22) > 0
		set(hard) = dataWatcher.updateObject(22, if (hard) 1.toByte() else 0.toByte())
	
	var isUltraMode: Boolean
		get() = dataWatcher.getWatchableObjectByte(22) > 1
		set(ultra) = dataWatcher.updateObject(22, if (ultra) 2.toByte() else 0.toByte())
	
	var source: ChunkCoordinates
		get() = dataWatcher.getWatchedObject(23).getObject() as ChunkCoordinates
		set(source) = dataWatcher.updateObject(23, source)
	
	var playerCount: Int
		get() = dataWatcher.getWatchableObjectInt(24)
		set(count) = dataWatcher.updateObject(24, count)
	
	var summoner: String
		get() = dataWatcher.getWatchableObjectString(26)
		set(summoner) = dataWatcher.updateObject(26, summoner)
	
	// --------------------------------------------------------
	
	val isAggroed: Boolean
		get() = dataWatcher.getWatchableObjectByte(21) > 0
	
	val isAlive: Boolean
		get() = health > 0 && worldObj.difficultySetting != EnumDifficulty.PEACEFUL && !worldObj.isRemote && ASJUtilities.isServer
	
	val isDying: Boolean
		get() = aiTask != AITask.INVUL && health / maxHealth <= 0.125
	
	val isCasting: Boolean
		get() = aiTask.instant
	
	val AI = AIController()
	
	init {
		setSize(0.6f, 1.8f)
		navigator.setCanSwim(true)
		initAI()
		isImmuneToFire = true
		experienceValue = 1325
		hurtTimeActual = 0
	}
	
	override fun onLivingUpdate() {
		super.onLivingUpdate()
		
		if (ridingEntity != null) {
			if (ridingEntity.riddenByEntity != null)
				ridingEntity.riddenByEntity = null
			ridingEntity = null
		}
		
		if (!worldObj.isRemote && worldObj.difficultySetting == EnumDifficulty.PEACEFUL) setDead()
		
		// target
		if (playerDamage.isEmpty()) playerDamage[summoner] = 0.1f
		
		val source = source
		var players = playersAround
		
		if (players.isNotEmpty() && worldObj.isRemote && AlfheimConfigHandler.flugelBossBar) BossBarHandler.setCurrentBoss(this)
		
		if (worldObj.isRemote && !isPlayingMusic && !isDead && players.isNotEmpty()) {
			Botania.proxy.playRecordClientSided(worldObj, source.posX, source.posY, source.posZ, (if (customNameTag == "Hatsune Miku") AlfheimItems.flugelDisc2 else AlfheimItems.flugelDisc) as ItemRecord)
			isPlayingMusic = true
		}
		
		spherePartickles()
		
		AI.updateAI()
		
		if (isDead) return
		
		if (!onGround) {
			motionY += 0.075
		}
		
		val invul = if (isEntityInvulnerable) aiTaskTimer else 0
		
		if (invul > 10) pylonPartickles(false)
		
		if (invul <= 0) {
			
			if (isAggroed) {
				ASJUtilities.faceEntity(this, worldObj.getPlayerEntityByName(playerDamage.maxBy { it.value }?.key ?: "Notch"), 360f, 360f)
				
				if (aiTask != AITask.INVUL && health / maxHealth <= 0.6 && stage < STAGE_MAGIC) stage = STAGE_MAGIC
			} else {
				val range = 3
				players = worldObj.getEntitiesWithinAABB(EntityPlayer::class.java, AxisAlignedBB.getBoundingBox(posX - range, posY - range, posZ - range, posX + range, posY + range, posZ + range)) as List<EntityPlayer>
				if (players.isNotEmpty()) attackEntityFrom(DamageSource.causePlayerDamage(players[0]), 0f)
			}
		}
		
		hurtTimeActual = max(0, --hurtTimeActual)
	}
	
	/*	================================	HEALTH DEATH STUFF	================================	*/
	
	override fun attackEntityFrom(source: DamageSource, damage: Float): Boolean {
		val e = source.entity ?: return false
		if ((source.damageType == "player" || source is DamageSourceSpell) && isTruePlayer(e) && !isEntityInvulnerable) {
			val player = e as EntityPlayer
			
			val crit = player.fallDistance > 0f && !player.onGround && !player.isOnLadder && !player.isInWater && !player.isPotionActive(Potion.blindness) && player.ridingEntity == null
			
			maxHit = if (player.capabilities.isCreativeMode) Float.MAX_VALUE else if (crit) 60f else 40f
			var dmg = min(maxHit, damage) * if (isUltraMode) MAX_DMG_UL else if (isHardMode) MAX_DMG_HD else MAX_DMG_EZ
			
			playerDamage[player.commandSenderName] = playerDamage[player.commandSenderName]?.plus(damage) ?: damage
			
			if (aiTask == AITask.TP) dmg /= 2f
			if (aiTask == AITask.REGEN) {
				dmg /= 2f
				aiTaskTimer = 0
			}
			
			AI.reUpdate()
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
		hp = max(prevHealth - maxHit * if (isUltraMode) MAX_DMG_UL else if (isHardMode) MAX_DMG_HD else MAX_DMG_EZ, hp)
		
		if (aiTask != AITask.INVUL && hp < prevHealth) if (hurtTimeActual > 0) return
		
		hurtTimeActual = 20
		super.setHealth(hp)
		
		if (aiTask == AITask.INVUL) return
		
		// every -10% hp forcely change AI task
		if (health < prevHealth && (health / (maxHealth / 10)).toInt() < (prevHealth / (maxHealth / 10)).toInt()) {
			if (!aiTask.instant && aiTask != AITask.DEATHRAY) {
				if (ModInfo.DEV) for (player in playersAround) ASJUtilities.say(player, "x10% AI change")
				aiTaskTimer = 0
			}
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
				(worldObj.getTileEntity(x, y, z) as? TileAnomaly ?: break).addSubTile(SubTileEntity.forName("Lightning") ?: break, "Lightning")
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
			for (name in playerDamage.keys) {
				if (worldObj.getPlayerEntityByName(name) == null) continue
				var droppedRecord = false
				
				if (hard) {
					if (ConfigHandler.relicsEnabled && name == summoner) {
						var relic = ItemStack(AlfheimItems.elvenResource, ASJUtilities.randInBounds(rnd, 4, 6) / if (ultra) 1 else 2, ElvenResourcesMetas.IffesalDust)
						
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
					
					entityDropItem(ItemStack(ModItems.ancientWill, 1, rnd.nextInt(6)), 1f)
					val count = if (lot) {
						if (ultra) 10 else if (hard) 6 else 3
					} else {
						if (ultra) 6 else if (hard) 4 else 2
					}
					entityDropItem(ItemStack(AlfheimItems.elvenResource, count, ElvenResourcesMetas.MuspelheimEssence), 1f)
					entityDropItem(ItemStack(AlfheimItems.elvenResource, count, ElvenResourcesMetas.NiflheimEssence), 1f)
					lot = false
					if (Math.random() < 0.9) entityDropItem(ItemStack(ModItems.manaResource, 16 + rnd.nextInt(12)), 1f)     // Manasteel
					if (Math.random() < 0.7) entityDropItem(ItemStack(ModItems.manaResource, 8 + rnd.nextInt(6), 1), 1f)    // Manapearl
					if (Math.random() < 0.5) entityDropItem(ItemStack(ModItems.manaResource, 4 + rnd.nextInt(3), 2), 1f)    // Manadiamond
					if (Math.random() < 0.25) entityDropItem(ItemStack(ModItems.overgrowthSeed, rnd.nextInt(3) + 1), 1f)
					
					if (Math.random() < 0.5) {
						val voidLotus = Math.random() < 0.3f
						entityDropItem(ItemStack(ModItems.blackLotus, if (voidLotus) 1 else rnd.nextInt(3) + 1, if (voidLotus) 1 else 0), 1f)
					}
					
					val runes = rnd.nextInt(6) + 1
					for (i in 0 until runes) if (Math.random() < 0.3) entityDropItem(ItemStack(ModItems.rune, 2 + rnd.nextInt(3), rnd.nextInt(16)), 1f)
					if (Math.random() < 0.2) entityDropItem(ItemStack(ModItems.pinkinator), 1f)
					
					if (Math.random() < 0.3) {
						val i = Item.getIdFromItem(Items.record_13)
						val j = Item.getIdFromItem(Items.record_wait)
						val k = i + rnd.nextInt(j - i + 1)
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
	
	/*	================================	RENDER STUFF		================================	*/
	
	fun spherePartickles() {
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
						val x = sin(radP.D) * cos(radY.D) * RANGE.D
						val y = cos(radP.D) * RANGE
						val z = sin(radP.D) * sin(radY.D) * RANGE.D
						
						// particle source position
						val nrm = Vector3(x, y, z).normalize()
						
						// noraml to pos
						val radp = (pitch + 90f) * PI.toFloat() / 180f
						val kx = sin(radp.D) * cos(radY.D)
						val ky = cos(radp.D)
						val kz = sin(radp.D) * sin(radY.D)
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
	}
	
	fun pylonPartickles(deathRay: Boolean) {
		val source = source
		val pos = Vector3.fromEntityCenter(this).sub(0.0, 0.2, 0.0)
		
		val miku = customNameTag == "Hatsune Miku"
		
		for (arr in PYLON_LOCATIONS) {
			val x = arr[0]
			val y = arr[1]
			val z = arr[2]
			
			val pylonPos = Vector3((source.posX + x).D, (source.posY + y).D, (source.posZ + z).D)
			var worldTime = ticksExisted.D
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
		getEntityAttribute(SharedMonsterAttributes.maxHealth).baseValue = MAX_HP.D
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
		tasks.addTask(0, EntityAIWatchClosest(this, EntityPlayer::class.java, java.lang.Float.MAX_VALUE))
	}
	
	// --------------------------------------------------------
	
	override fun setCustomNameTag(name: String) {
		if (health == 1f) dataWatcher.updateObject(10, name)
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
		for ((key, value) in playerDamage) map.setFloat(key, value)
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
		source = ChunkCoordinates(x, y, z)
		
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
		for (o in map.func_150296_c()) playerDamage[o as String] = map.getFloat(o)
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
		
		const val MAX_DMG_EZ = 1f
		const val MAX_DMG_HD = 0.6f
		const val MAX_DMG_UL = 0.3f
		
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
					e.setPosition(x + 0.5, (y + 3).D, z + 0.5)
					
					do {
						e.health = 1f
					} while (e.health > 1f)
					e.source = ChunkCoordinates(x, y, z)
					
					if (hard) e.isHardMode = hard
					if (ultra) e.isUltraMode = ultra
					
					e.summoner = player.commandSenderName
					e.playerDamage[player.commandSenderName] = 0.1f
					
					if (miku) {
						e.alwaysRenderNameTag = true
						e.customNameTag = "Hatsune Miku"
					}
					
					val players = e.playersAround
					val playerCount = players.count { isTruePlayer(it) }
					
					e.playerCount = playerCount
					e.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth).baseValue = (MAX_HP * playerCount * if (hard) 2 else 1).D
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
						if (k == -1 && i > -2 && i < 2 && j > -2 && j < 2 || k == 1 && abs(i) == 4 && abs(j) == 4 || k == 0 && i == 0 && j == 0 || Vector3.pointDistancePlane(i.D, j.D, 0.0, 0.0) > RANGE)
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
		
		val FAKE_PLAYER_PATTERN = "^(?:\\[.*])|(?:ComputerCraft)$".toRegex()
		
		fun isTruePlayer(e: Entity): Boolean {
			if (e !is EntityPlayer) return false
			
			val name = e.commandSenderName
			return !(e is FakePlayer || name.matches(FAKE_PLAYER_PATTERN))
		}
		
		@SideOnly(Side.CLIENT)
		var barRect: Rectangle? = null
		@SideOnly(Side.CLIENT)
		var hpBarRect: Rectangle? = null
	}
	
	inner class AIController {
		
		val constantTasks = ArrayList<AIConstantExecutable>()
		val temporalTasks = HashMap<Int, ArrayList<AIBase>>()
		val priorities: List<Int>
		
		var ai: AIBase
		
		init {
			var i = 0
			
			constantTasks.add(AIClearPotions(this@EntityFlugel))
			constantTasks.add(AIDestroyCheatBlocks(this@EntityFlugel))
			constantTasks.add(AIGetBack(this@EntityFlugel))
			constantTasks.add(AIRequireWings(this@EntityFlugel))
			
			temporalTasks.addTask(++i, AITeleport(this@EntityFlugel, AITask.TP))
			temporalTasks.addTask(i, AIChase(this@EntityFlugel, AITask.CHASE))
			temporalTasks.addTask(i, AIRegen(this@EntityFlugel, AITask.REGEN))
			temporalTasks.addTask(i, AILightning(this@EntityFlugel, AITask.LIGHTNING))
			temporalTasks.addTask(i, AIRays(this@EntityFlugel, AITask.RAYS))
			temporalTasks.addTask(i, AIEnergy(this@EntityFlugel, AITask.DARK))
			temporalTasks.addTask(i, AIDeathray(this@EntityFlugel, AITask.DEATHRAY))
			
			val invul = AIInvul(this@EntityFlugel, AITask.INVUL)
			temporalTasks.addTask(++i, invul)
			temporalTasks.addTask(++i, AIWait(this@EntityFlugel, AITask.NONE))
			
			ai = invul
			aiTask = AITask.INVUL
			ai.startExecuting()
			
			priorities = temporalTasks.keys.sortedDescending()
		}
		
		fun HashMap<Int, ArrayList<AIBase>>.addTask(i: Int, ai: AIBase) {
			(this[i] ?: ArrayList<AIBase>().also { this[i] = it } ).add(ai)
		}
		
		fun dropState() {
			if (worldObj.isRemote) return
			val source = source
			AITeleport.teleportTo(this@EntityFlugel, source.posX + 0.5, source.posY + 1.6, source.posZ + 0.5)
			stage = 0
			health = maxHealth
			aiTask = AITask.NONE
			aiTaskTimer = 0
			ai = temporalTasks[temporalTasks.keys.max()]!!.first { it.task == AITask.NONE}
			playerDamage.clear()
			playerDamage[summoner] = 0.1f
		}
		
		/**
		 * updates Flugels AI each time it gots hit
		 */
		fun reUpdate() {
			if (worldObj.isRemote) return
			if (stage < 0)
				stage = -stage
			else if (stage == 0) stage = STAGE_AGGRO
			
			newTask()
		}
		
		fun updateAI() {
			constantTasks.forEach{ it.execute() }
			
			if (ai.shouldContinue() && --aiTaskTimer > 0)
				ai.continueExecuting()
			else
				newTask()
		}
		
		fun newTask() {
			if (!ai.isInterruptible() && ai.shouldContinue()) return
			ai.endTask()
			
			while (true) {
				priorities.forEach { priority ->
					val task = temporalTasks[priority]!!.random()
					
					if (Math.random() < task.task.chance && !(task.task.instant && ai.task.instant) && stage >= task.task.stage) {
						
						if (task.shouldExecute()) {
							ai = task
							aiTask = ai.task
							ai.startExecuting()
							if (ModInfo.DEV) for (player in playersAround) ASJUtilities.chatLog("Set AI command to ${ai.task}", player)
							return
						}
					}
				}
			}
		}
	}
}