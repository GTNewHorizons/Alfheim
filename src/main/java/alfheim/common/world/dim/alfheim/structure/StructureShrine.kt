package alfheim.common.world.dim.alfheim.structure

import alexsocol.asjlib.SchemaUtils
import net.minecraft.world.World
import java.util.*

class StructureShrine: StructureBaseClass() {
	
	override fun generate(world: World, rand: Random, x: Int, y: Int, z: Int) =
		SchemaUtils.generate(world, x, y, z, AlfheimSchemas.shrines[rand.nextInt(AlfheimSchemas.shrines.size)]).let { true }
}
