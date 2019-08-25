package alfheim.common.item

import alexsocol.asjlib.math.Vector3
import alfheim.client.gui.ItemsRemainingRenderHandler
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.api.item.IBlockProvider
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.item.equipment.tool.ToolCommons
import vazkii.botania.common.item.rod.ItemExchangeRod
import java.util.*
import kotlin.math.floor

class ItemAstrolabe: ItemMod("Astrolabe") {

	init {
		maxStackSize = 1
	}
	
	override fun onItemUse(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		val block = world.getBlock(x, y, z)
		val meta = world.getBlockMetadata(x, y, z)
		
		if (player.isSneaking) {
			if (setBlock(stack, block, meta)) {
				displayRemainderCounter(player, stack)
				return true
			}
		} else {
			val did = placeAllBlocks(stack, player)
			if (did) {
				displayRemainderCounter(player, stack)
				if (!world.isRemote)
					player.swingItem()
			}
			
			return did
		}
		
		return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ)
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World?, player: EntityPlayer): ItemStack {
		if (player.isSneaking) {
			val size = getSize(stack)
			val newSize = if (size == 11) 3 else size + 2
			setSize(stack, newSize)
			ItemsRemainingRenderHandler[stack] = newSize.toString() + "x" + newSize
			
			world!!.playSoundAtEntity(player, "random.orb", 0.1f, 0.5f * ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7f + 1.8f))
		}
		
		return stack
	}
	
	fun placeAllBlocks(stack: ItemStack, player: EntityPlayer): Boolean {
		val blocksToPlace = getBlocksToPlace(stack, player)
		if (!hasBlocks(stack, player, blocksToPlace))
			return false
		
		val size = getSize(stack)
		val cost = size * 320
		if (!ManaItemHandler.requestManaExact(stack, player, cost, false))
			return false
		
		val stackToPlace = ItemStack(getBlock(stack), 1, getBlockMeta(stack))
		for (v in blocksToPlace) placeBlockAndConsume(player, stack, stackToPlace, v.x.toInt(), v.y.toInt(), v.z.toInt())
		ManaItemHandler.requestManaExact(stack, player, cost, true)
		
		return true
	}
	
	private fun placeBlockAndConsume(player: EntityPlayer, requestor: ItemStack, blockToPlace: ItemStack, x: Int, y: Int, z: Int) {
		if (blockToPlace.item == null)
			return
		
		val block = Block.getBlockFromItem(blockToPlace.item)
		val meta = blockToPlace.itemDamage
		player.worldObj.setBlock(x, y, z, block, meta, 3)
		player.worldObj.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block))
		
		if (player.capabilities.isCreativeMode) return
		
		val stacksToCheck = ArrayList<ItemStack>()
		for (i in 0 until player.inventory.sizeInventory) {
			val stackInSlot = player.inventory.getStackInSlot(i)
			if (stackInSlot != null && stackInSlot.stackSize > 0 && stackInSlot.item === blockToPlace.item && stackInSlot.itemDamage == blockToPlace.itemDamage) {
				stackInSlot.stackSize--
				return
			}
			
			if (stackInSlot != null && stackInSlot.stackSize > 0 && stackInSlot.item is IBlockProvider)
				stacksToCheck.add(stackInSlot)
		}
		
		for (providerStack in stacksToCheck) {
			val prov = providerStack.item as IBlockProvider
			
			if (prov.provideBlock(player, requestor, providerStack, block, meta, false)) {
				prov.provideBlock(player, requestor, providerStack, block, meta, true)
				return
			}
		}
	}
	
	fun displayRemainderCounter(player: EntityPlayer, stack: ItemStack) {
		val block = getBlock(stack)
		val meta = getBlockMeta(stack)
		val count = ItemExchangeRod.getInventoryItemCount(player, stack, block, meta)
		if (!player.worldObj.isRemote) ItemsRemainingRenderHandler.set(ItemStack(block, 1, meta), count)
	}
	
	private fun setBlock(stack: ItemStack?, block: Block, meta: Int): Boolean {
		if (block !== Blocks.air) {
			ItemNBTHelper.setString(stack!!, TAG_BLOCK_NAME, Block.blockRegistry.getNameForObject(block))
			ItemNBTHelper.setInt(stack, TAG_BLOCK_META, meta)
			return true
		}
		return false
	}
	
	@SideOnly(Side.CLIENT)
	override fun addInformation(par1ItemStack: ItemStack?, player: EntityPlayer?, par3List: MutableList<Any?>, flags: Boolean) {
		val block = getBlock(par1ItemStack)
		val size = getSize(par1ItemStack)
		
		par3List.add("$size x $size")
		if (block != null && block !== Blocks.air) par3List.add(ItemStack(block, 1, getBlockMeta(par1ItemStack)).displayName)
	}
	
	companion object {
		
		private const val TAG_BLOCK_NAME = "blockName"
		private const val TAG_BLOCK_META = "blockMeta"
		private const val TAG_SIZE = "size"
		
		fun hasBlocks(stack: ItemStack?, player: EntityPlayer, blocks: List<Vector3>): Boolean {
			if (player.capabilities.isCreativeMode)
				return true
			
			val block = getBlock(stack)
			val meta = getBlockMeta(stack)
			val reqStack = ItemStack(block, 1, meta)
			
			val required = blocks.size
			var current = 0
			val stacksToCheck = ArrayList<ItemStack>()
			for (i in 0 until player.inventory.sizeInventory) {
				val stackInSlot = player.inventory.getStackInSlot(i)
				if (stackInSlot != null && stackInSlot.stackSize > 0 && stackInSlot.item === reqStack.item && stackInSlot.itemDamage == reqStack.itemDamage) {
					current += stackInSlot.stackSize
					if (current >= required)
						return true
				}
				if (stackInSlot != null && stackInSlot.stackSize > 0 && stackInSlot.item is IBlockProvider)
					stacksToCheck.add(stackInSlot)
			}
			
			for (providerStack in stacksToCheck) {
				val prov = providerStack.item as IBlockProvider
				val count = prov.getBlockCount(player, stack, providerStack, block, meta)
				if (count == -1)
					return true
				
				current += count
				
				if (current >= required)
					return true
			}
			
			return false
		}
		
		fun getBlocksToPlace(stack: ItemStack?, player: EntityPlayer): List<Vector3> {
			val coords = ArrayList<Vector3>()
			val mop = ToolCommons.raytraceFromEntity(player.worldObj, player, true, 5.0)
			if (mop != null) {
				val p = Vector3(mop.blockX.toDouble(), mop.blockY.toDouble(), mop.blockZ.toDouble())
				val block = player.worldObj.getBlock(p.x.toInt(), p.y.toInt(), p.z.toInt())
				if (block.isReplaceable(player.worldObj, p.x.toInt(), p.y.toInt(), p.z.toInt())) p.sub(0.0, 1.0, 0.0)
				
				val range = (getSize(stack) xor 1) / 2
				
				val dir = ForgeDirection.getOrientation(mop.sideHit)
				val rot = floor(player.rotationYaw / 90.0 + 0.5).toInt() and 3
				val rotationDir = if (rot == 0) EnumFacing.SOUTH else if (rot == 1) EnumFacing.WEST else if (rot == 2) EnumFacing.NORTH else EnumFacing.EAST
				
				val pitchedVertically = player.rotationPitch > 60 || player.rotationPitch < -60
				
				val axisX = rotationDir == EnumFacing.WEST || rotationDir == EnumFacing.EAST
				val axisZ = rotationDir == EnumFacing.NORTH || rotationDir == EnumFacing.SOUTH
				
				val xOff = if (axisZ || pitchedVertically) range else 0
				val yOff = if (pitchedVertically) 0 else range
				val zOff = if (axisX || pitchedVertically) range else 0
				
				for (x in -xOff until xOff + 1) {
					for (y in 0 until yOff * 2 + 1) {
						for (z in -zOff until zOff + 1) {
							val xp = (p.x + x.toDouble() + dir.offsetX.toDouble()).toInt()
							val yp = (p.y + y.toDouble() + dir.offsetY.toDouble()).toInt()
							val zp = (p.z + z.toDouble() + dir.offsetZ.toDouble()).toInt()
							
							val newPos = Vector3(xp.toDouble(), yp.toDouble(), zp.toDouble())
							val block1 = player.worldObj.getBlock(xp, yp, zp)
							if (player.worldObj.isAirBlock(xp, yp, zp) || block1.isReplaceable(player.worldObj, xp, yp, zp)) coords.add(newPos)
						}
					}
				}
			}
			
			return coords
		}
		
		private fun setSize(stack: ItemStack, size: Int) {
			ItemNBTHelper.setInt(stack, TAG_SIZE, size or 1)
		}
		
		fun getSize(stack: ItemStack?): Int {
			return ItemNBTHelper.getInt(stack, TAG_SIZE, 3) or 1
		}
		
		fun getBlock(stack: ItemStack?): Block? {
			
			return Block.getBlockFromName(getBlockName(stack)) ?: return Blocks.air
		}
		
		fun getBlockName(stack: ItemStack?): String {
			return ItemNBTHelper.getString(stack, TAG_BLOCK_NAME, "")
		}
		
		fun getBlockMeta(stack: ItemStack?): Int {
			return ItemNBTHelper.getInt(stack, TAG_BLOCK_META, 0)
		}
	}
}