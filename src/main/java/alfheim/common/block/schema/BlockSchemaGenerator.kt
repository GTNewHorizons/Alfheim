package alfheim.common.block.schema

import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.colored.BlockColoredSapling
import alfheim.common.core.handler.AlfheimConfigHandler
import cpw.mods.fml.relauncher.FMLInjectionData
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.world.World
import java.io.File
import java.util.*

class BlockSchemaGenerator: BlockColoredSapling(name = "schematicOak") {
	
	override fun growTree(world: World?, x: Int, y: Int, z: Int, random: Random?) {
		if (world != null) {
			val plantedOn: Block = world.getBlock(x, y - 1, z)
			
			if (canGrowHere(plantedOn)) {
				val l = if (plantedOn === AlfheimBlocks.irisDirt) world.getBlockMetadata(x, y - 1, z) else -1
				
				if (l in AlfheimConfigHandler.schemaArray) {
					val schemaText = getSchema(l)
					
					if (schemaText != null) {
						SchemaGenerator.generate(world, x, y, z, schemaText)
					}
				}
			}
		}
	}
	
	override fun canGrowHere(block: Block) = block.material == Material.ground || block.material == Material.grass
	
	fun getSchema(meta: Int = -1): String? {
		val e = File(FMLInjectionData.data()[6] as File, "config/Alfheim/schemas/schema${if (meta < 0) "" else "_$meta"}.txt")
		if (!e.parentFile.exists()) e.parentFile.mkdirs()
		
		return if (e.exists()) e.readText() else null
	}
}