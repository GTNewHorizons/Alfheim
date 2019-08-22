package alfheim.common.block.magtrees.nether

import alfheim.common.block.ShadowFoxBlocks
import alfheim.common.block.colored.rainbow.*
import alfheim.common.item.block.*
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.*

class BlockNetherWoodSlab(full: Boolean, source: Block = ShadowFoxBlocks.netherPlanks): BlockRainbowWoodSlab(full, source) {
	
	override fun getFullBlock() = ShadowFoxBlocks.netherSlabsFull as BlockSlab
	
	override fun register() {
		GameRegistry.registerBlock(this, ItemSlabMod::class.java, name)
	}
	
	override fun getSingleBlock() = ShadowFoxBlocks.netherSlabs as BlockSlab
}

class BlockNetherWoodStairs(source: Block = ShadowFoxBlocks.netherPlanks): BlockRainbowWoodStairs(source) {
	override fun register() {
		GameRegistry.registerBlock(this, ItemBlockMod::class.java, name)
	}
}
