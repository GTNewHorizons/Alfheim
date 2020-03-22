package alfheim.common.block.magtrees.calico

import net.minecraft.world.*

interface IExplosionDampener {
	
	/**
	 * Make sure to remove explosion processing
	 */
	fun onBlockExploded(world: World, x: Int, y: Int, z: Int, explosion: Explosion)
}