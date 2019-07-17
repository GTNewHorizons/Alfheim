package alfheim.common.block

import alexsocol.asjlib.extendables.block.BlockMod
import alfheim.AlfheimCore
import cpw.mods.fml.relauncher.*
import net.minecraft.block.material.Material
import net.minecraft.util.Facing
import net.minecraft.world.IBlockAccess

class BlockWaterSlab: BlockMod(Material.rock) {
	
	init {
		setBlockBounds(0f, 0f, 0f, 1f, 0.5f, 1f)
		setBlockName("WaterSlab")
		setBlockTextureName("water_still")
		setCreativeTab(AlfheimCore.alfheimTab)
		setHardness(0.1f)
		setLightLevel(1f)
		setStepSound(soundTypeSnow)
	}
	
	override fun getRenderBlockPass() = 1
	
	override fun isOpaqueCube() = false
	
	override fun isNormalCube() = false
	
	@SideOnly(Side.CLIENT)
	override fun shouldSideBeRendered(access: IBlockAccess, x: Int, y: Int, z: Int, side: Int): Boolean {
		if (side < 2) return super.shouldSideBeRendered(access, x, y, z, side)
		
		val block = access.getBlock(x, y, z)
		
		if (access.getBlockMetadata(x, y, z) != access.getBlockMetadata(x - Facing.offsetsXForSide[side], y - Facing.offsetsYForSide[side], z - Facing.offsetsZForSide[side])) {
			return true
		}
		return if (block === this) {
			false
		} else super.shouldSideBeRendered(access, x, y, z, side)
	}
	
	@SideOnly(Side.CLIENT)
	override fun getMixedBrightnessForBlock(world: IBlockAccess, x: Int, y: Int, z: Int): Int {
		val l = world.getLightBrightnessForSkyBlocks(x, y + 1, z, 0)
		val i1 = world.getLightBrightnessForSkyBlocks(x, y + 1, z, 0)
		val j1 = l and 255
		val k1 = i1 and 255
		val l1 = l shr 16 and 255
		val i2 = i1 shr 16 and 255
		return (if (j1 > k1) j1 else k1) or ((if (l1 > i2) l1 else i2) shl 16)
	}
}
