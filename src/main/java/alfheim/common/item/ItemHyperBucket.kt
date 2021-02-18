package alfheim.common.item

import alexsocol.asjlib.meta
import alexsocol.asjlib.security.InteractionSecurity
import alfheim.client.gui.ItemsRemainingRenderHandler
import alfheim.common.core.util.AlfheimTab
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World
import vazkii.botania.common.core.helper.ItemNBTHelper

class ItemHyperBucket: ItemMod("HyperpolatedBucket") {
	
	init {
		creativeTab = AlfheimTab
		maxStackSize = 1
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		if (world.isRemote) return stack
		
		if (player.isSneaking) {
			setRange(stack, (getRange(stack) + 1) % (getMaxRange(stack) + 1))
			val r = getRange(stack) * 2 + 1
			ItemsRemainingRenderHandler[stack] = "${r}x$r"
			return stack
		}
		
		val mop = getMovingObjectPositionFromPlayer(world, player, true) ?: return stack
		
		if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			val x = mop.blockX
			val y = mop.blockY
			val z = mop.blockZ
			
			val block = world.getBlock(x, y, z)
			val range = getRange(stack)
			
			for (j in y.inRange(range).reversed())
				for (i in x.inRange(range))
					for (k in z.inRange(range)) {
						if (!world.canMineBlock(player, i, j, k)) continue
						
						val at = world.getBlock(i, j, k)
						
						if (block === Blocks.lava && at === Blocks.flowing_lava) ; else
							if (block === Blocks.flowing_lava && at === Blocks.lava) ; else
								if (block === Blocks.water && at === Blocks.flowing_water) ; else
									if (block === Blocks.flowing_water && at === Blocks.water) ; else
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
	
	override fun addInformation(stack: ItemStack, player: EntityPlayer?, tooltip: MutableList<Any?>, adv: Boolean) {
		val r = getRange(stack) * 2 + 1
		tooltip.add("$r x $r")
	}
	
	companion object {
		
		const val TAG_RANGE = "range"
		
		private fun Int.inRange(range: Int) = (this - range)..(this + range)
		
		fun getMaxRange(stack: ItemStack) = stack.meta + 1
		
		fun getRange(stack: ItemStack) = ItemNBTHelper.getInt(stack, TAG_RANGE, getMaxRange(stack))
		
		fun setRange(stack: ItemStack, range: Int) = ItemNBTHelper.setInt(stack, TAG_RANGE, range)
	}
}
