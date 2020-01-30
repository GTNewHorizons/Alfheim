package alfmod.common.item.material

import alfheim.common.core.util.meta
import alfheim.common.item.ItemMod
import alfmod.common.core.helper.IconHelper
import alfmod.common.core.util.AlfheimModularTab
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.*
import kotlin.math.*

class ItemEventResource: ItemMod("EventResource") {
	
	private lateinit var texture: Array<IIcon>
	
	init {
		setHasSubtypes(true)
		creativeTab = AlfheimModularTab
	}
	
	override fun registerIcons(reg: IIconRegister) {
		texture = Array(subItems.size) { IconHelper.forName(reg, subItems[it], "materials") }
	}
	
	override fun getIconFromDamage(meta: Int) = texture[max(0, min(meta, texture.size - 1))]
	
	override fun getUnlocalizedName(stack: ItemStack) = "item.${subItems[max(0, min(stack.itemDamage, subItems.size - 1))]}"
	
	override fun getSubItems(item: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
		for (i in subItems.indices) list.add(ItemStack(item, 1, i))
	}
	
	override fun addInformation(stack: ItemStack, player: EntityPlayer, list: MutableList<Any?>, extra: Boolean) {
		if (stack.meta == EventResourcesMetas.SnowRelic) addStringToTooltip(StatCollector.translateToLocal("alfmodmisc.WIP"), list as MutableList<String?>)
	}
	
	fun addStringToTooltip(s: String, tooltip: MutableList<String?>) {
		tooltip.add(s.replace("&".toRegex(), "§"))
	}
	
	companion object {
		val subItems = arrayOf("SnowRelic")
	}
}

object EventResourcesMetas {
	
	val SnowRelic: Int
	
	init {
		val items = ItemEventResource.subItems
		SnowRelic = items.indexOf("SnowRelic")
	}
}