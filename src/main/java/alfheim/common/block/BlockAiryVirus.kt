package alfheim.common.block

import alexsocol.asjlib.extendables.block.BlockModMeta
import alfheim.api.ModInfo
import alfheim.common.core.util.AlfheimTab
import net.minecraft.block.material.Material
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.world.*
import net.minecraftforge.common.util.ForgeDirection
import java.util.*

class BlockAiryVirus: BlockModMeta(Material.wood, 2, ModInfo.MODID, "AiryVirus", AlfheimTab, harvTool = "axe", harvLvl = 3) {
	
	init {
		tickRandomly = true
	}
	
	override fun isWood(world: IBlockAccess?, x: Int, y: Int, z: Int) = true
	
	override fun getCollisionBoundingBoxFromPool(world: World?, x: Int, y: Int, z: Int) = null
	
	override fun onBlockPlacedBy(world: World, x: Int, y: Int, z: Int, placer: EntityLivingBase?, stack: ItemStack?) {
		updateTick(world, x, y, z, world.rand)
	}
	
	override fun updateTick(world: World, x: Int, y: Int, z: Int, rand: Random) {
		val meta = world.getBlockMetadata(x, y, z)
		var none = true
		
		for (d in ForgeDirection.VALID_DIRECTIONS) {
			val i = x + d.offsetX
			val j = y + d.offsetY
			val k = z + d.offsetZ
			
			if (meta == 0) {
				if (world.isAirBlock(i, j, k))
					world.setBlock(i, j, k, this, 0, 3)
			} else {
				for (a in (i - 1)..(i + 1))
					for (b in (j - 1)..(j + 1))
						for (c in (k - 1)..(k + 1)) {
							val block = world.getBlock(a, b, c)
							val smeta = world.getBlockMetadata(a, b, c)
							
							if (block == this && smeta == 0)
								world.setBlockMetadataWithNotify(a, b, c, 1, 3).also { none = false }
						}
			}
		}
		
		if (none && meta != 0)
			world.setBlockToAir(x, y, z)
	}
}