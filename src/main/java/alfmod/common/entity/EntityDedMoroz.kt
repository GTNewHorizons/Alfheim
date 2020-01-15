package alfmod.common.entity

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.mfloor
import alfheim.common.network.MessageEffect
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
import kotlin.math.*

class EntityDedMoroz(world: World): EntityMob(world) {

	init {
		setSize(1.8F, 5.4F)
		
		tasks.addTask(4, EntityAIAttackOnCollide(this, EntityLiving::class.java, 1.2, false))
		tasks.addTask(5, EntityAIWander(this, 1.0))
		tasks.addTask(6, EntityAIWatchClosest(this, EntityLiving::class.java, 8.0f))
		tasks.addTask(6, EntityAILookIdle(this))
		
		targetTasks.addTask(1, EntityAIHurtByTarget(this, false))
		targetTasks.addTask(2, EntityAINearestAttackableTarget(this, EntityLiving::class.java, 0, true))
		
		addRandomArmor()
	}
	
	override fun applyEntityAttributes() {
		super.applyEntityAttributes()
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).baseValue = 0.35
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
	
	override fun dropFewItems(gotHit: Boolean, looting: Int) {
		val item = dropItem
		entityDropItem(ItemStack(item, 1 + if (item === Items.snowball) looting else 0, 1 + if (item.isDamageable) min(0f, item.maxDamage / (looting + 1f)).toInt() else 0), 0f)
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
		super.onLivingUpdate()
		
		if (attackTarget != null && ticksExisted % ASJUtilities.randInBounds(rand, 40, 50) == 0 && Vector3.entityDistance(this, attackTarget).toInt() in 8..16) {
			lookHelper.setLookPositionWithEntity(attackTarget, 30f, 30f)
			
			// TODO throw snowball
		}
	}
	
	override fun setAttackTarget(target: EntityLivingBase?) {
		if (target != null && target != attackTarget) {
			ASJUtilities.faceEntity(target, this, 360f, 360f)
			
			target.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDEternity, 100, 1))
			if (!worldObj.isRemote) AlfheimCore.network.sendToAll(MessageEffect(target.entityId, AlfheimConfigHandler.potionIDEternity, 100, 1))
		}
		
		super.setAttackTarget(target)
	}
	
	override fun getYOffset() = super.getYOffset() - 0.5
}