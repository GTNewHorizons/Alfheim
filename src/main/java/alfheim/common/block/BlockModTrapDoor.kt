package alfheim.common.block

import alfheim.common.core.helper.IconHelper
import alfheim.common.core.util.*
import alfheim.common.item.block.ItemBlockMod
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister

class BlockModTrapDoor(material: Material, val name: String): BlockTrapDoor(material) {
	
	var originalLight: Int = 0
	
	init {
		setCreativeTab(AlfheimTab)
		setBlockName(name)
	}
	
	override fun setBlockName(name: String): Block {
		if (shouldRegisterInNameSet())
			GameRegistry.registerBlock(this, ItemBlockMod::class.java, name)
		return super.setBlockName(name)
	}
	
	fun shouldRegisterInNameSet() = true
	
	override fun setLightLevel(level: Float): Block {
		originalLight = (level * 15).I
		return super.setLightLevel(level)
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerBlockIcons(reg: IIconRegister) {
		blockIcon = IconHelper.forBlock(reg, this)
	}
}