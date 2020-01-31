package alfheim.common.block.magtrees.nether

import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.colored.rainbow.*
import alfheim.common.item.block.*
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.*

class BlockNetherWoodSlab(full: Boolean, source: Block = AlfheimBlocks.netherPlanks): BlockRainbowWoodSlab(full, source) {
	
	override fun getFullBlock() = AlfheimBlocks.netherSlabsFull as BlockSlab
	
	override fun register() {
		GameRegistry.registerBlock(this, ItemSlabMod::class.java, name)
	}
	
	override fun getSingleBlock() = AlfheimBlocks.netherSlabs as BlockSlab
}

class BlockNetherWoodStairs(source: Block = AlfheimBlocks.netherPlanks): BlockRainbowWoodStairs(source) {
	override fun register() {
		GameRegistry.registerBlock(this, ItemBlockMod::class.java, name)
	}
}
