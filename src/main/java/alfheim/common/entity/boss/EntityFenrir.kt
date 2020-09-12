package alfheim.common.entity.boss

import alexsocol.asjlib.*
import alfheim.api.boss.IBotaniaBossWithName
import alfheim.common.item.AlfheimItems
import alfheim.common.item.material.ElvenResourcesMetas
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.entity.*
import net.minecraft.entity.ai.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.potion.Potion
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import vazkii.botania.client.core.handler.BossBarHandler
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
		getEntityAttribute(SharedMonsterAttributes.maxHealth).baseValue = 650.0
		stepHeight = 5f
	}
	
	override fun isAIEnabled() = true
	
	override fun func_145780_a(x: Int, y: Int, z: Int, block: Block?) {
		playSound("mob.wolf.step", soundVolume, soundPitch)
	}
	
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
		if (barRect == null)
			barRect = Rectangle(0, 88, 185, 15)
		return barRect!!
	}
	
	@SideOnly(Side.CLIENT)
	override fun getBossBarHPTextureRect(): Rectangle {
		if (hpBarRect == null)
			hpBarRect = Rectangle(0, 59, 181, 7)
		return hpBarRect!!
	}
	
	override fun getBossBarTexture() = BossBarHandler.defaultBossBar!!
	
	override fun bossBarRenderCallback(res: ScaledResolution?, x: Int, y: Int) = Unit
	
	companion object {
		@SideOnly(Side.CLIENT)
		private var barRect: Rectangle? = null
		@SideOnly(Side.CLIENT)
		private var hpBarRect: Rectangle? = null
	}
}
