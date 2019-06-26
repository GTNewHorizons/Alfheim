package alfheim.common.world.dim.alfheim.struct

import java.util.Random

import net.minecraft.world.World

abstract class StructureBaseClass {
	
	abstract fun generate(world: World, rand: Random, x: Int, y: Int, z: Int): Boolean
}