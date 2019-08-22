package alfheim.api.trees

import net.minecraft.block.Block

interface IIridescentSaplingVariant {
	
	val acceptableSoils: List<Block>
	fun getMeta(soil: Block, meta: Int, toPlace: Block): Int
	
	fun matchesSoil(soil: Block, meta: Int): Boolean
	
	fun getLeaves(soil: Block, meta: Int): Block
	
	fun getWood(soil: Block, meta: Int): Block
}
