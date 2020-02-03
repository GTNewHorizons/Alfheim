package alfheim.common.world.dim.alfheim.structure

import net.minecraft.world.World
import java.util.*

abstract class StructureBaseClass {
	
	abstract fun generate(world: World, rand: Random, x: Int, y: Int, z: Int): Boolean
}