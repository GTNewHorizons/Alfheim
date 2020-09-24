package alfheim.common.entity.boss

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.api.ModInfo
import alfheim.api.boss.IBotaniaBossWithName
import alfheim.client.render.world.VisualEffectHandlerClient
import alfheim.common.block.tile.*
import alfheim.common.core.handler.VisualEffectHandler
import alfheim.common.item.AlfheimItems
import alfheim.common.item.material.ElvenResourcesMetas
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.entity.*
import net.minecraft.entity.ai.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.potion.Potion
import net.minecraft.tileentity.TileEntityBeacon
import net.minecraft.util.*
import net.minecraft.world.*
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import vazkii.botania.client.core.handler.BossBarHandler
import vazkii.botania.common.item.ModItems
import java.awt.*
import kotlin.math.*

class EntityFenrir(world: World): EntityCreature(world), IBotaniaBossWithName {
	
	/** true is the wolf is wet else false */
	private var isDripping = false
	
	/** True if the wolf is shaking else False */
	private var isShaking = false
	
	/** This time increases while wolf is shaking and emitting water particles.  */
	private var timeWolfIsShaking = 0f
	private var prevTimeWolfIsShaking = 0f
	
	init {
		setSize(6f, 8f)
		navigator.avoidsWater = true
		tasks.addTask(1, EntityAISwimming(this))
		tasks.addTask(3, EntityAILeapAtTarget(this, 0.4f))
		tasks.addTask(4, EntityAIAttackOnCollide(this, 1.0, true))
		tasks.addTask(7, EntityAIWander(this, 1.0))
		tasks.addTask(9, EntityAIWatchClosest(this, EntityPlayer::class.java, 8.0f))
		tasks.addTask(9, EntityAILookIdle(this))
		targetTasks.addTask(1, EntityAINearestAttackableTarget(this, EntityPlayer::class.java, 0, false))
		targetTasks.addTask(3, EntityAIHurtByTarget(this, true))
	}
	
	override fun applyEntityAttributes() {
		super.applyEntityAttributes()
		getEntityAttribute(SharedMonsterAttributes.followRange).baseValue = 64.0
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).baseValue = 0.4
		getEntityAttribute(SharedMonsterAttributes.maxHealth).baseValue = 666.0
		stepHeight = 5f
	}
	
	override fun isAIEnabled() = true
	
	override fun canDespawn() = false
	
	override fun func_145780_a(x: Int, y: Int, z: Int, block: Block?) = playSound("mob.wolf.step", soundVolume, soundPitch)
	
	override fun getLivingSound() = "mob.wolf.growl"
	
	override fun getHurtSound() = "mob.wolf.hurt"
	
	override fun getDeathSound() = "mob.wolf.death"
	
	override fun getSoundVolume() = 10f
	
	override fun getDropItem() = AlfheimItems.elvenResource
	
	override fun dropFewItems(gotHit: Boolean, looting: Int) {
		entityDropItem(ItemStack(dropItem, rand.nextInt(looting * 2 + 2) + 3, ElvenResourcesMetas.FenrirFur), 5f)
	}
	
	override fun onLivingUpdate() {
		super.onLivingUpdate()
		if (!worldObj.isRemote && isDripping && !isShaking && !hasPath() && onGround) {
			isShaking = true
			timeWolfIsShaking = 0.0f
			prevTimeWolfIsShaking = 0.0f
			worldObj.setEntityState(this, 8.toByte())
		}
	}
	
	override fun onUpdate() {
		if (worldObj.difficultySetting == EnumDifficulty.PEACEFUL) {
			setDead()
			return
		}
		
		super.onUpdate()
		
		if (isWet) {
			isDripping = true
			isShaking = false
			timeWolfIsShaking = 0.0f
			prevTimeWolfIsShaking = 0.0f
		} else if ((isDripping || isShaking) && isShaking) {
			if (timeWolfIsShaking == 0.0f)
				playSound("mob.wolf.shake", soundVolume, (rand.nextFloat() - rand.nextFloat()) * 0.2f + 1.0f)
			
			prevTimeWolfIsShaking = timeWolfIsShaking
			timeWolfIsShaking += 0.05f
			if (prevTimeWolfIsShaking >= 2.0f) {
				isDripping = false
				isShaking = false
				prevTimeWolfIsShaking = 0.0f
				timeWolfIsShaking = 0.0f
			}
			if (timeWolfIsShaking > 0.4f) {
				val f = boundingBox.minY.toFloat()
				val i = (MathHelper.sin((timeWolfIsShaking - 0.4f) * Math.PI.toFloat()) * 7.0f).toInt()
				for (j in 0 until i) {
					val f1 = (rand.nextFloat() * 2.0f - 1.0f) * width * 0.5f
					val f2 = (rand.nextFloat() * 2.0f - 1.0f) * width * 0.5f
					worldObj.spawnParticle("splash", posX + f1.toDouble(), (f + 0.8f).toDouble(), posZ + f2.toDouble(), motionX, motionY, motionZ)
				}
			}
		}
	}
	
	override fun jump() {
		motionY = 1.0
		if (isPotionActive(Potion.jump)) {
			motionY += (getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1
		}
		if (isSprinting) {
			val f = rotationYaw * 0.017453292f
			motionX -= MathHelper.sin(f) * 0.2
			motionZ += MathHelper.cos(f) * 0.2
		}
		isAirBorne = true
		ForgeHooks.onLivingJump(this)
	}
	
	@SideOnly(Side.CLIENT)
	fun getWolfShaking(): Boolean {
		return isDripping
	}
	
	/**
	 * Used when calculating the amount of shading to apply while the wolf is shaking.
	 */
	@SideOnly(Side.CLIENT)
	fun getShadingWhileShaking(ticks: Float): Float {
		return 0.75f + (prevTimeWolfIsShaking + (timeWolfIsShaking - prevTimeWolfIsShaking) * ticks) / 2.0f * 0.25f
	}
	
	@SideOnly(Side.CLIENT)
	fun getShakeAngle(ticks: Float, phase: Float): Float {
		val f2 = max(0f, min(1f, (prevTimeWolfIsShaking + (timeWolfIsShaking - prevTimeWolfIsShaking) * ticks + phase) / 1.8f))
		return MathHelper.sin(f2 * Math.PI.toFloat()) * MathHelper.sin(f2 * Math.PI.toFloat() * 11.0f) * 0.15f * Math.PI.toFloat()
	}
	
	override fun getEyeHeight(): Float {
		return height * 0.8f
	}
	
	val ignoredDamage = arrayOf("drown", "fall", "inWall", "wither")
	
	override fun attackEntityFrom(source: DamageSource, amount: Float): Boolean {
		if (source.damageType in ignoredDamage || source.isFireDamage) return false
		
		return super.attackEntityFrom(source, amount)
	}
	
	override fun attackEntityAsMob(entity: Entity): Boolean {
		val success = entity.attackEntityFrom(DamageSource.causeMobDamage(this), 10f)
		if (success) heal(2f)
		return success
	}
	
	@SideOnly(Side.CLIENT)
	override fun handleHealthUpdate(value: Byte) {
		if (value.toInt() == 8) {
			isShaking = true
			timeWolfIsShaking = 0.0f
			prevTimeWolfIsShaking = 0.0f
		} else {
			super.handleHealthUpdate(value)
		}
	}
	
	@SideOnly(Side.CLIENT)
	fun getTailRotation(): Float = Math.toRadians(100 + sin(ticksExisted.D / 10) * 5).F
	
	override fun getNameColor() = Color(160, 0, 0).rgb
	
	@SideOnly(Side.CLIENT)
	override fun getBossBarTextureRect(): Rectangle {
		return barRect ?: Rectangle(0, 88, 185, 15).apply { barRect = this }
	}
	
	@SideOnly(Side.CLIENT)
	override fun getBossBarHPTextureRect(): Rectangle {
		return hpBarRect ?: Rectangle(0, 59, 181, 7).apply { hpBarRect = this }
	}
	
	override fun getBossBarTexture() = BossBarHandler.defaultBossBar!!
	
	override fun bossBarRenderCallback(res: ScaledResolution?, x: Int, y: Int) = Unit
	
	companion object {
		
		@SideOnly(Side.CLIENT)
		private var barRect: Rectangle? = null
		
		@SideOnly(Side.CLIENT)
		private var hpBarRect: Rectangle? = null
		
		init {
			object { // sideonly properties requiring sideonly getters/setters so this will be better :(
				@SubscribeEvent
				fun onPlayerInteract(e: PlayerInteractEvent) {
					if (e.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return
					val stack = e.entityPlayer.heldItem ?: return
					if (stack.item !== ModItems.ancientWill || stack.meta != 6) return
					
					e.isCanceled = spawn(e.entityPlayer, stack, e.world, e.x, e.y, e.z)
				}
			}.eventFML()
		}
		
		fun spawn(player: EntityPlayer, stack: ItemStack, world: World, x: Int, y: Int, z: Int): Boolean {
			if (world.getTileEntity(x, y, z) !is TileEntityBeacon)
				return false
			
			if (!EntityFlugel.isTruePlayer(player)) {
				if (!world.isRemote) ASJUtilities.say(player, "alfheimmisc.flugel.fakeplayer")
				return false
			}
			
			if (world.difficultySetting == EnumDifficulty.PEACEFUL) {
				if (!world.isRemote) ASJUtilities.say(player, "alfheimmisc.flugel.peacefulNoob")
				return false
			}
			
			if ((world.getTileEntity(x, y, z) as TileEntityBeacon).levels < 1) {
				if (!world.isRemote) ASJUtilities.say(player, "alfheimmisc.flugel.inactive")
				return false
			}
			
			if (!hasProperArena(world, x, y, z)) {
				if (!world.isRemote) ASJUtilities.say(player, "alfheimmisc.flugel.badArena")
				return false
			}
			
			stack.stackSize--
			if (world.isRemote) return true
			
			val e = EntityFenrir(world)
			e.setPosition(x + 0.5, (y + 3).D, z + 0.5)
			e.playSoundAtEntity("mob.enderdragon.growl", 10f, 0.1f)
			world.spawnEntityInWorld(e)
			return true
		}
		
		val structure = Companion::class.java.getResourceAsStream("/assets/${ModInfo.MODID}/schemas/FenrirRitual").use {
			it.readBytes().toString(Charsets.UTF_8)
		}
		
		val stars = arrayOf(
			arrayOf(
				arrayOf(0, 4),
				arrayOf(2, -3),
				arrayOf(-2, -3),
				arrayOf(0, -4),
				arrayOf(-2, 3),
				arrayOf(2, 3)
			) to 10040115,
			arrayOf(
				arrayOf(6, 0),
				arrayOf(4, -4),
				arrayOf(-4, -4),
				arrayOf(-6, 0),
				arrayOf(-4, 4),
				arrayOf(4, 4)
			) to 1710618
		)
		
		val items = Array(6) { ItemStack(ModItems.ancientWill, 1, it) }
		
		fun hasProperArena(world: World, x: Int, y: Int, z: Int): Boolean {
			val struct = SchemaUtils.checkStructure(world, x, y, z, structure)
			
			for (starData in stars)
				for ((id, s) in starData.first.withIndex()) {
					val (i, k) = s
					
					fun check(starData: Pair<Array<Array<Int>>, Int>): Boolean {
						val star = world.getTileEntity(x + i, y + 1, z + k) as? TileCracklingStar ?: return false
						if (star.color != starData.second) return false
						val (a, c) = starData.first[(id + 1) % starData.first.size]
						return star.pos == Vector3(x + a, y + 1, z + c)
					}
					
					if (!check(starData)) {
						VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.WISP, world.provider.dimensionId, x + i + 0.5, y + 0.5, z + k + 0.5, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 5.0, 0.0)
						return false
					}
				}
			
			for ((id, pos) in stars[1].first.withIndex()) {
				val (i, k) = pos
				
				fun check(): Boolean {
					val display = world.getTileEntity(x + i, y + 1, z + k) as? TileItemDisplay ?: return false
					return ASJUtilities.isItemStackEqualCrafting(ItemStack(ModItems.ancientWill, 1, id), display[0] ?: return false)
				}
				
				if (!check()) {
					VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.WISP, world.provider.dimensionId, x + i + 0.5, y + 0.5, z + k + 0.5, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 5.0, 0.0)
					return false
				}
			}
			
			return struct
		}
	}
}
