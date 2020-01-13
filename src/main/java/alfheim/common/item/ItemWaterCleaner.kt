package alfheim.common.item

import alfheim.common.core.util.AlfheimTab
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World

class ItemWaterCleaner: ItemMod("HyperpolatedBucket") {
	
	init {
		creativeTab = AlfheimTab
		maxStackSize = 1
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		val mop = getMovingObjectPositionFromPlayer(world, player, true) ?: return stack
		
		if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			val x = mop.blockX
			val y = mop.blockY
			val z = mop.blockZ
			
			val block = world.getBlock(x, y, z)
			
			for (i in (x - 2)..(x + 2))
				for (j in (y - 2)..(y + 2))
					for (k in (z - 2)..(z + 2)) {
						if (!world.canMineBlock(player, i, j, k))
							continue
						
						if (!player.canPlayerEdit(i, j, k, mop.sideHit, stack))
							continue
						
						val at = world.getBlock(i, j, k)
						if (at != block) continue
						
						val material = at.material
						val l = world.getBlockMetadata(i, j, k)
						
						if ((material === Material.lava || material === Material.water) && l == 0) {
							world.setBlockToAir(i, j, k)
							
							for (f in 0..4)
								world.spawnParticle("explode", i + Math.random(), j + Math.random(), k + Math.random(), 0.0, 0.0, 0.0)
						}
					}
		}
		
		return stack
	}
}
