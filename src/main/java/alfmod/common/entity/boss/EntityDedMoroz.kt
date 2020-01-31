package alfmod.common.entity.boss

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.boss.IBotaniaBossWithName
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.*
import alfheim.common.network.MessageEffect
import alfmod.common.core.handler.WRATH_OF_THE_WINTER
import alfmod.common.entity.EntitySniceBall
import alfmod.common.item.AlfheimModularItems
import cpw.mods.fml.relauncher.*
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.entity.*
import net.minecraft.entity.ai.*
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.potion.*
import net.minecraft.util.DamageSource
import net.minecraft.world.World
import vazkii.botania.client.core.handler.BossBarHandler
import java.awt.Rectangle

private const val FOLLOW = 20.0

class EntityDedMoroz(world: World): EntityMob(world), IBotaniaBossWithName {

	init {
		setSize(1.5f, 4.5f)
		
		tasks.addTask(4, EntityAIAttackOnCollide(this, EntityPlayer::class.java, 1.2, false))
		tasks.addTask(5, EntityAIWander(this, 1.0))
		tasks.addTask(6, EntityAIWatchClosest(this, EntityPlayer::class.java, 8.0f))
		tasks.addTask(6, EntityAILookIdle(this))
		
		targetTasks.addTask(1, EntityAIHurtByTarget(this, false))
		targetTasks.addTask(2, EntityAINearestAttackableTarget(this, EntityPlayer::class.java, 0, true))
		
		addRandomArmor()
		equipmentDropChances.fill(0f)
	}
	
	constructor(world: World, x: Double, y: Double, z: Double): this(world) {
		setPosition(x, y, z)
	}
	
	override fun applyEntityAttributes() {
		super.applyEntityAttributes()
		getEntityAttribute(SharedMonsterAttributes.attackDamage).baseValue = 1.0
		getEntityAttribute(SharedMonsterAttributes.followRange).baseValue = FOLLOW
		getEntityAttribute(SharedMonsterAttributes.knockbackResistance).baseValue = 0.9
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).baseValue = 0.25
		getEntityAttribute(SharedMonsterAttributes.maxHealth).baseValue = 400.0
	}
	
	override fun isAIEnabled() = true
	
	override fun attackEntityAsMob(target: Entity): Boolean {
		return if (super.attackEntityAsMob(target) && target is EntityLivingBase) {
			target.addPotionEffect(PotionEffect(Potion.moveSlowdown.id, 200))
			true
		} else false
	}
	
	override fun damageEntity(src: DamageSource, amount: Float) {
		if (attackTarget == null && src.entity is EntityLivingBase)
			attackTarget = src.entity as EntityLivingBase
		
		super.damageEntity(src, amount * if (src.isMagicDamage || src is DamageSourceSpell) 0.1f else 0.75f)
	}
	
	override fun getDropItem() =
		when ((Math.random() * 6).mfloor()) {
			0 -> AlfheimModularItems.snowSword
			1 -> AlfheimModularItems.snowHelmet
			2 -> AlfheimModularItems.snowChest
			3 -> AlfheimModularItems.snowLeggings
			4 -> AlfheimModularItems.snowBoots
			else -> Items.snowball
		}!!
	
	override fun dropFewItems(gotHit: Boolean, looting: Int) {
		val item = dropItem
		entityDropItem(ItemStack(item, 1 + if (item === Items.snowball) looting else 0, if (item.isDamageable) item.maxDamage / (looting + 1) else 0), 0f)
	}
	
	override fun addRandomArmor() {
		equipmentDropChances.fill(0f)
		
		setCurrentItemOrArmor(0, ItemStack(AlfheimModularItems.snowSword))
		setCurrentItemOrArmor(1, ItemStack(AlfheimModularItems.snowBoots))
		setCurrentItemOrArmor(2, ItemStack(AlfheimModularItems.snowLeggings))
		setCurrentItemOrArmor(3, ItemStack(AlfheimModularItems.snowChest))
		setCurrentItemOrArmor(4, ItemStack(AlfheimModularItems.snowHelmet))
	}
	
	override fun onLivingUpdate() {
		if (!WRATH_OF_THE_WINTER) setDead().also { return }
		
		super.onLivingUpdate()
		
		if (worldObj.isRaining && AlfheimCore.winter)
			heal(if (attackTarget == null) 0.1f else 0.02f)
		
		if (attackTarget != null) {
			(attackTarget as? EntityPlayer)?.let {
				if (it.capabilities.isFlying) {
					it.capabilities.isFlying = false
					ASJUtilities.say(it, "alfmodmisc.ded.icebound")
					it.sendPlayerAbilities()
				}
			}
			
			val dist = Vector3.entityDistance(this, attackTarget)
			
			if (ticksExisted % 40 == 0 && 4 <= dist && dist <= FOLLOW) {
				if (!worldObj.isRemote)
					worldObj.spawnEntityInWorld(EntitySniceBall(worldObj, this))
			}
			
			if (ticksExisted % 600 == 0 && dist <= FOLLOW) {
				val v = Vector3()
				
				val pe = PotionEffect(Potion.moveSlowdown.id, 20, 4)
				attackTarget.addPotionEffect(pe)
				if (!worldObj.isRemote)
					AlfheimCore.network.sendToAll(MessageEffect(attackTarget, pe))
				
				for (i in 0..64) {
					if (!worldObj.isRemote) {
						v.rand().sub(0.5).normalize().mul(Math.random() * 16, Math.random() * 16, Math.random() * 16)
						worldObj.spawnEntityInWorld(EntitySniceBall(worldObj, attackTarget.posX + v.x, attackTarget.posY + v.y + 20, attackTarget.posZ + v.z).also {
							it.thrower = this
							it.setSize(2.0, 2.0)
						})
					}
				}
				
				worldObj.spawnEntityInWorld(EntitySniceBall(worldObj, attackTarget.posX, attackTarget.posY + 8, attackTarget.posZ).also {
					it.thrower = this
					it.setSize(3.0, 3.0)
				})
			}
		}
	}
	
	override fun setAttackTarget(target: EntityLivingBase?) {
		if (target is EntityPlayer && target.capabilities.disableDamage) return
		
		if (target != null && target != attackTarget) {
			if (target.isEntityInvulnerable) return
			
			ASJUtilities.faceEntity(target, this, 360f, 360f)
			
			target.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDEternity, 100, 1))
			if (!worldObj.isRemote)
				AlfheimCore.network.sendToAll(MessageEffect(target.entityId, AlfheimConfigHandler.potionIDEternity, 100, 1))
		}
		
		super.setAttackTarget(target)
	}
	
	override fun getYOffset() = super.getYOffset() - 0.5
	
	override fun canDespawn() = !WRATH_OF_THE_WINTER
	
	@SideOnly(Side.CLIENT)
	override fun getNameColor() = 0x0095FF
	
	@SideOnly(Side.CLIENT)
	override fun getBossBarTexture() = BossBarHandler.defaultBossBar!!
	
	@SideOnly(Side.CLIENT)
	override fun getBossBarTextureRect(): Rectangle {
		if (barRect == null)
			barRect = Rectangle(0, 88, 185, 15)
		return barRect!!
	}
	
	@SideOnly(Side.CLIENT)
	override fun getBossBarHPTextureRect(): Rectangle {
		if (hpBarRect == null)
			hpBarRect = Rectangle(0, 37, 181, 7)
		return hpBarRect!!
	}
	
	@SideOnly(Side.CLIENT)
	override fun bossBarRenderCallback(res: ScaledResolution, x: Int, y: Int) = Unit
	
	@SideOnly(Side.CLIENT)
	var barRect: Rectangle? = null
	
	@SideOnly(Side.CLIENT)
	var hpBarRect: Rectangle? = null
}