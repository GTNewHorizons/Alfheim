package alfheim.common.block.tile.corporea

import alexsocol.asjlib.*
import alexsocol.asjlib.extendables.ASJTile
import alfheim.common.core.helper.CorporeaAdvancedHelper
import alfheim.common.core.helper.CorporeaAdvancedHelper.getFilters
import alfheim.common.core.helper.CorporeaAdvancedHelper.requestMatches
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.MathHelper
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.api.corporea.*
import vazkii.botania.common.block.tile.corporea.TileCorporeaFunnel
import vazkii.botania.common.core.helper.InventoryHelper

class TileCorporeaAutocrafter: ASJTile(), ICorporeaInterceptor, IInventory {
	
	var pendingRequest = false
	
	var request: Any? = null
	var requestCount = 0
	var requestMissing = 0
	var requestX = 0
	var requestY = -1
	var requestZ = 0
	
	/** how many items will be produced from this autocrafter */
	var craftResult = 1
	
	var prevRedstone = false
	
	val spark: ICorporeaSpark? get() = CorporeaHelper.getSparkForBlock(worldObj, xCoord, yCoord, zCoord)
	
	override fun interceptRequestLast(request: Any?, count: Int, thisSpark: ICorporeaSpark?, requestorSpark: ICorporeaSpark, allFoundStacks: MutableList<ItemStack>, allScannedInventories: MutableList<IInventory>?, doIt: Boolean) {
		if (worldObj.isRemote) return
		
		val filters = getFilters()
		
		for (filter in filters)
			if (requestMatches(request, filter)) {
				var missing = count
				
				for (stack in allFoundStacks) missing -= stack.stackSize
				
				if (missing > 0) {
					val requestor = requestorSpark.inventory as TileEntity
					
					setPendingRequest(requestor.xCoord, requestor.yCoord, requestor.zCoord, request, count, missing)
					
					return
				}
			}
	}
	
	fun setPendingRequest(x: Int, y: Int, z: Int, req: Any?, count: Int, missing: Int) {
		if (pendingRequest) return
		
		pendingRequest = true
		
		request = req
		requestCount = count
		requestMissing = MathHelper.ceiling_float_int(missing / craftResult.F)
		
		requestX = x
		requestY = y
		requestZ = z
		
		worldObj.func_147453_f(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord))
		
//		ASJUtilities.chatLog("CAC at ${Vector3.fromTileEntity(this)} got request $count of $req (missing $missing) from $x $y $z. Need to craft $requestMissing times.")
	}
	
	override fun updateEntity() {
		checkRedstone()
		
		if (!pendingRequest) return
		
		if (requestMissing > 0)
			return doAutocraft()
		
		val stacks = CorporeaHelper.requestItem(request, -1, spark, request is ItemStack, false)
		val count = stacks.sumBy { it.stackSize }
		
		if (count >= requestCount) {
			fulfillRequest()
		}
	}
	
	var buffer = arrayOfNulls<ItemStack?>(27)
	var waitingForIngredient = false
	
	fun doAutocraft() {
		if (waitingForIngredient) return
		
		val patterns = InventoryHelper.getInventory(worldObj, xCoord, yCoord + 1, zCoord) ?: return
		
		val ourSpark = spark
		
		buffer = buffer.ensureCapacity(patterns.sizeInventory)
		for (i in 0 until patterns.sizeInventory) {
			val pattern = patterns[i] ?: continue
			
			if (buffer[i] != null) continue
			
			val got = CorporeaHelper.requestItem(pattern, pattern.stackSize, ourSpark, true, true)
			
			// not enough
			if (got == null || got.sumBy { it.stackSize } < pattern.stackSize) {
				waitingForIngredient = true
				return
			}
			
			// else all fine
			buffer[i] = got[0].copy().apply { stackSize = got.sumBy { it.stackSize } }
		}
		
		// buffer is filled and everything can be passed to crafter
		
		val down = InventoryHelper.getInventory(worldObj, xCoord, yCoord - 2, zCoord) ?: return
		
		for (i in 0 until patterns.sizeInventory) {
			patterns[i] ?: continue
			
			val ret = buffer.getOrNull(i) ?: continue
			buffer[i] = null
			
			if (down !is TileCorporeaFunnel && ret.stackSize == InventoryHelper.testInventoryInsertion(down, ret, ForgeDirection.UP)) {
				// FIXME puts in the first slot instead of indexed
				InventoryHelper.insertItemIntoInventory(down, ret)
			} else {
				ourSpark?.also { CorporeaAdvancedHelper.putToNetwork(it, ret) } ?: continue
				worldObj.spawnEntityInWorld(EntityItem(worldObj, xCoord + 0.5, yCoord + 2.5, zCoord + 0.5, ret).also { item -> item.setMotion(0.0) })
			}
		}
		
		--requestMissing
	}
	
	fun fulfillRequest() {
		if (!pendingRequest) return
		
		val spark = CorporeaHelper.getSparkForBlock(worldObj, requestX, requestY, requestZ)
		
		if (spark != null) {
			val inv = spark.inventory
			
			if (inv is ICorporeaRequestor) {
				inv.doCorporeaRequest(request, requestCount, spark)
				worldObj.func_147453_f(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord))
			} else if (inv is TileCorporeaAutocrafter)
				inv.waitingForIngredient = false
		}
		
		pendingRequest = false
		onWanded()
	}
	
	fun onWanded(): Boolean {
		pendingRequest = false
		request = null
		requestCount = 0
		requestMissing = 0
		requestX = 0
		requestY = -1
		requestZ = 0
		waitingForIngredient = false
		
		val spark = spark ?: return true
		
		buffer.forEach {
			if (it == null) return@forEach
			val drop = CorporeaAdvancedHelper.putToNetwork(spark, it) ?: return@forEach
			worldObj.spawnEntityInWorld(EntityItem(worldObj, xCoord + 0.5, yCoord + 2.5, zCoord + 0.5, drop).also { item -> item.setMotion(0.0) })
		}
		
		return true
	}
	
	fun checkRedstone() {
		var redstone = false
		
		for (dir in ForgeDirection.VALID_DIRECTIONS)
			redstone = redstone || worldObj.getIndirectPowerLevelTo(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ, dir.ordinal) > 0
		
		if (!prevRedstone && redstone)
			waitingForIngredient = false
		
		prevRedstone = redstone
	}
	
	override fun writeCustomNBT(nbt: NBTTagCompound) {
		super.writeCustomNBT(nbt)
		
		nbt.setInteger(TAG_CRAFT_RESULT, craftResult)
		
		nbt.setBoolean(TAG_PENDING_REQUEST, pendingRequest)
		
		when (request) {
			null         -> nbt.setInteger(TAG_REQUEST_TYPE, REQUEST_NULL)
			
			is String    -> {
				nbt.setInteger(TAG_REQUEST_TYPE, REQUEST_STRING)
				nbt.setString(TAG_REQUEST_CONTENTS, request as String)
			}
			
			is ItemStack -> {
				nbt.setInteger(TAG_REQUEST_TYPE, REQUEST_ITEMSTACK)
				nbt.setTag(TAG_REQUEST_CONTENTS, (request as ItemStack).writeToNBT(NBTTagCompound()))
			}
		}
		
		nbt.setInteger(TAG_REQUEST_COUNT, requestCount)
		nbt.setInteger(TAG_REQUEST_MISSING, requestMissing)
		
		nbt.setInteger(TAG_REQUEST_X, requestX)
		nbt.setInteger(TAG_REQUEST_Y, requestY)
		nbt.setInteger(TAG_REQUEST_Z, requestZ)
		
		nbt.setBoolean(TAG_IS_WAITING, waitingForIngredient)
		
		val invNbt = NBTTagCompound()
		invNbt.setInteger("TAG_COUNT", buffer.size)
		
		for (i in buffer.indices) {
			buffer.getOrNull(i)?.let { invNbt.setTag("TAG_SLOT_$i", it.writeToNBT(NBTTagCompound())) }
		}
		
		nbt.setTag("TAG_INVENTORY", invNbt)
	}
	
	override fun readCustomNBT(nbt: NBTTagCompound) {
		super.readCustomNBT(nbt)
		
		craftResult = nbt.getInteger(TAG_CRAFT_RESULT)
		
		pendingRequest = nbt.getBoolean(TAG_PENDING_REQUEST)
		
		request = when (nbt.getInteger(TAG_REQUEST_TYPE)) {
			REQUEST_STRING    -> nbt.getString(TAG_REQUEST_CONTENTS)
			REQUEST_ITEMSTACK -> ItemStack.loadItemStackFromNBT(nbt.getCompoundTag(TAG_REQUEST_CONTENTS))
			else              -> null
		}
		
		requestCount = nbt.getInteger(TAG_REQUEST_COUNT)
		requestMissing = nbt.getInteger(TAG_REQUEST_MISSING)
		
		requestX = nbt.getInteger(TAG_REQUEST_X)
		requestY = nbt.getInteger(TAG_REQUEST_Y)
		requestZ = nbt.getInteger(TAG_REQUEST_Z)
		
		waitingForIngredient = nbt.getBoolean(TAG_IS_WAITING)
		
		val invNbt = nbt.getTag("TAG_INVENTORY") as NBTTagCompound
		
		buffer = arrayOfNulls(invNbt.getInteger("TAG_COUNT"))
		for (i in buffer.indices)
			buffer[i] = if (invNbt.hasKey("TAG_SLOT_$i")) ItemStack.loadItemStackFromNBT(invNbt.getTag("TAG_SLOT_$i") as NBTTagCompound) else null
	}
	
	companion object {
		const val TAG_CRAFT_RESULT = "craftResult"
		
		const val TAG_PENDING_REQUEST = "pendingRequest"
		const val TAG_REQUEST_TYPE = "requestType"
		const val TAG_REQUEST_CONTENTS = "requestContents"
		const val TAG_REQUEST_COUNT = "requestCount"
		const val TAG_REQUEST_MISSING = "requestMissing"
		const val TAG_REQUEST_X = "requestX"
		const val TAG_REQUEST_Y = "requestY"
		const val TAG_REQUEST_Z = "requestZ"
		
		const val TAG_REDSTONE = "redstone"
		const val TAG_IS_WAITING = "waiting"
		
		const val REQUEST_NULL = 0
		const val REQUEST_ITEMSTACK = 1
		const val REQUEST_STRING = 2
	}
	
	// UNUSED
	
	override fun interceptRequest(request: Any?, count: Int, thisSpark: ICorporeaSpark?, requestorSpark: ICorporeaSpark?, currentlyFoundStacks: MutableList<ItemStack>?, currentlyScannedInventories: MutableList<IInventory>?, doIt: Boolean) = Unit
	override fun getStackInSlot(slot: Int) = null
	override fun decrStackSize(slot: Int, side: Int) = null
	override fun getSizeInventory() = 0
	override fun getStackInSlotOnClosing(slot: Int) = null
	override fun hasCustomInventoryName() = false
	override fun isItemValidForSlot(slot: Int, stack: ItemStack?) = false
	override fun getInventoryName() = null
	override fun getInventoryStackLimit() = 0
	override fun isUseableByPlayer(player: EntityPlayer?) = false
	override fun openInventory() = Unit
	override fun closeInventory() = Unit
	override fun setInventorySlotContents(slot: Int, stack: ItemStack?) = Unit
}