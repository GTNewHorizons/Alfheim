package alfheim.common.block.base

import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.block.ItemIridescentBlockMod
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import vazkii.botania.api.lexicon.*

open class BlockStairsMod(val source: Block, val meta: Int, val name: String): BlockStairs(source, meta), ILexiconable {
	
	init {
		setCreativeTab(AlfheimTab)
		setBlockName(name)
		setStepSound(source.stepSound)
		useNeighborBrightness = true
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
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?): LexiconEntry? = null
}
