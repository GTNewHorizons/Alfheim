package alfheim.common.block.magtrees.circuit

import alexsocol.asjlib.toItem
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.base.BlockLeavesMod
import alfheim.common.item.block.ItemBlockLeavesMod
import alfheim.common.lexicon.ShadowFoxLexiconData
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.*
import vazkii.botania.api.lexicon.ILexiconable
import java.util.*

class BlockCircuitLeaves: BlockLeavesMod(), ICircuitBlock, ILexiconable {
	
	init {
		setBlockName("circuitLeaves")
	}
	
	override fun isInterpolated() = true
	
	override fun register(name: String) {
		GameRegistry.registerBlock(this, ItemBlockLeavesMod::class.java, name)
	}
	
	override fun getItemDropped(meta: Int, random: Random, fortune: Int) = AlfheimBlocks.circuitSapling.toItem()
	
	override fun quantityDropped(random: Random) = if (random.nextInt(60) == 0) 1 else 0
	
	override fun func_150125_e() = arrayOf("circuitLeaves")
	
	override fun decayBit(): Int = 0x1
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = ShadowFoxLexiconData.circuitSapling
	
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