package alfheim.common.block.alt

import alfheim.common.block.ShadowFoxBlocks
import alfheim.common.block.base.*
import alfheim.common.item.block.*
import alfheim.common.lexicon.ShadowFoxLexiconData
import cpw.mods.fml.common.IFuelHandler
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.world.World
import vazkii.botania.api.lexicon.ILexiconable

class BlockAltWoodSlab(full: Boolean, meta: Int, source: Block = ShadowFoxBlocks.altPlanks):
	BlockSlabMod(full, meta, source, source.unlocalizedName.replace("tile.".toRegex(), "") + "Slab" + (if (full) "Full" else "") + meta), ILexiconable, IFuelHandler {
	
	init {
		GameRegistry.registerFuelHandler(this)
	}
	
	override fun getFullBlock() = ShadowFoxBlocks.altSlabsFull[meta] as BlockSlab
	
	override fun getIcon(par1: Int, par2: Int) = source.getIcon(par1, meta)!!
	
	override fun register() {
		GameRegistry.registerBlock(this, ItemRegularSlabMod::class.java, name)
	}
	
	override fun getSingleBlock() = ShadowFoxBlocks.altSlabs[meta] as BlockSlab
	
	override fun getBurnTime(fuel: ItemStack) = if (fuel.item == Item.getItemFromBlock(this)) 150 else 0
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = ShadowFoxLexiconData.irisSapling
	
	override fun isToolEffective(type: String?, metadata: Int) = (type != null && type == "axe")
	
	override fun getHarvestTool(metadata: Int) = "axe"
}

class BlockAltWoodStairs(meta: Int, source: Block = ShadowFoxBlocks.altPlanks):
	BlockStairsMod(source, meta, source.unlocalizedName.replace("tile.".toRegex(), "") + "Stairs" + meta) {
	
	override fun register() {
		GameRegistry.registerBlock(this, ItemBlockMod::class.java, name)
	}
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = ShadowFoxLexiconData.irisSapling
	
	override fun isToolEffective(type: String?, metadata: Int) = (type != null && type == "axe")
	
	override fun getHarvestTool(metadata: Int) = "axe"
}
