package alfheim.common.block

import alexsocol.asjlib.extendables.block.BlockModMeta
import alfheim.api.ModInfo
import cpw.mods.fml.relauncher.*
import net.minecraft.block.material.Material
import net.minecraft.util.Facing
import net.minecraft.world.IBlockAccess

class BlockShrineGlass: BlockModMeta(Material.glass, 2, ModInfo.MODID, "ShrineGlass", resist = 600f, folder = "shrines/") {
	
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
