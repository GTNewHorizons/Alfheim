package alfheim.common.block.colored

import alfheim.client.core.helper.IconHelper
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.block.ItemIridescentGrassMod
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.block.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.passive.EntitySheep
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.world.*
import vazkii.botania.api.lexicon.ILexiconable
import java.awt.Color
import java.util.*

class BlockColoredGrass: BlockTallGrass(), ILexiconable {
	
	val TYPES: Int = 16
	
	init {
		setBlockName("irisGrass")
		setCreativeTab(AlfheimTab)
		setStepSound(Block.soundTypeGrass)
	}
	
	override fun func_149851_a(world: World, x: Int, y: Int, z: Int, remote: Boolean) = true
	
	override fun func_149853_b(world: World, random: Random, x: Int, y: Int, z: Int) {
		val l = world.getBlockMetadata(x, y, z)
		val b0 = l % 8
		
		if (AlfheimBlocks.irisTallGrass0.canPlaceBlockAt(world, x, y, z)) {
			if (l < 8) {
				world.setBlock(x, y, z, AlfheimBlocks.irisTallGrass0, b0, 2)
				world.setBlock(x, y + 1, z, AlfheimBlocks.irisTallGrass0, 8, 2)
			} else {
				world.setBlock(x, y, z, AlfheimBlocks.irisTallGrass1, b0, 2)
				world.setBlock(x, y + 1, z, AlfheimBlocks.irisTallGrass1, 8, 2)
			}
		}
	}
	
	internal fun register(name: String) {
		GameRegistry.registerBlock(this, ItemIridescentGrassMod::class.java, name)
	}
	
	override fun setBlockName(par1Str: String): Block {
		register(par1Str)
		return super.setBlockName(par1Str)
	}
	
	@SideOnly(Side.CLIENT)
	override fun getBlockColor() = 0xFFFFFF
	
	override fun onSheared(item: ItemStack, world: IBlockAccess, x: Int, y: Int, z: Int, fortune: Int): ArrayList<ItemStack> {
		val ret = ArrayList<ItemStack>()
		ret.add(ItemStack(this, 1, world.getBlockMetadata(x, y, z)))
		return ret
	}
	
	/**
	 * Returns the color this block should be rendered. Used by leaves.
	 */
	@SideOnly(Side.CLIENT)
	override fun getRenderColor(meta: Int): Int {
		if (meta >= EntitySheep.fleeceColorTable.size)
			return 0xFFFFFF
		
		val color = EntitySheep.fleeceColorTable[meta]
		return Color(color[0], color[1], color[2]).rgb
	}
	
	@SideOnly(Side.CLIENT)
	override fun colorMultiplier(access: IBlockAccess?, x: Int, y: Int, z: Int): Int {
		val meta = access!!.getBlockMetadata(x, y, z)
		return getRenderColor(meta)
	}
	
	override fun getSubBlocks(item: Item?, tab: CreativeTabs?, list: MutableList<Any?>?) {
		if (list != null && item != null)
			for (i in 0 until TYPES) {
				list.add(ItemStack(item, 1, i))
			}
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerBlockIcons(iconRegister: IIconRegister) {
		blockIcon = IconHelper.forBlock(iconRegister, this)
	}
	
	@SideOnly(Side.CLIENT)
	override fun getIcon(side: Int, meta: Int) = blockIcon!!
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) = AlfheimLexiconData.pastoralSeeds
}
