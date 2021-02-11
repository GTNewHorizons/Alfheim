package alfheim.common.item.compat.tinkersconstruct

import alexsocol.asjlib.*
import alfheim.common.item.ItemMod
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.*
import net.minecraft.util.IIcon
import tconstruct.library.TConstructRegistry

class ItemNaturalMaterial: ItemMod("NaturalMaterial") {
	
	var materialList = arrayOf("RedBowstring", "ManaBowstring")
	lateinit var textures: Array<IIcon>
	
	init {
		setHasSubtypes(true)
		creativeTab = TConstructRegistry.materialTab
	}
	
	override fun registerIcons(reg: IIconRegister) {
		textures = Array(materialList.size) { reg.registerIcon("tinker:materials/${materialList[it]}") }
	}
	
	override fun getIconFromDamage(meta: Int) = textures.safeGet(meta)
	
	override fun getUnlocalizedName(stack: ItemStack) = "item.${materialList.safeGet(stack.meta)}"
	
	override fun getSubItems(item: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
		for (i in materialList.indices) list.add(ItemStack(item, 1, i))
	}
}
