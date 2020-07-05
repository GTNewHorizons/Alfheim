package alfheim.common.block.corporea

import alexsocol.asjlib.*
import alexsocol.asjlib.extendables.block.BlockModContainer
import alfheim.common.block.tile.corporea.TileCorporeaAutocrafter
import alfheim.common.core.util.AlfheimTab
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.*
import vazkii.botania.api.wand.IWandable
import kotlin.math.max

class BlockCorporeaAutocrafter: BlockModContainer(Material.iron), IWandable {
	
	init {
		setBlockName("CorporeaAutocrafter")
		setCreativeTab(AlfheimTab)
		setHardness(5.5f)
		setStepSound(Block.soundTypeMetal)
	}
	
	override fun onNeighborChange(world: IBlockAccess, x: Int, y: Int, z: Int, tileX: Int, tileY: Int, tileZ: Int) {
		super.onNeighborChange(world, x, y, z, tileX, tileY, tileZ)
		
		if (tileX == x && tileY == y + 1 && tileZ == z) {
			(world.getTileEntity(x, y, z) as? TileCorporeaAutocrafter)?.onFilterChange()
		}
	}
	
	override fun breakBlock(world: World, x: Int, y: Int, z: Int, block: Block?, meta: Int) {
		dropAll(world, x, y, z)
		world.func_147453_f(x, y, z, block)
		super.breakBlock(world, x, y, z, block, meta)
	}
	
	fun dropAll(world: World, x: Int, y: Int, z: Int) {
		val inv = world.getTileEntity(x, y, z) as? TileCorporeaAutocrafter ?: return
		
		for (slot in 0 until inv.sizeInventory) {
			val itemstack = inv.getStackInSlot(slot)
			
			if (itemstack != null) {
				val f = world.rand.nextFloat() * 0.8f + 0.1f
				val f1 = world.rand.nextFloat() * 0.8f + 0.1f
				val f2 = world.rand.nextFloat() * 0.8f + 0.1f
				
				var entityitem: EntityItem
				
				while (itemstack.stackSize > 0) {
					var k1: Int = world.rand.nextInt(21) + 10
					
					if (k1 > itemstack.stackSize) k1 = itemstack.stackSize
					
					itemstack.stackSize -= k1
					entityitem = EntityItem(world, (x + f).D, (y + f1).D, (z + f2).D, ItemStack(itemstack.item, k1, itemstack.getItemDamage()))
					
					val f3 = 0.05
					entityitem.motionX = world.rand.nextGaussian() * f3
					entityitem.motionY = world.rand.nextGaussian() * f3 + 0.2
					entityitem.motionZ = world.rand.nextGaussian() * f3
					
					if (itemstack.hasTagCompound()) entityitem.entityItem.tagCompound = itemstack.tagCompound.copy() as NBTTagCompound
					world.spawnEntityInWorld(entityitem)
				}
			}
		}
	}
	
	override fun createNewTileEntity(world: World?, meta: Int) = TileCorporeaAutocrafter()
	
	override fun onBlockClicked(world: World, x: Int, y: Int, z: Int, player: EntityPlayer) {
		(world.getTileEntity(x, y, z) as? TileCorporeaAutocrafter)?.let {
			it.craftResult = max(1, it.craftResult + if (player.isSneaking) -1 else 1)
			ASJUtilities.chatLog("Craft result: ${it.craftResult}")
		}
	}
	
	override fun onUsedByWand(player: EntityPlayer?, stack: ItemStack?, world: World, x: Int, y: Int, z: Int, side: Int): Boolean {
		dropAll(world, x, y, z)
		val inv = world.getTileEntity(x, y, z) as? TileCorporeaAutocrafter ?: return false
		inv.onWanded()
		return true
	}
}
