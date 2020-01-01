package alfheim.common.block

import alfheim.AlfheimCore
import alfheim.common.block.base.BlockMod
import alfheim.common.core.util.AlfheimTab
import net.minecraft.block.IGrowable
import net.minecraft.block.material.Material
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.world.*
import net.minecraftforge.common.IPlantable
import net.minecraftforge.common.util.ForgeDirection
import java.util.*

class BlockSnowGrass: BlockMod(Material.grass), IGrowable {
	
	init {
		setBlockName("SnowGrass")
		setCreativeTab(AlfheimTab)
		setHardness(0.6f)
		setHarvestLevel("shovel", 0)
		setStepSound(soundTypeGrass)
		
		tickRandomly = true
	}
	
	override fun getIcon(side: Int, meta: Int) = when (side) {
		0       -> Blocks.dirt.getIcon(0, 0)
		1       -> Blocks.snow.getIcon(1, 0)
		in 2..5 -> Blocks.grass.field_149993_M
		else    -> blockIcon
	}!!
	
	override fun func_149851_a(world: World?, x: Int, y: Int, z: Int, hz: Boolean) = true
	override fun func_149852_a(world: World?, random: Random?, x: Int, y: Int, z: Int) = true
	override fun func_149853_b(world: World?, random: Random?, x: Int, y: Int, z: Int) = Unit
	override fun canSustainPlant(world: IBlockAccess?, x: Int, y: Int, z: Int, direction: ForgeDirection?, plantable: IPlantable?) = true
	override fun getItemDropped(meta: Int, random: Random?, fortune: Int) = Item.getItemFromBlock(Blocks.dirt)!!
	
	override fun updateTick(world: World, x: Int, y: Int, z: Int, random: Random) {
		val above = world.getBlock(x, y + 1, z)
		
		if (AlfheimCore.winter) {
			if (above === Blocks.air) {
				world.setBlock(x, y + 1, z, AlfheimBlocks.snowLayer)
			} else if (above === AlfheimBlocks.snowLayer) {
				val meta = world.getBlockMetadata(x, y + 1, z)
				val upMeta = ((x xor y xor z) and 7) / 2
				
				if (meta < upMeta)
					world.setBlockMetadataWithNotify(x, y + 1, z, meta + 1, 1 or 2)
			}
		} else {
			if (above === AlfheimBlocks.snowLayer) world.setBlockToAir(x, y + 1, z)
			world.setBlock(x, y, z, Blocks.grass)
		}
	}
}