package alfheim.common.item

import alexsocol.asjlib.mfloor
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.util.AlfheimTab
import net.minecraft.block.BlockSkull
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntitySkull
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
	
	override fun onItemUse(stack: ItemStack, player: EntityPlayer?, world: World, x: Int, y: Int, z: Int, side: Int, sideX: Float, sideY: Float, sideZ: Float): Boolean {
		var i = x
		var j = y
		var k = z
		
		var dir = ForgeDirection.getOrientation(side)
		
		if (world.getBlock(i, j, k).isReplaceable(world, i, j, k) && dir != ForgeDirection.DOWN) {
			dir = ForgeDirection.UP
			j--
		}
		
		if (dir == ForgeDirection.DOWN)
			return false
		
		if (!world.isSideSolid(i, j, k, dir))
			return false
		
		when (dir) {
			ForgeDirection.UP    -> j++
			ForgeDirection.NORTH -> k--
			ForgeDirection.SOUTH -> k++
			ForgeDirection.WEST  -> i--
			ForgeDirection.EAST  -> i++
			else                 -> return false
		}
		
		if (world.isRemote)
			return true
		
		if (!AlfheimBlocks.flugelHeadBlock.canPlaceBlockOnSide(world, i, j, k, side))
			return false
		
		world.setBlock(i, j, k, AlfheimBlocks.flugelHeadBlock, dir.ordinal, 2)
		var headAngle = 0
		
		if (dir == ForgeDirection.UP)
			headAngle = (player!!.rotationYaw * 16f / 360f + 0.5).mfloor() and 15
		
		val tileentity = world.getTileEntity(i, j, k)
		
		if (tileentity is TileEntitySkull) {
			tileentity.func_145903_a(headAngle)
			(Blocks.skull as BlockSkull).func_149965_a(world, i, j, k, tileentity)
		}
		
		--stack.stackSize
		
		return true
	}
	
	override fun isValidArmor(stack: ItemStack, armorType: Int, entity: Entity) = armorType == 0
}
