package alfheim.common.block.magtrees.lightning

import alfheim.common.block.ShadowFoxBlocks
import alfheim.common.block.colored.rainbow.*
import alfheim.common.item.block.*
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.*

class BlockLightningWoodSlab(full: Boolean, source: Block = ShadowFoxBlocks.lightningPlanks): BlockRainbowWoodSlab(full, source) {
    
	override fun getFullBlock() = ShadowFoxBlocks.lightningSlabsFull as BlockSlab
	
	override fun register() {
		GameRegistry.registerBlock(this, ItemSlabMod::class.java, name)
	}
	
	override fun getSingleBlock() = ShadowFoxBlocks.lightningSlabs as BlockSlab
}

class BlockLightningWoodStairs(source: Block = ShadowFoxBlocks.lightningPlanks): BlockRainbowWoodStairs(source) {
	override fun register() {
		GameRegistry.registerBlock(this, ItemBlockMod::class.java, name)
	}
}
