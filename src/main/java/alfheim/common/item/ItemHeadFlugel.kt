package alfheim.common.item

import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.util.AlfheimTab
import net.minecraft.block.BlockSkull
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntitySkull
import net.minecraft.util.MathHelper
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

class ItemHeadFlugel: ItemMod("FlugelHead") {

	init {
		creativeTab = AlfheimTab
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World?, player: EntityPlayer): ItemStack {
		Blocks.pumpkin
		if (player.getCurrentArmor(3) == null) player.setCurrentItemOrArmor(4, stack.splitStack(1))
		return stack
	}
	
	// > ItemGaiaHead#onItemUse:
	// I couldn't deal with it. ~Vazkii
	// :thinking: ~AlexSocol
	override fun onItemUse(stack: ItemStack?, player: EntityPlayer?, world: World, x: Int, y: Int, z: Int, side: Int, sideX: Float, sideY: Float, sideZ: Float): Boolean {
		var x = x
		var y = y
		var z = z
		// The side of the wall the head is being used on.
		var sideDir = ForgeDirection.getOrientation(side)
		
		// If we can replace the block we're clicking on, then we'll go ahead
		// and replace it (eg, snow).
		if (world.getBlock(x, y, z).isReplaceable(world, x, y, z) && sideDir != ForgeDirection.DOWN) {
			sideDir = ForgeDirection.UP
			y--
		}
		
		// Skulls can't be placed on the bottom side of a block.
		if (sideDir == ForgeDirection.DOWN)
			return false
		
		// If the side we're trying to place the skull on isn't solid, then
		// we can't place it either.
		if (!world.isSideSolid(x, y, z, sideDir))
			return false
		
		// Figure out where the skull actually goes based on the side we're placing it against.
		when (sideDir) {
			ForgeDirection.UP    -> y++
			ForgeDirection.NORTH -> z--
			ForgeDirection.SOUTH -> z++
			ForgeDirection.WEST  -> x--
			ForgeDirection.EAST  -> x++
			else                 -> return false // Oops, this shouldn't happen.
		}// If we're placing it on the top, then the skull goes 1 block above.
		// Placing it on the north side (Z- axis).
		// Placing it on the south side (Z+ axis).
		// Placing it on the west side (X- axis).
		// Placing it on the east side (X+ axis).
		
		// We can't place blocks as a measly client.
		if (world.isRemote)
			return true
		
		// If the skull says no, who are we to argue?
		if (!AlfheimBlocks.flugelHeadBlock.canPlaceBlockOnSide(world, x, y, z, side))
			return false
		
		// Flugel head, instead of skull
		world.setBlock(x, y, z, AlfheimBlocks.flugelHeadBlock, sideDir.ordinal, 2)
		var headAngle = 0
		
		// If we place the skull on top of a block, we should also make it
		// face the player by rotating it.
		if (sideDir == ForgeDirection.UP)
			headAngle = MathHelper.floor_double(player!!.rotationYaw * 16.0f / 360.0f + 0.5) and 15
		
		// Update the skull's orientation if it lets us.
		val tileentity = world.getTileEntity(x, y, z)
		
		if (tileentity is TileEntitySkull) {
			tileentity.func_145903_a(headAngle)
			(Blocks.skull as BlockSkull).func_149965_a(world, x, y, z, tileentity)
		}
		
		// Remove a head from the stack.
		--stack!!.stackSize
		
		// Call it a success and leave.
		return true
	}
	
	override fun isValidArmor(stack: ItemStack, armorType: Int, entity: Entity) = armorType == 0
}
