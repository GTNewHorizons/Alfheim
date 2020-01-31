package alfheim.common.item.block

import alfheim.api.ModInfo
import alfheim.common.block.base.IDoublePlant
import alfheim.common.block.colored.rainbow.BlockRainbowGrass
import alfheim.common.core.helper.IconHelper
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.passive.EntitySheep
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.*
import java.awt.Color
import kotlin.properties.Delegates

class ItemIridescentGrassMod(par2Block: Block): ItemSubtypedBlockMod(par2Block) {
	override fun getColorFromItemStack(par1ItemStack: ItemStack, pass: Int): Int {
		if (par1ItemStack.itemDamage >= EntitySheep.fleeceColorTable.size)
			return 0xFFFFFF
		
		val color = EntitySheep.fleeceColorTable[par1ItemStack.itemDamage]
		return Color(color[0], color[1], color[2]).rgb
	}
}

open class ItemIridescentTallGrassMod0(par2Block: Block): ItemSubtypedBlockMod(par2Block) {
	
	open val colorSet = 0
	var topIcon: IIcon by Delegates.notNull()
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(par1IconRegister: IIconRegister) {
		topIcon = IconHelper.forName(par1IconRegister, "irisDoubleGrassTop")
	}
	
	override fun getIcon(stack: ItemStack, pass: Int) = topIcon
	
	override fun getColorFromItemStack(par1ItemStack: ItemStack, pass: Int): Int {
		if (par1ItemStack.itemDamage >= EntitySheep.fleeceColorTable.size)
			return 0xFFFFFF
		
		val color = EntitySheep.fleeceColorTable[par1ItemStack.itemDamage + colorSet * 8]
		return Color(color[0], color[1], color[2]).rgb
	}
	
	override fun addInformation(par1ItemStack: ItemStack?, par2EntityPlayer: EntityPlayer?, par3List: MutableList<Any?>?, par4: Boolean) {
		if (par1ItemStack == null) return
		addStringToTooltip("&7" + StatCollector.translateToLocal("misc.${ModInfo.MODID}.color." + (par1ItemStack.itemDamage + (colorSet * 8))) + "&r", par3List)
	}
}

open class ItemRainbowGrassMod(var par2Block: Block): ItemBlockWithMetadata(par2Block, par2Block) {
	
	override fun getUnlocalizedNameInefficiently(par1ItemStack: ItemStack) =
		super.getUnlocalizedNameInefficiently(par1ItemStack).replace("tile.", "tile.${ModInfo.MODID}:") + par1ItemStack.itemDamage
	
	fun addStringToTooltip(s: String, tooltip: MutableList<Any?>?) {
		tooltip!!.add(s.replace("&".toRegex(), "\u00a7"))
	}
	
	override fun addInformation(par1ItemStack: ItemStack?, par2EntityPlayer: EntityPlayer?, par3List: MutableList<Any?>?, par4: Boolean) {
		if (par1ItemStack == null) return
		val meta = par1ItemStack.itemDamage
		if (meta == BlockRainbowGrass.GRASS || meta == BlockRainbowGrass.AURORA)
			addStringToTooltip("&7" + StatCollector.translateToLocal("misc.${ModInfo.MODID}.color.${meta + 16}") + "&r", par3List)
	}
}

open class ItemRainbowDoubleGrassMod(par2Block: Block): ItemRainbowGrassMod(par2Block) {
	
	override fun getIcon(stack: ItemStack, pass: Int) = (par2Block as IDoublePlant).getTopIcon(stack.itemDamage)
}

open class ItemIridescentTallGrassMod1(par2Block: Block): ItemIridescentTallGrassMod0(par2Block) {
	override val colorSet = 1
}
