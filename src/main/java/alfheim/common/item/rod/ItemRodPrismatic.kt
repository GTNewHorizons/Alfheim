package alfheim.common.item.rod

import alexsocol.asjlib.*
import alfheim.client.core.helper.IconHelper
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.tile.TileRainbowManaFlame
import alfheim.common.item.*
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.api.item.IPhantomInkable
import vazkii.botania.api.mana.*
import vazkii.botania.common.core.helper.ItemNBTHelper

class ItemRodPrismatic: ItemMod("rodRainbowLight"), IManaUsingItem, IPhantomInkable {
	
	init {
		maxStackSize = 1
	}
	
	lateinit var overlayIcon: IIcon
	
	override fun requiresMultipleRenderPasses() = true
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		itemIcon = IconHelper.forItem(reg, this)
		overlayIcon = IconHelper.forItem(reg, this, "Overlay")
	}
	
	@SideOnly(Side.CLIENT)
	override fun getIconFromDamageForRenderPass(meta: Int, pass: Int) =
		if (pass == 1) overlayIcon else super.getIconFromDamageForRenderPass(meta, pass)!!
	
	override fun getColorFromItemStack(par1ItemStack: ItemStack, pass: Int) =
		if (pass > 0) 0xFFFFFF else ItemIridescent.rainbowColor()
	
	override fun onItemUse(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		if (world.getBlock(x, y, z) == AlfheimBlocks.rainbowFlame) {
			world.setBlock(x, y, z, Blocks.air)
			world.playSoundEffect(x.D + 0.5, y.D + 0.5, z.D + 0.5, "random.fizz", 0.3F, Math.random().F * 0.4F + 0.8F)
			return true
		}
		val toPlace = ItemStack(AlfheimBlocks.rainbowFlame)
		if (ManaItemHandler.requestManaExactForTool(stack, player, COST, false)) {
			val dir = ForgeDirection.getOrientation(side)
			if (world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ).isAir(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ)) {
				toPlace.tryPlaceItemIntoWorld(player, world, x, y, z, side, hitX, hitY, hitZ)
				if (toPlace.stackSize == 0) {
					world.playSoundEffect(x.D + 0.5, y.D + 0.5, z.D + 0.5, "fire.ignite", 0.3F, Math.random().F * 0.4F + 0.8F)
					ManaItemHandler.requestManaExactForTool(stack, player, COST, true)
					val tile = world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ)
					if (tile is TileRainbowManaFlame) {
						tile.invisible = hasPhantomInk(stack)
					}
				}
				return true
			}
		}
		return false
	}
	
	fun addStringToTooltip(s: String, tooltip: MutableList<Any?>) {
		tooltip.add(s.replace("&".toRegex(), "\u00a7"))
	}
	
	override fun addInformation(par1ItemStack: ItemStack?, par2EntityPlayer: EntityPlayer?, par3List: MutableList<Any?>, fpar4: Boolean) {
		if (par1ItemStack == null) return
		if (hasPhantomInk(par1ItemStack))
			addStringToTooltip(StatCollector.translateToLocal("botaniamisc.hasPhantomInk"), par3List)
	}
	
	override fun usesMana(stack: ItemStack) = true
	
	override fun hasPhantomInk(stack: ItemStack) = ItemNBTHelper.getBoolean(stack, "invisible", false)
	
	override fun setPhantomInk(stack: ItemStack, ink: Boolean) {
		ItemNBTHelper.setBoolean(stack, "invisible", ink)
	}
	
	override fun isFull3D() = true
	
	companion object {
		
		val COST = 100
	}
}
