package alfheim.common.block

import alexsocol.asjlib.meta
import alfheim.api.lib.LibRenderIDs
import alfheim.common.block.base.BlockContainerMod
import alfheim.common.block.tile.TileBarrel
import alfheim.common.item.material.*
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.AxisAlignedBB
import net.minecraft.world.*

class BlockBarrel: BlockContainerMod(Material.wood) {
	
	init {
		setBlockName("barrel")
		setLightOpacity(0)
		setStepSound(Block.soundTypeWood)
	}
	
	override fun onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		val tile = world.getTileEntity(x, y, z) as? TileBarrel ?: return false
		val stack = player.heldItem
		
		if (stack == null) {
			if (player.isSneaking) {
				tile.closed = !tile.closed
				return true
			}
			
			return false
		}
		
		if (!tile.closed && stack.item is ItemElvenFood && (stack.meta == ElvenFoodMetas.GreenGrapes || stack.meta == ElvenFoodMetas.RedGrapes)) {
			if (tile.vineStage == 0 && tile.vineType == TileBarrel.VINE_TYPE_NONE) {
				tile.vineStage = TileBarrel.VINE_STAGE_GRAPE
				tile.vineType = stack.meta
			} else if (!(tile.vineStage == TileBarrel.VINE_STAGE_GRAPE && tile.vineType == stack.meta && tile.vineLevel < TileBarrel.MAX_VINE_LEVEL))
				return false
			
			tile.vineLevel++
			stack.stackSize--
			
			return true
		}
		
		return false
	}
	
	override fun createNewTileEntity(world: World, meta: Int) = TileBarrel()
	
	override fun renderAsNormalBlock() = false
	override fun isOpaqueCube() = false
	override fun getRenderType() = LibRenderIDs.idBarrel
	
	override fun setBlockBoundsBasedOnState(world: IBlockAccess?, x: Int, y: Int, z: Int) {
		setBlockBounds(0f, 0f, 0f, 1f, 1f, 1f)
		super.setBlockBoundsBasedOnState(world, x, y, z)
	}
	
	override fun addCollisionBoxesToList(world: World?, x: Int, y: Int, z: Int, aabb: AxisAlignedBB?, list: MutableList<Any?>?, entity: Entity?) {
		val tile = world?.getTileEntity(x, y, z) as? TileBarrel
		
		val f = 2f / 16f
		
		setBlockBounds(0f, 0f, 0f, 1f, f, 1f)
		super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity)
		
		if (tile != null && tile.closed) {
			setBlockBounds(0f, 1 - f, 0f, 1f, 1f, 1f)
			super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity)
		}
		
		setBlockBounds(0f, 0f, 0f, f, 1f, 1f)
		super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity)
		
		setBlockBounds(0f, 0f, 0f, 1f, 1f, f)
		super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity)
		
		setBlockBounds(1 - f, 0f, 0f, 1f, 1f, 1f)
		super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity)
		
		setBlockBounds(0f, 0f, 1 - f, 1f, 1f, 1f)
		super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity)
	}
}
