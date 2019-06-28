package alexsocol.asjlib.extendables

import net.minecraft.block.material.*

// For inline purpose; first call methods from here, then from Material
class MaterialPublic(color: MapColor): Material(color) {
	
	var blocker = true
	var grass = true
	var liquid = false
	var opaque = true
	var solid = true
	
	// BLOCKS WATER
	override fun blocksMovement(): Boolean {
		return blocker
	}
	
	override fun getCanBlockGrass(): Boolean {
		return grass
	}
	
	override fun isLiquid(): Boolean {
		return liquid
	}
	
	override fun isOpaque(): Boolean {
		return opaque
	}
	
	/** Can be replaced -_-  */
	override fun isSolid(): Boolean {
		return solid
	}
	
	public override fun setAdventureModeExempt(): Material {
		return super.setAdventureModeExempt()
	}
	
	public override fun setBurning(): Material {
		return super.setBurning()
	}
	
	public override fun setImmovableMobility(): Material {
		return super.setImmovableMobility()
	}
	
	fun setLiquid(): MaterialPublic {
		liquid = true
		return this
	}
	
	fun setGrass(): MaterialPublic {
		grass = true
		return this
	}
	
	public override fun setNoPushMobility(): Material {
		return super.setNoPushMobility()
	}
	
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
	
	public override fun setRequiresTool(): Material {
		return super.setRequiresTool()
	}
}
