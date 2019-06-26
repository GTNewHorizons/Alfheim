package alfheim.common.world.dim.alfheim.customgens

import java.util.Random

import alexsocol.asjlib.ASJUtilities
import alfheim.common.integration.thaumcraft.ThaumcraftAlfheimModule
import cpw.mods.fml.common.IWorldGenerator
import net.minecraft.block.Block
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.feature.WorldGenMinable
import thaumcraft.api.aspects.Aspect
import thaumcraft.common.config.Config
import thaumcraft.common.lib.world.biomes.BiomeHandler
import vazkii.botania.common.block.ModBlocks

class WorldGenAlfheimThaumOre: IWorldGenerator {
	
	override fun generate(random: Random, chunkX: Int, chunkZ: Int, world: World, chunkGenerator: IChunkProvider, chunkProvider: IChunkProvider) {
		generateOres(world, random, chunkX, chunkZ)
	}
	
	private fun generateOres(world: World, random: Random, chunkX: Int, chunkZ: Int) {
		var i: Int
		var randPosX: Int
		var randPosZ: Int
		var randPosY: Int
		var md: Block?
		if (Config.genCinnibar) {
			i = 0
			while (i < 18) {
				randPosX = chunkX * 16 + random.nextInt(16)
				randPosZ = random.nextInt(world.height / 5)
				randPosY = chunkZ * 16 + random.nextInt(16)
				md = world.getBlock(randPosX, randPosZ, randPosY)
				if (md != null && md.isReplaceableOreGen(world, randPosX, randPosZ, randPosY, ModBlocks.livingrock)) {
					world.setBlock(randPosX, randPosZ, randPosY, ThaumcraftAlfheimModule.alfheimThaumOre, 0, 0)
				}
				++i
			}
		}
		
		if (Config.genAmber) {
			i = 0
			while (i < 20) {
				randPosX = chunkX * 16 + random.nextInt(16)
				randPosZ = chunkZ * 16 + random.nextInt(16)
				randPosY = world.getHeightValue(randPosX, randPosZ) - random.nextInt(25)
				md = world.getBlock(randPosX, randPosY, randPosZ)
				if (md != null && md.isReplaceableOreGen(world, randPosX, randPosY, randPosZ, ModBlocks.livingrock)) {
					world.setBlock(randPosX, randPosY, randPosZ, ThaumcraftAlfheimModule.alfheimThaumOre, 7, 2)
				}
				++i
			}
		}
		
		if (Config.genInfusedStone) {
			i = 0
			while (i < 8) {
				randPosX = chunkX * 16 + random.nextInt(16)
				randPosZ = chunkZ * 16 + random.nextInt(16)
				randPosY = random.nextInt(Math.max(5, world.getHeightValue(randPosX, randPosZ) - 5))
				var meta = random.nextInt(6) + 1
				if (random.nextInt(3) == 0) {
					val e = BiomeHandler.getRandomBiomeTag(world.getBiomeGenForCoords(randPosX, randPosZ).biomeID,
														   random)
					if (e == null) {
						meta = 1 + random.nextInt(6)
					} else if (e === Aspect.AIR) {
						meta = 1
					} else if (e === Aspect.FIRE) {
						meta = 2
					} else if (e === Aspect.WATER) {
						meta = 3
					} else if (e === Aspect.EARTH) {
						meta = 4
					} else if (e === Aspect.ORDER) {
						meta = 5
					} else if (e === Aspect.ENTROPY) {
						meta = 6
					}
				}
				
				try {
					WorldGenMinable(ThaumcraftAlfheimModule.alfheimThaumOre, meta, 6, ModBlocks.livingrock).generate(world, random, randPosX, randPosY, randPosZ)
				} catch (e: Exception) {
					ASJUtilities.error("Something went wrong while generating Thaumcraft ores in Alfheim:")
					e.printStackTrace()
				}
				
				++i
			}
		}
	}
}
