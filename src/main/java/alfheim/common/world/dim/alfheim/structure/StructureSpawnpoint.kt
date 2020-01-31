package alfheim.common.world.dim.alfheim.structure

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.schema.SchemaGenerator
import alfheim.common.core.handler.AlfheimConfigHandler
import net.minecraft.init.Blocks
import net.minecraft.world.World
import java.util.*

object StructureSpawnpoint {
	
	fun generate(world: World) {
		if (!AlfheimCore.enableElvenStory || AlfheimConfigHandler.bothSpawnStructures) generateSpawnCastle(world, 0, world.getTopSolidOrLiquidBlock(0, 0) + 10, 0)
		if (AlfheimCore.enableElvenStory || AlfheimConfigHandler.bothSpawnStructures) generateStartBox(world, 0, 248, 0)
		
		ASJUtilities.log("Spawn created")
	}
	
	fun generateSpawnCastle(world: World, x: Int, y: Int, z: Int) {
		world.setSpawnLocation(x, y, z)
		javaClass.getResourceAsStream("/assets/${ModInfo.MODID}/schemas/spawnpoint").use {
			SchemaGenerator.generate(world, x, y, z, it.readBytes().toString(Charsets.UTF_8))
		}
		ASJUtilities.fillGenHoles(world, if (AlfheimCore.winter) AlfheimBlocks.snowGrass else Blocks.grass, 0, x - 11, x + 11, y - 8, z - 41, z + 1, 0)
	}
	
	fun generateStartBox(world: World, x: Int, y: Int, z: Int) {
		world.setSpawnLocation(x, y + 2, z)
		SchemaGenerator.generate(world, x, y, z, AlfheimSchemas.spawnbox)
	}
}