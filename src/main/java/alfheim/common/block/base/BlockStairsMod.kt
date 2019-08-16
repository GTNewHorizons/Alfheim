package alfheim.common.block.base

import alfheim.AlfheimCore
import alfheim.common.item.block.ItemIridescentBlockMod
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.*
import net.minecraft.world.World
import vazkii.botania.api.lexicon.ILexiconable

abstract class BlockStairsMod(val source: Block, val meta: Int, val name: String): BlockStairs(source, meta), ILexiconable {
	
	init {
		setCreativeTab(AlfheimCore.baTab)
		useNeighborBrightness = true
		setStepSound(source.stepSound)
		setBlockName(name)
	}
	
	override fun getBlockHardness(world: World?, x: Int, y: Int, z: Int) =
		source.getBlockHardness(world, x, y, z)
	
	override fun setBlockName(par1Str: String): Block {
		register()
		return super.setBlockName(name)
	}
	
	open fun register() {
		GameRegistry.registerBlock(this, ItemIridescentBlockMod::class.java, name)
	}
}
