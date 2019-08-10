package alfheim.common.world.dim.alfheim.structure

import alexsocol.asjlib.ASJUtilities
import alfheim.common.block.schema.SchemaGenerator
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.core.util.AlfheimConfig
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.PotionEffect
import net.minecraft.world.World
import java.util.*

class StructureShrine: StructureBaseClass() {
	
	override fun generate(world: World, rand: Random, x: Int, y: Int, z: Int) =
		SchemaGenerator.generate(world, x, y, z, AlfheimSchemas.shrines[rand.nextInt(AlfheimSchemas.shrines.size)]).let {
			if (AlfheimConfig.detectShrines) {
				ASJUtilities.sayToAllOnline("Shrine at: $x $y $z")
				
				world.playerEntities.forEach {
					it as EntityPlayer
					it.setPositionAndUpdate(x.toDouble(), y + 2.0, z.toDouble())
					it.addPotionEffect(PotionEffect(AlfheimRegistry.eternity.id, 200, 200))
				}
			}
			
			true
		}
}
