package alfheim.common.item.block

import alfheim.common.block.AlfheimBlocks
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World
import net.minecraftforge.common.util.*
import net.minecraftforge.event.ForgeEventFactory

class ItemBlockGrapeWhite(block: Block): ItemBlock(block) {
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		val mop = getMovingObjectPositionFromPlayer(world, player, true) ?: return stack
		
		if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			val i = mop.blockX
			val j = mop.blockY
			val k = mop.blockZ
			
			if (!world.canMineBlock(player, i, j, k)) {
				return stack
			}
			
			if (!player.canPlayerEdit(i, j, k, mop.sideHit, stack)) {
				return stack
			}
			
			if (world.getBlock(i, j, k).material === Material.water && world.getBlockMetadata(i, j, k) == 0 && world.isAirBlock(i, j + 1, k)) {
				val blocksnapshot = BlockSnapshot.getBlockSnapshot(world, i, j + 1, k)
				
				world.setBlock(i, j + 1, k, AlfheimBlocks.grapesWhite)
				
				if (ForgeEventFactory.onPlayerBlockPlace(player, blocksnapshot, ForgeDirection.UP).isCanceled) {
					blocksnapshot.restore(true, false)
					return stack
				}
				
				if (!player.capabilities.isCreativeMode) --stack.stackSize
			}
		}
		
		return stack
	}
}