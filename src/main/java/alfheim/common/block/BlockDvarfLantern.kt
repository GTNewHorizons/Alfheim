package alfheim.common.block

import alfheim.common.block.base.BlockMod
import alfheim.common.core.helper.IconHelper
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.IIcon

class BlockDvarfLantern: BlockMod(Material.rock) {
	
	lateinit var icons: Array<IIcon>
	
	init {
		setBlockName("DvarfLantern")
		setHardness(10f)
		setHarvestLevel("pickaxe", 2)
		setLightLevel(1f)
		setResistance(10000f)
		setStepSound(soundTypeStone)
	}
	
	override fun registerBlockIcons(reg: IIconRegister) {
		icons = arrayOf(
			IconHelper.forBlock(reg, this, "Top"),
			IconHelper.forBlock(reg, this)
		)
	}
	
	override fun getIcon(side: Int, meta: Int) = if (meta != 1) { if (side < 2) icons[0] else icons[1] } else icons[1]
}
