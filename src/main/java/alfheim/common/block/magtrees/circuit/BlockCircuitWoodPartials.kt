package alfheim.common.block.magtrees.circuit

import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.colored.rainbow.*
import alfheim.common.item.block.*
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.*
import net.minecraft.world.*
import java.util.*

class BlockCircuitWoodSlab(full: Boolean, source: Block = AlfheimBlocks.circuitPlanks): BlockRainbowWoodSlab(full, source) {
	
	override fun getFullBlock() = AlfheimBlocks.circuitSlabsFull as BlockSlab
	
	override fun register() {
		GameRegistry.registerBlock(this, ItemSlabMod::class.java, name)
	}
	
	override fun getSingleBlock() = AlfheimBlocks.circuitSlabs as BlockSlab
	
	// ####
	
	override fun updateTick(world: World, x: Int, y: Int, z: Int, random: Random) {
		super.updateTick(world, x, y, z, random)
		world.notifyBlocksOfNeighborChange(x, y, z, this)
	}
	
	override fun getLightValue(world: IBlockAccess?, x: Int, y: Int, z: Int) = 8
	
	override fun canProvidePower() = true
	
	override fun tickRate(world: World) = 1
	
	override fun isProvidingWeakPower(blockAccess: IBlockAccess, x: Int, y: Int, z: Int, meta: Int) = ICircuitBlock.getPower(blockAccess, x, y, z)
}

class BlockCircuitWoodStairs(source: Block = AlfheimBlocks.circuitPlanks): BlockRainbowWoodStairs(source) {
	
	override fun register() {
		GameRegistry.registerBlock(this, ItemBlockLeavesMod::class.java, name)
	}
	
	// ####
	
	override fun updateTick(world: World, x: Int, y: Int, z: Int, random: Random) {
		super.updateTick(world, x, y, z, random)
		world.notifyBlocksOfNeighborChange(x, y, z, this)
	}
	
	override fun getLightValue(world: IBlockAccess?, x: Int, y: Int, z: Int) = 8
	
	override fun canProvidePower() = true
	
	override fun tickRate(world: World) = 1
	
	override fun isProvidingWeakPower(blockAccess: IBlockAccess, x: Int, y: Int, z: Int, meta: Int) = ICircuitBlock.getPower(blockAccess, x, y, z)
}