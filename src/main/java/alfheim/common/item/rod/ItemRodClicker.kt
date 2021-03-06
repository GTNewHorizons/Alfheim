package alfheim.common.item.rod

import alexsocol.asjlib.*
import alexsocol.asjlib.ItemNBTHelper.getBoolean
import alexsocol.asjlib.ItemNBTHelper.setBoolean
import alfheim.api.lib.LibResourceLocations
import alfheim.client.core.helper.IconHelper
import alfheim.common.entity.boss.EntityFlugel
import alfheim.common.item.ItemMod
import com.mojang.authlib.GameProfile
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.*
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.inventory.*
import net.minecraft.item.ItemStack
import net.minecraft.server.MinecraftServer
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.common.util.*
import net.minecraftforge.event.ForgeEventFactory
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import vazkii.botania.api.item.*
import vazkii.botania.common.block.tile.TileAvatar
import vazkii.botania.common.core.helper.InventoryHelper
import java.util.*

class ItemRodClicker: ItemMod("RodClicker"), IAvatarWieldable {
	
	init {
		maxStackSize = 1
		setFull3D()
	}
	
	override fun registerIcons(reg: IIconRegister) {
		itemIcon = IconHelper.forItem(reg, this, "Right")
		iconLeft = IconHelper.forItem(reg, this, "Left")
	}
	
	override fun getIconIndex(stack: ItemStack) = if (stack.isLeftClick()) iconLeft else itemIcon!!
	
	override fun getIcon(stack: ItemStack, pass: Int) = getIconIndex(stack)
	
	override fun addInformation(stack: ItemStack, player: EntityPlayer?, list: MutableList<Any?>, adv: Boolean) {
		list.add(StatCollector.translateToLocal("${this.unlocalizedName}.leftclick.${stack.isLeftClick()}"))
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World?, player: EntityPlayer?): ItemStack {
		stack.setLeftClick(!stack.isLeftClick())
		return super.onItemRightClick(stack, world, player)
	}
	
	override fun onAvatarUpdate(avatar: IAvatarTile?, wand: ItemStack?) {
		if (wand?.item !== this) return
		
		val tile = avatar as? TileAvatar ?: return
		val world = tile.worldObj ?: return
		
		val x = tile.xCoord
		val y = tile.yCoord
		val z = tile.zCoord
		
		val delay = (16 - world.getStrongestIndirectPower(x, y, z)) * 10
		
		if (world.isRemote || world.totalWorldTime % delay != 0L) return
		
		if (tile.currentMana < COST_AVATAR) return
		
		var xl = x
		var yl = y
		var zl = z
		var s = tile.getBlockMetadata()
		
		when (s - 2) {
			0    -> zl -= 2
			2    -> xl -= 2
			3    -> xl += 2
			else -> zl += 2
		}
		
		val leftClick = wand.isLeftClick()
		
		val entities = world.getEntitiesWithinAABB(Entity::class.java, getBoundingBox(xl + 0.5, yl + 0.5, zl + 0.5).expand(0.5)) as MutableList<Entity>
		
		var block = world.getBlock(xl, yl, zl)
		if (block === Blocks.air) {
			yl -= 1
			s = 1
		} else {
			s = ForgeDirection.values()[s].opposite.ordinal
		}
		
		block = world.getBlock(xl, yl, zl)
		
		val player = getFake(world.provider.dimensionId)
		player.isSneaking = !world.isAirBlock(x, y + 1, z)
		player.setPositionAndRotation(tile.xCoord + 0.5, tile.yCoord + 0.5, tile.zCoord + 0.5, when (tile.blockMetadata) {
			2    -> 180f
			4    -> 90f
			5    -> -90f
			else -> 0f
		}, 0f)
		
		val inv = InventoryHelper.getInventory(world, x, y - 1, z)
		
		val unequip = equipPlayer(player, inv, delay)
		
		try {
			// code from Thaumic Tinkerer by Vazkii
			// TODO add events
			if (leftClick) run {
				if (entities.isEmpty()) return@run
				player.getAttributeMap().applyAttributeModifiers(player.heldItem.attributeModifiers)
				entities.firstOrNull {
					it !is EntityItem && !it.isDead && (it as? EntityLivingBase)?.isEntityAlive != false
				}?.let {
					player.attackTargetEntityWithCurrentItem(it)
					tile.recieveMana(-COST_AVATAR)
				}
			} else {
				ForgeEventFactory.onPlayerInteract(player, PlayerInteractEvent.Action.RIGHT_CLICK_AIR, xl, yl, zl, s, world)
				val entity = if (entities.isEmpty()) null else entities.random()
				
				var done = (entity != null && player.interactWith(entity))
				if (!done) done = player.heldItem?.item?.onItemUseFirst(player.heldItem, player, world, xl, yl, zl, s, 0f, 0f, 0f) == true
				if (!done) done = block.onBlockActivated(world, xl, yl, zl, player, s, 0f, 0f, 0f)
				if (!done) done = player.heldItem?.item?.onItemUse(player.heldItem, player, world, xl, yl, zl, s, 0f, 0f, 0f) == true
				if (!done) player.heldItem?.let {
					player.setCurrentItemOrArmor(0, it.item.onItemRightClick(player.heldItem, world, player))
					done = true
				}
				
				if (done) tile.recieveMana(-COST_AVATAR)
			}
		} catch (ignore: Throwable) {
			ignore.printStackTrace()
		}
		
		if (unequip) unequipPlayer(player, inv!!)
	}
	
	fun equipPlayer(player: FakePlayer, inv: IInventory?, ticksSkipped: Int): Boolean {
		if (inv == null) return false
		
		for (i in 0 until player.inventory.sizeInventory) {
			if (i >= inv.sizeInventory) break
			
			var stack = inv[i]?.copy()
			if (stack != null && inv is ISidedInventory && !inv.canExtractItem(i, stack, 1))
				if (stack.stackSize <= 0) stack = null
			inv[i] = null
			
			player.inventory[i] = stack
			stack?.let { for (t in 0 until ticksSkipped) it.item.onUpdate(stack, player.worldObj, player, i, i == 0) }
		}
		
		return true
	}
	
	fun unequipPlayer(player: FakePlayer, inv: IInventory) {
		for (i in 0 until player.inventory.sizeInventory) {
			var stack = player.inventory[i]?.copy()
			if (stack == null || stack.stackSize <= 0) stack = null
			
			player.inventory[i] = null
			if (i >= inv.sizeInventory) {
				if (stack != null) player.dropPlayerItemWithRandomChoice(stack, true)
				continue
			}
			
			inv[i] = stack
			player.inventory[i] = null
		}
	}
	
	override fun getOverlayResource(tile: IAvatarTile?, stack: ItemStack?) = LibResourceLocations.avatarClicker
	
	private fun ItemStack.isLeftClick() = getBoolean(this, TAG_MODE, false)
	
	private fun ItemStack.setLeftClick(left: Boolean) = setBoolean(this, TAG_MODE, left)
	
	companion object {
		
		const val TAG_MODE = "mode"
		const val COST_AVATAR = 10
		
		lateinit var iconLeft: IIcon
		
		init {
			this.eventForge()
		}
		
		fun getFake(dim: Int): FakePlayer {
			return FakePlayerFactory.get(MinecraftServer.getServer().worldServerForDimension(dim), GameProfile(UUID(dim.toLong(), dim.toLong()), "Avatar-Clicker_$dim"))
		}
		
		fun isFakeNotAvatar(player: EntityPlayer) = player.commandSenderName.startsWith("Avatar-Clicker_") || EntityFlugel.isTruePlayer(player)
	}
}
