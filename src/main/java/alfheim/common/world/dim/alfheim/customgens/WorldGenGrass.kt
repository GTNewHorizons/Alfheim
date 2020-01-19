package alfheim.common.world.dim.alfheim.customgens

import alfheim.AlfheimCore
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.util.inn
import cpw.mods.fml.common.IWorldGenerator
import net.minecraft.block.*
import net.minecraft.init.Blocks
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import vazkii.botania.common.block.*
import vazkii.botania.common.block.subtile.generating.SubTileDaybloom
import vazkii.botania.common.block.tile.TileSpecialFlower
import vazkii.botania.common.core.handler.ConfigHandler
import vazkii.botania.common.lib.LibBlockNames
import java.util.*
import kotlin.math.*

class WorldGenGrass(val grass: Boolean, val flowers: Boolean, val doubleFlowers: Boolean, val botanicalFlowers: Boolean, val mod: Double): IWorldGenerator {
	
	val G = if (AlfheimCore.winter) arrayOf(AlfheimBlocks.snowGrass, Blocks.grass) else arrayOf(Blocks.grass)
	
	override fun generate(rand: Random, chunkX: Int, chunkZ: Int, world: World, chunkGenerator: IChunkProvider, chunkProvider: IChunkProvider) {
		val cx = chunkX * 16
		val cz = chunkZ * 16
		if (botanicalFlowers) {
			val dist = min(8, max(1, ConfigHandler.flowerPatchSize))
			for (i in 0 until ConfigHandler.flowerQuantity)
				if (rand.nextInt((ConfigHandler.flowerPatchChance / mod).roundToLong().toInt()) == 0) {
					val x = cx + rand.nextInt(16)
					val z = cz + rand.nextInt(16)
					val y = world.getTopSolidOrLiquidBlock(x, z)
					val color = rand.nextInt(17)
					val primus = rand.nextInt(380) == 0
					for (j in 0 until ConfigHandler.flowerDensity * ConfigHandler.flowerPatchChance) {
						val x1 = x + rand.nextInt(dist * 2) - dist
						val z1 = z + rand.nextInt(dist * 2) - dist
						if (world.isAirBlock(x1, y, z1) && world.getBlock(x1, y - 1, z1) inn G)
							if (primus) {
								world.setBlock(x1, y, z1, ModBlocks.specialFlower, 0, 2)
								val flower = world.getTileEntity(x1, y, z1) as TileSpecialFlower
								flower.setSubTile(if (rand.nextBoolean()) LibBlockNames.SUBTILE_NIGHTSHADE_PRIME else LibBlockNames.SUBTILE_DAYBLOOM_PRIME)
								val subtile = flower.subTile as SubTileDaybloom
								subtile.setPrimusPosition()
							} else {
								val rainbow = color == 16
								
								world.setBlock(x1, y, z1, if (rainbow) AlfheimBlocks.rainbowGrass else ModBlocks.flower, if (rainbow) 2 else color, 2)
								// `can place` condition start
								if (rand.nextDouble() < ConfigHandler.flowerTallChance
									&& (
										if (rainbow)
											(AlfheimBlocks.rainbowGrass as IGrowable).func_149851_a(world, x1, y, z1, false)
										else
											(ModBlocks.flower as BlockModFlower).func_149851_a(world, x1, y, z1, false)
									   ))
								// `can place` condition end
									if (rainbow) {
										world.setBlock(x1, y, z1, AlfheimBlocks.rainbowTallFlower, 0, 0)
										world.setBlock(x1, y + 1, z1, AlfheimBlocks.rainbowTallFlower, 8, 0)
									} else
										BlockModFlower.placeDoubleFlower(world, x1, y, z1, color, 0)
							}
					}
				}
		}
		
		for (i in 0 until ConfigHandler.mushroomQuantity) {
			val x = cx + rand.nextInt(16)
			val z = cz + rand.nextInt(16)
			val y = rand.nextInt(28) + 4
			val color = rand.nextInt(17)
			val rainbow = color == 16
			
			// `can place` condition start
			if (world.isAirBlock(x, y, z)
				&& (
					if (rainbow)
						AlfheimBlocks.rainbowMushroom.canBlockStay(world, x, y, z)
					else
						ModBlocks.mushroom.canBlockStay(world, x, y, z)
				   )) {
				// `can place` condition end
				world.setBlock(x, y, z, if (rainbow) AlfheimBlocks.rainbowMushroom else ModBlocks.mushroom, if (rainbow) 3 else color, 2)
			}
		}
		
		var perChunk = (64 * mod).roundToLong().toInt()
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
			if (!world.isAirBlock(x, y, z) || !(world.getBlock(x, y - 1, z) inn G)) continue
			
			val type = rand.nextInt(20)
			
			run loop@{
				run select@{
					if (type > 12) return@loop
					if (type > 10 && !doubleFlowers) return@select
					if (type < 5 && !flowers) return@select
					if (type in 5..10 && !grass)
						return@select
					else if (type == 4)
						metas[4] = (rand.nextInt(8) + 1).toByte()
					else if (type == 11)
						world.setBlock(x, y + 1, z, types[11], metas[11] + 8, 2)
					else if (type == 12) {
						metas[12] = rand.nextInt(6).toByte()
						if (metas[12].toInt() == 2) return@loop
						world.setBlock(x, y + 1, z, types[12], metas[12] + 8, 2)
					}
					
					world.setBlock(x, y, z, types[type], metas[type].toInt(), 2)
				}
				--perChunk
			}
		}
	}
}