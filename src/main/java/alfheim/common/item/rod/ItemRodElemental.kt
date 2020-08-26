package alfheim.common.item.rod

import alexsocol.asjlib.*
import alfheim.api.ModInfo
import alfheim.common.item.*
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.api.mana.*
import vazkii.botania.common.Botania
import java.awt.Color
import kotlin.math.*

class ItemRodElemental(name: String, private val barrier: () -> Block): ItemMod(name), IManaUsingItem {
	
	private var rubyIcon: IIcon? = null
	private var sapphireIcon: IIcon? = null
	
	init {
		setFull3D()
		maxDamage = 1200
		maxStackSize = 1
	}

	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		itemIcon = reg.registerIcon(ModInfo.MODID + ':'.toString() + unlocalizedName.substring(5))
		rubyIcon = reg.registerIcon(ModInfo.MODID + ":RubyRod")
		sapphireIcon = reg.registerIcon(ModInfo.MODID + ":SapphireRod")
	}
	
	override fun getIconIndex(par1ItemStack: ItemStack): IIcon? {
		val name = par1ItemStack.displayName.toLowerCase().trim { it <= ' ' }
		return if (name == "magical ruby" && this === AlfheimItems.rodFire) rubyIcon else if (name == "magical sapphire" && this === AlfheimItems.rodIce) sapphireIcon else super.getIconIndex(par1ItemStack)
	}
	
	override fun getIcon(stack: ItemStack, pass: Int): IIcon? {
		return getIconIndex(stack)
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		if (stack.meta > 0) return stack
		if (!world.isRemote) {
			var cd = false
			for (x in -6..6)
				for (z in -6..6)
					for (y in -2..2)
						if (3 < sqrt(x.D.pow(2.0) + z.D.pow(2.0)) && sqrt(x.D.pow(2.0) + z.D.pow(2.0)) < 6) {
							val i = player.posX.mfloor() + x
							val j = player.posY.mfloor() + y
							val k = player.posZ.mfloor() + z
							val c = Color(if (this === AlfheimItems.rodFire) 0x880000 else 0x0055AA)
							if (world.isAirBlock(i, j, k) && barrier().canPlaceBlockAt(world, i, j, k)) {
								cd = cd or place(stack, player, world, i, j, k, 0, 0.5f, 0.5f, 0.5f, barrier(), if (player.capabilities.isCreativeMode) 0 else 150, c.red.F, c.green.F, c.blue.F)
							}
						}
			if (cd)
				stack.meta = getMaxDamage(stack)
		}
		return stack
	}
	
	override fun onUpdate(stack: ItemStack, world: World?, entity: Entity?, slotID: Int, inHand: Boolean) {
		if (stack.meta > 0) stack.meta = stack.meta - 1
	}
	
	override fun usesMana(stack: ItemStack): Boolean {
		return true
	}
	
	companion object {
		
		// Modified code from ItemDirtRod
		fun place(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float, block: Block, cost: Int, r: Float, g: Float, b: Float): Boolean {
			if (ManaItemHandler.requestManaExactForTool(stack, player, cost, false)) {
				val dir = ForgeDirection.getOrientation(side)
				
				val stackToPlace = ItemStack(block)
				stackToPlace.tryPlaceItemIntoWorld(player, world, x, y, z, side, hitX, hitY, hitZ)
				
				if (stackToPlace.stackSize == 0) {
					ManaItemHandler.requestManaExactForTool(stack, player, cost, true)
					for (i in 0..5) Botania.proxy.sparkleFX(world, x.D + dir.offsetX.D + Math.random(), y.D + dir.offsetY.D + Math.random(), z.D + dir.offsetZ.D + Math.random(), r, g, b, 1f, 5)
					return true
				}
			}
			return false
		}
	}
}
