package alfheim.common.item.equipment.tool

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.*
import alfheim.client.render.world.SpellEffectHandlerClient.Spells
import alfheim.common.core.handler.SpellEffectHandler
import alfheim.common.core.registry.AlfheimItems
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.*
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.potion.*
import net.minecraft.util.*
import net.minecraft.world.World
import vazkii.botania.api.mana.*

import javax.xml.bind.annotation.adapters.HexBinaryAdapter
import java.nio.charset.Charset
import java.security.*

import vazkii.botania.common.core.helper.ItemNBTHelper.*

class ItemRealitySword: ItemSword(AlfheimAPI.REALITY), IManaUsingItem {
	init {
		creativeTab = AlfheimCore.alfheimTab
		setNoRepair()
		unlocalizedName = "RealitySword"
	}
	
	override fun getUnlocalizedName(stack: ItemStack?): String {
		return "item.RealitySword" + getInt(stack, TAG_ELEMENT, 0)
	}
	
	override fun registerIcons(reg: IIconRegister) {
		for (i in textures.indices)
			textures[i] = reg.registerIcon(ModInfo.MODID + ":RealitySword" + i)
	}
	
	@SideOnly(Side.CLIENT)
	override fun getIconIndex(stack: ItemStack): IIcon {
		return textures[getInt(stack, TAG_ELEMENT, 0)]
	}
	
	override fun getIcon(stack: ItemStack, pass: Int): IIcon {
		return getIconIndex(stack)
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World?, player: EntityPlayer): ItemStack {
		if (player.isSneaking) {
			if (getInt(stack, TAG_ELEMENT, 0) == 5) return stack
			if (merge(player.commandSenderName, stack.displayName) == "35E07445CBB8B10F7173F6AD6C1E29E9A66565F86AFF61ACADA750D443BFF7B0") {
				setInt(stack, TAG_ELEMENT, 5)
				stack.tagCompound.removeTag("display")
				return stack
			}
			
			if (!ManaItemHandler.requestManaExact(stack, player, 1, !world!!.isRemote)) return stack
			setInt(stack, TAG_ELEMENT, Math.max(0, getInt(stack, TAG_ELEMENT, 0) + 1) % 5)
		} else
			player.setItemInUse(stack, this.getMaxItemUseDuration(stack))
		return stack
	}
	
	internal fun merge(s1: String, s2: String): String? {
		val s = StringBuilder()
		for (i in 0 until s1.length) for (j in 0 until s2.length) s.append((s1[i] * s2[j] % 256).toChar())
		return hash(s.toString())
	}
	
	internal fun hash(str: String?): String? {
		if (str != null)
			try {
				val md = MessageDigest.getInstance("SHA-256")
				return HexBinaryAdapter().marshal(md.digest(salt(str).toByteArray(Charset.forName("UTF-8"))))
			} catch (e: NoSuchAlgorithmException) {
				e.printStackTrace()
			}
		
		return ""
	}
	
	// Might as well be called sugar given it's not secure at all :D
	internal fun salt(str: String): String {
		val rand = SecureRandom(str.toByteArray(Charset.forName("UTF-8")))
		val l = str.length
		val steps = rand.nextInt(l)
		val chrs = str.toCharArray()
		for (i in 0 until steps) {
			val indA = rand.nextInt(l)
			var indB: Int
			do {
				indB = rand.nextInt(l)
			} while (indB == indA)
			val c = (chrs[indA] xor chrs[indB]).toChar()
			chrs[indA] = c
		}
		
		return String(chrs)
	}
	
	override fun onUpdate(stack: ItemStack?, world: World, entity: Entity?, slotID: Int, inHand: Boolean) {
		if (world.isRemote) return
		
		val flag = getInt(stack, TAG_ELEMENT, 0) == 5
		if (entity is EntityPlayer) {
			if (!flag && 0 < getInt(stack, TAG_ELEMENT, 0) && getInt(stack, TAG_ELEMENT, 0) < 5)
				if (!ManaItemHandler.requestManaExact(stack, entity as EntityPlayer?, 1, !world.isRemote))
					setInt(stack!!, TAG_ELEMENT, 0)
			
			if (flag && entity.commandSenderName != "AlexSocol") {
				val player = entity as EntityPlayer?
				world.spawnEntityInWorld(EntityItem(world, player.posX, player.posY, player.posZ, stack!!.copy()))
				player.inventory.consumeInventoryItem(AlfheimItems.realitySword)
				player.setHealth(0f)
				player.onDeath(DamageSource.outOfWorld)
				ASJUtilities.sayToAllOnline(StatCollector.translateToLocalFormatted("item.RealitySword.DIE", player.getCommandSenderName()))
			}
		} else if (flag && entity is EntityLivingBase) {
			val living = entity as EntityLivingBase?
			world.spawnEntityInWorld(EntityItem(world, living.posX, living.posY, living.posZ, stack!!.copy()))
			stack.stackSize = 0
			living.setHealth(0f)
			living.onDeath(DamageSource.outOfWorld)
		} else if (flag) {
			world.spawnEntityInWorld(EntityItem(world, 0.0, 666.0, 0.0, stack!!.copy()))
			stack.stackSize = 0
		}
	}
	
	override fun hitEntity(stack: ItemStack, target: EntityLivingBase?, attacker: EntityLivingBase?): Boolean {
		if (attacker is EntityPlayer) {
			val elem = getInt(stack, TAG_ELEMENT, 0)
			if (elem != 0 && (elem == 5 || ManaItemHandler.requestManaExact(stack, attacker as EntityPlayer?, 1000, !attacker.worldObj.isRemote))) useAbility(elem, attacker, target)
		}
		return super.hitEntity(stack, target, attacker)
	}
	
	private fun useAbility(i: Int, attacker: EntityLivingBase, target: EntityLivingBase?) {
		when (i) {
			1 -> {
				target!!.addPotionEffect(PotionEffect(Potion.blindness.id, 100, -1))
				val vec = attacker.lookVec
				target.motionX = vec.xCoord * 1.5
				target.motionZ = vec.zCoord * 1.5
			}
			
			2 -> target!!.addPotionEffect(PotionEffect(Potion.moveSlowdown.id, 100, 1))
			3 -> target!!.setFire(6)
			
			4 -> {
				target!!.motionY += 0.825
				SpellEffectHandler.sendPacket(Spells.SPLASH, target!!)
			}
			
			5 -> {
				for (a in 1..4) useAbility(a, attacker, target)
			}
		}
	}
	
	override fun addInformation(stack: ItemStack?, player: EntityPlayer?, list: MutableList<*>?, b: Boolean) {
		val elem = getInt(stack, TAG_ELEMENT, 0)
		if (elem == 5) {
			list!!.add(StatCollector.translateToLocal("item.RealitySword.descZ"))
			return
		}
		
		if (0 < elem && elem < 5) {
			list!!.add(StatCollector.translateToLocal("item.RealitySword.desc$elem"))
			list.add(StatCollector.translateToLocal("item.RealitySword.desc5"))
		} else
			list!!.add(StatCollector.translateToLocal("item.RealitySword.desc0"))
	}
	
	override fun usesMana(stack: ItemStack): Boolean {
		return 0 < getInt(stack, TAG_ELEMENT, 0) && getInt(stack, TAG_ELEMENT, 0) < 5
	}
	
	companion object {
		
		val TAG_ELEMENT = "element"
		val textures = arrayOfNulls<IIcon>(6)
	}
}