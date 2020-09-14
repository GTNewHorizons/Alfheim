package alfheim.common.item.relic

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.AlfheimTab
import com.google.common.collect.*
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.*
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.boss.IBossDisplayData
import net.minecraft.entity.monster.IMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityThrowable
import net.minecraft.item.ItemStack
import net.minecraft.potion.Potion
import net.minecraft.server.MinecraftServer
import net.minecraft.stats.Achievement
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.common.util.EnumHelper
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.internal.IManaBurst
import vazkii.botania.api.item.IRelic
import vazkii.botania.api.mana.*
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.entity.EntityManaBurst
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword
import vazkii.botania.common.item.relic.ItemRelic
import java.util.*

/**
 * This code is completely copied from 208th Botania version and fully made by Vazkii or whoever... :D<br></br>
 * Hope all required stuff is already done by Botania using iterfaces and stuff...
 */
class ItemExcaliber: ItemManasteelSword(toolMaterial, "Excaliber"), IRelic, ILensEffect {
	
	internal lateinit var achievement: Achievement
	
	init {
		creativeTab = AlfheimTab
	}
	
	override fun onUpdate(stack: ItemStack?, world: World, entity: Entity?, slotID: Int, inHand: Boolean) {
		if (entity is EntityPlayer) {
			val player = entity as EntityPlayer?
			ItemRelic.updateRelic(stack, player)
			if (ItemRelic.isRightPlayer(player!!, stack)) {
				val haste = player.getActivePotionEffect(Potion.digSpeed.id)
				val check = if (haste == null) 1f / 6f else if (haste.getAmplifier() == 0) 0.4f else if (haste.getAmplifier() == 2) 1f / 3f else 0.5f
				if (!world.isRemote && inHand && player.swingProgress == check && ManaItemHandler.requestManaExact(stack, player, 1, true)) {
					val burst = getBurst(player, stack!!)
					world.spawnEntityInWorld(burst)
					player.playSoundAtEntity("botania:terraBlade", 0.4f, 1.4f)
				}
			}
		}
	}
	
	override fun getIsRepairable(stack: ItemStack?, material: ItemStack?) = false
	
	override fun addInformation(stack: ItemStack?, player: EntityPlayer?, infoList: List<Any?>, advTooltip: Boolean) =
		ItemRelic.addBindInfo(infoList, stack, player)
	
	override fun bindToUsername(playerName: String, stack: ItemStack) =
		ItemRelic.bindToUsernameS(playerName, stack)
	
	override fun getSoulbindUsername(stack: ItemStack) = ItemRelic.getSoulbindUsernameS(stack)!!
	
	override fun getBindAchievement() = achievement
	
	override fun setBindAchievement(achievement: Achievement) {
		this.achievement = achievement
	}
	
	override fun usesMana(stack: ItemStack?) = false
	
	override fun isItemTool(p_77616_1_: ItemStack) = true
	
	override fun getEntityLifespan(itemStack: ItemStack?, world: World?) = Integer.MAX_VALUE
	
	override fun getAttributeModifiers(stack: ItemStack): Multimap<String, AttributeModifier> {
		val multimap = HashMultimap.create<String, AttributeModifier>()
		multimap.put(SharedMonsterAttributes.attackDamage.attributeUnlocalizedName, AttributeModifier(uuid, "Weapon modifier", 10.0, 0))
		multimap.put(SharedMonsterAttributes.movementSpeed.attributeUnlocalizedName, AttributeModifier(uuid, "Weapon modifier", 0.3, 1))
		return multimap
	}
	
	fun getBurst(player: EntityPlayer, stack: ItemStack): EntityManaBurst {
		val burst = EntityManaBurst(player)
		
		val motionModifier = 7f
		
		burst.color = 0xFFFF20
		burst.mana = 1
		burst.startingMana = 1
		burst.minManaLoss = 200
		burst.manaLossPerTick = 1f
		burst.gravity = 0f
		burst.setMotion(burst.motionX * motionModifier, burst.motionY * motionModifier, burst.motionZ * motionModifier)
		
		val lens = stack.copy()
		ItemNBTHelper.setString(lens, TAG_ATTACKER_USERNAME, player.commandSenderName)
		burst.sourceLens = lens
		return burst
	}
	
	override fun apply(stack: ItemStack, props: BurstProperties) = Unit
	
	override fun collideBurst(burst: IManaBurst, pos: MovingObjectPosition, isManaBlock: Boolean, dead: Boolean, stack: ItemStack) = dead
	
	override fun updateBurst(burst: IManaBurst, stack: ItemStack) {
		val entity = burst as EntityThrowable
		val axis = AxisAlignedBB.getBoundingBox(entity.posX, entity.posY, entity.posZ, entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).expand(1.0, 1.0, 1.0)
		
		val attacker = ItemNBTHelper.getString(burst.sourceLens, TAG_ATTACKER_USERNAME, "")
		var homeID = ItemNBTHelper.getInt(stack, TAG_HOME_ID, -1)
		if (homeID == -1) {
			val axis1 = AxisAlignedBB.getBoundingBox(entity.posX, entity.posY, entity.posZ, entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).expand(5.0, 5.0, 5.0)
			val entities = entity.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, axis1) as List<EntityLivingBase>
			for (living in entities) {
				if (living !is EntityPlayer && (living !is IBossDisplayData || AlfheimConfigHandler.superSpellBosses) && living is IMob && living.hurtTime == 0) {
					homeID = living.entityId
					ItemNBTHelper.setInt(stack, TAG_HOME_ID, homeID)
				}
			}
		}
		val entities = entity.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, axis) as List<EntityLivingBase>
		val home: Entity?
		if (homeID != -1) {
			home = entity.worldObj.getEntityByID(homeID)
			if (home != null) {
				val vecMotion = Vector3.fromEntityCenter(home).sub(Vector3.fromEntityCenter(entity))
				vecMotion.normalize().mul(Vector3(entity.motionX, entity.motionY, entity.motionZ).length())
				burst.setMotion(vecMotion.x, vecMotion.y, vecMotion.z)
			}
		}
		
		for (living in entities) {
			if (living !is EntityPlayer || living.commandSenderName != attacker && (MinecraftServer.getServer() == null || MinecraftServer.getServer().isPVPEnabled)) {
				if (living.hurtTime == 0) {
					val cost = 1
					val mana = burst.mana
					if (mana >= cost) {
						burst.mana = mana - cost
						var damage = 4f + toolMaterial.damageVsEntity
						if (!burst.isFake && !entity.worldObj.isRemote) {
							val player = living.worldObj.getPlayerEntityByName(attacker)
							val mod = player?.getAttributeMap()?.getAttributeInstance(SharedMonsterAttributes.attackDamage)?.attributeValue?.F
							damage = mod ?: damage
							if (player != null) damage += EnchantmentHelper.getEnchantmentModifierLiving(player, living)
							living.attackEntityFrom(if (player == null) DamageSource.magic else DamageSource.causePlayerDamage(player), damage)
							entity.setDead()
							break
						}
					}
				}
			}
		}
	}
	
	override fun doParticles(burst: IManaBurst, stack: ItemStack) = true
	
	override fun getRarity(sta: ItemStack) = BotaniaAPI.rarityRelic!!
	
	companion object {
		val uuid = UUID.fromString("7d5ddaf0-15d2-435c-8310-bdfc5fd1522d")!!
		
		const val TAG_ATTACKER_USERNAME = "attackerUsername"
		const val TAG_HOME_ID = "homeID"
		
		val toolMaterial = EnumHelper.addToolMaterial("B_EXCALIBER", 3, -1, 6.2f, 6f, 40)!!
	}
}