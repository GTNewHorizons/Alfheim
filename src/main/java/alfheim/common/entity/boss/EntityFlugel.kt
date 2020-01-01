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
import net.minecraft.entity.player.*
import net.minecraft.init.Items
import net.minecraft.item.*
import net.minecraft.nbt.*
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
class EntityFlugel (world: World): EntityCreature(world), IBotaniaBoss { // EntityDoppleganger
	
	val rnd = Random()
	
	val playerDamage: HashMap<String, Float> = HashMap()
	
	var maxHit = 1f
	var hurtTimeActual: Int = 0
	
	val playersAround: List<EntityPlayer>
		get() {
			val source = source
			return worldObj.getEntitiesWithinAABB(EntityPlayer::class.java, getBoundingBox(source.posX + 0.5, source.posY + 0.5, source.posZ + 0.5).expand(RANGE)).also {
				list -> list.removeAll {
					Vector3.vecEntityDistance(Vector3(source.posX + 0.5, source.posY + 0.5, source.posZ + 0.5), it as Entity) > RANGE
				}
			} as List<EntityPlayer>
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
		get() = dataWatcher.getWatchableObjectString(25)
		set(summoner) = dataWatcher.updateObject(25, summoner)
	
	// --------------------------------------------------------
	
	val isAlive: Boolean
		get() = health > 0 && worldObj.difficultySetting != EnumDifficulty.PEACEFUL && !worldObj.isRemote && ASJUtilities.isServer
	
	val isDying: Boolean
		get() = AI.stage > STAGE.INIT && health / maxHealth <= 0.125
	
	val isCasting: Boolean
		get() = AI.task.instant
	
	val AI: AIController
	
	init {
		setSize(0.6f, 1.8f)
		navigator.setCanSwim(true)
		tasks.taskEntries.clear()
		isImmuneToFire = true
		experienceValue = 1325
		hurtTimeActual = 0
		
		AI = AIController(this)
		AI.newTask(false)
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
		val players = playersAround
		
		if (players.isNotEmpty() && worldObj.isRemote && AlfheimConfigHandler.flugelBossBar) BossBarHandler.setCurrentBoss(this)
		
		if (worldObj.isRemote && !isPlayingMusic && !isDead && players.isNotEmpty()) {
			Botania.proxy.playRecordClientSided(worldObj, source.posX, source.posY, source.posZ, (if (customNameTag == "Hatsune Miku") AlfheimItems.flugelDisc2 else AlfheimItems.flugelDisc) as ItemRecord)
			isPlayingMusic = true
		}
		
		spherePartickles()
		
		if (isDead) return
		
		if (!onGround) {
			motionY += 0.075
		}
		
		hurtTimeActual = max(0, --hurtTimeActual)
		
		AI.updateAI()
	}
	
	/*	================================	HEALTH DEATH STUFF	================================	*/
	
	override fun attackEntityFrom(source: DamageSource, damage: Float): Boolean {
		val e = source.entity ?: return false
		if ((source.damageType == "player" || source is DamageSourceSpell) && isTruePlayer(e) && !isEntityInvulnerable) {
			val player = e as EntityPlayer
			
			val crit = player.fallDistance > 0f && !player.onGround && !player.isOnLadder && !player.isInWater && !player.isPotionActive(Potion.blindness) && player.ridingEntity == null
			
			maxHit = if (player.capabilities.isCreativeMode) Float.MAX_VALUE else if (crit) 60f else 40f
			val dmg = min(maxHit, damage) * if (isUltraMode) MAX_DMG_UL else if (isHardMode) MAX_DMG_HD else MAX_DMG_EZ
			
			playerDamage[player.commandSenderName] = playerDamage[player.commandSenderName]?.plus(damage) ?: damage
			
			AI.stage = STAGE.AGGRO
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
		@Suppress("SENSELESS_COMPARISON")
		// because setHealth gots called before AI initialization in EntityLivingBase constructor
		if (AI == null) return super.setHealth(set)
		
		var hp = set
		prevHealth = health
		hp = max(prevHealth - maxHit * if (isUltraMode) MAX_DMG_UL else if (isHardMode) MAX_DMG_HD else MAX_DMG_EZ, hp)
		
		if (AI.stage > STAGE.INIT && hp < prevHealth) if (hurtTimeActual > 0) return
		
		hurtTimeActual = 20
		super.setHealth(hp)
		
		if (AI.stage == STAGE.INIT) return
		
		// every -10% hp forcely change AI task
		if (health < prevHealth && (health / (maxHealth / 10)).toInt() < (prevHealth / (maxHealth / 10)).toInt()) {
			if (!AI.task.instant && AI.stage != STAGE.DEATHRAY) {
				if (AI.endTask()) {
					AI.newTask()
					
					if (ModInfo.DEV) for (player in playersAround) ASJUtilities.say(player, "x10% AI change")
				}
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
						
						worldObj.getPlayerEntityByName(name)?.addStat(AlfheimAchievements.mask, 1)
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
				worldObj.getPlayerEntityByName(summoner)?.addStat(AlfheimAchievements.flugelSoul, 1)
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
		dataWatcher.addObject(21, 0)								// AI Task
		dataWatcher.addObject(22, 0.toByte())						// Hard mode
		dataWatcher.addObject(23, ChunkCoordinates(0, 0, 0))		// Source position
		dataWatcher.addObject(24, 0)								// Player count
		dataWatcher.addObject(25, "")								// Summoner
	}
	
	override fun isEntityInvulnerable(): Boolean {
		val players = playersAround
		return players.isEmpty() || players.isNotEmpty() && AI.stage == STAGE.INIT
	}
	
	// --------------------------------------------------------
	
	override fun setCustomNameTag(name: String) {
		if (health == 1f) dataWatcher.updateObject(10, name)
	}
	
	override fun writeEntityToNBT(nbt: NBTTagCompound) {
		super.writeEntityToNBT(nbt)
		if (hasCustomNameTag()) nbt.setString(TAG_NAME, customNameTag)
		
		AI.save(nbt)
		
		nbt.setBoolean(TAG_HARDMODE, isHardMode)
		nbt.setBoolean(TAG_ULTRAMODE, isUltraMode)
		
		val source = source
		nbt.setInteger(TAG_SOURCE_X, source.posX)
		nbt.setInteger(TAG_SOURCE_Y, source.posY)
		nbt.setInteger(TAG_SOURCE_Z, source.posZ)
		
		nbt.setInteger(TAG_PLAYER_COUNT, playerCount)
		
		nbt.setString(TAG_SUMMONER, summoner)
		
		val map = NBTTagCompound()
		for ((key, value) in playerDamage) map.setFloat(key, value)
		nbt.setTag(TAG_ATTACKED, map)
	}
	
	override fun readEntityFromNBT(nbt: NBTTagCompound) {
		super.readEntityFromNBT(nbt)
		if (nbt.hasKey(TAG_PLAYER_COUNT)) dataWatcher.updateObject(10, nbt.getString(TAG_NAME))
		
		AI.load(nbt)
		
		var
			flag = nbt.getBoolean(TAG_HARDMODE)
		if (flag) isHardMode = flag
			flag = nbt.getBoolean(TAG_ULTRAMODE)
		if (flag) isUltraMode = flag
		
		val x = nbt.getInteger(TAG_SOURCE_X)
		val y = nbt.getInteger(TAG_SOURCE_Y)
		val z = nbt.getInteger(TAG_SOURCE_Z)
		source = ChunkCoordinates(x, y, z)
		
		playerCount = if (nbt.hasKey(TAG_PLAYER_COUNT))
			nbt.getInteger(TAG_PLAYER_COUNT)
		else
			1
		
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
		const val TAG_DATA_MAP = "dataMap"
		const val TAG_HARDMODE = "hardmode"
		const val TAG_ULTRAMODE = "ultramode"
		const val TAG_SOURCE_X = "sourceX"
		const val TAG_SOURCE_Y = "sourceY"
		const val TAG_SOURCE_Z = "sourceZ"
		const val TAG_PLAYER_COUNT = "playerCount"
		const val TAG_AI_TASK = "task"
		const val TAG_AI_TIMER = "aiTime"
		const val TAG_SUMMONER = "summoner"
		const val TAG_ATTACKED = "attacked"
		
		enum class STAGE {
			INIT, WAIT, AGGRO, MAGIC, DEATHRAY, POSTDEATHRAY;
		}
		
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
	
	inner class AIController(val flugel: EntityFlugel) {
		
		val constantTasks = ArrayList<AIConstantExecutable>()
		
		var task = AITask.INIT
			set(value) {
				dataWatcher.updateObject(21, value.ordinal)
				field = value
			}
		
		var timer = 0
		var stage = STAGE.INIT
		
		var extraData = HashMap<String, Any>()
		
		init {
			constantTasks.add(AIClearPotions(flugel))
			constantTasks.add(AIDestroyCheatBlocks(flugel))
			constantTasks.add(AIGetBack(flugel))
			constantTasks.add(AIRequireWings(flugel))
			constantTasks.add(AIWatchAgroHolder(flugel))
			constantTasks.add(AIChangeStage(flugel))
		}
		
		fun dropState() {
			if (worldObj.isRemote) return
			val source = source
			AITeleport.teleportTo(flugel, source.posX + 0.5, source.posY + 1.6, source.posZ + 0.5)
			health = maxHealth
			flugel.AI.stage = STAGE.WAIT
			playerDamage.clear()
			playerDamage[summoner] = 0.1f
		}
		
		fun updateAI() {
			constantTasks.forEach{ it.execute() }
			
			if (task.ai.shouldContinue(flugel))
				task.ai.continueExecuting(flugel)
			else
				if (endTask())
					newTask()
		}
		
		fun endTask(): Boolean {
			if (!task.ai.isInterruptible(flugel) && task.ai.shouldContinue(flugel)) return false
			task.ai.endTask(flugel)
			return true
		}
		
		fun newTask(checkPrev: Boolean = true) {
			@Suppress("LoopToCallChain")
			for (priority in AITask.priorityList) {
				val task = AITask.prioritySorted[priority]!!.random()
				
				if (Math.random() < task.chance && stage >= task.stage && stage != STAGE.DEATHRAY) {
					if (checkPrev) if (task.instant && this.task.instant) continue
					
					if (task.ai.shouldStart(flugel)) {
						this.task = task
						this.task.ai.startExecuting(flugel)
						
						if (ModInfo.DEV && !worldObj.isRemote) for (player in playersAround) ASJUtilities.chatLog("Set AI command to $task", player)
					}
				}
			}
		}
		
		fun save(nbt: NBTTagCompound) {
			nbt.setInteger(TAG_AI_TASK, task.ordinal)
			nbt.setInteger(TAG_AI_TIMER, timer)
			nbt.setInteger(TAG_STAGE, stage.ordinal)
			
			val data = NBTTagCompound()
			extraData.forEach { (k, v) ->
				when (v) {
					is Byte -> data.setByte(k, v)
					is Short -> data.setShort(k, v)
					is Int -> data.setInteger(k, v)
					is Long -> data.setLong(k, v)
					is Float -> data.setFloat(k, v)
					is Double -> data.setDouble(k, v)
					is ByteArray -> data.setByteArray(k, v)
					is String -> data.setString(k, v)
					is IntArray -> data.setIntArray(k, v)
				}
			}
			nbt.setTag(TAG_DATA_MAP, data)
		}
		
		fun load(nbt: NBTTagCompound) {
			stage = STAGE.values()[nbt.getInteger(TAG_STAGE)]
			timer = nbt.getInteger(TAG_AI_TIMER)
			task = AITask.values()[nbt.getInteger(TAG_AI_TASK)]
			
			val data = nbt.getTag(TAG_DATA_MAP) as NBTTagCompound
			data.tagMap.forEach { (k, v) ->
				when (data.getTag(k as String).id.toInt()) {
					1 -> extraData[k] = (v as NBTTagByte)		.func_150290_f()
					2 -> extraData[k] = (v as NBTTagShort)		.func_150289_e()
					3 -> extraData[k] = (v as NBTTagInt)		.func_150287_d()
					4 -> extraData[k] = (v as NBTTagLong)		.func_150291_c()
					5 -> extraData[k] = (v as NBTTagFloat)		.func_150288_h()
					6 -> extraData[k] = (v as NBTTagDouble)		.func_150286_g()
					7 -> extraData[k] = (v as NBTTagByteArray)	.func_150292_c()
					8 -> extraData[k] = (v as NBTTagString)		.func_150285_a_()
					11-> extraData[k] = (v as NBTTagIntArray)	.func_150302_c()
				}
			}
		}
	}
}