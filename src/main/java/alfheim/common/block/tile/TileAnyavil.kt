package alfheim.common.block.tile

import alexsocol.asjlib.*
import alexsocol.asjlib.extendables.TileItemContainer
import alexsocol.asjlib.math.Vector3
import alfheim.api.AlfheimAPI
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.handler.AlfheimConfigHandler
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.passive.EntitySheep
import net.minecraft.entity.player.*
import net.minecraft.inventory.ISidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.world.World
import vazkii.botania.api.internal.IManaBurst
import vazkii.botania.client.core.handler.HUDHandler
import vazkii.botania.common.Botania
import vazkii.botania.common.block.tile.mana.TilePool
import java.awt.Color
import kotlin.math.*

class TileAnyavil: TileItemContainer(), ISidedInventory {
	
	var pinkCharge = 0
	var knownMana = -1
	
	fun onBurstCollision(burst: IManaBurst, world: World) {
		val item = item
		if (burst.isFake) return
		if (item == null) return
		if (GameRegistry.findUniqueIdentifierFor(item.item)?.toString() ?: "null:null" in AlfheimConfigHandler.anyavilBlackList) return
		if (burst.color != -0xd7f5a) return
		
		val eitems = world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB.getBoundingBox((xCoord - 1).D, yCoord.D, (zCoord - 1).D, (xCoord + 2).D, (yCoord + 2).D, (zCoord + 2).D).expand(5.0, 3.0, 5.0))
		for (eitem in eitems) {
			eitem as EntityItem
			if (eitem.isDead) continue
			val stack = eitem.entityItem
			val pinkness = AlfheimAPI.getPinkness(stack)
			if (pinkness > 0) {
				pinkCharge += pinkness * stack.stackSize
				eitem.setDead()
			}
		}
		
		var extraPink = max(0, pinkCharge - MAX_PINK_CHARGE)
		val col = EntitySheep.fleeceColorTable[6]
		if (extraPink > 0) {
			extraPink = min(extraPink, 4000)
			pinkCharge = MAX_PINK_CHARGE
			val m = Vector3()
			while (extraPink > 0) {
				m.set(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5).normalize().mul(0.1)
				Botania.proxy.wispFX(world, xCoord + 0.5, yCoord + 1.5, zCoord + 0.5, col[0], col[1], col[2], 0.25f, m.x.F, m.y.F, m.z.F)
				extraPink--
			}
		}
		
		val needed = item.meta
		val transfer = max(0, min(needed, pinkCharge))
		pinkCharge -= transfer
		item.meta = item.meta - transfer
		
		for (i in 0..23) Botania.proxy.wispFX(world, xCoord.D + 0.5 + (worldObj.rand.nextFloat() / 5f - 0.1f).D, yCoord + 1.5, zCoord.D + 0.5 + (worldObj.rand.nextFloat() / 5f - 0.1f).D, col[0], col[1], col[2], 0.25f, 0f, worldObj.rand.nextFloat() * 0.2f - 0.1f, 0f)
	}
	
	fun onWanded(player: EntityPlayer?, wand: ItemStack): Boolean {
		if (player == null) return false
		
		if (!worldObj.isRemote) {
			val nbttagcompound = NBTTagCompound()
			writeCustomNBT(nbttagcompound)
			nbttagcompound.setInteger(TAG_KNOWN_MANA, pinkCharge)
			if (player is EntityPlayerMP) player.playerNetServerHandler.sendPacket(S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, nbttagcompound))
		}
		
		worldObj.playSoundAtEntity(player, "botania:ding", 0.11f, 1f)
		
		return true
	}
	
	fun renderHUD(res: ScaledResolution) {
		val name = ItemStack(AlfheimBlocks.anyavil).displayName
		val col = EntitySheep.fleeceColorTable[6]
		val color = Color(col[0], col[1], col[2]).rgb
		HUDHandler.drawSimpleManaHUD(color, knownMana, MAX_PINK_CHARGE, name, res)
	}
	
	override fun writeCustomNBT(nbt: NBTTagCompound) {
		super.writeCustomNBT(nbt)
		nbt.setInteger(TAG_MANA, pinkCharge)
		nbt.setInteger(TAG_MANA_CAP, MAX_PINK_CHARGE)
		nbt.setInteger(TAG_METADATA, blockMetadata)
		// nbt.setInteger(TAG_KNOWN_MANA, knownMana)
	}
	
	override fun readCustomNBT(nbt: NBTTagCompound) {
		super.readCustomNBT(nbt)
		pinkCharge = nbt.getInteger(TAG_MANA)
		blockMetadata = nbt.getInteger(TAG_METADATA)
		
		if (nbt.hasKey(TAG_KNOWN_MANA))
			knownMana = nbt.getInteger(TAG_KNOWN_MANA)
	}
	
	override fun getSizeInventory(): Int {
		return 1
	}
	
	override fun getStackInSlot(slot: Int): ItemStack? {
		return item
	}
	
	override fun decrStackSize(slot: Int, amount: Int): ItemStack? {
		if (getStackInSlot(slot) != null) {
			val itemstack: ItemStack?
			
			return if (getStackInSlot(slot)!!.stackSize <= amount) {
				itemstack = getStackInSlot(slot)
				setInventorySlotContents(slot, null)
				itemstack
			} else {
				itemstack = getStackInSlot(slot)!!.splitStack(amount)
				if (getStackInSlot(slot)!!.stackSize == 0) setInventorySlotContents(slot, null)
				itemstack
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
	
	override fun isItemValidForSlot(slot: Int, stack: ItemStack): Boolean {
		if (GameRegistry.findUniqueIdentifierFor(stack.item).toString() in AlfheimConfigHandler.anyavilBlackList) return false
		
		return stack.stackSize == 1 && stack.item.isDamageable
	}
	
	override fun getAccessibleSlotsFromSide(side: Int): IntArray {
		return intArrayOf(0)
	}
	
	override fun canInsertItem(slot: Int, stack: ItemStack, side: Int): Boolean {
		if (GameRegistry.findUniqueIdentifierFor(stack.item).toString() in AlfheimConfigHandler.anyavilBlackList) return false
		
		return side == 1 && isItemValidForSlot(slot, stack)
	}
	
	override fun canExtractItem(slot: Int, stack: ItemStack, side: Int): Boolean {
		return side == 0 && slot == 0 && !stack.isItemDamaged
	}
	
	companion object {
		
		const val MAX_PINK_CHARGE = TilePool.MAX_MANA_DILLUTED
		const val TAG_MANA = "mana"
		const val TAG_MANA_CAP = "manaCap"
		const val TAG_METADATA = "metadata"
		const val TAG_KNOWN_MANA = "knownMana"
	}
}