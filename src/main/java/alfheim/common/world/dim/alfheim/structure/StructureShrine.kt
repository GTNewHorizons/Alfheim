package alfheim.common.world.dim.alfheim.structure

import alexsocol.asjlib.SchemaUtils
import alfheim.common.block.tile.TilePowerStone
import net.minecraft.world.World
import ru.vamig.worldengine.standardcustomgen.StructureBaseClass
import java.util.*

class StructureShrine: StructureBaseClass() {
	
	override fun generate(world: World, rand: Random, x: Int, y: Int, z: Int): Boolean {
		SchemaUtils.generate(world, x, y, z, AlfheimSchemas.shrines[rand.nextInt(AlfheimSchemas.shrines.size)])

		for (i in 0..2) {
			val tile = world.getTileEntity(x, y + i, z) as? TilePowerStone ?: continue
			tile.lock(x, y + i, z, world.provider.dimensionId)
		}

		return true
	}
}
