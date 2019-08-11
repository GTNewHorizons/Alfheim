package alfheim.common.item.relic

import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.common.achievement.AlfheimAchievements
import alfheim.common.entity.*
import com.google.common.collect.Multimap
import com.sun.xml.internal.fastinfoset.stax.events.AttributeBase
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.enchantment.*
import net.minecraft.entity.*
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.stats.Achievement
import net.minecraft.util.*
import net.minecraft.world.World
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.item.IRelic
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.item.relic.ItemRelic
import vazkii.botania.common.lib.LibMisc
import java.util.*
import kotlin.math.min

/**
 * @author ExtraMeteorP, CKATEPTb
 */
class ItemSunrayBow: ItemBow(), IRelic {
	
	lateinit var icon: Array<IIcon>
	
	init {
		creativeTab = AlfheimCore.alfheimTab
		setFull3D()
		maxDamage = 0
		setMaxStackSize(1)
		unlocalizedName = "SunrayBow"
	}
	
	override fun getAttributeModifiers(stack: ItemStack): Multimap<String, AttributeBase> {
		val attrib = super.getAttributeModifiers(stack)
		val uuid = UUID(unlocalizedName.hashCode().toLong(), 0)
		attrib.put(SharedMonsterAttributes.attackDamage.attributeUnlocalizedName, AttributeModifier(uuid, "Weapon modifier", 5.0, 0))
		return attrib as Multimap<String, AttributeBase>
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		if (isRightPlayer(player, stack))
			player.setItemInUse(stack, getMaxItemUseDuration(stack))
		return stack
	}
	
	override fun getMaxItemUseDuration(stack: ItemStack?) = 72000
	
	override fun getItemUseAction(stack: ItemStack) = EnumAction.bow
	
	override fun onUsingTick(stack: ItemStack, player: EntityPlayer, count: Int) {
		if (player.worldObj.isRemote) {
			val v = Vector3()
			val l = player.lookVec
			val look = Vector3()
			val p = Vector3(0.0, if (player === Minecraft.getMinecraft().thePlayer) 0.0 else 1.62, 0.0).add(Vector3.fromEntity(player))
			val ds = arrayOf(0.3, 0.8)
			for (d in ds) {
				for (i in 1..36) {
					v.set(0.0, d, 0.0)
					v.rotate(i * 10.0, Vector3.oZ)
					v.rotate(player.rotationPitch.toDouble(), Vector3.oX)
					v.rotate(-player.rotationYaw.toDouble(), Vector3.oY)
					v.add(look.set(l).mul(if (d == 0.3) 1.75 else 1.0)).add(p)
					Botania.proxy.wispFX(player.worldObj, v.x, v.y, v.z, 0.1f, 0.85f, 0.1f, if (d == 0.3) 0.1f else  0.25f, 0f, 0.1f)
				}
			}
		}
	}
	
	override fun onPlayerStoppedUsing(stack: ItemStack, world: World, player: EntityPlayer, itemInUse: Int) {
		if (!isRightPlayer(player, stack)) return
		val m = maxDmg / 10
		val i = ((getMaxItemUseDuration(stack) - itemInUse) * chargeVelocityMultiplier).toInt()
		if (i < m) return
		val rank = (i - m) / 5
		if (ManaItemHandler.requestManaExactForTool(stack, player, min(maxDmg * 10, maxDmg + rank * 20), true)) {
			val arrow = EntityMagicArrow(world, player)
			arrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0f, 3.0f, 1.0f)
			arrow.damage = min(maxDmg, m + rank * 2)
			arrow.rotationYaw = player.rotationYaw
			arrow.rotation = MathHelper.wrapAngleTo180_float(-player.rotationYaw + 180)
			val j = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack)
			if (j > 0) arrow.damage = arrow.damage + j * 2
			arrow.life = min(150, 5 + i * 4)
			
			if (!world.isRemote)
				world.spawnEntityInWorld(arrow)
			
			world.playSoundAtEntity(player, "random.bow", 1f, 1f / (Item.itemRand.nextFloat() * 0.4f + 1.2f) + 0.5f)
		}
	}
	
	val chargeVelocityMultiplier: Float
		get() = 0.25f
	
	val maxDmg: Int
		get() = 20
	
	override fun addInformation(stack: ItemStack, player: EntityPlayer, list: MutableList<Any?>, adv: Boolean) {
		list.add(StatCollector.translateToLocalFormatted("${getUnlocalizedNameInefficiently(stack)}.desc", 2 * EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack)))
		list.add("")
		ItemRelic.addBindInfo(list, stack, player)
		super.addInformation(stack, player, list, adv)
	}
	
	override fun registerIcons(reg: IIconRegister) {
		itemIcon = reg.registerIcon("${LibMisc.MOD_ID}:SunrayBow")
		
		icon = Array(4) {
			reg.registerIcon("${LibMisc.MOD_ID}:SunrayBow_${it + 1}")
		}
	}
	
	override fun getItemIconForUseDuration(dur: Int) = icon[dur]
	
	override fun getIcon(stack: ItemStack, renderPass: Int, player: EntityPlayer?, usingItem: ItemStack?, useRemaining: Int): IIcon {
		val m = maxDmg / 10
		val j = (((stack.maxItemUseDuration - useRemaining) * chargeVelocityMultiplier - m) / 5) * 2 + m
		
		return if (usingItem == null) {
			itemIcon
		} else if (j >= maxDmg) {
			getItemIconForUseDuration(3)
		} else if (j >= maxDmg/3f*2f) {
			getItemIconForUseDuration(2)
		} else if (j > maxDmg/3f) {
			getItemIconForUseDuration(1)
		} else {
			if (j > 0) getItemIconForUseDuration(0) else itemIcon
		}
	}
	
	// ################################ ItemMod ################################
	
	override fun setUnlocalizedName(str: String): Item {
		GameRegistry.registerItem(this, str)
		return super.setUnlocalizedName(str)
	}
	
	// ################################ ItemRelic ################################
	
	val TAG_SOULBIND = "soulbind"
	
	override fun onUpdate(stack: ItemStack, world: World?, entity: Entity?, slot: Int, inHand: Boolean) {
		if (entity is EntityPlayer) {
			updateRelic(stack, entity)
		}
	}
	
	fun shouldDamageWrongPlayer() = true
	
	override fun getEntityLifespan(itemStack: ItemStack?, world: World?) = Int.MAX_VALUE
	
	fun addStringToTooltip(s: String, tooltip: MutableList<in String>) {
		tooltip.add(s.replace("&".toRegex(), "§"))
	}
	
	fun getSoulbindUsernameS(stack: ItemStack?) = ItemNBTHelper.getString(stack, "soulbind", "")!!
	
	fun updateRelic(stack: ItemStack?, player: EntityPlayer) {
		if (stack != null && stack.item is IRelic) {
			if (getSoulbindUsernameS(stack).isEmpty()) {
				player.addStat((stack.item as IRelic).bindAchievement, 1)
				bindToPlayer(player, stack)
			}
			
			if (!isRightPlayer(player, stack) && player.ticksExisted % 10 == 0 && (stack.item !is ItemRelic || (stack.item as ItemRelic).shouldDamageWrongPlayer())) {
				player.attackEntityFrom(damageSource(), 2.0f)
			}
		}
	}
	
	fun bindToPlayer(player: EntityPlayer, stack: ItemStack) {
		bindToUsernameS(player.commandSenderName, stack)
	}
	
	fun bindToUsernameS(username: String, stack: ItemStack) {
		ItemNBTHelper.setString(stack, "soulbind", username)
	}
	
	fun isRightPlayer(player: EntityPlayer, stack: ItemStack?) = isRightPlayer(player.commandSenderName, stack)
	
	fun isRightPlayer(player: String, stack: ItemStack?) = getSoulbindUsernameS(stack) == player
	
	fun damageSource() = DamageSource("botania-relic")
	
	override fun bindToUsername(playerName: String, stack: ItemStack) {
		bindToUsernameS(playerName, stack)
	}
	
	override fun getSoulbindUsername(stack: ItemStack) = getSoulbindUsernameS(stack)
	
	override fun getBindAchievement() = AlfheimAchievements.sunray
	
	override fun setBindAchievement(achievement: Achievement?) = Unit // NO-OP
	
	override fun getRarity(stack: ItemStack?) = BotaniaAPI.rarityRelic!!
}
