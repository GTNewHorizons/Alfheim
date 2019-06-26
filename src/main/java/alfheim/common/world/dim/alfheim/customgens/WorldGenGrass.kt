package alfheim.common.world.dim.alfheim.customgens

import cpw.mods.fml.common.IWorldGenerator
import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import vazkii.botania.common.block.*
import vazkii.botania.common.block.subtile.generating.SubTileDaybloom
import vazkii.botania.common.block.tile.TileSpecialFlower
import vazkii.botania.common.core.handler.ConfigHandler
import vazkii.botania.common.lib.LibBlockNames

import java.util.Random

class WorldGenGrass(val grass: Boolean, val flowers: Boolean, val doubleFlowers: Boolean, val botanicalFlowers: Boolean, val mod: Double): IWorldGenerator {
	
	override fun generate(rand: Random, chunkX: Int, chunkZ: Int, world: World, chunkGenerator: IChunkProvider, chunkProvider: IChunkProvider) {
		
		val cx = chunkX * 16
		val cz = chunkZ * 16
		if (botanicalFlowers) {
			val dist = Math.min(8, Math.max(1, ConfigHandler.flowerPatchSize))
			for (i in 0 until ConfigHandler.flowerQuantity)
				if (rand.nextInt(Math.round(ConfigHandler.flowerPatchChance / mod).toInt()) == 0) {
					val x = cx + rand.nextInt(16)
					val z = cz + rand.nextInt(16)
					val y = world.getTopSolidOrLiquidBlock(x, z)
					val color = rand.nextInt(16)
					val primus = rand.nextInt(380) == 0
					for (j in 0 until ConfigHandler.flowerDensity * ConfigHandler.flowerPatchChance) {
						val x1 = x + rand.nextInt(dist * 2) - dist
						val z1 = z + rand.nextInt(dist * 2) - dist
						if (world.isAirBlock(x1, y, z1) && world.getBlock(x1, y - 1, z1) === Blocks.grass)
							if (primus) {
								world.setBlock(x1, y, z1, ModBlocks.specialFlower, 0, 2)
								val flower = world.getTileEntity(x1, y, z1) as TileSpecialFlower
								flower.setSubTile(if (rand.nextBoolean()) LibBlockNames.SUBTILE_NIGHTSHADE_PRIME else LibBlockNames.SUBTILE_DAYBLOOM_PRIME)
								val subtile = flower.subTile as SubTileDaybloom
								subtile.setPrimusPosition()
							} else {
								world.setBlock(x1, y, z1, ModBlocks.flower, color, 2)
								if (rand.nextDouble() < ConfigHandler.flowerTallChance && (ModBlocks.flower as BlockModFlower).func_149851_a(world, x1, y, z1, false)) BlockModFlower.placeDoubleFlower(world, x1, y, z1, color, 0)
							}
					}
				}
		}
		
		for (i in 0 until ConfigHandler.mushroomQuantity) {
			val x = cx + rand.nextInt(16)
			val z = cz + rand.nextInt(16)
			val y = rand.nextInt(28) + 4
			val color = rand.nextInt(16)
			if (world.isAirBlock(x, y, z) && ModBlocks.mushroom.canBlockStay(world, x, y, z))
				world.setBlock(x, y, z, ModBlocks.mushroom, color, 2)
		}
		
		var perChunk = Math.round(64 * mod).toInt()
		var iteration = 256
		
		val types = arrayOf<Block>(Blocks.yellow_flower, Blocks.yellow_flower, // 0 1
								   Blocks.red_flower, Blocks.red_flower, Blocks.red_flower, // 2 3 4
								   Blocks.tallgrass, Blocks.tallgrass, Blocks.tallgrass, Blocks.tallgrass, Blocks.tallgrass, Blocks.tallgrass, // 5 6 7 8 9 10
								   Blocks.double_plant, Blocks.double_plant                                                                    // 11 12
		)
		
		val metas = byteArrayOf(0, 0, 0, 0, -1, 0, 2, 1, 1, 1, 1, 2, -1)
		
		while (perChunk > 0 && iteration > 0) {
			--iteration
			val x = cx + rand.nextInt(16)
			val z = cz + rand.nextInt(16)
			val y = world.getTopSolidOrLiquidBlock(x, z)
			if (!world.isAirBlock(x, y, z) || world.getBlock(x, y - 1, z) !== Blocks.grass) continue
			
			val type = rand.nextInt(20)
			select@ run {
				if (type > 12) continue
				if (type > 10 && !doubleFlowers) break@select
				if (type < 5 && !flowers) break@select
				if (4 < type && type < 11 && !grass)
					break@select
				else if (type == 4)
					metas[4] = (rand.nextInt(8) + 1).toByte()
				else if (type == 11)
					world.setBlock(x, y + 1, z, types[11], metas[11] + 8, 2)
				else if (type == 12) {
					metas[12] = rand.nextInt(6).toByte()
					if (metas[12].toInt() == 2) continue
					world.setBlock(x, y + 1, z, types[12], metas[12] + 8, 2)
				}
				
				world.setBlock(x, y, z, types[type], metas[type].toInt(), 2)
			}
			--perChunk
		}
	}
}