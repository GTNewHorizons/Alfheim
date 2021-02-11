package alfheim.common.item.material

import alexsocol.asjlib.*
import alfheim.api.ModInfo
import alfheim.client.core.helper.IconHelper
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.AlfheimItems
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.IIcon
import net.minecraft.world.World

class ItemElvenFood: ItemFood(0, 0f, false) {
	
	val subItems = 6
	
	lateinit var icons: Array<IIcon>
	
	// #### ItemMod ####
	
	init {
		setHasSubtypes(true)
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
			3    -> 1 // honey
			4, 5 -> 6 // vine
			else -> 0
		}
	}
	
	// foodSaturationLevel
	override fun func_150906_h(stack: ItemStack): Float {
		return when (stack.meta) {
			0    -> 5f // lembas
			1, 2 -> 0.05f // grapes
			3    -> 0.01f // honey
			4, 5 -> 0.3f // vine
			else -> 0f
		}
	}
	
	val drinkables = arrayOf(ElvenFoodMetas.RedWine, ElvenFoodMetas.WhiteWine)
	
	override fun getItemUseAction(stack: ItemStack) = if (stack.meta == ElvenFoodMetas.RedWine || stack.meta == ElvenFoodMetas.WhiteWine) EnumAction.drink else EnumAction.eat
	
	override fun onEaten(stack: ItemStack, world: World?, player: EntityPlayer?): ItemStack {
		val res = super.onEaten(stack, world, player)
		return if (stack.meta in drinkables) ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.Jug) else res
	}
	
	// #### Item ####
	
	override fun getSubItems(item: Item?, tab: CreativeTabs?, list: MutableList<Any?>) {
		(0 until subItems).forEach { list.add(ItemStack(item, 1, it)) }
	}
	
	override fun getItemStackLimit(stack: ItemStack): Int {
		return if (stack.meta in drinkables) 1 else super.getItemStackLimit(stack)
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
	val WhiteGrapes = m
	val Nectar = m
	val RedWine = m
	val WhiteWine = m
}