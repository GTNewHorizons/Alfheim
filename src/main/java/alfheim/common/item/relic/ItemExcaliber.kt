package alfheim.common.item.relic

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap

import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.boss.IBossDisplayData
import net.minecraft.entity.monster.IMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityThrowable
import net.minecraft.item.EnumRarity
import net.minecraft.item.ItemStack
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect
import net.minecraft.server.MinecraftServer
import net.minecraft.stats.Achievement
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.DamageSource
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World
import net.minecraftforge.common.util.EnumHelper
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.internal.IManaBurst
import vazkii.botania.api.item.IRelic
import vazkii.botania.api.mana.BurstProperties
import vazkii.botania.api.mana.ILensEffect
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.entity.EntityManaBurst
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword
import vazkii.botania.common.item.relic.ItemRelic

/**
 * This code is completely copied from 208th Botania version and fully made by Vazkii or whoever... :D<br></br>
 * Hope all required stuff is already done by Botania using iterfaces and stuff...
 */
class ItemExcaliber: ItemManasteelSword(toolMaterial, "Excaliber"), IRelic, ILensEffect {
	
	internal var achievement: Achievement
	
	init {
		this.creativeTab = AlfheimCore.alfheimTab
	}
	
	override fun onUpdate(stack: ItemStack?, world: World, entity: Entity?, slotID: Int, inHand: Boolean) {
		if (entity is EntityPlayer) {
			val player = entity as EntityPlayer?
			ItemRelic.updateRelic(stack, player)
			if (ItemRelic.isRightPlayer(player!!, stack)) {
				val haste = player.getActivePotionEffect(Potion.digSpeed)
				val check = if (haste == null) 1.0f / 6.0f else if (haste.getAmplifier() == 0) 0.4f else if (haste.getAmplifier() == 2) 1.0f / 3.0f else 0.5f
				if (!world.isRemote && inHand && player.swingProgress == check && ManaItemHandler.requestManaExact(stack, player, 100, true)) {
					val burst = getBurst(player, stack!!)
					world.spawnEntityInWorld(burst)
					world.playSoundAtEntity(player, "botania:terraBlade", 0.4f, 1.4f)
				}
			}
		}
	}
	
	override fun addInformation(stack: ItemStack?, player: EntityPlayer?, infoList: List<*>?, advTooltip: Boolean) {
		ItemRelic.addBindInfo(infoList!!, stack, player)
	}
	
	override fun bindToUsername(playerName: String, stack: ItemStack) {
		ItemRelic.bindToUsernameS(playerName, stack)
	}
	
	override fun getSoulbindUsername(stack: ItemStack): String {
		return ItemRelic.getSoulbindUsernameS(stack)
	}
	
	override fun getBindAchievement(): Achievement {
		return this.achievement
	}
	
	override fun setBindAchievement(achievement: Achievement) {
		this.achievement = achievement
	}
	
	override fun usesMana(stack: ItemStack?): Boolean {
		return false
	}
	
	override fun isItemTool(p_77616_1_: ItemStack): Boolean {
		return true
	}
	
	override fun getEntityLifespan(itemStack: ItemStack?, world: World?): Int {
		return Integer.MAX_VALUE
	}
	
	override fun getItemAttributeModifiers(): Multimap<*, *> {
		val multimap = HashMultimap.create()
		multimap.put(SharedMonsterAttributes.attackDamage.attributeUnlocalizedName, AttributeModifier(Item.field_111210_e, "Weapon modifier", 10.0, 0))
		multimap.put(SharedMonsterAttributes.movementSpeed.attributeUnlocalizedName, AttributeModifier(Item.field_111210_e, "Weapon modifier", 0.3, 1))
		return multimap
	}
	
	fun getBurst(player: EntityPlayer, stack: ItemStack): EntityManaBurst {
		val burst = EntityManaBurst(player)
		
		val motionModifier = 7.0f
		
		burst.color = 0xFFFF20
		burst.mana = 1
		burst.startingMana = 100
		burst.minManaLoss = 200
		burst.manaLossPerTick = 1.0f
		burst.gravity = 0.0f
		burst.setMotion(burst.motionX * motionModifier, burst.motionY * motionModifier, burst.motionZ * motionModifier)
		
		val lens = stack.copy()
		ItemNBTHelper.setString(lens, TAG_ATTACKER_USERNAME, player.commandSenderName)
		burst.sourceLens = lens
		return burst
	}
	
	override fun apply(stack: ItemStack, props: BurstProperties) {}
	
	override fun collideBurst(burst: IManaBurst, pos: MovingObjectPosition, isManaBlock: Boolean, dead: Boolean, stack: ItemStack): Boolean {
		return dead
	}
	
	override fun updateBurst(burst: IManaBurst, stack: ItemStack) {
		val entity = burst as EntityThrowable
		val axis = AxisAlignedBB.getBoundingBox(entity.posX, entity.posY, entity.posZ, entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).expand(1.0, 1.0, 1.0)
		
		val attacker = ItemNBTHelper.getString(burst.sourceLens, TAG_ATTACKER_USERNAME, "")
		var homeID = ItemNBTHelper.getInt(stack, TAG_HOME_ID, -1)
		if (homeID == -1) {
			val axis1 = AxisAlignedBB.getBoundingBox(entity.posX, entity.posY, entity.posZ, entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).expand(5.0, 5.0, 5.0)
			val entities = entity.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, axis1)
			for (living in entities) {
				if (living !is EntityPlayer && living !is IBossDisplayData && living is IMob && living.hurtTime == 0) {
					homeID = living.getEntityId()
					ItemNBTHelper.setInt(stack, TAG_HOME_ID, homeID)
				}
			}
		}
		val entities = entity.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, axis)
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
						val damage = 4.0f + toolMaterial.damageVsEntity
						if (!burst.isFake && !entity.worldObj.isRemote) {
							val player = living.worldObj.getPlayerEntityByName(attacker)
							living.attackEntityFrom(if (player == null) DamageSource.magic else DamageSource.causePlayerDamage(player), damage)
							entity.setDead()
							break
						}
					}
				}
			}
		}
	}
	
	override fun doParticles(burst: IManaBurst, stack: ItemStack): Boolean {
		return true
	}
	
	override fun getRarity(p_77613_1_: ItemStack): EnumRarity {
		return BotaniaAPI.rarityRelic
	}
	
	companion object {
		
		private val TAG_ATTACKER_USERNAME = "attackerUsername"
		private val TAG_HOME_ID = "homeID"
		
		val toolMaterial = EnumHelper.addToolMaterial("B_EXCALIBER", 3, -1, 6.2f, 6.0f, 40)
	}
}