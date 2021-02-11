package alfheim.api.trees

import net.minecraft.block.Block
import java.util.*

class IridescentSaplingBaseVariant(var soil: Block, var wood: Block, var leaves: Block): IIridescentSaplingVariant {
	
	var metaMin = 0
	var metaMax = 15
	var metaShift = 0
	
	override val acceptableSoils: List<Block>
		get() {
			val soils = ArrayList<Block>()
			soils.add(soil)
			return soils
		}
	
	constructor(soil: Block, wood: Block, leaves: Block, metaMin: Int, metaMax: Int): this(soil, wood, leaves) {
		this.metaMin = metaMin
		this.metaMax = metaMax
	}
	
	constructor(soil: Block, wood: Block, leaves: Block, metaMin: Int, metaMax: Int, metaShift: Int): this(soil, wood, leaves, metaMin, metaMax) {
		this.metaShift = metaShift
	}
	
	constructor(soil: Block, wood: Block, leaves: Block, meta: Int): this(soil, wood, leaves, meta, meta)
	
	override fun matchesSoil(soil: Block, meta: Int) =
		soil === this.soil && meta <= metaMax && meta >= metaMin
	
	override fun getMeta(soil: Block, meta: Int, toPlace: Block): Int {
		if (toPlace === wood) {
			return meta and 3
		} else if (toPlace === leaves) {
			return meta - metaShift
		}
		return 0
	}
	
	override fun getLeaves(soil: Block, meta: Int) = leaves
	
	override fun getWood(soil: Block, meta: Int) = wood
}
