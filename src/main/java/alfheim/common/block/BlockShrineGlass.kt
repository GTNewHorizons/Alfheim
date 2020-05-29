package alfheim.common.block

import alexsocol.asjlib.extendables.block.BlockModMeta
import alfheim.api.ModInfo
import alfheim.common.core.util.AlfheimTab
import cpw.mods.fml.relauncher.*
import net.minecraft.block.material.Material
import net.minecraft.util.Facing
import net.minecraft.world.IBlockAccess

class BlockShrineGlass: BlockModMeta(Material.glass, 5, ModInfo.MODID, "ShrineGlass", AlfheimTab, resist = 600f, folder = "decor/") {
	
	init {
		setLightOpacity(0)
	}
	
	override fun getRenderBlockPass() = 1
	
	override fun isOpaqueCube() = false
	
	override fun renderAsNormalBlock() = false
	
	override fun canSilkHarvest() = true
	
	@SideOnly(Side.CLIENT)
	override fun shouldSideBeRendered(access: IBlockAccess, x: Int, y: Int, z: Int, side: Int): Boolean {
		val block = access.getBlock(x, y, z)
		
		if (access.getBlockMetadata(x, y, z) != access.getBlockMetadata(x - Facing.offsetsXForSide[side], y - Facing.offsetsYForSide[side], z - Facing.offsetsZForSide[side])) {
			return true
		}
		return if (block === this) {
			false
		} else super.shouldSideBeRendered(access, x, y, z, side)
	}
}
