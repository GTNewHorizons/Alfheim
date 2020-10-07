package alfheim.common.block

import alexsocol.asjlib.extendables.block.BlockModMeta
import alexsocol.asjlib.safeGet
import alfheim.api.ModInfo
import alfheim.client.core.helper.IconHelper
import alfheim.common.core.util.AlfheimTab
import cpw.mods.fml.relauncher.*
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.world.IBlockAccess
import kotlin.math.abs

class BlockLivingMountain: BlockModMeta(Material.rock, 1, ModInfo.MODID, "LivingMountain", AlfheimTab, 5f) {
	
	override fun registerBlockIcons(reg: IIconRegister) {
		icons = Array(4) { IconHelper.forBlock(reg, this, it + 1, "decor") }
	}
	
	override fun getIcon(side: Int, meta: Int) = icons[0]
	
	@SideOnly(Side.CLIENT)
	override fun getIcon(world: IBlockAccess?, x: Int, y: Int, z: Int, side: Int) =
		when (side) {
			0, 1 -> icons.safeGet(abs(x % 2) + abs(z % 2) * 2)
			2, 3 -> icons.safeGet(abs(x % 2) + abs(y % 2) * 2)
			4, 5 -> icons.safeGet(abs(z % 2) + abs(y % 2) * 2)
			else -> icons[0]
		}
}
