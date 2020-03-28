package alfheim.common.item

import alexsocol.asjlib.meta
import alfheim.common.core.util.AlfheimTab
import alfheim.common.security.InteractionSecurity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World

class ItemHyperBucket: ItemMod("HyperpolatedBucket") {
	
	init {
		creativeTab = AlfheimTab
		maxStackSize = 1
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		if (world.isRemote) return stack
		
		val mop = getMovingObjectPositionFromPlayer(world, player, true) ?: return stack
		
		if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			val x = mop.blockX
			val y = mop.blockY
			val z = mop.blockZ
			
			val block = world.getBlock(x, y, z)
			val range = stack.meta + 1
			
			for (j in ((y - range)..(y + range)).reversed())
				for (i in (x - range)..(x + range))
					for (k in (z - range)..(z + range)) {
						if (!world.canMineBlock(player, i, j, k)) continue
						
						val at = world.getBlock(i, j, k)
						
						if (block === Blocks.lava && at === Blocks.flowing_lava); else
						if (block === Blocks.flowing_lava && at === Blocks.lava); else
						if (block === Blocks.water && at === Blocks.flowing_water); else
						if (block === Blocks.flowing_water && at === Blocks.water); else
						if (at !== block) continue
						
						val material = at.material
						val l = world.getBlockMetadata(i, j, k)
						
						if (material.isLiquid && l == 0) {
							if (!InteractionSecurity.canDoSomethingHere(player, i, j, k, world)) continue
							
							world.setBlockToAir(i, j, k)
							
							for (f in 0..4)
								world.spawnParticle("explode", i + Math.random(), j + Math.random(), k + Math.random(), 0.0, 0.0, 0.0)
						}
					}
		}
		
		return stack
	}
}
