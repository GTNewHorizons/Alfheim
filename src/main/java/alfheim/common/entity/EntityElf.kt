package alfheim.common.entity

import alexsocol.asjlib.*
import net.minecraft.entity.*
import net.minecraft.entity.ai.*
import net.minecraft.entity.monster.IMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.village.*
import net.minecraft.world.World

class EntityElf(world: World): EntityCreature(world), IMerchant, INpc {
	
	var attackTimer: Int = 0
	
	init {
		setSize(0.6f, 1.8f)
		tasks.addTask(1, EntityAIAttackOnCollide(this, 1.0, true))
		tasks.addTask(2, EntityAIMoveTowardsTarget(this, 0.9, 32f))
		tasks.addTask(3, EntityAIMoveThroughVillage(this, 0.6, true))
		tasks.addTask(4, EntityAIMoveTowardsRestriction(this, 1.0))
		tasks.addTask(5, EntityAISwimming(this))
		tasks.addTask(6, EntityAIWander(this, 0.6))
		tasks.addTask(7, EntityAIWatchClosest(this, EntityPlayer::class.java, 6f))
		tasks.addTask(7, EntityAIWatchClosest(this, EntityElf::class.java, 6f))
		tasks.addTask(8, EntityAILookIdle(this))
		targetTasks.addTask(2, EntityAIHurtByTarget(this, false))
		targetTasks.addTask(3, EntityAINearestAttackableTarget(this, EntityLiving::class.java, 0, false, true, IMob.mobSelector))
	}
	
	public override fun applyEntityAttributes() {
		super.applyEntityAttributes()
		getEntityAttribute(SharedMonsterAttributes.maxHealth).baseValue = (25 + rng.nextInt(15)).D
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).baseValue = 0.5
	}
	
	public override fun isAIEnabled() = true
	
	public override fun collideWithEntity(entity: Entity) {
		if (entity is IMob && rng.nextInt(20) == 0) {
			attackTarget = entity as EntityLivingBase
		}
		
		super.collideWithEntity(entity)
	}
	
	override fun attackEntityAsMob(entity: Entity): Boolean {
		attackTimer = 10
		worldObj.setEntityState(this, 4.toByte())
		return entity.attackEntityFrom(DamageSource.causeMobDamage(this), (3 + rand.nextInt(5)).F)
	}
	
	override fun setCustomer(p_70932_1_: EntityPlayer)  = Unit
	override fun getCustomer() = null
	override fun getRecipes(p_70934_1_: EntityPlayer) = null
	override fun setRecipes(p_70930_1_: MerchantRecipeList) = Unit
	override fun useRecipe(p_70933_1_: MerchantRecipe) = Unit
	override fun func_110297_a_(p_110297_1_: ItemStack) = Unit
}