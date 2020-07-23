package alfmod.common.item.material

import alexsocol.asjlib.*
import alfheim.common.item.ItemMod
import alfmod.common.core.helper.*
import alfmod.common.core.util.AlfheimModularTab
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.*
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge
import kotlin.math.*

class ItemEventResource: ItemMod("EventResource") {
	
	val texture = arrayOfNulls<IIcon>(subItems.size)
	
	init {
		setHasSubtypes(true)
		creativeTab = AlfheimModularTab
		
		if (ASJUtilities.isClient)
			MinecraftForge.EVENT_BUS.register(this)
	}
	
	fun isInterpolated(meta: Int) = meta == EventResourcesMetas.LavaMelon
	
	override fun registerIcons(reg: IIconRegister) {
		for (i in subItems.indices)
			if (!isInterpolated(i))
				texture[i] = IconHelper.forName(reg, subItems[i], "materials")
	}
	
	override fun getIconFromDamage(meta: Int) = texture[max(0, min(meta, texture.size - 1))]
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun loadTextures(event: TextureStitchEvent.Pre) {
		if (event.map.textureType == 1)
			for (i in subItems.indices)
				if (isInterpolated(i))
					texture[i] = InterpolatedIconHelper.forName(event.map, subItems[i], "materials")!!
	}
	
	override fun getUnlocalizedName(stack: ItemStack) = "item.${subItems[max(0, min(stack.meta, subItems.size - 1))]}"
	
	override fun getSubItems(item: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
		for (i in subItems.indices) list.add(ItemStack(item, 1, i))
	}
	
	val WIP = arrayOf(EventResourcesMetas.SnowRelic, EventResourcesMetas.VolcanoRelic)
	
	override fun addInformation(stack: ItemStack, player: EntityPlayer, list: MutableList<Any?>, extra: Boolean) {
		if (stack.meta in WIP)
			addStringToTooltip(StatCollector.translateToLocal("alfmodmisc.WIP"), list as MutableList<String?>)
	}
	
	fun addStringToTooltip(s: String, tooltip: MutableList<String?>) {
		tooltip.add(s.replace("&".toRegex(), "§"))
	}
	
	companion object {
		val subItems = arrayOf("SnowRelic", "VolcanoRelic", "LavaMelon")
	}
}

object EventResourcesMetas {
	
	val LavaMelon: Int
	val SnowRelic: Int
	val VolcanoRelic: Int
	
	init {
		val items = ItemEventResource.subItems
		LavaMelon = items.indexOf("LavaMelon")
		SnowRelic = items.indexOf("SnowRelic")
		VolcanoRelic = items.indexOf("VolcanoRelic")
	}
}