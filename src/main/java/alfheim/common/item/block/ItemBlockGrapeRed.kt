package alfheim.common.item.block

import alfheim.common.block.AlfheimBlocks
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.*
import net.minecraft.world.World

class ItemBlockGrapeRed(block: Block): ItemBlock(block) {
	
	override fun onItemUseFirst(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		if (!AlfheimBlocks.grapesRedPlanted.canBlockStay(world, x, y, z)) return false
		
		val blockAt = world.getBlock(x, y, z)
		if (blockAt !== Blocks.fence) return false
		
		world.setBlock(x, y, z, AlfheimBlocks.grapesRedPlanted, 0, 3)
		world.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, field_150939_a.stepSound.soundName, (field_150939_a.stepSound.volume + 1.0f) / 2.0f, field_150939_a.stepSound.pitch * 0.8f)
		
		if (!player.capabilities.isCreativeMode) --stack.stackSize
		
		return false
	}
	
	override fun func_150936_a(world: World, x: Int, y: Int, z: Int, side: Int, player: EntityPlayer?, stack: ItemStack) = true
}
