package alfheim.common.item

import alfheim.api.item.*
import alfheim.common.core.util.D
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ChunkCoordinates
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.common.core.helper.ItemNBTHelper.*
import kotlin.math.*

class ItemTriquetrum: ItemMod("Triquetrum"), IDoubleBoundItem, IRotationDisplay {
	
	init {
		maxStackSize = 1
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		if (player.isSneaking) {
			setFirstPosition(stack, 0, -1, 0)
			setSecondPosition(stack, 0, -1, 0)
		} else {
			setInt(stack, TAG_ROTATION, (getInt(stack, TAG_ROTATION, 0) + 1) % 4)
		}
		
		return stack
	}
	
	override fun onItemUse(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hX: Float, hY: Float, hZ: Float): Boolean {
		val first = getFirstPosition(stack)
		val second = getSecondPosition(stack)
		
		when {
			first == null -> setFirstPosition(stack, x, y, z)
			second == null -> {
				val i = min(first.posX, x)
				val j = min(first.posY, y)
				val k = min(first.posZ, z)
				
				val I = max(first.posX, x)
				val J = max(first.posY, y)
				val K = max(first.posZ, z)
				
				setFirstPosition(stack, i, j, k)
				setSecondPosition(stack, I, J, K)
			}
			
			else           -> run {
				val rotation = getRotation(stack)
				if (rotation == -1) {
					world.spawnParticle("explode", x.D, y.D, z.D, 0.D, 0.D, 0.D)
					return@run
				}
				
				val dir = ForgeDirection.getOrientation(side)
				
				val fx = min(first.posX, second.posX)
				val fX = max(first.posX, second.posX)
				
				val fy = min(first.posY, second.posY)
				val fY = max(first.posY, second.posY)
				
				val fz = min(first.posZ, second.posZ)
				val fZ = max(first.posZ, second.posZ)
				
				for ((xOff, i) in (fx..fX).withIndex()) {
					for ((yOff, j) in (fy..fY).withIndex()) {
						for ((zOff, k) in (fz..fZ).withIndex()) {
							val block = world.getBlock(i, j, k)
							val meta = world.getBlockMetadata(i, j, k)
							
							var flag = false
							
							when (rotation) {
								0 -> world.setBlock(x + xOff + dir.offsetX, y + yOff + dir.offsetY, z + zOff + dir.offsetZ, block, meta, 3).also { flag = it }
								1 -> world.setBlock(x + zOff + dir.offsetX, y + yOff + dir.offsetY, z - xOff + dir.offsetZ, block, meta, 3).also { flag = it }
								2 -> world.setBlock(x - xOff + dir.offsetX, y + yOff + dir.offsetY, z - zOff + dir.offsetZ, block, meta, 3).also { flag = it }
								3 -> world.setBlock(x - zOff + dir.offsetX, y + yOff + dir.offsetY, z + xOff + dir.offsetZ, block, meta, 3).also { flag = it }
							}
							
							if (flag) world.setBlockToAir(i, j, k)
						}
					}
				}
				
				setFirstPosition(stack, 0, -1, 0)
				setSecondPosition(stack, 0, -1, 0)
			}
		}
		
		return true
	}
	
	fun setFirstPosition(stack: ItemStack, x: Int, y: Int, z: Int) {
		setInt(stack, TAG_BIND_X_1, x)
		setInt(stack, TAG_BIND_Y_1, y)
		setInt(stack, TAG_BIND_Z_1, z)
	}
	
	fun setSecondPosition(stack: ItemStack, x: Int, y: Int, z: Int) {
		setInt(stack, TAG_BIND_X_2, x)
		setInt(stack, TAG_BIND_Y_2, y)
		setInt(stack, TAG_BIND_Z_2, z)
	}
	
	override fun getFirstPosition(stack: ItemStack): ChunkCoordinates? {
		val coords = ChunkCoordinates(getInt(stack, TAG_BIND_X_1, 0), getInt(stack, TAG_BIND_Y_1, -1), getInt(stack, TAG_BIND_Z_1, 0))
		return if (coords.posY == -1) null else coords
	}
	
	override fun getSecondPosition(stack: ItemStack): ChunkCoordinates? {
		val coords = ChunkCoordinates(getInt(stack, TAG_BIND_X_2, 0), getInt(stack, TAG_BIND_Y_2, -1), getInt(stack, TAG_BIND_Z_2, 0))
		return if (coords.posY == -1) null else coords
	}
	
	override fun getRotation(stack: ItemStack): Int {
		
		if (getFirstPosition(stack) != null && getSecondPosition(stack) != null)
			return getInt(stack, TAG_ROTATION, 0)
		return -1
	}
	
	companion object {
		
		const val TAG_BIND_X_1 = "bindx1"
		const val TAG_BIND_Y_1 = "bindy1"
		const val TAG_BIND_Z_1 = "bindz1"
		const val TAG_BIND_X_2 = "bindx2"
		const val TAG_BIND_Y_2 = "bindy2"
		const val TAG_BIND_Z_2 = "bindz2"
		
		const val TAG_ROTATION = "rotation"
	}
}
