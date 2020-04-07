package alfheim.common.block.colored.rainbow

import alexsocol.asjlib.toBlock
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.base.*
import cpw.mods.fml.common.IFuelHandler
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import vazkii.botania.api.lexicon.ILexiconable

open class BlockRainbowWoodSlab(full: Boolean, source: Block = AlfheimBlocks.rainbowPlanks): BlockSlabMod(full, 0, source, source.unlocalizedName.replace("tile.".toRegex(), "") + "Slab" + (if (full) "Full" else "")), IFuelHandler {
	
	init {
		setResistance(10f)
		GameRegistry.registerFuelHandler(this)
	}
	
	override fun isToolEffective(type: String?, metadata: Int) = (type != null && type == "axe")
	
	override fun getHarvestTool(metadata: Int) = "axe"
	
	override fun getFullBlock() = AlfheimBlocks.rainbowSlabFull as BlockSlab
	
	override fun getSingleBlock() = AlfheimBlocks.rainbowSlab as BlockSlab
	
	override fun getEntry(world: World?, x: Int, y: Int, z: Int, player: EntityPlayer?, lexicon: ItemStack?) = (source as ILexiconable).getEntry(world, x, y, z, player, lexicon)!!
	
	override fun getBurnTime(fuel: ItemStack) = if (fuel.item.toBlock() === this) if (field_150004_a) 300 else 150 else 0
}

open class BlockRainbowWoodStairs(source: Block = AlfheimBlocks.rainbowPlanks):
	BlockStairsMod(source, 0, source.unlocalizedName.replace("tile.".toRegex(), "") + "Stairs"), ILexiconable {
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = (source as ILexiconable).getEntry(p0, p1, p2, p3, p4, p5)!!
	
	override fun isToolEffective(type: String?, metadata: Int) = (type != null && type == "axe")
	
	override fun getHarvestTool(metadata: Int) = "axe"
}
