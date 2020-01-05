package alfmod.common.entity

import alfheim.common.core.util.mfloor
import alfmod.common.item.AlfheimModularItems
import net.minecraft.entity.*
import net.minecraft.entity.ai.*
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntitySnowball
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.potion.*
import net.minecraft.util.MathHelper
import net.minecraft.world.World
import kotlin.math.atan2

class EntityDedMoroz(world: World): EntityMob(world), IRangedAttackMob {

	init {
		setSize(0.6F, 1.8F)
		
		targetTasks.addTask(1, EntityAIHurtByTarget(this, false))
		targetTasks.addTask(2, EntityAINearestAttackableTarget(this, EntityPlayer::class.java, 0, true))
		tasks.addTask(4, EntityAIArrowAttack(this, 1.0, 20, 60, 15f))
		tasks.addTask(4, EntityAIAttackOnCollide(this, EntityPlayer::class.java, 1.2, false))
		tasks.addTask(5, EntityAIWander(this, 1.0))
		tasks.addTask(6, EntityAIWatchClosest(this, EntityPlayer::class.java, 8.0f))
		tasks.addTask(6, EntityAILookIdle(this))
		
		addRandomArmor()
	}
	
	override fun applyEntityAttributes() {
		super.applyEntityAttributes()
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).baseValue = 0.15
		getEntityAttribute(SharedMonsterAttributes.maxHealth).baseValue = 400.0
	}
	
	override fun isAIEnabled() = true
	
	override fun attackEntityAsMob(target: Entity): Boolean {
		return if (super.attackEntityAsMob(target) && target is EntityLivingBase) {
			target.addPotionEffect(PotionEffect(Potion.moveSlowdown.id, 200))
			true
		} else false
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
	
	override fun addRandomArmor() {
		setCurrentItemOrArmor(0, ItemStack(AlfheimModularItems.snowSword))
		setCurrentItemOrArmor(1, ItemStack(AlfheimModularItems.snowBoots))
		setCurrentItemOrArmor(2, ItemStack(AlfheimModularItems.snowLeggings))
		setCurrentItemOrArmor(3, ItemStack(AlfheimModularItems.snowChest))
		setCurrentItemOrArmor(4, ItemStack(AlfheimModularItems.snowHelmet))
	}
	
	override fun attackEntityWithRangedAttack(target: EntityLivingBase, p_82196_2_: Float) {
		val snowball = EntitySnowball(worldObj, this)
		
		snowball.renderDistanceWeight = 10.0
		snowball.posY = posY + eyeHeight - 0.10000000149011612
		val d0: Double = target.posX - posX
		val d1: Double = target.boundingBox.minY + (target.height / 3.0f) - snowball.posY
		val d2: Double = target.posZ - posZ
		val d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2).toDouble()
		
		if (d3 >= 1.0E-7) {
			val f2 = (atan2(d2, d0) * 180.0 / Math.PI).toFloat() - 90.0f
			val f3 = (-(atan2(d1, d3) * 180.0 / Math.PI)).toFloat()
			val d4 = d0 / d3
			val d5 = d2 / d3
			snowball.setLocationAndAngles(posX + d4, snowball.posY, posZ + d5, f2, f3)
			snowball.yOffset = 0.0f
			val f4 = d3.toFloat() * 0.2f
			snowball.setThrowableHeading(d0, d1 + f4.toDouble(), d2, 1.6f, 14 - worldObj.difficultySetting.difficultyId * 4f)
		}
		
		playSound("snowballpoof", 1f, 1f / (rng.nextFloat() * 0.4f + 0.8f))
		worldObj.spawnEntityInWorld(snowball)
	}
	
	override fun getYOffset() = super.getYOffset() - 0.5
}