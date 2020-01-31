package alfheim.common.block.magtrees.calico

import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.colored.rainbow.*
import alfheim.common.item.block.*
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.*

class BlockCalicoWoodSlab(full: Boolean, source: Block = AlfheimBlocks.calicoPlanks): BlockRainbowWoodSlab(full, source), IExplosionDampener {
	
	override fun getFullBlock() = AlfheimBlocks.calicoSlabsFull as BlockSlab
	
	override fun register() {
		GameRegistry.registerBlock(this, ItemSlabMod::class.java, name)
	}
	
	override fun getSingleBlock() = AlfheimBlocks.calicoSlabs as BlockSlab
}

class BlockCalicoWoodStairs(source: Block = AlfheimBlocks.calicoPlanks): BlockRainbowWoodStairs(source), IExplosionDampener {
	override fun register() {
		GameRegistry.registerBlock(this, ItemBlockMod::class.java, name)
	}
}