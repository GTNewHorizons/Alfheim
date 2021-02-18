package alfheim.common.block.magtrees.lightning

import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.colored.rainbow.*
import alfheim.common.item.block.*
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.*

class BlockLightningWoodSlab(full: Boolean, source: Block = AlfheimBlocks.lightningPlanks): BlockRainbowWoodSlab(full, source) {
	
	override fun getFullBlock() = AlfheimBlocks.lightningSlabsFull as BlockSlab
	
	override fun register() {
		GameRegistry.registerBlock(this, ItemSlabMod::class.java, name)
	}
	
	override fun getSingleBlock() = AlfheimBlocks.lightningSlabs as BlockSlab
}

class BlockLightningWoodStairs(source: Block = AlfheimBlocks.lightningPlanks): BlockRainbowWoodStairs(source) {
	
	override fun register() {
		GameRegistry.registerBlock(this, ItemBlockLeavesMod::class.java, name)
	}
}
