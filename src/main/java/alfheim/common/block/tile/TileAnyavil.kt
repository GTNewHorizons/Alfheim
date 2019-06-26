package alfheim.common.block.tile

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.extendables.ItemContainingTileEntity
import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.AlfheimAPI
import alfheim.common.core.registry.AlfheimBlocks
import alfheim.common.network.MessageTileItem
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.passive.EntitySheep
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.ISidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.AxisAlignedBB
import net.minecraft.world.World
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.internal.IManaBurst
import vazkii.botania.common.Botania
import vazkii.botania.common.block.tile.mana.TilePool

import java.awt.*

class TileAnyavil: ItemContainingTileEntity(), ISidedInventory {
	
	var pinkCharge = 0
	
	override var item: ItemStack?
		get() = super.item
		set(stack) {
			super.item = stack
			if (ASJUtilities.isServer && worldObj != null) {
				AlfheimCore.network.sendToDimension(MessageTileItem(xCoord, yCoord, zCoord, item), worldObj.provider.dimensionId)
			}
		}
	
	fun onBurstCollision(burst: IManaBurst, world: World, x: Int, y: Int, z: Int) {
		if (burst.isFake) return
		if (item == null) return
		if (burst.color != -0xd7f5a) return
		val eitems = world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB.getBoundingBox((xCoord - 1).toDouble(), yCoord.toDouble(), (zCoord - 1).toDouble(), (xCoord + 2).toDouble(), (yCoord + 2).toDouble(), (zCoord + 2).toDouble()).expand(5.0, 3.0, 5.0))
		for (eitem in eitems) {
			if (eitem.isDead) continue
			val item = eitem.getEntityItem()
			val pinkness = AlfheimAPI.getPinkness(item)
			if (pinkness > 0) {
				pinkCharge += pinkness * item.stackSize
				eitem.setDead()
			}
		}
		
		var extraPink = Math.max(0, pinkCharge - MAX_PINK_CHARGE)
		val col = EntitySheep.fleeceColorTable[6]
		if (extraPink > 0) {
			extraPink = Math.min(extraPink, 4000)
			pinkCharge = MAX_PINK_CHARGE
			val m = Vector3()
			while (extraPink > 0) {
				m.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize().mul(0.1)
				Botania.proxy.wispFX(world, xCoord + 0.5, yCoord + 1.5, zCoord + 0.5, col[0], col[1], col[2], 0.25f, m.x.toFloat(), m.y.toFloat(), m.z.toFloat())
				extraPink--
			}
		}
		
		val needed = item!!.itemDamage
		val transfer = Math.max(0, Math.min(needed, pinkCharge))
		pinkCharge -= transfer
		item!!.itemDamage = item!!.itemDamage - transfer
		
		for (i in 0..23) Botania.proxy.wispFX(world, xCoord.toDouble() + 0.5 + (worldObj.rand.nextFloat() / 5.0f - 0.1f).toDouble(), yCoord + 1.5, zCoord.toDouble() + 0.5 + (worldObj.rand.nextFloat() / 5.0f - 0.1f).toDouble(), col[0], col[1], col[2], 0.25f, 0f, worldObj.rand.nextFloat() * 0.2f - 0.1f, 0f)
	}
	
	fun renderHUD(mc: Minecraft, res: ScaledResolution, world: World, x: Int, y: Int, z: Int) {
		val name = AlfheimBlocks.anyavil.localizedName
		val col = EntitySheep.fleeceColorTable[6]
		val color = Color(col[0], col[1], col[2]).rgb
		BotaniaAPI.internalHandler.drawSimpleManaHUD(color, pinkCharge, MAX_PINK_CHARGE, name, res)
	}
	
	override fun writeCustomNBT(nbt: NBTTagCompound) {
		super.writeCustomNBT(nbt)
		nbt.setInteger(TAG_MANA, pinkCharge)
		nbt.setInteger(TAG_MANA_CAP, MAX_PINK_CHARGE)
		nbt.setInteger(TAG_METADATA, blockMetadata)
	}
	
	override fun readCustomNBT(nbt: NBTTagCompound) {
		super.readCustomNBT(nbt)
		pinkCharge = nbt.getInteger(TAG_MANA)
		this.blockMetadata = nbt.getInteger(TAG_METADATA)
	}
	
	override fun getSizeInventory(): Int {
		return 1
	}
	
	override fun getStackInSlot(slot: Int): ItemStack? {
		return item
	}
	
	override fun decrStackSize(slot: Int, ammount: Int): ItemStack? {
		if (getStackInSlot(slot) != null) {
			val itemstack: ItemStack?
			
			if (getStackInSlot(slot)!!.stackSize <= ammount) {
				itemstack = getStackInSlot(slot)
				setInventorySlotContents(slot, null)
				return itemstack
			} else {
				itemstack = this.getStackInSlot(slot)!!.splitStack(ammount)
				if (this.getStackInSlot(slot)!!.stackSize == 0) setInventorySlotContents(slot, null)
				return itemstack
			}
		}
		return null
	}
	
	override fun getStackInSlotOnClosing(slot: Int): ItemStack? {
		if (getStackInSlot(slot) != null) {
			val itemstack = getStackInSlot(slot)
			setInventorySlotContents(slot, null)
			return itemstack
		}
		return null
	}
	
	override fun setInventorySlotContents(slot: Int, stack: ItemStack?) {
		item = stack
		if (stack != null && stack.stackSize > inventoryStackLimit) stack.stackSize = inventoryStackLimit
	}
	
	override fun getInventoryName(): String {
		return "container.anyavil"
	}
	
	override fun hasCustomInventoryName(): Boolean {
		return false
	}
	
	override fun getInventoryStackLimit(): Int {
		return 1
	}
	
	override fun isUseableByPlayer(player: EntityPlayer): Boolean {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) === this && player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) <= 64.0
	}
	
	override fun openInventory() {}
	
	override fun closeInventory() {}
	
	override fun isItemValidForSlot(slot: Int, stack: ItemStack?): Boolean {
		return stack != null && stack.stackSize == 1 && stack.item.isDamageable
	}
	
	override fun getAccessibleSlotsFromSide(side: Int): IntArray {
		return intArrayOf(0)
	}
	
	override fun canInsertItem(slot: Int, stack: ItemStack, side: Int): Boolean {
		return side == 1 && isItemValidForSlot(slot, stack)
	}
	
	override fun canExtractItem(slot: Int, stack: ItemStack, side: Int): Boolean {
		return side == 0 && slot == 0 && !stack.isItemDamaged
	}
	
	companion object {
		
		val MAX_PINK_CHARGE = TilePool.MAX_MANA_DILLUTED
		private val TAG_MANA = "mana"
		private val TAG_MANA_CAP = "manaCap"
		private val TAG_METADATA = "metadata"
	}
}