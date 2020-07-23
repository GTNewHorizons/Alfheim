package alexsocol.asjlib.extendables

import net.minecraft.block.material.*

// For inline purpose; first call methods from here, then from Material
class MaterialPublic(color: MapColor): Material(color) {
	
	var blocker = true
	var blocksLight = true
	var liquid = false
	var opaque = true
	var solid = true
	
	// BLOCKS WATER
	override fun blocksMovement() = blocker
	
	override fun getCanBlockGrass() = blocksLight
	
	override fun isLiquid() = liquid
	
	override fun isOpaque() = opaque
	
	/** Can be replaced -_-  */
	override fun isSolid() = solid
	
	public override fun setAdventureModeExempt() = super.setAdventureModeExempt()!!
	
	public override fun setBurning() = super.setBurning()!!
	
	public override fun setImmovableMobility() = super.setImmovableMobility()!!
	
	fun setLiquid(): MaterialPublic {
		liquid = true
		return this
	}
	
	fun setBlocksLight(): MaterialPublic {
		blocksLight = true
		return this
	}
	
	public override fun setNoPushMobility() = super.setNoPushMobility()!!
	
	/** Can be washed away  */
	fun setNotBlocker(): MaterialPublic {
		blocker = false
		return this
	}
	
	fun setNotOpaque(): MaterialPublic {
		opaque = false
		return this
	}
	
	fun setNotSolid(): MaterialPublic {
		solid = false
		return this
	}
	
	public override fun setRequiresTool() = super.setRequiresTool()!!
}
