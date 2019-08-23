package alfheim.common.item.rod

import alfheim.api.ModInfo
import alfheim.common.item.*
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.api.mana.*
import vazkii.botania.common.Botania
import java.awt.Color
import kotlin.math.*

class ItemRodElemental(name: String, private val barrier: Block): ItemMod(name), IManaUsingItem {
	private var rubyIcon: IIcon? = null
	private var sapphireIcon: IIcon? = null
	
	init {
		setFull3D()
		maxDamage = 1200
		setMaxStackSize(1)
	}

	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		itemIcon = reg.registerIcon(ModInfo.MODID + ':'.toString() + this.unlocalizedName.substring(5))
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
		if (stack.itemDamage > 0) return stack
		if (!world.isRemote) {
			var cd = false
			for (x in -6..6)
				for (z in -6..6)
					for (y in -2..2)
						if (3 < sqrt(x.toDouble().pow(2.0) + z.toDouble().pow(2.0)) && sqrt(x.toDouble().pow(2.0) + z.toDouble().pow(2.0)) < 6) {
							val X = MathHelper.floor_double(player.posX) + x
							val Y = MathHelper.floor_double(player.posY) + y
							val Z = MathHelper.floor_double(player.posZ) + z
							val c = Color(if (this === AlfheimItems.rodFire) 0x880000 else 0x0055AA)
							if (world.isAirBlock(X, Y, Z) && barrier.canPlaceBlockAt(world, X, Y, Z)) {
								cd = cd or place(stack, player, world, X, Y, Z, 0, 0.5f, 0.5f, 0.5f, barrier, if (player.capabilities.isCreativeMode) 0 else 150, c.red.toFloat(), c.green.toFloat(), c.blue.toFloat())
							}
						}
			if (cd) stack.itemDamage = this.maxDamage
		}
		return stack
	}
	
	override fun onUpdate(stack: ItemStack, world: World?, entity: Entity?, slotID: Int, inHand: Boolean) {
		if (stack.itemDamage > 0) stack.itemDamage = stack.itemDamage - 1
	}
	
	override fun usesMana(stack: ItemStack): Boolean {
		return true
	}
	
	companion object {
		
		// Modified code from ItemDirtRod
		fun place(par1ItemStack: ItemStack, par2EntityPlayer: EntityPlayer, par3World: World, par4: Int, par5: Int, par6: Int, par7: Int, par8: Float, par9: Float, par10: Float, block: Block, cost: Int, r: Float, g: Float, b: Float): Boolean {
			if (ManaItemHandler.requestManaExactForTool(par1ItemStack, par2EntityPlayer, cost, false)) {
				val dir = ForgeDirection.getOrientation(par7)
				
				val stackToPlace = ItemStack(block)
				stackToPlace.tryPlaceItemIntoWorld(par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10)
				
				if (stackToPlace.stackSize == 0) {
					ManaItemHandler.requestManaExactForTool(par1ItemStack, par2EntityPlayer, cost, true)
					for (i in 0..5) Botania.proxy.sparkleFX(par3World, par4.toDouble() + dir.offsetX.toDouble() + Math.random(), par5.toDouble() + dir.offsetY.toDouble() + Math.random(), par6.toDouble() + dir.offsetZ.toDouble() + Math.random(), r, g, b, 1f, 5)
					return true
				}
			}
			return false
		}
	}
}
