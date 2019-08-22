package alfheim.common.block.magtrees.circuit

import alfheim.common.block.ShadowFoxBlocks
import alfheim.common.block.colored.rainbow.*
import alfheim.common.item.block.*
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.*

class BlockCircuitWoodSlab(full: Boolean, source: Block = ShadowFoxBlocks.circuitPlanks): BlockRainbowWoodSlab(full, source) {
	
	override fun getFullBlock() = ShadowFoxBlocks.circuitSlabsFull as BlockSlab
	
	override fun register() {
		GameRegistry.registerBlock(this, ItemSlabMod::class.java, name)
	}
	
	override fun getSingleBlock() = ShadowFoxBlocks.circuitSlabs as BlockSlab
}

class BlockCircuitWoodStairs(source: Block = ShadowFoxBlocks.circuitPlanks): BlockRainbowWoodStairs(source) {
	override fun register() {
		GameRegistry.registerBlock(this, ItemBlockMod::class.java, name)
	}
}