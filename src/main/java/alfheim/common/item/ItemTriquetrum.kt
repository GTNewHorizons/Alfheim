package alfheim.common.item

import alexsocol.asjlib.math.Vector3
import alfheim.api.item.*
import alfheim.common.core.util.*
import net.minecraft.block.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.common.core.helper.ItemNBTHelper.*
import kotlin.math.*

class ItemTriquetrum: ItemMod("Triquetrum"), IDoubleBoundItem, IRotationDisplay {
	
	init {
		maxStackSize = 1
	}
	
	override fun addInformation(stack: ItemStack, player: EntityPlayer, list: MutableList<Any?>, adv: Boolean) {
		val first = getFirstPosition(stack) ?: return
		val second = getSecondPosition(stack) ?: return
		
		val v = Vector3(first.posX, first.posY, first.posZ).add(second.posX, second.posY, second.posZ)
		list.add(StatCollector.translateToLocalFormatted("item.Triquetrum.blocks", abs(v.x * v.y * v.z).I))
		list.add(StatCollector.translateToLocalFormatted("item.Triquetrum.rotation", getRotation(stack) * 90))
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
		if (player.isSneaking) return false
		
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
							val nbt = NBTTagCompound()
							
							if (block is ITileEntityProvider) world.getTileEntity(i, j, k)?.writeToNBT(nbt)
							
							fun setBlockTile(world: World, x: Int, y: Int, z: Int, block: Block, meta: Int, cmp: NBTTagCompound): Boolean {
								if (world.setBlock(x, y, z, block, meta, 3)) {
									if (block is ITileEntityProvider) {
										val tile = TileEntity.createAndLoadEntity(cmp)
										tile.xCoord = x
										tile.yCoord = y
										tile.zCoord = z
										world.setTileEntity(x, y, z, tile)
									}
									
									return true
								}
								return false
							}
							
							when (rotation) {
								0 -> flag = setBlockTile(world, x + xOff + dir.offsetX, y + yOff + dir.offsetY, z + zOff + dir.offsetZ, block, meta, nbt)
								1 -> flag = setBlockTile(world, x + zOff + dir.offsetX, y + yOff + dir.offsetY, z - xOff + dir.offsetZ, block, meta, nbt)
								2 -> flag = setBlockTile(world, x - xOff + dir.offsetX, y + yOff + dir.offsetY, z - zOff + dir.offsetZ, block, meta, nbt)
								3 -> flag = setBlockTile(world, x - zOff + dir.offsetX, y + yOff + dir.offsetY, z + xOff + dir.offsetZ, block, meta, nbt)
							}
							
							if (flag) {
								if (block is ITileEntityProvider) world.removeTileEntity(i, j, k)
								world.setBlockToAir(i, j, k)
							}
						}
					}
				}
				
				setFirstPosition(stack, 0, -1, 0)
				setSecondPosition(stack, 0, -1, 0)
				setInt(stack, TAG_ROTATION, 0)
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
