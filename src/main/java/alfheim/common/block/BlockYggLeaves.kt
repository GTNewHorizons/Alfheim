package alfheim.common.block

import alfheim.client.core.util.mc
import alfheim.common.block.base.BlockMod
import alfmod.common.core.helper.IconHelper
import cpw.mods.fml.relauncher.*
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.IIcon

class BlockYggLeaves: BlockMod(Material.leaves) {
	
	var op = false
	var index = 0
	
	init {
		setBlockName("YggdrasilLeaves")
		setBlockUnbreakable()
	}
	
	override fun registerBlockIcons(reg: IIconRegister) {
		textures = Array(2) { IconHelper.forBlock(reg, this, it) }
	}
	
	override fun getIcon(side: Int, meta: Int): IIcon {
		setGraphicsLevel(mc.gameSettings.fancyGraphics)
		return textures[index]
	}
	
	@SideOnly(Side.CLIENT)
	fun setGraphicsLevel(fancy: Boolean) {
		op = fancy
		index = if (fancy) 0 else 1
	}
	
	override fun isOpaqueCube() = op
	
	companion object {
		lateinit var textures: Array<IIcon>
	}
}
