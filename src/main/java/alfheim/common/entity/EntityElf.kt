package alfheim.common.entity

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.api.AlfheimAPI
import alfheim.client.render.world.VisualEffectHandlerClient
import alfheim.common.core.handler.VisualEffectHandler
import alfheim.common.entity.spell.EntitySpellFenrirStorm
import alfheim.common.item.equipment.bauble.faith.*
import alfheim.common.spell.illusion.SpellShadowVortex
import alfheim.common.spell.wind.SpellFenrirStorm
import net.minecraft.command.IEntitySelector
import net.minecraft.entity.*
import net.minecraft.entity.ai.*
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.monster.IMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityLargeFireball
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.potion.*
import net.minecraft.util.DamageSource
import net.minecraft.village.*
import net.minecraft.world.World

class EntityElf(world: World): EntityCreature(world), IMerchant, INpc {
	
	var priestType
		get() = dataWatcher.getWatchableObjectInt(12)
		set(value) {
			dataWatcher.updateObject(12, value)
			
			getEntityAttribute(SharedMonsterAttributes.maxHealth).baseValue = if (isPriest) (60 + rng.nextInt(40)).D else (25 + rng.nextInt(15)).D
		}
	
	val isPriest
		get() = priestType != -1
	
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
		targetTasks.addTask(1, EntityAINearestAttackableTarget(this, EntityPlayer::class.java, 0, false, false, RagnarSelector))
		targetTasks.addTask(2, EntityAIHurtByTarget(this, false))
		targetTasks.addTask(3, EntityAINearestAttackableTarget(this, EntityLiving::class.java, 0, false, true, IMob.mobSelector))
	}
	
	override fun entityInit() {
		super.entityInit()
		dataWatcher.addObject(12, 0)
	}
	
	override fun applyEntityAttributes() {
		super.applyEntityAttributes()
		getEntityAttribute(SharedMonsterAttributes.followRange).baseValue = 64.0
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).baseValue = 0.5
	}
	
	override fun onLivingUpdate() {
		super.onLivingUpdate()
		
		heal(0.0125f)
		
		if (!isPriest) return
		val target = attackTarget ?: return
		
		when (priestType) {
			0 -> {
				if (rand.nextInt(300) != 0) return
				if (Vector3.entityDistance(this, target) > SpellFenrirStorm.radius - 1) return
				ASJUtilities.faceEntity(this, target, 360f, 360f)
				worldObj.spawnEntityInWorld(EntitySpellFenrirStorm(worldObj, this, true))
			}
			
			1 -> {
				if (rand.nextInt(200) == 0 && target is EntityPlayer)
					target.capabilities.isFlying = false
				
				if (rand.nextInt(600) != 0) return
				val (x, y, z) = Vector3.fromEntity(target).mf()
				
				val oxzs = target.width.mfloor() + 1
				val oye = target.health.mfloor() + 1
				
				for (i in x.bidiRange(oxzs))
					for (j in y.bidiRange(oye))
						for (k in z.bidiRange(oxzs))
							if (worldObj.isAirBlock(i, j, k))
								worldObj.setBlock(i, j, k, Blocks.dirt)
			}
			
			2 -> {
				if (rand.nextInt(400) != 0) return
				
				val (x, _, z) = Vector3.fromEntityCenter(target).mf()
				val y = target.eyeHeight.mfloor()
				
				if (!worldObj.isAirBlock(x, y, z)) return
				worldObj.setBlock(x, y, z, Blocks.water)
				
				if (worldObj.isAirBlock(x, y - 1, z))
					worldObj.setBlock(x, y - 1, z, Blocks.web)
				
				target.air = 0
			}
			
			3 -> {
				if (rand.nextInt(200) == 0)
					EntityLargeFireball(worldObj, this, target.posX - posX, target.posY - (posY + height / 2.0), target.posZ - posZ)
			}
			
			4 -> {
				if (rand.nextInt(300) == 0)
					for (i in 0..50)
						if (SpellShadowVortex.teleportRandomly(target)) {
							val (x, y, z) = Vector3.fromEntity(target)
							target.setPosition(x, y + rand.nextDouble() * 16 - 8, z)
							break
						}
			}
			
			5 -> {
				if (rand.nextInt(500) != 0) return
				
				target.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth).apply {
					val prev = getModifier(entityUniqueID)
					val amount = (prev?.amount ?: 0.0) + 2
					removeModifier(prev)
					applyModifier(AttributeModifier(entityUniqueID, "OdinPriestTookYourHeart", amount, 0))
				}
				
				heal(10f)
			}
		}
	}
	
	override fun collideWithEntity(entity: Entity) {
		if (entity is IMob && rng.nextInt(20) == 0) {
			attackTarget = entity as EntityLivingBase
		}
		
		super.collideWithEntity(entity)
	}
	
	override fun attackEntityAsMob(target: Entity): Boolean {
		val damage = DamageSource.causeMobDamage(this)
		var amount = (3 + rand.nextInt(5)).F
		
		if (isPriest)
			when (priestType) {
				0 -> { // Thor
					if (target is EntityLivingBase && !target.isPotionActive(Potion.moveSlowdown))
						target.addPotionEffect(PotionEffect(Potion.moveSlowdown.id, 75 + rand.nextInt(25), 1))
					
					FaithHandlerThor.traceLightning(this, target)
					damage.setDamageBypassesArmor()
					amount /= 2
				}
				
				1 -> run { // Sif
					if (rand.nextBoolean()) return@run
					
					if (target is EntityLivingBase && !target.isPotionActive(Potion.poison))
						target.addPotionEffect(PotionEffect(Potion.poison.id, 25 + rand.nextInt(25), 1))
				}
				
				2 -> { // Njord
					target.motionY += 0.5
					VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.SPLASH, target)
				}
				
				3 -> { // Loki
					if (rand.nextInt(12) == 0 && target is EntityLivingBase)
						target.removePotionEffect(Potion.fireResistance.id)
					
					target.setFire(rand.nextInt(5) + 3)
				}
				
				4 -> { // Heimdall
					if (target is EntityLivingBase && !target.isPotionActive(Potion.blindness))
						target.addPotionEffect(PotionEffect(Potion.blindness.id, 25 + rand.nextInt(25), 1))
				}
				
				5 -> run { // Odin
					if (rand.nextBoolean() && target is EntityPlayer)
						target.addExhaustion(rand.nextFloat() * 2 + 3)
					
					if (rand.nextInt(20) != 0) return@run
					if (target !is EntityLivingBase) return@run
					
					if (target is EntityPlayer)
						target.dropOneItem(true)
					else {
						target.entityDropItem(target.heldItem ?: return@run, target.height / 2)
						target.setCurrentItemOrArmor(0, null)
					}
				}
			}
		
		return target.attackEntityFrom(damage, amount)
	}
	
	override fun getTotalArmorValue() =
		if (isPriest) (0 until 4).sumBy { AlfheimAPI.ElvoriumArmor.getDamageReductionAmount(it) } else 0
	
	override fun writeEntityToNBT(nbt: NBTTagCompound) {
		super.writeEntityToNBT(nbt)
		nbt.setInteger(TAG_PRIEST, priestType)
	}
	
	override fun readEntityFromNBT(nbt: NBTTagCompound) {
		super.readEntityFromNBT(nbt)
		priestType = nbt.getInteger(TAG_PRIEST)
	}
	
	override fun isAIEnabled() = true
	override fun setCustomer(player: EntityPlayer) = Unit
	override fun getCustomer() = null
	override fun getRecipes(player: EntityPlayer) = null
	override fun setRecipes(recipeList: MerchantRecipeList) = Unit
	override fun useRecipe(recipe: MerchantRecipe) = Unit
	
	// verifySellingItem
	override fun func_110297_a_(stack: ItemStack?) = Unit
	
	companion object {
		
		const val TAG_PRIEST = "priest"
		
		object RagnarSelector: IEntitySelector {
			override fun isEntityApplicable(target: Entity?) =
				target is EntityPlayer && ItemRagnarokEmblem.getEmblem(target) != null
		}
	}
}