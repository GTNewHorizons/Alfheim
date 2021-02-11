package alfheim.common.lexicon

import alexsocol.asjlib.*
import cpw.mods.fml.relauncher.FMLLaunchHandler
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraft.util.ChunkCoordinates
import net.minecraft.world.World
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent
import java.util.*

class MultiblockComponentRainbow(relPos: ChunkCoordinates, default: Block, vararg blocks: Block): MultiblockComponent(relPos, default, -1) {
	
	private val blockpairs: MutableList<BlockPair> = ArrayList()
	val secondaryBlocks: Array<out Block> = blocks
	
	init {
		populatePairs(default, blockpairs)
		for (block in blocks) populatePairs(block, blockpairs)
	}
	
	private fun populatePairs(block: Block, pairs: MutableList<BlockPair>) {
		if (ASJUtilities.isServer) return
		val stacks = ArrayList<ItemStack>()
		val item = block.toItem()
		block.getSubBlocks(item, block.creativeTabToDisplayOn, stacks)
		
		for (stack in stacks)
			if (stack.item == item)
				pairs.add(BlockPair(block, stack.meta))
	}
	
	override fun getMeta() =
		blockpairs[(BotaniaAPI.internalHandler.worldElapsedTicks / 20 % blockpairs.size).I].meta
	
	override fun getBlock() =
		blockpairs[(BotaniaAPI.internalHandler.worldElapsedTicks / 20 % blockpairs.size).I].block
	
	override fun matches(world: World, x: Int, y: Int, z: Int) =
		blockpairs.any { world.getBlock(x, y, z) == it.block }
	
	override fun copy() =
		MultiblockComponentRainbow(relPos, block, *secondaryBlocks)
	
	private class BlockPair(val block: Block, val meta: Int)
}
