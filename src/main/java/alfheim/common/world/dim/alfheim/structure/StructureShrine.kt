package alfheim.common.world.dim.alfheim.structure

import alfheim.common.block.schema.SchemaGenerator
import net.minecraft.world.World
import java.util.*

class StructureShrine: StructureBaseClass() {
	
	override fun generate(world: World, rand: Random, x: Int, y: Int, z: Int) =
		SchemaGenerator.generate(world, x, y, z, AlfheimSchemas.shrines[rand.nextInt(AlfheimSchemas.shrines.size)]).let { true }
}
