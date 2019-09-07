package alfheim.common.item.relic

import alfheim.api.ModInfo
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.core.util.AlfheimTab
import alfheim.common.entity.*
import com.google.common.collect.Multimap
import cpw.mods.fml.common.FMLCommonHandler
import net.minecraft.entity.*
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.monster.IMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityThrowable
import net.minecraft.item.*
import net.minecraft.potion.PotionEffect
import net.minecraft.stats.Achievement
import net.minecraft.util.*
import net.minecraft.util.MathHelper
import net.minecraft.world.World
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.internal.IManaBurst
import vazkii.botania.api.mana.*
import vazkii.botania.common.core.helper.*
import vazkii.botania.common.entity.EntityManaBurst
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword
import vazkii.botania.common.item.relic.ItemRelic
import java.util.*

/**
 * @author ExtraMeteorP, CKATEPTb
 */
class ItemSpearSubspace: ItemRelic("SpearSubspace"), IManaUsingItem, ILensEffect {
	
	init {
		creativeTab = AlfheimTab
		setFull3D()
	}
	
	override fun getAttributeModifiers(stack: ItemStack?): Multimap<String, AttributeModifier> {
		val attrib = super.getAttributeModifiers(stack) as Multimap<String, AttributeModifier>
		val uuid = UUID(unlocalizedName.hashCode().toLong(), 0)
		attrib.put(SharedMonsterAttributes.attackDamage.attributeUnlocalizedName, AttributeModifier(uuid, "spear modifier ", 8.0, 0))
		return attrib
	}
	
	override fun onUpdate(stack: ItemStack, world: World, entity: Entity?, slot: Int, selected: Boolean) {
		if (!world.isRemote && entity is EntityPlayer) {
			updateRelic(stack, entity)
			if (!isRightPlayer(entity, stack)) return
			
			if (icd(stack)) {
				if (entity.swingProgressInt == 1) {
					if (entity.heldItem?.item === this && ManaItemHandler.requestManaExact(stack, entity, 500, true)) {
						val sub = EntitySubspace(world, entity)
						sub.liveTicks = 24
						sub.delay = 6
						sub.posX = entity.posX
						sub.posY = entity.posY - entity.yOffset + 2.5 + (world.rand.nextFloat() * 0.2f).toDouble()
						sub.posZ = entity.posZ
						sub.rotationYaw = entity.rotationYaw
						sub.rotation = MathHelper.wrapAngleTo180_float(-entity.rotationYaw + 180)
						sub.type = 1
						sub.size = 0.40f + world.rand.nextFloat() * 0.15f
						if (!world.isRemote && ManaItemHandler.requestManaExactForTool(stack, entity, 400, true))
							world.spawnEntityInWorld(sub)
					}
					
					scd(stack, 25)
				}
			} else scd(stack, gcd(stack) - 1)
		}
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		if (ManaItemHandler.requestManaExactForTool(stack, player, 1000, false)) player.setItemInUse(stack, getMaxItemUseDuration(stack))
		return stack
	}
	
	override fun getMaxItemUseDuration(stack: ItemStack?) = 200
	
	override fun getItemUseAction(stack: ItemStack?) = EnumAction.bow
	
	override fun getBindAchievement(): Achievement {
		return super.getBindAchievement()
	}
	
	override fun onPlayerStoppedUsing(stack: ItemStack, world: World, player: EntityPlayer, itemInUse: Int) {
		if (isRightPlayer(player, stack) && icd(stack)) {
			if (!ManaItemHandler.requestManaExactForTool(stack, player, 1000, true)) return
			
			player.isSprinting = true
			player.setJumping(true)
			player.motionY += 0.75
			if (!world.isRemote)
				for (i in 0 until 20) {
					/*var look = Vector3(player.lookVec).multiply(1.0, 0.0, 1.0)
					
					val playerRot = Math.toRadians((player.rotationYaw + 90).toDouble())
					if (look.x == 0.0 && look.z == 0.0)
						look = Vector3(cos(playerRot), 0.0, sin(playerRot))
					
					look = look.normalize().multiply(-2.0)
					
					val div = i / 8
					val mod = i % 8
					
					val pl = look.add(Vector3.fromEntityCenter(player)).add(0.0, 1.6, div * 0.1)
					
					val axis = look.normalize().crossProduct(Vector3(-1.0, 0.0, -1.0)).normalize()
					
					val rot = mod * Math.PI / 7 - Math.PI / 2
					
					var axis1 = axis.multiply(div * 3.5 + 5).rotate(rot, look)
					if (axis1.y < 0)
						axis1 = axis1.multiply(1.0, -1.0, 1.0)
					
					val end = pl.add(axis1)
					
					val sub = EntitySubspace(world, player)
					sub.liveTicks = 120
					sub.delay = 15 + world.rand.nextInt(12)
					sub.posX = end.x
					sub.posY = end.y - 0.5f + world.rand.nextFloat()
					sub.posZ = end.z
					sub.rotationYaw = player.rotationYaw
					sub.rotation = MathHelper.wrapAngleTo180_float(-player.rotationYaw + 180)
					sub.interval = 10 + world.rand.nextInt(10)
					sub.size = 1.0f + world.rand.nextFloat()
					sub.type = 0
					if (!world.isRemote)
						world.spawnEntityInWorld(sub)
					if (i == 1)
						world.playSoundAtEntity(sub, "spearsubspace", 1f, 1f)*/
					
					val look = Vector3(player.lookVec)
					look.y = 0.0
					look.normalize().negate().multiply(2.0)
					val div = i / 5
					val mod = i % 5
					val pl = look.copy().add(Vector3.fromEntityCenter(player)).add(0.0, 1.6, div.toDouble() * 0.1)
					val axis = look.copy().normalize().crossProduct(Vector3(-1.0, 0.0, -1.0)).normalize()
					val axis1 = axis.copy()
					val rot = mod.toDouble() * 3.141592653589793 / 4.0 - 1.5707963267948966
					axis1.multiply(div.toDouble() * 3.5 + 5.0).rotate(rot, look)
					if (axis1.y < 0.0) {
						axis1.y = -axis1.y
					}
					
					val end = pl.copy().add(axis1)
					val sub = EntitySubspace(world, player)
					sub.liveTicks = 120
					sub.delay = 15 + world.rand.nextInt(12)
					sub.posX = end.x
					sub.posY = end.y - 0.5f + world.rand.nextFloat()
					sub.posZ = end.z
					sub.rotationYaw = player.rotationYaw
					sub.rotation = MathHelper.wrapAngleTo180_float(-player.rotationYaw + 180.0f)
					sub.interval = 10 + world.rand.nextInt(10)
					sub.size = 1.0f + world.rand.nextFloat()
					sub.type = 0
					
					player.worldObj.spawnEntityInWorld(sub)
					
					if (i == 1) player.worldObj.playSoundAtEntity(sub, "${ModInfo.MODID}:spearsubspace", 1.0f, 1.0f + player.worldObj.rand.nextFloat() * 3.0f)
				}
			player.addPotionEffect(PotionEffect(AlfheimRegistry.eternity.id, 120, 0))
			
			scd(stack, 200)
		}
		
		super.onPlayerStoppedUsing(stack, world, player, itemInUse)
	}
	
	fun gcd(stack: ItemStack) = ItemNBTHelper.getInt(stack, TAG_COOLDOWN, 0)
	fun icd(stack: ItemStack) = ItemNBTHelper.getInt(stack, TAG_COOLDOWN, 0) == 0
	fun scd(stack: ItemStack, cd: Int) = ItemNBTHelper.setInt(stack, TAG_COOLDOWN, cd)
	
	override fun usesMana(arg0: ItemStack) = true
	
	private val MANA_PER_DAMAGE = 160
	
	override fun doParticles(burst: IManaBurst?, stack: ItemStack?) = true
	
	override fun collideBurst(burst: IManaBurst, mop: MovingObjectPosition, arg2: Boolean, dead: Boolean, stack: ItemStack): Boolean {
		val entity = burst as EntityThrowable
		if (burst.color == 0XFFAF00) {
			entity.worldObj.spawnParticle("hugeexplosion", entity.posX, entity.posY, entity.posZ, 1.0, 0.0, 0.0)
			entity.worldObj.playSoundEffect(entity.posX, entity.posY, entity.posZ, "random.explode", 4.0f, (1.0f + (entity.worldObj.rand.nextFloat() - entity.worldObj.rand.nextFloat()) * 0.2f) * 0.7f)
		}
		return dead
	}
	
	override fun apply(stack: ItemStack, props: BurstProperties) = Unit // NO-OP
	
	override fun updateBurst(burst: IManaBurst, stack: ItemStack) {
		val entity = burst as EntityThrowable
		val axis = AxisAlignedBB.getBoundingBox(entity.posX, entity.posY, entity.posZ, entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).expand(1.0, 1.0, 1.0)
		val attacker = ItemNBTHelper.getString(burst.sourceLens, TAG_ATTACKER_USERNAME, "")
		
		if (burst.color == 0XFFAF00 || burst.color == 0XFFD700) {
			val axis1 = AxisAlignedBB.getBoundingBox(entity.posX - 2.5f, entity.posY - 2.5f, entity.posZ - 2.5f, entity.lastTickPosX + 2.5f, entity.lastTickPosY + 2.5f, entity.lastTickPosZ + 2.5f)
			if (burst.color == 0XFFD700)
				axis1.expand(1.5, 1.5, 1.5)
			val entities = entity.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, axis1) as List<EntityLivingBase>
			for (living in entities) {
				if (living is EntityPlayer && (living.commandSenderName == attacker || FMLCommonHandler.instance().minecraftServerInstance != null && !FMLCommonHandler.instance().minecraftServerInstance.isPVPEnabled) && burst.color == 0XFFAF00)
					continue
				if (entity.ticksExisted % 3 == 0)
					EntitySubspaceSpear.dealTrueDamage(living, living, if (burst.color == 0XFFD700) 1.8f else 2.2f)
			}
			return
		}
		
		var homeID = ItemNBTHelper.getInt(stack, ItemExcaliber.TAG_HOME_ID, -1)
		if (homeID == -1) {
			val axis1 = AxisAlignedBB.getBoundingBox(entity.posX - 5f, entity.posY - 5f, entity.posZ - 5f, entity.lastTickPosX + 5f, entity.lastTickPosY + 5f, entity.lastTickPosZ + 5f)
			val entities = entity.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, axis1) as List<EntityLivingBase>
			for (living in entities) {
				if (living is EntityPlayer || living !is IMob || living.hurtTime != 0)
					continue
				homeID = living.entityId
				ItemNBTHelper.setInt(stack, ItemExcaliber.TAG_HOME_ID, homeID)
				break
			}
		}
		
		val entities = entity.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, axis) as List<EntityLivingBase>
		if (homeID != -1) {
			val home = entity.worldObj.getEntityByID(homeID)
			if (home != null) {
				val vecEntity = Vector3.fromEntityCenter(home)
				val vecThis = Vector3.fromEntityCenter(entity)
				val vecMotion = vecEntity.subtract(vecThis)
				val vecCurrentMotion = Vector3(entity.motionX, entity.motionY, entity.motionZ)
				vecMotion.normalize().multiply(vecCurrentMotion.mag())
				burst.setMotion(vecMotion.x, vecMotion.y, vecMotion.z)
			}
		}
		for (living in entities) {
			if (living is EntityPlayer && (living.commandSenderName == attacker || FMLCommonHandler.instance().minecraftServerInstance != null && !FMLCommonHandler.instance().minecraftServerInstance.isPVPEnabled))
				continue
			
			if (living.isEntityAlive) {
				val cost = ItemManasteelSword.MANA_PER_DAMAGE / 3
				val mana = burst.mana
				if (mana >= cost) {
					burst.mana = mana - cost
					val damage = BotaniaAPI.terrasteelToolMaterial.damageVsEntity + 3f
					if (!burst.isFake && !entity.worldObj.isRemote) {
						val player = living.worldObj.getPlayerEntityByName(attacker)
						if (player != null) {
							EntitySubspaceSpear.dealTrueDamage(player, living, damage)
						}
						entity.setDead()
						break
					}
				}
			}
		}
	}
	
	val TAG_ATTACKER_USERNAME = "attackerUsername"
	val TAG_COOLDOWN = "cooldown"
	
	fun getBurst(player: EntityPlayer, stack: ItemStack): EntityManaBurst {
		val burst = EntityManaBurst(player)
		
		val motionModifier = 9f
		burst.color = 0xFFFF20
		burst.mana = MANA_PER_DAMAGE
		burst.startingMana = MANA_PER_DAMAGE
		burst.minManaLoss = 40
		burst.manaLossPerTick = 4f
		burst.gravity = 0f
		burst.setMotion(burst.motionX * motionModifier, burst.motionY * motionModifier, burst.motionZ * motionModifier)
		
		val lens = stack.copy()
		ItemNBTHelper.setString(lens, TAG_ATTACKER_USERNAME, player.commandSenderName)
		burst.sourceLens = lens
		return burst
	}
}