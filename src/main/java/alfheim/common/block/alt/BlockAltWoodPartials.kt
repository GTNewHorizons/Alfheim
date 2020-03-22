package alfheim.common.block.alt

import alfheim.api.lib.LibOreDict
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.base.*
import alfheim.common.core.util.*
import alfheim.common.item.block.*
import alfheim.common.lexicon.ShadowFoxLexiconData
import cpw.mods.fml.common.IFuelHandler
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.*
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.IIcon
import net.minecraft.world.World

class BlockAltWoodSlab(full: Boolean, source: Block = AlfheimBlocks.altPlanks):
	BlockSlabMod(full, 0, source, source.unlocalizedName.replace("tile.".toRegex(), "") + "Slab" + (if (full) "Full" else "")), IFuelHandler {
	
	init {
		GameRegistry.registerFuelHandler(this)
	}
	
	override fun getSubBlocks(item: Item?, tab: CreativeTabs?, list: MutableList<Any?>) {
		for (i in 0 until LibOreDict.ALT_TYPES.size - 1)
			list.add(ItemStack(item, 1, i))
	}
	
	override fun getFullBlock() = AlfheimBlocks.altSlabsFull as BlockSlab
	
	override fun getIcon(side: Int, meta: Int): IIcon {
		return source.getIcon(side, meta % 8)!!
	}
	
	override fun register() {
		GameRegistry.registerBlock(this, ItemMetaSlabMod::class.java, name)
	}
	
	override fun getSingleBlock() = AlfheimBlocks.altSlabs as BlockSlab
	
	override fun getBurnTime(fuel: ItemStack) = if (fuel.item === this.toItem()) if (fuel.meta == BlockAltLeaves.yggMeta) Int.MAX_VALUE / 8 else 150 else 0
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer?, lexicon: ItemStack?) = if (world.getBlockMetadata(x, y, z) == BlockAltLeaves.yggMeta) null else ShadowFoxLexiconData.irisSapling
}

open class BlockAltWoodStairs(meta: Int, source: Block = AlfheimBlocks.altPlanks):
	BlockStairsMod(source, meta, source.unlocalizedName.replace("tile.".toRegex(), "") + "Stairs" + meta), IFuelHandler {
	
	init {
		GameRegistry.registerFuelHandler(this)
	}
	
	override fun register() {
		GameRegistry.registerBlock(this, ItemBlockMod::class.java, name)
	}
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer?, lexicon: ItemStack?) = if (world.getBlockMetadata(x, y, z) == BlockAltLeaves.yggMeta) null else ShadowFoxLexiconData.irisSapling
	
	override fun getBurnTime(fuel: ItemStack) = if (fuel.item === this.toItem()) 300 else 0
}

class BlockYggStairs: BlockAltWoodStairs(BlockAltLeaves.yggMeta), IFuelHandler {
	
	init {
		setBlockUnbreakable()
	}
	
	override fun getEntry(world: World, x: Int, y: Int, z: Int, player: EntityPlayer?, lexicon: ItemStack?) = null
	
	override fun isToolEffective(type: String?, metadata: Int) = false
	
	override fun getHarvestTool(metadata: Int) = "Odin"
	
	override fun getBurnTime(fuel: ItemStack) = if (fuel.item === this.toItem()) Int.MAX_VALUE / 8 * 3 else 0
}