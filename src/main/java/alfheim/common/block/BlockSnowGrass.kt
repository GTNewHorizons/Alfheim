package alfheim.common.block

import alexsocol.asjlib.*
import alfheim.AlfheimCore
import alfheim.common.block.base.BlockMod
import alfheim.common.core.util.AlfheimTab
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.init.Blocks
import net.minecraft.world.*
import net.minecraftforge.common.*
import net.minecraftforge.common.util.ForgeDirection
import ru.vamig.worldengine.WE_PerlinNoise
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
	
	override fun registerBlockIcons(reg: IIconRegister) = Unit
	override fun func_149851_a(world: World?, x: Int, y: Int, z: Int, isRemote: Boolean) = true
	override fun func_149852_a(world: World?, random: Random?, x: Int, y: Int, z: Int) = true
	override fun func_149853_b(world: World?, random: Random?, x: Int, y: Int, z: Int) = Unit
	override fun getItemDropped(meta: Int, random: Random?, fortune: Int) = Blocks.dirt.toItem()
	
	override fun canSustainPlant(world: IBlockAccess, x: Int, y: Int, z: Int, direction: ForgeDirection, plantable: IPlantable): Boolean {
		// fuck you mojang and your protected shit!
		if (plantable is BlockBush && canPlaceBlockOn.invoke(plantable, Blocks.grass) as Boolean) {
			return true
		}
		
		return when (plantable.getPlantType(world, x, y + 1, z)) {
			EnumPlantType.Plains -> true
			EnumPlantType.Beach  -> world.getBlock(x - 1, y, z).material === Material.water || world.getBlock(x + 1, y, z).material === Material.water || world.getBlock(x, y, z - 1).material === Material.water || world.getBlock(x, y, z + 1).material === Material.water
			else                 -> false
		}
	}
	
	override fun updateTick(world: World, x: Int, y: Int, z: Int, random: Random) {
		val above = world.getBlock(x, y + 1, z)
		val meta = world.getBlockMetadata(x, y + 1, z)
		
		if (AlfheimCore.winter) {
			if (above === Blocks.snow_layer)
				world.setBlock(x, y + 1, z, AlfheimBlocks.snowLayer, meta, 3)
			
			// from BlockGrass:
			if (world.getBlockLightValue(x, y + 1, z) < 4 && world.getBlockLightOpacity(x, y + 1, z) > 2) {
				world.setBlock(x, y, z, Blocks.dirt)
			}
			
			if (world.isRaining && world.canBlockSeeTheSky(x, y + 1, z) && world.getPrecipitationHeight(x, z) >= y) {
				if (above === Blocks.air) {
					world.setBlock(x, y + 1, z, AlfheimBlocks.snowLayer)
				} else if (above === AlfheimBlocks.snowLayer) {
					val upMeta = WE_PerlinNoise.PerlinNoise2D(world.seed, x.D, z.D, 1.0, 1).times(15).I.and(7).div(2)
					
					if (meta < upMeta)
						world.setBlockMetadataWithNotify(x, y + 1, z, meta + 1, 1 or 2)
				} else {
					/*if (world.getBlockLightValue(x, y + 1, z) >= 9)*/
					for (l in 0..3) {
						val i = x + random.nextInt(3) - 1
						val j = y + random.nextInt(4) - 3
						val k = z + random.nextInt(3) - 1
						val block = world.getBlock(i, j, k)
						if ((block === Blocks.dirt || block === Blocks.grass) && world.getBlockMetadata(i, j, k) == 0 && world.getBlockLightValue(i, j + 1, k) >= 4 && world.getBlockLightOpacity(i, j + 1, k) <= 2)
							world.setBlock(i, j, k, this)
					}
				}
			}
		} else {
			if (world.rand.nextInt(meltDelay) != 0) return
			
			if (above === AlfheimBlocks.snowLayer || above === Blocks.snow_layer)
				world.setBlockToAir(x, y + 1, z)
			
			world.setBlock(x, y, z, Blocks.grass)
		}
	}
	
	companion object {
		
		var meltDelay = 20
		val canPlaceBlockOn = ASJReflectionHelper.getMethod(BlockBush::class.java, arrayOf("canPlaceBlockOn", "func_149854_a", "a"), arrayOf(Block::class.java)).also { it.isAccessible = true }
	}
}