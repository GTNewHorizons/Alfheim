package alfheim.common.item.material

import alexsocol.asjlib.*
import alfheim.api.ModInfo
import alfheim.common.core.helper.IconHelper
import alfheim.common.core.util.AlfheimTab
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.*
import net.minecraft.util.IIcon

class ItemElvenFood: ItemFood(0, 0f, false) {
	
	val subItems = 3
	
	lateinit var icons: Array<IIcon>
	
	// #### ItemMod ####
	
	init {
		creativeTab = AlfheimTab
		unlocalizedName = "ElvenFood"
	}
	
	override fun setUnlocalizedName(name: String): Item {
		GameRegistry.registerItem(this, name)
		return super.setUnlocalizedName(name)
	}
	
	override fun getItemStackDisplayName(stack: ItemStack) =
		super.getItemStackDisplayName(stack).replace("&".toRegex(), "\u00a7")
	
	override fun getUnlocalizedNameInefficiently(stack: ItemStack) =
		super.getUnlocalizedNameInefficiently(stack).replace("item\\.".toRegex(), "item.${ModInfo.MODID}:") + stack.meta
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		icons = Array(subItems) { IconHelper.forItem(reg, this, it, "materials/food") }
	}
	
	override fun getIconFromDamage(meta: Int) = icons.safeGet(meta)
	
	// #### ItemFood ####
	
	// foodLevel
	override fun func_150905_g(stack: ItemStack): Int {
		return when (stack.meta) {
			0    -> 20  // lembas
			1, 2 -> 1 // grapes
			else -> 0
		}
	}
	
	// foodSaturationLevel
	override fun func_150906_h(stack: ItemStack): Float {
		return when (stack.meta) {
			0    -> 5f // lembas
			1, 2 -> 0.05f // grapes
			else -> 0f
		}
	}
	
	// #### Item ####
	
	override fun getSubItems(item: Item?, tab: CreativeTabs?, list: MutableList<Any?>) {
		(0 until subItems).forEach { list.add(ItemStack(item, 1, it)) }
	}
}

object ElvenFoodMetas {
	var m = -1
	get() {
		field++
		return field
	}
	
	val Lembas = m
	val RedGrapes = m
	val GreenGrapes = m
}