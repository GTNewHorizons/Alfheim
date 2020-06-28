package alfheim.common.item.rod

import alexsocol.asjlib.*
import alfheim.api.lib.LibResourceLocations
import alfheim.common.item.ItemMod
import com.mojang.authlib.GameProfile
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.server.MinecraftServer
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.common.util.*
import vazkii.botania.api.item.*
import vazkii.botania.common.core.helper.ItemNBTHelper
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ItemRodClicker: ItemMod("clickerRod"), IAvatarWieldable {
	
	init {
		maxStackSize = 1
		setFull3D()
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World?, player: EntityPlayer?): ItemStack {
		ItemNBTHelper.setBoolean(stack, TAG_MODE, !ItemNBTHelper.getBoolean(stack, TAG_MODE, false))
		return super.onItemRightClick(stack, world, player)
	}
	
	// https://github.com/Thaumic-Tinkerer/ThaumicTinkerer/blob/1.7-final/src/main/java/thaumic/tinkerer/common/block/tile/tablet/TileAnimationTablet.java
	
	override fun onAvatarUpdate(avatar: IAvatarTile?, wand: ItemStack?) {
		if (wand?.item !== this) return
		
		val tile = avatar as? TileEntity ?: return
		val world = tile.worldObj ?: return
		
		val x = tile.xCoord
		val y = tile.yCoord
		val z = tile.zCoord
		
		val delay = (16 - world.getStrongestIndirectPower(x, y, z)) * 10
		
		if (world.isRemote || world.totalWorldTime % delay != 0L) return
		
		var xl = 0
		var yl = 0
		var zl = 0
		var s = tile.getBlockMetadata()
		
		when (s - 2) {
			0 -> zl = -2
			1 -> zl = 2
			2 -> xl = -2
			3 -> xl = 2
		}
		
		val leftClick = ItemNBTHelper.getBoolean(wand, TAG_MODE, false)
		
		val entities: MutableList<Entity>
		var block: Block
		
		s = ForgeDirection.values()[s].opposite.ordinal
		
		if (leftClick) {
			entities = world.getEntitiesWithinAABB(Entity::class.java, getBoundingBox(x + xl + 0.5, y + yl + 0.5, z + zl + 0.5).expand(0.5)) as MutableList<Entity>
			
			// stupid compiler
			block = Blocks.air
		} else {
			block = world.getBlock(x + xl, y + yl, z + zl)
			if (block === Blocks.air) {
				yl = -1
				s = 1
			}
			block = world.getBlock(x + xl, y + yl, z + zl)
			if (block === Blocks.air) return
			
			// stupid compiler
			entities = ArrayList(0)
		}
		
		val player = getFake(world.provider.dimensionId)
		player.isSneaking = !world.isAirBlock(x, y + 1, z)
		
		val inv = world.getTileEntity(x, y - 1, z) as? IInventory
		
		if (inv != null) run {
			var stack: ItemStack? = inv.getStackInSlot(0) ?: return@run
			
			inv.setInventorySlotContents(0, null)
			player.setCurrentItemOrArmor(0, stack)
			
			try {
				if (leftClick) {
					player.getAttributeMap().applyAttributeModifiers(player.heldItem.attributeModifiers)
					player.attackTargetEntityWithCurrentItem(entities.random())
				} else {
					player.heldItem.item.onItemUse(player.heldItem, player, world, x + xl, y + yl, z + zl, s, 0.5f, 0.5f, 0.5f)
				}
			} catch (ignore: Throwable) {
				ignore.printStackTrace()
			}
			
			stack = player.heldItem
			
			if (stack.stackSize <= 0) stack = null
			
			player.setCurrentItemOrArmor(0, null)
			inv.setInventorySlotContents(0, stack)
		} else {
			try {
				if (leftClick) {
				
				} else {
					block.onBlockActivated(world, x + xl, y + yl, z + zl, player, s, 0.5f, 0.5f, 0.5f)
				}
			} catch (ignore: Throwable) {
				ignore.printStackTrace()
			}
		}
	}
	
	override fun getOverlayResource(tile: IAvatarTile?, stack: ItemStack?) = LibResourceLocations.avatarClicker
	
	companion object {
		
		const val TAG_MODE = "mode"
		
		val fakePlayers = HashMap<Int, FakePlayer>()
		
		fun getFake(dim: Int): FakePlayer {
			return fakePlayers[dim] ?: FakePlayerFactory.get(MinecraftServer.getServer().worldServerForDimension(dim), GameProfile(UUID.randomUUID(), "AvatarClicker_$dim")).also { fakePlayers[dim] = it }
		}
	}
}
