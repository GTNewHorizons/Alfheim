package alfheim.common.item

import alfheim.common.entity.EntityThrownPotion
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.potion.Potion
import net.minecraft.util.*
import net.minecraft.world.World
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.brew.*
import vazkii.botania.client.core.handler.ClientTickHandler
import vazkii.botania.client.core.helper.IconHelper
import vazkii.botania.common.core.helper.ItemNBTHelper
import java.awt.Color
import kotlin.math.*

class ItemSplashPotion: ItemMod("splashPotion"), IBrewItem, IBrewContainer {
	
	lateinit var itemIconFluid: IIcon
	
	override fun getSubItems(item: Item?, tab: CreativeTabs?, list: MutableList<Any?>?) {
		super.getSubItems(item, tab, list)
		
		if (item != null && list != null) {
			for (brew in BotaniaAPI.brewMap.keys) {
				val brewStack = getItemForBrew(BotaniaAPI.brewMap[brew] as Brew, ItemStack(this))
				if (brewStack != null) {
					list.add(brewStack)
				}
			}
		}
	}
	
	override fun onItemRightClick(stack: ItemStack?, world: World?, player: EntityPlayer?): ItemStack? {
		if (stack != null && world != null && player != null) {
			if (!world.isRemote) {
				val potion = EntityThrownPotion(player, getBrew(stack).getPotionEffects(stack))
				world.spawnEntityInWorld(potion)
				
				stack.stackSize--
			}
		}
		
		return stack
	}
	
	override fun getColorFromItemStack(stack: ItemStack?, pass: Int): Int {
		if (stack != null) {
			return if (pass == 0) {
				0xCCCCCFF
			} else {
				val color = Color(getBrew(stack).getColor(stack))
				val add = (sin(ClientTickHandler.ticksInGame.toDouble() * 0.1) * 16.0).toInt()
				val r = max(0, min(255, color.red + add))
				val g = max(0, min(255, color.green + add))
				val b = max(0, min(255, color.blue + add))
				(r shl 16) or (g shl 8) or b
			}
		}
		
		return 0xFFFFFF
	}
	
	override fun registerIcons(par1IconRegister: IIconRegister) {
		itemIcon = IconHelper.forName(par1IconRegister, "vial" + "0")
		itemIconFluid = IconHelper.forName(par1IconRegister, "vial" + "1_0")
	}
	
	override fun addInformation(stack: ItemStack?, player: EntityPlayer?, list: MutableList<Any?>?, adv: Boolean) {
		if (stack != null && list != null) {
			val brew = getBrew(stack)
			addStringToTooltip("${EnumChatFormatting.DARK_PURPLE}${StatCollector.translateToLocalFormatted("botaniamisc.brewOf", StatCollector.translateToLocal(brew.getUnlocalizedName(stack)))}", list)
			
			for (effect in brew.getPotionEffects(stack)) {
				val potion = Potion.potionTypes[effect.potionID]
				val format = if (potion.isBadEffect) EnumChatFormatting.RED else EnumChatFormatting.GRAY
				addStringToTooltip("" + format + StatCollector.translateToLocal(effect.effectName) + (if (effect.amplifier == 0) "" else " " + StatCollector.translateToLocal("botania.roman" + (effect.amplifier + 1))) + EnumChatFormatting.GRAY + (if (potion.isInstant) "" else " (" + Potion.getDurationString(effect) + ")"), list)
			}
		}
	}
	
	override fun getItemForBrew(brew: Brew, stack: ItemStack): ItemStack? {
		val brewStack = ItemStack(this)
		setBrew(brewStack, brew)
		return brewStack
		
	}
	
	internal fun addStringToTooltip(s: String, tooltip: MutableList<Any?>?) {
		tooltip?.add(s.replace("&".toRegex(), "§"))
	}
	
	override fun requiresMultipleRenderPasses() = true
	
	override fun getIcon(stack: ItemStack, pass: Int) = (if (pass == 0) itemIcon else itemIconFluid)!!
	
	override fun getBrew(stack: ItemStack): Brew {
		val key = ItemNBTHelper.getString(stack, "brewKey", "")
		return BotaniaAPI.getBrewFromKey(key)
	}
	
	override fun getManaCost(p0: Brew?, p1: ItemStack?) = p0?.manaCost?.times(1.5)?.toInt() ?: 400
	
	fun setBrew(stack: ItemStack, brew: Brew?) {
		setBrew(stack, (brew ?: BotaniaAPI.fallbackBrew).key)
	}
	
	fun setBrew(stack: ItemStack, brew: String) {
		ItemNBTHelper.setString(stack, "brewKey", brew)
	}
}
