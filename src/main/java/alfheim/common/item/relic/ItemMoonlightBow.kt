package alfheim.common.item.relic

import alexsocol.asjlib.math.Vector3
import alfheim.api.ModInfo
import alfheim.common.achievement.AlfheimAchievements
import alfheim.common.core.util.AlfheimTab
import alfheim.common.entity.*
import alfheim.common.item.relic.ShootHelper.isLookingAtMoon
import com.google.common.collect.Multimap
import com.sun.xml.internal.fastinfoset.stax.events.AttributeBase
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
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
import java.util.*
import kotlin.math.*

/**
 * @author ExtraMeteorP, CKATEPTb, AlexSocol
 */
class ItemMoonlightBow: ItemBow(), IRelic {
	
	lateinit var icons: Array<IIcon>
	lateinit var moonD: IIcon
	lateinit var moons: Array<IIcon>
	lateinit var bownana: IIcon
	
	init {
		creativeTab = AlfheimTab
		maxDamage = 0
		maxStackSize = 1
		setFull3D()
		unlocalizedName = "MoonlightBow"
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
			val moon = isLookingAtMoon(player.entityWorld, player, Minecraft.getMinecraft().timer.renderPartialTicks, false)
			var r = 0.1f * if (moon) 3 else 1
			var g = 0.85f
			var b = if (moon) g else 0.1f
			
			if (stack.displayName.toLowerCase().trim { it <= ' ' } == "i'm a banana") {
				r = 0.95f
				g = 0.95f
				b = 0.1f
			}
			
			for (d in ds) {
				for (i in 1..36) {
					v.set(0.0, d, 0.0)
					v.rotate(i * 10.0, Vector3.oZ)
					v.rotate(player.rotationPitch.toDouble(), Vector3.oX)
					v.rotate(-player.rotationYaw.toDouble(), Vector3.oY)
					v.add(look.set(l).mul(if (d == 0.3) 1.75 else 1.0)).add(p)
					Botania.proxy.wispFX(player.worldObj, v.x, v.y, v.z, r, g, b, if (d == 0.3) 0.1f else 0.25f, 0f, 0.1f)
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
		var dmg = min(maxDmg, m + rank * 2).toFloat()
		var mana = min(maxDmg * 10, maxDmg + rank * 20) * 5
		var life = min(150, 5 + i * 4)
		var dispersion = 1f
		var speed = 2.5f
		if (dmg >= maxDmg && isLookingAtMoon(world, player, 0f, false)) {
			dmg = -1f
			mana = 20000
			life = 200
			dispersion = 0f
			speed = 5f
		}
		
		if (ManaItemHandler.requestManaExactForTool(stack, player, mana, true)) {
			val arrow = EntityMagicArrow(world, player)
			arrow.shoot(player, player.rotationPitch, player.rotationYaw, 0f, speed, dispersion)
			arrow.damage = dmg
			arrow.rotationYaw = player.rotationYaw
			arrow.rotation = MathHelper.wrapAngleTo180_float(-player.rotationYaw + 180)
			
			if (dmg != -1f) {
				val j = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack)
				if (j > 0) arrow.damage = arrow.damage + j * 2
			}
			
			arrow.life = life
			
			if (!world.isRemote)
				world.spawnEntityInWorld(arrow)
			
			world.playSoundAtEntity(player, "random.bow", 1f, 1f / (Item.itemRand.nextFloat() * 0.4f + 1.2f) + 0.5f)
		}
	}
	
	val chargeVelocityMultiplier: Float
		get() = 0.5f
	
	val maxDmg: Int
		get() = 20
	
	override fun addInformation(stack: ItemStack, player: EntityPlayer, list: MutableList<Any?>, adv: Boolean) {
//		list.add(StatCollector.translateToLocalFormatted("${getUnlocalizedNameInefficiently(stack)}.desc", 2 * EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack)))
//		list.add("")
		ItemRelic.addBindInfo(list, stack, player)
		super.addInformation(stack, player, list, adv)
	}
	
	override fun registerIcons(reg: IIconRegister) {
		itemIcon = reg.registerIcon("${ModInfo.MODID}:PhoebusBow")
		moonD = reg.registerIcon("${ModInfo.MODID}:MoonBow")
		bownana = reg.registerIcon("${ModInfo.MODID}:Bownana")
		
		icons = Array(4) {
			reg.registerIcon("${ModInfo.MODID}:PhoebusBow_${it + 1}")
		}
		
		moons = Array(4) {
			reg.registerIcon("${ModInfo.MODID}:MoonBow_${it + 1}")
		}
	}
	
	override fun getItemIconForUseDuration(dur: Int) = icons[dur]
	
	override fun getIcon(stack: ItemStack, renderPass: Int, player: EntityPlayer?, usingItem: ItemStack?, useRemaining: Int): IIcon {
		val m = maxDmg / 10
		val j = (((stack.maxItemUseDuration - useRemaining) * chargeVelocityMultiplier - m) / 5) * 2 + m
		
		var iconD = itemIcon
		var iconA = icons
		var moon = false
		
		if (player != null) {
			if (isLookingAtMoon(player.worldObj, player, Minecraft.getMinecraft().timer.renderPartialTicks, false)) {
				moon = true
				iconD = moonD
				iconA = moons
			}
		}
		
		return if (!moon && stack.displayName.toLowerCase().trim { it <= ' ' } == "i'm a banana")
			bownana
		else if (usingItem == null) {
			iconD
		} else if (j >= maxDmg) {
			iconA[3]
		} else if (j >= maxDmg / 3f * 2f) {
			iconA[2]
		} else if (j > maxDmg / 3f) {
			iconA[1]
		} else {
			if (j > 0) iconA[0] else iconD
		}
	}
	
	// ################################ ItemMod ################################
	
	override fun setUnlocalizedName(name: String): Item {
		GameRegistry.registerItem(this, name)
		return super.setUnlocalizedName(name)
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
	
	override fun getBindAchievement() = AlfheimAchievements.moonlightBow
	
	override fun setBindAchievement(achievement: Achievement?) = Unit // NO-OP
	
	override fun getRarity(stack: ItemStack?) = BotaniaAPI.rarityRelic!!
}

/**
 * Code from iChunUtil ichun.common.core.EntityHelperBase
 * @author iChun
 */
private object ShootHelper {
	
	fun isLookingAtMoon(world: World, ent: EntityLivingBase, renderTick: Float, goThroughTransparentBlocks: Boolean): Boolean {
		if (ent.dimension == -1 || ent.dimension == 1) {
			return false
		}
		
		//13000 - 18000 - 23000
		//0.26 - 0.50 - 0.74
		//rotYaw = -88 to -92, 268 to 272
		//opposite for the other end, -268 to -272, 88 to 92
		//at 0.26 = -88 to -92
		//at 0.4 = -86 to -94
		//at 0.425 = -85 to -95
		//at 0.45 = -83 to -97
		//at 0.475 = -78 to -102
		//at 0.4875 = -64 to -116 //can 360 from here on i guess?
		//at 0.5 = -0 to -180
		// y = range, x = 0.45 etc, e = standard constant
		// y=e^(8.92574x) - 90 and y=-e^(8.92574x) - 90
		// 1.423 = 0.26 to 0.4
		// 1.52 = 0.4 to
		var de = 2.71828183
		var f = world.getCelestialAngle(1f)
		
		if (f !in 0.26f..0.74f) return false
		
		if (ent.rotationPitch !in -120f..-60f) return false
		
		// val f2 = if (f > 0.5f) f - 0.5f else 0.5f - f // unused
		var f3 = (if (ent.rotationYaw > 0f) 270 else -90).toFloat()
		f3 = if (f > 0.5f) if (ent.rotationYaw > 0f) 90f else -270f else f3
		f = if (f > 0.5f) 1.0f - f else f
		
		de = when {
			f <= 0.475  -> 2.71828183
			f <= 0.4875 -> 3.88377
			f <= 0.4935 -> 4.91616
			f <= 0.4965 -> 5.40624
			f <= 0.5000 -> 9.8
			else        -> de
		}
		
		//yaw check = player.rotationYaw % 360 <= Math.pow(de, (4.92574 * mc.theWorld.getCelestialAngle(1.0F))) + f3 && mc.thePlayer.rotationYaw % 360 >= -Math.pow(de, (4.92574 * mc.theWorld.getCelestialAngle(1.0F))) + f3
		//pitch check = mc.thePlayer.rotationPitch <= ff + 2.5F && mc.thePlayer.rotationPitch >= ff - 2.5F
		
		//yaw check = player.rotationYaw % 360 <= Math.pow(de, (4.92574 * mc.theWorld.getCelestialAngle(1.0F))) + f3 && mc.thePlayer.rotationYaw % 360 >= -Math.pow(de, (4.92574 * mc.theWorld.getCelestialAngle(1.0F))) + f3
		val yawCheck = ent.rotationYaw % 360 <= de.pow(4.92574 * world.getCelestialAngle(1.0f)) + f3 && ent.rotationYaw % 360 >= -de.pow(4.92574 * world.getCelestialAngle(1.0f)) + f3
		var ff = world.getCelestialAngle(1.0f)
		ff = if (ff > 0.5f) 1.0f - ff else ff
		ff -= 0.26f
		ff = ff / 0.26f * -94f - 4f
		//pitch check = mc.thePlayer.rotationPitch <= ff + 2.5F && mc.thePlayer.rotationPitch >= ff - 2.5F
		val pitchCheck = ent.rotationPitch <= ff + 2.5f && ent.rotationPitch >= ff - 2.5f
		val vec3d = getEntityPositionEyes(ent, renderTick)
		val vec3d1 = ent.getLook(renderTick)
		val vec3d2 = vec3d.addVector(vec3d1.xCoord * 500.0, vec3d1.yCoord * 500.0, vec3d1.zCoord * 500.0)
		val mopCheck = rayTrace(ent.worldObj, vec3d, vec3d2, true, false, goThroughTransparentBlocks, 500) == null
		return yawCheck && pitchCheck && mopCheck
	}
	
	fun rayTrace(world: World, vec3d0: Vec3, vec3d1: Vec3, flag: Boolean, flag1: Boolean, goThroughTransparentBlocks: Boolean, distance: Int): MovingObjectPosition? {
		var vec3d = vec3d0
		
		data class Vec3i(var x: Int, var y: Int, var z: Int)
		
		if (java.lang.Double.isNaN(vec3d.xCoord) || java.lang.Double.isNaN(vec3d.yCoord) || java.lang.Double.isNaN(vec3d.zCoord)) {
			return null
		}
		
		if (java.lang.Double.isNaN(vec3d1.xCoord) || java.lang.Double.isNaN(vec3d1.yCoord) || java.lang.Double.isNaN(vec3d1.zCoord)) {
			return null
		}
		
		val i = MathHelper.floor_double(vec3d1.xCoord)
		val j = MathHelper.floor_double(vec3d1.yCoord)
		val k = MathHelper.floor_double(vec3d1.zCoord)
		var l = MathHelper.floor_double(vec3d.xCoord)
		var i1 = MathHelper.floor_double(vec3d.yCoord)
		var j1 = MathHelper.floor_double(vec3d.zCoord)
		val block = world.getBlock(l, i1, j1)
		val meta = world.getBlockMetadata(l, i1, j1)
		
		if ((!flag1 || block.getCollisionBoundingBoxFromPool(world, l, i1, j1) != null) && block.canCollideCheck(meta, flag)) {
			val movingobjectposition = block.collisionRayTrace(world, l, i1, j1, vec3d, vec3d1)
			
			if (movingobjectposition != null) {
				return movingobjectposition
			}
		}
		
		var l1 = distance
		while (l1-- >= 0) {
			if (java.lang.Double.isNaN(vec3d.xCoord) || java.lang.Double.isNaN(vec3d.yCoord) || java.lang.Double.isNaN(vec3d.zCoord)) {
				return null
			}
			
			if (l == i && i1 == j && j1 == k) {
				return null
			}
			
			var flag5 = true
			var flag3 = true
			var flag4 = true
			var d0 = 999.0
			var d1 = 999.0
			var d2 = 999.0
			
			when {
				i > l -> d0 = l.toDouble() + 1.0
				i < l -> d0 = l.toDouble() + 0.0
				else  -> flag5 = false
			}
			
			when {
				j > i1 -> d1 = i1.toDouble() + 1.0
				j < i1 -> d1 = i1.toDouble() + 0.0
				else   -> flag3 = false
			}
			
			when {
				k > j1 -> d2 = j1.toDouble() + 1.0
				k < j1 -> d2 = j1.toDouble() + 0.0
				else   -> flag4 = false
			}
			
			var d3 = 999.0
			var d4 = 999.0
			var d5 = 999.0
			val d6 = vec3d1.xCoord - vec3d.xCoord
			val d7 = vec3d1.yCoord - vec3d.yCoord
			val d8 = vec3d1.zCoord - vec3d.zCoord
			
			if (flag5) {
				d3 = (d0 - vec3d.xCoord) / d6
			}
			
			if (flag3) {
				d4 = (d1 - vec3d.yCoord) / d7
			}
			
			if (flag4) {
				d5 = (d2 - vec3d.zCoord) / d8
			}
			
			if (d3 == -0.0) {
				d3 = -1.0E-4
			}
			
			if (d4 == -0.0) {
				d4 = -1.0E-4
			}
			
			if (d5 == -0.0) {
				d5 = -1.0E-4
			}
			
			val enumfacing: EnumFacing
			
			if (d3 < d4 && d3 < d5) {
				enumfacing = if (i > l) EnumFacing.WEST else EnumFacing.EAST
				vec3d = Vec3.createVectorHelper(d0, vec3d.yCoord + d7 * d3, vec3d.zCoord + d8 * d3)
			} else if (d4 < d5) {
				enumfacing = if (j > i1) EnumFacing.DOWN else EnumFacing.UP
				vec3d = Vec3.createVectorHelper(vec3d.xCoord + d6 * d4, d1, vec3d.zCoord + d8 * d4)
			} else {
				enumfacing = if (k > j1) EnumFacing.NORTH else EnumFacing.SOUTH
				vec3d = Vec3.createVectorHelper(vec3d.xCoord + d6 * d5, vec3d.yCoord + d7 * d5, d2)
			}
			
			l = MathHelper.floor_double(vec3d.xCoord) - if (enumfacing == EnumFacing.EAST) 1 else 0
			i1 = MathHelper.floor_double(vec3d.yCoord) - if (enumfacing == EnumFacing.UP) 1 else 0
			j1 = MathHelper.floor_double(vec3d.zCoord) - if (enumfacing == EnumFacing.SOUTH) 1 else 0
			
			val block1 = world.getBlock(l, i1, j1)
			val meta1 = world.getBlockMetadata(l, i1, j1)
			
			if (goThroughTransparentBlocks && isTransparent(block1)) {
				continue
			}
			
			if ((!flag1 || block1.getCollisionBoundingBoxFromPool(world, l, i1, j1) != null) && block1.canCollideCheck(meta1, flag)) {
				val movingobjectposition1 = block1.collisionRayTrace(world, l, i1, j1, vec3d, vec3d1)
				
				if (movingobjectposition1 != null) {
					return movingobjectposition1
				}
			}
		}
		
		return null
	}
	
	fun isTransparent(block: Block) = block.lightOpacity != 0xff
	
	fun getEntityPositionEyes(ent: Entity, partialTicks: Float): Vec3 {
		return if (partialTicks == 1.0f) {
			Vec3.createVectorHelper(ent.posX, ent.posY + ent.eyeHeight, ent.posZ)
		} else {
			val d0 = ent.prevPosX + (ent.posX - ent.prevPosX) * partialTicks.toDouble()
			val d1 = ent.prevPosY + (ent.posY - ent.prevPosY) * partialTicks.toDouble() + ent.eyeHeight
			val d2 = ent.prevPosZ + (ent.posZ - ent.prevPosZ) * partialTicks.toDouble()
			Vec3.createVectorHelper(d0, d1, d2)
		}
	}
}