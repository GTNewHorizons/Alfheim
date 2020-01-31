package alfheim.common.block.base

import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.block.ItemColoredSlabMod
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.item.*
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World
import vazkii.botania.api.lexicon.ILexiconable
import java.util.*

abstract class BlockSlabMod(val full: Boolean, val meta: Int, val source: Block, val name: String):
	BlockSlab(full, source.material), ILexiconable {
	
	init {
		setBlockName(name)
		setCreativeTab(AlfheimTab)
		setStepSound(source.stepSound)
		if (!full) {
			useNeighborBrightness = true
		}
	}
	
	override fun getBlockHardness(world: World?, x: Int, y: Int, z: Int) =
		source.getBlockHardness(world, x, y, z)
	
	@SideOnly(Side.CLIENT)
	override fun getIcon(par1: Int, par2: Int) = source.getIcon(par1, par2)!!
	
	override fun getPickBlock(target: MovingObjectPosition?, world: World, x: Int, y: Int, z: Int) = ItemStack(getSingleBlock())
	
	override fun getItemDropped(meta: Int, random: Random?, fortune: Int) = Item.getItemFromBlock(getSingleBlock())!!
	
	public override fun createStackedBlock(par1: Int) = ItemStack(getSingleBlock())
	
	@SideOnly(Side.CLIENT)
	override fun registerBlockIcons(par1IconRegister: IIconRegister) = Unit
	
	open fun register() {
		GameRegistry.registerBlock(this, ItemColoredSlabMod::class.java, name)
	}
	
	override fun func_150002_b(i: Int) = name
	
	abstract fun getFullBlock(): BlockSlab
	
	abstract fun getSingleBlock(): BlockSlab
}
