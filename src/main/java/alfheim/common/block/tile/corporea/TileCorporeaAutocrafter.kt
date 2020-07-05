package alfheim.common.block.tile.corporea

import alexsocol.asjlib.*
import alexsocol.asjlib.extendables.ASJTile
import alexsocol.asjlib.math.Vector3
import alfheim.common.core.helper.CorporeaInsertHelper
import net.minecraft.entity.item.*
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
import vazkii.botania.common.lib.LibMisc

class TileCorporeaAutocrafter: ASJTile(), ICorporeaInterceptor, IInventory {
	
	val spark get() = CorporeaHelper.getSparkForBlock(worldObj, xCoord, yCoord, zCoord)
	
	override fun interceptRequest(request: Any?, count: Int, spark: ICorporeaSpark?, source: ICorporeaSpark?, stacks: MutableList<ItemStack>?, inventories: MutableList<IInventory>?, doit: Boolean) = Unit
	
	override fun interceptRequestLast(request: Any?, count: Int, thisSpark: ICorporeaSpark?, requestSpart: ICorporeaSpark, stacksInSystem: List<ItemStack>, inventories: List<IInventory?>?, doit: Boolean) {
		if (worldObj.isRemote || buffer.isEmpty()) return
		
		val filters = getFilters()
		
		for (filter in filters)
			if (requestMatches(request, filter)) {
				var missing = count
				
				for (stack in stacksInSystem) missing -= stack.stackSize
				
				if (missing > 0) {
					val requestor = requestSpart.inventory as TileEntity
					
					setPendingRequest(requestor.xCoord, requestor.yCoord, requestor.zCoord, request, missing, missing)
					
					return
				}
			}
	}
	
	fun requestMatches(request: Any?, filter: ItemStack?): Boolean {
		return if (filter == null) false else when (request) {
			is ItemStack -> request.isItemEqual(filter) && ItemStack.areItemStackTagsEqual(filter, request)
			is String    -> CorporeaHelper.stacksMatch(filter, request)
			else         -> false
		}
		
	}
	
	fun getFilters(): List<ItemStack> {
		val filter = ArrayList<ItemStack>()
		
		val orientationToDir = intArrayOf(
			3, 4, 2, 5
		)
		
		for (dir in LibMisc.CARDINAL_DIRECTIONS) {
			val frames = worldObj.getEntitiesWithinAABB(EntityItemFrame::class.java, boundingBox().offset(dir.offsetX.D, dir.offsetY.D, dir.offsetZ.D)) as MutableList<EntityItemFrame>
			for (frame in frames) {
				val orientation = frame.hangingDirection
				if (orientationToDir[orientation] == dir.ordinal)
					filter.add(frame.displayedItem)
			}
		}
		
		return filter
	}
	
	fun doRequest(stack: ItemStack, doIt: Boolean): ItemStack? {
		val spark = spark
		
		return if (spark?.master != null) doCorporeaRequest(stack.copy(), spark, doIt) else null
	}
	
	fun doCorporeaRequest(request: ItemStack, spark: ICorporeaSpark, doIt: Boolean): ItemStack? {
		val stacks = CorporeaHelper.requestItem(request, request.stackSize, spark, true, doIt)
		
		if (doIt) spark.onItemsRequested(stacks)
		
		if (stacks.isEmpty()) return null
		
		return stacks.fold(stacks[0].copy().also { it.stackSize = 0 }) { acc, res -> acc.also { it.stackSize += res.stackSize } }
	}
	
	// ######## request checking cycle ########
	
	override fun updateEntity() {
		if (buffer.isEmpty()) onFilterChange()
		
		if (!pendingRequest) return
		
		val spark = spark
		if (spark != null && spark.master != null && request != null) {
			val stacks = CorporeaHelper.requestItem(request, -1, spark, request is ItemStack, false)
			
			val itemCount = stacks.sumBy { it.stackSize }
			
//			if (itemCount < requestCount && needToCraft <= 0)
//				VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.ECHO_MOB, worldObj.provider.dimensionId, xCoord.D + 0.5, yCoord.D + 0.5, zCoord.D + 0.5)
			
			if (itemCount >= requestCount && needToCraft <= 0)
				fulfilRequest()
			else {
				if (needToCraft > 0)
					if (doAutocraft())
						--needToCraft
			}
		}
	}
	
	fun doAutocraft(): Boolean {
		val inv = InventoryHelper.getInventory(worldObj, xCoord, yCoord + 1, zCoord)
		
		if (inv != null) {
			val spark = spark
			
//			var fail = false
			
			for (i in 0 until inv.sizeInventory) {
				val pattern = inv.getStackInSlot(i)?.copy() ?: continue
				
				val ret = doRequest(pattern, true)
				
				if (ret != null) buffer[i] = ret
				
				if (ret == null || ret.stackSize < pattern.stackSize) {
//					fail = true
//					break
					return false
				}
			}
			
//			if (fail) {
//				for (it in buffer) {
//					CorporeaInsertHelper.putToNetwork(spark, it) ?: continue
//					worldObj.spawnEntityInWorld(EntityItem(worldObj, xCoord + 0.5, yCoord + 2.5, zCoord + 0.5, it).also { item -> item.setMotion(0.0) })
//				}
//
//				return false
//			}
			
			val down = InventoryHelper.getInventory(worldObj, xCoord, yCoord - 2, zCoord) ?: return false
			
			var success = true
			
			for (i in 0 until inv.sizeInventory) {
				inv.getStackInSlot(i) ?: continue
				
				val ret = buffer.getOrNull(i) ?: return false
				buffer[i] = null
				
				if (down !is TileCorporeaFunnel && ret.stackSize == InventoryHelper.testInventoryInsertion(down, ret, ForgeDirection.UP)) {
					InventoryHelper.insertItemIntoInventory(down, ret)
				} else {
					CorporeaInsertHelper.putToNetwork(spark, ret) ?: continue
					worldObj.spawnEntityInWorld(EntityItem(worldObj, xCoord + 0.5, yCoord + 2.5, zCoord + 0.5, ret).also { item -> item.setMotion(0.0) })
					success = false
				}
			}
			
			
			return success
		}
		
		return false
	}
	
	// ######## TileCorporeaRetainer ########
	
	var pendingRequest = false
	
	var requestX = 0
	var requestY = -1
	var requestZ = 0
	var request: Any? = null
	var requestCount = 0
	
	var needToCraft = 0
	
	var craftResult = 1
	
	fun setPendingRequest(x: Int, y: Int, z: Int, request: Any?, requestCount: Int, missing: Int) {
		if (pendingRequest) return
		
		needToCraft = MathHelper.ceiling_float_int(missing / craftResult.F)
		requestX = x
		requestY = y
		requestZ = z
		this.request = request
		this.requestCount = requestCount
		pendingRequest = true
		worldObj.func_147453_f(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord))
		
		ASJUtilities.chatLog("CAC at ${Vector3.fromTileEntity(this)} got request $requestCount of $request (missing $missing) from $x $y $z. Need to craft $needToCraft times.")
	}
	
	fun fulfilRequest() {
		if (!pendingRequest) return
		
		val spark = CorporeaHelper.getSparkForBlock(worldObj, requestX, requestY, requestZ)
		
		if (spark != null) {
			val inv = spark.inventory
			
			if (inv != null && inv is ICorporeaRequestor) {
				val requestor = inv as ICorporeaRequestor
				requestor.doCorporeaRequest(request, requestCount, spark)
				worldObj.func_147453_f(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord))
			}
		}
		
		buffer.forEach {
		
		}
		
		pendingRequest = false
	}
	
	override fun writeCustomNBT(nbt: NBTTagCompound) {
		super.writeCustomNBT(nbt)
		
		nbt.setBoolean(TAG_PENDING_REQUEST, pendingRequest)
		
		nbt.setInteger(TAG_REQUEST_X, requestX)
		nbt.setInteger(TAG_REQUEST_Y, requestY)
		nbt.setInteger(TAG_REQUEST_Z, requestZ)
		
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
		nbt.setInteger(TAG_NEED_TO_CRAFT, needToCraft)
		nbt.setInteger(TAG_CRAFT_RESULT, craftResult)
		
		
		val invNbt = NBTTagCompound()
		invNbt.setInteger("TAG_COUNT", buffer.size)
		
		for (i in buffer.indices) {
			buffer.getOrNull(i)?.let { invNbt.setTag("TAG_SLOT_$i", it.writeToNBT(NBTTagCompound())) }
		}
		
		nbt.setTag("TAG_INVENTORY", invNbt)
	}
	
	override fun readCustomNBT(nbt: NBTTagCompound) {
		super.readCustomNBT(nbt)
		
		pendingRequest = nbt.getBoolean(TAG_PENDING_REQUEST)
		
		requestX = nbt.getInteger(TAG_REQUEST_X)
		requestY = nbt.getInteger(TAG_REQUEST_Y)
		requestZ = nbt.getInteger(TAG_REQUEST_Z)
		
		request = when (nbt.getInteger(TAG_REQUEST_TYPE)) {
			REQUEST_STRING    -> nbt.getString(TAG_REQUEST_CONTENTS)
			REQUEST_ITEMSTACK -> ItemStack.loadItemStackFromNBT(nbt.getCompoundTag(TAG_REQUEST_CONTENTS))
			else              -> null
		}
		
		requestCount = nbt.getInteger(TAG_REQUEST_COUNT)
		needToCraft = nbt.getInteger(TAG_NEED_TO_CRAFT)
		craftResult = nbt.getInteger(TAG_CRAFT_RESULT)
		
		val invNbt = nbt.getTag("TAG_INVENTORY") as NBTTagCompound
		
		buffer = arrayOfNulls(invNbt.getInteger("TAG_COUNT"))
		for (i in buffer.indices) buffer[i] = if (invNbt.hasKey("TAG_SLOT_$i")) ItemStack.loadItemStackFromNBT(invNbt.getTag("TAG_SLOT_$i") as NBTTagCompound) else null
	}
	
	// ######## buffer ########
	
	var buffer = arrayOfNulls<ItemStack?>(worldObj?.let { InventoryHelper.getInventory(it, xCoord, yCoord + 1, zCoord)?.sizeInventory } ?: 0)
	
	override fun getStackInSlot(slot: Int) = buffer.getOrNull(slot)
	
	override fun decrStackSize(slot: Int, count: Int): ItemStack? {
		if (buffer[slot] != null) {
			val stackAt: ItemStack
			
			if (buffer[slot]!!.stackSize <= count) {
				stackAt = buffer[slot]!!
				buffer[slot] = null
				return stackAt
			} else {
				stackAt = buffer[slot]!!.splitStack(count)
				if (buffer[slot]!!.stackSize == 0) buffer[slot] = null
				return stackAt
			}
		}
		
		return null
	}
	
	override fun getSizeInventory() = buffer.size
	
	override fun getStackInSlotOnClosing(slot: Int) = getStackInSlot(slot)
	
	override fun hasCustomInventoryName() = true
	
	override fun isItemValidForSlot(slot: Int, stack: ItemStack?) = true
	
	override fun getInventoryName() = "CorporeaAutocrafter"
	
	override fun getInventoryStackLimit() = 64
	
	override fun isUseableByPlayer(player: EntityPlayer?) = false
	
	override fun openInventory() = Unit
	
	override fun closeInventory() = Unit
	
	override fun setInventorySlotContents(slot: Int, stack: ItemStack?) {
		if (slot in buffer.indices) {
			buffer[slot] = stack
		}
	}
	
	fun onWanded() {
		needToCraft = 0
		pendingRequest = false
		requestX = 0
		requestY = -1
		requestZ = 0
		request = null
		requestCount = 0
	}
	
	fun onFilterChange() {
		val spark = spark
		
		if (spark != null)
			buffer.forEach { stack ->
			stack?.let { CorporeaInsertHelper.putToNetwork(spark, it) } ?: return@forEach
			
			worldObj.spawnEntityInWorld(EntityItem(worldObj, xCoord + 0.5, yCoord + 2.5, zCoord + 0.5, stack).also { item -> item.setMotion(0.0) })
		}
		
		val inv = InventoryHelper.getInventory(worldObj, xCoord, yCoord + 1, zCoord) ?: return
		
		buffer = arrayOfNulls(inv.sizeInventory)
	}
	
	companion object {
		const val TAG_CRAFT_RESULT = "craftResult"
		
		const val TAG_NEED_TO_CRAFT = "needToCraft"
		const val TAG_PENDING_REQUEST = "pendingRequest"
		const val TAG_REQUEST_X = "requestX"
		const val TAG_REQUEST_Y = "requestY"
		const val TAG_REQUEST_Z = "requestZ"
		const val TAG_REQUEST_TYPE = "requestType"
		const val TAG_REQUEST_CONTENTS = "requestContents"
		const val TAG_REQUEST_COUNT = "requestCount"
		
		const val REQUEST_NULL = 0
		const val REQUEST_ITEMSTACK = 1
		const val REQUEST_STRING = 2
	}
}
