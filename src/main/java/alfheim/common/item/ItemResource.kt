package alfheim.common.item

import alfheim.common.block.ShadowFoxBlocks
import alfheim.common.block.colored.rainbow.BlockRainbowGrass
import alfheim.common.core.helper.*
import cpw.mods.fml.common.IFuelHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.*
import net.minecraft.util.IIcon
import net.minecraft.world.World
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge
import vazkii.botania.api.recipe.IFlowerComponent
import vazkii.botania.common.Botania
import vazkii.botania.common.block.ModBlocks
import java.awt.Color
import kotlin.math.min

class ItemResource: ItemMod("resource"), IFlowerComponent, IFuelHandler {
	
	init {
		setHasSubtypes(true)
		if (FMLLaunchHandler.side().isClient)
			MinecraftForge.EVENT_BUS.register(this)
		GameRegistry.registerFuelHandler(this)
	}
	
	val TYPES = 8
	
	var icons: Array<IIcon?> = arrayOfNulls(TYPES)
	
	private fun isInterpolated(meta: Int) = meta == 0 || meta == 4 || meta == 5
	
	private fun isFlowerComponent(meta: Int) = meta == 4 || meta == 7
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		for (i in 0 until TYPES) {
			if (!isInterpolated(i)) {
				icons[i] = IconHelper.forItem(reg, this, i)
			}
		}
	}
	
	override fun getColorFromItemStack(stack: ItemStack, pass: Int) =
		if (stack.itemDamage == 6 || stack.itemDamage == 7)
            ItemIridescent.rainbowColor()
		else
			super.getColorFromItemStack(stack, pass)
	
	override fun canFit(stack: ItemStack, inventory: IInventory) = isFlowerComponent(stack.itemDamage)
	
	override fun getParticleColor(stack: ItemStack): Int {
		return when (stack.itemDamage) {
			4    -> 0x6B2406
			7    -> ItemIridescent.rainbowColor()
			else -> 0xFFFFFF
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun loadTextures(event: TextureStitchEvent.Pre) {
		if (event.map.textureType == 1) {
			for (i in 0 until TYPES) {
				if (isInterpolated(i)) {
					icons[i] = InterpolatedIconHelper.forItem(event.map, this, i)
				}
			}
		}
	}
	
	override fun onItemUse(stack: ItemStack, player: EntityPlayer?, world: World, x: Int, y: Int, z: Int, side: Int, par8: Float, par9: Float, par10: Float): Boolean {
		val block = world.getBlock(x, y, z)
		if (block == ModBlocks.pool && world.getBlockMetadata(x, y, z) == 0 && stack.itemDamage == 6) {
			world.setBlockMetadataWithNotify(x, y, z, 3, 2)
			stack.stackSize--
			return true
		} else if (block == ModBlocks.flower && stack.itemDamage == 6) {
			world.setBlock(x, y, z, ShadowFoxBlocks.rainbowGrass, BlockRainbowGrass.FLOWER, 3)
			for (i in 0..40) {
				val color = Color.getHSBColor(Math.random().toFloat() + 1f / 2f, 1f, 1f)
				Botania.proxy.wispFX(world,
									 x.toDouble() + Math.random(), y.toDouble() + Math.random(), z.toDouble() + Math.random(),
									 color.red / 255f, color.green / 255f, color.blue / 255f,
									 0.5f, 0f, 0.125f, 0f)
			}
			world.playSoundEffect(x.toDouble(), y.toDouble(), z.toDouble(), "botania:enchanterEnchant", 1f, 1f)
			stack.stackSize--
			return true
		} else if (side == 1 && ShadowFoxBlocks.rainbowGrass.canBlockStay(world, x, y + 1, z)) {
			world.setBlock(x, y + 1, z, ShadowFoxBlocks.rainbowGrass, BlockRainbowGrass.BURIED, 3)
			stack.stackSize--
			return true
		}
		return false
	}
	
	override fun getSubItems(item: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
		for (i in 0 until TYPES)
			list.add(ItemStack(item, 1, i))
	}
	
	override fun getUnlocalizedName(par1ItemStack: ItemStack) =
        super.getUnlocalizedName(par1ItemStack) + par1ItemStack.itemDamage
	
	override fun getIconFromDamage(dmg: Int) = icons[min(TYPES - 1, dmg)]
	
	override fun getBurnTime(fuel: ItemStack): Int {
		if (fuel.item == ShadowFoxItems.resource) {
			when (fuel.itemDamage) {
				1, 3 -> return 100 // Splinters smelt half an item.
				4    -> return 2400 // Flame-Laced Coal smelts 12 items.
			}
		}
		return 0
	}
}
