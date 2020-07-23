package alfmod.common.entity

import alexsocol.asjlib.setPosition
import alfheim.common.item.AlfheimItems
import alfheim.common.item.material.ElvenResourcesMetas
import alfmod.common.item.AlfheimModularItems
import net.minecraft.block.Block
import net.minecraft.entity.*
import net.minecraft.entity.ai.*
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityLargeFireball
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.potion.*
import net.minecraft.world.World

class EntityMuspellsun(world: World): EntityMob(world) {
	
	init {
		tasks.addTask(4, EntityAIAttackOnCollide(this, EntityPlayer::class.java, 1.2, false))
		tasks.addTask(4, EntityAIAttackOnCollide(this, EntityFirespirit::class.java, 1.2, false))
		tasks.addTask(5, EntityAIWander(this, 1.0))
		tasks.addTask(6, EntityAIWatchClosest(this, EntityPlayer::class.java, 8f))
		tasks.addTask(6, EntityAILookIdle(this))
		targetTasks.addTask(1, EntityAIHurtByTarget(this, false))
		targetTasks.addTask(2, EntityAINearestAttackableTarget(this, EntityPlayer::class.java, 0, true))
		targetTasks.addTask(2, EntityAINearestAttackableTarget(this, EntityFirespirit::class.java, 0, true))
		isImmuneToFire = true
		addRandomArmor()
		setSize(0.9f, 2.7f)
	}
	
	override fun applyEntityAttributes() {
		super.applyEntityAttributes()
		getEntityAttribute(SharedMonsterAttributes.attackDamage).baseValue = 4.0
		getEntityAttribute(SharedMonsterAttributes.knockbackResistance).baseValue = 0.75
		getEntityAttribute(SharedMonsterAttributes.maxHealth).baseValue = 60.0
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).baseValue = 0.25
	}
	
	override fun onLivingUpdate() {
		super.onLivingUpdate()
		
		if (rand.nextInt(100) != 0) return
		val target = attackTarget ?: entityToAttack as? EntityLivingBase ?: entityLivingToAttack ?: attackingPlayer ?: return
		
		worldObj.spawnEntityInWorld(
			EntityLargeFireball(worldObj, this, 0.0, -1.0, 0.0).apply {
				field_92057_e = rand.nextInt(3) + 2
				setPosition(target, oY = 5.0)
			}
		)
	}
	
	override fun isAIEnabled() = true
	override fun getLivingSound() = "mob.blaze.breathe"
	override fun getHurtSound() = "mob.wither.hurt"
	override fun getDeathSound() = "mob.blaze.death"
	override fun func_145780_a(x: Int, y: Int, z: Int, block: Block) = playSound("mob.irongolem.walk", 1f, 1f)
	
	override fun attackEntityAsMob(entity: Entity): Boolean {
		return if (super.attackEntityAsMob(entity)) {
			if (entity is EntityLivingBase && rand.nextBoolean())
				entity.addPotionEffect(PotionEffect(Potion.wither.id, 200))
			
			entity.setFire(5)
			true
		} else false
	}
	
	override fun updateRidden() {
		super.updateRidden()
		if (ridingEntity is EntityCreature)
			renderYawOffset = (ridingEntity as EntityCreature).renderYawOffset
	}
	
	override fun getDropItem() =
		when (rng.nextInt(32)) {
			in 0..2   -> AlfheimModularItems.volcanoMace
			in 3..5   -> AlfheimModularItems.volcanoHelmet
			in 6..8   -> AlfheimModularItems.volcanoChest
			in 9..11  -> AlfheimModularItems.volcanoLeggings
			in 12..14 -> AlfheimModularItems.volcanoBoots
			in 15..26 -> Items.fire_charge!!
			in 27..31 -> Items.coal // actually nethercoal, changed below
			else      -> AlfheimItems.elvenResource
		}
	
	override fun dropFewItems(gotHit: Boolean, looting: Int) {
		if (!gotHit) return
		
		var item = dropItem
		
		val meta = when (item) {
			AlfheimItems.elvenResource -> ElvenResourcesMetas.MuspelheimEssence
			Items.coal                 -> ElvenResourcesMetas.NetherwoodCoal
			Items.fire_charge          -> 0
			else                       -> (item.maxDamage - 1) / (looting + 1)
		}
		
		val size = 1 + when (item) {
			AlfheimItems.elvenResource -> looting / 2
			Items.fire_charge          -> looting * 2
			Items.coal                 -> looting
			else                       -> 0
		}
		
		if (item == Items.coal) item = AlfheimItems.elvenResource
		
		entityDropItem(ItemStack(item, size, meta), 0f)
	}
	
	override fun addRandomArmor() {
		setCurrentItemOrArmor(0, ItemStack(AlfheimModularItems.volcanoMace))
		setCurrentItemOrArmor(1, ItemStack(AlfheimModularItems.volcanoBoots))
		setCurrentItemOrArmor(2, ItemStack(AlfheimModularItems.volcanoLeggings))
		setCurrentItemOrArmor(3, ItemStack(AlfheimModularItems.volcanoChest))
		setCurrentItemOrArmor(4, ItemStack(AlfheimModularItems.volcanoHelmet))
		equipmentDropChances.fill(0f)
	}
	
	override fun getYOffset() = super.getYOffset() - 0.5
}